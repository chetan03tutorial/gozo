package com.lbg.ib.api.sales.product.domain.domains;

import com.lbg.ib.api.shared.validation.RequiredFieldValidation;

public class TerminateArrangement {
    
    @RequiredFieldValidation
    String arrangementId;
    @RequiredFieldValidation
    String userId;
    @RequiredFieldValidation
    String action;
    @RequiredFieldValidation
    String reasonCode;
    
    public TerminateArrangement(){
      //Empty Constructor to avoid Sonar Violations
    }
    public TerminateArrangement(String arrangementId,String userId,String action, String reasonCode){
        setArrangementId(arrangementId);
        setUserId(userId);
        setAction(action);
        setReasonCode(reasonCode);
    }
    
    public String getArrangementId() {
        return arrangementId;
    }
    
    public void setArrangementId(String arrangementId) {
        this.arrangementId = arrangementId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public String getReasonCode() {
        return reasonCode;
    }
    
    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }
    
}
