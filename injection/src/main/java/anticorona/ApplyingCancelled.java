package anticorona;

public class ApplyingCancelled extends AbstractEvent {

    private Long applyingId;
    private Long injectionId;
    private Long userId;
    private String status;
  

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
   
}