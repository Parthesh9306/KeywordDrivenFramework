package operation;

import static com.jayway.restassured.RestAssured.given;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.http.TruncatedChunkException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Utility.ProvisoException;

import com.fasterxml.jackson.databind.type.MapType;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Headers;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.jayway.restassured.specification.ResponseSpecification;

import constants.Separator;


/**
 * Execute Rest Request(GET/POST/PUT/DELETE)
 * @author Atul.Gadhave
 *
 */

public class ExecuteREST {
	
	public static String restResponseStatusCode = null;
	public static String restResponseStatusLine = null;
	public static String restResponse = null;
	public static Iterator<Header> restResponseHeaders = null;
	public static RequestSpecBuilder builder = new RequestSpecBuilder();
	public static RequestSpecification requestSpec = null;
	public static ResponseSpecification responseSpecification = null;
	public static Response response = null;
	public static String responseObject = null;
	

	 /**
     * Execute GET Rest Request
     * @param baseURL
     * @return Response
     * @throws JSONException 
     */
	public static String executeRestGETRequest(String baseURL,HashMap<String, String> headers, String user, String pwd) throws JSONException {
		
		
		if(headers != null){
			builder.addHeaders(headers);
		}
		requestSpec = builder.build();
		if(user != null && pwd != null){
			requestSpec.authentication().preemptive().basic(user, pwd);
		}	
		responseSpecification = given().spec(requestSpec).expect();
		
		response = responseSpecification.get(baseURL);
		
		restResponseStatusCode = String.valueOf(response.getStatusCode());
		restResponseStatusLine = response.getStatusLine();
		restResponse = response.asString();
		if(!headers.isEmpty()){
		restResponseHeaders = response.getHeaders().iterator();
		}
		
		System.out.println("Request submitted successfully, response details are as below:");
        System.out.println("StatusCode: " + restResponseStatusCode);
        System.out.println("StatusLine: " + restResponseStatusLine);
		System.out.println("Rest Response: "+ restResponse);
		return restResponse;
	}
	
	 /**
     * Execute POST Rest Request
     * @param  baseURL
     * @param  apiBody
     * @return Response
     * @throws JSONException,InterruptedException,IOException 
     */
	public static String  executeRestPOSTRequest(String baseURL,String apiBody,HashMap<String, String> headers, String user, String pwd) throws JSONException,InterruptedException,IOException,TruncatedChunkException {
		 
		if(null != headers  ){
			builder.addHeaders(headers);
		}
		builder.setBody(apiBody);
		requestSpec = builder.build();
		
		if(null !=  user && null != pwd ){
			requestSpec.authentication().preemptive().basic(user, pwd);
		}		
		responseSpecification = given().spec(requestSpec).expect();
		response = responseSpecification.post(baseURL);				
			
		restResponseStatusCode = String.valueOf(response.getStatusCode());
		restResponseStatusLine = response.getStatusLine();
		System.out.println("Response :- " + response.getStatusCode());
		restResponse = response.asString();
		//response.jsonPath();
		if(!headers.isEmpty()){
			restResponseHeaders = response.getHeaders().iterator();
		}
		
		System.out.println("Request submitted successfully, response details are as below:");
        System.out.println("StatusCode: " + restResponseStatusCode);
        System.out.println("StatusLine: " + restResponseStatusLine);
		System.out.println("Rest Response: "+ restResponse);	
		return restResponse;
	
	}

	 /**
     * Execute PUT Rest Request
     * @param  baseURL
     * @param  apiBody
     * @return Response
     * @throws JSONException,InterruptedException,IOException 
     */
	public static String executeRestPUTRequest(String baseURL,String apiBody,HashMap<String, String> headers, String user, String pwd) throws JSONException,InterruptedException,IOException {
		 
		if(headers != null){
			builder.addHeaders(headers);
		}
		builder.setBody(apiBody);
		requestSpec = builder.build();
		
		if(user != null && pwd != null){
			requestSpec.authentication().preemptive().basic(user, pwd);
		}		
		responseSpecification = given().spec(requestSpec).expect();
		response = responseSpecification.put(baseURL);		
		
		restResponseStatusCode = String.valueOf(response.getStatusCode());
		restResponseStatusLine = response.getStatusLine();
		restResponse = response.asString();
		if(!headers.isEmpty()){
			restResponseHeaders = response.getHeaders().iterator();
		}
		
		System.out.println("Request submitted successfully, response details are as below:");
        System.out.println("StatusCode: " + restResponseStatusCode);
        System.out.println("StatusLine: " + restResponseStatusLine);
		System.out.println("Rest Response: "+ restResponse);	
		return restResponse;       
	}
	
	 /**
     * Execute POST Rest Request
     * @param  baseURL
     * @return Response
     * @throws JSONException 
     */
	
	public static String executeRestDELETERequest(String baseURL,HashMap<String, String> headers, String user, String pwd) throws JSONException {
		
		if(headers != null){
			builder.addHeaders(headers);
		}
		requestSpec = builder.build();
		if(user != null && pwd != null){
			requestSpec.authentication().preemptive().basic(user, pwd);
		}
		responseSpecification = given().spec(requestSpec).expect();
		response = responseSpecification.delete(baseURL);
		
		restResponseStatusCode = String.valueOf(response.getStatusCode());
		restResponseStatusLine = response.getStatusLine();
		restResponse = response.asString();
		if(!headers.isEmpty()){
			restResponseHeaders = response.getHeaders().iterator();
		}
		
		System.out.println("Request submitted successfully, response details are as below:");
        System.out.println("StatusCode: " + restResponseStatusCode);
        System.out.println("StatusLine: " + restResponseStatusLine);
		System.out.println("Rest Response: "+ restResponse);	
		return restResponse; 
	}
	
	
	public static boolean verifyRESTAttributes(String JSONResponse,String verifyAttrStr, String operator) throws JSONException,ProvisoException, JsonParseException, JsonMappingException, IOException{
		//responseObject = JSONResponse;
		
		/*JSONArray JSONResponseArr = null;
		JSONArray JSONAttributeArr = null;
		if(JSONResponse.startsWith("["))
		{
			JSONResponseArr = new   JSONArray(JSONResponse);
		}
		else {
			JSONResponseArr = new   JSONArray("["+JSONResponse+"]");
		}
		
		if(verifyAttr.startsWith("["))
		{
			JSONAttributeArr = new   JSONArray(verifyAttr);
		}
		else {
			JSONAttributeArr = new   JSONArray("["+verifyAttr+"]");
		}*/
         //HashMap<String, String> JSONresponseData = new HashMap<String, String> ();
         //HashMap<String, String> verifyAttrdata = new HashMap<String, String> ();
         boolean attributeFlag = false;
         ObjectMapper responseMapper = null;
         ObjectMapper verifyAttrMapper = new ObjectMapper();
    	 org.codehaus.jackson.map.type.MapType responseMaptype = null;
    	 org.codehaus.jackson.map.type.MapType verifyAttrMaptype = null;
    	 Map<String, Object> JSONresponseData = null;
    	 Map<String, Object> verifyAttrdata = null;
    	 String verifyAttr = verifyAttrStr.replaceAll("NULL", "null");
    	 
         if(JSONResponse.length() > 0){
        	 responseMapper = new ObjectMapper();
        	 responseMaptype   = responseMapper.getTypeFactory().constructMapType(Map.class, java.lang.String.class,java.lang.Object.class);
        	 JSONresponseData = responseMapper.readValue(JSONResponse, responseMaptype);
             //System.out.println("JSON Response Parser:- "+JSONresponseData);
     	    /*for(int i = 0; i<JSONResponseArr.length(); i++)
     	    {
     	    	JSONObject JSONResp = (JSONObject) JSONResponseArr.get(i);
     	    	String JSONRespArr[] = JSONResp.toString().split(Separator.SEPARATOR_COMMA);
     	    	for(String JSONStrObj : JSONRespArr){
     	    		String JSONst = JSONStrObj.replaceAll("\\{", "").replaceAll("\\}","").replaceAll("\\[","").replaceAll("\\]","");
     	    		JSONresponseData.put(JSONst.split(Separator.SEPARATOR_COLON)[0], JSONst.split(Separator.SEPARATOR_COLON)[1]);
     	    		
     	    	if(JSONResp.has("address1")){
       			  System.out.println("Response Attribute :-"+respJSON.getString("address1"));
       		  }
     	    	}
     	    }            	   
     	 */
        	 
        	
        	 
         if(verifyAttr.length() > 0){
        	 verifyAttrMapper = new ObjectMapper();
        	 verifyAttrMaptype   = verifyAttrMapper.getTypeFactory().constructMapType(Map.class, java.lang.String.class,java.lang.Object.class);
        	 verifyAttrdata = verifyAttrMapper.readValue(verifyAttr, verifyAttrMaptype);
             //System.out.println("JSON Verify Attributes Parser:- "+verifyAttrdata);
     	   /* for(int i = 0; i<JSONAttributeArr.length(); i++)
     	    {
     	    	JSONObject attribute = (JSONObject) JSONAttributeArr.get(i);
     	    	String JSONAttrArr[] = attribute.toString().split(Separator.SEPARATOR_COMMA);
     	    	for(String JSONAttrStrObj : JSONAttrArr){
     	    		String JSONst = JSONAttrStrObj.replaceAll("\\{", "").replaceAll("\\}","").replaceAll("\\[","").replaceAll("\\]","");
     	    	verifyAttrdata.put(JSONst.split(Separator.SEPARATOR_COLON)[0], JSONst.split(Separator.SEPARATOR_COLON)[1]); 
     	    }
     	    }*/ 
        	 
     	 }
         else {
         	System.out.println("Incorrect Attributes provided for Verification");
  			return false;
         }
         switch(operator)
         {
       case "=":
    	   /*
    	    * Case "=" used only when key value comparison  
    	    * For Sub attributes comparison you need to pass parent attribute first and then inside it can have any(less) number of sub attributes
    	    */
    	   for(String keyAttr : verifyAttrdata.keySet()){
            	if(JSONresponseData.containsKey(keyAttr)){
            		System.out.println("Going to Verify Attribute Key:- "+keyAttr);
            		if(null != JSONresponseData.get(keyAttr) && null != verifyAttrdata.get(keyAttr))
            		{
            			if((JSONresponseData.get(keyAttr).toString()).equals((verifyAttrdata.get(keyAttr).toString()))){
            				System.out.println("Expected Attribute Key : "+keyAttr+" : Expected Attribute Value : "+JSONresponseData.get(keyAttr));
   		        			//System.out.println("Given Attribute present in Rest Response : "+keyAttr.replace("{", ""));
   		        			//System.out.println("Expected Attribute Key : "+keyAttr.replace("{", "")+" : Expected Attribute Value : "+JSONresponseData.get(keyAttr).replace("}", ""));
   		        			//Debug.traceMessage = Debug.traceMessage  + "Expected Key : "+keyAttr+"  Expected Value : "+JSONresponseData.get(keyAttr).replace("}", "");
            				attributeFlag = true;
   		        			}
        			else{
        				if(verifyAttrdata.get(keyAttr).toString().startsWith("{") || verifyAttrdata.get(keyAttr).toString().startsWith("[{")){
        					//JSONArray jsonVerify = null;
        				/*	if(!verifyAttrdata.get(keyAttr).toString().startsWith("[")){
        						jsonVerify = new JSONArray("["+verifyAttrdata.get(keyAttr).toString()+"]");
        					}
        					else{
        					jsonVerify = new JSONArray(verifyAttrdata.get(keyAttr).toString());
        					}*/
        					//JSONObject jsonResp = new JSONObject(JSONresponseData.get(keyAttr).toString());
        					String verifyArr[] = verifyAttrdata.get(keyAttr).toString().replaceAll("\\{", "").replaceAll("\\[", "").replaceAll("\\}", "").replaceAll("\\]", "").split(",");
        					String respArr[] = JSONresponseData.get(keyAttr).toString().replaceAll("\\{", "").replaceAll("\\[", "").replaceAll("\\}", "").replaceAll("\\]", "").split(",");
        					List<String> verifyList = Arrays.asList(verifyArr);
        					List<String> respList = Arrays.asList(respArr);
        					
        					//Iterator x = jsonVerify.keys(); .replaceAll("\\{", "").replaceAll("\\[", "").replaceAll("\\}", "").replaceAll("\\]", "")
        					//JSONArray jsonVerifyArray = new JSONArray();
        					//jsonVerifyArray = jsonVerify.toJSONArray(jsonVerify.);
        					for(int z = 0;z< verifyList.size(); z++){
        						//String verifyL = verifyList.get(z).replaceAll("\\{", "").replaceAll("\\[", "").replaceAll("\\}", "").replaceAll("\\]", "");
        					if(respList.contains(verifyList.get(z))){
        						attributeFlag = true;
        					}
        				  else{
        					  	System.out.println("One of the Sub attribute value does not matches with given attribute : "+keyAttr);
        					  	System.out.println("Given attribute :- "+keyAttr+"  &  Umatched Sub Attribute :-"+verifyList.get(z));
        						attributeFlag = false;
        						break;
        					}
        				}
        					if(!attributeFlag){
        						break;
        					}
        				}else{ 
        				System.out.println("Rest response Attribute/Sub Attribute value does not matches with given attribute : "+keyAttr);
        				System.out.println("Given Attribute Value : "+verifyAttrdata.get(keyAttr)+" : Expected Attribute Value : "+JSONresponseData.get(keyAttr));
        				//Debug.traceMessage = Debug.traceMessage  + "<br> Rest response does not contains given Attribute : "+keyAttr.replace("{", "");
   	        			attributeFlag = false;
   	        		    break;
        			      }
        			   }
            		}
            		else {
            			System.out.println("Given Attribute value is NULL: "+keyAttr);
            			attributeFlag = true;
            		}
        			}
            	else{
            		System.out.println("Given Attribute/Sub Attribute Key is not present in Rest Response : "+keyAttr);
        			attributeFlag = true;
            	}
                 }
    	   //System.out.println("Number of Attributes present in Rest response are :- "+JSONresponseData.size());
    	  // System.out.println("Number of Attributes present in JSON array that you want to compare with Rest Response :- "+verifyAttrdata.size());
         break;
         case "!=":
        	 /*
      	      * Case "!=" used for
      	      * 1.Verify that attribute value is not NULL / Blank
      	      * 2.If given attribute is  key value pair should not be matched
      	      * 3.If given attribute should not be present in Rest response     
      	      */
        	 for(String keyAttr : verifyAttrdata.keySet()){
        		 if(JSONresponseData.containsKey(keyAttr)){
        			 System.out.println("Going to Verify Attribute Key:- "+keyAttr);
        			 if(null == JSONresponseData.get(keyAttr)){
        				 System.out.println("Given Attribute Value is NULL in Response: "+keyAttr);
		        		    //System.out.println("Given Attribute Key : "+keyAttr.replace("{", "")+" : Given Attribute Value : "+verifyAttrdata.get(keyAttr).replace("}", ""));
		        			//System.out.println("Expected Attribute Key : "+keyAttr.replace("{", "")+" : Expected Attribute Value : "+JSONresponseData.get(keyAttr).replace("}", ""));
		        			Debug.traceMessage = Debug.traceMessage  + "Expected Key : "+keyAttr+"  Expected Value : "+JSONresponseData.get(keyAttr);
		        			attributeFlag = true;
        			 }
        			 else if(null != JSONresponseData.get(keyAttr)) {
        			 if(!(JSONresponseData.get(keyAttr).equals(verifyAttrdata.get(keyAttr)))){
		        			//System.out.println("Given Attribute Value not matches with Rest Response Attribute: "+keyAttr.replace("{", ""));
		        		    System.out.println("Given Attribute Key : "+keyAttr+" : Given Attribute Value : "+verifyAttrdata.get(keyAttr));
		        			System.out.println("Expected Attribute Key : "+keyAttr+" : Expected Attribute Value : "+JSONresponseData.get(keyAttr));
		        			Debug.traceMessage = Debug.traceMessage  + "Expected Key : "+keyAttr+"  Expected Value : "+JSONresponseData.get(keyAttr);
		        			attributeFlag = true;
		        			}
                    }
        		 }
               	else if(!JSONresponseData.containsKey(keyAttr)){
               		System.out.println("Given Attribute Key not present in Rest Response: "+"Key:-"+keyAttr+" : Value:-"+verifyAttrdata.get(keyAttr));
      		        Debug.traceMessage = Debug.traceMessage  + "<br> Attribute Key not matches with Rest Response Key :- "+keyAttr+" : Value :- "+verifyAttrdata.get(keyAttr);
      		        attributeFlag = false;
               	}
         			}
        	 //System.out.println("Number of Attributes present in Rest response are :- "+JSONresponseData.size());
      	     //System.out.println("Number of Attributes present in JSON array that you want to compare with Rest Response :- "+verifyAttrdata.size());
                break;
         default:
                System.out.println("Please provide correct Operator('=' or '!=') for Keyword VERIFY_REST_ATTRIBUTES");
                attributeFlag = false;
         break;        
         } 
        
         if(attributeFlag){
        	    return true;
			}else{
				return false;
			}
		}else{
			System.out.println("Rest response does not contains any attributes");
			 return false;
		}
         
		
	}
	 
	/**
     * Write Attribute value to a variable 
     * colon(:) separated
     * For multiple pass as '##' separated
     * 
     * @param key
     * @param value
     * @throws IOException 
	 * @throws JSONException 
     */
    public static boolean writeAttributeToVariable(String JSONResponse,String valueToWrite) throws IOException, JSONException {
    	/*JSONArray JSONResponseArr = null;
		if(JSONResponse.startsWith("["))
		{
			JSONResponseArr = new   JSONArray(JSONResponse);
		}
		else {
			JSONResponseArr = new   JSONArray("["+JSONResponse+"]");
		}*/
       ObjectMapper responseMapper = null;
       org.codehaus.jackson.map.type.MapType responseMaptype = null;
   	   Map<String, Object> JSONresponseData = null;
   	   
       String filepath = System.getProperty("user.dir")+"/objects/";
       String fileName = "variables.properties";
       
       //HashMap<String, String> JSONresponseData = new HashMap<String, String> ();
       boolean attributeFlag = false;
        File configFile = new File(filepath + fileName);
	    Properties props = new Properties();
	    
	    if (configFile.exists()) {
		FileInputStream in = new FileInputStream(filepath + fileName);
		props.load(in);
	    }
	    
        if(JSONResponse.length() > 0){
        	responseMapper = new ObjectMapper();
       	    responseMaptype   = responseMapper.getTypeFactory().constructMapType(Map.class, java.lang.String.class,java.lang.Object.class);
       	    JSONresponseData = responseMapper.readValue(JSONResponse, responseMaptype);
            //System.out.println("JSON Response Parser:- "+JSONresponseData);
    	    /*for(int i = 0; i<JSONResponseArr.length(); i++)
    	    {
    	    	JSONObject JSONResp = (JSONObject) JSONResponseArr.get(i);
    	    	String JSONRespArr[] = JSONResp.toString().split(Separator.SEPARATOR_COMMA);
    	    	for(String JSONStrObj : JSONRespArr){
    	    		String JSONStrOb = JSONStrObj.replaceAll("\\{", "").replaceAll("\\}","").replaceAll("\\[","").replaceAll("\\]","");
    	    		JSONresponseData.put(JSONStrOb.split(Separator.SEPARATOR_COLON)[0], JSONStrOb.split(Separator.SEPARATOR_COLON)[1].replace("}", ""));
    	    	}
    	    }*/
    	    
	    String[] propertiesToWrite = valueToWrite.split(Separator.SEPARATOR_HASH);
	    for (String propertyKeyValue : propertiesToWrite) {
	    	if(propertyKeyValue.contains(Separator.SEPARATOR_COLON)){
	    		String[] properties = propertyKeyValue.replaceAll("\"", "").split(Separator.SEPARATOR_COLON, 2);
		    	if(JSONresponseData.containsKey(properties[0]) && null != JSONresponseData.get(properties[0])){
		    		props.setProperty(properties[1], JSONresponseData.get(properties[0]).toString());
		        			System.out.println("Given Attribute present in Rest Response : Key:- "+properties[0]+" : Value :- "+JSONresponseData.get(properties[0]));
		        			attributeFlag=  true;
    			}
	    	    else{
    				System.out.println("Rest response does not contains given Attribute : "+properties[0]);
    				attributeFlag =  false;
	        	}
		    	
		      }
	   }
	     if (attributeFlag){
	    FileWriter writer = new FileWriter(configFile);
	    props.store(writer, "Variable properties");
	    writer.close();
	    return attributeFlag;
	    }
	     else {
	    	 return attributeFlag;
	     }
	}
		return attributeFlag;
    }
	
    /**
     * Count number of attributes in Rest Response
     *
     * 
     * @param Rest Response 
     * @throws IOException 
	 * @throws JSONException 
     */
    public static int countNumberOfAttributes(String JSONResponse,String attrCount) throws IOException, JSONException {
    	
    	/*JSONArray JSONResponseArr = null;
		if(JSONResponse.startsWith("["))
		{
			JSONResponseArr = new   JSONArray(JSONResponse);
		}
		else {
			JSONResponseArr = new   JSONArray("["+JSONResponse+"]");
		}
       HashMap<String, String> JSONresponseData = new HashMap<String, String> ();*/
    	
    	ObjectMapper responseMapper = null;
        org.codehaus.jackson.map.type.MapType responseMaptype = null;
    	Map<String, Object> JSONresponseData = null;
    	
       int attributesCount = 0;
           
        if(JSONResponse.length() > 0){
        	responseMapper = new ObjectMapper();
       	    responseMaptype   = responseMapper.getTypeFactory().constructMapType(Map.class, java.lang.String.class,java.lang.Object.class);
       	    JSONresponseData = responseMapper.readValue(JSONResponse, responseMaptype);
    	 /*   for(int i = 0; i<JSONResponseArr.length(); i++)
    	    {
    	    	JSONObject JSONResp = (JSONObject) JSONResponseArr.get(i);
    	    	String JSONRespArr[] = JSONResp.toString().split(Separator.SEPARATOR_COMMA);
    	    	for(String JSONStrObj : JSONRespArr){
    	    		String JSONStrOb = JSONStrObj.replaceAll("\\{", "").replaceAll("\\}","").replaceAll("\\[","").replaceAll("\\]","");
    	    		JSONresponseData.put(JSONStrOb.split(Separator.SEPARATOR_COLON)[0], JSONStrOb.split(Separator.SEPARATOR_COLON)[1].replace("}", ""));
    	    	}
    	    }*/
     	     
	     if (JSONresponseData.size() > 0){
	    	 attributesCount= JSONresponseData.size(); 
	    	 System.out.println("Number of Attributes Present in Given Rest Response :-"+attributesCount);
  			Debug.traceMessage = Debug.traceMessage  + "<br><br> Number of Attributes Present in Given Rest Response :-"+attributesCount;
				
	    return attributesCount;
	    }
	     else {
	    	 return attributesCount;
	     }
	}
        return attributesCount;
    }
    
	 /* public static void main(String[] args) {
		 
		//Initializing Rest API's URL
		String APIUrl = "http://jsonplaceholder.typicode.com/posts";

			
		//Initializing payload or API body
		String APIBody = "{\"name\":\"Norway\"}"; //e.g.- "{\"key1\":\"value1\",\"key2\":\"value2\"}"
				//String APIBody = "{\"userId\": 61, \"id\": 502, \"title\": \"sunt aut facere repellat provident occaecati excepturi optio reprehenderit\",\"body\": \"quia et \"}";
					
		// Building request using requestSpecBuilder
		RequestSpecBuilder builder = new RequestSpecBuilder();
			
		//Setting API's body
		builder.setBody(APIBody);
			
		//Setting content type as application/json or application/xml
		//builder.setContentType("application/json; charset=UTF-8");
			
		RequestSpecification requestSpec = builder.build();
		
		//Making post request with authentication, leave blank in case there are no credentials- basic("","")
		Response response = given().spec(requestSpec).when().post(APIUrl);
		
		System.out.println("Request submitted successful, response detail as below:");

        System.out.println("StatusCode: " + response.getStatusCode());

        System.out.println("StatusLine: " + response.getStatusLine());

        System.out.println("ResponseString: " + response.asString());

       
		}*/

}



