package operation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadObject {

	/***
	 * getObjectRepository: get values from the OR
	 * 
	 * @return: p (OR from the Property file)
	 * 
	 * ****/

	Properties p = new Properties();

	public Properties getObjectRepository(String fileName) throws IOException {
		// Read object repository file
		InputStream stream = new FileInputStream(new File(
				System.getProperty("user.dir") + "\\objects\\" + fileName
						+ ".properties"));
		// load all objects
		p.load(stream);
		return p;
	}

}
