package com.daeliin.components.core.resource.repository;

import com.daeliin.components.domain.pagination.Page;
import com.daeliin.components.domain.pagination.PageRequest;
import com.daeliin.components.domain.resource.Conversion;
import com.daeliin.components.domain.resource.Persistable;
import com.daeliin.components.domain.resource.PersistentResource;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.RelationalPathBase;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLUpdateClause;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Transactional
public abstract class ResourceRepository<E extends PersistentResource, B> implements PagingRepository<E> {

    @Inject
    protected SQLQueryFactory queryFactory;

    protected final Conversion<E, B> conversion;
    protected final RelationalPathBase<B> rowPath;
    protected final StringPath idPath;

    protected ResourceRepository(Conversion<E, B> conversion, RelationalPathBase<B> rowPath, StringPath idPath) {
        this.conversion = conversion;
        this.rowPath = rowPath;
        this.idPath = idPath;
    }

    @Override
    public E save(E resource) {
        if (resource == null) {
            throw new IllegalArgumentException("Cannot create null resource");
        }

        if (exists(resource.uuid())) {
            queryFactory.update(rowPath)
                    .where(idPath.eq(resource.uuid()))
                    .populate(conversion.map(resource))
                    .execute();
        } else {
            queryFactory.insert(rowPath)
                    .populate(conversion.map(resource))
                    .execute();
        }

        return resource;
    }

    @Override
    public Collection<E> save(Collection<E> resources) {
        Collection<String> resourceIds = resources.stream().map(Persistable::uuid).collect(Collectors.toList());
        Collection<String> persistedResourceIds = findAllIds(resources);
        boolean insertBatchShouldBeExecuted = resourceIds.size() > persistedResourceIds.size();
        boolean updateBatchShouldBeExecuted = persistedResourceIds.size() > 0;

        SQLInsertClause insertBatch = queryFactory.insert(rowPath);
        SQLUpdateClause updateBatch = queryFactory.update(rowPath);

        resources.forEach(resource -> {
            if (persistedResourceIds.contains(resource.uuid())) {
                updateBatch.populate(conversion.map(resource)).addBatch();
            } else {
                insertBatch.populate(conversion.map(resource)).addBatch();
            }
        });

        if (insertBatchShouldBeExecuted) {
            insertBatch.execute();
        }

        if (updateBatchShouldBeExecuted) {
            updateBatch.execute();
        }

        return resources;
    }

    @Transactional(readOnly = true)
    @Override
    public E findOne(String resourceId) {
        if (resourceId == null) {
            return null;
        }

        B persistedResource = queryFactory.select(rowPath)
                .from(rowPath)
                .where(idPath.eq(resourceId))
                .fetchOne();

        return conversion.instantiate(persistedResource);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<E> findAll(Collection<String> resourceIds) {
        return queryFactory.select(rowPath)
                .from(rowPath)
                .where(idPath.in(resourceIds))
                .fetch()
                .stream()
                .map(conversion::instantiate)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<E> findAll(PageRequest pageRequest) {
        long totalItems = count();
        long totalPages = Double.valueOf(Math.ceil(totalItems / pageRequest.size)).intValue();
        OrderSpecifier[] orders = computeOrders(pageRequest);

        List<E> items = queryFactory.select(rowPath)
                .from(rowPath)
                .limit(pageRequest.size)
                .offset(pageRequest.offset)
                .orderBy(orders)
                .fetch()
                .stream()
                .map(conversion::instantiate)
                .collect(toList());

        return new Page(items, totalItems, totalPages);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<E> findAll() {
        return queryFactory.select(rowPath)
                .from(rowPath)
                .fetch()
                .stream()
                .map(conversion::instantiate)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    @Override
    public boolean exists(String resourceId) {
        if (resourceId == null) {
            return false;
        }

        return queryFactory.select(idPath)
                .from(rowPath)
                .where(idPath.eq(resourceId))
                .fetchOne() != null;
    }

    @Transactional(readOnly = true)
    @Override
    public long count() {
        return queryFactory.select(idPath)
                .from(rowPath)
                .fetchCount();
    }

    @Override
    public boolean delete(String resourceId) {
        if (resourceId == null) {
            return false;
        }

        return queryFactory.delete(rowPath)
                .where(idPath.eq(resourceId))
                .execute() == 1;
    }

    @Override
    public boolean delete(Collection<String> resourceIds) {
        return queryFactory.delete(rowPath)
                .where(idPath.in(resourceIds))
                .execute() == resourceIds.size();
    }

    @Override
    public boolean deleteAll() {
        return queryFactory.delete(rowPath).execute() > 0;
    }

    protected Collection<String> findAllIds(Collection<E> resources) {
        Collection<String> resourceIds = resources.stream().map(E::uuid).collect(Collectors.toList());

        return queryFactory.select(idPath)
                .from(rowPath)
                .where(idPath.in(resourceIds))
                .fetch();
    }

    protected OrderSpecifier[] computeOrders(PageRequest pageRequest) {
        List<OrderSpecifier> orders = new ArrayList<>();

        List<Path> sortablePaths =
                rowPath.getColumns()
                        .stream()
                        .filter(path -> path instanceof ComparableExpressionBase)
                        .collect(toList());

        for (Path<?> path : sortablePaths) {
            String columnName = path.getMetadata().getName();

            if (pageRequest.sorts.containsKey(columnName)) {
                ComparableExpressionBase comparableExpressionBase = (ComparableExpressionBase)path;

                if (pageRequest.sorts.containsKey(columnName)) {
                    switch (pageRequest.sorts.get(columnName)) {
                        case ASC:
                            orders.add(comparableExpressionBase.asc());
                            break;
                        case DESC:
                            orders.add(comparableExpressionBase.desc());
                            break;
                        default:
                            orders.add(comparableExpressionBase.asc());
                            break;
                    }
                }
            }
        }

        return orders.toArray(new OrderSpecifier[orders.size()]);
    }
}
