package testScript;

import hibernate.entity.StepResult;
import hibernate.entity.TestcaseResult;

import java.awt.AWTException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Properties;

import operation.Debug;
import operation.InternalDbOperation;
import operation.PropertyFileOperation;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import Utility.GlobalLib;
import Utility.ProvisoException;
import Utility.ScreenshotUtility;
import constants.Separator;
import excelExportAndFileIO.ReadAFWExcelFile;

public class Driver extends ReadAFWExcelFile {

	/**
	 * ReadSubGroupData -
	 * 
	 * @param allObjects
	 *            , Properties, String GroupName, ArrayList<String> al, String
	 *            groupFilePath
	 * 
	 * @return strTestCaseResult:
	 * 
	 * 
	 */

	public static String testCaseFilePath = null, strGrpResult = null,
			strResult = null, strMainResult = null, imageFolder = null,
			testCaseFileName, imageFileName;
	public static int continueRow = 0;

	public static String ReadSubGroupData(Properties allObjects,
			String GroupName, ArrayList<String> al, String groupFilePath)
			throws IOException, AWTException {
		String startDate, skipFlag, strTestCase, keyword, description, objectName, value;
		String strTestCaseID = "";
		String strTcResult = "Pass";
		String testCaseId = "0";
		// Get Debug flag
		Debug.pollingDuration = 5;
		Row xlRow = null;
		strMainResult = "Pass";
		try {
			ReadAFWExcelFile file = new ReadAFWExcelFile();
			Iterator<String> itr = al.iterator();
			itr.next();
			while (itr.hasNext()) {
				strGrpResult = "Pass";
				String[] parts = itr.next().split(Separator.SEPARATOR_COLON);
				strTestCase = parts[0];
				String execFlag = parts[1];
				String TCDesc = parts[2];

				if (execFlag.equalsIgnoreCase("Y")) {
					
					switch (Debug.runMode.toUpperCase()) {

					case "DB":
						PropertyFileOperation propertyFileOperation = new PropertyFileOperation();
						propertyFileOperation.setPropertyFileName("environment.properties");
						System.out.println("Execution started on " + Debug.executionId);
						ResultSet resultSetTestSuite = InternalDbOperation.getTestSuitesForExecutionMasterId();
						Debug.tcFailFlag = false;
						Debug.tcPassCount = 0;
						Debug.tcFailCount = 0;
						boolean testSuiteCreatedFlag = false;
						int productId = 0;
						int projectId = 0;
						
						while (resultSetTestSuite.next()) {
							Debug.testSuiteDescription = resultSetTestSuite.getString("ts_name");
							Debug.testSuiteName = resultSetTestSuite.getString("ts_name");
							
							productId = resultSetTestSuite.getInt("product_id");
							projectId = resultSetTestSuite.getInt("project_id");
							TCDesc = resultSetTestSuite.getString("tc_description");
							strTestCaseID = resultSetTestSuite.getString("testcase_id");
							testCaseId = resultSetTestSuite.getString("id");
							
							String businessFlowNameDb = resultSetTestSuite.getString("business_flow_name");
							if (!testSuiteCreatedFlag) {
								InternalDbOperation.createTestSuiteResult(Debug.testSuiteName, Debug.testSuiteDescription, 0, 0, "IN_PROGRESS", Debug.executionId);
								testSuiteCreatedFlag = true;
							}
							startDate = GlobalLib.getConvertedDateString( new Date(), "MM/dd/yyyy HH:mm:ss");
							Debug.testStepStatus = "Pass";
							
							// Set Results filename
							String resultStamp = GlobalLib.getResultStamp();
							String strTestCaseFile = "Result_TestCase_"+ strTestCaseID + "_" + resultStamp + ".html";

							// initiate TestCase Results file
							testCaseFileName = GlobalLib.setResultFile("TestCase", strTestCaseFile + ":"+ strTestCaseID);
							testCaseFilePath = System.getProperty("user.dir") + "\\Results\\" + testCaseFileName;

							// create directory to store images
							imageFolder = System.getProperty("user.dir") + "\\Results\\" + strTestCaseID + "_" + resultStamp + "\\";
							new File(imageFolder).mkdir();
							imageFileName = strTestCaseID + "_" + resultStamp + "\\";
							// @todo move it to up to avoid extra blank files
							ResultSet resultSetTestCase = InternalDbOperation.getTestCaseDataByTestCaseId(testCaseId);
							Debug.stepFailFlag = false;
							System.out.println("### Testcase Id : " + strTestCaseID);
                            System.out.println("### Testcase Description : " + TCDesc);
                            InternalDbOperation.createTestCaseResult(TCDesc, "IN_PROGRESS",strTestCaseID, businessFlowNameDb, testCaseId);
							
                            while (resultSetTestCase.next()) {
								try {
									description = resultSetTestCase.getString("description");
									skipFlag = resultSetTestCase.getString("skip_step");
									keyword = resultSetTestCase.getString("keyword_name");
									objectName = resultSetTestCase.getString("object_repository_name");
									value = resultSetTestCase.getString("test_data_value");
									String businessFlowNameMain = resultSetTestCase.getString("business_flow_name");
									
									System.out.println("#### Row : " + resultSetTestCase.getFetchSize());
									System.out.println("### Keyword : " + keyword);
									System.out.println("### Object/Soap UI Step: " + objectName);
									System.out.println("### Value : " + value);
									System.out.println("## Business flow name : " + businessFlowNameMain);
									
									if (null != value && !"".equals(value)) {
										InternalDbOperation.createTestStepResult("", keyword, objectName, "IN_PROGRESS", description, value, 0);
									}
									
									strTcResult = GlobalLib.testStepRunner(null, description, skipFlag, keyword, objectName, value, allObjects, null, testCaseFilePath, 5, imageFolder);
									
									if (strTcResult.equalsIgnoreCase("Fail")) {
									    Debug.stepFailFlag = true;
	                                    break;
	                                }
									if (keyword.equalsIgnoreCase("CALL_TEST_CASE") && !value.isEmpty()) {
	                                    String[] subTestDatas = value.split(Separator.SEPARATOR_DOUBLE_EQUALTO);
	                                    for (String subTestData : subTestDatas) {
	                                        String[] subgrp = subTestData.split(Separator.SEPARATOR_DOUBLE_HASH);
	                                        if (subgrp.length != 2) {
	                                        	Debug.stepFailFlag = true;
	                                        	Debug.traceMessage = "Invalid paramter " + value;
	                                        	strTcResult = "Fail";
	                                        	if (!"".equals(value)) {
	                                        		InternalDbOperation.updateTestStepResult(strTcResult.toUpperCase());
	                                        	}
	                                            break;
	                                        }
	                                        String businessFlowName = subgrp[0];
	                                        String testCaseIdSubGroup = subgrp[1];
		                                    int businessFlowId = InternalDbOperation.getBusinessFlowByName(businessFlowName, productId, projectId);
										    int subGroupTestcaseId = InternalDbOperation.getTestCaseByTestCaseIdAndBusinessFlowId(testCaseIdSubGroup, businessFlowId);
										    
		                                    
		                                    ResultSet resultSetTestCaseSubGroup = InternalDbOperation.getTestCaseDataByTestCaseId(String.valueOf(subGroupTestcaseId));
	
		                                    while (resultSetTestCaseSubGroup.next()) {
		                                        String descriptionSubTestcase = resultSetTestCaseSubGroup.getString("description");
		                                        String skipFlagSubTestcase = resultSetTestCaseSubGroup.getString("skip_step");
		                                        String keywordSubTestcase = resultSetTestCaseSubGroup.getString("keyword_name");
		                                        String objectNameSubTestcase = resultSetTestCaseSubGroup.getString("object_repository_name");
		                                        String valueSubTestcase = resultSetTestCaseSubGroup.getString("test_data_value");
		                                        System.out.println("#### Row : " + resultSetTestCaseSubGroup .getFetchSize());
		                                        System.out.println("### Keyword : " + keywordSubTestcase);
		                                        System.out.println("### Object/Soap UI Step: " + objectNameSubTestcase);
		                                        System.out.println("### Value : " + valueSubTestcase);
		                                        descriptionSubTestcase = "Sub Testcase Id : " + testCaseIdSubGroup + "<br>" + descriptionSubTestcase;
		                                        
		                                        if (!"".equals(valueSubTestcase)) {
		        									InternalDbOperation.createTestStepResult("", keywordSubTestcase, objectNameSubTestcase, "IN_PROGRESS", descriptionSubTestcase, valueSubTestcase, 0);
		        								}
		                                        
		                                        strTcResult = GlobalLib.testStepRunner(null, descriptionSubTestcase, skipFlagSubTestcase, keywordSubTestcase, objectNameSubTestcase, valueSubTestcase, allObjects, null, testCaseFilePath, 5, imageFolder);
		                                    	
		                                        if (!"".equals(valueSubTestcase)) {
		                                    		InternalDbOperation.updateTestStepResult(strTcResult.toUpperCase());
		                                    	}
		                                    	
		                                        if (strTcResult.equalsIgnoreCase("Fail")) {
		                                            Debug.stepFailFlag = true;
		                                            break;
		                                        }
		                                    }
		                                    if (strTcResult.equalsIgnoreCase("Fail")) {
		                                        Debug.stepFailFlag = true;
		                                        break;
		                                    }
		                                }
										
										if (strTcResult.equalsIgnoreCase("Fail")) {
										    Debug.stepFailFlag = true;
											break;
										}
									}
								} catch (Exception e) {
									Debug.stepFailFlag = true;
									
									ProvisoException.exceptionHandler(e, null);
									break;
								}
								
							}
							strResult = Debug.testStepStatus;

							System.out.println("Test Case Status: " + strResult);
							String endDate = GlobalLib.getConvertedDateString( new Date(), "MM/dd/yyyy HH:mm:ss");

							// get time difference
							String timeDiff = GlobalLib.getTimeDiff(startDate, endDate);
							
							if (Debug.stepFailFlag) {
								strResult = "Fail";
								Debug.tcFailFlag = true;
							}

							switch (strResult.toUpperCase()) {
								case "PASS":
									Debug.tcPassCount = Debug.tcPassCount + 1;
									System.out.println("Test case Pass count = " + Debug.tcPassCount);
									break;
	
								case "FAIL":
									Debug.tcFailCount = Debug.tcFailCount + 1;
									System.out.println("Test case Fail count = " + Debug.tcFailCount);
									break;
	
								default:
									System.out.println("No result set for tc: " + strTestCaseID);
							}

							InternalDbOperation.updateTestCaseResult(startDate, endDate, strResult);
							
							Debug.grpResultFilePath = groupFilePath;
							
							try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(groupFilePath, true)))) {
								out.println("<tr bgcolor = #FFFFFF>");
								out.println("<td width=400><p align=center><b><font face=Verdana size=2 color=#FF0000><a href=\"" + testCaseFileName + "\" target=\"_blank\">" + strTestCaseID + "</a> </href></font></b></td>");
								out.println("<td width=400><p align=center><font face=Verdana size=2>" + businessFlowNameDb + "</td>");
								out.println("<td width=400><p align=center><font face=Verdana size=2>" + TCDesc + "</td>");
								out.println("<td width=400><p align=center><b><font face=Verdana size=2 color=#FF0000><a href=\"" + imageFolder + "\" target=\"_blank\">" + strResult + "</a> </href></font></b></td>");
								out.println("<td width=400><p align=center><font face=Verdana size=2>" + startDate + "</td>");
								out.println("<td width=400><p align=center><font face=Verdana size=2>" + endDate + "</td>");
								out.println("<td width=400><p align=center><font face=Verdana size=2>" + timeDiff + "</td>");
								out.println("</td></tr>");
								out.close();
							} catch (IOException e) {
								e.printStackTrace();
								System.out.println(e);
							}
						}
						
						break;

					case "EXCEL":
						String businessFlowNameExcel = "";
						// ################## TEST STEP EXECUTION STARTS
						Sheet AFWSheet = file.readExcel(
								System.getProperty("user.dir") + "\\",
								"/testCases/" + GroupName + ".xlsx",
								strTestCase);

						Debug.tcRowCount = AFWSheet.getLastRowNum()
								- AFWSheet.getFirstRowNum();

						int columnCount = AFWSheet.getRow(1).getLastCellNum();

						// Start TestSuit loop
						// #################### Set Value Column
						for (int valueColumn = 5; valueColumn < columnCount; ++valueColumn) {
							Debug.testStepStatus = "Pass";
							strTestCaseID = GlobalLib
									.getCellValueAsString(AFWSheet.getRow(1)
											.getCell((short) valueColumn));
							
							TCDesc = GlobalLib
                                .getCellValueAsString(AFWSheet.getRow(3)
                                        .getCell((short) valueColumn));

							System.out.println("Running Test Case: "
									+ strTestCaseID);

							String strTestCaseFlag = GlobalLib
									.getCellValueAsString(AFWSheet.getRow(2)
											.getCell((short) valueColumn));

							DateFormat dateFormat = new SimpleDateFormat(
									"MM/dd/yyyy HH:mm:ss");


							Date sDate = new Date();
							startDate = dateFormat.format(sDate);

							// Set Results filename
							String resultStamp = GlobalLib.getResultStamp();
							String strTestCaseFile = "Result_TestCase_"
									+ strTestCaseID + "_" + resultStamp
									+ ".html";

							// initiate TestCase Results file
							
							if (!strTestCaseID.contains(Separator.SEPARATOR_DOUBLE_HASH)) {
							    testCaseFileName = GlobalLib.setResultFile(
                                    "TestCase", strTestCaseFile + ":"
                                            + strTestCaseID);
							    testCaseFilePath = System.getProperty("user.dir")
                                    + "\\Results\\" + testCaseFileName;
							    imageFolder = System.getProperty("user.dir")
                                    + "\\Results\\" + strTestCaseID + "_"
                                    + resultStamp + "\\";
                                    new File(imageFolder).mkdir();
                                    imageFileName = strTestCaseID + "_" + resultStamp
                                    + "\\";
							}
							

							// create directory to store images
							
							// @todo move it to up to avoid extra blank files
							if (!strTestCaseFlag.toUpperCase()
									.equalsIgnoreCase("Y")) {

								continue;
							}
							System.out.println("### Testcase Id : " + strTestCaseID);
                            System.out.println("### Testcase Description : " + TCDesc);
                            
							for (Debug.tcStepIterator = 3; Debug.tcStepIterator <= Debug.tcRowCount; ++Debug.tcStepIterator) {
								strTcResult = "Pass";
								businessFlowNameExcel  = AFWSheet.getSheetName(); 
								xlRow = AFWSheet.getRow(Debug.tcStepIterator);
								description = GlobalLib
										.getCellValueAsString(xlRow
												.getCell((short) 0));
								skipFlag = GlobalLib.getCellValueAsString(xlRow
										.getCell((short) 1));

								keyword = GlobalLib.getCellValueAsString(xlRow
										.getCell((short) 3));
								objectName = GlobalLib
										.getCellValueAsString(xlRow
												.getCell((short) 4));
								value = GlobalLib.getCellValueAsString(xlRow
										.getCell((short) valueColumn));

								System.out.println("#### Row : "
										+ Debug.tcStepIterator);
								System.out.println("### Keyword : " + keyword);
								System.out
										.println("### Object/Soap UI Step: " + objectName);
								System.out.println("### Value : " + value);

								// ################ Run test steps from
								// SubGroupData
								if (keyword.equalsIgnoreCase("GETSUBDATA")
										&& !value.isEmpty()) {

									// Split Value
									// int suBi = startRow,
									String[] subgrp = objectName.split(",");
									String subDataSheetName = subgrp[0];
									int startRow = Integer.parseInt(subgrp[1]) - 1;
									int endRow = Integer.parseInt(subgrp[2]);
									Debug.tcSubEndRow = endRow;
									// Read SubData sheet
									// ------------------------------

									if (startRow < 3) {

										startRow = 3;

									}
									Sheet SubDATASheet = file.readExcel(
											System.getProperty("user.dir")
													+ "\\", "/testCases/"
													+ GroupName + ".xlsx",
											subDataSheetName);

									int subColumnCount = SubDATASheet.getRow(1)
											.getLastCellNum();

									// #################### Set SubData Value
									// Column
									for (int subValueColumn = 5; subValueColumn < subColumnCount; ++subValueColumn) {

										String subStrTestCaseID = GlobalLib
												.getCellValueAsString(SubDATASheet
														.getRow(1)
														.getCell(
																(short) subValueColumn));

										String subRunFlag = GlobalLib
												.getCellValueAsString(SubDATASheet
														.getRow(2)
														.getCell(
																(short) subValueColumn));

										if (subStrTestCaseID
												.equalsIgnoreCase(strTestCaseID)
												&& subRunFlag
														.equalsIgnoreCase("Y")) {

											// #################### Iterate
											// through
											// SubData rows
											for (Debug.tcSubStepIterator = startRow; Debug.tcSubStepIterator < Debug.tcSubEndRow; Debug.tcSubStepIterator++) {
												Row subXlRow = SubDATASheet
														.getRow(Debug.tcSubStepIterator);
												String suBdescription = GlobalLib
														.getCellValueAsString(subXlRow
																.getCell((short) 0));
												String suBskipFlag = GlobalLib
														.getCellValueAsString(subXlRow
																.getCell((short) 1));
												String suBkeyword = GlobalLib
														.getCellValueAsString(subXlRow
																.getCell((short) 3));
												String suBobjectName = GlobalLib
														.getCellValueAsString(subXlRow
																.getCell((short) 4));
												String suBvalue = GlobalLib
														.getCellValueAsString(subXlRow
																.getCell((short) subValueColumn))
														+ "";

												System.out
														.println("#### Sub Row : "
																+ Debug.tcSubStepIterator);
												System.out
														.println("### Sub Keyword : "
																+ suBkeyword);
												strTcResult = GlobalLib
														.testStepRunner(
																subXlRow,
																suBdescription,
																suBskipFlag,
																suBkeyword,
																suBobjectName,
																suBvalue,
																allObjects,
																file,
																testCaseFilePath,
																subValueColumn,
																imageFolder);
												// ## Execute Test Step
											}
										}
										if (strTcResult
												.equalsIgnoreCase("Fail")) {
											break;

										}

									}

                                } else if (keyword
                                    .equalsIgnoreCase("CALL_TEST_CASE")
                                    && !value.isEmpty()) {
                                    String[] subTestDatas = value.split(Separator.SEPARATOR_DOUBLE_EQUALTO);
                                    for (String subTestData : subTestDatas) {
                                        String[] subgrp = subTestData
                                            .split(Separator.SEPARATOR_DOUBLE_HASH);
                                        if (subgrp.length < 2) {
                                        	throw new ProvisoException("Invalid test data");
                                        }
                                        String subDataSheetName = subgrp[0];
                                        String subTestCaseIdToExecute = subgrp[1];
                                        int startRow = 3;

                                        Sheet SubDATASheet = file.readExcel(
                                            System.getProperty("user.dir") + "\\",
                                            "/testCases/" + GroupName + ".xlsx",
                                            subDataSheetName);

                                        int totalRows = SubDATASheet
                                            .getLastRowNum()
                                            - SubDATASheet.getFirstRowNum();
                                        Debug.tcSubEndRow = totalRows;

                                        int subColumnCount = SubDATASheet.getRow(1)
                                            .getLastCellNum();
                                        
                                        for (int subValueColumn = 5; subValueColumn < subColumnCount; ++subValueColumn) {

                                            String subStrTestCaseID = GlobalLib
                                                .getCellValueAsString(SubDATASheet
                                                    .getRow(1).getCell(
                                                        (short) subValueColumn));
                                            if (!subTestCaseIdToExecute
                                                .equals(subStrTestCaseID)) {
                                                continue;
                                            }

                                            for (Debug.tcSubStepIterator = startRow; Debug.tcSubStepIterator < Debug.tcSubEndRow; Debug.tcSubStepIterator++) {
                                                Row subXlRow = SubDATASheet
                                                    .getRow(Debug.tcSubStepIterator);
                                                String suBdescription = GlobalLib
                                                    .getCellValueAsString(subXlRow
                                                        .getCell((short) 0));
                                                suBdescription = " Sub Testcase Id : " + subStrTestCaseID +"<br>"+ suBdescription;
                                                String suBskipFlag = GlobalLib
                                                    .getCellValueAsString(subXlRow
                                                        .getCell((short) 1));

                                                String suBkeyword = GlobalLib
                                                    .getCellValueAsString(subXlRow
                                                        .getCell((short) 3));
                                                String suBobjectName = GlobalLib
                                                    .getCellValueAsString(subXlRow
                                                        .getCell((short) 4));
                                                String suBvalue = GlobalLib
                                                    .getCellValueAsString(subXlRow
                                                        .getCell((short) subValueColumn))
                                                    + "";

                                                System.out
                                                    .println("#### Sub Row : "
                                                        + Debug.tcSubStepIterator);
                                                System.out
                                                    .println("### Sub Keyword : "
                                                        + suBkeyword);
                                                strTcResult = GlobalLib
                                                    .testStepRunner(subXlRow,
                                                        suBdescription,
                                                        suBskipFlag, suBkeyword,
                                                        suBobjectName, suBvalue,
                                                        allObjects, file,
                                                        testCaseFilePath,
                                                        subValueColumn, imageFolder);
                                                if (strTcResult
                                                    .equalsIgnoreCase("Fail")) {
                                                    break;

                                                }

                                            }
                                            
                                            // }
                                            if (strTcResult
                                                .equalsIgnoreCase("Fail")) {
                                                break;

                                            }
                                    }
                                    if (strTcResult
                                        .equalsIgnoreCase("Fail")) {
                                        break;

                                    }
                                }
                                    
                            } else {
									strTcResult = GlobalLib.testStepRunner(
											xlRow, description, skipFlag,
											keyword, objectName, value,
											allObjects, file, testCaseFilePath,
											valueColumn, imageFolder);
								}

								// set the group result Fail if any testcase
								// fails
								
								if (strTcResult.equalsIgnoreCase("Fail")) {
                                    Debug.testStepStatus = "Fail";
                                    Debug.tcFailFlag = true;
                                    break;
                                }
								System.gc();
							}

							strResult = Debug.testStepStatus;

							System.out
									.println("Test Case Status: " + strResult);
							switch (strResult.toUpperCase()) {

							case "PASS":

								Debug.tcPassCount = Debug.tcPassCount + 1;
								System.out.println("Test case Pass count = "
										+ Debug.tcPassCount);
								break;

							case "FAIL":

								Debug.tcFailCount = Debug.tcFailCount + 1;
								System.out.println("Test case Fail count = "
										+ Debug.tcFailCount);
								break;

							default:

								System.out.println("No result set for tc: "
										+ strTestCaseID);

							}
							// Move images to tc dir

							

							
							Date eDate = new Date();
							String endDate = dateFormat.format(eDate);

							// get time difference
							String timeDiff = GlobalLib.getTimeDiff(startDate,
									endDate);
							TestcaseResult testcaseResult = new TestcaseResult();
							testcaseResult.setDecription(TCDesc);
							testcaseResult.setDuration(timeDiff);
							testcaseResult.setResult(strResult);
							testcaseResult.setTestcaseId(strTestCaseID);
							testcaseResult.setStartTime(startDate);
							testcaseResult.setEndTime(endDate);
							testcaseResult.setStepResults(Debug.stepResults);
							Debug.testcaseResults.add(testcaseResult);
							Debug.stepResults = new LinkedHashSet<StepResult>();

							Debug.grpResultFilePath = groupFilePath;
							if (strTestCaseID.contains(Separator.SEPARATOR_DOUBLE_HASH)) {
							    continue;
							}
							try (PrintWriter out = new PrintWriter(
									new BufferedWriter(new FileWriter(
											groupFilePath, true)))) {
								out.println("<tr bgcolor = #FFFFFF>");
								out.println("<td width=400><p align=center><b><font face=Verdana size=2 color=#FF0000><a href=\""
										+ testCaseFileName
										+ "\" target=\"_blank\">"
										+ strTestCaseID
										+ "</a> </href></font></b></td>");
								out.println("<td width=400><p align=center><font face=Verdana size=2>"
										+ businessFlowNameExcel + "</td>");
								out.println("<td width=400><p align=center><font face=Verdana size=2>"
										+ TCDesc + "</td>");
								
								out.println("<td width=400><p align=center><b><font face=Verdana size=2 color=#FF0000><a href=\""
										+ imageFolder
										+ "\" target=\"_blank\">"
										+ strResult
										+ "</a> </href></font></b></td>");
								out.println("<td width=400><p align=center><font face=Verdana size=2>"
										+ startDate + "</td>");
								out.println("<td width=400><p align=center><font face=Verdana size=2>"
										+ endDate + "</td>");
								out.println("<td width=400><p align=center><font face=Verdana size=2>"
										+ timeDiff + "</td>");
								out.println("</td></tr>");
								out.close();

							} catch (IOException e) {
								e.printStackTrace();
								// exception handling left as an exercise for
								// the
								System.out.println(e);
								// reader
							}

						}// ################## End Test Suite Loopbreak;
						break;
					default:
						System.out
								.println("Please define RunFrom property in Environments.properties ");
						break;

					}// End Switch

				}

			}

		} catch (Exception ee) {
			ProvisoException.exceptionHandler(ee, null);
			ScreenshotUtility.ScreenShot(imageFolder);
			Debug.tcFailFlag = true;
		}

		if (Debug.tcFailFlag) {
			strResult = "Fail";
		}

		return strResult;
	}
}
