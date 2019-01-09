package hibernate.dao;

import hibernate.entity.ExecutionMaster;
import hibernate.service.HibernateUtilService;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Group dao to implement session, transaction, save, delete , update methods
 * 
 * @author Chetan.Aher
 * 
 */
public class ExecutionMasterDao implements
		ExecutionMasterDaoInterface<ExecutionMaster, String> {
	/**
	 *  constructor to initialise  HibernateUtilService
	 */
	public ExecutionMasterDao() {
		HibernateUtilService.getInstance();
	}

	/**
	 * Update group result
	 */
	public void update(ExecutionMaster executionMaster) {
		Session session = HibernateUtilService.getSessionFactory().openSession();
		Transaction transation = session.beginTransaction();
		
    	System.out.println(executionMaster.getId() + ">>>>ID to SAVE");
        session.update(executionMaster);
        
        transation.commit();
        session.close();
	}

	/**
	 * Find group result by id
	 * 
	 * @param id
	 */
	public ExecutionMaster findById(Integer id) {
		Session session = HibernateUtilService.getSessionFactory().openSession();
		Transaction transation = session.beginTransaction();
		
	    ExecutionMaster executionMaster = (ExecutionMaster) session.get(
	        ExecutionMaster.class, id);
	    
	    transation.commit();
	    session.close();
		return executionMaster;
	}
	@Override
	public void persist(ExecutionMaster entity) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public ExecutionMaster findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void delete(ExecutionMaster entity) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<ExecutionMaster> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Delete group result
	 */
	/*public void delete(ExecutionMaster executionMaster) {
		Session session = HibernateUtilService.getSessionFactory().openSession();
		HibernateSessionManager.getCurrentSession().delete(executionMaster);
	}
*/
	/**
	 * Find all group result
	 */
/*	public List<ExecutionMaster> findAll() {
		@SuppressWarnings("unchecked")
		List<ExecutionMaster> executionMasters = (List<ExecutionMaster>) HibernateSessionManager.getCurrentSession()
				.createQuery("from ExecutionMaster").list();
		return executionMasters;
	}
*/
	
	/**
	 * delete all group result
	 */
/*	public void deleteAll() {
		List<ExecutionMaster> entityList = findAll();
		for (ExecutionMaster entity : entityList) {
			delete(entity);
		}
	}
*/
    //@Override
 /*   public ExecutionMaster findById(String id) {
        // TODO Auto-generated method stub
        return null;
    }
*/
}
