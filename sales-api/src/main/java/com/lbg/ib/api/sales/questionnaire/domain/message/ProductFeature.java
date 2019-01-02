package com.lbg.ib.api.sales.questionnaire.domain.message;

import java.util.LinkedList;
import java.util.List;

import com.lbg.ib.api.sales.common.validation.BinaryStringValidation;
import com.lbg.ib.api.sales.common.validation.BinaryType;
import com.lbg.ib.api.shared.validation.RequiredFieldValidation;
import com.lbg.ib.api.shared.validation.Validate;

@Validate
public class ProductFeature {

    @RequiredFieldValidation
    @BinaryStringValidation(value = { BinaryType.Y, BinaryType.N })
    private String                      optionValue;

    /**
     * @CustomFieldValidation(validation = ProductOptionValidation.class)
     */
    @RequiredFieldValidation
    private String                      optionType;

    /**
     * @CustomFieldValidation(validation= ProductRelatedOptionValidation.class)
     */
    private List<ProductRelatedFeature> productRelatedOptions;

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    public List<ProductRelatedFeature> getProductRelatedOptions() {
        if (productRelatedOptions == null) {
            productRelatedOptions = new LinkedList<ProductRelatedFeature>();
        }
        return productRelatedOptions;
    }

    public void setProductRelatedOptions(List<ProductRelatedFeature> productRelatedOptions) {
        this.productRelatedOptions = productRelatedOptions;
    }

}
