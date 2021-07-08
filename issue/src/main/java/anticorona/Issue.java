package anticorona;

import javax.persistence.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

@Entity
@Table(name="Issue")
public class Issue {

    @Transient
    Logger logger = LoggerFactory.getLogger(Issue.class);

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long issueId;
    private Long applyingId;
    private Long injectionId;  
    private Long userId;
    private String status;
    private String issueStatus;

    @PreUpdate
    public void onPreUpdate(){
        
        logger.info("ISSUE on PreUpdate Executed");
        // 발급 완료 처리 //
        Completed completed = new Completed();
        BeanUtils.copyProperties(this, completed);
        completed.setIssueStatus("ISSUE_COMPLETED");
        completed.publishAfterCommit();      

    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public Long getApplyingId() {
        return applyingId;
    }

    public void setApplyingId(Long applyingId) {
        this.applyingId = applyingId;
    }

    public Long getInjectionId() {
        return injectionId;
    }

    public void setInjectionId(Long injectionId) {
        this.injectionId = injectionId;
    }    
   
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
