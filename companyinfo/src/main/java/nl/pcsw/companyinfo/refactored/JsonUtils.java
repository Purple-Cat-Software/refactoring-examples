package nl.pcsw.companyinfo.refactored;

import nl.pcsw.companyinfo.old.additional.S4HMapping;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.Objects;

public class JsonUtils {

    public String getS4HJSONMapping(SharedObject sharedObject) {
        final var mappings = sharedObject.getMappings();
        var xPath = XPathFactory.newInstance().newXPath();
        var root = getRootXML(xPath, sharedObject.getXml());

        for (var s4hMapping : mappings) {
            try {
                var nodePath = determineNodePath(s4hMapping.getCiField());
                var nodeValue = determineNodeValue(xPath, nodePath, s4hMapping);
                if (Objects.nonNull(nodeValue) && !nodeValue.isBlank()) {

                }
            } catch (XPathExpressionException e) {
                System.out.println("XPathExpressionException: " + e.getMessage());
                // Just continue with the rest.
            }
        }

        return "";
    }


    private String determineNodeValue(XPath xPath, String nodePath, S4HMapping s4hMapping) throws XPathExpressionException {
        if (s4hMapping.isHardcoded()) {
            return s4hMapping.getCiField();
        }
        return xPath.evaluate("/", nodePath);
    }

    private String determineNodePath(String companyInfoField) {
        if (companyInfoField.contains(".")) {
            return "/" + companyInfoField.replace(".", "/");
        }
        return "/" + companyInfoField;
    }

    private Node getRootXML(XPath xPath, InputSource input) {
        try {
            return (Node) xPath.evaluate("/", input, XPathConstants.NODE);
        } catch (XPathExpressionException e1) {
            e1.printStackTrace();
        }
        return null;
    }

}
