package anticorona.external;

public class Injection {

    private Long injectionId;
    private String vcName;
    private String status;
    private Long userId;

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
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
   
}
