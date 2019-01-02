package com.lbg.ib.api.sales.overdraft.domain;


public class E170Response extends OverdraftManagementResponse{
    private String featureNextReviewFlagDate ;
    private String featureNextReviewDate;
    public String getFeatureNextReviewFlagDate() {
        return featureNextReviewFlagDate;
    }
    public void setFeatureNextReviewFlagDate(String featureNextReviewFlagDate) {
        this.featureNextReviewFlagDate = featureNextReviewFlagDate;
    }
    public String getFeatureNextReviewDate() {
        return featureNextReviewDate;
    }
    public void setFeatureNextReviewDate(String featureNextReviewDate) {
        this.featureNextReviewDate = featureNextReviewDate;
    }
}
