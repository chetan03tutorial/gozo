/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.paperless.dto;

/**
 * User mandate info for paperless.
 * 
 * @author tkhann
 */
public class UserMandateInfoResult {
    /**
     * User Registration State Code.
     */
    private String userRegStateCode;
    /**
     * User Registration State Description.
     */
    private String userRegStateDesc;
    /**
     * User date first Logon.
     */
    private String dateFirstLogon;
    /**
     * User date last Logon.
     */
    private String dateLastLogon;
    /**
     * User Last login in Mins.
     */
    private long   lastLoginInMins;

    /**
     * @return the userRegStateCode
     */
    public String getUserRegStateCode() {
        return userRegStateCode;
    }

    /**
     * @param userRegStateCode
     *            the userRegStateCode to set
     */
    public void setUserRegStateCode(String userRegStateCode) {
        this.userRegStateCode = userRegStateCode;
    }

    /**
     * @return the userRegStateDesc
     */
    public String getUserRegStateDesc() {
        return userRegStateDesc;
    }

    /**
     * @param userRegStateDesc
     *            the userRegStateDesc to set
     */
    public void setUserRegStateDesc(String userRegStateDesc) {
        this.userRegStateDesc = userRegStateDesc;
    }

    /**
     * @return the dateFirstLogon
     */
    public String getDateFirstLogon() {
        return dateFirstLogon;
    }

    /**
     * @param dateFirstLogon
     *            the dateFirstLogon to set
     */
    public void setDateFirstLogon(String dateFirstLogon) {
        this.dateFirstLogon = dateFirstLogon;
    }

    /**
     * @return the dateLastLogon
     */
    public String getDateLastLogon() {
        return dateLastLogon;
    }

    /**
     * @param dateLastLogon
     *            the dateLastLogon to set
     */
    public void setDateLastLogon(String dateLastLogon) {
        this.dateLastLogon = dateLastLogon;
    }

    /**
     * @return the lastLoginInMins
     */
    public long getLastLoginInMins() {
        return lastLoginInMins;
    }

    /**
     * @param lastLoginInMins
     *            the lastLoginInMins to set
     */
    public void setLastLoginInMins(long lastLoginInMins) {
        this.lastLoginInMins = lastLoginInMins;
    }

}