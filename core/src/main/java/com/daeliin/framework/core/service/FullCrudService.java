package com.daeliin.framework.core.service;

import com.daeliin.framework.commons.model.PersistentResource;
import java.io.Serializable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Provides CRUD operations and pagination for a resource.
 * @param <E> resource type
 * @param <ID> resource id type
 */
public interface FullCrudService<E extends PersistentResource, ID extends Serializable> {
   
    /**
     * Saves or updates a resource.
     * @param resource resource to create or update
     * @return updated resource
     */
    E save(E resource);

    /**
     * Saves or updates mulitple resources.
     * @param iterable resources to create or update
     * @return updated resources
     */
    Iterable<E> save(Iterable<E> iterable);
    
    /**
     * Returns true if the resource exists, false otherwise
     * @param id resource id
     * @return true of the resource exists, false otherwise
     */
    boolean exists(ID id);
    
    /**
     * Returns the total number of resources.
     * @return total number of resources
     */
    long count();
    
    /**
     * Finds a resource by its id.
     * @param id resource id
     * @return resource
     */
    E findOne(ID id);
    
    /**
     * Finds every resources.
     * @return every resources
     */
    Iterable<E> findAll();
    
    /**
     * Finds every resources, and applies a sort.
     * @param sort sort to apply
     * @return every resources sorted according to the sort
     */
    Iterable<E> findAll(Sort sort);

    /**
     * Finds a page of resources.
     * @param pageable resource page request
     * @return resource page
     */
    Page<E> findAll(Pageable pageable);
    
    /**
     * Finds multiple resources by their ids.
     * @param iterable resources ids
     * @return resources
     */
    Iterable<E> findAll(Iterable<ID> iterable);

    /**
     * Delete a resource by its id.
     * @param id id of the resource to delete
     */
    void delete(ID id);

    /**
     * Deletes a resource.
     * @param resource resource to delete
     */
    void delete(E resource);

    /**
     * Deletes multiple resources.
     * @param iterable resources to delete
     */
    void delete(Iterable<? extends E> iterable);

    /**
     * Deletes all resources.
     */
    void deleteAll();
}
