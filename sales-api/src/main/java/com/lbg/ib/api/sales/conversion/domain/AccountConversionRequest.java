/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.conversion.domain;

import java.util.List;
import java.util.Map;

/**
 * Account conversion resource.
 * @author csharma8
 */
public class AccountConversionRequest {

    /**
     * Template name of email
     */
    private String templateName;
    /**
     * Mneominic value of product
     */
    private String productMnemonic;
    /**
     * life style benefit code.
     */
    private String lifeStyleBenefitCode;

    /**
     * life style benefit required.
     */
    private boolean lifeStyleReq;
    /**
     * Request for the card upgrade.
     */
    private boolean cardUpgradeReq;

    private Map<String, String> emailTokens;

    /**
     * It's not customer email Id
     */
    private String groupEmailId;


    private String opsEmail;

    private List<AdditionalPartyDetails> additionalPartyDetailsList;
    
    private String communicationType;
    /**
     * @return the templateName
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * @param templateName the templateName to set
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * @return the productMnemonic
     */
    public String getProductMnemonic() {
        return productMnemonic;
    }

    /**
     * @param productMnemonic the productMnemonic to set
     */
    public void setProductMnemonic(String productMnemonic) {
        this.productMnemonic = productMnemonic;
    }

    /**
     * @return the lifeStyleBenefitCode
     */
    public String getLifeStyleBenefitCode() {
        return lifeStyleBenefitCode;
    }

    /**
     * @param lifeStyleBenefitCode the lifeStyleBenefitCode to set
     */
    public void setLifeStyleBenefitCode(String lifeStyleBenefitCode) {
        this.lifeStyleBenefitCode = lifeStyleBenefitCode;
    }

    /**
     * @return the emailTokens
     */
    public Map<String, String> getEmailTokens() {
        return emailTokens;
    }

    /**
     * @param emailTokens the emailTokens to set
     */
    public void setEmailTokens(Map<String, String> emailTokens) {
        this.emailTokens = emailTokens;
    }

    public String getGroupEmailId() {
        return groupEmailId;
    }

    public void setGroupEmailId(String groupEmailId) {
        this.groupEmailId = groupEmailId;
    }

    /**
     * @return the lifeStyleReq
     */
    public boolean isLifeStyleReq() {
        return lifeStyleReq;
    }

    /**
     * @param lifeStyleReq the lifeStyleReq to set
     */
    public void setLifeStyleReq(boolean lifeStyleReq) {
        this.lifeStyleReq = lifeStyleReq;
    }

    /**
     * @return the cardUpgradeReq
     */
    public boolean isCardUpgradeReq() {
        return cardUpgradeReq;
    }

    /**
     * @param cardUpgradeReq the cardUpgradeReq to set
     */
    public void setCardUpgradeReq(boolean cardUpgradeReq) {
        this.cardUpgradeReq = cardUpgradeReq;
    }

    public String getOpsEmail() {
        return opsEmail;
    }

    public void setOpsEmail(String opsEmail) {
        this.opsEmail = opsEmail;
    }

    public List<AdditionalPartyDetails> getAdditionalPartyDetailsList() {
        return additionalPartyDetailsList;
    }

    public void setAdditionalPartyDetailsList(List<AdditionalPartyDetails> additionalPartyDetailsList) {
        this.additionalPartyDetailsList = additionalPartyDetailsList;
    }

	public String getCommunicationType() {
		return communicationType;
	}

	public void setCommunicationType(String communicationType) {
		this.communicationType = communicationType;
	}
    
    
}
