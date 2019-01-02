
package com.lbg.ib.api.sales.dto.device;

import java.util.Map;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class ThreatMatrixDTO {
    private String deviceId;

    private String smartDeviceId;

    private String smartDeviceIdConfidence;

    private String trueIp;

    private String accountLogin;

    private String summartRiskScore;

    private String summaryReasonCode;

    private String policyScore;

    private String reasonCode;

    private String riskRating;

    private String reviewStatus;

    private String deviceFirstSeen;

    private String devicelastSeen;

    private String trueIpGeo;

    private String trueIpIsp;

    private String trueIpOrgnaisation;

    private String proxyIpGeo;

    private String dnsIpGeo;

    private String browserLanguage;

    public ThreatMatrixDTO(String deviceId, String smartDeviceId, String smartDeviceIdConfidence, String trueIp,
            String accountLogin, String summartRiskScore, String summaryReasonCode, String policyScore,
            String reasonCode, String riskRating, String reviewStatus, String deviceFirstSeen, String devicelastSeen,
            String trueIpGeo, String trueIpIsp, String trueIpOrgnaisation, String proxyIpGeo, String dnsIpGeo,
            String browserLanguage) {
        super();
        this.deviceId = deviceId;
        this.smartDeviceId = smartDeviceId;
        this.smartDeviceIdConfidence = smartDeviceIdConfidence;
        this.trueIp = trueIp;
        this.accountLogin = accountLogin;
        this.summartRiskScore = summartRiskScore;
        this.summaryReasonCode = summaryReasonCode;
        this.policyScore = policyScore;
        this.reasonCode = reasonCode;
        this.riskRating = riskRating;
        this.reviewStatus = reviewStatus;
        this.deviceFirstSeen = deviceFirstSeen;
        this.devicelastSeen = devicelastSeen;
        this.trueIpGeo = trueIpGeo;
        this.trueIpIsp = trueIpIsp;
        this.trueIpOrgnaisation = trueIpOrgnaisation;
        this.proxyIpGeo = proxyIpGeo;
        this.dnsIpGeo = dnsIpGeo;
        this.browserLanguage = browserLanguage;
    }

    public ThreatMatrixDTO(Map<String, String> map) {
        this(map.get("device_id"), map.get("fuzzy_device_id"), map.get("fuzzy_device_id_confidence"),
                map.get("true_ip"), map.get("account_login"), map.get("summary_risk_score"),
                map.get("summary_reason_code"), map.get("policy_score"), map.get("reason_code"), map.get("risk_rating"),
                map.get("review_status"), map.get("device_first_seen"), map.get("device_last_event"),
                map.get("true_ip_geo"), map.get("true_ip_isp"), map.get("true_ip_organization"),
                map.get("proxy_ip_geo"), map.get("dns_ip_geo"), map.get("browser_language"));
    }

    public ThreatMatrixDTO() {
        super();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getSmartDeviceId() {
        return smartDeviceId;
    }

    public String getSmartDeviceIdConfidence() {
        return smartDeviceIdConfidence;
    }

    public String getTrueIp() {
        return trueIp;
    }

    public String getAccountLogin() {
        return accountLogin;
    }

    public String getSummartRiskScore() {
        return summartRiskScore;
    }

    public String getSummaryReasonCode() {
        return summaryReasonCode;
    }

    public String getPolicyScore() {
        return policyScore;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public String getRiskRating() {
        return riskRating;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }

    public String getDeviceFirstSeen() {
        return deviceFirstSeen;
    }

    public String getDevicelastSeen() {
        return devicelastSeen;
    }

    public String getTrueIpGeo() {
        return trueIpGeo;
    }

    public String getTrueIpIsp() {
        return trueIpIsp;
    }

    public String getTrueIpOrgnaisation() {
        return trueIpOrgnaisation;
    }

    public String getProxyIpGeo() {
        return proxyIpGeo;
    }

    public String getDnsIpGeo() {
        return dnsIpGeo;
    }

    public String getBrowserLanguage() {
        return browserLanguage;
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }
}
