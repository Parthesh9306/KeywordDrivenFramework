package Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import org.apache.commons.lang.RandomStringUtils;

import constants.Separator;

/**
 * Generate random values
 * 
 * @author Chetan.Aher
 *
 */
public class Generator {
	private String pre ="";
	
	private String post ="";
	
	/**
	 * Generate random values by providing types and formats
	 * 
	 * @param types
	 * @param formats
	 * @return
	 */
	public String generateRandomValue(String types, String formats) {
		String generatedValue = "";
		String[] typesArray = types.split(Separator.SEPARATOR_UNDERSCORE);
		String[] formatsArray = formats.split(Separator.SEPARATOR_UNDERSCORE);
		if (formatsArray.length != typesArray.length) {
			System.out.println("Invalid types and format");
		} else {
			for (int i = 0; i < formatsArray.length; i++) {
				generatedValue += getRandomValueByType(typesArray[i],
						formatsArray[i]);
			}
		}
		
		return pre + generatedValue + post;
	}

	/**
	 * Generate separate random value by type
	 * 
	 * @param type
	 * @param formate
	 * @return
	 */
	public String getRandomValueByType(String type, String formate) {
		switch (type) {
		case "DATE":
			SimpleDateFormat sdf = new SimpleDateFormat(formate);
			Date date = new Date();
			return sdf.format(date);
			
		case "STRING":
			return RandomStringUtils.randomAlphabetic(Integer.parseInt(formate)).toLowerCase();
		
		case "NUMBER":
			return RandomStringUtils.randomNumeric(Integer.parseInt(formate));

		case "PRE":
			pre = formate;
			break;
	
		case "POST":
			post = formate;
			break;
			
		case "DATEOPERATION":
			String[] dateFormatAndOperation = formate.split(Separator.SEPARATOR_DOUBLE_EQUALTO);
			DateFormat dateFormat = new SimpleDateFormat(dateFormatAndOperation[0]);
	        Calendar cal = Calendar.getInstance();
	        cal.add(Calendar.DATE, Integer.parseInt(dateFormatAndOperation[1]));        
	        return dateFormat.format(cal.getTime());
		case "UTCTIME":
		    dateFormatAndOperation = formate.split(Separator.SEPARATOR_DOUBLE_EQUALTO);
		    TimeZone timeZone = TimeZone.getTimeZone("UTC");
		    Calendar calendar = Calendar.getInstance(timeZone);
		    SimpleDateFormat simpleDateFormat = 
		           new SimpleDateFormat(dateFormatAndOperation[0], Locale.US);
		    simpleDateFormat.setTimeZone(timeZone);

		    System.out.println("Time zone: " + timeZone.getID());
		    System.out.println("default time zone: " + TimeZone.getDefault().getID());
		    System.out.println();

		    System.out.println("UTC:     " + simpleDateFormat.format(calendar.getTime()));
		    System.out.println("Default: " + calendar.getTime());
          
           return simpleDateFormat.format(calendar.getTime());
	    
		case "SPECIALCHAR":
			int formatlen = formate.length();
			Random rd = new Random();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < formatlen; i++) {
			    sb.append(formate.charAt(rd.nextInt(formatlen)));
			}
			return RandomStringUtils.randomAlphabetic(Integer.parseInt(formate)).toLowerCase();
			
		default:
			System.out.println("Invalid random generator type");
			break;
		}
		return "";
	}
	
	
}
