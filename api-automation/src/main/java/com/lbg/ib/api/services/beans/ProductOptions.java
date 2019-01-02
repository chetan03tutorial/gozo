package com.lbg.ib.api.services.beans;

import java.util.List;

public class ProductOptions {

    private String mnenomic;

    private String code;

    private String isEligible;

    private String desc;

    /*private String subBuildingName;

    private String buildingName;

    private String buildingNumber;
    private List<String> addressLines;

    private String postcode;
    private String deliveryPointSuffix;*/

    public ProductOptions() {/*jackson*/ }

    public ProductOptions(String desc, String code, String mnenomic, String isEligible) {
        this.desc = desc;
        this.code = code;
        this.mnenomic = mnenomic;
        this.isEligible = isEligible;
        
    }

    public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void mnenomic(String mnenomic) {
		this.mnenomic = mnenomic;
	}

	public void setisEligible(String isEligible) {
		this.isEligible = isEligible;
	}


	public String getDesc() { return desc; }

    public String getCode() { return code; }

    public String mnenomic() { return mnenomic; }

    public String isEligible() { return isEligible; }

   

}
