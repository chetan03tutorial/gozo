package com.lbg.ib.api.sales.product.domain.pending;


import com.lbg.ib.api.sales.product.domain.Condition;
import com.lbg.ib.api.sales.product.domain.activate.AssessmentEvidence;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerDocument;
import com.lbg.ib.api.shared.validation.RequiredFieldValidation;

import java.io.Serializable;
import java.util.List;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * @author Debashish Bhattacharjee
 * @version 1.0
 * @since 4thApril2017
 ***********************************************************************/

public class ModifyProductArrangement implements Serializable {
    @RequiredFieldValidation
    private String arrangementType;
    @RequiredFieldValidation
    private String arrangementId;

    @RequiredFieldValidation
    private Condition[]   conditions;

    private List<CustomerDocument> customerDocuments;
    
    private AssessmentEvidence parentAssessmentEvidence;
    private AssessmentEvidence primaryAssessmentEvidence;
    
    public String getArrangementType() {
        return arrangementType;
    }
    
    public void setArrangementType(String arrangementType) {
        this.arrangementType = arrangementType;
    }
    
    public String getArrangementId() {
        return arrangementId;
    }
    
    public void setArrangementId(String arrangementId) {
        this.arrangementId = arrangementId;
    }
    
    public List<CustomerDocument> getCustomerDocuments() {
        return customerDocuments;
    }
    
    public void setCustomerDocuments(List<CustomerDocument> customerDocuments) {
        this.customerDocuments = customerDocuments;
    }
    
    public AssessmentEvidence getParentAssessmentEvidence() {
        return parentAssessmentEvidence;
    }

    public void setParentAssessmentEvidence(AssessmentEvidence parentAssessmentEvidence) {
        this.parentAssessmentEvidence = parentAssessmentEvidence;
    }

    public AssessmentEvidence getPrimaryAssessmentEvidence() {
        return primaryAssessmentEvidence;
    }

    public void setPrimaryAssessmentEvidence(AssessmentEvidence primaryAssessmentEvidence) {
        this.primaryAssessmentEvidence = primaryAssessmentEvidence;
    }

    public Condition[] getConditions() {
        return conditions;
    }

    public void setConditions(Condition[] conditions) {
        this.conditions = conditions;
    }
}
