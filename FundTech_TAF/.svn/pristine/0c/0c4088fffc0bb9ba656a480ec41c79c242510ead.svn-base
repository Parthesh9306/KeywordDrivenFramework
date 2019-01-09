 package excelExportAndFileIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadAFWExcelFile {
	/***
	 * readExcel: Read data from the excel sheet
	 * 
	 * @param filePath
	 *            : Location of file
	 * @param fileName
	 *            : name of the excel File
	 * @param sheetname
	 *            : name of the excel sheet
	 * @return: name of the excel sheet
	 * 
	 * ****/
	
	public Sheet readExcel(String filePath,String fileName,String sheetName) throws IOException{
	//Create a object of File class to open xlsx file
	
	File file =	new File(filePath+"\\"+fileName);
	if (!file.exists()) {
		fileName = fileName.replaceAll(".xlsx", ".xls");
		file =	new File(filePath+"\\"+fileName);
	}
	//Create an object of FileInputStream class to read excel file
	FileInputStream inputStream = new FileInputStream(file);
	Workbook AFWWorkbook = null;
	//Find the file extension by spliting file name in substing and getting only extension name
	String fileExtensionName = fileName.substring(fileName.indexOf("."));
	//Check condition if the file is xlsx file
	if(fileExtensionName.equals(".xlsx")){
	//If it is xlsx file then create object of XSSFWorkbook class
	AFWWorkbook = new XSSFWorkbook(inputStream);
	}
	//Check condition if the file is xls file
	else if(fileExtensionName.equals(".xls")){
		//If it is xls file then create object of XSSFWorkbook class
		AFWWorkbook = new HSSFWorkbook(inputStream);
	}
	//Read sheet inside the workbook by its name
	Sheet  AFWSheet = AFWWorkbook.getSheet(sheetName);
	 return AFWSheet;	
	}
}
