package operation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import Utility.GlobalLib;
import constants.Separator;
/**
 * Perform xml operation 
 * 
 * @author Chetan.Aher
 *
 */
public class XMLOperation {
    /**
     * Get xml tag value from xpath by providing xml content as string
     * 
     * @param xmlContent
     * @param xpath
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws XPathExpressionException
     */
    public String getXMLTagFromXpath(String xmlContent, String xpath)
	    throws ParserConfigurationException, SAXException, IOException,
	    XPathExpressionException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder builder = builderFactory.newDocumentBuilder();
    	Document xmlDocument = builder.parse(new InputSource(new StringReader(xmlContent)));
    	XPath xPath = XPathFactory.newInstance().newXPath();
    
    	return xPath.compile(xpath).evaluate(xmlDocument);
    }

    /**
     * Update xml by providing xpath and value
     * 
     * @param filePath
     * @param xpathAndUpdateText xpath and text to set
     * @throws ParserConfigurationException
     * @throws XPathExpressionException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerException
     */
    public void updateXmlByXpath(String filePath, String xpathAndUpdateText) throws ParserConfigurationException, XPathExpressionException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document document = builder.parse(new File(filePath));
        String[] xpathAndTextArray = xpathAndUpdateText.split(Separator.SEPARATOR_DOUBLE_EXCLAMATION);
        
        for (String xpathAndValueToSet : xpathAndTextArray) {
            String[] xpathAndValueToSetArray = xpathAndValueToSet.split(Separator.SEPARATOR_DOUBLE_EQUALTO);
            if (xpathAndValueToSetArray.length == 3) {
             // Get a node using XPath
                XPath xPath = XPathFactory.newInstance().newXPath();
                Node node = (Node) xPath.evaluate(xpathAndValueToSetArray[1], document, XPathConstants.NODE);
                if (xpathAndValueToSetArray[0].equalsIgnoreCase("NODE")) {
                    node.appendChild(document.createElement(xpathAndValueToSetArray[2]));
                } else if (xpathAndValueToSetArray[0].equalsIgnoreCase("TEXT")){
                    node.setTextContent(xpathAndValueToSetArray[2]);
                } else {
                    System.out.println("Invalid type to update xml");
                }
                
                // Write changes to a file
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.transform(new DOMSource(document), new StreamResult(new File(filePath)));
            } else {
                System.out.println("Invalid xapth and value provided to update");
            }
        }
    }
    
    /**
     * Get xpath value from xml file or content
     * 
     * @param expression
     * @param fileOrXml
     * @return
     * @throws Exception 
     */
    public String getXpathValue(String expression, String fileOrXml) throws Exception {
        String xmlPath = System.getProperty("user.dir") + File.separator + "xml" + File.separator;
          FileInputStream fileInputStream;
          String xpathValue = "";
          
          if (fileOrXml.startsWith("File_")) {
              String[] parts = fileOrXml.split(Separator.SEPARATOR_UNDERSCORE, 2);
              fileInputStream = new FileInputStream(new File(xmlPath + parts[1]));
          } else {
              String xmlContent = GlobalLib.getPropertyOrValue(fileOrXml);
              File file = new File(xmlPath + "temp.xml");
              
              if (!file.exists()) {
                  file.createNewFile();
              }
              
              fileInputStream = new FileInputStream(file);
              FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
              BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
              bufferWriter.write(xmlContent);
              bufferWriter.close();
          }
          
         
          Reader reader = new InputStreamReader(fileInputStream,"UTF-8");
          InputSource is = new InputSource(reader);
          is.setEncoding("UTF-8");

          
          DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
          DocumentBuilder builder =  builderFactory.newDocumentBuilder();
          Document xmlDocument = builder.parse(is);
          XPath xPath =  XPathFactory.newInstance().newXPath();
          //Node node = null;
          /*System.out.println("Xpath Expression : " + expression);
          XPathExpression expr = xPath.compile(expression);
          Object result = expr.evaluate(xmlDocument, XPathConstants.NODESET);
          Node node = (Node) result;
          NodeList nodes = (NodeList) result;
          for (int i = 0; i < nodes.getLength(); i++) {
        	     System.out.println(nodes.item(i).getNodeValue());
        	}*/
          ///xpathValue = (String)xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
          //xpathValue = xPath.compile(expression).evaluate(xmlDocument);
          //XPathExpression xp = XPathFactory.newInstance().newXPath().compile("//@Ccy='AUD'");
          //XPathExpression xp = XPathFactory.newInstance().newXPath().compile(expression);
          //boolean obj =(boolean) xp.evaluate(xmlDocument, XPathConstants.BOOLEAN);
          //if(obj){
        	  //xpathValue =  xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODE);
        	  //node = (Node) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.STRING);
        	  xpathValue =  xPath.compile(expression).evaluate(xmlDocument);
          //}
         
          System.out.println("Xpath value : " + xpathValue);
              
          return xpathValue;
    }

}
