/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.paperless.dto;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

import java.util.Calendar;

/**
 * DTO to have user mandate information(paperless).
 * @author tkhann
 */
public class UserMandateInfoDTO {
    /**
     * User id of customer.
     */
    private String userid;
    /**
     * Mandate state
     */
    private String mandatestateMax;
    /**
     * IB Active user registration description.
     */
    private String userregstatedesc;
    /**
     * IB Active user registration code.
     */
    private String userregstatecode;
    /**
     * Date Application Submitted.
     */
    private Calendar dateApplicationSubmitted;
    /**
     * Date Application Verified.
     */
    private Calendar dateApplicationVerified;
    /**
     * User date first logon.
     */
    private Calendar dateFirstLogon;
    /**
     * User date Last logon.
     */
    private Calendar dateLastLogon;
    /**
     * BT Transactions On Audit Table.
     */
    private boolean auditTable;
    /**
     * BT Transactions On Audit Table.
     */
    private boolean auditHistTable;
    /**
     * Code for Paperless.
     */
    private String userpaperlesscode;
    /**
     * PwdstateF.
     */
    private String pwdstate;

    /**
     * @return the userid
     */
    public String getUserid() {
        return userid;
    }

    /**
     * @param userid the userid to set
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    /**
     * @return the mandatestateMax
     */
    public String getMandatestateMax() {
        return mandatestateMax;
    }

    /**
     * @param mandatestateMax the mandatestateMax to set
     */
    public void setMandatestateMax(String mandatestateMax) {
        this.mandatestateMax = mandatestateMax;
    }

    /**
     * @return the userregstatedesc
     */
    public String getUserregstatedesc() {
        return userregstatedesc;
    }

    /**
     * @param userregstatedesc the userregstatedesc to set
     */
    public void setUserregstatedesc(String userregstatedesc) {
        this.userregstatedesc = userregstatedesc;
    }

    /**
     * @return the userregstatecode
     */
    public String getUserregstatecode() {
        return userregstatecode;
    }

    /**
     * @param userregstatecode the userregstatecode to set
     */
    public void setUserregstatecode(String userregstatecode) {
        this.userregstatecode = userregstatecode;
    }

    /**
     * @return the dateApplicationSubmitted
     */
    public Calendar getDateApplicationSubmitted() {
        return dateApplicationSubmitted;
    }

    /**
     * @param dateApplicationSubmitted the dateApplicationSubmitted to set
     */
    public void setDateApplicationSubmitted(Calendar dateApplicationSubmitted) {
        this.dateApplicationSubmitted = dateApplicationSubmitted;
    }

    /**
     * @return the dateApplicationVerified
     */
    public Calendar getDateApplicationVerified() {
        return dateApplicationVerified;
    }

    /**
     * @param dateApplicationVerified the dateApplicationVerified to set
     */
    public void setDateApplicationVerified(Calendar dateApplicationVerified) {
        this.dateApplicationVerified = dateApplicationVerified;
    }

    /**
     * @return the dateFirstLogon
     */
    public Calendar getDateFirstLogon() {
        return dateFirstLogon;
    }

    /**
     * @param dateFirstLogon the dateFirstLogon to set
     */
    public void setDateFirstLogon(Calendar dateFirstLogon) {
        this.dateFirstLogon = dateFirstLogon;
    }

    /**
     * @return the dateLastLogon
     */
    public Calendar getDateLastLogon() {
        return dateLastLogon;
    }

    /**
     * @param dateLastLogon the dateLastLogon to set
     */
    public void setDateLastLogon(Calendar dateLastLogon) {
        this.dateLastLogon = dateLastLogon;
    }

    /**
     * @return the userpaperlesscode
     */
    public String getUserpaperlesscode() {
        return userpaperlesscode;
    }

    /**
     * @param userpaperlesscode the userpaperlesscode to set
     */
    public void setUserpaperlesscode(String userpaperlesscode) {
        this.userpaperlesscode = userpaperlesscode;
    }

    /**
     * @return the pwdstate
     */
    public String getPwdstate() {
        return pwdstate;
    }

    /**
     * @param pwdstate the pwdstate to set
     */
    public void setPwdstate(String pwdstate) {
        this.pwdstate = pwdstate;
    }

    /**
     * Override toString Method.
     */
    @Override
    public String toString() {
        return reflectionToString(this);
    }

    /**
     * @return the auditTable
     */
    public boolean isAuditTable() {
        return auditTable;
    }

    /**
     * @param auditTable the auditTable to set
     */
    public void setAuditTable(boolean auditTable) {
        this.auditTable = auditTable;
    }

    /**
     * @return the auditHistTable
     */
    public boolean isAuditHistTable() {
        return auditHistTable;
    }

    /**
     * @param auditHistTable the auditHistTable to set
     */
    public void setAuditHistTable(boolean auditHistTable) {
        this.auditHistTable = auditHistTable;
    }
}