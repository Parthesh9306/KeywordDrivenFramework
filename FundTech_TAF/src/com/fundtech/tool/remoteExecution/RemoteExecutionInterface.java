package com.fundtech.tool.remoteExecution;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteExecutionInterface extends Remote {
	public int startExecution(Integer executionMasterId) throws RemoteException;
}
