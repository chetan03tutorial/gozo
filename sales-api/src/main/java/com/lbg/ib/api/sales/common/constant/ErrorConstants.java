
package com.lbg.ib.api.sales.common.constant;

/**
 * Error constants
 * 
 * @version 0.1
 */
public enum ErrorConstants {

    ERRORCODE_BLANK("000000"),

    EXCP_ERR_APP_NO_PARTIES("1100017"),

    EXCP_ERR_APP_NO_MATCHES("1100018"),

    EXCP_ERR_APP_DUPLICATE("1100020"),

    EXCP_ERR_APP_MANDATE_EXISTS("1100021"),

    EXCP_ERR_APP_INELIGIBLE("1100023"),

    EXCP_ERR_APP_DUP_IB("1100044"),

    ERR_ERR_MANDATE_SUSPENDED("9201116");

    /**
     * Code.
     */
    private final String code;

    /**
     * Default constructor
     */
    ErrorConstants() {
        this.code = null;
    }

    /**
     * Class constructor
     * 
     * @param code
     * @param intValue
     */
    ErrorConstants(String code) {
        this.code = code;
    }

    /**
     * This method returns the String representation of the event passed
     * 
     * @return the String representation of the event passed
     */
    public String code() {
        return (this.code);
    }

    /**
     * Gets the enum corresponding to the type code given.
     * 
     * @param aCode
     *            the internal type code for the key
     * @return the enum corresponding to the type code given
     */
    public static ErrorConstants fromTypeCode(String aCode) {
        for (ErrorConstants e : values()) {
            if (e.code.equals(aCode)) {
                return e;
            }
        }
        return (ERRORCODE_BLANK);
    }

    /**
     * @return the string representation of the object overriding the one inside
     *         object class
     */
    @Override
    public String toString() {
        return (super.toString() + ", code: " + this.code);
    }

}