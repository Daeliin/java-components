package com.daeliin.components.core.resource.service;

import com.daeliin.components.domain.pagination.Page;
import com.daeliin.components.domain.pagination.PageRequest;
import com.daeliin.components.domain.resource.Persistable;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Provides CRUD operations and pagination for a resource.
 * @param <E> resource type
 */
public interface PagingService<E> {

    /**
     * Creates a resource.
     * @param resource resource to create
     * @return created resource
     */
    E create(E resource);

    /**
     * Creates multiple resources.
     * @param resources resources to create
     * @return created resources
     */
    Collection<E> create(Collection<E> resources);

    /**
     * Updates a resource.
     * @param resource resource to update
     * @return updated resource
     */
    E update(E resource);

    /**
     * Updates multiple resources.
     * @param resources resources to update
     * @return updated resources
     */
    Collection<E> update(Collection<E> resources);

    /**
     * Finds a resource by its id.
     * @param resourceId resource id
     * @return resource
     */
    E findOne(Long resourceId);

    /**
     * Finds every resources.
     * @return every resources
     */
    Collection<E> findAll();

    /**
     * Finds multiple resources by their ids.
     * @param resourceIds resources ids
     * @return resources
     */
    Collection<E> findAll(Collection<Long> resourceIds);

    /**
     * Finds a page of resources.
     * @param pageRequest resource page request
     * @return resource page
     */
    Page<E> findAll(PageRequest pageRequest);

    /**
     * Returns true if the resource exists, false otherwise
     * @param resourceId resource id
     * @return true of the resource exists, false otherwise
     */
    boolean exists(Long resourceId);

    /**
     * Returns the total number of resources.
     * @return total number of resources
     */
    long count();

    /**
     * Delete a resource by its id.
     * @param resourceId id of the resource to delete
     */
    boolean delete(Long resourceId);

    /**
     * Delete resources by their ids.
     * @param resourceIds resources ids
     */
    boolean delete(Collection<Long> resourceIds);

    /**
     * Deletes a resource.
     * @param resource resource to delete
     */
    boolean delete(E resource);

    /**
     * Deletes all resources.
     */
    boolean deleteAll();
}
