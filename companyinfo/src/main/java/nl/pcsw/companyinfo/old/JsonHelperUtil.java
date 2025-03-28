package nl.pcsw.companyinfo.old;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class JsonHelperUtil {

    public static final String BP_IDENTIFICATION_NUMBER = "BPIdentificationNumber";
    public static final String BP_IDENTIFICATION_TYPE = "BPIdentificationType";

    private JsonHelperUtil() {
    }

    /**
     * When a BuPaIdentification element is not complete. We should not send it to s4h. As this will result in an
     * error.
     * For instance, if the following BuPaIdentification occurred:
     * <pre>{@code
     * "to_BuPaIdentification" : [
     *      {
     *          "BPIdentificationType" : "BUP002",
     *          "BPIdentificationNumber" : "70110522"
     *      },
     *      {
     *          "BPIdentificationType" : "BUP005"
     *      }
     *  ]
     * }</pre>
     * The last element which has "BUP005" should be removed. This method makes sure that only the filled in elements
     * are present and send. The above example results in the following output.
     * <pre>{@code
     * "to_BuPaIdentification" : [
     *      {
     *          "BPIdentificationType" : "BUP002",
     *          "BPIdentificationNumber" : "70110522"
     *      }
     *  ]
     * }</pre>
     *
     * @param original jsonObject
     *
     * @return JsonArray which only hold correctly formatted data.
     */
    public static JsonArray createValidBuPaIdentificationElement(JsonArray original) {
        JsonArray newBuPaIdentificationElement = new JsonArray();
        if (isNull(original) || original.isEmpty()) {
            return newBuPaIdentificationElement;
        }

        JsonElement numberToBeAddedToArrayElement = null;
        JsonElement typeToBeAddedToNewArrayElement = null;
        for (JsonElement element : original.asList()) {
            JsonObject originalParsedElement = element.getAsJsonObject();
            JsonElement identificationNumber = originalParsedElement.get(BP_IDENTIFICATION_NUMBER);
            JsonElement identificationType = originalParsedElement.get(BP_IDENTIFICATION_TYPE);

            if (nonNull(identificationNumber) && nonNull(identificationType)) {
                newBuPaIdentificationElement.add(originalParsedElement);
            } else if (isNull(identificationNumber) && nonNull(identificationType)) {
                typeToBeAddedToNewArrayElement = identificationType;
            } else if (nonNull(identificationNumber)) {
                numberToBeAddedToArrayElement = identificationNumber;
            }

            if (nonNull(numberToBeAddedToArrayElement) && nonNull(typeToBeAddedToNewArrayElement)) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(BP_IDENTIFICATION_NUMBER, numberToBeAddedToArrayElement.getAsString());
                jsonObject.addProperty(BP_IDENTIFICATION_TYPE, typeToBeAddedToNewArrayElement.getAsString());
                newBuPaIdentificationElement.add(jsonObject);
            }

        }

        return newBuPaIdentificationElement;
    }

}
