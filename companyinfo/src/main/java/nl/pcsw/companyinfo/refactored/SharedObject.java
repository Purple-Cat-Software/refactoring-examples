package nl.pcsw.companyinfo.refactored;

import nl.pcsw.companyinfo.old.additional.S4HMapping;
import org.xml.sax.InputSource;

import java.util.ArrayList;
import java.util.List;

public class SharedObject {
    private InputSource xml;
    private String vatXml;
    private List<S4HMapping> mappings = new ArrayList<>();

    public InputSource getXml() {
        return xml;
    }

    public void setXml(InputSource xml) {
        this.xml = xml;
    }

    public String getVatXml() {
        return vatXml;
    }

    public void setVatXml(String vatXml) {
        this.vatXml = vatXml;
    }

    public List<S4HMapping> getMappings() {
        return mappings;
    }

    public void setMappings(List<S4HMapping> mappings) {
        this.mappings = mappings;
    }
}
