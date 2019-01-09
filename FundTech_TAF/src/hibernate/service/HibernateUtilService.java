package hibernate.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Common service to handle hibernate session factory
 * 
 * @author chetan.aher
 * 
 */
public class HibernateUtilService {

	/**
	 * HibernateUtilService static innstance
	 */
	private static HibernateUtilService instance = null;

	/**
	 * Single ton session factory object
	 */
	private static SessionFactory sessionFactory;

	/**
	 * Hibernate Util service
	 */
	private HibernateUtilService() {
		Properties properties = new Properties();
		String userDir = System.getProperty("user.dir");
		try {
			String hibernatePropertiesFile = userDir + File.separator
					+ "objects" + File.separator
					+ "applicationConfig.properties";
			FileInputStream in = new FileInputStream(hibernatePropertiesFile);

			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		File hibernateConfigFile = new File(userDir + File.separator
				+ "hibernate" + File.separator + "hibernate.cfg.xml");
		sessionFactory = new Configuration().mergeProperties(properties)
				.configure(hibernateConfigFile).buildSessionFactory();
	}

	/**
	 * Method to get single to object of HibernateUtilService
	 * @return
	 */
	public static HibernateUtilService getInstance() {
		if (instance == null) {
			instance = new HibernateUtilService();
		}
		return instance;
	}

	/**
	 * Get session factory
	 * @return
	 */
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}
