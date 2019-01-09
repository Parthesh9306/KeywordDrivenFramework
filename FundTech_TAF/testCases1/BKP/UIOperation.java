package operation;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.ui.Select;

import textVerifier.TextVerifier;
import Utility.Generator;
import Utility.GlobalLib;
import Utility.ProvisoException;
import constants.Separator;

public class UIOperation {
	
	WebDriver driver;
	String[] parts = null;
	String[] rParts = null;
	String searchText = null;
	int columnNumber, rowIndex, colIndex = 0;
	WebElement row = null;
	int listOfUniqueReferenceNumber;
	int pollingInterval = 0;
	int pollingCount = 1;
	String ColumnName = null;
	String[] subParts = null;

	// Constructor
	public UIOperation(WebDriver driver) {
		this.driver = driver;
	}

	public UIOperation() {
		// TODO Auto-generated constructor stub
	}

	/***
	 * perform: Action performed on element for UI (KEYWORDS ACTIONS)
	 * 
	 * @return: p (OR from the Property file)
	 * @param: operation: Keywords
	 * @param:objectName: Keywords Object
	 * @param Value
	 *            : Object values
	 * 
	 * ****/

	public String perform(Properties p, String operation, String objectName,
			String value, String imageFolder) throws Exception {
		try {
			DbOperation dbOperation = new DbOperation();
		    QueueOperation queueOperation = new QueueOperation();
	        objectName = objectName.trim();
	        ReadObject object = new ReadObject();
	        TextVerifier textVerifier = new TextVerifier();
	        Properties allEnvObjects = object.getObjectRepository("environment");
	        XMLOperation xmlOperation = new XMLOperation();
	        int maxTimeout = Integer.parseInt(allEnvObjects
	                .getProperty("maxTimeout"));
	        String HardStopOnTextVerification = allEnvObjects
	                .getProperty("HardStopOnTextVerification");
	        System.out.println("");
	        String actualValue, strResult = "Pass";
	        PropertyFileOperation propertyFileOperation = new PropertyFileOperation();
	        Generator generator = new Generator();
	        int dbPollingDuration = Integer.parseInt(allEnvObjects.getProperty("dbPollingDuration"));
	        int dbPollingMaxAttempt = Integer.parseInt(allEnvObjects.getProperty("dbPollingMaxAttempt"));
	        value = GlobalLib.replacePropertiesFromString(value);
	        
			switch (operation.toUpperCase()) {

			case "DEBUG":
				if (objectName.equalsIgnoreCase("ON")) {
					Debug.Flag = true;
				} else if (objectName.equalsIgnoreCase("OFF")) {
					Debug.Flag = false;
				}
				strResult = "Pass";

				break;

			case "NAVIGATETO":
				GlobalLib.navigateTo(p, objectName, driver, maxTimeout);
				Thread.sleep(Debug.iSleepMin);
				strResult = "Pass";
				break;

			case "GRIDTABLECLICKONCELL":
				Thread.sleep(Debug.iSleepMin);
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));

				parts = value.split(Separator.SEPARATOR_COMMA);
				searchText = GlobalLib.getPropertyOrValue(parts[0]);
				setDebugValueIsProperty(value, searchText);
				colIndex = Integer.parseInt(parts[1]);
				columnNumber = Integer.parseInt(parts[2]);
				rowIndex = GlobalLib.getRow2(searchText, p, objectName, driver,
						colIndex);
				strResult = GlobalLib.clickOnTableCell(rowIndex, columnNumber,
						p, objectName, driver);
				Debug.traceMessage = Debug.traceMessage + "Search Text : " + searchText + "<br> Column Number : " + columnNumber + "<br> Row Number : " + rowIndex;
				strResult = "Pass";
				break;
				
			case "GRIDTABLECLICKONCELLMEPS":
				Thread.sleep(Debug.iSleepMin);
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				parts = value.split(Separator.SEPARATOR_COMMA);
				searchText = parts[0];
				colIndex = Integer.parseInt(parts[1]);
				columnNumber = Integer.parseInt(parts[2]);
				rowIndex = GlobalLib.getRow(searchText, p, objectName, driver,
						colIndex);
				strResult = GlobalLib.clickOnTableCellForGPP(rowIndex, columnNumber,
						p, objectName, driver);

				strResult = "Pass";
				break;

			case "GRIDTABLESELECTINCELL":
				Thread.sleep(Debug.iSleepMin);
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				parts = value.split(Separator.SEPARATOR_COMMA);

				if(parts[0].contains("Prop")){
					String[] property = parts[0].split(Separator.SEPARATOR_UNDERSCORE);
					searchText = propertyFileOperation.readProperty(property[1]);
				}else{
					searchText = parts[0];
				}				
				colIndex = Integer.parseInt(parts[1]);
				String selectValue = parts[2];
				columnNumber = Integer.parseInt(parts[3]);
				rowIndex = GlobalLib.getRow(searchText, p, objectName, driver,
						colIndex);
				Debug.traceMessage = Debug.traceMessage + "Search Text : " + searchText + "<br> Column Number : " + columnNumber + "<br> Row Number : " + rowIndex;
				strResult = GlobalLib.selectInTableCell(rowIndex, columnNumber,
						selectValue, p, objectName, driver);

				strResult = "Pass";

				break;

			case "GRIDTABLECLICKONCELLFROMVAR":
				Thread.sleep(Debug.iSleepMin);
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				parts = value.split(Separator.SEPARATOR_COMMA);

				if(parts[0].contains("Prop")){
					String[] property = parts[0].split(Separator.SEPARATOR_UNDERSCORE);
					searchText = propertyFileOperation.readProperty(property[1]);
					value = searchText;
				}else{
					searchText = parts[0];
					value = Debug.getListValue(Integer.parseInt(searchText.trim()) - 1);
				}	
				colIndex = Integer.parseInt(parts[1]);
				columnNumber = Integer.parseInt(parts[2]);
				if(!(parts[0].contains("Prop"))){
					if (Integer.parseInt(searchText.trim()) - 1 > Debug.listOfUniqueValues
						.size()) {
						throw new ProvisoException("Value for variable is not set yet.");
					}
				}
				rowIndex = GlobalLib.getRow(value, p, objectName,
						driver, colIndex);
				Debug.traceMessage = Debug.traceMessage + "Search Text : " + searchText + "<br> Column Number : " + columnNumber + "<br> Row Number : " + rowIndex;
				strResult = GlobalLib.clickOnTableCellRow(rowIndex,
						columnNumber, p, objectName, driver);				
				break;

			case "GRIDTABLESELECTINCELLFROMVAR":
				Thread.sleep(Debug.iSleepMin);
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				parts = value.split(Separator.SEPARATOR_COMMA);
				if(parts[0].contains("Prop")){
					String[] property = parts[0].split(Separator.SEPARATOR_UNDERSCORE);
					searchText = propertyFileOperation.readProperty(property[1]);
					value = searchText;
				}else{
					searchText = parts[0];
					value = Debug.getListValue(Integer.parseInt(searchText.trim()) - 1);
				}	
				colIndex = Integer.parseInt(parts[1]);
				selectValue = parts[2];
				columnNumber = Integer.parseInt(parts[3]);
				if(!(parts[0].contains("Prop"))){
					if (Integer.parseInt(searchText.trim()) - 1 > Debug.listOfUniqueValues
						.size()) {
					    throw new ProvisoException("Value for variable is not set yet.");
					}
				}
				rowIndex = GlobalLib.getRow(value, p, objectName,
						driver, colIndex);
				Debug.traceMessage = Debug.traceMessage + "Search Text : " + searchText + "<br> Column Number : " + columnNumber + "<br> Row Number : " + rowIndex;
				strResult = GlobalLib.selectInTableCell(rowIndex,
						columnNumber, selectValue, p, objectName, driver);

				strResult = "Pass";
				break;

			case "GETTEXTINTOVAR":
				String[] refText = value.split(Separator.SEPARATOR_COMMA);
				String startWith = refText[0];
				String endWith = refText[1];
				listOfUniqueReferenceNumber = Integer.parseInt(refText[2]
						.trim()) - 1;
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				System.out.println(Debug.listOfUniqueValues.size());
				value = driver.findElement(
						this.getObject(p, objectName)).getText();
				
				if (!startWith.isEmpty() && !endWith.isEmpty()) {
					value = GlobalLib.getStringPattern(startWith, endWith, value);
				}

				if (value != null) {
					Debug.setListValue(listOfUniqueReferenceNumber, value);
					Debug.VALUE = value; 
					strResult = "Pass";					
				} else {
					strResult = "Fail";
				}
				break;

			case "SAVETEXTINTOVAR":
				parts = value.split(Separator.SEPARATOR_COMMA);
				String propName = "";
				GlobalLib.waitForElementVisible(driver, maxTimeout, this.getObject(p, objectName));
				String capturedValue = driver.findElement(this.getObject(p, objectName)).getText();
				System.out.println(capturedValue);
				if (parts.length > 1) {
					String startWithF = parts[0];
					String endWithF = parts[1];
					propName = parts[2];
					capturedValue = GlobalLib.getStringPatternGen(startWithF, endWithF,capturedValue);
				} else {
					propName = parts[0];
				}
				
				propertyFileOperation.writeProperty(propName + "," + capturedValue);
				Debug.VALUE = capturedValue;
				//System.out.println(capturedValue);
				break;

			case "GETTEXTINTOVARFROMHIDDENFIELD":
				WebElement hiddenInput = driver.findElement(this.getObject(p,
						objectName));
				String hiddenValue = hiddenInput.getAttribute("value");

				listOfUniqueReferenceNumber = Integer.parseInt(value) - 1;
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				System.out.println(hiddenValue);
				value = GlobalLib.getStringPattern("PRIMARY_KEY=", ",",
						hiddenValue);
				if (value != null) {
					Debug.setListValue(listOfUniqueReferenceNumber, value);					
				}
				Debug.traceMessage = Debug.traceMessage + "Hidden Text: " + hiddenValue;
				
				strResult = "Pass";
				break;

			case "GETTEXTINTOVARFROMHIDF":
				WebElement hiddenIs = driver.findElement(this.getObject(p,
						objectName));
				String hiddenVals = hiddenIs.getAttribute("value");

				listOfUniqueReferenceNumber = Integer.parseInt(value) - 1;
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				System.out.println(hiddenVals);
				value = hiddenVals;
				if (value != null) {
					Debug.setListValue(listOfUniqueReferenceNumber, value);					
				}
				System.out.println("Hidden Text: " + hiddenVals);
				strResult = "Pass";
				break;

			case "SETTEXTFROMVAR":

				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				if (Integer.parseInt(value.trim()) > Debug.listOfUniqueValues
						.size()) {
				    throw new ProvisoException("Value for variable is not set yet.");
				} else {
					value = Debug.getListValue(Integer.parseInt(value.trim()) - 1);
					strResult = setTextByClear(p,objectName, driver, value);					
				}
				break;

			case "SETTEXTFROMPROPERTY":

				String propertyValue = propertyFileOperation
						.readProperty(value);

				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				if (propertyValue.isEmpty()) {
					System.out.println("Value for variable is not set yet.");
					strResult = "Fail";
				} else {
					strResult = setTextByClear(p,objectName, driver, propertyValue);						
					strResult = "Pass";
				}
				break;

			case "CLICKLINKFROMVAR":
				if(value.contains("Prop")){
					String[] property = value.split(Separator.SEPARATOR_UNDERSCORE);
					value = propertyFileOperation.readProperty(property[1]);
				}else{
					System.out.println(Debug.listOfUniqueValues.size());
					value = Debug.getListValue(Integer.parseInt(value.trim()) - 1);
				}				
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						By.linkText(value));
				driver.findElement(By.linkText(value)).click();
				strResult = "Pass";
				break;

			case "CLICKLINKTEXT":
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						By.linkText(value));
				driver.findElement(By.linkText(value)).click();
				strResult = "Pass";
				break;

			case "CLICKOBJECTBYTEXT":
				parts = p.getProperty(objectName).split(Separator.SEPARATOR_COLON);
				String objType = parts[0];
				String objAttribute = parts[1];
				String cssText = objType + "[" + objAttribute + "='" + value
						+ "']";
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						By.cssSelector(cssText));
				driver.findElement(By.cssSelector(cssText)).click();
				strResult = "Pass";
				break;

			case "CLICKRADIOBYTEXT":

				GlobalLib.waitForElementVisible(driver, maxTimeout,
						By.xpath("//input[contains(.,'" + value + "')]"));
				driver.findElement(
						By.xpath("//input[contains(.,'" + value + "')]"))
						.click();
				strResult = "Pass";
				break;

			case "DROPDOWNLISTVALUE":
				Thread.sleep(Debug.iSleepMin);
				value = GlobalLib.getPropertyOrValue(value);
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				GlobalLib.waitForElementEnable(driver, maxTimeout,
						this.getObject(p, objectName));
				Select dropdown = new Select(driver.findElement(this.getObject(
						p, objectName)));
				dropdown.selectByVisibleText(value);
				
				strResult = "Pass";
				break;

			case "DROPDOWNLISTTEXT":
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				Select dropdown1 = new Select(driver.findElement(this
						.getObject(p, objectName)));
				dropdown1.selectByValue(value);
				Thread.sleep(Debug.iSleepMin);
				strResult = "Pass";
				break;

			case "CLICK":
				Thread.sleep(Debug.iSleepMin);
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				GlobalLib.waitForElementEnable(driver, maxTimeout,
						this.getObject(p, objectName));
				if (driver.findElement(this.getObject(p, objectName))
						.isEnabled()) {
					driver.findElement(this.getObject(p, objectName)).click();
					strResult = "Pass";
				} else {
					strResult = "Fail";
				}
				break;

			case "MOUSEOVER":
				WebElement searchBtn = driver.findElement(this.getObject(p,objectName));
				Actions action1 = new Actions(driver);
				action1.moveToElement(searchBtn).perform();
				Thread.sleep(Debug.iSleepMin);
				break;

			case "WAITFORJSLOAD":
				Thread.sleep(Debug.iSleepMin);
				GlobalLib.waitForJsLoad(driver, maxTimeout);
				Thread.sleep(Debug.iSleepMid);
				break;

			case "SETTEXT":
				Thread.sleep(Debug.iSleepMin);
				value = GlobalLib.getPropertyOrValue(value);
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				GlobalLib.waitForElementEnable(driver, maxTimeout,
						this.getObject(p, objectName));
				driver.findElement(this.getObject(p, objectName)).click();
				// Changed by nazim to clear the text
				strResult = setTextByClear(p,objectName, driver, value);					
				strResult = "Pass";
				Debug.VALUE = value; 
				break;
			       
			case "GOTOURL":
				String url = p.getProperty(value);
				if(url == null){
					url = value;
				}
				if (GlobalLib.linkExists(url)) {
					driver.get(url);
					driver.manage().timeouts()
						.implicitlyWait(maxTimeout, TimeUnit.SECONDS);
					strResult = "Pass";
				}else{
					strResult = "Fail";
					throw new IOException("URL is not working...");
				}
				
				break;

			case "VERIFYTEXTTRUE":
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				value = GlobalLib.getPropertyOrValue(value);
				Thread.sleep(Debug.iSleepMin);
				actualValue = driver.findElement(this.getObject(p, objectName))
						.getText().trim();
				strResult = GlobalLib.getObjectverifySafely(actualValue, value,
						true);
				Debug.VALUE = "Test Data : " + Debug.VALUE +"<br> Verified with : " + actualValue;
 
				System.out.println(strResult);
				break;

			case "VERIFYTEXTFALSE":
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				value = GlobalLib.getPropertyOrValue(value);
				actualValue = driver.findElement(this.getObject(p, objectName))
						.getText().trim();
				strResult = GlobalLib.getObjectverifySafely(actualValue, value,
						false);
				Debug.VALUE = "Test Data : " + Debug.VALUE +"<br> Verified with : " + actualValue;
				System.out.println(strResult);
				break;

			case "NEXTWINDOW":
				Thread.sleep(Debug.iSleepMin);
				strResult = GlobalLib.multipleWindowshandle(p, objectName,
						driver, maxTimeout, value);
				strResult = "Pass";
				break;

			case "SETUPLOADFILEPATH":
				// assuming driver is a healthy WebDriver instance
				/*
				 * WebElement fileInput =
				 * driver.findElement(By.xpath("//input[@type='file']"));
				 * fileInput.sendKeys(value);
				 */
				StringSelection stringSelection = new StringSelection(value);
				Toolkit.getDefaultToolkit().getSystemClipboard()
						.setContents(stringSelection, null);
				// native key strokes for CTRL, V and ENTER keys
				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);

				strResult = "Pass";
				break;

			case "PRESSKEY":
				strResult = GlobalLib.keyPressHandle(value);
				Thread.sleep(Debug.iSleepMax);
				strResult = "Pass";
				break;

			case "PRESSKEYONELEMENT":
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));

				WebElement weKP = driver.findElement(this.getObject(p,
						objectName));
				Thread.sleep(Debug.iSleepMin);
				GlobalLib.keyPressONElement(weKP, value);
				strResult = "Pass";
				break;

			case "HANDLEPOPUP":
				parts = value.split(Separator.SEPARATOR_COMMA);
				String condition = parts[0];
				String action = parts[1];

				if (driver.findElement(this.getObject(p, condition))
						.isDisplayed()) {

					driver.findElement(this.getObject(p, action)).click();

				}
				strResult = "Pass";
				break;

			case "WAIT":
				String waitTime = allEnvObjects.getProperty(value);
				if (waitTime != null) {
					int sleepTime = Integer.parseInt(waitTime) * 1000;
					Thread.sleep(sleepTime);
					strResult = "Pass";
				} else {
				    throw new ProvisoException("Wait type not found expected MINIMUM/MODERATE/MAXIMUM");
				}
				break;

			case "NEXTGRIDTABLECLICKONCELL":
				rParts = objectName.split(Separator.SEPARATOR_COLON);
				objectName = rParts[0];
				String selectTable = rParts[1];
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				parts = value.split(Separator.SEPARATOR_COMMA);
				searchText = GlobalLib.getPropertyOrValue(parts[0]);
				setDebugValueIsProperty(value, searchText);
				colIndex = Integer.parseInt(parts[1]);
				columnNumber = Integer.parseInt(parts[2]);
				rowIndex = GlobalLib.getRow1(searchText, p, objectName, driver,
						colIndex);
				strResult = GlobalLib.clickOnTableCellChild(rowIndex + 1,
						columnNumber, p, selectTable, driver);
				Thread.sleep(Debug.iSleepMin);
				strResult = "Pass";
				break;

			case "NEXTGRIDTABLECLICKONCELL1":
				rParts = objectName.split(Separator.SEPARATOR_COLON);
				objectName = rParts[0];
				String selectTable2 = rParts[1];
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				parts = value.split(Separator.SEPARATOR_COMMA);
				searchText = GlobalLib.getPropertyOrValue(parts[0]);
				setDebugValueIsProperty(value, searchText);
				String headerText = parts[1];
				colIndex = GlobalLib.getColumn(headerText, p, objectName,
						driver);
				columnNumber = Integer.parseInt(parts[2]);
				rowIndex = GlobalLib.getRow1(searchText, p, objectName, driver,
						colIndex);
				strResult = GlobalLib.clickOnTableCellChild(rowIndex + 1,
						columnNumber, p, selectTable2, driver);
				Thread.sleep(Debug.iSleepMin);
				strResult = "Pass";
				break;

			case "NEXTGRIDTABLECLICKDOACTION1":
				rParts = objectName.split(Separator.SEPARATOR_COLON);
				objectName = rParts[0];
				String selectTable1 = rParts[1];
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				parts = value.split(Separator.SEPARATOR_COMMA);
				searchText = GlobalLib.getPropertyOrValue(parts[0]);
				setDebugValueIsProperty(value, searchText);
				String headerText3 = parts[1];
				colIndex = GlobalLib.getColumn(headerText3, p, objectName,
						driver);
				columnNumber = Integer.parseInt(parts[2]);
				rowIndex = GlobalLib.getRow1(searchText, p, objectName, driver,
						colIndex);
				strResult = GlobalLib.clickOnTableCellChild1(rowIndex + 1,
						columnNumber, p, selectTable1, driver);
				Thread.sleep(Debug.iSleepMin);
				strResult = "Pass";
				break;

			
			   case "NEXTGRIDTABLEDOACTION":
				   /*
				rParts = objectName.split(Separator.SEPARATOR_COLON);
				objectName = rParts[0];
				String selectTable4 = rParts[1];
				String objectAction=rParts[2];
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				parts = value.split(Separator.SEPARATOR_COMMA);
				searchText = GlobalLib.getPropertyOrValue(parts[0]);
				setDebugValueIsProperty(value, searchText);
				String headerText4 = parts[1];
				colIndex = GlobalLib.getColumn(headerText4, p, objectName,
						driver);
				columnNumber = Integer.parseInt(parts[2]);
				rowIndex = GlobalLib.getRow1(searchText, p, objectName, driver,
						colIndex);
				strResult = GlobalLib.clickOnNextTableCell(rowIndex + 1,
						columnNumber, p, selectTable4, objectAction, driver);
				Thread.sleep(Debug.iSleepMin);
				strResult = "Pass";
				break;
				*/
				   rParts = objectName.split(Separator.SEPARATOR_COLON);
					objectName = rParts[0];
					String selectTable5 = rParts[1];
					String objectAction1=rParts[2];
					GlobalLib.waitForElementVisible(driver, maxTimeout,
							this.getObject(p, objectName));
					parts = value.split(Separator.SEPARATOR_COMMA);
					searchText = GlobalLib.getPropertyOrValue(parts[0]);
					setDebugValueIsProperty(value, searchText);
					String headerText5 = parts[1];
					colIndex = GlobalLib.getColumn(headerText5, p, objectName,
							driver);
					columnNumber = Integer.parseInt(parts[2]);
					rowIndex = GlobalLib.getRow1(searchText, p, objectName, driver,
							colIndex);
					strResult = GlobalLib.clickOnNextTableCell2(rowIndex + 1,
							columnNumber, p, selectTable5, objectAction1, driver);
					Thread.sleep(Debug.iSleepMin);
					strResult = "Pass";
				
					break;
			
			case "NEXTGRIDTABLECLICKONEDIT":
				rParts = objectName.split(Separator.SEPARATOR_COLON);
				objectName = rParts[0];
				String editSelectTable1 = rParts[1];
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				parts = value.split(Separator.SEPARATOR_COMMA);
				searchText = GlobalLib.getPropertyOrValue(parts[0]);
				setDebugValueIsProperty(value, searchText);
				String editHeaderText3 = parts[1];
				colIndex = GlobalLib.getColumn(editHeaderText3, p, objectName,
						driver);
				columnNumber = Integer.parseInt(parts[2]);
				rowIndex = GlobalLib.getRow1(searchText, p, objectName, driver,
						colIndex);
				strResult = GlobalLib.clickOnTableCellChild2(rowIndex + 1,
						columnNumber, p, editSelectTable1, driver);
				Thread.sleep(Debug.iSleepMin);
				strResult = "Pass";
				break;

			case "NEXTGRIDTABLECLICKDOACTION":
				rParts = objectName.split(Separator.SEPARATOR_COLON);
				objectName = rParts[0];
				String selectTable3 = rParts[1];
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				parts = value.split(Separator.SEPARATOR_COMMA);
				searchText = GlobalLib.getPropertyOrValue(parts[0]);
				setDebugValueIsProperty(value, searchText);
				colIndex = Integer.parseInt(parts[1]);
				columnNumber = Integer.parseInt(parts[2]);
				rowIndex = GlobalLib.getRow1(searchText, p, objectName, driver,
						colIndex);
				strResult = GlobalLib.clickOnTableCellChild1(rowIndex + 1,
						columnNumber, p, selectTable3, driver);
				Thread.sleep(Debug.iSleepMin);
				strResult = "Pass";
				break;

			case "GRIDTABLEVERIFYTEXT":

				Thread.sleep(Debug.iSleepMin);

				if (Debug.pollingFlag) {
					pollingInterval = Debug.pollingDuration * 1000;
					pollingCount = Debug.pollingCount;
				} else {
					pollingInterval = Debug.iSleepMax;
				}

				strResult = "Pass";
				for (int i = 1; i <= pollingCount; i++) {
					if (Debug.pollingFlag) {
						driver.navigate().refresh();
						Thread.sleep(pollingInterval);
					}
					// GlobalLib.waitForElementVisible(driver, maxTimeout,
					// this.getObject(p, objectName));
					parts = value.split(Separator.SEPARATOR_COMMA);
					searchText = GlobalLib.getPropertyOrValue(parts[0]);
					colIndex = Integer.parseInt(parts[1]);
					String verifyText = parts[2];
					columnNumber = Integer.parseInt(parts[3]);
					// GlobalLib.waitForElementVisible(driver, maxTimeout,
					// this.getObject(p, objectName));
					rowIndex = GlobalLib.getRow1(searchText, p, objectName,
							driver, colIndex);
					if (rowIndex >= 0) {

						strResult = GlobalLib
								.verifyTableCellText1(rowIndex, columnNumber,
										verifyText, p, objectName, driver);

						if (strResult.equalsIgnoreCase("PASS")) {
							break;
						}
					} else {
						strResult = "Fail";

					}

				}
				Debug.pollingFlag = false;
				if (strResult.equalsIgnoreCase("FAIL")
						&& HardStopOnTextVerification.equalsIgnoreCase("ON")) {
					throw new Exception("Text Verification Failed");
				}

				break;

			case "GRIDTABLEVERIFYTEXT1":

				Thread.sleep(Debug.iSleepMin);

				if (Debug.pollingFlag) {
					pollingInterval = Debug.pollingDuration * 1000;
					pollingCount = Debug.pollingCount;
				} else {
					pollingInterval = Debug.iSleepMax;
				}

				strResult = "Pass";
				for (int i = 1; i <= pollingCount; i++) {
					if (Debug.pollingFlag) {
						driver.navigate().refresh();
						Thread.sleep(pollingInterval);
					}
					// GlobalLib.waitForElementVisible(driver, maxTimeout,
					// this.getObject(p, objectName));
					parts = value.split(Separator.SEPARATOR_COMMA);
					searchText = GlobalLib.getPropertyOrValue(parts[0]);
					setDebugValueIsProperty(value, searchText);
					String headerText1 = parts[1];
					String verifyText = parts[2];
					String headerText2 = parts[3];
					// columnNumber = Integer.parseInt(parts[3]);
					colIndex = GlobalLib.getColumn(headerText1, p, objectName,
							driver);

					// GlobalLib.waitForElementVisible(driver, maxTimeout,
					// this.getObject(p, objectName));
					rowIndex = GlobalLib.getRow1(searchText, p, objectName,
							driver, colIndex);
					if (rowIndex >= 0) {

						columnNumber = GlobalLib.getColumn(headerText2, p,
								objectName, driver);
						strResult = GlobalLib
								.verifyTableCellText1(rowIndex, columnNumber,
										verifyText, p, objectName, driver);

						if (strResult.equalsIgnoreCase("PASS")) {
	//						Debug.VALUE = "Test Data : " + Debug.VALUE +"<br> Verified with : " + verifyText;
							break;
						}
					} else {
						strResult = "Fail";

					}

				}
				Debug.pollingFlag = false;
				if (strResult.equalsIgnoreCase("FAIL")
						&& HardStopOnTextVerification.equalsIgnoreCase("ON")) {
				    throw new ProvisoException("Text Verification Failed");
				}

				break;

			case "GRIDTABLEVERIFYTEXTINCELL":
				// GlobalLib.waitForElementVisible(driver, maxTimeout, p,
				// objectName);
				Thread.sleep(1000);

				if (Debug.pollingFlag) {
					pollingInterval = Debug.pollingDuration * 1000;
					pollingCount = Debug.pollingCount;
				} else {
					pollingInterval = 5000;
				}

				strResult = "Pass";
				for (int i = 1; i <= pollingCount; i++) {
					driver.navigate().refresh();
					Thread.sleep(pollingInterval);
					// GlobalLib.waitForElementVisible(driver, maxTimeout,
					// this.getObject(p, objectName));
					parts = value.split(Separator.SEPARATOR_COMMA);
					searchText = GlobalLib.getPropertyOrValue(parts[0]);
					setDebugValueIsProperty(value, searchText);
					colIndex = Integer.parseInt(parts[1]);
					String verifyText = parts[2];
					columnNumber = Integer.parseInt(parts[3]);
					// GlobalLib.waitForElementVisible(driver, maxTimeout,
					// this.getObject(p, objectName));
					rowIndex = GlobalLib.getRow(searchText, p, objectName,
							driver, colIndex);

					strResult = GlobalLib.verifyTableCellText(rowIndex,
							columnNumber, verifyText, p, objectName, driver);

					if (strResult.equalsIgnoreCase("PASS")) {
						break;
					}
					strResult = "Pass";
				}
				Debug.pollingFlag = false;
				if (strResult.equalsIgnoreCase("FAIL")
						&& HardStopOnTextVerification.equalsIgnoreCase("ON")) {
				    throw new ProvisoException("Text Verification Failed");
				}
				break;
			case "CAPTURESCREEN":
				// kScreenshotUtility.ScreenShot(driver, imageFolder);
				strResult = "Pass";
				break;
			case "WAITFOROBJECT":
				Thread.sleep(Debug.iSleepMin);
				if (GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName))) {

					strResult = "Pass";
				} else {
					strResult = "Fail";
					throw new Exception(objectName + "Object Not Displayed");
				}
				break;
			case "WAITFOROBJECTNOTDISPLAYED":
				GlobalLib.waitForElementNotVisible(driver, maxTimeout,
						this.getObject(p, objectName));

				strResult = "Pass";
				break;

			case "WAITFOROBJECTENABLE":

				if (GlobalLib.waitForElementEnable(driver, maxTimeout,
						this.getObject(p, objectName))) {
					strResult = "Pass";
				} else {
					strResult = "Pass";
				}

				break;
			case "VERIFYOBJECTDISPLAYED":
				Debug.traceMessage = Debug.traceMessage + "Object verified : " + objectName;
				if (GlobalLib.waitForElementEnable(driver, maxTimeout,
						this.getObject(p, objectName))) {
					strResult = "Pass";
				} else {
				    throw new ProvisoException(objectName + "Object Not Displayed");
				}

				break;

			case "VERIFYOBJECTNOTDISPLAYED":

				if (GlobalLib.waitForElementEnable(driver, 10,
						this.getObject(p, objectName))) {
					strResult = "Fail";
					throw new Exception(objectName + "Object Displayed");
				} else {
					strResult = "Pass";
				}

				break;

			case "POLLING":
				rParts = value.split(Separator.SEPARATOR_COMMA);
				if (rParts[0].trim().equalsIgnoreCase("ON")) {

					Debug.pollingFlag = true;
				} else {
					Debug.pollingFlag = false;
				}
				Debug.pollingCount = Integer.parseInt(rParts[1].trim());
				Debug.pollingDuration = Integer.parseInt(rParts[2].trim());

				strResult = "Pass";
				break;

			case "DROPDOWNLISTITEM":

				objectName = objectName + "[" + value.trim() + "]";
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				WebElement listItem = driver.findElement(this.getObject(p,
						objectName));
				listItem.click();
				Thread.sleep(Debug.iSleepMin);
				strResult = "Pass";

				break;

			case "REFRESHPAGE":
				driver.navigate().refresh();
				Thread.sleep(Debug.iSleepMid);

				strResult = "Pass";
				break;

			case "UPLOADFTPFILE":
				rParts = value.split(Separator.SEPARATOR_COMMA);
				if (rParts.length != 6) {
					System.out.println("Invalid parameter send to ftp upload");
				}
				String fileNameToUpload = GlobalLib.getPropertyOrValue(rParts[0]);
                String fileToUpload  = System.getProperty("user.dir")+"\\FileToUpload\\"+fileNameToUpload;
                Debug.traceMessage = Debug.traceMessage + "File to Upload : " + fileToUpload + "<Br> Location to Upload : " + rParts[1] ;
                
				if (GlobalLib.UploadAndTest(rParts[2],
						Integer.parseInt(rParts[3]), rParts[4], rParts[5],
						rParts[1], fileToUpload)) {
					System.out.println("pass");
					strResult = "Pass";
				} else {
					System.out.println("fail");
					strResult = "Fail";
				}

				break;
			case "DOWNLOADFTPFILE":
                parts = value.split(Separator.SEPARATOR_DOUBLE_HASH);
                String downloadPath = System.getProperty("user.dir") +  File.separator + "TemplateFile" + File.separator;
                Debug.traceMessage = Debug.traceMessage + "Server path : " + downloadPath + "<br> Proviso download location : " + downloadPath;
                
                if (GlobalLib.copyFileFromServer(parts[0], Integer.parseInt(parts[1]), parts[2], parts[3], parts[4], downloadPath)) {
                    System.out.println("pass");
                    strResult = "Pass";
                } else {
                    System.out.println("fail");
                    strResult = "Fail";
                }
                break;

			case "FILEFINDNREPLACE":

				Map<String, String> replacements = new HashMap<String, String>();
				System.out.println(value);
				String[] splitExcelRowArray = value.split(Separator.SEPARATOR_HASH);
				for (int i = 1; i < splitExcelRowArray.length; i++) {
					String splitExcelRow = splitExcelRowArray[i];
					String[] splitReplacements = splitExcelRow.split(Separator.SEPARATOR_COLON);
					if (splitReplacements[1].startsWith("VAR_")) {
						splitReplacements[1] = propertyFileOperation
								.readProperty(splitReplacements[1].split(Separator.SEPARATOR_UNDERSCORE)[1]);
					}
					replacements
							.put(splitReplacements[0], splitReplacements[1]);
				}
				String[] srcDestFileArray = splitExcelRowArray[0].split(Separator.SEPARATOR_COLON);
				String srcFile = GlobalLib.getPropertyOrValue(srcDestFileArray[0]);
				String destFile = GlobalLib.getPropertyOrValue(srcDestFileArray[1]);
				
				String replacedFileName = FileOperation
						.replaceFileContentToUpload(srcFile,
								destFile,
								System.getProperty("user.dir")
										+ "\\TemplateFile\\",
								System.getProperty("user.dir")
										+ "\\FileToUpload\\", replacements);

				System.out.println(replacedFileName);

				strResult = "Pass";
				break;
			// /########## SOAP UI CHANGES
			/*
			 * case "LOADSOAPUIPROJECT": //int sleep =
			 * Integer.parseInt(value)*1000; SOAUIRunner.project = new
			 * WsdlProject( value ); Thread.sleep(10000); strResult = "Pass";
			 * break;
			 */

			case "LOADSOAPUITESTSUITE":
				// int sleep = Integer.parseInt(value)*1000;
				Thread.sleep(Debug.iSleepMid);
				SOAUIRunner.setSOAPUITestSuite(value);
				strResult = "Pass";
				break;

			case "LOADSOAPUITESTCASE":
				// int sleep = Integer.parseInt(value)*1000;
				Thread.sleep(Debug.iSleepMid);
				SOAUIRunner.setSOAPUITestCase(value);
				strResult = "Pass";
				break;

			case "RUNSOAPUISTEP":
				Thread.sleep(Debug.iSleepMid);
				strResult = SOAUIRunner.RunTestStep(SOAUIRunner.project, objectName);
				String[] propertiesToPrint = p.getProperty("testCasePropertyToPrint").split(Separator.SEPARATOR_COMMA);
				
				for (String properyToPrint : propertiesToPrint) {
					if (objectName.contains(properyToPrint)) {
						String propertyFound = SOAUIRunner
								.getSOAPUITestCaseProperty(properyToPrint);
						System.out.println(properyToPrint + "=" + propertyFound);
						Debug.VALUE = value + ", " + properyToPrint + "=" + propertyFound;
					}
				}
				
//				if (strResult.equalsIgnoreCase("FAIL")) {
//					throw new Exception("Test Step Failed - See Output.txt");
//				}

				break;

			case "VERIFYSOAPUITESTCASEPROPERTY":
				Thread.sleep(Debug.iSleepMid);
				parts = value.split(Separator.SEPARATOR_COMMA);
				String propertyName = parts[0];
				String verifyText = parts[1];
				String actualText = SOAUIRunner
						.getSOAPUITestCaseProperty(propertyName);
				System.out.println(actualText);

				if (actualText.equalsIgnoreCase(verifyText)) {
					strResult = "Pass";
				} else {
					strResult = "Fail";
				}
				break;

			case "VERIFYSOAPUITESTSTEPPROPERTY":
				Thread.sleep(Debug.iSleepMid);
				parts = value.split(Separator.SEPARATOR_COMMA);
				String propertyNameTS = parts[0];
				String verifyTextTS = parts[1];
				String actualTextTS = SOAUIRunner
						.getSOAPUITestStepProperty(propertyNameTS);
				System.out.println(actualTextTS);

				if (actualTextTS.equalsIgnoreCase(verifyTextTS)) {
					strResult = "Pass";
				} else {
					strResult = "Fail";
				}

				break;

			case "GETSOAPUITESTCASEPROPERTY":
				Thread.sleep(Debug.iSleepMid);
				parts = value.split(Separator.SEPARATOR_COMMA);
				String testCasePropertyName = parts[0];
				String variableName = parts[1];
				String testcasePropertyFetched = SOAUIRunner
						.getSOAPUITestCaseProperty(testCasePropertyName);
				System.out.println("property fetched " + testcasePropertyFetched);
				propertyFileOperation.writeProperty(variableName + ","
						+ testcasePropertyFetched);
				String variablePropertyFetched = propertyFileOperation.readProperty(variableName);
				 if (variablePropertyFetched.equalsIgnoreCase(testcasePropertyFetched)){
					 strResult = "Pass";
				 }else{
					 strResult = "Fail";
				 }
				break;
			// END SOAP UI CHNAGES
			// Read Write property file

			case "WRITEPROPERTY":
				propertyFileOperation.writeProperty(value);
				break;
			case "WRITESOAPUIMESSAGEPROPERTY":
				//propertyValue##toFind##ToReplace##xml
				parts = value.split(Separator.SEPARATOR_DOUBLE_HASH);
				String replaceValue = parts[1].replaceAll("\n", "&#xA;");
				propertyFileOperation.writeByKeyValueProperty(parts[0], replaceValue);
				strResult = "Pass";
				break;
			case "READPROPERTY":
				/**
				 * TODO: Use in future
				 */
				String propertyValue1 = propertyFileOperation
						.readProperty(value);
				break;
				
			case "VERIFYSOAPUITESTCASEPROPERTYFROMFILE":
				strResult = textVerifier
						.verifyByStringLengthAndSoapUIProperties(value);
				break;
				
			case "VERIFYTEXTFROMFILE":
				strResult = textVerifier.verifyStringByLength(value);
				break;
				
			case "WRITEPROPERTYFROMSOAPUITESTCASEPROPERTYXML":
				parts = value.split(Separator.SEPARATOR_COMMA);

				if (parts.length != 3) {
				    throw new ProvisoException("Invalid parameter provided for expected is 3");
				} else {
					String xmlContent = SOAUIRunner
							.getSOAPUITestCaseProperty(parts[0]);
					String xmlTagValue = xmlOperation.getXMLTagFromXpath(
							xmlContent, parts[1]);
					propertyFileOperation.writeByKeyValueProperty(parts[2],
							xmlTagValue);
					strResult = "Pass";
				}
				break;

			case "SWITCHTOIFRAME":
				GlobalLib.switchTOIframe(objectName,p, driver, maxTimeout);
				Thread.sleep(Debug.iSleepMin);
				strResult = "Pass";
				break;

			case "SWITCHTOIDEFAULTFRAME":
				driver.switchTo().defaultContent();
				break;
				
			case "SAVEPARENTWINDOW":
				Debug.parentWindow = driver.getWindowHandle();
				strResult = "Pass";
				break;

			case "SETPARENTWINDOW":
				driver.switchTo().window(Debug.parentWindow);
				strResult = "Pass";
				break;
			case "VERIFYTEXTFROMPROPERTYBYLENGTH":
			    parts = value.split(Separator.SEPARATOR_COMMA); 
			    if (parts.length != 3) {
			        throw new ProvisoException("Invalid parameter provided for expected is 3");
                } else {
					String valueStored = propertyFileOperation.readProperty(parts[2]);
					if (valueStored.contains("\\")) {
						valueStored.replace("\\", "");
					} 

	        		String[] toFrom = parts[1].split(Separator.SEPARATOR_DASH);
	        		if (toFrom.length != 2) {
	        		    System.out.println("Invalid to-From for VERIFYTEXTFROMPROPERTYBYLENGTH");
	        		    strResult = "Fail";
	        		} else {
	        		    String subString = valueStored.substring(Integer.parseInt(toFrom[0]), Integer.parseInt(toFrom[1]));
	        		    if(parts[0].equals(subString)) {
	        			strResult = "Pass";
	        		    }else{
	        		    	strResult = "Fail";
	        		    }
	        		}
			    }
		    	break;
    	    case "SETSOAPUITESTSTEPPPROPERTY":
	    		Thread.sleep(Debug.iSleepMid);
	    		
	    		parts = objectName.split(Separator.SEPARATOR_COMMA);
	    		String propertyNameStep = parts[0];
	    		String propertyValueStep = value;
	    		String testStep = parts[1];
	    
	    		boolean setProperty = SOAUIRunner.setSOAPUITestStepProperty(
	    			propertyNameStep, propertyValueStep, testStep);
	    
	    		if (setProperty) {
	    		    System.out
	    			    .println("Test step property is set successfully as : "
	    				    + value + " to property :" + propertyValueStep);
	    		    strResult = "Pass";
	    		} else {
	    		    strResult = "Fail";
        		}
	    		break;
    	    case "SETSOAPUITESTCASEPPROPERTY":
				Thread.sleep(Debug.iSleepMid);
				if (value.startsWith("Prop_")) {
					value = propertyFileOperation.readProperty(value.split(Separator.SEPARATOR_UNDERSCORE, 2)[1]);
				}
				
				setProperty = SOAUIRunner
							.setSOAPUITestCaseProperty(objectName, value);
				
				if (setProperty) {
					System.out.println("Test case property is set successfully as- " + value + " : " + objectName);
					strResult = "Pass";
				}else{
					strResult = "Fail";
				}
				
				break;/*
			case "COPYANDEDIT": 
				parts = value.split(Separator.SEPARATOR_COMMA);
				String fileName =  propertyFileOperation.readProperty(parts[0]);
				String source = parts[1]+fileName;
				String dest = System.getProperty("user.dir")+ "\\TemplateFile";
				
				boolean copyStatus = GlobalLib.copyFileFromServer(parts[2], Integer.valueOf(parts[3]), parts[4], parts[5],
						source, dest);
				String from = dest+"\\"+fileName;
				String to = System.getProperty("user.dir")+"\\FileToUpload\\"+fileName;
				
				if(copyStatus){
					boolean editStatus = FindAndReplace.editFile(from, to);
					if(editStatus){
						System.out.println("File is copied and edited successfully");
						strResult = "Pass";
					}else{
						System.out.println("File is not successfully edited");
						strResult = "Fail";
					}
				}else{
					System.out.println("File is not successfully copied from server");
					strResult = "Fail";
				}				
				break;*/
/*			case "VERIFYTEXTFROMDBWITHPROPERTY":
				parts = value.split(Separator.SEPARATOR_DOUBLE_HASH);
				String[] conditionValue = parts[6].split(Separator.SEPARATOR_EQUALTO);
				String storedValue = propertyFileOperation.readProperty(conditionValue[1]);
				String dbFetchedValueForProperty = dbOperation.performQueryDbFunction(parts[2], parts[3], parts[4], parts[5], conditionValue[0] + "="+ storedValue);
				String propertyVariable = propertyFileOperation.readProperty(parts[0]);
				String[] toAndFrom = parts[1].split(Separator.SEPARATOR_DASH);
			    actualValue = propertyVariable.substring(Integer.parseInt(toAndFrom[0]), Integer.parseInt(toAndFrom[1]));
			    System.out.println(dbFetchedValueForProperty + "==" + actualValue);
			     
			    if (objectName.equalsIgnoreCase("CONTAIN")) {
			    	 if (dbFetchedValueForProperty.contains(actualValue)) {
				    	 strResult = "Pass";
				     } else {
				    	 strResult = "Fail";
				     }
			     } else {
			    	 if (dbFetchedValueForProperty.equals(actualValue)) {
				    	 strResult = "Pass";
				     } else {
				    	 strResult = "Fail";
				     }
			     }
			     break;
	     	case "DBPOLLINGFOREXPECTEDSTATUS":
				parts = value.split(Separator.SEPARATOR_DOUBLE_HASH);
				String fieldValue = propertyFileOperation.readProperty(parts[0]);				
				boolean statusFound = false;
				String result = "";
				for (int i=0; i<Debug.pollingCount; i++){		
					result = dbOperation.performQueryDbFunction(parts[1],parts[2],parts[3],parts[4],parts[5] +"="+ fieldValue);
					if(result.equalsIgnoreCase(parts[6])){
						System.out.println("Select field is updated with status : "+parts[6]);
						statusFound = true;
						strResult = "Pass";
						break;
					}
					else{
						System.out.println("Waiting for field to update with expected status");
						Thread.sleep(Debug.pollingDuration * 1000);						
					}
				}
				if(!statusFound){
					System.out.println("Expected status is not found after reaching the max timeout. Actual Status : "+result);
					strResult = "Fail";
				}			
				break;
			*/
			case "VERIFYTEXTFROMDB":
				parts = value.split(Separator.SEPARATOR_DOUBLE_HASH);
				System.out.println("dbPollingDuration : " + dbPollingDuration + " dbPollingMaxAttempt : " + dbPollingMaxAttempt);
				
				boolean statusFound = false;
				String valueToVerify = GlobalLib.getPropertyOrValue(parts[0]);
				String dbFetchedValue = "";
				
				for (int i = 0;i < dbPollingMaxAttempt;i++){	
					if (parts.length == 6) {
						dbFetchedValue = dbOperation.performQueryDbFunction(parts[1], parts[2], parts[3], parts[4], parts[5]);
					} else {
						dbFetchedValue = dbOperation.performQueryDbFunction(parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
					}
					
					
					if (dbFetchedValue.equalsIgnoreCase(valueToVerify)) {
						System.out.println("Select field is updated with status : " + dbFetchedValue);
						statusFound = true;
						strResult = "Pass";
						break;
					}
					else{
						System.out.println("Waiting for field to update with expected value : " + valueToVerify);
						Thread.sleep(dbPollingDuration);						
					}
				}
				
				if (!statusFound) {
					
					System.out.println("Expected value : " + dbFetchedValue + " is not found after reaching the max timeout. Actual Status : " + valueToVerify);
					Debug.VALUE = value + "<br> Actual Value : " + dbFetchedValue;
					
					if (value.toUpperCase().contains("MID") 
							&& "Y".equalsIgnoreCase(allEnvObjects.getProperty("gpp_fail_action", "N"))) {
						Debug.VALUE = Debug.VALUE  + dbOperation.performGPPDBVerificationFailAction(parts[1], parts[5]);
					}
					strResult = "Fail";
				}	
			    break;

			case "SAVEFIELDVALUEFROMDATABASE":
				parts = value.split(Separator.SEPARATOR_DOUBLE_HASH);
				String result = "";
				if (parts.length == 6) {
					result = dbOperation.performQueryDbFunction(parts[0],parts[1], parts[2], parts[3], parts[4]);
				} else {
					result = dbOperation.performQueryDbFunction(parts[0],parts[1], parts[2], parts[3], parts[4], parts[5]);
				}
				
				
				if(result != null && !(result.equalsIgnoreCase(" "))){
					System.out.println("Select field is updated with value : "+result);
					Debug.VALUE = value + "<br>" + parts[5] + " : " + result;
					propertyFileOperation.writeByKeyValueProperty(parts[5], result);
					strResult = "Pass";
					Debug.tcTrace=Debug.tcTrace + parts[5] + " : " + result +"<br>";
				}
				else{ 
					System.out.println("Select field is empty");
					strResult = "Fail";					
				}											
				break;
			case "SAVEFIELDVALUEFROMDATABASEQUERY":
				parts = value.split(Separator.SEPARATOR_FOUR_HASH);
				
				for (String part : parts) {
					String[] subParts = part.split(Separator.SEPARATOR_DOUBLE_HASH);
					if (dbOperation.executeQueryAndStoreProperties(subParts[0], subParts[1], subParts[2], dbPollingDuration, dbPollingMaxAttempt)) {
						strResult = "Pass";
					} else {
						strResult = "Fail";
						break;
					}
				}
				
				break;
			case "VERIFYFROMDBQUERY":
				parts = value.split(Separator.SEPARATOR_FOUR_HASH);
				for (String part : parts) {
					subParts = part.split(Separator.SEPARATOR_DOUBLE_HASH);
					dbFetchedValue = "";
					
					if (dbOperation.executeQueryAndVerify(subParts[1], subParts[2], subParts[0], dbPollingDuration, dbPollingMaxAttempt)) {
						strResult = "Pass";
					} else {
						if (value.toUpperCase().contains("MID") 
								&& "Y".equalsIgnoreCase(allEnvObjects.getProperty("gpp_fail_action", "N"))) {
							Debug.traceMessage = Debug.traceMessage  + dbOperation.performGPPDBVerificationFailActionQuery(subParts[1]);
						}
						strResult = "Fail";
						break;
					}
					
				}
				break;
				
			case "SETUPDBCONNECTION":
				if (dbOperation.setUpDbConnection(value)) {
					strResult = "Pass";
				} else {
					strResult = "Fail";
				}
				Debug.traceMessage = Debug.traceMessage + "Db connection set to : " + value;
				break;
			case "DESTROYDBCONNECTION":
				dbOperation.destroyConnection(value);
				Debug.traceMessage = Debug.traceMessage + "Db connection destroyed : " + value;
				break;
			case "GENERATERANDOMVALUEINPROPERTY":
				parts = value.split(Separator.SEPARATOR_FOUR_HASH);
				
				for (String part : parts) {
					subParts = part.split(Separator.SEPARATOR_DOUBLE_HASH);
					String generatedValue = generator.generateRandomValue(subParts[1], subParts[2]);
					Debug.traceMessage = Debug.traceMessage + "Property Name : " + subParts[0] + "<br> Value generated " + generatedValue;
					propertyFileOperation.writeProperty(subParts[0] + "," + generatedValue);
				}
				
				strResult = "Pass";
				break;
			case "GRIDTABLESELECTCOLUMNCELLS": 	//Alok // To do need to optimize
				Thread.sleep(Debug.iSleepMin);
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				parts = value.split(Separator.SEPARATOR_COMMA);			
				String selectValue1 = parts[0];
				columnNumber = Integer.parseInt(parts[1]);
				
				strResult = GlobalLib.selectAmountTableCell(columnNumber,
						selectValue1, p, objectName, driver);

				strResult = "Pass";

				break;
				
			case "SELECTALLROWSINTABLE":
				Thread.sleep(Debug.iSleepMin);
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				
				strResult = GlobalLib.checkAllRowInTable(p, objectName, driver, value);
				strResult = "Pass";
				break;
			case "SETVARBYATTRIBUTE":
				parts = value.split(Separator.SEPARATOR_DOUBLE_HASH);
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				WebElement foundWebElement = driver.findElement(this.getObject(p,
						objectName));
				String textByAttribute = foundWebElement.getAttribute(parts[0]);
				
				if (textByAttribute != null) {
					strResult = "Pass";
					Debug.VALUE = textByAttribute;
					propertyFileOperation.writeByKeyValueProperty(parts[1], textByAttribute);					
				} else {
					strResult = "Fail";
					throw new ProvisoException("Null Value is return from Hidden Field");					
				}
				break;
			case "CALLSTOREDPROCEDURE":
				parts = value.split(Separator.SEPARATOR_DOUBLE_HASH);
				if (parts.length != 3) {
				    throw new ProvisoException("Invalid parameter provided expected is 3");
				}
				dbOperation.performStoredProcedure(parts[0], parts[1], parts[2]);
				break;
			case "DROPDOWNLISTINDEX":
				Thread.sleep(Debug.iSleepMin);
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				GlobalLib.waitForElementEnable(driver, maxTimeout,
						this.getObject(p, objectName));
				if(!value.equalsIgnoreCase(" ")){
					dropdown = new Select(driver.findElement(this.getObject(p, objectName)));
					dropdown.selectByIndex(Integer.parseInt(value));
					strResult = "Pass";
				}else{
					strResult = "Fail";
				}
				
				break;
			case "CLICKONHIDDENELEMENT":
			//	GlobalLib.waitForElementVisible(driver, maxTimeout, this.getObject(p, objectName));
				if (driver.findElement(this.getObject(p, objectName)).isEnabled()) {
					WebElement js1 = driver.findElement(this.getObject(p,
							objectName));
					String jsToExecute = "arguments[0].click();";
					GlobalLib.executeJavaScript(jsToExecute, js1, driver);
					
					strResult = "Pass";
				} else {
					strResult = "Fail";
				}
				break;
			
			case "VERIFYTEXTBYATTRIBUTE":
				parts = value.split(Separator.SEPARATOR_COMMA);
				parts[0] = GlobalLib.getPropertyOrValue(parts[0]);
				/*GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				Thread.sleep(Debug.iSleepMin);*/
				actualValue = driver.findElement(this.getObject(p, objectName)).getAttribute(parts[1]).trim();
				strResult = GlobalLib.getObjectverifySafely(actualValue, parts[0], true);				
				break;

			case "GETSUBSTRINGFROMPROPERTY":
				parts = value.split(Separator.SEPARATOR_DOUBLE_HASH);
				int startIndex =Integer.parseInt(parts[1]);
				int lastIndex = Integer.parseInt(parts[2]);
				String sourceString = propertyFileOperation.readProperty(parts[0]);
				if(sourceString.length() >= lastIndex){
					sourceString = sourceString.substring(startIndex, lastIndex);
					propertyFileOperation.writeByKeyValueProperty(parts[0], sourceString);
					strResult = "Pass";
				}
				else{
					System.out.println("String is not have enough length to get the substring");
					strResult = "Fail";					
				}						
				break;
				
			case "GETSUBSTRING":
				parts = value.split(Separator.SEPARATOR_FOUR_HASH);
				for (String part : parts) {
					subParts = part.split(Separator.SEPARATOR_DOUBLE_HASH);
					
					if (subParts[0].startsWith("FILE_")) {
						String filePath = subParts[0].split(Separator.SEPARATOR_UNDERSCORE, 2)[1];
						sourceString = FileUtils.readFileToString(new File(filePath));
					} else {
						sourceString = GlobalLib.getPropertyOrValue(subParts[0]);
					}				

					if (subParts.length == 4) {
						if(subParts[1].startsWith("Index_")) {
							startIndex = Integer.parseInt(subParts[1].split(Separator.SEPARATOR_UNDERSCORE, 2)[1]);
						} else {
							startIndex = sourceString.indexOf(subParts[1]) + subParts[1].length();
						}
						
						if(subParts[2].startsWith("Index_")) {
							lastIndex = Integer.parseInt(subParts[2].split(Separator.SEPARATOR_UNDERSCORE, 2)[1]) - 1;
						} else {
							lastIndex = sourceString.indexOf(subParts[2]) - 1;
						}
						
						if(sourceString.length() >= lastIndex){
							sourceString = sourceString.substring(startIndex, lastIndex);
							propertyFileOperation.writeByKeyValueProperty(subParts[3], sourceString);
							strResult = "Pass";
						}
					} else if (subParts.length == 3) {
						if(subParts[1].startsWith("Index_")){
							startIndex = Integer.parseInt(subParts[1].split(Separator.SEPARATOR_UNDERSCORE, 2)[1]);
						}else{
							startIndex = sourceString.indexOf(subParts[1]) + subParts[1].length();
						}
						sourceString = sourceString.substring(startIndex);
						propertyFileOperation.writeByKeyValueProperty(subParts[2], sourceString);
						strResult = "Pass";
					} else{
						System.out.println("String is not have enough length to get the substring");
						strResult = "Fail";		
						break;
					}		
				}
								
				break;
				
			case "DOUBLECLICK":
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				GlobalLib.waitForElementEnable(driver, maxTimeout,
						this.getObject(p, objectName));
				if (driver.findElement(this.getObject(p, objectName))
						.isEnabled()) {
                    Actions actions = new Actions(driver);
                    actions.doubleClick(driver.findElement(this.getObject(p, objectName))).build().perform();
					strResult = "Pass";
				} else {
					strResult = "Fail";
				}
				break;
				
			case "COMPAREPROPERTIESVALUES":
                parts = value.split(Separator.SEPARATOR_DOUBLE_HASH);
                if (parts.length != 4) {
                    throw new ProvisoException("Invalid parameter provided expected is 4");
                } else {
                       
                       String firstVarValue = GlobalLib.getPropertyOrValue(parts[0]);
                       String secondVarValue =GlobalLib.getPropertyOrValue(parts[1]);
                       Debug.traceMessage = Debug.traceMessage + "PropValue1: " + firstVarValue + "PropValue1: " + secondVarValue;
                       strResult = GlobalLib.comparePropertyValues(firstVarValue, secondVarValue,parts[2], parts[3]);
                       
                        }
                 break;
	
			case "SETDATE":
				parts = value.split(Separator.SEPARATOR_DOUBLE_HASH);
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				if (parts.length < 2) {
				    throw new ProvisoException("Invalid parameter provided expected is 2");
				} else {
					
					String getDate = GlobalLib.getDate(parts[0],Integer.valueOf(parts[1]));
					Debug.traceMessage = Debug.traceMessage + "Date Set to : " + getDate;
					strResult = setTextByClear(p,objectName,driver,getDate);					
				}
			break;	
			
			case "CHECKOBJECTVISIBILITY":
				strResult = "Fail";
				try{
					if (value.equalsIgnoreCase("Enable")) {
						if(driver.findElement(this.getObject(p, objectName)).isEnabled()){
							strResult = "Pass";						
						}
					}else if(value.equalsIgnoreCase("Disable")){
						if(!(driver.findElement(this.getObject(p, objectName)).isEnabled())){
							strResult = "Pass";
						}
					}else if(value.equalsIgnoreCase("Selected")){
						if((driver.findElement(this.getObject(p, objectName)).isSelected())){
							strResult = "Pass";
						}
					}else if(value.equalsIgnoreCase("Not Selected")){
						if(!(driver.findElement(this.getObject(p, objectName)).isSelected())){
							strResult = "Pass";
						}
					}					
					else{
						System.out.println("Inputs are not proper.");
						strResult = "Fail";
						}
				}catch(org.openqa.selenium.NoSuchElementException e){
					strResult = "Fail";
				}
			break;
			
			case "VERIFYDROPDOWNVALUES":
				parts = value.split(Separator.SEPARATOR_DOUBLE_HASH);
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				
				strResult = GlobalLib.VerifyDropdownValues(driver, objectName, parts, p);
				break;
				
			case "DROPDOWNDEFAULTVALUE":
				Select dropdownElement = new Select(driver.findElement(this.getObject(p, objectName)));
				WebElement defaultValue = dropdownElement.getFirstSelectedOption();				
				String dropdownDefaultValue = defaultValue.getText().trim();
				System.out.println("Dropdown Default Value: "+dropdownDefaultValue);
				String expectedValue = value.trim();

				if (dropdownDefaultValue.equalsIgnoreCase(expectedValue)){
					strResult = "Pass";
				} else {
					strResult = "Fail";
				}
				break;	
				
			case "VERIFYFIELDBYTAGNAME":				
				WebElement tag = driver.findElement(this.getObject(p,objectName));
				actualValue = tag.getTagName();
				strResult = GlobalLib.getObjectverifySafely(actualValue, value, true);	
				break;				
			
			case "GRIDTABLECLICKONCHECKBOX" :
				
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				parts = value.split(Separator.SEPARATOR_DOUBLE_HASH);
				for(int i = 0; i < parts.length; i++){
				strResult=GlobalLib.clickCheckbox(parts[i], p, objectName, driver);
				if(strResult.equalsIgnoreCase("Fail")){
					break;
					}
				}
				
				break;
				
			case "GRIDTABLEENTERTEXTINTOCOLUMNCELL" :
				
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				parts = value.split(Separator.SEPARATOR_DOUBLE_HASH);
				String ColumnName = parts[0];
				int ColumnIndex = GlobalLib.getColumn(ColumnName, p, objectName, driver);
				for(int i = 1; i < parts.length; i++){
					rParts=parts[i].split(Separator.SEPARATOR_COLON);
					strResult=GlobalLib.enterValueInColumnCell(rParts[0],ColumnIndex, rParts[1], p, objectName, driver);
				}
				break;

			case "GRIDTABLESELECTSCROLLLISTVALUE" :
				
				GlobalLib.waitForElementVisible(driver, maxTimeout,
						this.getObject(p, objectName));
				parts = value.split(Separator.SEPARATOR_AND_HASH);
			    ColumnName = parts[0];
				int ColumnIndex1 = GlobalLib.getColumn(ColumnName, p, objectName, driver);
				for(int i = 1; i < parts.length; i++){
					rParts=parts[i].split(Separator.SEPARATOR_COLON);
					strResult=GlobalLib.selectScrollListValueInCell(rParts[0], ColumnIndex1, rParts[1], p, objectName, driver);
				}
				break;
				
	         case "FOCUSONWEBELEMENT":
	        	 
                WebElement hiddenElement=driver.findElement(this.getObject(p, objectName));
                Coordinates coordinates = ((Locatable)hiddenElement).getCoordinates();
                coordinates.inViewPort();
                strResult = "Pass";
                break; 
              
	         case "SEARCHANDSELECTOBJECT":
	        	 GlobalLib.waitForElementVisible(driver, maxTimeout,
							this.getObject(p, objectName)); 
	        	 if (driver.findElement(this.getObject(p, objectName))
							.isEnabled()) {
	        		 WebElement searchlement = driver.findElement(this.getObject(p, objectName));
	        		 searchlement.click();
	        		 value = GlobalLib.getPropertyOrValue(value);	   		 
	        		 searchlement.sendKeys(value);
	        		 Thread.sleep(Debug.iSleepMid);
	        		 searchlement.sendKeys(Keys.ARROW_DOWN);
	        		 searchlement.sendKeys(Keys.ENTER);
	        	     Debug.VALUE = value; 
	        		 strResult = "Pass";
	        		
	        	 }else{
	        		 strResult = "Fail";
	        	 } 
	        	break; 
	       
	         case "SELECTDATEFROMCALENDAR":
	        	 // Need to change ##
	        	 rParts = objectName.split(Separator.SEPARATOR_COLON);
	        	 GlobalLib.waitForElementVisible(driver, maxTimeout,
							this.getObject(p, rParts[0]));  	        	 
	        	 parts = value.split(Separator.SEPARATOR_DASH);
	        	 strResult = GlobalLib.setCalenderDate(driver, p, rParts[0],rParts[1],rParts[2],rParts[3],rParts[4],rParts[5], parts[0], parts[1], parts[2]);
	        	 break;
	        	 
	         case "SETTEXTBYJS":
	        	// GlobalLib.waitForElementVisible(driver, maxTimeout, this.getObject(p, objectName));
					if (driver.findElement(this.getObject(p, objectName)).isEnabled()) {
						WebElement js1 = driver.findElement(this.getObject(p,
								objectName));
						value = GlobalLib.getPropertyOrValue(value);		
						String jsToExecute = "arguments[0].setAttribute('value', '" + value + "');";
					    GlobalLib.executeJavaScript(jsToExecute, js1, driver);
						strResult = "Pass";
					} else {
						strResult = "Fail";
					}
			break;
	         case "DATEPICKERADVSEARCH":
	        	 // Need to change ## Takes paramenters
	        	 // in Object/ value column: Object of data Text filed, Prev button object, Month and year object, Next button object
	        	 // value 10-March-2016
	        	 rParts = objectName.split(Separator.SEPARATOR_COLON);
	        	 GlobalLib.waitForElementVisible(driver, maxTimeout,
							this.getObject(p, rParts[0]));
	        	 WebElement webElementDateField = driver.findElement(this.getObject(p,
	        			 rParts[0]));
	        	 
	        	 webElementDateField.click();
	        	 GlobalLib.waitForJsLoad(driver, maxTimeout);
	        	 
	        	 WebElement WEprev = driver.findElement(this.getObject(p,
	        			 rParts[1]));
	        	WebElement WEnext = driver.findElement(this.getObject(p,
	        			 rParts[3]));
	        	 strResult = GlobalLib.setCalenderDateAdvSearch(driver,p, WEprev, rParts[2], WEnext, value);	        	 
	        	
	        	 break;
		
	        	 

	         case "CHANGEDATEBYDATEPICKERJS":
	        	 parts = value.split(Separator.SEPARATOR_DOUBLE_HASH);
	         	 GlobalLib.waitForElementVisible(driver, maxTimeout, this.getObject(p, objectName));
				if (driver.findElement(this.getObject(p, objectName)).isEnabled()) {
					WebElement js1 = driver.findElement(this.getObject(p,
							objectName));
					String id = js1.getAttribute("id");
					String dateDefaultValue = js1.getAttribute("value");
					Calendar cal = GlobalLib.changeDate(parts[0], dateDefaultValue, parts[1], Integer.parseInt(parts[2]));
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    int year = cal.get(Calendar.YEAR);
				
                    String jsToExecute = "$('#" + id + "').datepicker().datepicker('setDate',(new Date(" +year+ "," +month+ "," +day+ ")))";
					GlobalLib.executeJavaScript(jsToExecute, js1, driver);
             		Debug.VALUE = js1.getAttribute("value");
					strResult = "Pass";
				} else {
					strResult = "Fail";
				}
			break;
			
			
	         case "SETTEXTBYDATEPICKERJS":
	        	 parts = value.split(Separator.SEPARATOR_DOUBLE_HASH);
	        	 String date = parts[0];
	        	 int month = Integer.valueOf(parts[1])-1;
	        	 String year = parts[2];
	        	 GlobalLib.waitForElementVisible(driver, maxTimeout, this.getObject(p, objectName));
					if (driver.findElement(this.getObject(p, objectName)).isEnabled()) {
						WebElement js1 = driver.findElement(this.getObject(p,
								objectName));
						String id = js1.getAttribute("id");
						value = GlobalLib.getPropertyOrValue(value);	
						String jsToExecute = "$('#" + id + "').datepicker().datepicker('setDate',(new Date(" +year+ "," +month+ "," +date+ ")))";
					    GlobalLib.executeJavaScript(jsToExecute, js1, driver);
						strResult = "Pass";
					} else {
						strResult = "Fail";
					}
			break;
			
	         case "CLICKOBJECTATPOSITION":	        	 
	    		 List<WebElement> elements = driver.findElements(this.getObject(p,objectName));
	    		 boolean elementFound = false;
	    		 for(int i = 0; i < elements.size(); i++){
	    			 if(i == (Integer.parseInt(value)-1)){
	    				 elements.get(i).click();
	    				 strResult = "Pass";
	    				 elementFound = true;
	    				 break;
	    			 }
	    		 }
	    		 if(!elementFound){
	    			 strResult = "Fail";
	    		 }
			break;
			
	         case "VERIFYPDFTEXT": 
	             parts = value.split(Separator.SEPARATOR_DOUBLE_HASH);
	             
	             if (parts.length != 2) {
	                 strResult = "Fail";
	             } else {
	            	 Debug.traceMessage = Debug.traceMessage + " Search Text : " + parts[1];
	                 File pdfFile = new File(System.getProperty("user.dir")+"\\FileToUpload\\" + parts[0]);  
	                 // The PDF file from where you would like to extract
	                 try {
	                     PDDocument pdfDocument = PDDocument.load(pdfFile);
	                     PDFTextStripper stripper = new PDFTextStripper();
	                     String fileText = stripper.getText(pdfDocument);
	                     if(fileText.contains(parts[1])) {
	                         strResult = "Pass";
	                     } else {
	                         strResult = "Fail";
	                     }
	                 } catch (IOException e) {
	                     strResult = "Fail";
	                     e.printStackTrace();
	                 }
	             }
	         break;
			
	         case "SAVEATTRIBUTEVALUEINTOVAR":
	        	   parts = value.split(Separator.SEPARATOR_DOUBLE_HASH); 
	        	   if (parts.length != 2) {
	  	      		   
	        		   strResult = "Fail";
	                } else {
					//GlobalLib.waitForElementVisible(driver, maxTimeout, this.getObject(p, objectName));
					String capturedAttributeValue = driver.findElement(this.getObject(p, objectName)).getAttribute(parts[1]);
					Debug.traceMessage = Debug.traceMessage + "Attribute : " + parts[0] + "<br> Value found : "  + capturedAttributeValue;
					propertyFileOperation.writeProperty(parts[0] + "," + capturedAttributeValue);
					Debug.VALUE = capturedAttributeValue;
					System.out.println(capturedAttributeValue);
					strResult = "Pass";
	                }
					break;
					
	         case "VALIDATEXSDANDXML":
	        	   parts = value.split(Separator.SEPARATOR_DOUBLE_HASH); 
	        	   if (parts.length == 2 || parts.length == 3) {
	        		   if (GlobalLib.validateXMLSchema(parts[0], parts[1])) {	        			  
	                		strResult = "Pass";
	                	} else {
                			if (parts.length == 3) {
                				if (parts[2].equalsIgnoreCase("Fail")) {
                					strResult = "Pass";
                				}		        				   
                			} else {
                				strResult = "Fail";
                				}
	                		}
	        	   		  	      		  
	        	   } else {
	        		   System.out.println("Invalid input");
	        		   strResult = "Fail";
	        	   }
				  
	        	    break;
	         case "VERIFYBYXPATH":
        	 	   parts = value.split(Separator.SEPARATOR_FOUR_HASH);
        	 	   for (String part : parts) {
        	 		  subParts = part.split(Separator.SEPARATOR_DOUBLE_HASH);
	   	        	   String xpathValueToVerify = GlobalLib.getPropertyOrValue(subParts[0]);
	   	        	   if (subParts.length != 3) {
	   	        		Debug.traceMessage = Debug.traceMessage + "Invalid input";
	   	        		   strResult = "Fail";
	   	        		   break;
	   	        	   } else {
	   	        		   String xpathValue = xmlOperation.getXpathValue(subParts[1], subParts[2]);
	   	        		   Debug.traceMessage = Debug.traceMessage + "Xpath / File : " + subParts[2] + " Xapth value : "  + xpathValue; 
	   	        		   if (xpathValue.equals(xpathValueToVerify)) {
	   	        			   strResult = "Pass";
	   	        		   } else {
	   	        			   strResult = "Fail";
	   	        			   break;
	   	        		   }
	   	        	   }
        	 	   }
	        	   
	        	   break;
	         case "SAVEVARIABLEBYXPATH":
	        	   parts = value.split(Separator.SEPARATOR_FOUR_HASH);
	        	   for (String part : parts) {
	        		   subParts = part.split(Separator.SEPARATOR_DOUBLE_HASH);
	        		   if (subParts.length != 3) {
	        			   Debug.traceMessage = Debug.traceMessage + "Invalid input";
		        		   strResult = "Fail";
		        		   break;
		        	   } else {
		        		  
		        		   String xpathValue = xmlOperation.getXpathValue(subParts[1], subParts[2]);
		        		   Debug.traceMessage = Debug.traceMessage + "Xpath / File : " + subParts[2] + " <br>Xapth value : "  + xpathValue + " <br>Variable to Store: "  + parts[0]; 
		        		   propertyFileOperation.writeByKeyValueProperty(subParts[0], xpathValue);
		        		   strResult = "Pass";
		        	   }
	        	   }
	        	  
	        	   break;
	         case "SAVEFUTURETIMESTAMPINVARIABLE":
					parts = value.split(Separator.SEPARATOR_DOUBLE_HASH);
					String timestamp =  GlobalLib.getPropertyOrValue(parts[1]);
					if (parts.length != 6) {
					    throw new ProvisoException("Invalid parameters provided expected is 6");
					} else {
						Calendar cal = GlobalLib.changeDate(parts[0],timestamp,parts[2],Integer.valueOf(parts[3]));
						Date datentime = cal.getTime();             
						SimpleDateFormat format = new SimpleDateFormat(parts[4]);
						String futureTime = format.format(datentime); 
						propertyFileOperation.writeByKeyValueProperty(parts[5],	futureTime);
						strResult = "Pass";
					}
					break;
	         case "CLICKOBJECTBYCONTAINTEXT":
	        	 GlobalLib.waitForElementVisible(driver, maxTimeout,
                               By.xpath("//"+objectName+"[contains(.,'" + value + "')]"));
                 driver.findElement(
                               By.xpath("//"+objectName+"[contains(.,'" + value + "')]")).click();
                 strResult = "Pass";
                 break;
             case "UPDATEFROMDATABASEQUERY":
                 //value = GlobalLib.replacePropertiesFromString(value);
                 int recordsUpdated = dbOperation.performUpdate(value);
                 System.out.println("Records updated : " + recordsUpdated);
                 strResult = "Pass";
                 break;
             case "FILEOPERATION":
                 parts = value.split(Separator.SEPARATOR_DOUBLE_HASH);
                 if (parts.length == 2) {
                     strResult = FileOperation.createOrDeleteFile(parts[0], parts[1]);
                 } else if (parts.length == 3) {
                    strResult =  FileOperation.copyFile(parts[1], parts[2]);
                 } else {
                     strResult = "Fail";
                 }
                 break;
             case "CLOSEALLBROWSERINSTANCE":
            	 String closeStringExe = "";
				switch (value) {
				case "IE":
					closeStringExe = "IEDriverServer.exe";
					Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe /T");
					break;
				case "FF":
					closeStringExe = "firefox.exe";
					break;
				case "CHROME":
					closeStringExe = "chromedriver.exe";
					break;
				default:
					break;
				}
            	 Runtime.getRuntime().exec("taskkill /F /IM "+ closeStringExe +"/T");
                 break;
                 
             case "GRIDTABLE_VERIFY_TEXT_FALSE":

 				Thread.sleep(Debug.iSleepMin);

 				if (Debug.pollingFlag) {
 					pollingInterval = Debug.pollingDuration * 1000;
 					pollingCount = Debug.pollingCount;
 				} else {
 					pollingInterval = Debug.iSleepMax;
 				}

 				strResult = "Pass";
 				for (int i = 1; i <= pollingCount; i++) {
 					if (Debug.pollingFlag) {
 						driver.navigate().refresh();
 						Thread.sleep(pollingInterval);
 					}
 					// GlobalLib.waitForElementVisible(driver, maxTimeout,
 					// this.getObject(p, objectName));
 					parts = value.split(Separator.SEPARATOR_COMMA);
 					searchText = GlobalLib.getPropertyOrValue(parts[0]);
 					setDebugValueIsProperty(value, searchText);
 					String headerText1 = parts[1];
 					 verifyText = parts[2];
 					String headerText2 = parts[3];
 					// columnNumber = Integer.parseInt(parts[3]);
 					colIndex = GlobalLib.getColumn(headerText1, p, objectName,
 							driver);

 					// GlobalLib.waitForElementVisible(driver, maxTimeout,
 					// this.getObject(p, objectName));
 					rowIndex = GlobalLib.getRow1(searchText, p, objectName,
 							driver, colIndex);
 					if (rowIndex >= 0) {

 						columnNumber = GlobalLib.getColumn(headerText2, p,
 								objectName, driver);
 						strResult = GlobalLib
 								.verifyTableCellText1(rowIndex, columnNumber,
 										verifyText, p, objectName, driver);

 						if (strResult.equalsIgnoreCase("PASS")) {
 							strResult = "Fail";
 							break;
 						}
 					} else {
 						strResult = "Pass";

 					}

 				}
 				Debug.pollingFlag = false;
 				if (strResult.equalsIgnoreCase("FAIL")
 						&& HardStopOnTextVerification.equalsIgnoreCase("ON")) {
 					throw new ProvisoException("Text Verification Failed");
 				}

 				break;
            
             case "SET_DEFAULT_DATE_INTO_VAR_FROM_DATEPICKER_BYJS":
                 GlobalLib.waitForElementVisible(driver, maxTimeout,
                                          this.getObject(p, objectName));
                            if (driver.findElement(this.getObject(p, objectName))
                                          .isEnabled()) {
                                   WebElement js1 = driver.findElement(this.getObject(p,
                                                 objectName));
                                   String id = js1.getAttribute("id");
                                   String jsToExecute = "return $('#" + id
                                                 + "').datepicker().val()";
                                   JavascriptExecutor executor = (JavascriptExecutor) driver;
                                   result = (String) executor.executeScript(jsToExecute);

                                   propertyFileOperation
                                                 .writeByKeyValueProperty(value, result);
                                   strResult = "Pass";
                            } else {
                                   strResult = "Fail";
                            }
                 break;
            case "UPDATE_XML_BY_XPATH":
                parts = value.split(Separator.SEPARATOR_DOUBLE_HASH);
                xmlOperation.updateXmlByXpath(parts[0], parts[1]);
                strResult = "Pass";
                break;
            case "SETUP_QUEUE_CONNECTION":
                strResult = queueOperation.setUpQueueConnection(value);
                Debug.traceMessage = Debug.traceMessage + "Queue connection : " + value;
                break;
            case "WRITE_QUEUE":
                parts = value.split(Separator.SEPARATOR_DOUBLE_HASH);
                if (parts.length == 2) {
                    parts[2] = null;
                } 
                strResult = queueOperation.sendMessage(parts[0], parts[2], parts[1]);
                break;
            case "READ_QUEUE":
                parts =  value.split(Separator.SEPARATOR_DOUBLE_HASH);
                String jmsCorrelationId = null;
                
                if (parts.length >= 5) {
                	if (parts.length == 6){
                		jmsCorrelationId = parts[5];
                	}
                    if (parts[1].equalsIgnoreCase("Browse")) {
                        strResult = queueOperation.browseAndSearchMessage(parts[0], parts[2], parts[3], parts[4], jmsCorrelationId);
                    } else if (parts[1].equalsIgnoreCase("Consume")) {
                        strResult = queueOperation.consumeAndSearch(parts[0], parts[2], parts[3], parts[4], jmsCorrelationId);
                    } else {
                        throw new ProvisoException("Invalid Queue operation expected Browse/Consume");
                    }
                } else{
                	throw new ProvisoException("Input parameters are not proper");
                }
                break;
            case "DESTROY_QUEUE_CONNECTION":
                strResult = queueOperation.destroyQueueConnection();
                break;
            case "VARIABLE_OPERATION":
            	parts = value.split(Separator.SEPARATOR_DOUBLE_HASH);
            	strResult = GlobalLib.variableOperation(parts);
            	
            	if (!"".equals(strResult) && !strResult.equalsIgnoreCase("Fail")) {
            		propertyFileOperation.writeByKeyValueProperty(parts[1], strResult);
            		strResult = "Pass";
            	} 
                break;
                
            case "HANDLEALERT" :
            	switch (value.toUpperCase()){	
            	
            	case "ACCEPT":            	
            		driver.switchTo().alert().accept();
            		strResult = "Pass";
            		break;
            		
            	case "DISMISS":         
            		driver.switchTo().alert().dismiss();
            		strResult = "Pass";
            		break;
            		
            	default:
            		System.out.println("Invalid input");
            		strResult = "Fail";
            		break;
            	}
            	
			break;
            case "GET_TEXT_BY_DATE_PICKER_JS":
                GlobalLib.waitForElementVisible(driver, maxTimeout,
                                         this.getObject(p, objectName));
                           if (driver.findElement(this.getObject(p, objectName))
                                         .isEnabled()) {
                                  WebElement js1 = driver.findElement(this.getObject(p,
                                                objectName));
                                  String id = js1.getAttribute("id");
                                  String jsToExecute = "return $('#" + id
                                                + "').datepicker().val()";
                                  JavascriptExecutor executor = (JavascriptExecutor) driver;
                                  result = (String) executor.executeScript(jsToExecute);

                                  propertyFileOperation
                                                .writeByKeyValueProperty(value, result);
                                  strResult = "Pass";
                           } else {
                                  strResult = "Fail";
                           }
                break;
                
            case "SET_TEXT_AT_POSITION":	  
            	 parts = value.split(Separator.SEPARATOR_DOUBLE_HASH);
	    		 elements = driver.findElements(this.getObject(p,objectName));
	    		 elementFound = false;
	    		 for(int i = 0; i < elements.size(); i++){
	    			 if(i == (Integer.parseInt(parts[0])-1)){
	    				 setTextByClear(elements.get(i),parts[1]);
	    				 strResult = "Pass";
	    				 elementFound = true;
	    				 break;
	    			 }
	    		 }
	    		 if(!elementFound){
	    			 strResult = "Fail";
	    		 }
			break;
			
            case "EXECUTE_SOAP_REQUEST":	  
           	 parts = value.split(Separator.SEPARATOR_DOUBLE_HASH);
           	 String encoding = "UTF-8";
           	 if (parts.length >= 2) {
           		if (parts.length == 3) {
               		encoding = parts[2];
               	 }           		
           		if (SOAUIRunner.executeSOAPRequest(parts[0], parts[1], encoding)) {
           			strResult = "Pass";
           		}
           		else {
           		 	System.out.println("Invalid parameters provided");
           		 	strResult = "Fail";
           	         }  
       	     }
			 break;
            case "VERIFY_CONTAINS":	  
				parts = value.split(Separator.SEPARATOR_DOUBLE_HASH);
				if (parts.length == 2) {
					sourceString = parts[0];
					if (parts[0].startsWith("FILE_")) {
						String filePath = parts[0].split(Separator.SEPARATOR_UNDERSCORE, 2)[1];
						sourceString = FileUtils.readFileToString(new File(filePath));
					} 
					if (sourceString.contains(parts[1])) {
						strResult = "Pass";
					} else {
						strResult = "Fail";
					}
				} else {
					throw new ProvisoException("invalid parameter specified");
				}
   			 break;
            case "VERIFY_NOT_CONTAINS":	  
             	 parts = value.split(Separator.SEPARATOR_DOUBLE_HASH);
             	 if (parts.length == 2) {
             		sourceString = parts[0];
             		if (parts[0].startsWith("FILE_")) {
						String filePath = parts[0].split(Separator.SEPARATOR_UNDERSCORE, 2)[1];
						sourceString = FileUtils.readFileToString(new File(filePath));
					} 
					if (!sourceString.contains(parts[1])) {
						strResult = "Pass";
					} else {
						strResult = "Fail";
					}
             	 } else {
             		throw new ProvisoException("invalid parameter specified");
             	 }
  			 break;
            case "CHECKBOX_CHECK_UNCHECK": 
            	WebElement element = driver.findElement(this.getObject(p, objectName));
            	
            	if(element.getAttribute("src").contains("unchecked")) { 
            		if(value.equalsIgnoreCase("SELECTED")){
            			element.click();
            			strResult = "Pass";
            		}
            		strResult = "Pass";
            	}            	
            	else if(element.getAttribute("src").contains("checked")) {          		
            		if(value.equalsIgnoreCase("NOT SELECTED")){
            			element.click();
            			strResult = "Pass";
            		}
            		strResult = "Pass";
            	}                     	
            	else if ((!element.isSelected()) && value.equalsIgnoreCase("CHECK")) {
					element.click();
					strResult = "Pass";
				}
            	else if (element.isSelected() && value.equalsIgnoreCase("UNCHECK")) {
					element.click();
					strResult = "Pass";
				}
				break;
            case "EXECUTE_JAVASCRIPT": 
            	JavascriptExecutor js = (JavascriptExecutor)driver;
            	parts = value.split(Separator.SEPARATOR_DOUBLE_HASH);
            	if (parts.length == 1) {
            		js.executeScript(parts[0]);
            	} else if(parts.length == 2) {
            		result = (String) js.executeScript("return "+parts[0]);
            		propertyFileOperation.writeByKeyValueProperty(parts[1], result);
            		Debug.traceMessage = Debug.traceMessage + " Value found " + result;
            	} else {
            		Debug.traceMessage = Debug.traceMessage + " Invalid parameters provided";
            		strResult = "Fail";
            		break;
            	}
            	strResult = "Pass";
				break;
				
            case "VARFINDNREPLACE":
				Map<String, String> varFileReplacements = new HashMap<String, String>();
				System.out.println(value);
				String[] splitExcelRowArrayVarFile = value.split(Separator.SEPARATOR_HASH);
				for (int i = 1; i < splitExcelRowArrayVarFile.length; i++) {
					String splitExcelRow = splitExcelRowArrayVarFile[i];
					String[] splitReplacements = splitExcelRow.split(Separator.SEPARATOR_COLON);
					if (splitReplacements[1].startsWith("VAR_")) {
						splitReplacements[1] = propertyFileOperation
								.readProperty(splitReplacements[1].split(Separator.SEPARATOR_UNDERSCORE)[1]);
					}
					varFileReplacements
							.put(splitReplacements[0], splitReplacements[1]);
				}
				String[] srcDestVarFileArray = splitExcelRowArrayVarFile[0].split(Separator.SEPARATOR_COLON);
				String srcVar = GlobalLib.getPropertyOrValue(srcDestVarFileArray[0]);
				String destVarFile = GlobalLib.getPropertyOrValue(srcDestVarFileArray[1]);
				
				String replacedVarFileName = FileOperation
						.replaceFileContentToUpload(srcVar,
								destVarFile,
								System.getProperty("user.dir")
										+ "\\TemplateFile\\",
								System.getProperty("user.dir")
										+ "\\FileToUpload\\", varFileReplacements);

				System.out.println(replacedVarFileName);

				strResult = "Pass";
				break;	
            default:
                 List<String> notStandardKeywords = new ArrayList<String>();
                 notStandardKeywords.add("CALL_TEST_CASE");
                 
                 if (!notStandardKeywords.contains(operation.toUpperCase())) {
                     System.out.println("Keyword not found");
                     throw new ProvisoException("Keyword not found : " + operation.toUpperCase());
                 }
                 strResult = "Pass";
                 break;
		    	
			}
			
			if (strResult.equalsIgnoreCase("Fail")) {
	            GlobalLib.closeBrowsers(driver, imageFolder);
	        }
			
			return strResult;


		} catch (Exception e) {
			GlobalLib.closeBrowsers(driver, imageFolder);
			ProvisoException.exceptionHandler(e, driver);
			//System.out.println("## ERROR ##" + Debug.errorMessage);
			//e.printStackTrace();
			return "FailException";
		}		
	}

	/**
	 * Set text into field after clear the watermark .
	 * @param p
	 * @param element
	 * @param driver
	 * @param textToSet
	 * @return
	 * @throws Exception
	 */
	private String setTextByClear(Properties p, String object, WebDriver driver, String textToSet) throws Exception {
		WebElement element = driver.findElement(this.getObject(p, object));
		
		return setTextByClear(element, textToSet);
	}
	
	/**
	 * Set text into field after clear the watermark .
	 * @param element
	 * @param textToSet
	 * @return
	 * @throws Exception
	 */
	private String setTextByClear(WebElement element, String textToSet) throws Exception {
		if (element.isEnabled()) {
			element.sendKeys(Keys.END);
			element.sendKeys(Keys.SHIFT, Keys.HOME);
			element.sendKeys(Keys.DELETE);
			element.clear();
			element.sendKeys(textToSet);

			return "Pass";
		} else {
			return "Fail";
		}
	}

	/**
	 * Set DebugValue by generated random number
	 * @param value
	 * @param searchText
	 */
	private void setDebugValueIsProperty(String value, String searchText) {
		if (value.contains("Prop_")) {
			Debug.VALUE = "Test Data : " + value +"<br> Generated : " + searchText;
		}
	}
	
	/**
	 * Find element BY using object type and value
	 * 
	 * @param p
	 * @param objectName
	 * @throws Exception
	 */

	public By getObject(Properties p, String objectName) throws Exception {
		// Find by xpath

		String strArr = p.getProperty(objectName);
		try {
			if(strArr == null){
				throw new Exception("Object Not Defined: " + objectName);
			}else if(strArr.isEmpty()){
				
				throw new Exception("No locatore defined for Object: " + objectName);
			}

			String[] parts = strArr.split(Separator.SEPARATOR_COLON, 2);
			
			if (parts.length < 2) {
				throw new Exception("Incorrect Locator defined. Use syntax <findby>:<locator> Ex. id:UserName,xpath://");
			}
			
			String arrObjectType = parts[0];
			String arrObjectProp = parts[1];
			
		
	
			if (arrObjectType.equalsIgnoreCase("XPATH")) {

				// return By.xpath(p.getProperty(objectName));
				return By.xpath(arrObjectProp);
			}
			// Find by id
			if (arrObjectType.equalsIgnoreCase("ID")) {

				// return By.id(p.getProperty(objectName));
				return By.id(arrObjectProp);
			}
			// find by class
			else if (arrObjectType.equalsIgnoreCase("CLASSNAME")) {

				// return By.className(p.getProperty(objectName));
				return By.className(arrObjectProp);
			}
			// find by name
			else if (arrObjectType.equalsIgnoreCase("NAME")) {

				// return By.name(p.getProperty(objectName));
				return By.name(arrObjectProp);
			}
			// Find by css
			else if (arrObjectType.equalsIgnoreCase("cssSelector")) {

				// return By.cssSelector(p.getProperty(objectName));
				return By.cssSelector(arrObjectProp);
			}
			// find by link
			else if (arrObjectType.equalsIgnoreCase("LINK")) {

				// return By.linkText(p.getProperty(objectName));
				return By.linkText(arrObjectProp);
			}
			// find by partial link
			else if (arrObjectType.equalsIgnoreCase("PARTIALLINK")) {

				// return By.partialLinkText(p.getProperty(objectName));
				return By.partialLinkText(arrObjectProp);

			} else {
				throw new Exception("Wrong object locator type use: XPATH/ID/CLASSNAME/NAME/cssSelector/LINK/PARTIALLINK ");
			}

		} catch (Exception e) {
			ProvisoException.exceptionHandler(e, null);
			System.out.println(Debug.traceMessage);
			//e.printStackTrace();
		}
		throw new Exception(Debug.traceMessage);
	}
}
