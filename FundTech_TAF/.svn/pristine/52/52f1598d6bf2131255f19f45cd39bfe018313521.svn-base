package com.fundtech.tool.remoteExecution;
import operation.Debug;
import operation.InternalDbOperation;

import org.testng.TestNG;

import testScript.HybridExecuteTest;
import de.root1.simon.annotation.SimonRemote;

/**
 * Implementation of RemoteServerInterface for remove execution of testcase
 * using SIMON library
 *  
 * @author chetan.aher
 *
 */
@SimonRemote(value = {RemoteServerInterface.class}) 
public class RemoteServerInterfaceImpl implements RemoteServerInterface {

   private static final long serialVersionUID = 1L;

   @Override
	public void execute(int executionMasterId) {
	   InternalDbOperation.closeConnectionOne();
	   InternalDbOperation.closeConnectionTwo(false);
	   TestNG testng = new TestNG();
		Class[] classes = new Class[] { HybridExecuteTest.class };
		testng.setTestClasses(classes);
		Debug.executionId = executionMasterId;
		testng.run();
	}
}