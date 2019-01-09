package Utility;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import operation.Debug;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Reporter;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.screentaker.ViewportPastingStrategy;

public class ScreenshotUtility  {
	private static int Count = 0;

	/***
	 * ScreenShot: Code to get screen resolution, Get the default toolkit
	 * Toolkit toolkit = Toolkit.getDefaultToolkit(); Get the current screen
	 * size Dimension scrnsize = toolkit.getScreenSize(); Print the screen size
	 * System.out.println ("Screen size : " + scrnsize);
	 * @throws IOException 
	 * 
	 * 
	 * **/

	public static String ScreenShot(WebDriver driver, String imagePath) {
		String NewFileNamePath = "";
		
		try {
			
			// Get the dir path
			File directory = new File(".");
			// System.out.println(directory.getCanonicalPath());
			// get current date time with Date() to create unique file name
			DateFormat dateFormat = new SimpleDateFormat(
					"dd_MMM_yyyy__hh_mm_ssaa");
			// get current date time with Date()
			Date date = new Date();
			// System.out.println(dateFormat.format(date));
			// To identify the system
			InetAddress ownIP;
			
				ownIP = InetAddress.getLocalHost();
			
			// System.out.println("IP of my system is := "+ownIP.getHostAddress());

			NewFileNamePath = imagePath
					+ dateFormat.format(date) + "_" + ownIP.getHostAddress()
					+ ".png";
			System.out.println(NewFileNamePath);

			// Capture the screen shot of the area of the screen defined by the
			// rectangle
		/*	Robot robot = new Robot();
			BufferedImage bi = robot.createScreenCapture(new Rectangle(1280,
					1024));
			ImageIO.write(bi, "png", new File(NewFileNamePath));
			Count++;// Assign each screen shot a number
			NewFileNamePath = "ScreenShot" + Count + "";
			// Place the reference in TestNG web report
			 * 
*/			// Uncommented for CHROME / Firefox changes
        if (driver != null &&  null != ((RemoteWebDriver) driver).getSessionId()) {
            if (Debug.BrowserType.equalsIgnoreCase("CHROME")) {
                final Screenshot screenshot = new AShot().shootingStrategy(
                    new ViewportPastingStrategy(500)).takeScreenshot(driver);
                final BufferedImage image = screenshot.getImage();
                ImageIO.write(image, "PNG", new File(NewFileNamePath));
            } else {
                File scrFile = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(scrFile, new File(NewFileNamePath));

            }
            Reporter.log(NewFileNamePath);
        }
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
			///Screenshot for CHROME
        return NewFileNamePath;
		
	}
	
	public static String ScreenShot(String imageFolder) throws IOException, AWTException {
		String NewFileNamePath = "";
		
			
			
			// Get the dir path
			File directory = new File(".");
			// System.out.println(directory.getCanonicalPath());
			// get current date time with Date() to create unique file name
			DateFormat dateFormat = new SimpleDateFormat(
					"dd_MMM_yyyy__hh_mm_ssaa");
			// get current date time with Date()
			Date date = new Date();
			// System.out.println(dateFormat.format(date));
			// To identify the system
			InetAddress ownIP = InetAddress.getLocalHost();
			// System.out.println("IP of my system is := "+ownIP.getHostAddress());

			NewFileNamePath = imageFolder
					+ dateFormat.format(date) + "_" + ownIP.getHostAddress()
					+ ".png";
			System.out.println(NewFileNamePath);

			// Capture the screen shot of the area of the screen defined by the
			// rectangle
			Robot robot = new Robot();
			BufferedImage bi = robot.createScreenCapture(new Rectangle(1280,
					1024));
			ImageIO.write(bi, "png", new File(NewFileNamePath));
			Count++;// Assign each screen shot a number
			//NewFileNamePath = "ScreenShot" + Count + "";
			// Place the reference in TestNG web report
			/*File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(NewFileNamePath));*/
	      

			Reporter.log(NewFileNamePath);
			
		
		return NewFileNamePath;
		
	}
}
