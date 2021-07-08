package anticorona;

public class Completed extends AbstractEvent {

    private Long issueId;
    private Long applyingId​;
    private Long userId​;
    private Long injectionId;
    private String status;
    private String issueStatus;
    
    public Long getIssueId() {
        return issueId;
    }
    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }
    public Long getApplyingId​() {
        return applyingId​;
    }
    public void setApplyingId​(Long applyingId​) {
        this.applyingId​ = applyingId​;
    }
    public Long getUserId​() {
        return userId​;
    }
    public void setUserId​(Long userId​) {
        this.userId​ = userId​;
    }
    public Long getInjectionId() {
        return injectionId;
    }
    public void setInjectionId(Long injectionId) {
        this.injectionId = injectionId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getIssueStatus() {
        return issueStatus;
    }
    public void setIssueStatus(String issueStatus) {
        this.issueStatus = issueStatus;
    }

    
}