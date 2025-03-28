package nl.pcsw.companyinfo.old.additional;

public enum RoleType {
    SUPPLIER("Leverancier"),
    CUSTOMER("Klant");

    private final String description;

    RoleType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
