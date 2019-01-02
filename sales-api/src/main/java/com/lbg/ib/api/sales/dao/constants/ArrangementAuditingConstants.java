package com.lbg.ib.api.sales.dao.constants;

/**
 * Created by 1174091 on 25/04/2016.
 */
public enum ArrangementAuditingConstants {

    NO_DEVICEID("NoDeviceID"),

    AFDI_EVENT("AFDI"),

    SIRA_SUCCESS("SUCCESS"),

    SIRA_FAILURE("FAIL"),

    ACCEPT("ACCEPT"),

    REFER("REFER"),

    DECLINE("DECLINE"),

    SUBMIT("SUBMITTED"),

    EVENT_ID_SIRA_CONNECT("SZ06"),

    EVENT_ID_SIRA_DECISION("SZ07"),

    EVENT_ID_SUCCESS_TM_CALL("SZ03"),

    EVENT_ID_FAILURE_TM_CALL("SZ04");

    /**
     * Code.
     */
    private final String code;

    /**
     * Default constructor
     */
    ArrangementAuditingConstants() {
        this.code = null;
    }

    /**
     * Class constructor
     *
     * @param code
     */
    ArrangementAuditingConstants(String code) {
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
     * @return the string representation of the object overriding the one inside
     *         object class
     */
    @Override
    public String toString() {
        return (super.toString() + ", code: " + this.code);
    }

}
