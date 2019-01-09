
package textVerifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.eviware.soapui.support.SoapUIException;

import constants.Separator;

import operation.SOAUIRunner;

/**
 * Read file and verify its content by specifying length
 * 
 * @author Chetan.Aher
 *
 */
public class TextVerifier {
    /**
     * Folder location from where the verification file will be fetched
     */
    private final String filePathToVerify = System.getProperty("user.dir") + "\\FileToVerifyFrom\\";
    
    /**
     * Excel input parameter length to verify
     */
    public static final int VERIFY_PARAMETER_LENGTH = 3;
    
    /**
     * After separating 
     */
    public static final int VERIFY_PARAMETER_SEPARATOR_LENGTH = 2;

    /**
     * Pass file name, string length separated by '-' and values to verify separated by ',' and all separated by ':'
     * e.g.  sampleText.txt:0-8,8-10,10-16:UXPIIEU ,04,000104
     * Explanation: 
     * sampleText.txt filename to get from folder of automation specified
     * 0-8,8-10,10-16 lengths to and from find
     * UXPIIEU ,04,000104 file content to verify with
     * (Note : Add spaces if text contains any)
     * 
     * @param fileNameAndStringLengthAndValueToVerify e.g. sampleText.txt:0-8,8-10,10-16:UXPIIEU ,04,000104
     * @return Pass if match found else return content which not match with fail status  
     * @throws IOException
     */
    public String verifyStringByLength(String fileNameAndStringLengthAndValueToVerify) throws IOException {

	String[] fileNameAndStringLengthAndValueToVerifySplitArray = fileNameAndStringLengthAndValueToVerify.split(Separator.SEPARATOR_COLON);
	if (fileNameAndStringLengthAndValueToVerifySplitArray.length != VERIFY_PARAMETER_LENGTH) {
	    System.out.println("Invalid parameter provided to verify by length");
	}

	String fileName = fileNameAndStringLengthAndValueToVerifySplitArray[0];
	String[] stringLengthsArrayToVerify = fileNameAndStringLengthAndValueToVerifySplitArray[1].split(Separator.SEPARATOR_COMMA);
	String[] stringsArrayToVerify = fileNameAndStringLengthAndValueToVerifySplitArray[2].split(Separator.SEPARATOR_COMMA);
	
	if (stringLengthsArrayToVerify.length != stringsArrayToVerify.length) {
	    System.out.println("Invalid string lengths or string provided to verify by length");
	}
	
	String fileContent = readFile(filePathToVerify + fileName);
	
	for (int i = 0; i < stringsArrayToVerify.length; i++) {
	    String[] lengthToFromArray = stringLengthsArrayToVerify[i].split(Separator.SEPARATOR_DASH);
	
	    if (lengthToFromArray.length != VERIFY_PARAMETER_SEPARATOR_LENGTH) {
		System.out.println("Invalid length parameter provided");
	    }
	    
	    String fetchedString = fileContent.substring(Integer.parseInt(lengthToFromArray[0]), Integer.parseInt(lengthToFromArray[1]));
	    
	    if (!fetchedString.equals(stringsArrayToVerify[i])) {
		return fetchedString + "," + stringsArrayToVerify[i] + ",Fail";
	    }
	}
	
	return "Pass";
    }
    
    /**
     * Read file by path specified
     * 
     * @param fileName file name or path from which need to read file
     * @return whole file content
     * @throws IOException
     */
    private String readFile(String fileName) throws IOException {
	BufferedReader br = new BufferedReader(new FileReader(fileName));
	try {
	    StringBuilder sb = new StringBuilder();
	    String line = br.readLine();

	    while (line != null) {
		sb.append(line);
		sb.append("\n");
		line = br.readLine();
	    }
	    return sb.toString();
	} finally {
	    br.close();
	}
    }

    /**
     * Verify by string length and soap ui properties 
     * 
     * @param fileNameAndStringLengthAndSoapUIProperties sampleText.txt:0-6:ObusinessDate
     * filename : string length : soapui testcase property
     * @return
     * @throws IOException 
     * @throws SoapUIException 
     * @throws Exception
     */
    public String verifyByStringLengthAndSoapUIProperties(String fileNameAndStringLengthAndSoapUIProperties) throws IOException, SoapUIException, Exception  {
	String[] stringFileNameAndLengthsAndSoapUIPropertiesArrayToVerify = fileNameAndStringLengthAndSoapUIProperties.split(Separator.SEPARATOR_COLON);
	if (stringFileNameAndLengthsAndSoapUIPropertiesArrayToVerify.length != VERIFY_PARAMETER_LENGTH) {
	    System.out.println("Invalid parameter provided to verify by length");
	}

	String fileName = stringFileNameAndLengthsAndSoapUIPropertiesArrayToVerify[0];
	String[] stringLengthsArrayToVerify = stringFileNameAndLengthsAndSoapUIPropertiesArrayToVerify[1].split(Separator.SEPARATOR_COMMA);
	String[] propertiesArrayToVerify = stringFileNameAndLengthsAndSoapUIPropertiesArrayToVerify[2].split(Separator.SEPARATOR_COMMA);
	
	if (stringLengthsArrayToVerify.length != propertiesArrayToVerify.length) {
	    System.out.println("Invalid string lengths or string provided to verify by length and property");
	}
	String fileContent = readFile(filePathToVerify + fileName);
	
	for (int i = 0; i < stringLengthsArrayToVerify.length; i++) {
	    String[] lengthToFromArray = stringLengthsArrayToVerify[i].split(Separator.SEPARATOR_DASH);
	
	    if (lengthToFromArray.length != VERIFY_PARAMETER_SEPARATOR_LENGTH) {
		System.out.println("Invalid length parameter provided");
	    }
	    String soapUITestcaseProperty = SOAUIRunner.getSOAPUITestCaseProperty(propertiesArrayToVerify[i]);
	    String fetchedString = fileContent.substring(Integer.parseInt(lengthToFromArray[0]), Integer.parseInt(lengthToFromArray[1]));
	    System.out.println("Soap Ui property=>" + soapUITestcaseProperty+ " File string=>"+ fetchedString);
	    if (!soapUITestcaseProperty.equals(fetchedString)) {
		return fetchedString + "," + soapUITestcaseProperty + ",Fail";
	    }
	}
	
	return "Pass";
    }
}
