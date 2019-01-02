package com.lbg.ib.api.sales.product.domain;

import com.lbg.ib.api.sso.domain.product.Category;

import java.util.Calendar;
import java.util.List;

public class ProductHolding {
    
    private List<Product>  products;
    
    private List<Category> categories;
    
    private Calendar       lastLoginDate;
    
    private String         internalUserIdentifier;
    
    private String         ocisId;
    
    private Boolean        kycRefreshRequired;
    
    private String 		   cbsCustomerNumber;
    
    public List<Product> getProducts() {
        return products;
    }
    
    public void setProducts(List<Product> products) {
        this.products = products;
    }
    
    public List<Category> getCategories() {
        return categories;
    }
    
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
    
    public String getOcisId() {
        return ocisId;
    }
    
    public void setOcisId(String ocisId) {
        this.ocisId = ocisId;
    }
    
    public Calendar getLastLoginDate() {
        return lastLoginDate;
    }
    
    public void setLastLoginDate(Calendar lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
    
    public String getInternalUserIdentifier() {
        return internalUserIdentifier;
    }
    
    public void setInternalUserIdentifier(String internalUserIdentifier) {
        this.internalUserIdentifier = internalUserIdentifier;
    }
    
    public Boolean getKycRefreshRequired() {
        return kycRefreshRequired;
    }
    
    public void setKycRefreshRequired(Boolean kycRefreshRequired) {
        this.kycRefreshRequired = kycRefreshRequired;
    }
    
	/**
	 * @return the cbsCustomerNumber
	 */
	public String getCbsCustomerNumber() {
		return cbsCustomerNumber;
	}

	/**
	 * @param cbsCustomerNumber the cbsCustomerNumber to set
	 */
	public void setCbsCustomerNumber(String cbsCustomerNumber) {
		this.cbsCustomerNumber = cbsCustomerNumber;
	}

	@Override
    public boolean equals(Object o) {
        /*
         * if (this == o) { return true; } if (o == null || getClass() !=
         * o.getClass()) { return false; }
         */
        
        ProductHolding that = (ProductHolding) o;
        
        if (products != null ? !products.equals(that.products) : that.products != null) {
            return false;
        }
        if (categories != null ? !categories.equals(that.categories) : that.categories != null) {
            return false;
        }
        return true;
        
    }
    
    @Override
    public int hashCode() {
        int result = products != null ? products.hashCode() : 0;
        result = 31 * result + (categories != null ? categories.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "ProductHolding {" + "products='" + products + '\'' + ", categories='" + categories + '\'' + '}';
    }
    
}
