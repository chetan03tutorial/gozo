package com.lbg.ib.api.sales.dto.product.offer.address;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class PostalAddressComponentDTO {
    private final StructuredPostalAddressDTO   structured;
    private final UnstructuredPostalAddressDTO unstructured;
    private final String                       durationOfStay;
    private final String                       addressStatus;
    private final String                       deliveryPointSuffix;
    private final String                       postcode;
    private final Boolean                      isPAFFormat;
    private final Boolean                      isBFPOAddress;

    public PostalAddressComponentDTO(StructuredPostalAddressDTO structured, String durationOfStay, String addressStatus,
            String deliveryPointSuffix, String postcode, Boolean isPAFFormat, Boolean isBFPOAddress) {
        this.structured = structured;
        this.durationOfStay = durationOfStay;
        this.addressStatus = addressStatus;
        this.deliveryPointSuffix = deliveryPointSuffix;
        this.postcode = postcode;
        this.isPAFFormat = isPAFFormat;
        this.isBFPOAddress = isBFPOAddress;
        this.unstructured = null;
    }

    public PostalAddressComponentDTO(UnstructuredPostalAddressDTO unstructured, String durationOfStay,
            String addressStatus, String deliveryPointSuffix, String postcode, Boolean isPAFFormat,
            Boolean isBFPOAddress) {
        this.unstructured = unstructured;
        this.durationOfStay = durationOfStay;
        this.addressStatus = addressStatus;
        this.deliveryPointSuffix = deliveryPointSuffix;
        this.postcode = postcode;
        this.isPAFFormat = isPAFFormat;
        this.isBFPOAddress = isBFPOAddress;
        this.structured = null;
    }

    public Boolean isStructured() {
        return unstructured == null;
    }

    public StructuredPostalAddressDTO structured() {
        return structured;
    }

    public UnstructuredPostalAddressDTO unstructured() {
        return unstructured;
    }

    public String durationOfStay() {
        return durationOfStay;
    }

    public String addressStatus() {
        return addressStatus;
    }

    public String deliveryPointSuffix() {
        return deliveryPointSuffix;
    }

    public String postcode() {
        return postcode;
    }

    public Boolean isPAFFormat() {
        return isPAFFormat;
    }

    public Boolean isBFPOAddress() {
        return isBFPOAddress;
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