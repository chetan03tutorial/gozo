package com.lbg.ib.api.services.beans;


public class DetermineEligibility {

private String mnemonic;

private String code;

private String isEligible;

private String desc;



public DetermineEligibility() {/*jackson*/ }

public DetermineEligibility(String desc, String code, String mnemonic, String isEligible) {
    this.desc = desc;
    this.code = code;
    this.mnemonic = mnemonic;
    this.isEligible = isEligible;
    
}

public void setDesc(String desc) {
	this.desc = desc;
}

public void setCode(String code) {
	this.code = code;
}

public void mnemonic(String mnemonic) {
	this.mnemonic = mnemonic;
}

public void setisEligible(String isEligible) {
	this.isEligible = isEligible;
}


public String getDesc() { return desc; }

public String getcode() { return code; }

public String getmnenomic() { return mnemonic; }

public String getisEligible() { return isEligible; }



}
