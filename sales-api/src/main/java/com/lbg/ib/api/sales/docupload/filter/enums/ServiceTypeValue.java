/**********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 * Class Name: ServiceTypeValue
 *
 * Author(s): 8735182
 *
 * Date: 26 Nov 2015
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.filter.enums;

public enum ServiceTypeValue {

    /**
     * constant for retail.
     */
    PERSONAL("RETAIL"),

    /**
     * constant for commercial.
     */
    BUSINESS("COMMERCIAL");

    private final String serviceType;

    /**
     * Constructor for service type.
     * 
     * @param serviceType
     *            - service type.
     */
    private ServiceTypeValue(final String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * return serviceType value.
     * 
     * @return serviceType
     */
    public String getServiceType() {
        return (serviceType);
    }

}
