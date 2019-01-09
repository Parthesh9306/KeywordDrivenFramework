package testScript;

import hibernate.entity.TestSuiteResult;
import hibernate.entity.TestcaseResult;

import java.awt.AWTException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Properties;

import mail.Email;
import operation.Debug;
import operation.InternalDbOperation;
import operation.PropertyFileOperation;
import operation.ReadObject;

import org.apache.log4j.LogManager;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

import results.jaxb.XMLGenerator;
import Utility.GlobalLib;
import Utility.ProvisoException;
import excelExportAndFileIO.ReadAFWExcelFile;

public class HybridExecuteTest extends Driver {
	WebDriver webdriver;
	String strTestCaseName = null;
	static String indexFilePath = null;
	static String groupFilePath = null;
	static String strMasterFile = null;
	static String strGroupFile = null;
	static String strTestCaseFile = null;

	/**
	 * testLogin -Login test method
	 * 
	 * @param testcaseName
	 *            , keyword, objectName, objectType, value
	 * 
	 * 
	 */
	@BeforeTest
	public void before() throws Exception {
		// Debug.closeBrowser("ALL");
		Runtime.getRuntime().exec(
				"taskkill /F /IM IEDriverServer.exe /T");
	}

	@Test(dataProvider = "hybridData")
	public void testLogin(String GroupName, String runFlag, String Description,
			String objectType, String value) {
	    try {
		LogManager.resetConfiguration(); 
	    
	    Debug.tsFailCount = 0;
		Debug.tsPassCount = 0;
		String timeDiff, startDate, endDate;
		String strResult = null;
		String strExecutionFlag = "A", strExecutionVal = null, strTestCaseDesc = null, groupFileName = null, obRepoName = "object";
		Row xlRow = null;
		ReadObject object = new ReadObject();
		Properties allEnvObjects = object.getObjectRepository("environment");
		//ExecutionMasterService executionMasterService = null; 
		if ("Y".equalsIgnoreCase(allEnvObjects.getProperty("saveRecordsToDbFlag"))) {
			//executionMasterService = new ExecutionMasterService();
			;
		//    EnvironmentService environmentService = new EnvironmentService();
		    InternalDbOperation.createEnvironment(InternalDbOperation.findEnvironmentIdById(Debug.executionId));
		    
		}
		switch(Debug.runMode.toUpperCase()){
			case "DB":
				PropertyFileOperation propertyFileOperation = new PropertyFileOperation();
				propertyFileOperation.setPropertyFileName("object.properties");
				propertyFileOperation.clearProperty();
				
				HashMap<String, String> propertyValuesMap = InternalDbOperation.getObjectsJDBC(Debug.executionId);
				propertyFileOperation.writePropertyByMap(propertyValuesMap);
			obRepoName = "object";
				
			break;
			case "EXCEL":
				obRepoName = allEnvObjects.getProperty("RepositoryName");
			break;
			default:
				System.out.println("Please specify RunMode in environment.properties");
		
		}
		
		Debug.closeBrowserFlag = allEnvObjects.getProperty("closeBrowsersFlag");
		Properties allObjects = object.getObjectRepository(obRepoName);

		// Get Start Date
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		// get current date time with Date()
		Date sDate = new Date();
		startDate = dateFormat.format(sDate);

		// Switch DB  /Excel mode
		
		switch(Debug.runMode.toUpperCase()){
		case "EXCEL":
		if (runFlag.equalsIgnoreCase("Y")) {
			System.out.println("Running Group: " + GroupName);
			ReadAFWExcelFile file = new ReadAFWExcelFile();
			Sheet AFWSheet = file.readExcel(System.getProperty("user.dir")
					+ "\\", "/testCases/MasterDriver.xlsx", GroupName);
			System.out.println("Sheet reading : " + AFWSheet.getSheetName());
			// Initiate Index file
			groupFileName = GlobalLib.setResultFile("Group", strGroupFile + ":"
					+ GroupName);
			// COMMENTED
			groupFilePath = System.getProperty("user.dir") + "\\Results\\"
					+ groupFileName;
			// groupFilePath.lastIndexOf("\\");
			System.out.println(groupFilePath.substring(groupFilePath
					.lastIndexOf("\\")));
			int rowCount = AFWSheet.getLastRowNum() - AFWSheet.getFirstRowNum();
			// Arraylist to Store TC IDs
			ArrayList<String> al = new ArrayList<String>();
			// Iterate through rows
			for (int i = 0; i <= rowCount; ++i) {
				xlRow = AFWSheet.getRow(i);
				strExecutionVal = GlobalLib.getCellValueAsString(xlRow.getCell((short) 1));
				strExecutionFlag = GlobalLib.getCellValueAsString(xlRow.getCell((short) 3));
				
				strTestCaseDesc = GlobalLib.getCellValueAsString(xlRow.getCell((short) 4));;
				// if 2nd column has Y then get 1st column value
				al.add(strExecutionVal + ":" + strExecutionFlag + ":"
						+ strTestCaseDesc);

			}
			strResult = ReadSubGroupData(allObjects, GroupName, al,
					groupFilePath);
		} else {
			groupFilePath = null;
			strResult = "Skipped";

		}
		Debug.testSuiteDescription = strTestCaseDesc;
		Debug.testSuiteName = GroupName;
		break;
		
		case "DB":
			// Initiate Index file
						groupFileName = GlobalLib.setResultFile("Group", strGroupFile + ":"
								+ GroupName);
						// COMMENTED
						groupFilePath = System.getProperty("user.dir") + "\\Results\\"
								+ groupFileName;
						// groupFilePath.lastIndexOf("\\");
						System.out.println(groupFilePath.substring(groupFilePath
								.lastIndexOf("\\")));
			// Arraylist to Store TC IDs
						ArrayList<String> al = new ArrayList<String>();
						al.add("TestRun:Flag:Desc");
						//al.add("MercuryFlightsDemo:Y:MercuryFlightsDemo for sanity testing automation framework");
						al.add("RunStart:Y:MercuryFlightsDemo for sanity testing automation framework");
						strResult = ReadSubGroupData(allObjects, GroupName, al,
								groupFilePath);
		break;
		default:
			System.out.println("Please specify RunMode in environment.properties");
		
	}
		// get current date time with Date()
		Date eDate = new Date();
		endDate = dateFormat.format(eDate);
		// get time difference
		timeDiff = GlobalLib.getTimeDiff(startDate, endDate);
		
		TestSuiteResult testSuiteResult = new TestSuiteResult();
		testSuiteResult.setDescription(Debug.testSuiteDescription);
		testSuiteResult.setDuration(timeDiff);
		testSuiteResult.setName(Debug.testSuiteName);
		testSuiteResult.setResult(strResult);
		testSuiteResult.setTestcaseResults(Debug.testcaseResults);
		testSuiteResult.setPassCount(Debug.tcPassCount);
		testSuiteResult.setFailCount(Debug.tcFailCount);
		
		if (Debug.tcFailCount > 0){
			Debug.tsFailCount = Debug.tsFailCount +1;
		} else {
			Debug.tsPassCount = Debug.tsPassCount +1;
		}
		
		InternalDbOperation.updateTestSuiteResult(Debug.tcFailCount, Debug.tcPassCount, strResult);
		
		Debug.testSuiteResults.add(testSuiteResult);
		Debug.testcaseResults = new LinkedHashSet<TestcaseResult>();
		try (PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter(System.getProperty("user.dir") + "\\Results\\"
						+ indexFilePath, true)))) {
			out.println("<tr bgcolor = #FFFFFF>");
			out.println("<td width=400><p align=center><b><font face=Verdana size=2 color=#FF0000><a href=\""
					+ groupFileName
					+ "\" target=\"_blank\">"
					+ Debug.testSuiteName
					+ "</a> </href></font></b></td>");
			out.println("<td width=400><p align=center><font face=Verdana size=2>"
					+ Debug.testSuiteDescription + "</td>");
			out.println("<td width=400><p align=center><b><font face=Verdana size=2 color=#FF0000><a href=\"\" target=\"_blank\">"
					+ strResult + "</a> </href></font></b></td>");
			out.println("<td width=400><p align=center><font face=Verdana size=2>"
					+ Debug.tcPassCount + "</td>");
			out.println("<td width=400><p align=center><font face=Verdana size=2>"
					+ Debug.tcFailCount + "</td>");
			out.println("<td width=400><p align=center><font face=Verdana size=2>"
					+ timeDiff + "</td>");
			out.println("</td></tr>");
		} catch (IOException e) {
			// exception handling left as an exercise for the reader
		}
		Date todayDate = new Date();
		String todayDateString = GlobalLib.getConvertedDateString(todayDate, "dd-MM-YY hh_mm_ss");
		/*XMLGenerator.generateXML(System.getProperty("user.dir") + "\\Results\\"
				+ "result" + todayDateString + ".xml");*/

		// save records to Db yes or no
		if ("Y".equalsIgnoreCase(allEnvObjects.getProperty("saveRecordsToDbFlag"))) {
			System.out.println("Storing records to Db....");
			/*Debug.executionMaster.setTestSuiteResults(Debug.testSuiteResults);
            Result[] results = Result.values();
            for (Result result : results) {
                if (strResult.equalsIgnoreCase(result.displayName())) {
                    Debug.executionMaster.setResult(result);
                }
            }
			
			Debug.executionMaster.setPassCount(Debug.tsPassCount);
			Debug.executionMaster.setFailCount(Debug.tsFailCount);
			*/
			//executionMasterService.update(Debug.executionMaster);
			System.out.println("Stored records successfully!");
			InternalDbOperation.updateExecutionMaster(Debug.tcFailCount, Debug.tcPassCount, strResult);
		}
		
		// Send Mail
		if (allEnvObjects.getProperty("sendMailFlag").equalsIgnoreCase("Y")
				&& runFlag.equalsIgnoreCase("Y")) {
			Email.sendMail(indexFilePath);
		}
				
		Debug.testSuiteResults =new LinkedHashSet<TestSuiteResult>();
	    } catch (MySQLSyntaxErrorException e) {
	        ProvisoException.exceptionHandler(e, null);
        } catch (IOException e) {
	        ProvisoException.exceptionHandler(e, null);
        } catch (ClassNotFoundException e) {
            ProvisoException.exceptionHandler(e, null);
        } catch (SQLException e) {
            ProvisoException.exceptionHandler(e, null);
        } catch (ProvisoException e) {
            ProvisoException.exceptionHandler(e, null);
        } catch (AWTException e) {
            ProvisoException.exceptionHandler(e, null);
        } catch (Exception e) {
            ProvisoException.exceptionHandler(e, null);
        } 
	}

	/**
	 * getDataFromDataprovider - input the login values from the DataProvider
	 * 
	 * @return object
	 * 
	 * 
	 * 
	 */

	@DataProvider(name = "hybridData")
	public Object[][] getDataFromDataprovider() throws Exception {
		ReadObject rObject = new ReadObject();
		Properties allEnvObjects = rObject
				.getObjectRepository("environment");
		Debug.runMode = allEnvObjects.getProperty("RunFrom");
		Debug.debugFlag = allEnvObjects.getProperty("debug_flag", "N");
		
		Debug.iSleepMin = Integer.parseInt(allEnvObjects.getProperty("sleepMIN", "1000"));
		Debug.iSleepMid = Integer.parseInt(allEnvObjects.getProperty("sleepMID", "3000"));
		Debug.iSleepMax = Integer.parseInt(allEnvObjects.getProperty("sleepMAX", "5000"));
		Object[][] object = null;
		ReadAFWExcelFile file = new ReadAFWExcelFile();
		String resultStamp = GlobalLib.getResultStamp();
		// strMasterFile = "Result_Index_" + resultStamp + ".html";
		strMasterFile = "Index_" + resultStamp + ".html";
		strGroupFile = "Result_Group_" + resultStamp + ".html";

		// Initiate Index file
		indexFilePath = GlobalLib.setResultFile("Index", strMasterFile);
		// Switch between DB and Excel mode ########################k
		switch(Debug.runMode.toUpperCase()){
		case "DB":
		
			object = new Object[1][5];
//			object[0][0] = "MercuryFlightsDemo".toString();
//			object[0][1] = "Y".toString();
//			object[0][2] = "MercuryFlightsDemo".toString();
			object[0][0] = "RunStart".toString();
			object[0][1] = "Y".toString();
			object[0][2] = "RunStart".toString();
		break;
		
		case "EXCEL":
			Sheet AFWSheet = file.readExcel(System.getProperty("user.dir") + "\\",
				"/testCases/MasterDriver.xlsx", "Driver");

		// Find number of rows in excel file
		int rowCount = AFWSheet.getLastRowNum() - AFWSheet.getFirstRowNum();
		object = new Object[rowCount][5];
		for (int i = 0; i < rowCount; i++) {
			// Loop over all the rows
			Row row = AFWSheet.getRow(i + 1);
			// Create a loop to print cell values in a row
			for (int j = 0; j < row.getLastCellNum(); j++) {
				// Print excel data in console
				object[i][j] = row.getCell(j).toString();
			}
			
		}
		break;
		default:
			System.out.println("Please specify RunMode in environment.properties");
		}
		System.out.println("");
		return object;
	}

	
	@AfterTest
	public void after() throws Exception {
		InternalDbOperation.closeConnectionOne();
		InternalDbOperation.closeConnectionTwo(false);
		   
		try (PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter(System.getProperty("user.dir") + "\\Results\\"
						+ indexFilePath, true)))) {
			out.println("</body></html>");

		} catch (IOException e) {
			// exception handling left as an exercise for the reader
		}
	}

}
