package com.lbg.ib.api.sales.dto.postcode;

import java.util.List;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class PostalAddressDTO {
    private final String       district;
    private final String       town;
    private final String       postcode;
    private final String       county;
    private final String       organisation;
    private final String       subBuilding;
    private final String       buildingName;
    private final String       buildingNumber;
    private final List<String> addressLines;
    private final String       deliveryPointSuffix;

    public PostalAddressDTO(String district, String town, String postcode, String county, String organisation,
            String subBuilding, String buildingName, String buildingNumber, List<String> addressLines,
            String deliveryPointSuffix) {
        this.district = district;
        this.town = town;
        this.postcode = postcode;
        this.county = county;
        this.organisation = organisation;
        this.subBuilding = subBuilding;
        this.buildingName = buildingName;
        this.buildingNumber = buildingNumber;
        this.addressLines = addressLines;
        this.deliveryPointSuffix = deliveryPointSuffix;
    }

    public String getDistrict() {
        return district;
    }

    public String getTown() {
        return town;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCounty() {
        return county;
    }

    public String getOrganisation() {
        return organisation;
    }

    public String getSubBuilding() {
        return subBuilding;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public List<String> getAddressLines() {
        return addressLines;
    }

    public String getDeliveryPointSuffix() {
        return deliveryPointSuffix;
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
