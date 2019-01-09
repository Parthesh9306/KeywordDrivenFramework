package hibernate.entity;

import java.util.Set;

/**
 * Entity to store test suite result
 * 
 * @author Chetan.Aher
 * 
 */
public class TestSuiteResult {
    private Integer id;

    private String name;

    private String description;

    private String result;

    private String duration;

    private ExecutionMaster executionMaster;

    private Set<TestcaseResult> testcaseResults;

    private Integer passCount;

    private Integer failCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Set<TestcaseResult> getTestcaseResults() {
        return testcaseResults;
    }

    public void setTestcaseResults(Set<TestcaseResult> testcaseResults) {
        this.testcaseResults = testcaseResults;
    }

    public Integer getPassCount() {
        return passCount;
    }

    public void setPassCount(Integer passCount) {
        this.passCount = passCount;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

    public ExecutionMaster getExecutionMaster() {
        return executionMaster;
    }

    public void setExecutionMaster(ExecutionMaster executionMaster) {
        this.executionMaster = executionMaster;
    }

}
