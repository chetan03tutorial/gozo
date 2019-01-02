package com.lbg.ib.api.sales.dto.product;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lbg.ib.api.sales.dto.product.suitability.ProductSuitabilityOptionsDto;

public class ProductData {
    private String                             name;
    private String                             identifier;
    private String                             mnemonic;
    private String                             pdtFamilyIdentifier;
    private Map<String, String>                extProdIdentifiers;
    private List<ProductData>                  children;
    private List<ProductSuitabilityOptionsDto> productOptions;

    public ProductData() {
        /*
         * Default Constructor for jackson initialization
         */
    }

    public ProductData(String name, String identifier, String mnemonic, String pdtFamilyIdentifier,
            List<ProductSuitabilityOptionsDto> optionList, Map<String, String> extProdIdentifiers,
            List<ProductData> children) {
        this.name = name;
        this.identifier = identifier;
        this.mnemonic = mnemonic;
        this.pdtFamilyIdentifier = pdtFamilyIdentifier;
        this.productOptions = optionList;
        this.extProdIdentifiers = extProdIdentifiers;
        this.children = children;
    }

    public List<ProductData> getChildren() {
        if (null == this.children) {
            children = new ArrayList<ProductData>();
        }
        return this.children;
    }

    public Map<String, String> getExtProdIdentifiers() {
        return extProdIdentifiers;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public String getName() {
        return name;
    }

    public String getPdtFamilyIdentifier() {
        return pdtFamilyIdentifier;
    }

    public void setChildren(List<ProductData> children) {
        getChildren().addAll(children);
    }

    public void setExtProdIdentifiers(Map<String, String> extProdIdentifiers) {
        this.extProdIdentifiers = extProdIdentifiers;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPdtFamilyIdentifier(String pdtFamilyIdentifier) {
        this.pdtFamilyIdentifier = pdtFamilyIdentifier;
    }

    public List<ProductSuitabilityOptionsDto> getProductOptions() {
        return productOptions;
    }

    public void setProductOptions(List<ProductSuitabilityOptionsDto> productOptions) {
        this.productOptions = productOptions;
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
        return "ProductData [name=" + name + ", identifier=" + identifier + ", mnemonic=" + mnemonic
                + ", pdtFamilyIdentifier=" + pdtFamilyIdentifier + ", extProdIdentifiers=" + extProdIdentifiers
                + ", children=" + children + ", productOptions=" + productOptions + "]";
    }

}
