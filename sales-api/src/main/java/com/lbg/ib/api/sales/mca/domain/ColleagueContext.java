/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.mca.domain;

public class ColleagueContext {

    private String  colleagueId;
    private String  domain;
    private boolean isAuthorized;

    public ColleagueContext(String colleagueId, boolean isAuthorized) {
        this.colleagueId = colleagueId;
        this.isAuthorized = isAuthorized;
    }

    public String getColleagueId() {
        return colleagueId;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String toString() {
        return "ColleagueContext [colleagueId=" + colleagueId + ", isAuthorized=" + isAuthorized + " ,domain = "
                + domain + "]";
    }

}
