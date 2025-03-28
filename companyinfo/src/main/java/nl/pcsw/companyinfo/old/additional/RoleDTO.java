package nl.pcsw.companyinfo.old.additional;

public record RoleDTO(int id, String name, RoleType roleType) {

    public static RoleDTO convert(Role role) {
        return new RoleDTO(role.getId(), role.getName(), role.getType());
    }

}
