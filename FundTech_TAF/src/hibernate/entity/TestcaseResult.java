package hibernate.entity;

import java.util.Set;

/**
 * Entity to store testcase result
 * 
 * @author Chetan.Aher
 * 
 */
public class TestcaseResult {
	private Integer id;

	private String testcaseId;

	private String decription;

	private String result;

	private String startTime;

	private String endTime;

	private String duration;

	private TestSuiteResult testSuiteResult;

	private Set<StepResult> stepResults;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTestcaseId() {
		return testcaseId;
	}

	public void setTestcaseId(String testcaseId) {
		this.testcaseId = testcaseId;
	}

	public String getDecription() {
		return decription;
	}

	public void setDecription(String decription) {
		this.decription = decription;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDuration() {
		return duration;
	}

	public TestSuiteResult getTestSuiteResult() {
		return testSuiteResult;
	}

	public void setTestSuiteResult(TestSuiteResult testSuiteResult) {
		this.testSuiteResult = testSuiteResult;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public Set<StepResult> getStepResults() {
		return stepResults;
	}

	public void setStepResults(Set<StepResult> stepResults) {
		this.stepResults = stepResults;
	}

}
