package operation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import Utility.GlobalLib;
import Utility.ProvisoException;

import constants.Separator;

/**
 * Perform file related operation in this file
 * @author Chetan.Aher
 *
 */
public class FileOperation {

    /**
     * Copy file from source to destination
     * @param source
     * @param destination
     * @return 
     * @throws IOException 
     */
    public static String copyFile(String source, String destination) throws IOException {
        String result = "Fail";
        File sourceFile = new File(source);
        File dest = new File(destination);
        
        FileUtils.copyFile(sourceFile, dest);
        result = "Pass";
        
        return result;
    }
    
    /**
     * Replace template file content to create dynamically generated file 
     * and use it for upload testing
     * 
     * @param templateFile Template file location which will get replaced  
     * @param destinationFile specify destination file which need to be created
     * @param templateFilePath Template file path
     * @param replacedFilePath Location to store replaced dynamically generated file to store
     * @param replacements constant to specify file type
     * @return file name 
     * @throws ProvisoException 
     */
    public static String replaceFileContentToUpload(String templateFile,
    String destinationFile, String templateFilePath, String replacedFilePath, Map<String, String> replacements) throws ProvisoException {
    
    try {
        // read specified file
    	String generatedFileContent = "",TempFile= "", tempfname= "";
    	File file = null;
    	BufferedReader reader = null;
    	BufferedReader readerCount = null;
    	PropertyFileOperation propertyFileOperation = new PropertyFileOperation();
    	if(templateFile.startsWith("${READVAR_")){
    		  templateFile = templateFile.replace("READVAR", "Prop");
    		  generatedFileContent = GlobalLib.getPropertyOrValue(templateFile);
    	      reader = new BufferedReader(new StringReader(generatedFileContent));
    	      readerCount = new BufferedReader(new StringReader(generatedFileContent));
    	}
    	else {
    		File folder = new File(templateFilePath);
    		File[] listOfFiles = folder.listFiles();
    		boolean isFileFound = false;
    		if(templateFile.contains(".")){
    		tempfname = templateFile.substring(0,templateFile.lastIndexOf((".")));}
    		else{
    			tempfname = templateFile;
    		}
		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		    	  TempFile = listOfFiles[i].getName();
		    	  if(TempFile.contains(tempfname)){
		          isFileFound=true;
		    	  break;
		    	  }
		      }
    	}
		    if(isFileFound){
		    	System.out.println("TemplateFile File Found containing characters:- " + tempfname);	
		    }
		    else{
		    	System.out.println("Invalid File name provided containing string - "+templateFile);
		    	throw new ProvisoException("Invalid File name provided containing string - "+templateFile );
		        }
		    
		    file = new File(templateFilePath + TempFile);
            reader = new BufferedReader(new FileReader(file));
            readerCount = new BufferedReader(new FileReader(file));
    	}
    	String line = "", templateFileContent = "";
        
        int lines = 0;
        while (readerCount.readLine() != null) {
        	lines++;
        } 
        readerCount.close();
        
        int count = 1;
        while ((line = reader.readLine()) != null) {
	        templateFileContent += line;
	        if (count < lines) {
	        	templateFileContent += "\r\n";
	        }
	        count++;
        }
        
        reader.close();
        
         generatedFileContent = templateFileContent;
        
        // replace content by map key value
        for (Entry<String, String> entry : replacements.entrySet()) {
            if(entry.getValue().contains("PROP_")){
                String random = propertyFileOperation.readProperty(entry.getValue().split(Separator.SEPARATOR_UNDERSCORE, 2)[1]);
                generatedFileContent = generatedFileContent.replaceAll(entry.getKey(), random);
            }else{
                generatedFileContent = generatedFileContent.replaceAll(entry.getKey(), entry.getValue());
            }
        }

        if(destinationFile.contains("."))
        {
        	String ext = FilenameUtils.getExtension(replacedFilePath + destinationFile); 
        	if(!ext.isEmpty()){
        		// create new file at specified location
        		 FileWriter writer = new FileWriter(new File(replacedFilePath + destinationFile));
        	        writer.write(generatedFileContent);
        	        writer.close();
        	}
        }
        else 
        {
        	propertyFileOperation.writeProperty(destinationFile+Separator.SEPARATOR_DOUBLE_HASH+generatedFileContent);
        }
        
        System.out.println("New file/Varible created with name:"+ destinationFile);
    	
    } catch (IOException ioe) {
        System.out.println("Exception while replacing upload file from template");
        ioe.printStackTrace();
    }
    
    return destinationFile;
    }

    /**
     * Convert date to specific date format
     * 
     * @param date
     * @param format
     * @return
     */
    public static String convertDateToFormat(Date date, String format) {
    DateFormat formatedDate = new SimpleDateFormat(format);

    return formatedDate.format(date);
    }
    
    /**
     * Edit file by specifying source and destination
     * 
     * @param src
     * @param dest
     * @return
     * @throws IOException
     */
    public static boolean editFile(String src, String dest) throws IOException{
        try{
            File fin = new File (src);
            FileInputStream fis = new FileInputStream(fin);
            //Construct BufferedReader from InputStreamReader
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        
            FileWriter fw = new FileWriter(dest);
        
            int counter = 0;
            String line = null;
            while ((line = br.readLine()) != null) {
                char firstChar = line.charAt(0);
                if(String.valueOf(firstChar).equals("1") || String.valueOf(firstChar).equals("9")){
                    if(counter<2){
                        fw.write(line);
                        fw.write("\n");
                        counter++;
                    }
                }           
            }
            fw.close();     
            br.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Create or delete file specified at location 
     * @param type
     * @param fileToCreate
     * @return
     * @throws IOException 
     */
    public static String createOrDeleteFile(String type, String fileToCreate) throws IOException {
        File file = new File(fileToCreate);
        String result = "Fail";
        
        switch (type) {
        case "CREATE":
            if (file.createNewFile()) {
                System.out.println("File is created!");
                result = "Pass";
            } else {
                System.out.println("File already exists.");
            }
            break;
        case "DELETE":
            if (file.delete()) {
                System.out.println(file.getName() + " is deleted!");
                result = "Pass";
            } else {
                System.out.println("Delete operation is failed.");
            }
            break;

        default:
            System.out.println("Invalid file openration type");
            break;
        }

        return result;
    }

}
