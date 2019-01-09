package Utility;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import operation.ReadObject;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.testsuite.TestCase;
import com.eviware.soapui.model.testsuite.TestStep;
import com.eviware.soapui.model.testsuite.TestSuite;

/**
 * Generate Excel from soap ui xml file
 * 
 * @author Chetan.Aher
 *
 */
public class SoapUIExcelGenerator {
	/**
	 * path of soap ui xml file
	 */
	private String soapUIXmlPath = "";
	
	/**
	 * Excel file to generate
	 */
	private String soapUIOutputExcelPath = "";
	
	/**
	 * Browser to select in output excel
	 */
	private String soapUIOutputExcelBrowser = "";
	
	/**
	 * Skip test in output excel
	 */
	private String soapUIOutputSkipTest ="";
	
	/**
	 * Total number of headers in excel file
	 */
	public static int HEADER_ROW_COUNT = 7;
	
	/**
	 * Initialize path from environment property file
	 */
	public SoapUIExcelGenerator() {
		ReadObject object = new ReadObject();
		Properties allEnvObjects = null;
		try {
			allEnvObjects = object.getObjectRepository("environment");
			soapUIXmlPath = allEnvObjects.getProperty("soapUIXmlPath");
			soapUIOutputExcelPath = allEnvObjects.getProperty("soapUIOutputExcelPath");
			soapUIOutputExcelBrowser = allEnvObjects.getProperty("soapUIOutputExcelBrowser");
			soapUIOutputSkipTest = allEnvObjects.getProperty("soapUIOutputSkipTest");
		} catch (IOException e) {
			System.out.println("Exception while getting soap ui xml to excel environment properties");
			e.printStackTrace();
		}
	}

	/**
	 * Generate excel from soap ui xml file
	 */
	public void generateExcelFromSoapUIXML() {
		try {
			XSSFWorkbook wb = new XSSFWorkbook();
			
			if (soapUIXmlPath == "") {
				System.out.println("Xml file not specified in environment property");
			}
			
			if (soapUIOutputExcelPath == "") {
				System.out.println("Out put excel path not specified in environment property");
			}
			System.out.println("Xml read from => " + soapUIOutputExcelPath);
			WsdlProject wsdlProject = new WsdlProject(soapUIXmlPath);
			List<TestSuite> testSuites = wsdlProject.getTestSuiteList();
			
			for (int testSuiteCount = 0; testSuiteCount < testSuites.size(); testSuiteCount++) {
				TestSuite testSuite = testSuites.get(testSuiteCount);
				XSSFSheet sheet = wb.createSheet(testSuite.getName());
				System.out.println("TestSuite name => " + testSuite.getName());
				List<TestCase> testCases = testSuite.getTestCaseList();

				for (int testCaseCount = 0; testCaseCount < testCases.size(); testCaseCount++) {
					TestCase testCase = testCases.get(testCaseCount);
					List<TestStep> testSteps = testCase.getTestStepList();
					
					for (int testStepCount = 0; testStepCount < testSteps.size(); testStepCount++) {
						XSSFRow row;
						XSSFCell cell;
						
						for(int headerRowCount = 0; headerRowCount < HEADER_ROW_COUNT; headerRowCount++) {
							row = sheet.getRow((short) headerRowCount);
							
							if (row == null) {
								row = sheet.createRow(headerRowCount);
							}
							cell = row.getCell(testCaseCount);
							
							if (cell == null) {
								cell = row.createCell(testCaseCount);
							}
							String cellValue = getHeaderCellValue(headerRowCount, testCase, testSuite);
							cell.setCellValue(cellValue);
						}
						
						int rowCountWithHeader = testStepCount + HEADER_ROW_COUNT;
						row = sheet.getRow((short) rowCountWithHeader);
					
						if (row == null) {
							row = sheet.createRow(rowCountWithHeader);
						}
						cell = row.getCell(testCaseCount);
						
						if (cell == null) {
							cell = row.createCell(testCaseCount);
						}
						TestStep testStep = testSteps.get(testStepCount);
						cell.setCellValue(testStep.getName());
					}
				}
			}
			FileOutputStream fileOut = new FileOutputStream(soapUIOutputExcelPath);
			System.out.println("File generated on location => " + soapUIOutputExcelPath);
			wb.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			System.out.println("Exception while generating excel file from soap ui excel");
			e.printStackTrace();
		}
	}

	/**
	 * Get header cell values 
	 * 
	 * @param headerRowCount
	 * @param testCase
	 * @param testSuite
	 * @return
	 */
	private String getHeaderCellValue(int headerRowCount, TestCase testCase, TestSuite testSuite) {
		String cellValue = "";
		switch (headerRowCount) {
		case 0:
			cellValue  = "Testcase";
			break;
		case 1:
			cellValue = testCase.getName();	
			break;
		case 2:
			cellValue = soapUIOutputSkipTest;
			break;
		case 3:
			cellValue = soapUIOutputExcelBrowser;	
			break;
		case 4:
			cellValue = soapUIXmlPath;
			break;
		case 5:
			cellValue = testSuite.getName();
			break;
		case 6:
			cellValue = testCase.getName();	
			break;
		}
		
		return cellValue;
	}
}
