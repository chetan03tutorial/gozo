
package com.lbg.ib.api.sales.docupload.constants;

/**
 * @author chandra kachhawaha
 * 
 */

public enum ErrorConstants {

    /**
    * 
    */
    EXCP_ERRCD_SERVICE_LOCATOR_NOT_FOUND("8200300"),

    /**
    * 
    */
    ERRORCODE_BLANK("0000000");
    /**
     * /** Code.
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
