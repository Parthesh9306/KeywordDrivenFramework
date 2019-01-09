package com.fundtech.tool.remoteExecution;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Properties;

import operation.ReadObject;
import de.root1.simon.Registry;
import de.root1.simon.Simon;
import de.root1.simon.exceptions.NameBindingException;

/**
 * Added for testing purpose of simon library
 * remote execution
 * @TODO remove after stability
 * @author chetan.aher
 *
 */
public class Server {

    public static void main(String[] args)
            throws UnknownHostException, IOException, NameBindingException {
    	ReadObject object = new ReadObject();
	    Properties allEnvObjects = object
		    .getObjectRepository("environment");
	    String registryPort = allEnvObjects.getProperty("registryPort");
	    
        // create the serverobject
        RemoteServerInterfaceImpl serverImpl = new RemoteServerInterfaceImpl();

        // create the server's registry ...
        Registry registry = Simon.createRegistry(Integer.valueOf(registryPort));
        //registry.start();
        // ... where we can bind the serverobject to
        registry.bind("remote-automation", serverImpl);

        System.out.println("Server up and running!");

        // some mechanism to shutdown the server should be placed here
        // this should include the following command:
        // registry.unbind("server");
        // registry.stop();
    }
}