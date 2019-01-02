package com.lbg.ib.api.sales.product.domain.features;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

import java.util.Map;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class ProductFeatures {
    private Product             product;
    private DeviceID            deviceID;
    private Map<String, String> productVariances;

    public ProductFeatures() {
        /* jackson */}

    public ProductFeatures(Product product, DeviceID deviceID) {
        this.product = product;
        this.deviceID = deviceID;
    }

    public ProductFeatures(Product product, DeviceID deviceID, Map<String, String> productVariances) {
        this.product = product;
        this.deviceID = deviceID;
        this.productVariances = productVariances;
    }

    public Product getProduct() {
        return product;
    }

    public DeviceID getDeviceID() {
        return deviceID;
    }

    @Override
    public boolean equals(Object o) {
        return reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }
}
