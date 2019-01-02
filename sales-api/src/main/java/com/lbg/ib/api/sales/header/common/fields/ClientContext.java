package com.lbg.ib.api.sales.header.common.fields;

import java.util.HashMap;
import java.util.Map;

public class ClientContext {

    public static final String KEY_SESSION_CLIENT_CONTEXT = "clientContext";
    private String channelId;
    private String partyId;
    private String osisId;
    private String chansecmode;
    private String sessionId;
    private String ipAddress;
    private String language;
    private String userAgent;
    private String host;
    private String inboxIdClient;
    private String userId;
    private String lastErrorId;
    private Object lastErrorMsg;
    private Map colleagueMap;
    private String ocisIdCommercial;

    public ClientContext() {
        colleagueMap = new HashMap();
    }

    public String toString() {
        StringBuilder retVal = new StringBuilder();
        retVal.append("User Id: ");
        retVal.append(getUserId());
        retVal.append(", Session Id: ");
        retVal.append(getSessionId());
        retVal.append(", Error Id: ");
        retVal.append(getLastErrorId());
        retVal.append(", Message details: ");
        retVal.append(lastErrorMsg);
        return retVal.toString();
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getOsisId() {
        return osisId;
    }

    public void setOsisId(String osisId) {
        this.osisId = osisId;
    }

    public String getChansecmode() {
        return chansecmode;
    }

    public void setChansecmode(String chansecmode) {
        this.chansecmode = chansecmode;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getInboxIdClient() {
        return inboxIdClient;
    }

    public void setInboxIdClient(String inboxIdClient) {
        this.inboxIdClient = inboxIdClient;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastErrorId() {
        return lastErrorId;
    }

    public void setLastErrorId(String lastErrorId) {
        this.lastErrorId = lastErrorId;
    }

    public Object getLastErrorMsg() {
        return lastErrorMsg;
    }

    public void setLastErrorMsg(Object lastErrorMsg) {
        this.lastErrorMsg = lastErrorMsg;
    }

    public Map getColleagueMap() {
        return colleagueMap;
    }

    public void setColleagueMap(Map colleagueMap) {
        this.colleagueMap = colleagueMap;
    }

    public String getOcisIdCommercial() {
        return ocisIdCommercial;
    }

    public void setOcisIdCommercial(String anOcisId) {
        ocisIdCommercial = anOcisId;
    }

}
