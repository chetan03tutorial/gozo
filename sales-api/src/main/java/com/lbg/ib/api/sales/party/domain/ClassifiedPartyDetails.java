/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.party.domain;

public class ClassifiedPartyDetails {

    private String  countryName;
    private boolean tinRequired;
    private String  regex;
    private String  taxResidencyType; // 1-FATCA, 4-CRS

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public boolean isTinRequired() {
        return tinRequired;
    }

    public void setTinRequired(boolean tinRequired) {
        this.tinRequired = tinRequired;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getTaxResidencyType() {
        return taxResidencyType;
    }

    public void setTaxResidencyType(String taxResidencyType) {
        this.taxResidencyType = taxResidencyType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((countryName == null) ? 0 : countryName.hashCode());
        result = prime * result + ((regex == null) ? 0 : regex.hashCode());
        result = prime * result + ((taxResidencyType == null) ? 0 : taxResidencyType.hashCode());
        result = prime * result + (tinRequired ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ClassifiedPartyDetails other = (ClassifiedPartyDetails) obj;
        if (countryName == null) {
            if (other.countryName != null) {
                return false;
            }
        } else if (!countryName.equals(other.countryName)) {
            return false;
        }
        if (regex == null) {
            if (other.regex != null) {
                return false;
            }
        } else if (!regex.equals(other.regex)) {
            return false;
        }
        if (taxResidencyType == null) {
            if (other.taxResidencyType != null) {
                return false;
            }
        } else if (!taxResidencyType.equals(other.taxResidencyType)) {
            return false;
        }
        if (tinRequired != other.tinRequired) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ClassifiedPartyDetails {countryName=" + countryName + ", tinRequired=" + tinRequired + ", regex="
                + regex + ", taxResidencyType=" + "}";
    }

}
