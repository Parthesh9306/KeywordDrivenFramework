package com.fundtech.tool.remoteExecution;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.util.Properties;

import operation.ReadObject;

/**
 * Remote execution of test automation
 * 
 * @author Chetan.Aher
 * 
 */
public class RemoteExecutionServer {

    /**
     * Start server from remote batch file
     * 
     * @return
     */
    public boolean startServer() {
	try {
	    Policy.setPolicy(new Policy() {
		public PermissionCollection getPermissions(CodeSource codesource) {
		    Permissions perms = new Permissions();
		    perms.add(new AllPermission());
		    return (perms);
		}

		public void refresh() {
		}
	    });

	    ReadObject object = new ReadObject();
	    Properties allEnvObjects = object
		    .getObjectRepository("environment");
	    String registryPort = allEnvObjects.getProperty("registryPort");

	    String rmiUrl = "rmi://" + allEnvObjects.getProperty("rmiUrl");
	    System.out.println("Starting Remote Execution Server on " + rmiUrl + ".....");
	    LocateRegistry.createRegistry(Integer.valueOf(registryPort));
	    System.setSecurityManager(new RMISecurityManager());
	    Runtime.getRuntime().exec("rmiregistry " + registryPort);
	    RemoteExecution remoteExecution = new RemoteExecution();
	    Naming.rebind(rmiUrl, remoteExecution);
	    System.out.println("Remote Execution Server is ready on " + rmiUrl + "!");
	} catch (Exception e) {
	    System.out.println("Exception occur while starting server!");
	    e.printStackTrace();
	}
	return false;
    }
}
