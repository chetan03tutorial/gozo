package com.lbg.ib.api.sales.product.domain;

import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lbg.ib.api.sales.dto.product.ProductDTO;
import com.lbg.ib.api.sales.product.domain.features.ExternalProductIdentifier;

public class SelectedProduct {
    private String                            identifier;
    private String                      name;
    private String                      pdtFamilyIdentifier;
    private ExternalProductIdentifier[] exterIdentifiers;
    private String                            mnemonic;
    private Map<String, SelectedProduct>      alternateProductMap;
    private List<ProductDTO>                  children;
    private Map<String, String>               optionsMap;

    public SelectedProduct(){
    }

    public SelectedProduct(String name, String identifier, String pdtFamilyIdentifier,
            ExternalProductIdentifier[] exterIdentifiers, String mnemonic, List<ProductDTO> children,
            Map<String, String> optionsMap) {
        this.setIdentifier(identifier);
        this.setName(name);
        this.setPdtFamilyIdentifier(pdtFamilyIdentifier);
        this.setExterIdentifiers(exterIdentifiers);
        this.setMnemonic(mnemonic);
        this.setAlternateProductMap(new HashMap());
        this.setChildren(children);
        this.setOptionsMap(optionsMap);
    }
    
    public boolean hasAlternate(String productId) {
        return getAlternateProductMap().containsKey(productId);
    }
    
    public SelectedProduct getAlternateProduct(String productId) {
        return getAlternateProductMap().get(productId);
    }
    
    public String getIdentifier() {
        return identifier;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPdtFamilyIdentifier() {
        return pdtFamilyIdentifier;
    }
    
    public ExternalProductIdentifier[] getExternalProductIdentifiers() {
        return getExterIdentifiers();
    }
    
    public String getMnemonic() {
        return mnemonic;
    }
    
    public void addAlternateProduct(SelectedProduct alternateProduct) {
        getAlternateProductMap().put(alternateProduct.getIdentifier(), alternateProduct);
    }
    
    public List<ProductDTO> getChildren() {
        if (children == null) {
            return new ArrayList<ProductDTO>();
        }
        return children;
    }
    
    public void setChildren(List<ProductDTO> children) {
        this.children = children;
    }
    
    public Map<String,String> getOptionsMap(){
        return optionsMap;
    }


    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPdtFamilyIdentifier(String pdtFamilyIdentifier) {
        this.pdtFamilyIdentifier = pdtFamilyIdentifier;
    }

    public ExternalProductIdentifier[] getExterIdentifiers() {
        return exterIdentifiers;
    }

    public void setExterIdentifiers(ExternalProductIdentifier[] exterIdentifiers) {
        this.exterIdentifiers = exterIdentifiers;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public Map<String, SelectedProduct> getAlternateProductMap() {
        return alternateProductMap;
    }

    public void setAlternateProductMap(Map<String, SelectedProduct> alternateProductMap) {
        this.alternateProductMap = alternateProductMap;
    }

    public void setOptionsMap(Map<String, String> optionsMap) {
        this.optionsMap = optionsMap;
    }


    @Override
    public int hashCode() {
        return reflectionHashCode(this);
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
        SelectedProduct other = (SelectedProduct) obj;
        if (!Arrays.equals(getExterIdentifiers(), other.getExterIdentifiers())) {
            return false;
        }
        if (getIdentifier() == null) {
            if (other.getIdentifier() != null) {
                return false;
            }
        } else if (!getIdentifier().equals(other.getIdentifier())) {
            return false;
        }
        if (getMnemonic() == null) {
            if (other.getMnemonic() != null) {
                return false;
            }
        } else if (!getMnemonic().equals(other.getMnemonic())) {
            return false;
        }
        if (getName() == null) {
            if (other.getName() != null) {
                return false;
            }
        } else if (!getName().equals(other.getName())) {
            return false;
        }
        if (getPdtFamilyIdentifier() == null) {
            if (other.getPdtFamilyIdentifier() != null) {
                return false;
            }
        } else if (!getPdtFamilyIdentifier().equals(other.getPdtFamilyIdentifier())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }
}
