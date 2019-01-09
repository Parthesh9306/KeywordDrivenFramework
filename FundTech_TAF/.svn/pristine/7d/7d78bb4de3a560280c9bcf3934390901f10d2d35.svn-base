package operation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.stream.StreamSource;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.soap.MessageFactoryImpl;
import org.xml.sax.SAXException;

import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCaseRunner;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestRequestStepResult;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestStep;
import com.eviware.soapui.model.testsuite.TestStepResult;
import com.eviware.soapui.support.SoapUIException;
import com.eviware.soapui.support.types.StringToObjectMap;



public class SOAUIRunner {
	
	 public static WsdlProject project = null;
	 static String testSuite = null;
	 static String testCase = null;
	 //static String testStep = null;
	 static WsdlTestStep testStepObj = null; 
	 static WsdlTestCase testCaseObj = null;
		
	public static String RunTestStep(WsdlProject project,String StepName) throws Exception, IOException, SoapUIException{

	/*// retrieve all test suite from project 
	List<TestSuite> testSuiteList = project.getTestSuiteList(); 

	// Iterate all TestSuites of project 
	for (TestSuite ts : testSuiteList) {
	 System.out.println("****Running Test suite " + ts.getName() + "********");
	 // Retrieve all TestCases from a particular 
	 List<TestCase> testCaseList = ts.getTestCaseList();
	 // Iterate all TestCases of the particular TestSuite
	 for (TestCase testcase : testCaseList) {
	 System.out.println("****Running Test Case " + testcase.getName()+ "*****");
	 }
	 }*/
	
	//testCaseObj = project.getTestSuiteByName(testSuite).getTestCaseByName(testCase);
	//testStep = testCase.getTestStepByName( StepName );
	//testCaseObj = testCase;
		//testStepObj = testCaseObj.getTestStepByName( StepName );
		System.out.println(StepName);
			WsdlTestStep testStep = testCaseObj.getTestStepByName(StepName);
			testStepObj = testStep;
	/*WsdlTestStep testStep1 = testCase.getTestStepByName( "Save businessDate to ObusinessDate");
	WsdlTestStep testStep2 = testCase.getTestStepByName( "MT103 Message");
	WsdlTestStep testStep3 = testCase.getTestStepByName( "Delay 1");
	WsdlTestStep testStep4 = testCase.getTestStepByName( "Get message MT103 P_MID" );
	WsdlTestStep testStep5 = testCase.getTestStepByName( "Save P_MID to MT103 MID");
	WsdlTestStep testStep6 = testCase.getTestStepByName( "Validate MT103 message status - CDBWAIT");*/
	
	WsdlTestCaseRunner runner = new WsdlTestCaseRunner(testCaseObj, new StringToObjectMap());
	TestStepResult runResult = null;
	
	runResult = runner.runTestStep(testStep);

	if (runResult.getStatus().name().equalsIgnoreCase("OK") || runResult.getStatus().name().equalsIgnoreCase("UNKNOWN")) {
	    if (runResult instanceof WsdlTestRequestStepResult) {
	        String response = ((WsdlTestRequestStepResult) runResult).getResponse().getContentAsString();
	        System.out.println("Soap Response => "+response);
	     }
	    return "Pass";
	}
	
	return "Fail";
	/*runner.runTestStep( testStep1 );
	runner.runTestStep( testStep2 );
	runner.runTestStep( testStep3 );
	runner.runTestStep( testStep4 );
	runner.runTestStep( testStep5 );
	runner.runTestStep( testStep6 );*/
	//String tcp = testCase.getPropertyValue("ObusinessDate");
	/*String tsp = testStep2.getPropertyValue("Response");
	String tsp4 = testStep4.getPropertyValue("ResponseAsXml");*/
	
	//System.out.println(tcp);
	//System.out.println(tsp);
	/*System.out.println(tsp4);
	System.out.println(tsp4.contains("fetchSize=\"10\""));
	*/
	
	}
	
	public static void setSOAPUITestSuite(String SuiteName){
		
		testSuite = SuiteName;
	}
	
	public static void setSOAPUITestCase(String TestCaseName) throws Exception, IOException, SoapUIException{
		testCase = TestCaseName;
		testCaseObj = project.getTestSuiteByName(testSuite).getTestCaseByName(testCase);
	}
	
	public static String getSOAPUITestCaseProperty(String propertyName) throws Exception, IOException, SoapUIException{
		
		String propertyValue = testCaseObj.getPropertyValue(propertyName);
		return propertyValue;
	}
	
	public static String getSOAPUITestStepProperty(String propertyName) throws Exception, IOException, SoapUIException{
		
		String propertyValue = testStepObj.getPropertyValue(propertyName);
		return propertyValue;
	}
	
	public static boolean setSOAPUITestStepProperty(String propertyName, String value, String testStep) throws Exception, IOException, SoapUIException{
            
            if(testCaseObj.getTestStepByName(testStep).hasProperty(propertyName)){
                   testCaseObj.getTestStepByName(testStep).setPropertyValue(propertyName, value);
                         return true;
            }else{
                   System.out.println("Property name :"+propertyName +" does not exist");
                   return false;
            }
	}
	

	/**
	 * Set Soap ui testcase property
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 * @throws Exception
	 * @throws IOException
	 * @throws SoapUIException
	 */
	public static boolean setSOAPUITestCaseProperty(String propertyName, String value) throws Exception, IOException, SoapUIException{
		if(testCaseObj.hasProperty(propertyName)){
			testCaseObj.setPropertyValue(propertyName, value);
				return true;
		}else{
			System.out.println("Property name :"+propertyName +" does not exist");
			return false;
		}		
	}
	
	/**
	 * Execute Soap Request
	 * 
	 * @param soapEndPoint
	 * @param soapMessage
	 * @return
	 * @throws SAXException
	 * @throws ServiceException
	 * @throws AxisFault
	 * @throws SOAPException
	 * @throws ParserConfigurationException 
	 * @throws UnsupportedEncodingException 
	 */
	public static boolean executeSOAPRequest(String soapEndPoint, String soapMessage,String encoding) throws SAXException, ServiceException, AxisFault, SOAPException, UnsupportedEncodingException{
		byte[] reqBytes = null;
		//Convert SOAP Message to ISO-8859-1/ UTF-8 
		byte[] req = soapMessage.getBytes();
		String soapMessageISO = new String(req, encoding);//ISO-8859-1 / UTF-8
		reqBytes = soapMessageISO.getBytes();
		
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(reqBytes);
		StreamSource streamSource = new StreamSource(byteArrayInputStream);
		
		//Create a SOAP Message Object
		MessageFactoryImpl messageFactory = new MessageFactoryImpl();
		SOAPMessage message = messageFactory.createMessage();
		message.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, encoding);
		SOAPPart soapPart = message.getSOAPPart();
		//Set the soapPart Content with the stream source
		soapPart.setContent(streamSource);
		
		//Create a WebService Call
		Service service = new Service();
		Call call = (Call)service.createCall();
		call.setTargetEndpointAddress(soapEndPoint);
		
		//Invoke the WebService.
		SOAPEnvelope soapEnvelope = call.invoke(((org.apache.axis.SOAPPart)soapPart).getAsSOAPEnvelope());
		
		//Reading result
		System.out.println("SOAP request executed sucessfully with below response .......");
		System.out.println(soapEnvelope.toString());
		
		return true;
	}
}
