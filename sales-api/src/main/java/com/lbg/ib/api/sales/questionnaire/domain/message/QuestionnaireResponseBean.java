package com.lbg.ib.api.sales.questionnaire.domain.message;

public class QuestionnaireResponseBean {

    private int    reasonCode;
    private String reasonText;

    public int getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(int reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getReasonText() {
        return reasonText;
    }

    public void setReasonText(String reasonText) {
        this.reasonText = reasonText;
    }

}
