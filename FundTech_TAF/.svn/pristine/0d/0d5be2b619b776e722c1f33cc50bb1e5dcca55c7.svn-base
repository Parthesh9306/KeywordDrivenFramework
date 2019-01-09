package main;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Properties;

import operation.InternalDbOperation;
import operation.ReadObject;
import Utility.ProvisoException;

import com.fundtech.tool.remoteExecution.RemoteServerInterfaceImpl;

import de.root1.simon.Registry;
import de.root1.simon.Simon;
import de.root1.simon.exceptions.NameBindingException;

/**
 * Run this file to start remote server
 * 
 * @author Chetan.Aher
 *
 */
public class Remote {

	/**
	 * Start remote server
	 * 
	 * @param args
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ProvisoException {
		try {
			String currentJarName = new java.io.File(Remote.class.getProtectionDomain().getCodeSource().getLocation()
					  .getPath())
					.getName();
			if (InternalDbOperation.isValidCurrentJar(currentJarName)) {
				RemoteServerInterfaceImpl serverImpl = new RemoteServerInterfaceImpl();
		        Registry registry;
			
				ReadObject object = new ReadObject();
			    Properties allEnvObjects = object
				    .getObjectRepository("environment");
			    String registryPort = allEnvObjects.getProperty("registryPort");
				registry = Simon.createRegistry(Integer.valueOf(registryPort));
		        registry.bind("remote-automation", serverImpl);
		        System.out.println("Server up and running! ");
		        
			} else {
				System.out.println("You either pointing to wrong environment(Prod/Demo) or Proviso Jar is not updated please update it from Proviso Export Section ");
				System.exit(1);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NameBindingException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
