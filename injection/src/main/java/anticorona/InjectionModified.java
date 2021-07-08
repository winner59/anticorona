package anticorona;

public class InjectionModified extends AbstractEvent {

    private Long  injectionId;
    private String vcName;
    private Long  userID;
    private String status;

    public InjectionModified(){
        super();
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

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

   
}
