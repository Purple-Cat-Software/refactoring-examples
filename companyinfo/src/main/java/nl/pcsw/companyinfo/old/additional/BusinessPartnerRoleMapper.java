package nl.pcsw.companyinfo.old.additional;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

public class BusinessPartnerRoleMapper {

    private static final String BUSINESS_PARTNER_ROLE = "BusinessPartnerRole";

    public JsonArray add(CustomerTypeSelected customerTypeSelected) {
        JsonArray mappedRoles = new JsonArray();
        if (customerTypeSelected.customer()) {
            final List<RoleDTO> roles = getCustomerRoles();
            roles.forEach(role -> mappedRoles.add(create(role.name())));
        }

        if (customerTypeSelected.supplier()) {
            final List<RoleDTO> roles = getSupplierRoles();
            roles.forEach(role -> mappedRoles.add(create(role.name())));
        }
        return mappedRoles;
    }

    private JsonObject create(String roleName) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(BUSINESS_PARTNER_ROLE, roleName);
        return jsonObject;
    }

    private List<RoleDTO> getCustomerRoles() {
        return List.of(
            new RoleDTO(1, "role_1", RoleType.CUSTOMER),
            new RoleDTO(2, "role_2", RoleType.CUSTOMER)
        );
    }

    private List<RoleDTO> getSupplierRoles() {
        return List.of(
            new RoleDTO(3, "role_3", RoleType.SUPPLIER),
            new RoleDTO(4, "role_4", RoleType.SUPPLIER)
        );
    }

}
