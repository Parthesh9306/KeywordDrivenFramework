package hibernate.enums;

/**
 * Execution master status
 * 
 * @author Chetan.Aher
 * 
 */
public enum Result {
    PASS("Pass"), FAIL("Fail"), IN_PROGRESS("In Progress");

    private String displayName;

    Result(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
    
}