package nl.pcsw.companyinfo.old.additional;

import com.mybrand.cloud.companyinfoapp.persistence.entity.businesspartner.BusinessPartner;
import com.mybrand.cloud.companyinfoapp.persistence.entity.businesspartner.InternationalBusinessPartner;
import com.mybrand.cloud.companyinfoapp.service.dto.RoleDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "CI_ROLE")
public class Role {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private int id;
    @Column(name = "NAME")
    private String name;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "TYPE")
    private RoleType type;

    @ManyToMany(mappedBy = "roles")
    private Set<BusinessPartner> customers;

    @ManyToMany(mappedBy = "roles")
    private Set<InternationalBusinessPartner> internationalBusinessPartners;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RoleType getType() {
        return type;
    }

    public void setType(RoleType type) {
        this.type = type;
    }

    public Set<BusinessPartner> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<BusinessPartner> customers) {
        this.customers = customers;
    }

    public Set<InternationalBusinessPartner> getInternationalBusinessPartners() {
        return internationalBusinessPartners;
    }

    public void setInternationalBusinessPartners(Set<InternationalBusinessPartner> internationalBusinessPartners) {
        this.internationalBusinessPartners = internationalBusinessPartners;
    }

    public boolean hasValue(RoleType roleType) {
        return getType().equals(roleType);
    }

    public static Role create(String name, RoleType type) {
        Role role = new Role();
        role.setName(name);
        role.setType(type);
        return role;
    }

    public static Role convert(RoleDTO roleDTO) {
        Role role = create(roleDTO.name(), roleDTO.roleType());
        role.setId(roleDTO.id());
        return role;
    }

}
