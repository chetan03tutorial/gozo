package com.lbg.ib.api.sales.dao.product.holding.domain;

import org.apache.commons.lang.StringUtils;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * The ApplicationSessionManagement is the implementation of the
 * SessionManagementDAO.
 *
 * @author Debashish Bhattacharjee
 * @version 1.0
 * @since 14thFeb2016
 ***********************************************************************/
public enum ProductArrangementLifecycleStatus {

    O("Open"), C("Closed"), B("Blocked"), R("Referred"), U("Unknown");

    private String value;

    private ProductArrangementLifecycleStatus(final String value) {
        this.value = value;
    }

    /**
     * 
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * 
     * @param value
     * @return
     */
    public static ProductArrangementLifecycleStatus getByValue(final String value) {
        if (StringUtils.isNotEmpty(value)) {
            for (ProductArrangementLifecycleStatus addressTypeEnum : ProductArrangementLifecycleStatus.values()) {
                if (addressTypeEnum.value.equals(value.trim())) {
                    return addressTypeEnum;
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
        throw new IllegalArgumentException(value);
    }

}
