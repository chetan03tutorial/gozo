/**
 * 
 */
package com.lbg.ib.api.sales.common.session.dto;

/**
 * @author ssama1
 */
public class ArrangeToOfferParameters {

    private String arrangementId;

    private String applicationStatus;

    private String individualIdentifier;

    /**
     * @return the applicationStatus
     */
    public String getApplicationStatus() {
        return applicationStatus;
    }

    /**
     * @return the arrangementId
     */
    public String getArrangementId() {
        return arrangementId;
    }

    /**
     * @return the individualIdentifier
     */
    public String getIndividualIdentifier() {
        return individualIdentifier;
    }

    /**
     * @param applicationStatus
     *            the applicationStatus to set
     */
    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    /**
     * @param arrangementId
     *            the arrangementId to set
     */
    public void setArrangementId(String arrangementId) {
        this.arrangementId = arrangementId;
    }

    /**
     * @param individualIdentifier
     *            the individualIdentifier to set
     */
    public void setIndividualIdentifier(String individualIdentifier) {
        this.individualIdentifier = individualIdentifier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ArrangeToOfferParameters [arrangementId=" + arrangementId + ", applicationStatus=" + applicationStatus
                + ", individualIdentifier=" + individualIdentifier + "]";
    }

}
