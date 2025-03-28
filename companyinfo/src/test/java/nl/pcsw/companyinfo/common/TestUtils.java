package nl.pcsw.companyinfo.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import nl.pcsw.companyinfo.old.additional.CustomerTypeSelected;
import nl.pcsw.companyinfo.old.additional.S4HMapping;
import org.junit.platform.commons.util.StringUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void compareContentEquals(String expected, String result) {
        try {
            assertEquals(objectMapper.readTree(expected), objectMapper.readTree(result));
        } catch (JsonProcessingException ignore) {
        }
    }

    public static JsonArray createJsonArrayWithCustomerType(CustomerTypeSelected customerTypeSelected) {
        JsonArray mappedRoles = new JsonArray();
        if (customerTypeSelected.customer()) {
            mappedRoles.add(create("FLCU00"));
            mappedRoles.add(create("FLCU01"));
        }
        if (customerTypeSelected.supplier()) {
            mappedRoles.add(create("FLVN01"));
            mappedRoles.add(create("FLVN00"));
        }
        return mappedRoles;
    }

    private static JsonObject create(String roleName) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("BusinessPartnerRole", roleName);
        return jsonObject;
    }

    public static List<S4HMapping> createMappingListForS4H() {
        return List.of(
            createS4Hmapping(1, "trade_name_full", 0, "BusinessPartnerFullName", "81", "String", "extended"),
            createS4Hmapping(29, "domain_name", 0, "to_BusinessPartnerAddress.to_URLAddress.WebsiteURL", "",
                             "String", "extended"
            ), createS4Hmapping(6, "01", 1, "LegalForm", "", "String", "general"),
            createS4Hmapping(25, "dossier_number", 0, "to_BuPaIdentification.BPIdentificationNumber", "", "",
                             "extended"
            ),
            createS4Hmapping(26, "BUP005", 1, "to_BuPaIdentification.BPIdentificationType", "", "", "general"),
            createS4Hmapping(15, "establishment_address.formatted.street", 0,
                             "to_BusinessPartnerAddress.StreetName", "", "String", "extended"
            ),
            createS4Hmapping(12, "establishment_address.formatted.house_number", 0,
                             "to_BusinessPartnerAddress.HouseNumber", "", "String", "extended"
            ),
            createS4Hmapping(501, "trade_names.item.0", 0, "ZZ1_Handelsnaam1_bus_bus", "40", "String",
                             "extended"
            ),
            createS4Hmapping(17, "telephone_number", 0, "to_BusinessPartnerAddress.to_PhoneNumber.PhoneNumber",
                             "", "String", "extended"
            ),
            createS4Hmapping(11, "0003", 1, "FormOfAddress", "", "String", "general"),
            createS4Hmapping(50, "legal_name", 0, "SearchTerm1", "20", "String", "general"),
            createS4Hmapping(10, "NL", 1, "to_BusinessPartnerAddress.Country", "", "String", "general"),
            createS4Hmapping(2, "trade_name_45", 0, "BusinessPartnerName", "81", "String", "extended"),
            createS4Hmapping(28, "discontinuation_date", 0, "OrganizationLiquidationDate", "", "Date",
                             "extended"
            ),
            createS4Hmapping(502, "establishment_number", 0, "ZZ1_Locationnumber_bus_bus", "40", "String",
                             "extended"
            ), createS4Hmapping(503, "sbi_collection.company_info.item.0.sbi_code", 0,
                                "ZZ1_Prim_SBIcodeCI_bus_bus", "40", "String", "extended"
            ),
            createS4Hmapping(504, "annual_financial_statement_summary.assets.amount", 0,
                             "ZZ1_fin_assets_bus_bus", "40", "Decimal", "extended"
            ),
            createS4Hmapping(505, "annual_financial_statement_summary.profit.amount", 0,
                             "ZZ1_fin_profit_bus_bus", "40", "Decimal", "extended"
            ),
            createS4Hmapping(506, "annual_financial_statement_summary.turnover.amount", 0,
                             "ZZ1_fin_turnover_bus_bus", "50", "Decimal", "extended"
            ),
            createS4Hmapping(23, "NL0", 1, "to_BusinessPartnerTax.BPTaxType", "", "String", "general"),
            createS4Hmapping(30, "establishment_date", 0, "OrganizationFoundationDate", "", "Date", "extended"),
            createS4Hmapping(3, "trade_name_45", 0, "OrganizationBPName1", "40", "String", "extended"),
            createS4Hmapping(24, "BUP002", 1, "to_BuPaIdentification.BPIdentificationType", "", "", "general"),
            createS4Hmapping(27, "establishment_number", 0, "to_BuPaIdentification.BPIdentificationNumber", "",
                             "", "extended"
            ),
            createS4Hmapping(4, "2", 1, "BusinessPartnerCategory", "", "String", "general"),
            createS4Hmapping(14, "establishment_address.formatted.postcode", 0,
                             "to_BusinessPartnerAddress.PostalCode", "", "String", "extended"
            ),
            createS4Hmapping(9, "establishment_address.formatted.city", 0, "to_BusinessPartnerAddress.CityName",
                             "", "String", "extended"
            ),
            createS4Hmapping(5, "BP02", 1, "BusinessPartnerGrouping", "", "String", "general"),
            createS4Hmapping(507, "authorized_share_capital", 0, "ZZ1_auth_sharecap_bus_bus", null, "Decimal",
                             "extended"
            ),
            createS4Hmapping(511, "indication_non_mailing", 0, "ZZ1_non_mail_bus_bus", null, "Boolean",
                             "extended"
            ),
            createS4Hmapping(514, "indication_import", 0, "ZZ1_import_bus_bus", null, "Boolean", "extended"),
            createS4Hmapping(510, "indication_organisation_code", 0, "ZZ1_org_code_bus_bus", null, "String",
                             "extended"
            ),
            createS4Hmapping(509, "indication_economically_active", 0, "ZZ1_econ_active_bus_bus", null,
                             "Boolean", "extended"
            ),
            createS4Hmapping(508, "indication_bankruptcy", 0, "ZZ1_bankruptcy_bus_bus", null, "Boolean",
                             "extended"
            ),
            createS4Hmapping(512, "indication_dip", 0, "ZZ1_dip_bus_bus", null, "Boolean", "extended"),
            createS4Hmapping(513, "indication_export", 0, "ZZ1_export_bus_bus", null, "Boolean", "extended"),
            createS4Hmapping(515, "issued_share_capital", 0, "ZZ1_share_cap_bus_bus", null, "Decimal",
                             "extended"
            ),
            createS4Hmapping(516, "legal_name", 0, "ZZ1_legal_name_bus_bus", "81", "String", "extended"),
            createS4Hmapping(517, "paid_up_share_capital", 0, "ZZ1_paid_up_capi_bus_bus", null, "Decimal",
                             "extended"
            ),
            createS4Hmapping(518, "personnel", 0, "ZZ1_personnel_bus_bus", null, "String", "extended"),
            createS4Hmapping(519, "personnel_fulltime", 0, "ZZ1_pers_fulltime_bus_bus", null, "String",
                             "extended"
            ),
            createS4Hmapping(520, "sbi_collection.company_info.item.0.sbi_code", 0, "ZZ1_prim_sbicode_bus_bus",
                             null, "String", "extended"
            ),
            createS4Hmapping(521, "rsin_number", 0, "ZZ1_rsin_number_bus_bus", null, "String", "extended"),
            createS4Hmapping(522, "secondary_sbi_code1_text", 0, "ZZ1_secsbicode_text_bu_bus", null, "String",
                             "extended"
            ),
            createS4Hmapping(523, "structure.ultimate_parent", 0, "ZZ1_ultimateparent_cus_bus", "20", "String",
                             "extended"
            ),
            createS4Hmapping(22, "VATJsonModel.vat_number", 0, "to_BusinessPartnerTax.BPTaxNumber", "",
                             "String", "extended"
            ),
            createS4Hmapping(51, "trade_names", 0, "SearchTerm2", "20", "String", "extended"),
            createS4Hmapping(13, "NL", 1, "to_BusinessPartnerAddress.Language", "", "String", "general")
        );
    }

    private static S4HMapping createS4Hmapping(long id, String ciField, int hardcoded, String s4hField,
        String s4hFieldLength, String s4hFieldType, String service) {
        S4HMapping s4HMapping = new S4HMapping();
        s4HMapping.setId(id);
        s4HMapping.setCiField(ciField);
        s4HMapping.setHardcoded(hardcoded != 0);
        s4HMapping.sets4hField(s4hField);
        s4HMapping.sets4hFieldLength(StringUtils.isNotBlank(s4hFieldLength) ? s4hFieldLength : "");
        s4HMapping.sets4hFieldType(s4hFieldType);
        s4HMapping.sets4hService(service);
        return s4HMapping;
    }
}
