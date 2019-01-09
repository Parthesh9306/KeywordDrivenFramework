package Utility;

import java.util.ArrayList;

import javax.xml.soap.SOAPException;

import operation.Debug;

import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

/**
 * 
 * @author Nilesh.Kulkarni
 * 
 */
public class ProvisoException extends Exception {

    private static final long serialVersionUID = 1L;

    private String message = null;
    
    public ProvisoException() {
        super();
    }
 
    public ProvisoException(String message) {
        super(message);
        this.message = message;
    }
 
    public ProvisoException(Throwable cause) {
        super(cause);
    }
 
    @Override
    public String toString() {
        return message;
    }
 
    @Override
    public String getMessage() {
        return message;
    }
    
    /**
     * exceptionHandler : Prints custom exception
     * @param driver 
     * 
     * @param Exception
     *            : exception object
     * @return : Returns Status
     * @throws Exception
     */

    public static void exceptionHandler(Exception ex, WebDriver driver) {
        
        try {
        	
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
        	throw (ex);
        } catch (ArrayIndexOutOfBoundsException e) {
            Debug.traceMessage = "Invalid input data / ojbect with at array index : " + e.getMessage();
        } catch (NullPointerException e) {
            Debug.traceMessage = "Null Value found " + e.getMessage();
        } catch (NumberFormatException e) {
            Debug.traceMessage = "Invalid number";
        } catch (MySQLSyntaxErrorException e) {
            Debug.traceMessage = e.getMessage();
        } catch (ElementNotVisibleException e) {
            Debug.traceMessage = e.getMessage();
        } catch (UnhandledAlertException e) {
            Debug.traceMessage = e.getMessage();
        } catch (WebDriverException e) {
            Debug.traceMessage = e.getMessage()
                + "## Message: Please check if browser version is upgraded.If not contact ProvisoSupport@dh.com ";
        } catch (ProvisoException e) {
            Debug.traceMessage = e.getMessage();
        } catch (SOAPException e) {
            Debug.traceMessage = e.getMessage();
        } catch (Exception e) {
            Debug.traceMessage = e.getMessage();
        }
        
        System.out.println("Error Message : " + Debug.traceMessage);
        
        if ("Y".equalsIgnoreCase(Debug.debugFlag)) {
            ex.printStackTrace();
        }
        
    }

}
