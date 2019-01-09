package com.fundtech.tool.remoteExecution;

/**
 * Remove execution interface for executing 
 * testcase remotely simon library 
 * @author chetan.aher
 *
 */
public interface RemoteServerInterface {
	/**
	 * Execute testcase by selecting execution master id
	 * 
	 * @param executionMasterId
	 */
	public void execute(int executionMasterId);
}
