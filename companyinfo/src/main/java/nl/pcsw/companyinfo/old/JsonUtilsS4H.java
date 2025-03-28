package nl.pcsw.companyinfo.old;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import nl.pcsw.companyinfo.old.additional.BooleanMappingDTO;
import nl.pcsw.companyinfo.old.additional.BusinessPartnerRoleMapper;
import nl.pcsw.companyinfo.old.additional.CustomerTypeSelected;
import nl.pcsw.companyinfo.old.additional.MappingDTO;
import nl.pcsw.companyinfo.old.additional.S4HMapping;
import nl.pcsw.companyinfo.old.additional.S4HMappingRepository;
import nl.pcsw.companyinfo.old.additional.StringUtils;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class JsonUtilsS4H {

    private static final String DATE = "/Date(";
    private static final String DECIMAL = "Decimal";
    private static final String BOOLEAN = "Boolean";

    private static final String POSTAL_CODE = "to_BusinessPartnerAddress.PostalCode";
    private static final String PHONE_NUMBER = "to_BusinessPartnerAddress.to_PhoneNumber.PhoneNumber";
    private static final String YY1_DEEPLINK_BUS = "YY1_Deeplink_bus";

    private static final String TO_BU_PA_IDENTIFICATION = "to_BuPaIdentification";
    private static final String TO_BUSINESS_PARTNER_TAX_BP_TAX_NUMBER = "to_BusinessPartnerTax.BPTaxNumber";

    private final S4HMappingRepository s4HMappingRepository;
    private final BusinessPartnerRoleMapper businessPartnerRoleMapper;

    public JsonUtilsS4H(S4HMappingRepository s4HMappingRepository,
        BusinessPartnerRoleMapper businessPartnerRoleMapper) {
        this.s4HMappingRepository = s4HMappingRepository;
        this.businessPartnerRoleMapper = businessPartnerRoleMapper;
    }

    public JsonObject parseJSONForCreate(JsonObject input, CustomerTypeSelected customerTypeSelected) {
        JsonObject id = new JsonObject();
        if (input.has(TO_BU_PA_IDENTIFICATION)) {
            id.add(TO_BU_PA_IDENTIFICATION, input.getAsJsonArray(TO_BU_PA_IDENTIFICATION));
            input.remove(TO_BU_PA_IDENTIFICATION);
        }
        List<BooleanMappingDTO> customBoolean = new ArrayList<>();
        List<MappingDTO> businessPartner = new ArrayList<>();
        List<MappingDTO> businessPartnerAddress = new ArrayList<>();
        List<MappingDTO> businessPartnerAddressPhone = new ArrayList<>();
        List<MappingDTO> businessPartnerAddressUrl = new ArrayList<>();
        List<MappingDTO> buPaIndustry = new ArrayList<>();
        List<MappingDTO> businessPartnerTax = new ArrayList<>();
        HashMap<String, Object> mapping = new Gson().fromJson(input, HashMap.class);
        for (Map.Entry<String, Object> entry : mapping.entrySet()) {
            String key = entry.getKey();
            Object valueObject = entry.getValue();
            if (Objects.nonNull(valueObject) && valueObject.getClass() == String.class) {
                String value = (String) entry.getValue();
                if (key.contains("to_BusinessPartnerAddress.to_PhoneNumber")) {
                    businessPartnerAddressPhone.add(new MappingDTO(key, value));
                } else if (key.contains("to_BusinessPartnerAddress.to_URLAddress")) {
                    businessPartnerAddressUrl.add(new MappingDTO(key, value));
                } else if (key.contains("to_BusinessPartnerAddress")) {
                    businessPartnerAddress.add(new MappingDTO(key, value));
                } else if (key.contains("to_BuPaIndustry")) {
                    buPaIndustry.add(new MappingDTO(key, value));
                } else if (key.contains("to_BusinessPartnerTax")) {
                    businessPartnerTax.add(new MappingDTO(key, value));
                } else {
                    businessPartner.add(new MappingDTO(key, value));
                }
            } else if (Objects.nonNull(valueObject)) {
                customBoolean.add(new BooleanMappingDTO(key, (Boolean) valueObject));
            }
        }
        JsonObject result = new JsonObject();
        addBusinessPartners(businessPartner, result);
        addBooleans(customBoolean, result);
        addNestedBusinessPartnerAddress(result, businessPartnerAddress, businessPartnerAddressPhone,
                                        "to_BusinessPartnerAddress.to_PhoneNumber.", "to_PhoneNumber"
        );
        addNestedBusinessPartnerAddress(result, businessPartnerAddress, businessPartnerAddressUrl,
                                        "to_BusinessPartnerAddress.to_URLAddress.", "to_URLAddress"
        );
        addBusinessPartnerObject(result, "to_BuPaIndustry", buPaIndustry);

        checkAndAddVatNumber(businessPartnerTax, result);

        JsonArray mappedBusinessPartnerRoles = businessPartnerRoleMapper.add(customerTypeSelected);
        if (Objects.nonNull(mappedBusinessPartnerRoles) && !mappedBusinessPartnerRoles.isEmpty()) {
            result.add("to_BusinessPartnerRole", mappedBusinessPartnerRoles);
        }

        JsonArray elements =
            JsonHelperUtil.createValidBuPaIdentificationElement(id.getAsJsonArray(TO_BU_PA_IDENTIFICATION));
        result.add(TO_BU_PA_IDENTIFICATION, elements);
        return result;
    }

    private boolean bothFieldsArePresent(List<MappingDTO> businessPartnerTax) {
        return businessPartnerTax.size() == 2;
    }

    private void checkAndAddVatNumber(List<MappingDTO> businessPartnerTax, JsonObject result) {
        //There need to be a value for vatNumber and taxType, else S4H will throw an error.
        if (!businessPartnerTax.isEmpty() && bothFieldsArePresent(businessPartnerTax)) {
            addBusinessPartnerObject(result, "to_BusinessPartnerTax.", businessPartnerTax);
        }
    }

    private void addNestedBusinessPartnerAddress(JsonObject result, List<MappingDTO> businessPartnerAddress,
        List<MappingDTO> businessPartnerNested, String nestedValue, String nested) {
        if (!businessPartnerAddress.isEmpty()) {
            JsonObject address = new JsonObject();
            for (MappingDTO element : businessPartnerAddress) {
                String key = element.key();
                key = key.replace("to_BusinessPartnerAddress.", "");
                address.addProperty(key, element.value());
            }
            if (!businessPartnerNested.isEmpty()) {
                JsonObject phone = new JsonObject();
                for (MappingDTO element : businessPartnerNested) {
                    String key = element.key();
                    key = key.replace(nestedValue, "");
                    phone.addProperty(key, element.value());
                }
                JsonArray phoneArray = new JsonArray();
                phoneArray.add(phone);
                address.add(nested, phoneArray);
            }
            JsonArray addressArray = new JsonArray();
            addressArray.add(address);
            result.add("to_BusinessPartnerAddress", addressArray);
        }
    }

    private void addBusinessPartnerObject(JsonObject result, String name, List<MappingDTO> list) {
        if (!list.isEmpty()) {
            JsonObject object = new JsonObject();
            for (MappingDTO element : list) {
                String key = element.key();
                key = key.replace(name, "");
                object.addProperty(key, element.value());
            }
            JsonArray jsonArray = new JsonArray();
            jsonArray.add(object);
            String nameWithoutColon = name.replace(".", "");
            result.add(nameWithoutColon, jsonArray);
        }
    }

    private void addBusinessPartners(List<MappingDTO> businessPartner, JsonObject result) {
        for (MappingDTO element : businessPartner) {
            result.addProperty(element.key(), element.value());
        }
    }

    private void addBooleans(List<BooleanMappingDTO> customBoolean, JsonObject result) {
        for (BooleanMappingDTO element : customBoolean) {
            result.addProperty(element.key(), element.value());
        }
    }

    public JsonObject getS4HJSONMapping(final InputSource inputSource, String vat) {
        List<S4HMapping> mappingsGeneral = s4HMappingRepository.getS4HMapping();
        JsonObject s4hGeneralJson = new JsonObject();
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        Node root = null;
        try {
            root = (Node) xPath.evaluate("/", inputSource, XPathConstants.NODE);
        } catch (XPathExpressionException e1) {
            e1.printStackTrace();
            return new JsonObject();
        }
        for (int i = 0; i < mappingsGeneral.size(); ++i) {
            if (mappingsGeneral.get(i).gets4hField().contains(TO_BUSINESS_PARTNER_TAX_BP_TAX_NUMBER)) {
                createVatNumber(s4hGeneralJson, vat);
            } else if (mappingsGeneral.get(i).gets4hField().indexOf("Model") < 0) {
                try {
                    final String[] fieldDepth = mappingsGeneral.get(i).gets4hField().split("\\.");
                    String nodepath = "";
                    if (mappingsGeneral.get(i).getCiField().contains(".")) {
                        nodepath = "/" + mappingsGeneral.get(i).getCiField().replace(".", "/");
                    } else {
                        nodepath = "/" + mappingsGeneral.get(i).getCiField();
                    }
                    String nodeValue = "";
                    if (mappingsGeneral.get(i).isHardcoded()) {
                        nodeValue = mappingsGeneral.get(i).getCiField();
                    } else {
                        nodeValue = xPath.evaluate("/" + nodepath, root);
                    }
                    if (nodeValue.length() > 0) {
                        if (!mappingsGeneral.get(i).gets4hService().contains("multiple")) {
                            if (mappingsGeneral.get(i).isHardcoded()) {
                                if (mappingsGeneral.get(i).getCiField().equals("TODAY")) {
                                    s4hGeneralJson.addProperty(mappingsGeneral.get(i).gets4hField(),
                                                               DATE + new Date().getTime() + ")/"
                                    );
                                } else {
                                    s4hGeneralJson.addProperty(mappingsGeneral.get(i).gets4hField(),
                                                               mappingsGeneral.get(i).getCiField()
                                    );
                                }
                            } else if (mappingsGeneral.get(i).gets4hFieldType().equals("Date")) {
                                if (!nodeValue.isBlank()) {
                                    String year = xPath.evaluate("/" + nodepath + "/year", root);
                                    String month = xPath.evaluate("/" + nodepath + "/month", root);
                                    String day = xPath.evaluate("/" + nodepath + "/day", root);
                                    Date date1 =
                                        new SimpleDateFormat("dd/MM/yyyy").parse(day + "/" + month + "/" + year);
                                    s4hGeneralJson.addProperty(mappingsGeneral.get(i).gets4hField(),
                                                               DATE + date1.getTime() + ")/"
                                    );
                                } else {
                                    s4hGeneralJson.add(mappingsGeneral.get(i).gets4hField(), JsonNull.INSTANCE);
                                }
                            } else if (Arrays.asList(BOOLEAN, DECIMAL, YY1_DEEPLINK_BUS)
                                .contains(mappingsGeneral.get(i).gets4hFieldType()) || Arrays.asList(POSTAL_CODE,
                                                                                                     PHONE_NUMBER
                            ).contains(mappingsGeneral.get(i).gets4hField())) {
                                Object processedVal;
                                if (Arrays.asList(BOOLEAN, DECIMAL, YY1_DEEPLINK_BUS)
                                    .contains(mappingsGeneral.get(i).gets4hFieldType()) && !Arrays.asList(
                                    POSTAL_CODE, PHONE_NUMBER).contains(mappingsGeneral.get(i).gets4hField())) {
                                    processedVal = processField(mappingsGeneral.get(i).gets4hFieldType(), nodeValue);
                                } else {
                                    processedVal = processField(mappingsGeneral.get(i).gets4hField(), nodeValue);
                                }
                                if (mappingsGeneral.get(i).gets4hFieldType().equals(BOOLEAN)) {
                                    s4hGeneralJson.addProperty(mappingsGeneral.get(i).gets4hField(),
                                                               Boolean.parseBoolean(processedVal.toString())
                                    );
                                } else {
                                    s4hGeneralJson.addProperty(mappingsGeneral.get(i).gets4hField(),
                                                               Objects.toString(processedVal)
                                    );
                                }
                            } else if (mappingsGeneral.get(i).gets4hFieldLength() != null && !mappingsGeneral.get(i)
                                .gets4hFieldLength().isEmpty()
                                && Integer.parseInt(mappingsGeneral.get(i).gets4hFieldLength()) > 1
                                && Integer.parseInt(mappingsGeneral.get(i).gets4hFieldLength())
                                < nodeValue.length()) {
                                nodeValue = nodeValue.substring(0,
                                                                Integer.parseInt(mappingsGeneral.get(i).gets4hFieldLength())
                                );
                                s4hGeneralJson.addProperty(mappingsGeneral.get(i).gets4hField(), nodeValue);
                            } else {
                                s4hGeneralJson.addProperty(mappingsGeneral.get(i).gets4hField(), nodeValue);
                            }
                        } else {
                            JsonArray s4hArrayDeep = new JsonArray();
                            JsonObject s4hDeepObject = new JsonObject();
                            if (!s4hGeneralJson.has(fieldDepth[0])) {
                                s4hDeepObject.addProperty(mappingsGeneral.get(i).gets4hField()
                                                              .substring(mappingsGeneral.get(i).gets4hField().lastIndexOf(
                                                                  ".") + 1),
                                                          nodeValue
                                );
                                s4hArrayDeep.add(s4hDeepObject);
                                s4hGeneralJson.add(fieldDepth[0], s4hArrayDeep);
                            } else if (!s4hGeneralJson.get(fieldDepth[0]).getAsJsonArray()
                                .get(s4hGeneralJson.get(fieldDepth[0]).getAsJsonArray().size() - 1)
                                .getAsJsonObject().has(mappingsGeneral.get(i).gets4hField()
                                                           .substring(mappingsGeneral.get(i).gets4hField().lastIndexOf(
                                                               ".") + 1))) {
                                s4hGeneralJson.get(fieldDepth[0]).getAsJsonArray()
                                    .get(s4hGeneralJson.get(fieldDepth[0]).getAsJsonArray().size() - 1)
                                    .getAsJsonObject().addProperty(mappingsGeneral.get(i).gets4hField()
                                                                       .substring(mappingsGeneral.get(i).gets4hField().lastIndexOf(
                                                                           ".") + 1),
                                                                   nodeValue
                                    );
                            } else {
                                s4hDeepObject = new JsonObject();
                                s4hDeepObject.addProperty(mappingsGeneral.get(i).gets4hField()
                                                              .substring(mappingsGeneral.get(i).gets4hField().lastIndexOf(
                                                                  ".") + 1),
                                                          nodeValue
                                );
                                s4hGeneralJson.get(fieldDepth[0]).getAsJsonArray().add(s4hDeepObject);
                            }
                        }
                    }
                } catch (XPathExpressionException | ParseException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return s4hGeneralJson;
    }

    private void createVatNumber(JsonObject jsonObject, String vat) {
        try {
            InputSource isVAT = new InputSource(new StringReader(vat));
            XPathFactory xPathFactoryVAT = XPathFactory.newInstance();
            XPath xPathVAT = xPathFactoryVAT.newXPath();
            Node rootVAT = (Node) xPathVAT.evaluate("/", isVAT, XPathConstants.NODE);
            String nodeValueVAT = xPathVAT.evaluate("/" + "/vat_number", rootVAT);

            if (StringUtils.isNotBlank(nodeValueVAT)) {
                jsonObject.addProperty(TO_BUSINESS_PARTNER_TAX_BP_TAX_NUMBER, nodeValueVAT);
            }
        } catch (XPathExpressionException e) {
            System.out.println("Could not parse xml. Error: " + e.getMessage());
        }
    }

    public static String decode(final String url) {
        String prevURL;
        String decodeURL;
        for (
            prevURL = "", decodeURL = url; !prevURL.equals(decodeURL);
            prevURL = decodeURL, decodeURL = URLDecoder.decode(decodeURL, StandardCharsets.UTF_8)) {
        }
        return decodeURL;
    }

    private static Object processField(final String fieldType, final String inValue) {
        Object outValue = inValue;
        switch (fieldType) {
            case YY1_DEEPLINK_BUS -> outValue = "https://company.info/id/" + inValue;
            case DECIMAL -> {
                DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance(Locale.US);
                DecimalFormat df2 = new DecimalFormat("#.00", decimalFormatSymbols);
                outValue = df2.format(Float.parseFloat(inValue));
            }
            case POSTAL_CODE -> outValue = inValue.replaceAll("(?<=\\d)(?=\\D)", " ");
            case "Date" -> {
                if (inValue != null && !inValue.isEmpty()) {
                    outValue = "//Date(" + inValue + ")//";
                    break;
                }
                outValue = null;
            }
            case BOOLEAN -> outValue = Boolean.parseBoolean(inValue);
            default -> {
            }
        }
        return outValue;
    }

}
