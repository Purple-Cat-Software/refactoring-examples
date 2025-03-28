package nl.pcsw.companyinfo.old.additional;

public final class StringUtils {

    private StringUtils() {
    }

    public static boolean isNotBlank(String string) {
        return string != null && !string.isEmpty();
    }
}
