package com.lbg.ib.api.sales.header.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.header.mapper.AbstractLBGHeaderMapper;
import com.lbg.ib.api.sales.header.mapper.BranchHeaderMapper;
import com.lbg.ib.api.sales.header.mapper.DigitalHeaderMapper;
import com.lbg.ib.api.sales.shared.context.RequestContext;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lloydstsb.ea.logging.constants.ApplicationAttribute;

public abstract class AbstractBaseHeader implements SoapHeaderBuilder {

    @Autowired
    protected SessionManagementDAO sessionManager;
    @Autowired
    protected LoggerDAO logger;
    @Autowired
    private ModuleContext beanLoader;
    
    static final String ORIGINATOR_TYPE = "Browser";
    static final String OPERATOR_TYPE = "Customer";
    static final String CONTACT_POINT_TYPE = "003";
    static final String CONTACT_POINT_ID = "0000777505";
    static final String APPLICATION_ID = "Internet Banking";
    static final String TELEPHONY_VALUE = "TELEPHONY";
    static final String UNP_MECH_TYPE = "IB";
    static final String USER_TYPE = "010";
    static final String ID = "LloydsTSBSecurityToken";
    static final String UNDEFINED = "<Un-defined>";
    static final String DOTS = "...";
    

    public void registerHandler(Set<AbstractBaseHeader> headerSet) {
        headerSet.add(this);
    }

    public AbstractLBGHeaderMapper getLBGHeaderBuilder() {
        AbstractLBGHeaderMapper lbgHeaderBuilder = null;
        if (null != sessionManager.getBranchContext()) {
            lbgHeaderBuilder = beanLoader.getService(BranchHeaderMapper.class);
        } else {
            lbgHeaderBuilder = beanLoader.getService(DigitalHeaderMapper.class);
        }
        return lbgHeaderBuilder;
    }

    public static String toSoapValueFromArc(String attribute) {
        return toSoapValue((String) RequestContext.getInRequestContext(attribute));
    }

    public static String toSoapValueFromArc(ApplicationAttribute attribute) {
        return toSoapValue((String) RequestContext.getInRequestContext(attribute));
    }

    public static String toSoapValue(String value) {
        if (value == null) {
            return "";
        }
        String trimmed = value.trim();
        if (trimmed.length() == 0) {
            return "";
        }
        if ("...".equals(trimmed)) {
            return "";
        }
        if ("<Un-defined>".equals(trimmed)) {
            return "";
        } else {
            return value;
        }
    }

    public static String toSoapValue(boolean value) {
        return String.valueOf(value);
    }

    public static String withDefault(String value, String defaultValue) {
        return value.length() == 0 ? defaultValue : value;
    }

    public static String mandatory(String value, String description) {
        if (value.length() == 0) {
            IllegalArgumentException argumentException = new IllegalArgumentException(
                    (new StringBuilder()).append(description).append(" in BAPI Header cannot be null").toString());
            throw argumentException;
        } else {
            return value;
        }
    }

    public static String getDefaultValueForOverride(String key) {
        String defaultValue = null;
        String mangerOverrideFlag = getValueFromColleagueMapFromARC("MgrAuthReqd");
        String tellerOverrideFlag = getValueFromColleagueMapFromARC("TlrOverride");
        if (null != tellerOverrideFlag && tellerOverrideFlag.equalsIgnoreCase("1")
                && (null == mangerOverrideFlag || mangerOverrideFlag.equalsIgnoreCase("0"))) {
            defaultValue = setDefaultValueForTellerOverride(key);
        } else {
            defaultValue = setDefaultValueForManagerOverride(key);
        }
        return defaultValue;
    }

    private static String getValueFromColleagueMapFromARC(String key) {
        Object colleagueContextObj = RequestContext.getInRequestContext("ColleagueContext");
        String value = null;
        if (colleagueContextObj != null && !"...".equals(colleagueContextObj.toString())) {
            Map<String, String> colleagueContextMap = (HashMap<String, String>) colleagueContextObj;
            if (!colleagueContextMap.isEmpty())
                value = colleagueContextMap.get(key);
        }
        return value;
    }

    private static String setDefaultValueForTellerOverride(String keyValue) {
        if ("ManagerID".equalsIgnoreCase(keyValue)) {
            return getValueFromColleagueMapFromARC("ColleagueId");
        }
        if ("InputOfficerStatusFlag".equalsIgnoreCase(keyValue)) {
            return "0";
        }
        if ("InputOfficerStatusCode".equalsIgnoreCase(keyValue)) {
            return "0";
        }
        if ("OverrideDetailsCode".equalsIgnoreCase(keyValue)) {
            return "2";
        }
        if ("SkillLevelAccuired".equalsIgnoreCase(keyValue)) {
            return "1";
        } else {
            return null;
        }
    }

    private static String setDefaultValueForManagerOverride(String keyValue) {
        if ("InputOfficerStatusFlag".equalsIgnoreCase(keyValue)) {
            return "0";
        }
        if ("InputOfficerStatusCode".equalsIgnoreCase(keyValue)) {
            return "0";
        }
        if ("OverrideDetailsCode".equalsIgnoreCase(keyValue)) {
            return "1";
        }
        if ("SkillLevelAccuired".equalsIgnoreCase(keyValue)) {
            return "0";
        } else {
            return null;
        }
    }

    public static String getValueFromARC(String key) {
        Object valueObj = RequestContext.getInRequestContext(key);
        String value = null;
        if (null != valueObj && !"...".equals(valueObj.toString())) {
            value = valueObj.toString();
        }
        return value;
    }

    public static void setNullValInARC(String key, String value) {
        RequestContext.setInRequestContext(key, value);
    }

}
