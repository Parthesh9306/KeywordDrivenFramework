package hibernate.service;

import hibernate.dao.ExecutionMasterDao;
import hibernate.entity.ExecutionMaster;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import operation.DbOperation;
import operation.PropertyFileOperation;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import Utility.ProvisoException;

/**
 * perform operation related to group result
 * 
 * @author Chetan.Aher
 * 
 */
public class ExecutionMasterService {
    /**
     * Group result dao object
     */
    private static ExecutionMasterDao executionMasterDao;
    
    /**
     * Create group result dao
     */
    public ExecutionMasterService() {
        executionMasterDao = new ExecutionMasterDao();
        HibernateUtilService.getInstance();
    }

    /**
     * Save group result
     * 
     * @param groupResult
     */
    public void update(ExecutionMaster executionMaster) {
    	executionMasterDao.update(executionMaster);
    }

    /**
     * Find execution master by id
     *
     * @param id
     * @return
     */
    public ExecutionMaster findById(Integer id) {
    	ExecutionMaster executionMaster = executionMasterDao.findById(id);
        
        return executionMaster;
    }
    
    /**
     * Returns the map of objects retrieved from database with the help of Execution Id
     * 
     * @param id : Execution Id
     * @return propertyValuesMap
     */
    public HashMap<String,String> getObjects(Integer id){
    	Session session = HibernateUtilService.getSessionFactory().openSession();
    	
    	Transaction transaction = session.beginTransaction();
		Query query = session.createQuery(" SELECT orep.name AS object_name, orep.locator AS object_locator FROM "+
		" ExecutionMaster em, ExecutionDetails ed,ObjectRepository orep WHERE em.id = :executionID"	+ 
		" AND em.productId = orep.productId AND em.id = ed.executionMasterId AND em.projectId = orep.customerId").setParameter("executionID",  id);
		HashMap<String,String> propertyValuesMap = new HashMap<String,String>();
		
		List list = query.list();
		
		for(Object o:list){
			Object[] arr = (Object[])o;
			propertyValuesMap.put((String)arr[0], (String)arr[1]);
		}
		transaction.commit();
		session.close();
		return propertyValuesMap;
    }
    
    /**
     * Returns the map of objects retrieved from database with the help of Execution Id
     * 
     * @param id : Execution Id
     * @return propertyValuesMap
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @throws SQLException 
     */
   /* public HashMap<String,String> getObjectsJDBC(Integer id) throws ClassNotFoundException, IOException, SQLException{
    	DbOperation dbOperation = new DbOperation();
		// Iterate through Resultset and execute each test case
		PropertyFileOperation propertyFileOperation = new PropertyFileOperation();
		
		// Connect to DB
		// Create Results Set
		ResultSet resultSetObjectRepo = dbOperation
				.executeQuery("ConnectionCore","SELECT orep.name AS object_name, "+
						"orep.locator AS object_locator "+
						"FROM execution_master AS em "+ 
						"LEFT JOIN execution_details ed ON ed.execution_master_id =em.id "+ 
						//"LEFT JOIN machine m ON em.machine_id = m.id "+
						"LEFT JOIN object_repository orep ON em.product_id = orep.product_id AND em.project_id = orep.customer_id "+ 
						//"WHERE m.name= '"+hostName+"' AND em.execution_status = '1' ");
						"WHERE em.id = "+ id);
		
		propertyFileOperation.setPropertyFileName("object.properties");
		propertyFileOperation.clearProperty();
		HashMap<String, String> propertyValuesMap = new HashMap<String, String>();
		
		while (resultSetObjectRepo.next()) {
			propertyValuesMap.put(resultSetObjectRepo.getString("object_name"), resultSetObjectRepo.getString("object_locator"));
		}
		return propertyValuesMap;
    }*/
    
    /**
     * Find Environment by id
     * 
     * @param environmentId
     * @return
     */
    /*public Environment findEnvironmentById(Integer environmentId) {
        Environment environment = (Environment) HibernateSessionManager.getCurrentSession().get(
            Environment.class, environmentId);
            
       return environment;
    }*/
    
    
    /**
     * @todo use in future public GroupResultDao groupResultDao() { return
     *       groupResultDao; } public void update(GroupResult entity) {
     *       groupResultDao.openCurrentSessionwithTransaction();
     *       groupResultDao.update(entity);
     *       groupResultDao.closeCurrentSessionwithTransaction(); }
     * 
     *       public GroupResult findById(String id) {
     *       groupResultDao.openCurrentSession(); GroupResult groupResult =
     *       groupResultDao.findById(id); groupResultDao.closeCurrentSession();
     *       return groupResult; }
     * 
     *       public void delete(String id) {
     *       groupResultDao.openCurrentSessionwithTransaction(); GroupResult
     *       groupResult = groupResultDao.findById(id);
     *       groupResultDao.delete(groupResult);
     *       groupResultDao.closeCurrentSessionwithTransaction(); }
     * 
     *       public List<GroupResult> findAll() {
     *       groupResultDao.openCurrentSession(); List<GroupResult> groupResults
     *       = groupResultDao.findAll(); groupResultDao.closeCurrentSession();
     *       return groupResults; }
     * 
     *       public void deleteAll() {
     *       groupResultDao.openCurrentSessionwithTransaction();
     *       groupResultDao.deleteAll();
     *       groupResultDao.closeCurrentSessionwithTransaction(); }
     */
}
