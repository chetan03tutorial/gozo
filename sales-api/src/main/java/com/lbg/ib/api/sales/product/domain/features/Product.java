package com.lbg.ib.api.sales.product.domain.features;

import org.codehaus.jackson.annotate.JsonIgnore;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import java.util.List;
import java.util.Map;

public class Product {
    private String                  name;
    @JsonIgnore
    private String                  identifier;
    private String                  mnemonic;
    @JsonIgnore
    private String                  pdtFamilyIdentifier;
    private List<ProductAttributes> productAttributes;
    private Map<String, List<String>> extProdIdentifiers;
    
    private String                  brandName;
    
    public Product() {
        /* JSON auto binder needs this */}
        
    public Product(String name, String identifier, String mnemonic, String pdtFamilyIdentifier,
            List<ProductAttributes> productAttributes, Map<String, List<String>> extProdIdentifiers) {
        this.setName(name);
        this.identifier = identifier;
        this.setMnemonic(mnemonic);
        this.pdtFamilyIdentifier = pdtFamilyIdentifier;
        this.productAttributes = productAttributes;
        this.extProdIdentifiers = extProdIdentifiers;
    }
    
    public String getName() {
        return name;
    }
    
    @JsonIgnore
    public String getIdentifier() {
        return identifier;
    }
    
    public String getMnemonic() {
        return mnemonic;
    }
    
    @JsonIgnore
    public String getPdtFamilyIdentifier() {
        return pdtFamilyIdentifier;
    }
    
    public List<ProductAttributes> getProductAttributes() {
        return productAttributes;
    }

    public Map<String, List<String>> getExtProdIdentifiers() {
        return extProdIdentifiers;
    }

    public String getBrandName() {
        return brandName;
    }
    
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
    
    @Override
    public boolean equals(Object o) {
        return reflectionEquals(this, o);
    }
    


    public void setName(String name) {
        this.name = name;
    }


    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
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
