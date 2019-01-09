package com.fundtech.tool.remoteExecution;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import operation.Debug;
import operation.InternalDbOperation;

import org.testng.TestNG;

import testScript.HybridExecuteTest;

public class RemoteExecution extends UnicastRemoteObject implements
		RemoteExecutionInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RemoteExecution() throws RemoteException {
	}
	@Deprecated
	@Override
	public int startExecution(Integer executionMasterId) throws RemoteException {
		
		TestNG testng = new TestNG();
		Class[] classes = new Class[] { HybridExecuteTest.class };
		testng.setTestClasses(classes);
		Debug.executionId = executionMasterId;
		testng.run();
		
		return 0;
	}

}
