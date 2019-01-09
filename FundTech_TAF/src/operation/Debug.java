package operation;

import hibernate.entity.StepResult;
import hibernate.entity.TestSuiteResult;
import hibernate.entity.TestcaseResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Debug {
	/***
	 * getFlag: GetFlag value for Debug (on/off)
	 * 
	 * @return: flag value
	 * 
	 * ****/
	public static boolean Flag = false;
	public static List<String> listOfUniqueValues = new ArrayList<String>();
	public static List<String> setParentWindowHandle = new ArrayList<String>();
	public static int tcStepIterator = 0;
	public static int tcSubStepIterator = 0;
	public static int tcRowCount = 0;
	public static int tcSubEndRow = 0;
	public static String imageFile = "";
	public static boolean pollingFlag = false;
	public static int pollingCount = 0;
	public static int pollingDuration = 5;
	public static String testStepStatus = "Pass";
	public static String grpResultFilePath = "";
	public static String closeBrowserFlag = "";
	public static String BrowserType = "";
	public static String parentWindow = "";
	public static String VALUE = "";
	public static String runMode = "";
	public static String testSuiteDescription = "Default";
	public static String testSuiteName = "Default";
	public static int tcPassCount = 0;
	public static int tcFailCount = 0;
	public static boolean stepFailFlag = false;
	public static boolean tcFailFlag = false;
	public static int tsPassCount = 0;
	public static int tsFailCount = 0;
	public static int executionId = 0;
	public static int iSleepMin = 1000;
	public static int iSleepMid = 3000;
	public static int iSleepMax = 5000;
	public static String traceMessage = "";
	
	public static boolean enviromentIsOn = false;
	public static String enviromentFileName = "";
	
	public static String tcTrace = "";
	public static String DBConnectionProperty = "";
	/**
     * Property file path to read Db connectiion details
     */
	public static String propertyFileToRead = System.getProperty("user.dir")
            + "/objects/environment.properties";
	
	public static String objectsFolderPath = System.getProperty("user.dir")
        + "/objects/";
    
    /**
	 * Added to store result to db
	 */
	public static Set<StepResult> stepResults = new LinkedHashSet<StepResult>();
	public static Set<TestcaseResult> testcaseResults = new LinkedHashSet<TestcaseResult>();
	public static Set<TestSuiteResult> testSuiteResults = new LinkedHashSet<TestSuiteResult>();
	//public static ExecutionMaster executionMaster = new ExecutionMaster();
    public static String debugFlag = "N";
   

	public static boolean getFlag() {

		return Flag;
	}

	public static void closeBrowser(String BrowserProc) throws IOException {

		if (closeBrowserFlag.equalsIgnoreCase("ON")) {
			if (BrowserProc.equalsIgnoreCase("ALL")) {
				Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe /T");
				Runtime.getRuntime().exec("taskkill /F /IM firefox.exe /T");
				Runtime.getRuntime().exec("taskkill /F /IM chrome.exe /T");
				Runtime.getRuntime().exec(
						"taskkill /F /IM IEDriverServer.exe /T");
			} else {
				Runtime.getRuntime().exec(
						"taskkill /F /IM " + BrowserProc + " /T");
			}
		}

	}

	public static void setListValue(int listOfUniqueReferenceNumber,
			String getValue) {
		listOfUniqueValues.add(listOfUniqueReferenceNumber, getValue);

	}

	public static String getListValue(int index) {
		String txtVal = "";
		txtVal = listOfUniqueValues.get(index).toString().trim();
		return txtVal;

	}

	public static void runUtility(String cmd, String path) throws Exception {

		Runtime.getRuntime().exec(
				cmd + System.getProperty("user.dir") + "\\externalUtility\\"
						+ path);
	}

	public static void setWindowHandle(int listOfUniqueReferenceNumber,
			String getValue) {
		setParentWindowHandle.add(listOfUniqueReferenceNumber - 1, getValue);

	}

	public static String getWindowHandle(int index) {
		String txtVal = "";
		txtVal = setParentWindowHandle.get(index - 1).toString().trim();
		return txtVal;
	}

}
