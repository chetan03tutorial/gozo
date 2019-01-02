/**********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.  
 *
 * All Rights Reserved.   
 *
 * Class Name: RequestHeaderDetails   
 *
 * Author(s): 8735182
 *
 * Date: 26 Nov 2015  
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.common.rest.enums;

public enum RequestHeaderDetails {

    /**
     * Key for Brand .
     */
    KEY_HEADER_BRAND("Brand"), //$NON-NLS-1$
    /**
     * Key for ServiceType .
     */
    KEY_HEADER_SERVICE_TYPE("ServiceType"), //$NON-NLS-1$
    /**
     * Key for Host .
     */
    KEY_HEADER_HOST("Host"), //$NON-NLS-1$
    /**
     * Key for Brand .
     */
    KEY_HEADER_XBRAND("XBrandId"),
    /**
     * Key for CONTENT Type .
     */
    KEY_HEADER_CONTENT_TYPE("ContentType"),
    /**
     * Key for Channel .
     */
    KEY_HEADER_CHANNEL("XChannel"),
    /**
     * Key for SessionId .
     */
    KEY_HEADER_SESSION_ID("XSessionId"),
    /**
     * Key for DeviceIp .
     */
    KEY_HEADER_DEVICE_IP("XDeviceIp"),
    /**
     * Section for RequestHeaderDetails .
     */
    SECTION_REQUEST_HEADER_DETAILS("RequestHeaderDetails");

    /**
     * Declare code.
     */
    private final String code;

    /**
     * Default constructor
     */
    RequestHeaderDetails() {
        this.code = null;
    }

    /**
     * Class constructor
     * 
     * @param code
     */
    RequestHeaderDetails(String code) {
        this.code = code;
    }

    /**
     * Return code.
     * 
     * @return the String representation of the state passed
     */
    public String code() {
        return (this.code);
    }
}
