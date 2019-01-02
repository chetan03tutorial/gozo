package com.lbg.ib.api.sales.dao.product.holding.domain;

import com.lbg.ib.api.sso.domain.product.Category;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 *
 *
 * @author Debashish Bhattacharjee
 * @version 1.0
 * @since 14thFeb2016
 ***********************************************************************/
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer {
    
    private String         host;
    
    private String         ocisId;
    
    private String         partyId;
    
    private String         lastLoginDate;
    
    private String         internalUserIdentifier;
    
    private String 		   cbsCustomerNum;
    
    private List<Category> categories;
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public String getOcisId() {
        return ocisId;
    }
    
    public void setOcisId(String ocisId) {
        this.ocisId = ocisId;
    }
    
    public String getPartyId() {
        return partyId;
    }
    
    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }
    
    public List<Category> getCategories() {
        return categories;
    }
    
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
    
    public String getLastLoginDate() {
        return lastLoginDate;
    }
    
    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
    
    public String getInternalUserIdentifier() {
        return internalUserIdentifier;
    }
    
    public void setInternalUserIdentifier(String internalUserIdentifier) {
        this.internalUserIdentifier = internalUserIdentifier;
    }
    
    /**
	 * @return the cbsCustomerNum
	 */
	public String getCbsCustomerNum() {
		return cbsCustomerNum;
	}

	/**
	 * @param cbsCustomerNum the cbsCustomerNum to set
	 */
	public void setCbsCustomerNum(String cbsCustomerNum) {
		this.cbsCustomerNum = cbsCustomerNum;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        Customer that = (Customer) o;
        
        if (host != null ? !host.equals(that.host) : that.host != null) {
            return false;
        }
        if (ocisId != null ? !ocisId.equals(that.ocisId) : that.ocisId != null) {
            return false;
        }
        if (partyId != null ? !partyId.equals(that.partyId) : that.partyId != null) {
            return false;
        }
        if (categories != null ? !categories.equals(that.categories) : that.categories != null) {
            return false;
        }
        return true;
        
    }
    
    @Override
    public int hashCode() {
        int result = host != null ? host.hashCode() : 0;
        result = 31 * result + (ocisId != null ? ocisId.hashCode() : 0);
        result = 31 * result + (partyId != null ? partyId.hashCode() : 0);
        result = 31 * result + (categories != null ? categories.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "Customer {" + "host='" + host + '\'' + ", ocisId='" + ocisId + '\'' + ", partyId='" + partyId + '\''
                + ", categories='" + categories + '\'' + ", lastLoginDate='" + lastLoginDate + '\''
                + ", internalUserIdentifier='" + internalUserIdentifier + '}';
        
    }
}