package hibernate.entity;

/**
 * Entity to store step result
 * 
 * @author Chetan.Aher
 * 
 */
public class StepResult {
	private Integer id;

	private String stepDescription;

	private String keyword;

	private String object;

	private String duration;

	private String result;

	private String testData;

	private TestcaseResult testcaseResult;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStepDescription() {
		return stepDescription;
	}

	public void setStepDescription(String stepDescription) {
		this.stepDescription = stepDescription;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getDuration() {
		return duration;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getTestData() {
		return testData;
	}

	public void setTestData(String testData) {
		this.testData = testData;
	}

	public TestcaseResult getTestcaseResult() {
		return testcaseResult;
	}

	public void setTestcaseResult(TestcaseResult testcaseResult) {
		this.testcaseResult = testcaseResult;
	}

}
