package hibernate.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Interface to save, update, delete group result
 * 
 * @author Chetan.Aher
 * 
 * @param <T>
 * @param <Id>
 */
public interface ExecutionMasterDaoInterface<T, Id extends Serializable> {
    /**
     * Save group result
     * 
     * @param entity
     */
    public void persist(T entity);

    /**
     * Update group result
     * 
     * @param entity
     */
    public void update(T entity);

    /**
     * Find group result by id
     * 
     * @param id
     * @return
     */
    public T findById(Id id);

    /**
     * Delete group result
     * 
     * @param entity
     */
    public void delete(T entity);

    /**
     * Find all group result
     * 
     * @return
     */
    public List<T> findAll();

    /**
     * Delete all group result
     */
    public void deleteAll();
}
