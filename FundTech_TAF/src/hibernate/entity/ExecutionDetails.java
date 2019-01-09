package hibernate.entity;

import java.sql.Timestamp;
import java.util.Date;

public class ExecutionDetails {

	private Integer id;
	
	private Date createdAt;
	
	private Integer executionMasterId;
	
	private Integer testCaseId;
	
	private ExecutionMaster executionMaster;

	public ExecutionMaster getExecutionMaster() {
		return executionMaster;
	}

	public void setExecutionMaster(ExecutionMaster executionMaster) {
		this.executionMaster = executionMaster;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Integer getExecutionMasterId() {
		return executionMasterId;
	}

	public void setExecutionMasterId(Integer executionMasterId) {
		this.executionMasterId = executionMasterId;
	}

	public Integer getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(Integer testCaseId) {
		this.testCaseId = testCaseId;
	}
	
}
