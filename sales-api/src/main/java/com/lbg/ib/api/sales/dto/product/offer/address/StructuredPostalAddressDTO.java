package com.lbg.ib.api.sales.dto.product.offer.address;

import java.util.List;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class StructuredPostalAddressDTO {
    private final String       district;
    private final String       town;
    private final String       county;
    private final String       organisation;
    private final String       subBuilding;
    private final String       building;
    private final String       buildingNumber;
    private final List<String> addressLines;

    public StructuredPostalAddressDTO(String district, String town, String county, String organisation,
            String subBuilding, String building, String buildingNumber, List<String> addressLines) {
        this.district = district;
        this.town = town;
        this.county = county;
        this.organisation = organisation;
        this.subBuilding = subBuilding;
        this.building = building;
        this.buildingNumber = buildingNumber;
        this.addressLines = addressLines;
    }

    public String district() {
        return district;
    }

    public String town() {
        return town;
    }

    public String county() {
        return county;
    }

    public String organisation() {
        return organisation;
    }

    public String subBuilding() {
        return subBuilding;
    }

    public String building() {
        return building;
    }

    public String buildingNumber() {
        return buildingNumber;
    }

    public List<String> addressLines() {
        return addressLines;
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
