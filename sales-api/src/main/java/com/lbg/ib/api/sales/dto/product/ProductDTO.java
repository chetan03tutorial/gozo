package com.lbg.ib.api.sales.dto.product;

import static java.util.Collections.unmodifiableMap;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductDTO {
    private String              name;
    private String              identifier;
    private String              mnemonic;
    private String              pdtFamilyIdentifier;
    private Map<String, String> options;
    private Map<String, List<String>> extProdIdentifiers;
    private List<ProductDTO>    children = null;
    private String              productFrequency;
    
    public ProductDTO(){
    }
    
    public ProductDTO(String name, String identifier, String mnemonic, String pdtFamilyIdentifier,
            Map<String, String> options, Map<String, List<String>> extProdIdentifiers, List<ProductDTO> children) {
        this.name = name;
        this.identifier = identifier;
        this.mnemonic = mnemonic;
        this.pdtFamilyIdentifier = pdtFamilyIdentifier;
        this.options = unmodifiableMap(options);
        this.extProdIdentifiers = extProdIdentifiers;
        this.children = children;
    }
    
    public List<ProductDTO> getChildren() {
        if (null == this.children) {
            children = new ArrayList<ProductDTO>();
        }
        return this.children;
    }
    
    public String getName() {
        return name;
    }
    
    public Map<String, String> getOptions() {
        return options;
    }
    
    public String getIdentifier() {
        return identifier;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getMnemonic() {
        return mnemonic;
    }
    
    public Map<String, List<String>> getExtProdIdentifiers() {
        return extProdIdentifiers;
    }
    
    public String getPdtFamilyIdentifier() {
        return pdtFamilyIdentifier;
    }
    
    public String getProductFrequency() {
        return productFrequency;
    }

    public void setProductFrequency(String productFrequency) {
        this.productFrequency = productFrequency;
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
