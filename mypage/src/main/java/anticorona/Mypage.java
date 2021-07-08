package anticorona;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="Mypage_table")
public class Mypage {

        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        private Long applyingId;
        private Long issueId;
        private Long injectionId;
        private String vcName;
        private Long userId;
        private String status;
        private String issueStatus;
        
        public Long getApplyingId() {
            return applyingId;
        }
        public void setApplyingId(Long applyingId) {
            this.applyingId = applyingId;
        }
        public Long getIssueId() {
            return issueId;
        }
        public void setIssueId(Long issueId) {
            this.issueId = issueId;
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
        public String getIssueStatus() {
            return issueStatus;
        }
        public void setIssueStatus(String issueStatus) {
            this.issueStatus = issueStatus;
        }


        
}
