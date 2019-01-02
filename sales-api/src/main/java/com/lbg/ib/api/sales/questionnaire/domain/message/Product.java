package com.lbg.ib.api.sales.questionnaire.domain.message;

import java.util.LinkedList;
import java.util.List;

import com.lbg.ib.api.shared.validation.RequiredFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;

@Validate
public class Product {

    @RequiredFieldValidation
    private List<ProductFeature>        productOptions;

    private List<ProductRelatedFeature> productRelatedOption;

    private String                      productIdentifier;

    private String                      productName;

    public String getProductIdentifier() {
        return productIdentifier;
    }

    public void setProductIdentifier(String productIdentifier) {
        this.productIdentifier = productIdentifier;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<ProductFeature> getProductOptions() {
        if (productOptions == null) {
            productOptions = new LinkedList<ProductFeature>();
        }
        return productOptions;
    }

    public void setProductOptions(List<ProductFeature> productOptions) {
        this.productOptions = productOptions;
    }

    public List<ProductRelatedFeature> getProductRelatedOption() {
        if (productRelatedOption == null) {
            productRelatedOption = new LinkedList<ProductRelatedFeature>();
        }
        return productRelatedOption;
    }

    public void setProductRelatedOption(List<ProductRelatedFeature> productRelatedOption) {
        this.productRelatedOption = productRelatedOption;
    }

}