package anticorona;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

@Entity
@Table(name="Injection")
public class Injection {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long injectionId;
    private String vcName;
    private Long userId;
    private String status;
   

    @PostLoad
    public void postLoad(){
        //Set all previous fields to actual values
      //  previousStock = stock;
     //   previousBookQty = bookQty;
    }

    @PostPersist
    public void onPostPersist(){
        Registered registered = new Registered();
        BeanUtils.copyProperties(this, registered);
        registered.publishAfterCommit();
    }

    @PostUpdate
    public void onPostUpdate(){
       
        // 접종 정보  변경
      //  if(previousBookQty != bookQty){
            InjectionModified injectionModified = new InjectionModified();
            BeanUtils.copyProperties(this, injectionModified);
            injectionModified.publishAfterCommit();
       // }
    }


    public Long getInjectionId() {
        return injectionId;
    }

    public void setInjectionId(Long injectionId) {
        this.injectionId = injectionId;
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
   
   
}
