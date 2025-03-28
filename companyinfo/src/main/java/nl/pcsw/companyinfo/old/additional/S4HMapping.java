package nl.pcsw.companyinfo.old.additional;

public class S4HMapping {

    private Long id;
    private String ciField;
    private String s4hField;
    private String s4hFieldType;
    private String s4hFieldLength;
    private boolean hardcoded;
    private String service;

    public String gets4hFieldType() {
        return s4hFieldType;
    }

    public void sets4hFieldType(String s4hFieldType) {
        this.s4hFieldType = s4hFieldType;
    }

    public String gets4hFieldLength() {
        return s4hFieldLength;
    }

    public void sets4hFieldLength(String s4hFieldLength) {
        this.s4hFieldLength = s4hFieldLength;
    }

    public boolean isHardcoded() {
        return hardcoded;
    }

    public void setHardcoded(boolean hardcoded) {
        this.hardcoded = hardcoded;
    }

    public String gets4hService() {
        return service;
    }

    public void sets4hService(String s4hService) {
        this.service = s4hService;
    }

    public String getCiField() {
        return ciField;
    }

    public void setCiField(String ciField) {
        this.ciField = ciField;
    }

    public String gets4hField() {
        return s4hField;
    }

    public void sets4hField(String s4hField) {
        this.s4hField = s4hField;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
