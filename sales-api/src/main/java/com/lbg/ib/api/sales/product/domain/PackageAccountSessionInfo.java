package com.lbg.ib.api.sales.product.domain;

import com.lbg.ib.api.sales.product.domain.arrangement.Arranged;
import com.lbg.ib.api.sales.product.domain.arrangement.Arrangement;
import com.lbg.ib.api.sales.product.domain.domains.PldAppeal;

/**
 * This class is the container for the session information required for the
 * package journey.
 * @author 8903735
 *
 */
public class PackageAccountSessionInfo {
    
    private Arrangement offerRequest;
    
    private Arranged offerResponse;
    
    private String environmentName;

    private com.lbg.ib.api.sso.domain.user.Arrangement userInfo;

    PldAppeal pldAppeal;

    public PldAppeal getPldAppeal() {
        if(this.pldAppeal==null){
            this.pldAppeal = new PldAppeal();
        }

        return this.pldAppeal;
    }

    public void setPldAppeal(PldAppeal pldAppeal) {
        this.pldAppeal = pldAppeal;
    }

    /**
     * @return the offerRequest
     */
    public Arrangement getOfferRequest() {
        return offerRequest;
    }

    /**
     * @param offerRequest the offerRequest to set
     */
    public void setOfferRequest(Arrangement offerRequest) {
        this.offerRequest = offerRequest;
    }

    /**
     * @return the offerResponse
     */
    public Arranged getOfferResponse() {
        return offerResponse;
    }

    /**
     * @param offerResponse the offerResponse to set
     */
    public void setOfferResponse(Arranged offerResponse) {
        this.offerResponse = offerResponse;
    }

    /**
     * @return the environmentName
     */
    public String getEnvironmentName() {
        return environmentName;
    }

    /**
     * @param environmentName the environmentName to set
     */
    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }


    public com.lbg.ib.api.sso.domain.user.Arrangement getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(com.lbg.ib.api.sso.domain.user.Arrangement userInfo) {
        this.userInfo = userInfo;
    }
    
}
