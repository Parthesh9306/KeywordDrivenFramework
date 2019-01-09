package Utility;

import hibernate.entity.StepResult;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.ProxySelector;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import operation.Debug;
import operation.PropertyFileOperation;
import operation.ReadObject;
import operation.SOAUIRunner;
import operation.InternalDbOperation;
import operation.UIOperation;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.xml.sax.SAXException;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;


import constants.Separator;
import excelExportAndFileIO.ReadAFWExcelFile;

public class GlobalLib {
	public static WebDriver driver;
	public static String indexFileName = null;
	public static String groupFileName = null;
	public static WebDriver webdriver = null;
	public static UIOperation operation = null;

	// Constructor
	public GlobalLib(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * getRow : Return the row from the grid table
	 * 
	 * @param WebDriver
	 *            : Selection of the WebElement
	 * @param searchText
	 *            : Search the specified text
	 * @param objectName
	 *            : Locator of the Element
	 * @paramProperties p : Get the values from the properties file
	 * @return row : Return specified row
	 */

	public static int getRow(String searchText, Properties p,
			String objectName, WebDriver driver, int colIndex) throws Exception {
		String cellText = "";
		boolean isDisplayed = false;
		UIOperation operation = null;
		operation = new UIOperation(driver);
		int page = 0;
		// Grab the table

		do {
			++page;
			WebElement table = driver.findElement(operation.getObject(p,
					objectName));
			List<WebElement> columnIndex = table.findElements(By
					.xpath(".//tbody/tr/td[" + colIndex + "]"));
			System.out.println("NUMBER OF ROWS IN THIS TABLE = "
					+ columnIndex.size());
			int row_num = 1;
			for (WebElement cell : columnIndex) {

				List<WebElement> childs = cell.findElements(By
						.cssSelector("input[type='text']"));
				System.out.println("CellSize:" + childs.size());
				if (childs.size() > 0) {

					for (WebElement childElement : childs) {
						cellText = childElement.getAttribute("value");
						System.out.println("CellValue:" + cellText);
						cellText = cellText.replace(",", "");

						if (cellText.equals(searchText)) {
							return row_num;
						}
					}

				} else {
					cellText = cell.getText();
					cellText = cellText.replace(",", "");
					System.out.println("CellValue:" + cellText);
					if (cellText.equals(searchText)) {
						return row_num;
					}
				}
				row_num++;
			}

			try {
				WebElement lastRow = driver.findElement(By
						.cssSelector("a[id='nextpage']"));
				if (lastRow.isDisplayed() == true) {
					JavascriptExecutor executor = (JavascriptExecutor) driver;
					executor.executeScript("arguments[0].click();", lastRow);
					isDisplayed = true;
				} else {

					isDisplayed = false;
				}
			} catch (Exception e) {

				break;
			}

		} while (isDisplayed == true);

		return 0;
	}

	/**
	 * getRow : Return the row from the grid table
	 * 
	 * @param WebDriver
	 *            : Selection of the WebElement
	 * @param searchText
	 *            : Search the specified text
	 * @param objectName
	 *            : Locator of the Element
	 * @paramProperties p : Get the values from the properties file
	 * @return row : Return specified row
	 */

	public static int getRow1(String searchText, Properties p,
			String objectName, WebDriver driver, int colIndex) throws Exception {

		int page = 0; 
		// Grab the table
		do {
			++page;
			WebElement table = driver.findElement(operation.getObject(p,
					objectName));
			int row_num = -1;
			try {
				String dataRow = table.findElement(
						By.xpath(".//tbody/tr/td[" + colIndex + "]/*[text()='"
								+ searchText + "']/../..")).getAttribute(
						"data-recordindex");
				row_num = Integer.parseInt(dataRow);
				if (row_num != -1)
					return row_num;
			} catch (Exception e) {

				WebElement lastRow = driver.findElement(By
						.xpath("//*[@data-qtip='Next Page']"));

				if (lastRow.getAttribute("class").contains("disable") == false) {
					lastRow.click();
					// To do add explicit wait
					Thread.sleep(6000);
				} else {
					break;
				}
			}

		} while (true);
		return -1;
	}

	/**
	 * getColumn : Return the column from the grid table
	 * 
	 * @param WebDriver
	 *            : Selection of the WebElement
	 * @param searchText
	 *            : Search the specified text
	 * @param objectName
	 *            : Locator of the Element
	 * @paramProperties p : Get the values from the properties file
	 * @return row : Return specified row
	 */

	public static int getColumn(String searchText, Properties p,
			String objectName, WebDriver driver) throws Exception {

		int col = 1;
		// Grab the table
		WebElement table = driver.findElement(operation
				.getObject(p, objectName));
		WebElement tableHeader = table.findElement(By
				.xpath("../../../div/div/div"));

		// WebElement tableHeader=
		// driver.findElement(By.xpath(".//div[@id='summaryGrid_groupViewPanel-normal-body']/div/table/../../../div/div/div"));
		List<WebElement> table_columns = tableHeader.findElements(By
				.xpath("div"));
		for (WebElement column : table_columns) {

			Coordinates coordinates = ((Locatable)column).getCoordinates();
            coordinates.inViewPort();
			if (column.getText().trim().equalsIgnoreCase(searchText.trim())) {
				System.out.println(column.getText());
				System.out.println(col);
				return col;
			}
			if (column.isDisplayed() == true)
				col++;
		}

		System.out.println("-1");
		return -1;
	}

	/**
	 * clickOnTableCell : Return the cell value from the grid table
	 * 
	 * @param WebElement
	 *            row : Selected row
	 * @param int columnNumber : column number
	 * @return Pass : Return the cell values
	 */

	public static String clickOnTableCell(int rowIndex, int columnNumber,
			Properties p, String objectName, WebDriver driver) throws Exception {

		UIOperation operation = null;
		operation = new UIOperation(driver);

		// Grab the table
		WebElement table = driver.findElement(operation
				.getObject(p, objectName));

		List<WebElement> columnIndex = table.findElements(By
				.xpath(".//tbody/tr/td[" + columnNumber + "]"));
		System.out.println("NUMBER OF ROWS IN THIS TABLE = "
				+ columnIndex.size());
		int row_num = 1;
		for (WebElement cell : columnIndex) {

			if (row_num == rowIndex) {

				WebElement cells = cell.findElement(By
						.cssSelector("a[class='linkbox acceptlink']"));
				cells.click();

			}
			row_num++;

		}

		return "Pass";

	}
	
	
	/**
	 * clickOnTableCellForGPP : Return the cell value from the grid table
	 * 
	 * @param WebElement
	 *            row : Selected row
	 * @param int columnNumber : column number
	 * @return Pass : Return the cell values
	 */

	public static String clickOnTableCellForGPP(int rowIndex, int columnNumber,
			Properties p, String objectName, WebDriver driver) throws Exception {

		UIOperation operation = null;
		operation = new UIOperation(driver);

		// Grab the table
		WebElement table = driver.findElement(operation
				.getObject(p, objectName));

		List<WebElement> columnIndex = table.findElements(By
				.xpath(".//tbody/tr/td[" + columnNumber + "]"));
		System.out.println("NUMBER OF ROWS IN THIS TABLE = "
				+ columnIndex.size());
		int row_num = 1;
		for (WebElement cell : columnIndex) {

			if (row_num == rowIndex) {

				WebElement cells = cell
						.findElement(By
								.xpath("//img[@src='../dhtmlx/dhtmlxGrid/codebase/imgs/item_chk0.gif']"));
				cells.click();

			}
			row_num++;

		}

		return "Pass";

	}

	/**
	 * clickOnTableCell : Return the cell value from the grid table
	 * 
	 * @param WebElement
	 *            row : Selected row
	 * @param int columnNumber : column number
	 * @return Pass : Return the cell values
	 */

	public static String clickOnTableCellRow(int rowIndex, int columnNumber,
			Properties p, String objectName, WebDriver driver) throws Exception {

		UIOperation operation = null;
		operation = new UIOperation(driver);

		// Grab the table
		WebElement table = driver.findElement(operation
				.getObject(p, objectName));

		List<WebElement> columnIndex = table.findElements(By
				.xpath(".//tbody/tr/td[" + columnNumber + "]"));
		System.out.println("NUMBER OF ROWS IN THIS TABLE = "
				+ columnIndex.size());
		int row_num = 1;
		for (WebElement cell : columnIndex) {

			if (row_num == rowIndex) {
				WebElement cells = cell.findElement(By
						.cssSelector("input[value='on']"));
				cells.click();

			}
			row_num++;

		}

		return "Pass";

	}

	/**
	 * clickOnTableCellChild : Click on specified child object of searched
	 * Column
	 * 
	 * @param WebElement
	 *            row : Selected row
	 * @param int columnNumber : column number
	 * @return Pass : Return the cell values
	 */

	public static String clickOnTableCellChild(int rowIndex, int columnNumber,
			Properties p, String objectName, WebDriver driver) throws Exception {

		UIOperation operation = null;
		operation = new UIOperation(driver);

		// Grab the table
		WebElement table = driver.findElement(operation
				.getObject(p, objectName));

		List<WebElement> columnIndex = table.findElements(By
				.xpath(".//tbody/tr/td[" + columnNumber + "]"));
		System.out.println("NUMBER OF ROWS IN THIS TABLE = "
				+ columnIndex.size());
		int row_num = 1;
		for (WebElement cell : columnIndex) {

			if (row_num == rowIndex) {

				WebElement cells = cell.findElement(By
						.cssSelector("div[class='x-grid-row-checker']"));
				cells.click();

			}
			row_num++;

		}

		return "Pass";

	}

	/**
	 * clickOnTableCellChild1 : Click on specified child object of searched to
	 * open menu Column
	 * 
	 * @param WebElement
	 *            row : Selected row
	 * @param int columnNumber : column number
	 * @return Pass : Return the cell values
	 */

	public static String clickOnTableCellChild1(int rowIndex, int columnNumber,
			Properties p, String objectName, WebDriver driver) throws Exception {

		UIOperation operation = null;
		operation = new UIOperation(driver);

		// Grab the table
		WebElement table = driver.findElement(operation
				.getObject(p, objectName));

		List<WebElement> columnIndex = table.findElements(By
				.xpath(".//tbody/tr/td[" + columnNumber + "]"));
		System.out.println("NUMBER OF ROWS IN THIS TABLE = "
				+ columnIndex.size());
		int row_num = 1;
		for (WebElement cell : columnIndex) {

			if (row_num == rowIndex) {

				
				WebElement cells = cell.findElement(By.name("btnMore"));
				cells.click();

			}
			row_num++;

		}

		return "Pass";

	}

	
	/**
	 * clickOnTableCell : Click on specified child object of searched to
	 * open menu Column
	 * 
	 * @param WebElement
	 *            row : Selected row
	 * @param int columnNumber : column number
	 * @return Pass : Return the cell values
	 */

	public static String clickOnNextTableCell(int rowIndex, int columnNumber,
			Properties p, String objectName,String actionObject, WebDriver driver) throws Exception {

		UIOperation operation = null;
		operation = new UIOperation(driver);

		// Grab the table
		WebElement table = driver.findElement(operation
				.getObject(p, objectName));

		WebElement columnIndex = table.findElement(By
				.xpath(".//tbody/tr[" + rowIndex + "]/td[" + columnNumber + "]"));
		columnIndex.click();
		/*System.out.println("NUMBER OF ROWS IN THIS TABLE = "
				+ columnIndex.size());*/
		
		/*int row_num = 1;
		for (WebElement cell : columnIndex) {

			if (row_num == rowIndex) {

				
				WebElement cells = cell.findElement(operation
						.getObject(p, actionObject));
				cells.click();

			}
			row_num++;

		}*/

		return "Pass";
	}
	

	/**
	 * clickOnTableCell2 : Click on specified child object of searched to
	 * open menu Column
	 * 
	 * @param WebElement
	 *            row : Selected row
	 * @param int columnNumber : column number
	 * @return Pass : Return the cell values
	 */
	
	public static String clickOnNextTableCell2(int rowIndex, int columnNumber,
			Properties p, String objectName,String actionObject, WebDriver driver) throws Exception {

		UIOperation operation = null;
		operation = new UIOperation(driver);

		// Grab the table
		WebElement table = driver.findElement(operation
				.getObject(p, objectName));

		WebElement rowInGrid = table.findElement(By
				.xpath(".//tbody/tr[" + rowIndex + "]"));
		
		/*System.out.println("NUMBER OF ROWS IN THIS TABLE = "
				+ columnIndex.size());*/
		
		/*int row_num = 1;
		for (WebElement cell : columnIndex) {

			if (row_num == rowIndex) {
			*/

				WebElement cells = rowInGrid.findElement(operation
						.getObject(p, actionObject));
				cells.click();

		     return "Pass";
	}
	
	/**
	 * clickOnTableCellChild2 : Click on Edit of specified child object of
	 * searched to open Edit menu Column
	 * 
	 * @param WebElement
	 *            row : Selected row
	 * @param int columnNumber : column number
	 * @return Pass : Return the cell values
	 */

	public static String clickOnTableCellChild2(int rowIndex, int columnNumber,
			Properties p, String objectName, WebDriver driver) throws Exception {

		UIOperation operation = null;
		operation = new UIOperation(driver);

		// Grab the table
		WebElement table = driver.findElement(operation
				.getObject(p, objectName));

		List<WebElement> columnIndex = table.findElements(By
				.xpath(".//tbody/tr/td[" + columnNumber + "]"));
		System.out.println("NUMBER OF ROWS IN THIS TABLE = "
				+ columnIndex.size());
		int row_num = 1;
		for (WebElement cell : columnIndex) {

			if (row_num == rowIndex) {

				WebElement cells = cell.findElement(By.name("btnEdit"));
				cells.click();

			}
			row_num++;

		}

		return "Pass";

	}

	/**
	 * selectInTableCell : Return the cell value from the grid table
	 * 
	 * @param WebElement
	 *            row : Selected row
	 * @param int columnNumber : column number
	 * @return Pass : Return the cell values
	 * @throws Exception
	 */

	public static String selectInTableCell(int rowIndex, int columnNumber,
			String value, Properties p, String objectName, WebDriver driver)
			throws Exception {

		UIOperation operation = null;
		operation = new UIOperation(driver);

		// Grab the table
		WebElement table = driver.findElement(operation
				.getObject(p, objectName));

		List<WebElement> columnIndex = table.findElements(By
				.xpath(".//tbody/tr/td[" + columnNumber + "]"));
		System.out.println("NUMBER OF ROWS IN THIS TABLE = "
				+ columnIndex.size());
		int row_num = 1;
		for (WebElement cell : columnIndex) {

			if (row_num == rowIndex) {
				WebElement cells = cell.findElement(By
						.cssSelector("select[tabIndex='1']"));
				// Select dropdown = new Select(cells);

				// dropdown.selectByVisibleText(value);
				List<WebElement> options = cells.findElements(By
						.tagName("option"));
				for (WebElement option : options) {
					if (option.getText().equals(value)) {
						option.click();
						option.sendKeys(Keys.ARROW_DOWN);
						option.click();
						break;
					}
				}

			}
			row_num++;

		}

		return "Pass";

	}

	/**
	 * verifyTableCellText : Return the cell value from the grid table
	 * 
	 * @param WebElement
	 *            row : Selected row
	 * @param int columnNumber : column number
	 * @return Pass : Return the cell values
	 * @throws Exception
	 */

	public static String verifyTableCellText(int rowIndex, int columnNumber,
			String value, Properties p, String objectName, WebDriver driver)
			throws Exception {

		UIOperation operation = null;
		operation = new UIOperation(driver);
		String cellText = "";
		// Grab the table
		WebElement table = driver.findElement(operation
				.getObject(p, objectName));

		List<WebElement> columnIndex = table.findElements(By
				.xpath(".//tbody/tr/td[" + columnNumber + "]"));
		System.out.println("NUMBER OF ROWS IN THIS TABLE = "
				+ columnIndex.size());
		int row_num = 1;
		for (WebElement cell : columnIndex) {
			if (row_num == rowIndex) {

				List<WebElement> childs = cell.findElements(By
						.cssSelector("input[type='text']"));
				// System.out.println("CellSize:" + childs.size());

				if (childs.size() > 0) {

					for (WebElement childElement : childs) {
						cellText = childElement.getAttribute("value");
						System.out.println("CellValue:" + cellText);
						cellText = cellText.replace(",", "");
						System.out.println("Expected value:" + value
								+ ", CellValue:" + cellText);
						if (cellText.contains(value)) {
							return "Pass";
						} else {
							return "Fail";
						}
					}

				} else {
					cellText = cell.getText();
					cellText = cellText.replace(",", "");
					// System.out.println("CellValue:" + cellText);
					System.out.println("Expected value:" + value
							+ ", CellValue:" + cellText);
					if (cellText.contains(value)) {
						return "Pass";
					} else {
						return "Fail";
					}
				}
			}
			row_num++;

		}

		return "Pass";

	}

	/**
	 * verifyTableCellText1 : Return the cell value from the grid table using
	 * xpath
	 * 
	 * @param WebElement
	 *            row : Selected row
	 * @param int columnNumber : column number
	 * @return Pass : Return the cell values
	 * @throws Exception
	 */

	public static String verifyTableCellText1(int rowIndex, int columnNumber,
			String value, Properties p, String objectName, WebDriver driver) {
		try {
			WebElement table = driver.findElement(operation.getObject(p,
					objectName));
			WebElement searchElement = table.findElement(By
					.xpath(".//tbody/tr[" + (rowIndex + 1) + "]/td["
							+ columnNumber + "]/*[contains(text(),'" + value
							+ "')]"));
			if (value.contains(searchElement.getText())) {
				return "Pass";
			} else {
				return "Fail";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Fail";
		}
	}

	/**
	 * getDropDownValues : Return the Values from the drop down values
	 * 
	 * @param WebDriver
	 *            : Selection of the WebElement
	 * @param searchText
	 *            : Search the specified text
	 * @param objectName
	 *            : Locator of the Element
	 * @paramProperties p : Get the values from the properties file
	 * @return Pass :
	 */

	public static String getDropDownValues(String searchText, Properties p,
			String objectName, WebDriver driver) throws Exception {

		UIOperation operation = null;
		operation = new UIOperation(driver);
		// Grab the table
		WebElement debitaccountdropdown = driver.findElement(operation
				.getObject(p, objectName));
		Select select = new Select(debitaccountdropdown);
		List<WebElement> options = select.getOptions();
		for (WebElement listvalues : options) {
			if (listvalues.getText().equals(searchText)) {
				listvalues.click();
			}
		}
		return "Pass";

	}

	/**
	 * getObjectverifySafely : Verify the actual object and expected object
	 * 
	 * @paramString actualValue : actual result
	 * @param String
	 *            expectedValue : expected result
	 * @param boolean resultMatch : description
	 * @return strResult : Result Match
	 */

	public static String getObjectverifySafely(String actualValue,
			String expectedValue, boolean resultMatch) {
		String strResult = "Fail";
		Debug.traceMessage = "Expected Value : " + expectedValue + "<br> Actual value : " + actualValue;
		
		if (actualValue.equalsIgnoreCase(expectedValue.trim())) {
			if (resultMatch) {
				strResult = "Pass";
			} else {
				strResult = "Fail";
			}
		} else {
			if (resultMatch) {
				strResult = "Fail";
			} else {
				strResult = "Pass";
			}
		}

		return strResult;
	}

	/**
	 * WaitForElementVisible : Dynamic Wait for a element to visible on the
	 * page,can be executed only when user is on web page
	 * 
	 * @param WebDriver
	 *            : Selection of the WebElement
	 * @param ResponseTime
	 *            : Waiting time
	 * @param objectName
	 *            : Locator of the Element
	 * @paramProperties p : Get the values from the properties file
	 * @return False : Time out Exception if it reach the max waiting time and
	 *         element is not loaded on the page
	 */

	public static boolean waitForElementVisible(WebDriver driver,
			int maxTimeout, By Wl) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, maxTimeout);
			wait.until(ExpectedConditions.visibilityOfElementLocated(Wl));
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	/**
	 * WaitForElementNOtVisible : Dynamic Wait for a element to visible on the
	 * page,can be executed only when user is on web page
	 * 
	 * @param WebDriver
	 *            : Selection of the WebElement
	 * @param ResponseTime
	 *            : Waiting time
	 * @param objectName
	 *            : Locator of the Element
	 * @paramProperties p : Get the values from the properties file
	 * @return False : Time out Exception if it reach the max waiting time and
	 *         element is not loaded on the page
	 */

	public static boolean waitForElementNotVisible(WebDriver driver,
			int maxTimeout, By Wl) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, maxTimeout);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(Wl));
		return true;
	}

	/**
	 * WaitForElementVisible : Dynamic Wait for a element to visible on the
	 * page,can be executed only when user is on web page
	 * 
	 * @param WebDriver
	 *            : Selection of the WebElement
	 * @param ResponseTime
	 *            : Waiting time
	 * @param objectName
	 *            : Locator of the Element
	 * @paramProperties p : Get the values from the properties file
	 * @return False : Time out Exception if it reach the max waiting time and
	 *         element is not loaded on the page
	 */

	public static boolean waitForElementEnable(WebDriver driver,
			int maxTimeout, By Wl) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, maxTimeout);
			wait.until(ExpectedConditions.elementToBeClickable(Wl));
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	public static boolean waitForJsLoad(WebDriver driver, int maxTimeout) {
		final JavascriptExecutor js = (JavascriptExecutor) driver;

		try {
			WebDriverWait wait = new WebDriverWait(driver, maxTimeout);

			// wait for jQuery to load
			ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
				@Override
				public Boolean apply(WebDriver driver) {
					try {
						System.out.println(js
								.executeScript("return jQuery.active"));
						return ((Long) js.executeScript("return jQuery.active") == 0);
					} catch (WebDriverException e) {
						return true;
					}
				}
			};

			// wait for Javascript to load
			ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
				@Override
				public Boolean apply(WebDriver driver) {
					return js.executeScript("return document.readyState")
							.toString().equals("complete");
				}
			};

			return wait.until(jQueryLoad) && wait.until(jsLoad);
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	/**
	 * setResultFile: Save the result on specified location
	 * 
	 * @param fileType
	 * @param filename
	 * @return fileName with Path
	 */

	public static String setResultFile(String fileType, String filename)
			throws Exception {
		ReadObject object = new ReadObject();
		Properties allEnvObjects = object.getObjectRepository("environment");
		String resultFileProduct = allEnvObjects.getProperty("resultFileProduct");
		String resultFileCustomer = allEnvObjects.getProperty("resultFileCustomer");
		String resultFileBuild = allEnvObjects.getProperty("resultFileBuild");

		
		Path newFile = null;
		// Get Date and System Info
		String todayDateString = GlobalLib.getConvertedDateString(new Date(), "dd MMM yyyy hh:mm:ssaa");
		// get current date time with Date()
		
		
		// System.out.println(dateFormat.format(date));
		// To identify the system
		InetAddress ownIP = InetAddress.getLocalHost();
		switch (fileType.toUpperCase()) {
		case "INDEX":
			
			String destination = System.getProperty("user.dir") + "//Results"; // main  location
			File theFile = new File(destination);
			theFile.mkdirs();
			newFile = Paths.get(System.getProperty("user.dir"), "Results",
					filename);
			indexFileName = filename;
			try {
				Files.deleteIfExists(newFile);
				newFile = Files.createFile(newFile);
			} catch (IOException ex) {
				System.out.println("Error creating INDEX file");
			}
			System.out.println(Files.exists(newFile));
			
			try (BufferedWriter writer = Files.newBufferedWriter(newFile,
					Charset.defaultCharset())) {
				writer.append("<html> <head> <meta http-equiv=Content-Languagecontent=en-us> <meta http-equiv=Content-Typecontent=text/html; charset=windows-1252:> <title>Test Automation Execution Summery Report </title> </head> <body> <blockquote> <table border=2 bordercolor=#000000 id=table1 width=1000 height=31 bordercolorlight= #000000> <tr> <td COLSPAN = 10 bgcolor = #FFF8C6> <p align=center><font color=#000080 size=4 face=\"Copperplate Gothic Bold\">Test Automation Execution Result</font><font face=\"Copperplate Gothic Bold\"></font> </p>");
				writer.append("<p align=center><font color=#000080 size=3 face=\"Copperplate Gothic Bold\">Product Name: " +  resultFileProduct + "</font><font face=\"Copperplate Gothic Bold\"></font> </p>");
				writer.append("<p align=center><font color=#000080 size=3 face=\"Copperplate Gothic Bold\">Customer: " + resultFileCustomer + "</font><font face=\"Copperplate Gothic Bold\"></font> </p>");
				writer.append("<p align=center><font color=#000080 size=3 face=\"Copperplate Gothic Bold\">Build / Release:" + resultFileBuild + " </font><font face=\"Copperplate Gothic Bold\"></font> </p></td></tr>");
				writer.append("<tr><td COLSPAN = 10 bgcolor = #FFF8C6><p align=justify><b><font color=#000080 size=2 face= Verdana> DATE:"
						+ todayDateString);
				writer.append("<p align=justify><b><font color=#000080 size=2 face= Verdana> SystemInfo: "
						+ ownIP.getCanonicalHostName() + "</td></tr><tr></tr>");
				writer.append("<tr bgcolor=#8A4117><td width=300><p align=center><b><font color = white face=Arial Narrow size=2>Group Name</b>");
				writer.append("<td width=300><p align=center><b><font color = white face=Arial Narrow size=2>Description</b></td>");
				writer.append("<td width=300><p align=center><b><font color = white face=Arial Narrow size=2>Result</b></td>");
				writer.append("<td width=300><p align=center><b><font color = white face=Arial Narrow size=2>Pass Count</b></td>");
				writer.append("<td width=300><p align=center><b><font color = white face=Arial Narrow size=2>Fail Count</b></td>");
				writer.append("<td width=300><p align=center><b><font color = white face=Arial Narrow size=2>Duration (HH:MM:SS)</b></td></blockquote>");
				writer.flush();
			} catch (IOException exception) {
				System.out.println("Error writing to INDEX file");
			}
			break;
		case "GROUP":
			String[] parts = filename.split(Separator.SEPARATOR_COLON);
			filename = parts[0];
			String grpName = parts[1];
			groupFileName = filename;
			newFile = Paths.get(System.getProperty("user.dir"), "Results",
					filename);
			try {
				Files.deleteIfExists(newFile);
				newFile = Files.createFile(newFile);
			} catch (IOException ex) {
				System.out.println("Error creating GROUP file");
			}
			
			System.out.println(Files.exists(newFile));
			try (BufferedWriter writer = Files.newBufferedWriter(newFile,
					Charset.defaultCharset())) {
				writer.append("<html> <head> <meta http-equiv=Content-Languagecontent=en-us> <meta http-equiv=Content-Typecontent=text/html; charset=windows-1252:> <title>Test Automation Execution Summery Report </title> </head> <body> <blockquote> <table border=2 bordercolor=#000000 id=table1 width=1000 height=31 bordercolorlight= #000000> <tr> <td COLSPAN = 10 bgcolor = #FFF8C6> <p align=center><font color=#000080 size=4 face=\"Copperplate Gothic Bold\">Test Automation Execution Result for Group: "
						+ grpName
						+ "</font><font face=\"Copperplate Gothic Bold\"></font> </p>");
				writer.append("<p align=center><font color=#000080 size=3 face=\"Copperplate Gothic Bold\">Product Name: "+  resultFileProduct + "</font><font face=\"Copperplate Gothic Bold\"></font> </p>");
				writer.append("<p align=center><font color=#000080 size=3 face=\"Copperplate Gothic Bold\">Customer: " + resultFileCustomer + "</font><font face=\"Copperplate Gothic Bold\"></font> </p>");
				writer.append("<p align=center><font color=#000080 size=3 face=\"Copperplate Gothic Bold\">Build / Release: " + resultFileBuild + " </font><font face=\"Copperplate Gothic Bold\"></font> </p></td></tr>");
				writer.append("<tr><td COLSPAN = 10 bgcolor = #FFF8C6><p align=justify><b><font color=#000080 size=2 face= Verdana> DATE:"
						+ todayDateString);
				writer.append("<p align=justify><b><font color=#000080 size=2 face= Verdana> SystemInfo: "
						+ ownIP.getCanonicalHostName() + "</td></tr><tr></tr>");
				writer.append("<tr bgcolor=#8A4117><td width=300><p align=center><b><font color = white face=Arial Narrow size=2>TestCase ID</b>");
				writer.append("<td width=300><p align=center><b><font color = white face=Arial Narrow size=2>Business Flow</b></td>");
				writer.append("<td width=300><p align=center><b><font color = white face=Arial Narrow size=2>Description</b></td>");
				writer.append("<td width=300><p align=center><b><font color = white face=Arial Narrow size=2>Result</b></td>");
				writer.append("<td width=300><p align=center><b><font color = white face=Arial Narrow size=2>Start Time</b></td>");
				writer.append("<td width=300><p align=center><b><font color = white face=Arial Narrow size=2>End Time</b></td>");
				writer.append("<td width=300><p align=center><b><font color = white face=Arial Narrow size=2>Duration (HH:MM:SS)</b></td></blockquote></body></html>");
				writer.flush();
			} catch (IOException exception) {
				System.out.println("Error writing to GROUP file");
			}
			break;
		case "TESTCASE":
			String[] tcparts = filename.split(Separator.SEPARATOR_COLON);
			filename = tcparts[0];
			String testcaseName = tcparts[1];

			newFile = Paths.get(System.getProperty("user.dir"), "Results",
					filename);
			try {
				Files.deleteIfExists(newFile);
				newFile = Files.createFile(newFile);
			} catch (IOException ex) {
				System.out.println("Error creating TESTCASE file");
			}
			System.out.println(Files.exists(newFile));
			try (BufferedWriter writer = Files.newBufferedWriter(newFile,
					Charset.defaultCharset())) {
				writer.append("<html> <head> <meta http-equiv=Content-Languagecontent=en-us> <meta http-equiv=Content-Typecontent=text/html; charset=windows-1252:> <title>Test Automation Execution Summery Report </title> </head> <body> <blockquote> <table border=2 bordercolor=#000000 id=table1 width=1000 height=31 bordercolorlight= #000000> <tr> <td COLSPAN = 10 bgcolor = #FFF8C6> <p align=center><font color=#000080 size=4 face=\"Copperplate Gothic Bold\">Test Automation Execution Result for Test Case: "
						+ testcaseName
						+ "</font><font face=\"Copperplate Gothic Bold\"></font> </p>");
				writer.append("<p align=center><font color=#000080 size=3 face=\"Copperplate Gothic Bold\">Product Name: " +  resultFileProduct + "</font><font face=\"Copperplate Gothic Bold\"></font> </p>");
				writer.append("<p align=center><font color=#000080 size=3 face=\"Copperplate Gothic Bold\">Customer: " + resultFileCustomer + "</font><font face=\"Copperplate Gothic Bold\"></font> </p>");
				writer.append("<p align=center><font color=#000080 size=3 face=\"Copperplate Gothic Bold\">Build / Release: " + resultFileBuild + " </font><font face=\"Copperplate Gothic Bold\"></font> </p></td></tr>");
				writer.append("<tr><td COLSPAN = 10 bgcolor = #FFF8C6><p align=justify><b><font color=#000080 size=2 face= Verdana> DATE:"
						+ todayDateString);
				writer.append("<p align=justify><b><font color=#000080 size=2 face= Verdana> SystemInfo: "
						+ ownIP.getCanonicalHostName() + "</td></tr><tr></tr>");
				writer.append("<tr bgcolor=#8A4117><td width=300><p align=center><b><font color = white face=Arial Narrow size=2>Step Description</b>");
				writer.append("<td width=300><p align=center><b><font color = white face=Arial Narrow size=2>Action</b></td>");
				writer.append("<td width=300><p align=center><b><font color = white face=Arial Narrow size=2>Object / Data</b></td>");
				writer.append("<td width=300><p align=center><b><font color = white face=Arial Narrow size=2>Data</b></td>");
				writer.append("<td width=300><p align=center><b><font color = white face=Arial Narrow size=2>Result</b></td>");
				writer.append("<td width=300><p align=center><b><font color = white face=Arial Narrow size=2>Trace</b></td>");
				writer.append("<td width=300><p align=center><b><font color = white face=Arial Narrow size=2>Duration (HH:MM:SS)</b></td></blockquote></body></html>");
				writer.flush();
			} catch (IOException exception) {
				System.out.println("Error writing to INDEX file");
			}
			break;
		default:
			break;

		}

		return newFile.getFileName().toString();
	}

	/**
	 * getResultStamp: Save the result with time stamp on specified location
	 * 
	 * @throws exception
	 * @return fileName with rsStamp
	 */

	public static String getResultStamp() throws Exception {
		String rsStamp = null;
		ReadAFWExcelFile file = new ReadAFWExcelFile();

		// Initiate the Group Results file
		DateFormat dateFormat = new SimpleDateFormat("ddMMMyyyy__hhmmssaa");
		// get current date time with Date()
		Date date = new Date();
		// System.out.println(dateFormat.format(date));
		// To identify the system
		InetAddress ownIP = InetAddress.getLocalHost();
		rsStamp = dateFormat.format(date); // + "_" + ownIP.getHostAddress();

		return rsStamp;

	}

	/**
	 * getTimeDiff: Save the result with time stamp
	 * 
	 * @param String
	 *            dateStart, String dateStop
	 * @return timeDiff
	 */

	public static String getTimeDiff(String dateStart, String dateStop) {
		String timeDiff = null;

		// HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

		Date d1 = null;
		Date d2 = null;

		try {
			d1 = format.parse(dateStart);
			d2 = format.parse(dateStop);

			// in milliseconds
			long diff = d2.getTime() - d1.getTime();

			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);

			System.out.print(diffDays + " days, ");
			System.out.print(diffHours + " hours, ");
			System.out.print(diffMinutes + " minutes, ");
			System.out.print(diffSeconds + " seconds.");
			timeDiff = "" + diffHours + ":" + diffMinutes + ":" + diffSeconds;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return timeDiff;
	}

	/**
	 * naviagteTO
	 * 
	 * @param p
	 * @param object
	 * @param driver
	 * 
	 */

	public static String navigateTo(Properties p, String objectName,
			WebDriver driver, int maxTimeout) throws Exception {

		UIOperation operation = null;
		operation = new UIOperation(driver);
		List<String> items = Arrays.asList(objectName.split(Separator.SEPARATOR_LEFT_ARRAW_SPACE));

		for (int i = 0; i < items.size(); i++) {
			if (GlobalLib.waitForElementVisible(driver, 30,
					operation.getObject(p, items.get(i)))) {
				WebElement clickWindow = driver.findElement(operation
						.getObject(p, items.get(i)));
				clickWindow.click();
			} else {
				WebElement navi = driver.findElement(operation.getObject(p,
						items.get(i) + "_1"));
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", navi);
			}
		}

		return "Pass";
	}

	/**
	 * naviagteTO
	 * 
	 * @param p
	 * @param object
	 * @param driver
	 * 
	 */

/*	public static String switchTOIframe(String value, WebDriver driver,
			int maxTimeout) throws Exception {

		WebElement iframe = driver.findElement(By
				.xpath("//iframe[@src='../sd-html/ActionInputSearch.html']"));
		driver.switchTo().frame(iframe);

		return "Pass";
	}
	
	
	
	/**
	 * naviagteTO
	 * 
	 * @param p
	 * @param object
	 * @param driver
	 * 
	 */

	public static String switchTOIframe(String objectName,Properties p,WebDriver driver,
			int maxTimeout) throws Exception {

		WebElement iframe = driver.findElement(operation.getObject(p,
				objectName));
		driver.switchTo().frame(iframe);

		return "Pass";
	}
	
	

	/**
	 * getStringPattern: Returns String match pattern
	 * 
	 * @param String
	 *            startWith:
	 * @param String
	 *            endWith
	 * @param String
	 *            str
	 * 
	 */

	public static String getStringPattern(String startWith, String endWith,
			String str) {
		String strFound = getStringPatternGen(startWith, endWith, str);
		System.out.println(strFound.length());
		// This code is specific to GCP.. need to be made generic later
		// The code is of 12 length
		if (strFound.length() == 12) {
			strFound = strFound.substring(0, 12);
		} else {
			strFound = strFound.substring(0, 9);
		}
		return strFound;

	}

	public static String getPoiCellValue(Cell Cell) {
		if (Cell != null) {
			return Cell.getStringCellValue();
		} else {
			System.out.println("No value in the cell");
			return "Pass";
		}

	}

	/**
	 * multipleWindowshandle: Returns String match pattern
	 * 
	 * @param String
	 *            startWith:
	 * @param String
	 *            endWith
	 * @param String
	 *            str
	 * @return
	 * 
	 */
	
	public static String multipleWindowshandle(Properties p, String objectName,
			WebDriver driver, int maxTimeout, String action) throws Exception {

		if (action.equalsIgnoreCase("REMOVE")) {
			Debug.setParentWindowHandle.remove(Debug.setParentWindowHandle
					.size() - 1);
			System.out.println("No of Windows: "
					+ Debug.setParentWindowHandle.size());
			return "Pass";
		}

		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		Set<String> AllWindowHandles = driver.getWindowHandles();
		int listSize = Debug.setParentWindowHandle.size();
		System.out.println("No of Windows: " + listSize);
		for (int i = 0; i < AllWindowHandles.size(); i++) {

			String fromWindows = (String) AllWindowHandles.toArray()[i];
			if (Debug.setParentWindowHandle
					.contains(AllWindowHandles.toArray()[i])) {

			} else {
				Debug.setWindowHandle(listSize + 1, fromWindows);
				WebDriver oldDriver = driver;
				WebDriver newDriver = driver.switchTo().window(fromWindows);
				
				if (newDriver.getPageSource().equals("")) {
					driver.close();
					driver = oldDriver;
					continue;
				} else {
					driver.switchTo().window(fromWindows);
					System.out.println("Next Window Title : " + newDriver.getTitle());
					//System.out.println("Next Window Source : " + newDriver.getPageSource());
				}
				Thread.sleep(2500);
				driver.manage().window().maximize();
				Toolkit toolkit = Toolkit.getDefaultToolkit();
				int Width = (int) toolkit.getScreenSize().getWidth();
				int Height = (int) toolkit.getScreenSize().getHeight();
				driver.manage().window().setSize(new Dimension(Width, Height));
				driver.manage().window().maximize();

			}

		}
		System.out.println("Action: " + action);
		
		if (action.equalsIgnoreCase("CLOSE")) {
			//driver.close();
			driver.switchTo().window(Debug.getWindowHandle(listSize)).close();
			driver.switchTo().window(Debug.getWindowHandle(listSize - 1));
			Debug.setParentWindowHandle.remove(listSize - 1);
		}
		
		if (action.equalsIgnoreCase("MINIMIZE")) {
			         driver.manage().window().setSize(new Dimension(0,0));
			}
		return "Pass";
	}


	public static String keyPressHandle(String key) throws AWTException {
		Robot r;
		r = new Robot();
		switch (key.toUpperCase()) {

		case "F2":
			r.keyPress(KeyEvent.VK_F2);
			break;

		case "F3":
			r.keyPress(KeyEvent.VK_F3);
			break;
		case "F4":
			r.keyPress(KeyEvent.VK_F4);
			break;
		case "F7":
			r.keyPress(KeyEvent.VK_F7);
			break;
		case "F9":
			r.keyPress(KeyEvent.VK_F9);
			break;
		case "F10":
			r.keyPress(KeyEvent.VK_F10);
			break;
		case "F11":
			r.keyPress(KeyEvent.VK_F11);
			break;
		case "F12":
			r.keyPress(KeyEvent.VK_F12);
			break;
		case "PAGEUP":
			r.keyPress(KeyEvent.VK_PAGE_UP);
			break;
		case "HOME":
			r.keyPress(KeyEvent.VK_HOME);
			break;
		case "TAB":
			r.keyPress(KeyEvent.VK_TAB);
			break;
		case "ENTER":
			r.keyPress(KeyEvent.VK_ENTER);
			break;
		}
		return null;
	}

	public static String keyPressONElement(WebElement r, String key) {

		switch (key.toUpperCase()) {

		case "F2":
			r.sendKeys(Keys.F2);
			break;

		case "F3":
			r.sendKeys(Keys.F3);
			break;
		case "F4":
			r.sendKeys(Keys.F4);
			break;
		case "F5":
			r.sendKeys(Keys.F5);
			break;
		case "F6":
			r.sendKeys(Keys.F6);
			break;
		case "F7":
			r.sendKeys(Keys.F7);
			break;
		case "F8":
			r.sendKeys(Keys.F8);
			break;
		case "F9":
			r.sendKeys(Keys.F9);
			break;
		case "F10":
			r.sendKeys(Keys.F10);
			break;
		case "F11":
			r.sendKeys(Keys.F11);
			break;
		case "F12":
			r.sendKeys(Keys.F12);
			break;
		case "TAB":
			r.sendKeys(Keys.TAB);
			break;
		case "ENTER":
			r.sendKeys(Keys.ENTER);
			break;
		case "ARROW_DOWN":
			r.sendKeys(Keys.ARROW_DOWN);
			break;
		default:
			System.out.println("Key not defined in fn");
			break;
		}
		return null;
	}

	public static String testStepRunner(Row xlRow, String description,
			String skipFlag, String keyword, String objectName, String value,
			Properties allObjects, ReadAFWExcelFile file,
			String testCaseFilePath, int valueColumn, String imageFolder)
			throws IOException, AWTException, ClassNotFoundException, SQLException, ProvisoException {
		Debug.VALUE = value;
		Debug.traceMessage = "";
		// SOAPUI
		WsdlProject project = null;
		String imageFile = "";
		String strTcResult = "Pass";
		String tcstartDate = GlobalLib.getConvertedDateString(new Date(), "MM/dd/yyyy HH:mm:ss");
		
		if (value == null || value.isEmpty() || skipFlag.equalsIgnoreCase("Y")) {
			
			strTcResult = "Skiped";
			/*
			 * try (PrintWriter out = new PrintWriter(new BufferedWriter( new
			 * FileWriter(testCaseFilePath, true)))) {
			 * out.println("<tr bgcolor = #FFFFFF>"); out.println(
			 * "<td width=400><p align=center><font face=Verdana size=2>" +
			 * description + "</td>"); out.println(
			 * "<td width=400><p align=center><font face=Verdana size=2>" +
			 * keyword + "</td>"); out.println(
			 * "<td width=400><p align=center><font face=Verdana size=2>" +
			 * objectName + "</td>"); out.println(
			 * "<td width=400><p align=center><font face=Verdana size=2>" +
			 * value + "</td>"); out.println(
			 * "<td width=400><p align=center><font face=Verdana size=2>" +
			 * strTcResult + "</td>"); out.println(
			 * "<td width=400><p align=center><font face=Verdana size=2>" +
			 * "00:00:00" + "</td>"); out.println("</td></tr>"); out.close(); }
			 * catch (IOException e) { // exception handling left as an exercise
			 * for // the // reader }
			 */

			return strTcResult;
		}
		value = GlobalLib.replacePropertiesFromString(value);
		
		try {
			String resourcesPath = System.getProperty("user.dir") + File.separator + "Resources" + File.separator;
			if (allObjects.getProperty("execution_mode").equalsIgnoreCase("development")) {
				resourcesPath = allObjects.getProperty("resources_path");
			}
			
			
			if (keyword.equalsIgnoreCase("openbrowser")) {
								
				switch (value.toUpperCase()) {
				case "FF":
					Debug.BrowserType = "FF";
					Debug.closeBrowser("firefox.exe");
					//System.setProperty("webdriver.gecko.driver", "D:\\Executables\\Resources\\geckodriver.exe");
					//DesiredCapabilities capabilities = DesiredCapabilities.firefox();
					//capabilities.setCapability("marionette", true);
					//WebDriver driver = new FirefoxDriver(capabilities);
					
					// Runtime.getRuntime().exec("taskkill /F /IM firefox.exe /T");
					File pathToBinary = new File("C:\\Program Files\\Mozilla Firefox\\firefox.exe");
					FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
					FirefoxProfile firefoxProfile = new FirefoxProfile();       
					webdriver = new FirefoxDriver(ffBinary,firefoxProfile);
					
					//webdriver = new FirefoxDriver(capabilities);
					webdriver.manage().window().maximize();
					break;

				case "IE":
					Thread.sleep(2000);
					Debug.BrowserType = "IE";
					//Debug.closeBrowser("iexplore.exe");
					//Runtime.getRuntime().exec(resourcesPath + "CloseAllIeBrowsers.exe");
					Thread.sleep(3000);
					DesiredCapabilities caps = DesiredCapabilities
							.internetExplorer();
					caps.setCapability(
							InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
							true);
					caps.setCapability(InternetExplorerDriver.IE_SWITCHES, "-private");
					caps.setCapability("EnableNativeEvents", false);
					caps.setCapability("ignoreZoomSetting", true);
					
					System.setProperty("webdriver.ie.driver", resourcesPath + "IEDriverServer.exe");
					webdriver = new InternetExplorerDriver(caps);
					webdriver.findElement(By.tagName("html")).sendKeys(Keys.chord(Keys.CONTROL, "0"));
					webdriver.manage().window().maximize();
					break;

				case "CHROME":
					String browserPath = "";
					Debug.BrowserType = "CHROME";
					//Debug.closeBrowser("chrome.exe");
					//Runtime.getRuntime().exec("taskkill /F /IM chrome.exe /T");
					ChromeOptions options = new ChromeOptions();
					if(allObjects.containsKey("chrome_browser_path")){
					browserPath = allObjects.getProperty("chrome_browser_path");
					System.out.println("chrome_browser_path:-"+browserPath);
					}
					if(null != browserPath && !browserPath.isEmpty())
					 {	
						options.setBinary(browserPath+"chrome.exe");
					 }
					//ChromeOptions options = new ChromeOptions();
					System.setProperty("webdriver.chrome.driver",resourcesPath +"chromedriver.exe");
					HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
					chromePrefs.put("download.prompt_for_download", true);
					HashMap<String, Object> chromeOptionsMap = new HashMap<String, Object>();
					options.setExperimentalOption("prefs", chromePrefs);
					DesiredCapabilities cap = DesiredCapabilities.chrome();
					cap.setCapability(ChromeOptions.CAPABILITY, chromeOptionsMap);

					cap.setCapability(ChromeOptions.CAPABILITY, options);
					cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
					webdriver = new ChromeDriver(cap);
					webdriver.manage().window().maximize();
					break;

				default:
					Debug.BrowserType = "";
					webdriver = null;
					throw new Exception("Please specify browser type CHROME/FF/IE");
				}
				String strParentWindowHandle = webdriver.getWindowHandle();
				Debug.setWindowHandle(1, strParentWindowHandle);
				operation = new UIOperation(webdriver);
				strTcResult = "Pass";

			} else if (keyword.equalsIgnoreCase("closebrowser")) {
				Thread.sleep(1000);
				Debug.setParentWindowHandle.clear();
				webdriver.close();
				webdriver.quit();
				Runtime.getRuntime().exec(
						"taskkill /F /IM IEDriverServer.exe /T");
				// Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe /T");
				// Runtime.getRuntime().exec("taskkill /F /IM firefox.exe /T");
				 Runtime.getRuntime().exec("taskkill /F /IM chrome.exe /T");
				 Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");

				strTcResult = "Pass";
			} else if (keyword.toUpperCase().startsWith("BK_")) {
				//System.out.println(keyword);
				Sheet AFWKeywordSheet = file.readExcel(
						System.getProperty("user.dir") + "\\",
						"/testCases/businesskeywords.xlsx",
						keyword.toUpperCase());
				strTcResult = BusinessKeywordDriver(allObjects,
						AFWKeywordSheet, xlRow, testCaseFilePath, valueColumn,
						imageFolder);
				description = "";
				objectName = "End";
				value = "";
			}// SOAPUI
			else if (keyword.equalsIgnoreCase("LOADSOAPUIPROJECT")) {
				
				String soapProjectPath = System.getProperty("user.dir") + File.separator + "testCases" + File.separator + value;
				// added to solve the problem of soap ui with browser
				if (SOAUIRunner.project == null || !soapProjectPath.equals(SOAUIRunner.project.getPath())) {
				    ProxySelector proxy = ProxySelector.getDefault();
				    SOAUIRunner.project = new WsdlProject(soapProjectPath);
				    ProxySelector.setDefault(proxy);
	                Thread.sleep(10000);
                } 
				
				strTcResult = "Pass";

			} else if (keyword.equalsIgnoreCase("COMMENT")) {
				description = "";
				keyword = value;
				objectName = "";
				value = "";
				strTcResult = "";
			} else {
			    if (operation == null) {
			        operation = new UIOperation();
			    }
				strTcResult = operation.perform(allObjects, keyword,
						objectName, value, imageFolder);
			}
		} catch (Exception e) {
			ProvisoException.exceptionHandler(e, null);
			InternalDbOperation.updateTestStepResult("FAIL");
			
			strTcResult = "Fail";
		}
		
//		System.out.println(keyword);
		// get current date time with Date()
		String tcendDate = GlobalLib.getConvertedDateString(new Date(), "MM/dd/yyyy HH:mm:ss");
		String resultline = "<td width=400><p align=center><font face=Verdana size=2>"
				+ strTcResult + "</td>";
		String bgColor = "#FFFFFF";
		// Sense Bg Color
		if (strTcResult.equalsIgnoreCase("Fail")
				|| keyword.equalsIgnoreCase("CAPTURESCREEN")
				|| strTcResult.equalsIgnoreCase("FailException")) {

			if (strTcResult.equalsIgnoreCase("FailException")) {
				imageFile = Debug.imageFile;
				strTcResult = "Fail";
				bgColor = "#FA5858";
			} else if (strTcResult.equalsIgnoreCase("Fail")) {
				imageFile = ScreenshotUtility
						.ScreenShot(webdriver, imageFolder);
				bgColor = "#FA5858";
			} else {
				imageFile = ScreenshotUtility
						.ScreenShot(webdriver, imageFolder);
			}
			String[] arrIMG = imageFile.split(Separator.SEPARATOR_DIR);
			String fdr = arrIMG[arrIMG.length - 2];
			String fl = arrIMG[arrIMG.length - 1];

			imageFile = fdr + "\\" + fl;

			System.out.println(imageFile);
			resultline = "<td width=400><p align=center><b><font face=Verdana size=2><a href=\""
					+ imageFile
					+ "\" target=\"_blank\">"
					+ strTcResult
					+ "</a> </href></font></b></td>";

		}
		
		// get time difference
		String tctimeDiff = GlobalLib.getTimeDiff(tcstartDate, tcendDate);
		
		StepResult stepResult = new StepResult();
		stepResult.setDuration(tctimeDiff);
		stepResult.setStepDescription(description);
		stepResult.setObject(objectName);
		stepResult.setKeyword(keyword);
		stepResult.setResult(strTcResult);
		stepResult.setTestData(Debug.VALUE);
		Debug.stepResults.add(stepResult); 
		InternalDbOperation.updateTestStepResult(strTcResult);
		
		try (PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter(testCaseFilePath, true)))) {
			out.println("<tr bgcolor = " + bgColor + ">");
			out.println("<td width=400><p align=center><font face=Verdana size=2>"
					+ description + "</td>");
			out.println("<td width=400><p align=center><font face=Verdana size=2>"
					+ keyword + "</td>");
			out.println("<td width=400><p align=center><font face=Verdana size=2>"
					+ objectName + "</td>");
			out.println("<td width=400><p align=center><font face=Verdana size=2>"
					+ Debug.VALUE + "</td>");
			out.println(resultline);
			out.println("<td width=400><p align=center><font face=Verdana size=2>"
                + Debug.traceMessage + "</td>");
			out.println("<td width=400><p align=center><font face=Verdana size=2>"
					+ tctimeDiff + "</td>");
			out.println("</td></tr>");
			out.close();
		} catch (Exception e) {
			// exception handling left as an exercise for the
			// reader
		    e.printStackTrace();
			System.out.println("## ERROR ##" + e);
			return "Fail";
		}
		
		return strTcResult;

	}

	public static String BusinessKeywordDriver(Properties allObjects,
			Sheet AFWKWSheet, Row xlTCRow, String testCaseFilePath,
			int valueColumn, String imagePath) throws IOException {
		int rowCount = AFWKWSheet.getLastRowNum() - AFWKWSheet.getFirstRowNum();
		String keyword, skipFlag, screenName, description, objectName, value, strResult = "Pass", strKWResult = "Pass";
		UIOperation operation;
		operation = new UIOperation(webdriver);
		Row xlRow = null;

		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		// get current date time with Date()

		// Iterate through rows

		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(testCaseFilePath, true)));
			out.println("<tr bgcolor = #FFFFFF>");
			out.println("<td width=400><p align=center><font face=Verdana size=2>"
					+ xlTCRow.getCell((short) 0).getStringCellValue() + "</td>");
			out.println("<td width=400><p align=center><font face=Verdana size=2>"
					+ xlTCRow.getCell((short) 1).getStringCellValue() + "</td>");
			out.println("</td></tr>");
			// Index of value column

			for (int i = 1; i <= rowCount; ++i) {

				Date sDate = new Date();
				xlRow = AFWKWSheet.getRow(i);
				description = GlobalLib.getPoiCellValue(xlRow
						.getCell((short) 0));
				skipFlag = GlobalLib.getPoiCellValue(xlRow.getCell((short) 1))
						.toUpperCase();
				// Not used as of now
				/*
				 * screenName = xlRow.getCell((short) 2).getStringCellValue()
				 * .toUpperCase();
				 */
				keyword = GlobalLib.getPoiCellValue(xlRow.getCell((short) 2));
				objectName = GlobalLib
						.getPoiCellValue(xlRow.getCell((short) 3));
				// objectType = xlRow.getCell((short)3).getStringCellValue();
				value = GlobalLib.getPoiCellValue(xlTCRow
						.getCell((short) valueColumn));
				Debug.VALUE = value;
				// Call perform function to perform operation on UI
				if (skipFlag.equalsIgnoreCase("Y")) {
					strResult = "Skiped";
					continue;
				}
				strResult = operation.perform(allObjects, keyword, objectName,
						value, imagePath);
				if (strResult.equalsIgnoreCase("FAIL")) {
					ScreenshotUtility.ScreenShot(driver, imagePath);
				}
				Date eDate = new Date();
				// get time difference
				String timeDiff = GlobalLib.getTimeDiff(
						dateFormat.format(sDate), dateFormat.format(eDate));

				out.println("<tr bgcolor = #FFFFFF>");
				out.println("<td width=400><p align=center><font face=Verdana size=2 color=#336699>"
						+ description + "</td>");
				out.println("<td width=400><p align=center><font face=Verdana size=2 color=#336699>"
						+ keyword + "</td>");
				out.println("<td width=400><p align=center><font face=Verdana size=2 color=#336699>"
						+ objectName + "</td>");
				out.println("<td width=400><p align=center><font face=Verdana size=2 color=#336699>"
						+ Debug.VALUE + "</td>");
				out.println("<td width=400><p align=center><font face=Verdana size=2 color=#336699>"
						+ strResult + "</td>");
				out.println("<td width=400><p align=center><font face=Verdana size=2 color=#336699>"
						+ timeDiff + "</td>");
				out.println("</td></tr>");

				if (strResult.equalsIgnoreCase("Fail")) {
					strKWResult = strResult;
				}
			}
			out.close();
		} catch (Exception ee) {
			ee.printStackTrace();
			ScreenshotUtility.ScreenShot(driver, imagePath);
		}
		return strKWResult;
	}

	/**
	 * This method for the type of data in the cell, extracts the data and
	 * returns it as a string.
	 */
	public static String getCellValueAsString(Cell cell) {
		String strCellValue = "";
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				strCellValue = cell.toString();
				break;
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"dd/MM/yyyy");
					strCellValue = dateFormat.format(cell.getDateCellValue());
				} else {
					Double value = cell.getNumericCellValue();
					Long longValue = value.longValue();
					strCellValue = new String(longValue.toString());
				}
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				strCellValue = new String(new Boolean(
						cell.getBooleanCellValue()).toString());
				break;
			case Cell.CELL_TYPE_BLANK:
				strCellValue = "";
				break;

			case Cell.CELL_TYPE_FORMULA:
				System.out.println("Formula is " + cell.getCellFormula());
				switch (cell.getCachedFormulaResultType()) {
				case Cell.CELL_TYPE_NUMERIC:
					System.out.println("Last evaluated as: "
							+ cell.getNumericCellValue());
					Double value = cell.getNumericCellValue();
					Long longValue = value.longValue();
					strCellValue = new String(longValue.toString());
					break;
				case Cell.CELL_TYPE_STRING:
					System.out.println("Last evaluated as \""
							+ cell.getRichStringCellValue() + "\"");
					strCellValue = cell.getRichStringCellValue().toString();
					break;
				}
			}
		}
		return strCellValue;
	}
	
	/**
	 * SFTP upload file to server and check exists
	 * 
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @param directoryToUpload
	 * @param fileToUpload
	 * @return
	 * @throws JSchException 
	 * @throws SftpException 
	 * @throws FileNotFoundException 
	 * @throws InterruptedException 
	 */
	public static Boolean UploadAndTest(String host, int port, String username,
		String password, String directoryToUpload, String fileToUpload) throws JSchException, FileNotFoundException, SftpException, InterruptedException  {
		Session session = getSftpSession(username, host, port, password);
		Channel channel = session.openChannel("sftp");
		channel.connect();
		ChannelSftp channelSftp = (ChannelSftp) channel;
		channelSftp.cd(directoryToUpload);
		File f = new File(fileToUpload);
		for(int i=0; i<5; i++){
			if(f.exists()){
				break;
			}
			Thread.sleep(5000);
		}
		if (!f.canRead()){
			System.out.println("File: "+fileToUpload+" - Can not be read.. wating for 5 seconds.");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//f = new File(fileToUpload);
		}
		
		channelSftp.put(new FileInputStream(f), f.getName());
		//channelSftp.lstat(directoryToUpload + "/" + f.getName());
		channelSftp.disconnect();
		//channelSftp.exit();
		channel.disconnect();
		session.disconnect();
		if(channel.isClosed()){
			System.out.println("File: " +f.getName()+" has been uploaded succesfully at path :"+directoryToUpload);
			return true;
			}
		else{
			System.out.println("FTP connection is still open");
			return false;
		}		
	}
	
	/**
     * Dropdown Values verification
     * 
     * @param driver
     * @param object Name
     * @param parts
     * @param Properties
     * @return
     */
	
	
	public static String VerifyDropdownValues( WebDriver driver, String objectName, String[] parts, Properties p) {
		// To be reviewed by Chetan
		ArrayList<String> al = new ArrayList<String>();
    	try {
			Select dropdown=new Select(driver.findElement(operation.getObject(p, objectName)));
			System.out.println("Default Selected Value: "+dropdown.getFirstSelectedOption().getText());
            List<String> optionsToVerify = Arrays.asList(parts);
            
            List<WebElement> dropOptionsElements = dropdown.getOptions();
            
            for(WebElement dropDownValue: dropOptionsElements ){
            	al.add(dropDownValue.getText().trim());
            }
           
            for(String option: optionsToVerify){
            	if (!al.contains(option)) {
            		
            		return "Fail";
            	}
            }
            return "Pass";
        }
    	catch(Exception e){
			e.printStackTrace();
			return "Fail";
    	}
    }
	
	
	/**
     * SFTP upload file to server and check exists
     * 
     * @param host
     * @param port
     * @param username
     * @param password
     * @param directoryToUpload
     * @param fileToUpload
     * @return
	 * @throws JSchException 
	 * @throws SftpException 
	 * @throws IOException 
	 * @throws ProvisoException 
     */
    
	public static Boolean copyFileFromServer(String host, int port, String username,
	    String password, String copyFrom, String copyTo,String propFname,String propFileListCount) throws JSchException, SftpException, IOException, ProvisoException {
    	PropertyFileOperation propertyFileOperation = new PropertyFileOperation();
    	String fname = copyFrom.substring(copyFrom.lastIndexOf("/")+1);
		String dname = copyFrom.substring(0, copyFrom.lastIndexOf("/")+1);
		Session session = getSftpSession(username, host, port, password);
		Channel channel = session .openChannel("sftp");
	    channel.connect();
	    ChannelSftp channelSftp = (ChannelSftp) channel;
	    channelSftp.cd(dname);
	    @SuppressWarnings("unchecked")
		Vector<ChannelSftp.LsEntry> list = channelSftp.ls(fname);
	    if(list.isEmpty())
	    {
	    	System.out.println("No such file found on server");
	    	 throw new ProvisoException("No such file found on server");
	    }
	    else
	    {	
	    String fullFilepath = dname+list.get(0).getFilename();
	    channelSftp.get(fullFilepath, copyTo);
	    if(!propFname.isEmpty() && !("NULL".equalsIgnoreCase(propFname.toUpperCase()))){
	    propertyFileOperation.writeByKeyValueProperty(propFname, list.get(0).getFilename());	    
	    }
	    if(!propFileListCount.isEmpty() && !("NULL".equalsIgnoreCase(propFileListCount.toUpperCase()))){
	    	List<String> fStrList = new ArrayList<String>();
	    	for(int i = 0; i < list.size() ; i++)
	    	{
	    		fStrList.add(list.get(i).getFilename());
	    	}
	    	if(null != fStrList){
	    	propertyFileOperation.writeByKeyValueProperty(propFileListCount,fStrList.size()+"");
	    	//propertyFileOperation.writeByKeyValueProperty(propFileList+"Count",fStrList.size()+"");
	    	System.out.println("Number of Files Present on server having Similar Name :- "+fStrList.size());
	    	System.out.println("Server Files with having name:- "+fStrList.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
	    	Debug.traceMessage = Debug.traceMessage + "<br> Number of Files Present on server having Similar Name : " + fStrList.size()+"";
	    	Debug.traceMessage = Debug.traceMessage + "<br> Server Files having similar name : " + fStrList.toString().replaceAll("\\[", "").replaceAll("\\]", "");
	    	}	
	    }
	    }
	    channelSftp.exit();
        session.disconnect();
	    System.out.println("success");		
	    return true;
	}
    public static Boolean copyFileFromServerToServer(String host1, int port1, String username1,
    	    String password1, String copyFrom,String host2, int port2, String username2,
    	    String password2, String copyTo,String propFname) throws JSchException, SftpException, IOException, ProvisoException {
        	PropertyFileOperation propertyFileOperation = new PropertyFileOperation();
        	String fname1 = copyFrom.substring(copyFrom.lastIndexOf("/")+1);
    		String dname1 = copyFrom.substring(0, copyFrom.lastIndexOf("/")+1);
    		String fname2 = copyTo.substring(copyTo.lastIndexOf("/")+1);
    		String dname2 = copyTo.substring(0, copyTo.lastIndexOf("/")+1);
    		Session session1 = getSftpSession(username1, host1, port1, password1);
    		Session session2 = getSftpSession(username2, host2, port2, password2);
    		Channel channel1 = session1 .openChannel("sftp");
    		Channel channel2 = session2 .openChannel("sftp");
    	    channel1.connect();
    	    channel2.connect();
    	    ChannelSftp channelSftp1 = (ChannelSftp) channel1;
    	    channelSftp1.cd(dname1+fname1);
    	    @SuppressWarnings("unchecked")
    		Vector<ChannelSftp.LsEntry> list = channelSftp1.ls(propFname);
    	    if(list.isEmpty())
    	    	
    	    {
    	    	System.out.println("No such file found on server");
    	    	 throw new ProvisoException("No such file found on server");
    	    }
    	    else
    	    {	
    	    String fullFilepath = dname1+fname1+"/"+list.get(0).getFilename();
    	    String fullFilepath1 = dname2+fname2+"/"+list.get(0).getFilename();
    	  //  channelSftp1.
    	   // channelSftp1.put(fullFilepath, fullFilepath1);
    	    channelSftp1.rename(fullFilepath, fullFilepath1);
    	    if(!propFname.isEmpty()){
    	    propertyFileOperation.writeByKeyValueProperty(propFname, list.get(0).getFilename());}
    	    }
    	    channelSftp1.exit();
            session1.disconnect();
            session2.disconnect();
    	    System.out.println("success");		
    	    return true;
    	}
    
    /**
     * Common function to get Sftp session
     * @param username
     * @param host
     * @param port
     * @param password
     * @return
     */
    public static Session getSftpSession(String username, String host, int port, String password) {
    	Session session = null;
	    JSch jsch = new JSch();
	    try {
			session = jsch.getSession(username, host, port);
			session.setPassword(password);
		    java.util.Properties config = new java.util.Properties();
		    config.put("StrictHostKeyChecking", "no");
		    config.put("PreferredAuthentications", "publickey,keyboard-interactive,password");
		    
		    session.setConfig(config);
		    session.connect();
		} catch (JSchException e) {
			System.out.println("Exception while connectiong sftp");
			e.printStackTrace();
		}
	    
	    return session;
    }
    
    /**
	 * selectAllTableCell : Return the cell value from the grid table
	 * 
	 * @param WebElement
	 *            row : Selected row
	 * @param int columnNumber : column number
	 * @return Pass : Return the cell values
	 * @throws Exception
	 */
	// Alok
	public static String selectAmountTableCell(int columnNumber,
			String value, Properties p, String objectName, WebDriver driver)
			throws Exception {

		UIOperation operation = null;
		operation = new UIOperation(driver);

		// Grab the table
		WebElement table = driver.findElement(operation
				.getObject(p, objectName));
		
		int rowCount = getAllRowInTable(p,objectName,driver);
		for(int i=2; i<=rowCount; i++){
			
			List<WebElement> columnIndex = table.findElements(By
				.xpath(".//tbody/tr["+i+"]/td[" + columnNumber + "]"));
		
			for (WebElement cell : columnIndex) {

				WebElement cells = cell.findElement(By
					.cssSelector("select[tabIndex='1']"));
				// Select dropdown = new Select(cells);

				// dropdown.selectByVisibleText(value);
				List<WebElement> options = cells.findElements(By
					.tagName("option"));
				for (WebElement option : options) {
					if (option.getText().equals(value)) {
						option.click();
						option.sendKeys(Keys.ARROW_DOWN);
						option.click();
						break;
					}
				}
			}
		}

		return "Pass";

	}
	
	 /**
		 * enterValueInColumnCell : It is used to enter text in cell column and it returns the execution result 
		 * 
		 * @param WebDriver: driver
		 * @Param String Name : Search text
		 * @param int columnNumber : column index
		 * @ Param string Cell Value : text to be typed in column cell
		 * @Param Object : Table object
		 * @param Properties : Properties
		 * return : Pass/Fail
		 * @throws Exception
		 */
	
	public static String enterValueInColumnCell(String name,int columnNumber, String cellValue, Properties p, String objectName, WebDriver driver){
		try {
			WebElement element =driver.findElement(operation.getObject(p, objectName));
			WebElement gridCell= element.findElement(By.xpath("//tbody/tr/td/div[text()='"+name+"']/../../td["+ columnNumber +"]"));
			Thread.sleep(1000);
			Actions action = new Actions(driver);
			action.moveToElement(gridCell).click().build().perform();
			action.moveToElement(gridCell).click().sendKeys(cellValue).build().perform();
			WebElement checkBox= element.findElement(By.xpath("//tbody/tr/td/div[text()='"+name+"']/../../td/div/input[@type='checkbox']"));
			checkBox.sendKeys(Keys.ARROW_DOWN);
			return "Pass";			
		} catch (Exception e) {
			System.out.println("Element Not Found");
			e.printStackTrace();
			return "Fail";
		}
	}
	
	/**
	 * clickCheckbox : It is used to click on checkbox in the grid table only if only the grid table consists of single table and it returns the execution result 
	 * 
	 * @param WebDriver: driver
	 * @Param String Name : Search text
	 * @Param Object : Table object
	 * @param Properties : Properties
	 * return : Pass/Fail
	 * @throws Exception
	 */
	
	public static String clickCheckbox(String name, Properties p, String objectName, WebDriver driver){
		try {
			WebElement element =driver.findElement(operation.getObject(p, objectName));
			WebElement checkBox= element.findElement(By.xpath("//tbody/tr/td/div[text()='"+name+"']/../../td/div/input[@type='checkbox']"));
			Thread.sleep(1000);
			checkBox.click();
			return "Pass";
			
		} catch (Exception e) {
			System.out.println("Element Not Found");
			e.printStackTrace();
			return "Fail";
		}
	}
	
	/**
	 * selectScrollListValueInCell : It is used to Select text from scroll list in cell column and it returns the execution result 
	 * 
	 * @param WebDriver: driver
	 * @Param String Name : Search text
	 * @param int columnNumber : column index
	 * @ Param string Cell Value : text to be typed in column cell
	 * @Param Object : Table object
	 * @param Properties : Properties
	 * return : Pass/Fail
	 * @throws Exception
	 */

	public static String selectScrollListValueInCell(String name,int columnNumber, String cellValue, Properties p, String objectName, WebDriver driver){
		
		try {
			WebElement element = driver.findElement(operation.getObject(p, objectName));
			WebElement gridCell = element.findElement(By.xpath("//tbody/tr/td/div[text()='"+name+"']/../../td["+ columnNumber +"]"));
			Thread.sleep(2000);
			Actions action = new Actions(driver);
			action.moveToElement(gridCell).click().sendKeys(Keys.ARROW_DOWN).build().perform();
			WebElement scrollListValue = driver.findElement(By.xpath("//div/ul/li[text()='"+cellValue+"']"));
			action.moveToElement(scrollListValue).click().sendKeys(Keys.ENTER).build().perform();
			return "Pass";
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Fail";
		}	
	}

	/**
	 * getAllRowInTable  : Returns the total number of rows in grid table
	 * @param p : Path of property file
	 * @param objectName : locator of table
	 * @param driver  : driver
	 * @return Integer
	 * @throws Exception
	 */
	public static  int getAllRowInTable(Properties p, String objectName, WebDriver driver)
			throws Exception {

		UIOperation operation = null;
		operation = new UIOperation(driver);

		// Grab the table
		WebElement table = driver.findElement(operation
				.getObject(p, objectName));
	
		List<WebElement> rowCount = table.findElements(By
				.xpath(".//tbody/tr"));
		System.out.println("NUMBER OF ROWS IN THIS TABLE = "
				+ rowCount.size());
		return rowCount.size();
	}
	
	/**
	 * checkAllRowInTable : Select the checkbox of all row in table
	 * @param p
	 * @param objectName
	 * @param driver
	 * @param columnNumber
	 * @return
	 * @throws Exception
	 */
	public static String checkAllRowInTable(Properties p, String objectName, WebDriver driver, String columnNumber)
			throws Exception {

		UIOperation operation = null;
		operation = new UIOperation(driver);
		// Grab the table
		WebElement table = driver.findElement(operation
				.getObject(p, objectName));
		
		int rowCount = getAllRowInTable(p,objectName,driver);
		for(int i=2; i<=rowCount; i++){
			WebElement check = table.findElement(By.xpath(".//tbody/tr["+i+"]/td[" + columnNumber + "]/input"));
			check.click();
		}
		return "pass";
	}
	
	/**
	 * Get string by start with and end with
	 * @param startWith
	 * @param endWith
	 * @param str
	 * @return
	 */
	public static String getStringPatternGen(String startWith, String endWith,
			String str) {
		String strFound = "";
		// modified for special symbol
		String escapeCharacter = "\\";
		if (startWith.matches("[^a-zA-Z0-9 ]") || endWith.matches("[^a-zA-Z0-9 ]")) {
			escapeCharacter = "";
		}
		Pattern pattern = Pattern.compile(startWith + "(.*?)" + escapeCharacter + endWith);
		Matcher matcher = pattern.matcher(str);

		while (matcher.find()) {
			strFound = matcher.group(1).trim();
		}
		System.out.println(strFound);
		return strFound;
	}
	
	/**
	 * getRow2 : Return the row from the grid table
	 * 
	 * @param WebDriver
	 *            : Selection of the WebElement
	 * @param searchText
	 *            : Search the specified text
	 * @param objectName
	 *            : Locator of the Element
	 * @paramProperties p : Get the values from the properties file
	 * @return row : Return specified row
	 */
	public static int getRow2(String searchText, Properties p,
			String objectName, WebDriver driver, int colIndex) throws Exception {
		@SuppressWarnings("unused")
		String cellText = "";
		boolean isDisplayed = false;
		UIOperation operation = null;
		operation = new UIOperation(driver);
		@SuppressWarnings("unused")
		int page = 0;
		// Grab the table
		do {
			++page;
			WebElement table = driver.findElement(operation.getObject(p,
					objectName));
			List<WebElement> columnIndex = table.findElements(By
					.xpath(".//tbody/tr/td[" + colIndex + "]"));
			System.out.println("NUMBER OF ROWS IN THIS TABLE = "
					+ columnIndex.size());

			@SuppressWarnings("unused")
			int row_num = -1;
			for (int i = 0; i < columnIndex.size(); i++) {

				if (columnIndex.get(i).getText().equalsIgnoreCase(searchText)) {
					return i + 1;

				}

				row_num++;
			}

			WebElement lastRow = driver.findElement(By
					.cssSelector("a[id='nextpage']"));
			if (lastRow.isDisplayed() == true) {
				/*
				 * JavascriptExecutor executor = (JavascriptExecutor) driver;
				 * executor.executeScript("arguments[0].click();", lastRow);
				 */
				lastRow.click();
				isDisplayed = true;
			} else {

				isDisplayed = false;
			}

		} while (isDisplayed == true);

		return 0;
	}
	
	/**
	 * Convert date in to specific format
	 * 
	 * @param dateToConvert
	 * @param format
	 * @return
	 */
	public static String getConvertedDateString(Date dateToConvert, String format) {
		DateFormat formatedDate = new SimpleDateFormat(format);
		String convertedDate = formatedDate.format(dateToConvert);
		
		return convertedDate;
	}
	

	/**
     * Get Current, Previous and Next date
     * 
     * @param format Format in which we want date
     * @param days Days before current date
     * @return
     */
    public static String getDate(String format, int days) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, days);        
        return dateFormat.format(cal.getTime());
    }
    
	/**
     * Change Current, Previous and Next date
     * 
     * @param format Format in which we want date
     * @param days Days before current date
     * @return
	 * @throws ParseException 
     */
    public static Calendar changeDate(String format, String dateValue, String dateType,int days) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date date = (Date) dateFormat.parse(dateValue);
        Calendar cal = Calendar.getInstance();
        if (dateType.equalsIgnoreCase("APPDATE"))
            cal.setTime(date);
        cal.add(Calendar.DATE,  Integer.valueOf(days));
        
        return cal;
    }
    
    
    
    /**
     * Get property value
     * @param value
     * @return
     * @throws IOException 
     * @throws Exception 
     */
    public static String getPropertyOrValue(String value) throws IOException {
    	if(value.startsWith("Prop_")){
    		String[] text = value.split(Separator.SEPARATOR_UNDERSCORE, 2);
    		PropertyFileOperation propertyFileOperation = new PropertyFileOperation();
			value = propertyFileOperation.readProperty(text[1]);
    	} else {
    	    value = replacePropertiesFromString(value);
    	}
    	
    	return value; 
    }
    
    /**
  * compare two property values
  * 
  * @param firstPropertyValue
  * @param secondPropertyValue
  * @param type STRING, FLAOT, NUMBER
  * @param operator =,!=,++,-_,>,<
  * @return pass or fail
  */
 public static String comparePropertyValues(String firstPropertyValue,String secondPropertyValue,String type, String operator) {

    String strResult="Fail";
    
           switch (type) {
           case "STRING":
                  strResult = GlobalLib.compareStringValues(firstPropertyValue,
                               secondPropertyValue, operator);
                  break;
           case "NUMBER":
                  strResult = GlobalLib.compareIntergerValues(
                               Integer.parseInt(firstPropertyValue),
                               Integer.parseInt(secondPropertyValue), operator);
                  break;
           case "FLOAT":
                  strResult = GlobalLib.compareFloatValues(
                               Float.parseFloat(firstPropertyValue),
                               Float.parseFloat(secondPropertyValue), operator);
                  break;
           default:
                  System.out.println("Invalid Type");
           break;
           }
   
     return strResult;
 }
 
 
    /**
  * compare two integer numbers
  * 
  * @param number1
  * @param number2
  * @param operator =,!=,++,--,>,<
  * @return pass or fail
  */
 public static String compareIntergerValues(Integer firstPropertyValue,Integer secondPropertyValue,String operator ) {

    String strResult="Fail";
    
    switch(operator){
    case "=":
           if (firstPropertyValue.equals(secondPropertyValue))
                  strResult="Pass";
           break;
    case "!=":
           if (!firstPropertyValue.equals(secondPropertyValue))
                  strResult="Pass";
           break;
           case "++":
                  if ((++firstPropertyValue).equals(secondPropertyValue))
                        strResult="Pass";
                  break; 
           case "--":
                  if ((--firstPropertyValue).equals(secondPropertyValue))
                        strResult="Pass";
                  break;
           case ">":
                  if (firstPropertyValue > secondPropertyValue)
                        strResult="Pass";
                  break;
           case "<":
                  if (firstPropertyValue < secondPropertyValue)
                        strResult="Pass";
                  break;
           default:
                  System.out.println("Invalid Operator");
           break; 
    
           }  
     return strResult;
 }
 
    /**
  * compare two string values
  * 
  * @param string1
  * @param string2
  * @param operator ==,!=
  * @return pass or fail
  */
 public static String compareStringValues(String firstPropertyValue,String secondPropertyValue,String operator ) {

    String strResult="Fail";
    
    switch(operator)
           {
    case "=":
           if (firstPropertyValue.equalsIgnoreCase(secondPropertyValue))
                  strResult="Pass";
           break;
           case "!=":
                  if (!firstPropertyValue.equalsIgnoreCase(secondPropertyValue))
                        strResult="Pass";
                  break;
           default:
                  System.out.println("Invalid Operator");
           break;        
           }  
    return strResult;
 }

/**
  * compare two float values
  * 
  * @param float1
  * @param float2
  * @param operator ==,!=
  * @return pass or fail
  */
 public static String compareFloatValues(Float firstPropertyValue, Float secondPropertyValue, String operator ) {
    
    String strResult="Fail";
    switch(operator)
           {
    case "=":
           if (firstPropertyValue.compareTo(secondPropertyValue)==0)
                  strResult="Pass";
                  break;
           case "!=":
                  if (!(firstPropertyValue.compareTo(secondPropertyValue)==0))
                        strResult="Pass";
                  break;
           default:
                  System.out.println("Invalid Operator");
           break;        
           }  
    return strResult;
 }

 /**
  * Select date from date picker calendar
  * 
  * @param driver
  * @param p
  * @param objectName
  * @param date
  * @param month
  * @param year
  * @return
  * @throws Exception
  */
  public static String setCalenderDate(WebDriver driver, Properties p, String datePicker,String datePickerYear, String datePickerMonth,
		  String datePickerNext, String datePickerPrev, String datePickerDates, String date, String month, String year) throws Exception{
	  
	 String objectName;
	 Map<String, Integer> monthsMap = new HashMap<String, Integer>();
 	 String[] monthsOfYear = new DateFormatSymbols().getMonths();
	 
 	 for(int i = 0; i < monthsOfYear.length; i++){
		  monthsMap.put(monthsOfYear[i], i+1);
	 }
 	 
 	 objectName = datePicker;
 	 WebElement datepicker = driver.findElement(operation.getObject(p, objectName));
 	 objectName = datePickerYear;
 	 WebElement calenderYear = datepicker.findElement(operation.getObject(p, objectName));
 	// WebElement calenderYear = datepicker.findElement(By.xpath(".//div/div/span[2]"));
 	 String calenderYearText = calenderYear.getText();
 	 System.out.println("Calender Year : " +calenderYearText);
 	 
 	 int yearDiff = Integer.valueOf(year) - Integer.valueOf(calenderYearText);
 	 objectName = datePickerMonth;
 	 WebElement calenderMonth = datepicker.findElement(operation.getObject(p, objectName));
 	 //WebElement calenderMonth = datepicker.findElement(By.xpath(".//div/div/span[1]"));
 	 String calenderMonthText = calenderMonth.getText();
 	 System.out.println("Calender Month : " +calenderMonthText);
 	 
 	 int monthValue = monthsMap.get(calenderMonthText);
 	 System.out.println("Month Value :  " +monthValue);
 	 WebElement nextLink = null;
 	 WebElement prevLink = null;
 	 if(yearDiff!=0){
 		//if you have to move next year
 		 
          if(yearDiff>0){
         	 int remainingMonthToMove = 12-monthValue;
         	 int totalMonthToMove = remainingMonthToMove + Integer.valueOf(month);
              for(int i=0;i< totalMonthToMove;i++){	
             	 System.out.println("Moving Next Year");
                  System.out.println("Year Diff->"+i);
                  objectName = datePickerNext;
                  nextLink = driver.findElement(operation.getObject(p, objectName));
                  //nextLink = driver.findElement(By.xpath("//span[text()='Next']"));
                  nextLink.click();
                  Thread.sleep(2000);
              }	      
          }
          //if you have to move previous year
          if(yearDiff<0){
         	 int remainingMonthToMove = 12-Integer.valueOf(month);
         	 int totalMonthToMove = remainingMonthToMove + monthValue;
              for(int i=0;i< totalMonthToMove;i++){	
             	 System.out.println("Moving Previous Year");
                  System.out.println("Year Diff->"+i);	
                  objectName = datePickerPrev;
                  prevLink = driver.findElement(operation.getObject(p, objectName));
                 // prevLink =driver.findElement(By.xpath("//span[text()='Prev']"));
                  prevLink.click();	
                  Thread.sleep(2000);
              }	      
          }
 	 }
 	 else{
 		//if you have to move next month within same year
 		 int monthDiff =  Integer.valueOf(month) - monthValue;
 		 System.out.println("Total Month differenece : "+monthDiff);
 		 if(monthDiff > 0){
 			 for(int i=0;i< monthDiff;i++){	  
 				 System.out.println("Moving Next Month");
                  System.out.println("Month Diff->"+i);
                  objectName = datePickerNext;
                  nextLink = driver.findElement(operation.getObject(p, objectName));
                  //nextLink =driver.findElement(By.xpath("//span[text()='Next']"));
                  nextLink.click();
                  Thread.sleep(2000);
              }	 
 		 }
 		//if you have to move previous month within same year
 		 if(monthDiff < 0){
 			 for(int i=0;i< monthDiff*(-1);i++){	
 				 System.out.println("Moving Previous Month");
                  System.out.println("Month Diff->"+i);
                  objectName = datePickerPrev;
                  prevLink = driver.findElement(operation.getObject(p, objectName));
                 // prevLink =driver.findElement(By.xpath("//span[text()='Prev']"));
                  prevLink.click();	
                  Thread.sleep(2000);                 
              }	
 		 }
 	 }
 	 objectName = datePickerDates;
 	 List<WebElement> listAllDate = datepicker.findElements(operation.getObject(p, objectName));
 	// List<WebElement> listAllDate = datepicker.findElements(By.xpath(".//a[@class='ui-state-default']"));
 	 System.out.println("Total Days : " +listAllDate.size());
 	 for(int i=0; i<listAllDate.size(); i++){
 		 String selectDate = listAllDate.get(i).getText();
 		 if(selectDate.equalsIgnoreCase(date)){
 			 listAllDate.get(i).click();
 			 return "Pass";
 		 }
 	 }
 	return "Fail"; 	  
  }
  public static String setCalenderDateAdvSearch(WebDriver driver,Properties p, WebElement WEprev,String strmonthYear,WebElement WEnext, String strDate ) throws Exception{
	   
	   String[] arrDate = strDate.split("-") ;	   
	   WebElement WEmonthYear = driver.findElement(operation.getObject(p,
			   strmonthYear));
	   
	   // Assign month number mapping
	   Hashtable<String, Integer> hashtableCalMonths= 
			   new Hashtable<String, Integer>();
	   hashtableCalMonths.put("January",1);
	   hashtableCalMonths.put("February",2);
	   hashtableCalMonths.put("March",3);
	   hashtableCalMonths.put("April",4);
	   hashtableCalMonths.put("May",5);
	   hashtableCalMonths.put("June",6);
	   hashtableCalMonths.put("July",7);
	   hashtableCalMonths.put("August",8);
	   hashtableCalMonths.put("September",9);
	   hashtableCalMonths.put("October",10);
	   hashtableCalMonths.put("November",11);
	   hashtableCalMonths.put("December",12);
	   
	   // Get month and year separated
	   String monhtYear = WEmonthYear.getText();
	   
	   String[] arrMyear =  monhtYear.split(" ");
	   
	   int monthIndex = hashtableCalMonths.get(arrMyear[0]);
	   int yearIndex=Integer.parseInt(arrMyear[1]);
	   
	   // Get month and year from parameter set
	   
	   int setMonthindex = hashtableCalMonths.get(arrDate[1]);
	   int setYearindex = Integer.parseInt(arrDate[2]);
	   
	   while((setMonthindex!=monthIndex) || (setYearindex!=yearIndex)){
		   
		   if(setMonthindex<monthIndex && setYearindex<=yearIndex){
			   
			   WEprev.click();
		   }
		   else if(setMonthindex>monthIndex && setYearindex>=yearIndex){
			   
			   WEnext.click();
		//	   monthIndex++;
		   }
		   Thread.sleep(1000);
		   WEmonthYear = driver.findElement(operation.getObject(p,
				   strmonthYear));
		   monhtYear = WEmonthYear.getText();
 
		   arrMyear =  monhtYear.split(" ");
		   
		   monthIndex = hashtableCalMonths.get(arrMyear[0]);
		   yearIndex=Integer.parseInt(arrMyear[1]);
	   }
	   driver.findElement(By.linkText(arrDate[0])).click();
	   
	   return "Pass";
  }

	/**
	 * Checks for URL link exists or not
	 * 
	 * @param URLName
	 * @throws IOException
	 */
	public static boolean linkExists(String URLName) throws IOException {
		// disable ssl certification before link check
		disableSslVerification();
		HttpURLConnection.setFollowRedirects(false);
		HttpURLConnection con = (HttpURLConnection) new URL(URLName)
				.openConnection();
		con.setRequestMethod("HEAD");
		return (con.getResponseCode() != HttpURLConnection.HTTP_NOT_FOUND);
	}
  
  
  
  
  
  /**
   * Execute java script functions
   * 
   * @param jsToExecute
   * @param js1
   * @param driver
   */
  public static void executeJavaScript(String jsToExecute, WebElement js1, WebDriver driver) {
	  JavascriptExecutor executor = (JavascriptExecutor) driver;
	  executor.executeScript(jsToExecute, js1);
  }
  
  /**
   * Replace Prop_ value from string with values stored in properties file
   * 
   * @param stringToReplaceProperties
   * @return
 * @throws IOException 
 * @throws Exception 
   */
  public static String replacePropertiesFromString(String stringToReplaceProperties) throws IOException  {
      Matcher matcher = Pattern.compile("\\$\\{Prop_\\s*(\\w+)\\}").matcher(stringToReplaceProperties);
      PropertyFileOperation propertyFileOperation = new PropertyFileOperation();
	  
	  while (matcher.find()) {
		  String valueToReplace = propertyFileOperation.readProperty(matcher.group(1));
		  stringToReplaceProperties = stringToReplaceProperties.replace("${Prop_" + matcher.group(1)+ "}" , valueToReplace);
		  Debug.traceMessage = Debug.traceMessage + " variable : " + matcher.group(1) + " property value : " + valueToReplace + "<br>";
	   }
	   
	   matcher = Pattern.compile("\\$\\{PROVISO_HOME\\}").matcher(stringToReplaceProperties);
	      
       while (matcher.find()) {
          String provisoHomeDir = System.getProperty("user.dir");
          stringToReplaceProperties = stringToReplaceProperties.replace("${PROVISO_HOME}" , provisoHomeDir);
          Debug.traceMessage = Debug.traceMessage + " PROVISO_HOME : " + provisoHomeDir + "<br>";
       }
		
       matcher = Pattern.compile("\\$\\{Env_\\s*(\\w+)\\}").matcher(stringToReplaceProperties);
       propertyFileOperation.setPropertyFileName("environment.properties");
       String environmentFileName = propertyFileOperation.readProperty("environmentFileName");
       
       while (matcher.find()) {
           if (!Debug.enviromentIsOn) {
               Debug.enviromentIsOn = true;
                 Debug.enviromentFileName = environmentFileName;
           }
          
          System.out.println(matcher.group(1));
          propertyFileOperation.setPropertyFileName(Debug.enviromentFileName + ".properties");
          String valueToReplace = propertyFileOperation.readProperty(matcher.group(1));
          
          if (valueToReplace == null || "".equals(valueToReplace)) {
              valueToReplace = matcher.group(1);
          }
          Debug.traceMessage = Debug.traceMessage + " variable : " + matcher.group(1) + " environment value : " + valueToReplace + "<br>";
          stringToReplaceProperties = stringToReplaceProperties.replace("${Env_" + matcher.group(1)+ "}" , valueToReplace);
       }
       
	   return stringToReplaceProperties;
	}
  

  /**
   * Validate xml schema with xsd
   * 
   * @param xsd
   * @param xml
   * @return
 * @throws Exception 
   */
  public static boolean validateXMLSchema(String xsd, String xml) throws Exception {
	  File xsdFile = null;
	  String[] parts = null;
	  String xmlPath = System.getProperty("user.dir") + File.separator + "xml" + File.separator;
	  String xsdPath = System.getProperty("user.dir") + File.separator + "xsd" + File.separator;
	  
    try {
    	  xsdFile = new File(xsdPath + xsd);
    	  
    	  if (xml.startsWith("File_"))  {
    		  parts = xml.split(Separator.SEPARATOR_UNDERSCORE, 2);
    		  System.out.println("XML file to valicate with : " + xmlPath + parts[1]);
    		  BufferedReader bufferReader = new BufferedReader(new FileReader(new File(xmlPath + parts[1])));
    		  String line;
    		  StringBuilder stringBuffer = new StringBuilder();

    		  while ((line = bufferReader.readLine()) != null) {
    		      stringBuffer.append(line.trim());
    		  }
    		  
    		  xml = stringBuffer.toString();
    		  System.out.println("Xml file content : " + xml);
    	  } else {
    		  xml = getPropertyOrValue(xml); 
    	  }
    	  
          System.out.println("XSD file to validate : " + xsdFile.getPath());
          System.out.println("XML content to validate : " + xml);

          SchemaFactory factory = SchemaFactory
              .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
          Schema schema = factory.newSchema(xsdFile);

          Validator validator = schema.newValidator();
          validator.validate(new StreamSource(new StringReader(xml)));
      } catch (IOException e) {
          e.printStackTrace();
          System.out.println("Error while validating xml and xsd");
          return false;
      } catch (SAXException e) {
          e.printStackTrace();
          System.out.println("Error while validating xml and xsd");
          return false;
      }

      return true;
  }

 
    /**
     * Close all browser windows
     * 
     * @param driver
     * @param imageFolder
     * @throws IOException
     * @throws AWTException
     */
    public static void closeBrowsers(WebDriver driver, String imageFolder)
        throws IOException, AWTException {

        Debug.tcStepIterator = Debug.tcRowCount;
        Debug.tcSubStepIterator = Debug.tcSubEndRow;
        Debug.imageFile = ScreenshotUtility.ScreenShot(driver, imageFolder);

        if (driver != null &&  null != ((RemoteWebDriver) driver).getSessionId()) {
            if (Debug.BrowserType.equals("IE")) {
                if (driver instanceof JavascriptExecutor) {
                    System.out.println("Close all window called");
                    ((JavascriptExecutor) driver)
                        .executeScript("for(var dialog in this.openedWindows) {this.openedWindows[dialog].close()};");
                } else {
                    throw new IllegalStateException(
                        "This driver does not support JavaScript!");
                }
            }
            Debug.setParentWindowHandle = new ArrayList<String>();
            driver.close();
            driver.quit();
        }
    } 
    

   /**
    * Covert date to Julian date
    *  
    * @param format
    * @param dateInString
    * @return
    * @throws ParseException
    */
	public static String convertToJulian(String format, String dateInString)
			throws ParseException {
		/*
		 * SimpleDateFormat formatter = new SimpleDateFormat(format);
		 * 
		 * Date date = formatter.parse(dateInString); Calendar calendar =
		 * Calendar.getInstance(); calendar.setTime(date); int year =
		 * calendar.get(Calendar.YEAR); String syear =
		 * String.format("%04d",year).substring(2); int century =
		 * Integer.parseInt(String.valueOf(((year / 100)+1)).substring(1));
		 * String julianF =
		 * String.format("%d%s%03d",century,syear,calendar.get(Calendar
		 * .DAY_OF_YEAR)); int julian = Integer.parseInt(julianF.substring(3));
		 * 
		 * return julian;
		 */
		SimpleDateFormat formatter = new SimpleDateFormat(format);

		Date date = formatter.parse(dateInString);
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		String syear = String.format("%04d", year).substring(2);
		int century = Integer.parseInt(String.valueOf(((year / 100) + 1))
				.substring(1));
		String julianF = String.format("%d%s%03d", century, syear,
				calendar.get(Calendar.DAY_OF_YEAR));
		if (julianF.length() == 2) {
			return "0" + julianF;
		} else if (julianF.length() == 1) {
			return "00" + julianF;
		} else {
			return julianF.substring(3);
		}
	}
 
   /**
    * Perform operation on variable
    * @param parts
    * @return
 * @throws ProvisoException 
    */
	public static String variableOperation(String[] parts) throws ProvisoException {
		String result = "";
		try {
			switch (parts[0].toUpperCase()) {
			case "JULIANDATE":
				result = convertToJulian(parts[2], parts[3]);
				break;
			case "INCREMENT":
				result = String.valueOf(Integer.parseInt(parts[2]) + 1);
				break;
			case "DECREMENT":
				result = String.valueOf(Integer.parseInt(parts[2]) - 1);
				break;
			case "DATEOPERATION":
				Calendar calender = changeDate(parts[2], parts[4], "APPDATE", Integer.parseInt(parts[3]));
				SimpleDateFormat format1 = new SimpleDateFormat(parts[2]);
				System.out.println(calender.getTime());
				result = format1.format(calender.getTime());
				break;

			default:
				throw new ProvisoException("Invalid variable operation" + parts[0].toUpperCase());
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return "Fail";
		}
		
		return result;
	}
	
	/**
	 * Disable SSL verification certification
	 */
	private static void disableSslVerification() {
	    try
	    {
	        // Create a trust manager that does not validate certificate chains
	        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
	            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }
	            public void checkClientTrusted(X509Certificate[] certs, String authType) {
	            }
	            public void checkServerTrusted(X509Certificate[] certs, String authType) {
	            }
	        }
	        };

	        // Install the all-trusting trust manager
	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	        // Create all-trusting host name verifier
	        HostnameVerifier allHostsValid = new HostnameVerifier() {
	            public boolean verify(String hostname, SSLSession session) {
	                return true;
	            }

				
	        };

	        // Install the all-trusting host verifier
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (KeyManagementException e) {
	        e.printStackTrace();
	    }
	}
	
	
	 /**
	  * Arithmetic Operation
	  * 
	  * @param number1
	  * @param number2
	  * @param operator =,!=,++,--,>,<
	  * @return pass or fail
	 * @throws ProvisoException 
	  */
	 public static Object arithmeticOperation(String operand1,String optr,String operand2) throws ArithmeticException, ProvisoException{

	    String strResult="Fail";
	    Long opd1 = Long.parseLong(operand1);
	    Long opd2 = Long.parseLong(operand2);
	    Object reslt = null;
	    
	    switch(optr){
	      case "+":
	    			 reslt = opd1 + opd2;
	                 strResult="Pass";
	           break;
	           
	      case "-":
	    	  reslt = opd1 - opd2; 
	    	  /*if(opd1>opd2){
	    		  reslt = opd1 - opd2;  
	    	  }
	    	  else if(opd1<opd2){
	    		  reslt = opd2 - opd1;  
	    	  }
	    	  else {
	    		  System.out.println("Invalid Operands provided while performing Substraction Arithmetic operation");
	    	  }*/
              strResult="Pass";
              break;
	      case "*":
 			 reslt = opd1 * opd2;
              strResult="Pass";
             break;
          
	      case "/":
	    	  if(opd1>opd2){
	    		  reslt = opd1 / opd2;  
	    	  }
	    	  else if(opd1<opd2){
	    		  reslt = opd2 / opd1;  
	    	  }
	    	  else {
	    		  System.out.println("Invalid Operands provided while performing Division Arithmetic operation");
	    	  }
              strResult="Pass";
              break; 
              
	      case "%":
	    	  if(opd1>opd2){
	    		  reslt = opd1 % opd2;  
	    	  }
	    	  else if(opd1<opd2){
	    		  reslt = opd2 % opd1;  
	    	  }
	    	  else {
	    		  System.out.println("Invalid Operands provided while performing Modulas Arithmetic operation");
	    	  }
              strResult="Pass";
              break;
	           default:
	        	   throw new ProvisoException("Invalid Arithmetic perator");
	              
	           }  
	    
	     return reslt;
	 }
	 
	 /**
	  * Get Length of String / Count number of characters in Given Object
	  * 
	  * @param String Object whose length needs to be calculated
	  * @param Poperty to store length 
	  * @throws ProvisoException 	 
	  */
	 public static int getObjectLength(String obj1) throws ProvisoException{
	    int strLen = 0;
		 if(!obj1.isEmpty()){
			 if(obj1.contains("xpath://")){
				 String subStr = obj1.substring(obj1.indexOf("'")+1,obj1.lastIndexOf("'"));
				 strLen = subStr.length();
			 }
			 else {
				 strLen = obj1.length(); 
			 }
		 }
	     return strLen;
	 }
	 
	 
	 
}