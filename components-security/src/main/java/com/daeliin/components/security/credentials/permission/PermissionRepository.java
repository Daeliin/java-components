package com.daeliin.components.security.credentials.permission;

import com.daeliin.components.core.resource.repository.ResourceRepository;
import com.daeliin.components.security.sql.BAccountPermission;
import com.daeliin.components.security.sql.BPermission;
import com.daeliin.components.security.sql.QAccountPermission;
import com.daeliin.components.security.sql.QPermission;
import com.google.common.base.Strings;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * Loads permission in memory, and caches it.
 */
@Transactional
@Component
public class PermissionRepository extends ResourceRepository<BPermission, String> {

    public PermissionRepository() {
        super(QPermission.permission, QPermission.permission.id, BPermission::getId);
    }

    public Collection<BPermission> findForAccount(String accountId) {
        if (Strings.isNullOrEmpty(accountId)) {
            return new ArrayList<>();
        }

        return queryFactory.select(QPermission.permission)
                .from(QPermission.permission)
                .join(QAccountPermission.accountPermission)
                .on(QAccountPermission.accountPermission.permissionId.eq(QPermission.permission.id))
                .where(QAccountPermission.accountPermission.accountId.eq(accountId))
                .orderBy(QPermission.permission.name.asc())
                .fetch();
    }

    public void addToAccount(String accountId, String permissionId) {
        BAccountPermission accountPermission= new BAccountPermission(accountId, permissionId);

        Set<String> accountPermissions = findForAccount(accountId)
                .stream()
                .map(BPermission::getId)
                .collect(toSet());

        if (!accountPermissions.contains(permissionId)) {
            queryFactory.insert(QAccountPermission.accountPermission)
                    .populate(accountPermission)
                    .execute();
        }
    }

    public void deleteForAccount(String accountId) {
        queryFactory.delete(QAccountPermission.accountPermission)
                .where(QAccountPermission.accountPermission.accountId.eq(accountId))
                .execute();
    }

    public void deleteForAccount(String accountId, String permissionId) {
        queryFactory.delete(QAccountPermission.accountPermission)
                .where(QAccountPermission.accountPermission.accountId.eq(accountId)
                .and(QAccountPermission.accountPermission.permissionId.eq(permissionId)))
                .execute();
    }
}
