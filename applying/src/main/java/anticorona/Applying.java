package anticorona;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.ResourceSupport;


@Entity
@Table(name="Applying")
public class Applying extends ResourceSupport {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long applyingId;
    private Long injectionId;    
    private String vcName;
    private Long userId;
    private String status;
    private String issueStatus;

    @PrePersist
    public void onPrePersist(){
        this.setIssueStatus("APPLIED");

        System.out.println("##### setStatus###############");

    }

    @PostPersist
    public void onPostPersist() throws Exception {
        if(ApplyingApplication.applicationContext.getBean(anticorona.external.InjectionService.class)
            .checkStatus(this.injectionId)){

                System.out.println("##### checkStatus###############");
                Applied applied = new Applied();
                BeanUtils.copyProperties(this, applied);
                applied.publishAfterCommit();
            }
        else{
            throw new Exception("No Injection Infomation.");
        }

    }

    @PreUpdate
    @PostRemove
    public void onCancelled(){
        if("Applying_CANCELLED".equals(this.status)){
            ApplyingCancelled applyingCancelled = new ApplyingCancelled();
            BeanUtils.copyProperties(this, applyingCancelled);
            applyingCancelled.publishAfterCommit();
        }
    }


    public Long getApplyingId() {
        return applyingId;
    }

    public void setApplyingId(Long applyingId) {
        this.applyingId = applyingId;
    }    
  
    public String getVcName() {
        return vcName;
    }

    public void setVcName(String vcName) {
        this.vcName = vcName;
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

    public Long getInjectionId() {
        return injectionId;
    }

    public void setInjectionId(Long injectionId) {
        this.injectionId = injectionId;
    }

    public String getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(String issueStatus) {
        this.issueStatus = issueStatus;
    }

}
