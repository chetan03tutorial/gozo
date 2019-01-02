/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.domain.suitability;

import java.util.List;

import com.lbg.ib.api.sales.product.domain.eligibility.EligibilityDetails;

/**
 * DataMapper object to transfer list of eligible mnemonics and its
 * corresponding product attributes
 */
public class ProductQualifier {

    private List<EligibilityDetails>     eligibilityDetails;

    private List<ProductQualiferOptions> productOptions;

    public ProductQualifier() {
        /* Default constructor */
    }

    public ProductQualifier(List<EligibilityDetails> eligibilityDetails, List<ProductQualiferOptions> productOptions) {
        super();
        this.eligibilityDetails = eligibilityDetails;
        this.productOptions = productOptions;
    }

    public List<ProductQualiferOptions> getProductOptions() {
        return productOptions;
    }

    public List<EligibilityDetails> getEligibilityDetails() {
        return eligibilityDetails;
    }

    public void setEligibilityDetails(List<EligibilityDetails> eligibilityDetails) {
        this.eligibilityDetails = eligibilityDetails;
    }

    public void setProductOptions(List<ProductQualiferOptions> productOptions) {
        this.productOptions = productOptions;
    }

    @Override
    public String toString() {
        return "ProductQualifier [eligibilityDetails=" + eligibilityDetails + ", productOptions=" + productOptions
                + "]";
    }

}
