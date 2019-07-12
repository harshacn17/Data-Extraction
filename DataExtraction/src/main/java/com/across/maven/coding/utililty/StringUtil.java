

package com.across.maven.coding.utililty;

/*
 * Class Name : StringUtil File Name : StringUtil.java Description : Contains static methods related
 * to string manipulation.
 *
 * method
 */
public final class StringUtil {

    public static final String EMPTY_STRING = "";
    public static final String QUOTE = "\"";

    private StringUtil() {
    }

    /**
     * Returns true if a trimmed string is empty
     *
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(final String str) {
        return (str == null) || (str.trim().length() == 0);
    }

    /**
     * returns true if an untrimmed string contains only spaces or null
     *
     * @param str
     * @return
     */
    public static boolean isNullOrEmptyUnTrimmed(final String str) {
        return (str == null) || ((str.trim().length() == 0) && (str.length() > 0));
    }


    public static String value(final String str, final String defaultValue) {
        return isNullOrEmpty(str) ? defaultValue : str;
    }
}
