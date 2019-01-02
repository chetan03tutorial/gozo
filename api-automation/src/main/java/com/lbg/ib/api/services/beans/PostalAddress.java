package com.lbg.ib.api.services.beans;

import java.util.List;

public class PostalAddress {

    private String district;

    private String town;

    private String county;

    private String organisationName;

    private String subBuildingName;

    private String buildingName;

    private String buildingNumber;
    private List<String> addressLines;

    private String postcode;
    private String deliveryPointSuffix;

    public PostalAddress() {/*jackson*/ }

    public PostalAddress(String district, String town, String county, String organisationName, String subBuildingName,
                         String buildingName, String buildingNumber, List<String> addressLines, String postcode,
                         String deliveryPointSuffix) {
        this.district = district;
        this.town = town;
        this.county = county;
        this.organisationName = organisationName;
        this.subBuildingName = subBuildingName;
        this.buildingName = buildingName;
        this.buildingNumber = buildingNumber;
        this.addressLines = addressLines;
        this.postcode = postcode;
        this.deliveryPointSuffix = deliveryPointSuffix;
    }

    public void setDistrict(String district) {
		this.district = district;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public void setSubBuildingName(String subBuildingName) {
		this.subBuildingName = subBuildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public void setBuildingNumber(String buildingNumber) {
		this.buildingNumber = buildingNumber;
	}

	public void setAddressLines(List<String> addressLines) {
		this.addressLines = addressLines;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public void setDeliveryPointSuffix(String deliveryPointSuffix) {
		this.deliveryPointSuffix = deliveryPointSuffix;
	}

	public String getDistrict() { return district; }

    public String getTown() { return town; }

    public String getCounty() { return county; }

    public String getOrganisationName() { return organisationName; }

    public String getSubBuildingName() { return subBuildingName; }

    public String getBuildingName() { return buildingName; }

    public String getBuildingNumber() { return buildingNumber; }

    public List<String> getAddressLines() { return addressLines; }

    public String getPostcode() { return postcode; }

    public String getDeliveryPointSuffix() { return deliveryPointSuffix; }

}
