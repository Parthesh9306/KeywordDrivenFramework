package operation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import Utility.GlobalLib;
import Utility.ProvisoException;

public class InternalDbOperation {
	/**
	 * Initailise Db operation
	 */
	static DbOperation dbOperation = new DbOperation();
	
	public static int lastInsertedTetsSuiteResultId = 0;
	
	public static int lastInsertedTestCaseResultId = 0;
	
	public static int lastInsertedTestStepResultId = 0;
	
	public static String stepStartTime;
	
	public static String stepEndTime;
	
	public static String testCaseStartTime;
	
	public static String testCaseEndTime;
	
	public static String testSuiteStartTime;
	
	public static String testSuiteEndTime;
		
	public static Connection connection;
	
	public static Connection connectionTwo;
	
	public Connection connectionTemp;
	
	/**
	 * Create test suite 
	 * 
	 * @param name
	 * @param description
	 * @param passCount
	 * @param failCount
	 * @param result
	 * @param executionId
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws ProvisoException
	 */
	public static int createTestSuiteResult(String name, String description, int passCount, int failCount, String result, int executionId) {
		try {
			if ("DB".equalsIgnoreCase(Debug.runMode)) {
				testSuiteStartTime = GlobalLib.getConvertedDateString(new Date(), "MM/dd/yyyy HH:mm:ss");
						
				String sql = "INSERT INTO test_suite_result (name, description, fail_count, pass_count, result, execution_master_id) "
		        		+ "values (?, ?, ?, ?, ?, ?)";
		        PreparedStatement statement = getConnectionTwo().prepareStatement(sql);
				statement.setString(1, name);
		        statement.setString(2, description);
		        statement.setInt(3, passCount);
		        statement.setInt(4, failCount);
		        statement.setString(5, result.toUpperCase());
		        statement.setInt(6, executionId);
		        
		        statement.executeUpdate();
		        ResultSet rs = statement.getGeneratedKeys();
		        
		        lastInsertedTetsSuiteResultId = 0;
		        
		        if (rs.next()){
		        	lastInsertedTetsSuiteResultId = rs.getInt(1);
		         //   System.out.println("Last inserted Test Suite Result id " + lastInsertedTetsSuiteResultId);
		        }
		        
				return lastInsertedTetsSuiteResultId;
			}
		} catch (Exception e) {
			System.out.println("Exception while storing records");
			if ("Y".equalsIgnoreCase(Debug.debugFlag)) {
				e.printStackTrace();
			}
		} 
		
		
		return 0;
	}
	

	/**
	 * Create test case result 
	 * 
	 * @param description
	 * @param result
	 * @param testCaseId
	 * @param businessFlowName
	 * @param testCaseIdInt
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws ProvisoException
	 */
	public static int createTestCaseResult(String description, String result, String testCaseId, String businessFlowName, String testCaseIdInt) {
		try {
			if ("DB".equalsIgnoreCase(Debug.runMode)) {
				closeConnectionTwo(false);
				testCaseStartTime = GlobalLib.getConvertedDateString(new Date(), "MM/dd/yyyy HH:mm:ss");
				
				String sql = "INSERT INTO testcase_result (decription, result, start_time, testcase_id, test_suite_result_id, testcase_id_int, business_flow_name) "
		        		+ "values (?, ?, ?, ?, ?, ?, ?)";
		        PreparedStatement statement = getConnectionTwo().prepareStatement(sql);
		        
		        statement.setString(1, description);
		        statement.setString(2, result.toUpperCase());
		        statement.setString(3, testCaseStartTime);
		        statement.setString(4, testCaseId);
		        statement.setInt(5, lastInsertedTetsSuiteResultId);
		        statement.setString(6, testCaseIdInt);
		        statement.setString(7, businessFlowName);
		       
		        statement.executeUpdate();
		        
		        ResultSet rs = statement.getGeneratedKeys();
		        
		        lastInsertedTestCaseResultId = 0;
		        
		        if (rs.next()){
		        	lastInsertedTestCaseResultId = rs.getInt(1);
		        	//System.out.println("Last inserted Test Case Result id " + lastInsertedTestCaseResultId);
		        }
		        
				return lastInsertedTestCaseResultId;
			}
		} catch (Exception e) {
			closeConnectionTwo(true);
			System.out.println("Exception while storing records");
			if ("Y".equalsIgnoreCase(Debug.debugFlag)) {
				e.printStackTrace();
			}
		} 
		
		return 0;
    }
	
	/**
	 * Create test Suite results
	 *  
	 * @param duration
	 * @param keyword
	 * @param object
	 * @param result
	 * @param stepDescription
	 * @param testData
	 * @param testCaseResultId
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws ProvisoException
	 */
	public static int createTestStepResult(String duration, String keyword, String object,  
			String result, String stepDescription, String testData, int testCaseResultId) {
		try {
			if ("DB".equalsIgnoreCase(Debug.runMode)) {
				
				stepStartTime = GlobalLib.getConvertedDateString(new Date(), "MM/dd/yyyy HH:mm:ss");
				
				String sql = "INSERT INTO step_result (keyword, object, result, step_description, test_data, testcase_result_id) "
		        		+ "values (?, ?, ?, ?, ?, ?)";
		        PreparedStatement statement = getConnectionTwo().prepareStatement(sql);
		        
		        statement.setString(1, keyword);
		        statement.setString(2, object);
		        statement.setString(3, result.toUpperCase());
		        statement.setString(4, stepDescription);
		        statement.setString(5, testData);
		        statement.setInt(6, lastInsertedTestCaseResultId);
		       
		        statement.executeUpdate();
		        
		        ResultSet rs = statement.getGeneratedKeys();
		        
		        lastInsertedTestStepResultId = 0;
		        
		        if (rs.next()){
		        	lastInsertedTestStepResultId = rs.getInt(1);
		        	//System.out.println("Last inserted Test Step Result id " + lastInsertedTestStepResultId);
		        }
		        
				return lastInsertedTestStepResultId;
			}
		} catch (Exception e) {
			closeConnectionTwo(true);
			System.out.println("Exception while storing records");
			if ("Y".equalsIgnoreCase(Debug.debugFlag)) {
				e.printStackTrace();
			}
		} 
		return 0;
		
    }
	
	/**
	 * Update test step result
	 * 
	 * @param result
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws ProvisoException
	 */
	public static int updateTestStepResult(String result) {
		try {
			if ("DB".equalsIgnoreCase(Debug.runMode)) {
				
				stepEndTime = GlobalLib.getConvertedDateString(new Date(), "MM/dd/yyyy HH:mm:ss");
				
				String duration = GlobalLib.getTimeDiff(stepStartTime, stepEndTime);
				
				String updateTableSQL = "UPDATE step_result SET result = ?, duration = ?, test_data = ?, trace = ? "
		                + " WHERE id = ?";
				
		        PreparedStatement statement = getConnectionTwo().prepareStatement(updateTableSQL);
		        
		        statement.setString(1, result.toUpperCase());
		        statement.setString(2, duration);
		        statement.setString(3, Debug.VALUE);
		        statement.setString(4, Debug.traceMessage);
		        statement.setInt(5, lastInsertedTestStepResultId);
		        
		        return statement.executeUpdate();
			}
		} catch (Exception e) {
			closeConnectionTwo(true);
			System.out.println("Exception while storing records");
			if ("Y".equalsIgnoreCase(Debug.debugFlag)) {
				e.printStackTrace();
			}
		} 
		
		return 0;
	}
	
	/**
	 * Update test case result
	 * 
	 * @param startTime
	 * @param endTime
	 * @param result
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws ProvisoException
	 */
	public static int updateTestCaseResult(String startTime, String endTime, String result) {
		try {
			if ("DB".equalsIgnoreCase(Debug.runMode)) {
				
				testCaseEndTime = GlobalLib.getConvertedDateString(new Date(), "MM/dd/yyyy HH:mm:ss");
				
				String duration = GlobalLib.getTimeDiff(testCaseStartTime, testCaseEndTime);
				
				String updateTableSQL = "UPDATE testcase_result SET result = ?, duration = ?, start_time = ?, end_time = ? "
		                + " WHERE id = ?";
				
		        PreparedStatement statement = getConnectionTwo().prepareStatement(updateTableSQL);
		        
		        statement.setString(1, result.toUpperCase());
		        statement.setString(2, duration);
		        statement.setString(3, startTime);
		        statement.setString(4, endTime);
		        statement.setInt(5, lastInsertedTestCaseResultId);
		       
		        updateTestSuiteAndExecutionMasterForCount();
		        
		        return statement.executeUpdate();
			}
		} catch (Exception e) {
			closeConnectionTwo(true);
			System.out.println("Exception while storing records");
			if ("Y".equalsIgnoreCase(Debug.debugFlag)) {
				e.printStackTrace();
			}
		} 
		
		return 0;
	}
	
	/**
	 * Update test suite result
	 * 
	 * @param failCount
	 * @param passCount
	 * @param result
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws ProvisoException
	 */
	public static int updateTestSuiteResult(int failCount, int passCount, String result) {
		try {
			if ("DB".equalsIgnoreCase(Debug.runMode)) {
				testSuiteEndTime = GlobalLib.getConvertedDateString(new Date(), "MM/dd/yyyy HH:mm:ss");
				
				String duration = GlobalLib.getTimeDiff(testSuiteStartTime, testSuiteEndTime);
				
				String updateTableSQL = "UPDATE test_suite_result SET result = ?, duration = ?, pass_count = ?, fail_count = ? "
		                + " WHERE id = ?";
				
		        PreparedStatement statement = getConnectionTwo().prepareStatement(updateTableSQL);
		        statement.setString(1, result.toUpperCase());
		        statement.setString(2, duration);
		       
		        statement.setInt(3, passCount);
		        statement.setInt(4, failCount);
		        
		        statement.setInt(5, lastInsertedTetsSuiteResultId);
		       
		        return statement.executeUpdate();
			}
		} catch (Exception e) {
			closeConnectionTwo(true);
			System.out.println("Exception while storing records");
			if ("Y".equalsIgnoreCase(Debug.debugFlag)) {
				e.printStackTrace();
			}
		} 
		return 0;
		
    }
	
	/**
	 * Update execution master
	 * 
	 * @param failCount
	 * @param passCount
	 * @param result
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws ProvisoException
	 */
	public static int updateExecutionMaster(int failCount, int passCount, String result) {
		try {
			if ("DB".equalsIgnoreCase(Debug.runMode)) {
				String updateTableSQL = "UPDATE execution_master SET result = ?, pass_count = ?, fail_count = ? "
		                + " WHERE id = ?";
				
		        PreparedStatement statement = getConnectionTwo().prepareStatement(updateTableSQL);
		        
		        statement.setString(1, result.toUpperCase());
		        
		       
		        statement.setInt(2, passCount);
		        statement.setInt(3, failCount);
		        
		        statement.setInt(4, Debug.executionId);
		       
		        return statement.executeUpdate();
			}
		} catch (Exception e) {
			closeConnectionTwo(true);
			System.out.println("Exception while storing records");
			if ("Y".equalsIgnoreCase(Debug.debugFlag)) {
				e.printStackTrace();
			}
		} 
		
		return 0;
		
    }
	
	/**
	 * Get test suites for execution master id
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws SQLException
	 * @throws ProvisoException
	 */
	public static ResultSet getTestSuitesForExecutionMasterId() throws ClassNotFoundException, IOException, SQLException, ProvisoException {
		if ("DB".equalsIgnoreCase(Debug.runMode)) {
			
			String selectQuery = "SELECT "
					+ "em.id AS execution_master_id, "
					+ "tc.id AS id, "
					+ "tc.description AS tc_description, "
					+ "ts.name AS ts_name, "
					+ "ts.id AS ts_id, "
					+ "tc.testcase_id AS testcase_id, "
					+ "em.name, "
					+ "p.name AS product_name, "
					+ "p.id AS product_id, "
					+ "prj.name AS project_name, "
					+ "prj.id AS project_id, "
					+ "v.name AS version_name, "
					+ "v.id AS version_id, "
					+ "bm.name, "
					+ "tm.name, "
					+ "m.name, "
					+ "bf.name AS business_flow_name "
					+ "FROM execution_master AS em "
					+ "LEFT JOIN execution_details ed ON ed.execution_master_id =em.id "
					+ "LEFT JOIN product p ON em.product_id = p.id "
					+ "LEFT JOIN project prj ON em.project_id = prj.id "
					+ "LEFT JOIN test_suite ts ON em.test_suite_id = ts.id "
					+ "LEFT JOIN version v ON em.version_id = v.id "
					+ "LEFT JOIN business_module bm ON em.business_module_id = bm.id "
					+ "LEFT JOIN technical_module tm ON em.technical_module_id = tm.id "
					+ "LEFT JOIN machine m ON em.machine_id = m.id "
					+ "LEFT JOIN testcase tc ON ed.testcase_id = tc.id "
					+ "LEFT JOIN business_flow bf ON bf.id = tc.business_flow_id "
					+ "WHERE em.id = " + Debug.executionId + " AND tc.testcase_id IS NOT NULL"
					+" order by bf.name ASC, tc.id asc";
			if ("Y".equalsIgnoreCase(Debug.debugFlag)) {
				System.out.println("Query : " + selectQuery);
			}
			PreparedStatement statement = getConnection().prepareStatement(selectQuery);
	        
	        return statement.executeQuery();
		}
		
		return null;
	}
	
	/**
	 * Get objects by execution master id
	 * 
	 * @param id
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws SQLException
	 * @throws ProvisoException
	 */
	public static HashMap<String,String> getObjectsJDBC(Integer id) throws ClassNotFoundException, IOException, SQLException, ProvisoException{
		if ("DB".equalsIgnoreCase(Debug.runMode)) {
			
			PropertyFileOperation propertyFileOperation = new PropertyFileOperation();
			
			String objectQuery = "SELECT orep.name AS object_name, "+
					"orep.locator AS object_locator "+
					"FROM execution_master AS em "+ 
					"LEFT JOIN execution_details ed ON ed.execution_master_id =em.id "+ 
					"LEFT JOIN object_repository orep ON em.product_id = orep.product_id AND em.project_id = orep.customer_id "+ 
					"WHERE em.id = "+ id +" AND orep.locator IS NOT NULL AND orep.locator <> ''";
			
			if ("Y".equalsIgnoreCase(Debug.debugFlag)) {
				System.out.println("Query : " + objectQuery);
			}
			
			PreparedStatement statement = getConnection().prepareStatement(objectQuery);
			ResultSet resultSetObjectRepo = statement.executeQuery();
			
			propertyFileOperation.setPropertyFileName("object.properties");
			propertyFileOperation.clearProperty();
			HashMap<String, String> propertyValuesMap = new HashMap<String, String>();
			
			
			while (resultSetObjectRepo.next()) {
				propertyValuesMap.put(resultSetObjectRepo.getString("object_name"), resultSetObjectRepo.getString("object_locator"));
			}
			
			return propertyValuesMap;
		}
		
		return null;
	}
	
	/**
	 * Get test cases by id
	 * 
	 * @param testCaseId
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 * @throws ProvisoException 
	 */
	public static ResultSet getTestCaseDataByTestCaseId(String testCaseId) throws ClassNotFoundException, IOException, SQLException, ProvisoException {
		if ("DB".equalsIgnoreCase(Debug.runMode)) {
			
			String testCaseQuery = "SELECT tc.testcase_id AS testcase_id ,s.description AS description,"
	                + "s.skip_step AS skip_step,"
	                + "k.name AS keyword_name, "
	                + "orep.name AS object_repository_name,"
	                + "orep.locator AS object_locator,"
	                + "tc.description AS testcase_description,"
	                + "td.value AS test_data_value, "
	                + "bf.name AS business_flow_name "
	                + "FROM testcase tc "
	                + " LEFT JOIN test_data td ON td.testcase_id =tc.id "
	                + " LEFT JOIN step s ON s.id = td.step_action_id"
	                + " LEFT JOIN keyword k ON k.id = s.keyword_id"
	                + " LEFT JOIN object_repository orep ON orep.id = s.object_repository_id"
	                + " LEFT JOIN business_flow bf ON bf.id = tc.business_flow_id"
	                + " WHERE tc.id='"
	                + testCaseId
	                + "' AND td.value IS NOT NULL AND td.value <>'' "
	                + "ORDER BY td.test_data_count_number, s.step_count ASC";
			
			if ("Y".equalsIgnoreCase(Debug.debugFlag)) {
				System.out.println("Query : " + testCaseQuery);
			}
			PreparedStatement statement = getConnection().prepareStatement(testCaseQuery );
			ResultSet resultTestCase = statement.executeQuery();
			
		    return resultTestCase;
		}
		
		return null;
	}

	/**
	 * Get business flow by name
	 * 
	 * @param businessFlowName
	 * @param projectId 
	 * @param productId 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws SQLException
	 * @throws ProvisoException
	 */
	public static int getBusinessFlowByName(String businessFlowName, int productId, int projectId) throws ClassNotFoundException, IOException, SQLException, ProvisoException {
		ResultSet resultSetBusinessFlow = null;
		if ("DB".equalsIgnoreCase(Debug.runMode)) {
			
			String businessFlowQuery = "SELECT id FROM business_flow WHERE name = '"+ businessFlowName +"' AND product_id = " + productId +" AND project_id = " + projectId;
			
			if ("Y".equalsIgnoreCase(Debug.debugFlag)) {
				System.out.println("Query : " + businessFlowQuery);
			}
			
			PreparedStatement statement = getConnectionTwo().prepareStatement(businessFlowQuery);
			
			resultSetBusinessFlow = statement.executeQuery();
		}
		
		while (resultSetBusinessFlow.next()) {
	        return resultSetBusinessFlow.getInt("id");
	    }
		
		return 0;
	}


	/**
	 * Get test case by business flow id and test case id
	 * @param testCaseIdSubGroup
	 * @param businessFlowId
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws SQLException
	 * @throws ProvisoException
	 */
	public static int getTestCaseByTestCaseIdAndBusinessFlowId(
			String testCaseIdSubGroup, int businessFlowId) throws ClassNotFoundException, IOException, SQLException, ProvisoException {
		ResultSet resultSetTestcaseForId = null;
		if ("DB".equalsIgnoreCase(Debug.runMode)) {
			
			String businessFlowQuery = "SELECT id FROM testcase WHERE testcase_id = '"+ testCaseIdSubGroup +"' AND business_flow_id ='" + businessFlowId + "'";
			
			if ("Y".equalsIgnoreCase(Debug.debugFlag)) {
				System.out.println("Query : " + businessFlowQuery);
			}
			
			PreparedStatement statement = getConnectionTwo().prepareStatement(businessFlowQuery);
			
			resultSetTestcaseForId =  statement.executeQuery();
		}
		
		while (resultSetTestcaseForId.next()) {
            return resultSetTestcaseForId.getInt("id");
            
        }
		return 0;
	}
	
	/**
	 * Update exection master and test suite count for result
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws ProvisoException
	 */
	public static void updateTestSuiteAndExecutionMasterForCount() {
		try {
			if ("DB".equalsIgnoreCase(Debug.runMode)) {
				
				testSuiteEndTime = GlobalLib.getConvertedDateString(new Date(), "MM/dd/yyyy HH:mm:ss");
				String duration = GlobalLib.getTimeDiff(testSuiteStartTime, testSuiteEndTime);
				String updateExecutionMaster = "UPDATE execution_master SET pass_count = ?, fail_count = ?, duration = ? "
		                + " WHERE id = ?";
				
		        PreparedStatement statementExecutionMaster = getConnectionTwo().prepareStatement(updateExecutionMaster);
		        
		        statementExecutionMaster.setInt(1, Debug.tcPassCount);
		        statementExecutionMaster.setInt(2, Debug.tcFailCount);
		        statementExecutionMaster.setString(3, duration);
		        statementExecutionMaster.setInt(4, Debug.executionId);
		       
		        statementExecutionMaster.executeUpdate();
		        
		        String updateTestSuite = "UPDATE test_suite_result SET pass_count = ?, fail_count = ?, duration = ? "
		                + " WHERE id = ?";
				
		        PreparedStatement statementTestSuite = getConnectionTwo().prepareStatement(updateTestSuite);
		        
		        statementTestSuite.setInt(1, Debug.tcPassCount);
		        statementTestSuite.setInt(2, Debug.tcFailCount);
		        statementTestSuite.setString(3, duration);
		        statementTestSuite.setInt(4, lastInsertedTetsSuiteResultId);
		        
		        statementTestSuite.executeUpdate();
			}
		} catch (Exception e) {
			closeConnectionTwo(true);
			System.out.println("Exception while storing records");
			if ("Y".equalsIgnoreCase(Debug.debugFlag)) {
				e.printStackTrace();
			}
		} 
	}
	
	/**
	 * Check jar is valid or not
	 * @param currentJarName 
	 * 
	 * @return
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws ProvisoException 
	 */
	public static boolean isValidCurrentJar(String currentJarName) throws SQLException, ClassNotFoundException, IOException, ProvisoException {
		if (!"bin".equalsIgnoreCase(currentJarName)) {
			
			String jarQuery = "SELECT * FROM release_note WHERE jar = '"+ currentJarName +"'";
			
			if ("Y".equalsIgnoreCase(Debug.debugFlag)) {
				System.out.println("Query : " + jarQuery);
			}
			
			PreparedStatement statement = getConnection().prepareStatement(jarQuery);
			
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				String isEnable = dbOperation.getStringValueFromResultSet(resultSet.getObject("is_enable"));
				
				if ("Y".equalsIgnoreCase(isEnable)) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			return true;
		}
		
		return false;
	}
	
	/**
     * Find execution master by id
     *
     * @param id
     * @return
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws ProvisoException 
     */
    public static int findEnvironmentIdById(Integer id) throws SQLException, ClassNotFoundException, IOException, ProvisoException {
    	if ("DB".equalsIgnoreCase(Debug.runMode)) {
    		
    		String executionMasterQuery = "SELECT * FROM execution_master WHERE id = '"+ id +"'";
    		
    		if ("Y".equalsIgnoreCase(Debug.debugFlag)) {
				System.out.println("Query : " + executionMasterQuery);
			}
    		
    		PreparedStatement statement = getConnection().prepareStatement(executionMasterQuery);
    		
    		ResultSet resultSet = statement.executeQuery();
    		
        	while (resultSet.next()) {
        		return resultSet.getInt("environment_id");
        	}
        }
    	
        return 0;
    }
    
    /**
     * Create environment
     * 
     * @param id
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws ProvisoException
     */
    public static void createEnvironment(int id) throws SQLException, ClassNotFoundException, IOException, ProvisoException {
    	if ("DB".equalsIgnoreCase(Debug.runMode)) {
    		
    		if (id != 0) {
        		String environmentQuery = "SELECT * FROM environment WHERE id = '"+ id +"'";
        		
        		if ("Y".equalsIgnoreCase(Debug.debugFlag)) {
    				System.out.println("Query : " + environmentQuery);
    			}
        		
        		PreparedStatement statement = getConnection().prepareStatement(environmentQuery);
        		
        		ResultSet resultSet = statement.executeQuery();
        		
            	while (resultSet.next()) {
            		String environmentName = resultSet.getString("name");
            		String environmentValue = resultSet.getString("value");
            		BufferedWriter bufferedWriter = null;
                    File environmentFile = new File(Debug.objectsFolderPath
                        + environmentName + ".properties");

                    if (!environmentFile.exists()) {
                        environmentFile.createNewFile();
                    }

                    Debug.enviromentIsOn = true;
                    Debug.enviromentFileName = environmentName;

                    Writer writer = new FileWriter(environmentFile);
                    bufferedWriter = new BufferedWriter(writer);
                    bufferedWriter.write(environmentValue);
                    bufferedWriter.close();
        			
        			break;
        		}
        	}
    	}
    }
    
    /**
	 * Common function to get connection while executing queries
	 * 
	 * @param dbConnectProperty
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 * @throws ProvisoException 
	 */
	public static Connection getConnection() throws IOException, ClassNotFoundException, SQLException, ProvisoException {
		
		if (null == connection || connection.isClosed()) {
			PropertyFileOperation fileOperation = new PropertyFileOperation();
            Properties props = null;
            System.out.println("Creating connection : " + "ConnectionCore");
            props = fileOperation.getAllPropertiesFromFile(Debug.propertyFileToRead);    
            
			
			String driverName = props.getProperty("ConnectionCore"
					+ ".driverName");
			String connectionURL = props.getProperty("ConnectionCore"
					+ ".connectionURL");
			String username = props.getProperty("ConnectionCore" + ".username");
			String password = props.getProperty("ConnectionCore" + ".password");
			if (null == driverName || null == connectionURL || null == username || null == password) {
			    throw new ProvisoException("Please specify Db connection details in environment as driverName,"
			        + " connectionURL, username, password and connection name here");
			}
			
			Class.forName(driverName);

			connection = DriverManager.getConnection(connectionURL,
					username, password);
		}
	    	
		return connection;
	}
	
	/**
	 * Common function to get connection while executing queries
	 * 
	 * @param dbConnectProperty
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 * @throws ProvisoException 
	 */
	public static Connection getConnectionTwo() throws IOException, ClassNotFoundException, SQLException, ProvisoException {
		
		if (null == connectionTwo || connectionTwo.isClosed()) {
			PropertyFileOperation fileOperation = new PropertyFileOperation();
            Properties props = null;
            System.out.println("Creating connection : " + "ConnectionCore");
            props = fileOperation.getAllPropertiesFromFile(Debug.propertyFileToRead);    
            
			
			String driverName = props.getProperty("ConnectionCore"
					+ ".driverName");
			String connectionURL = props.getProperty("ConnectionCore"
					+ ".connectionURL");
			String username = props.getProperty("ConnectionCore" + ".username");
			String password = props.getProperty("ConnectionCore" + ".password");
			if (null == driverName || null == connectionURL || null == username || null == password) {
			    throw new ProvisoException("Please specify Db connection details in environment as driverName,"
			        + " connectionURL, username, password and connection name here");
			}
			
			Class.forName(driverName);

			connectionTwo = DriverManager.getConnection(connectionURL,
					username, password);
		}
	    	
		return connectionTwo;
	}
	
	/**
	 * Reconnect to Db connection2 for communication link failure
	 * 
	 * @throws SQLException
	 */
	public static void closeConnectionTwo(boolean isException) {
		try {
			int totalTestCaseCount = Debug.tcFailCount + Debug.tcPassCount;
			if ((isException == true && connectionTwo != null)  || (null != connectionTwo && totalTestCaseCount % 5 == 0)) {
				if ("Y".equalsIgnoreCase(Debug.debugFlag)) {
					System.out.println("connection 2 re-created");
				}
				
				connectionTwo.close();
				connectionTwo = null;
			}
		} catch (Exception e) {
			System.out.println("Unable to recreate connection 2");
		}
	}
	
	/**
	 * Reconnect to Db connection1 for communication link failure
	 * 
	 * @throws SQLException
	 */
	public static void closeConnectionOne() {
		try {
			if (connection != null) {
				if ("Y".equalsIgnoreCase(Debug.debugFlag)) {
					System.out.println("connection 1 re-created");
				}
				
				connection.close();
				connection = null;
			}
		} catch (Exception e) {
			System.out.println("Unable to recreate connection 1");
		}
	}
	
	/**
	 * Common function to get connection while executing queries
	 * 
	 * @param dbConnectProperty
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 * @throws ProvisoException 
	 */
	public Connection getConnectionTemp() throws IOException, ClassNotFoundException, SQLException, ProvisoException {
		
		if (null == connectionTemp) {
			PropertyFileOperation fileOperation = new PropertyFileOperation();
            Properties props = null;
            System.out.println("Creating connection : " + "Connection Temp");
            props = fileOperation.getAllPropertiesFromFile(Debug.propertyFileToRead);    
            
			
			String driverName = props.getProperty("ConnectionCore"
					+ ".driverName");
			String connectionURL = props.getProperty("ConnectionCore"
					+ ".connectionURL");
			String username = props.getProperty("ConnectionCore" + ".username");
			String password = props.getProperty("ConnectionCore" + ".password");
			if (null == driverName || null == connectionURL || null == username || null == password) {
			    throw new ProvisoException("Please specify Db connection details in environment as driverName,"
			        + " connectionURL, username, password and connection name here");
			}
			
			Class.forName(driverName);

			connectionTemp = DriverManager.getConnection(connectionURL,
					username, password);
		}
	    	
		return connectionTemp;
	}
	
	/**
	 * Reconnect to Db connection1 for communication link failure
	 * 
	 * @throws SQLException
	 */
	public void closeConnectionTemp() {
		try {
			if (connectionTemp != null) {
				if ("Y".equalsIgnoreCase(Debug.debugFlag)) {
					System.out.println("connection Temp re-created");
				}
				
				connectionTemp.close();
				connectionTemp = null;
			}
		} catch (Exception e) {
			System.out.println("Unable to recreate connection 1");
		}
	}
}

