/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.dao.session;

import com.lbg.ib.api.sales.common.session.UserContextService;
import com.lbg.ib.api.sales.common.session.dto.ArrangeToOfferParameters;
import com.lbg.ib.api.sales.common.session.dto.CustomerInfo;
import com.lbg.ib.api.sales.dto.communicationparty.CommunicationPartyDetailsDTO;
import com.lbg.ib.api.sales.dto.party.TriadResultDTO;
import com.lbg.ib.api.sales.product.domain.ArrangeToActivateParameters;
import com.lbg.ib.api.sales.product.domain.PackageAccountSessionInfo;
import com.lbg.ib.api.sales.product.domain.SelectedProduct;
import com.lbg.ib.api.sales.product.domain.eligibility.UpgradeOption;
import com.lbg.ib.api.sales.shared.constants.SharedConstants;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.DirectDebit;
import com.lbg.ib.api.sales.user.domain.AddParty;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.sales.user.domain.SelectedAccount;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lloydstsb.ea.logging.constants.ApplicationAttribute;
import com.lloydstsb.ea.logging.helper.ApplicationRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.lbg.ib.api.sales.dao.constants.CommonConstant.*;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved. The ApplicationSessionManagement is the implementation
 * of the SessionManagementDAO.
 *
 * @author Debashish Bhattacharjee
 * @version 1.0
 * @since 14thFeb2016
 ***********************************************************************/
@Component
public class ApplicationSessionManagement implements SessionManagementDAO {

    private static String MESSAGE_ID = "...";
    private HttpServletRequest request;

    private ChannelBrandingDAO channelBrandingDAO;
    private ConfigurationDAO configurationDAO;
    private LoggerDAO logger;

    private final Lock lock = new ReentrantLock();

    @Autowired
    public ApplicationSessionManagement(@Context HttpServletRequest request,ChannelBrandingDAO channelBrandingDAO,ConfigurationDAO configurationDAO,LoggerDAO logger) {
        this.request = request;
        this.channelBrandingDAO = channelBrandingDAO;
        this.configurationDAO = configurationDAO;
        this.logger = logger;
    }

    @TraceLog
    public void setSelectedProduct(SelectedProduct product) {
        setAttributeToSession(SESSION_KEY_FOR_PRODUCT_DETAILS, product);
    }

    @TraceLog
    public SelectedProduct getSelectedProduct() {
        return getAttributeFromSession(SESSION_KEY_FOR_PRODUCT_DETAILS, SelectedProduct.class);
    }

    @TraceLog
    public void setArrangeToActivateParameters(ArrangeToActivateParameters params) {
        setAttributeToSession(SESSION_KEY_FOR_ARRANGED, params);
    }

    @TraceLog
    public ArrangeToActivateParameters getArrangeToActivateParameters() {
        return getAttributeFromSession(SESSION_KEY_FOR_ARRANGED, ArrangeToActivateParameters.class);
    }

    @TraceLog
    public String getSessionId() {
        return request.getSession(false).getId();
    }

    @TraceLog
    public UserContext getUserContext() {

        if (getSessionHandle(false) != null && getAttributeFromSession(KEY_USER_CONTEXT, UserContext.class) != null) {
            return getAttributeFromSession(KEY_USER_CONTEXT, UserContext.class);
        } else {
            setSessionIdinARC();
            UserContext userContext = new UserContextService(this, channelBrandingDAO, configurationDAO)
                    .userContext("UNAUTHSALE");
            setAttributeToSession(KEY_USER_CONTEXT, userContext);
            return userContext;
        }
    }

    private void setSessionIdinARC() {
        if (ApplicationRequestContext.get(ApplicationAttribute.SESSIONID) == null
                || ApplicationRequestContext.get(ApplicationAttribute.SESSIONID).equals(MESSAGE_ID)) {
            ApplicationRequestContext.set(ApplicationAttribute.SESSIONID, getSessionId());
        }

    }

    @TraceLog
    public void setSendCommunicationPartyDetails(CommunicationPartyDetailsDTO partyDetails) {
        setAttributeToSession(SESSION_KEY_FOR_COMMUNICATION_PARTY_DETAILS, partyDetails);
    }

    @TraceLog
    public CommunicationPartyDetailsDTO getSendCommunicationPartyDetails() {
        return getAttributeFromSession(SESSION_KEY_FOR_COMMUNICATION_PARTY_DETAILS, CommunicationPartyDetailsDTO.class);
    }

    public void setOcisPartyIdInUserContext(String ocisId, String partyId) {
        UserContext userContext = getUserContext();
        userContext.setOcisId(ocisId);
        userContext.setPartyId(partyId);
        setAttributeToSession(KEY_USER_CONTEXT, userContext);
    }

    /*
     * (non-Javadoc)
     *
     * @see SessionManagementDAO#setUserContext(com.lloydstsb.ea.context.
     * ClientContext) this will set client context in session
     */

    /**
     * @param forceCreate set it to true if there is not current session a new one
     *                    is created and returned
     * @return httpSession handle
     */
    private HttpSession getSessionHandle(boolean forceCreate) {
        return request.getSession(forceCreate);

    }

    /**
     * @param key the name of the attribute
     * @return prepend the key with name space and returns it
     */
    public static String buildNameSpaceKey(String key) {
        StringBuilder sb = new StringBuilder(SESSION_NAMESPACE);
        sb.append(key);
        return (sb.toString());
    }

    /**
     * This method will push object to session, controlled with a lock
     *
     * @param key name of the attribute to be set in the session
     * @param obj the item/object to be stored in the session
     */
    private void setAttributeToSession(String key, Object obj) {
        lock.lock();
        try {
            getSessionHandle(false).setAttribute(buildNameSpaceKey(key), obj);
        } catch (Exception ex) {
        logger.traceLog(this.getClass(), "Number of active threads: " + Thread.activeCount());
        logger.traceLog(this.getClass(), "Exception : " + ex);
    } finally {
        lock.unlock();
    }
    }

    private <T> T getAttributeFromSession(String key, Class<T> clazz) {
        Object attribute = null;
        lock.lock();
        try {
            attribute = getSessionHandle(false).getAttribute(buildNameSpaceKey(key));
        } catch (Exception ex) {
            logger.traceLog(this.getClass(), "Number of active threads: " + Thread.activeCount());
            logger.traceLog(this.getClass(), "Exception : " + ex);
        } finally {
            lock.unlock();
        }
        return returnAttribute(attribute);
    }

    private <T> T returnAttribute(Object attribute) {
        if(null != attribute) {
            return (T) attribute;
        }else{
            return null;
        }
    }

    @TraceLog
    public void clearSessionAttributeForPipelineChasing() {
        if (getSessionHandle(false) != null) {
            lock.lock();
            try {
                getSessionHandle(false).removeAttribute(buildNameSpaceKey(SESSION_KEY_FOR_COMMUNICATION_PARTY_DETAILS));
                getSessionHandle(false).removeAttribute(buildNameSpaceKey(SESSION_KEY_FOR_ARRANGED));
                getSessionHandle(false).removeAttribute(buildNameSpaceKey(SESSION_KEY_FOR_OFFER_ARRANGED));
                getSessionHandle(false).removeAttribute(buildNameSpaceKey(SESSION_KEY_FOR_PRODUCT_DETAILS));
                getSessionHandle(false).removeAttribute(buildNameSpaceKey(SESSION_KEY_FOR_USER_INFO));
                getSessionHandle(false).removeAttribute(buildNameSpaceKey(SESSION_KEY_FOR_SELECTED_ACCOUNT));
                getSessionHandle(false).removeAttribute(buildNameSpaceKey(SESSION_KEY_EMAIL_RETRY_COUNT));
                getSessionHandle(false).removeAttribute(buildNameSpaceKey(SESSION_KEY_ALL_PARTY_DETAILS_SESSION_INFO));
                getSessionHandle(false).removeAttribute(buildNameSpaceKey(SESSION_KEY_DEMANDED_OD));
            } catch (Exception ex) {
                logger.traceLog(this.getClass(), "Number of active threads: " + Thread.activeCount());
                logger.traceLog(this.getClass(), "Exception : " + ex);
            }finally {
                lock.unlock();
            }
        }

    }

    private void setSessionIdInARC() {
        if (ApplicationRequestContext.get(ApplicationAttribute.SESSIONID) == null
                || ApplicationRequestContext.get(ApplicationAttribute.SESSIONID).equals(MESSAGE_ID)) {
            ApplicationRequestContext.set(ApplicationAttribute.SESSIONID, getSessionId());
        }
    }

    public void setUserInfo(Arrangement userInfo) {
        setAttributeToSession(SESSION_KEY_FOR_USER_INFO, userInfo);
    }

    public Arrangement getUserInfoArrangement() {
        return getAttributeFromSession(SESSION_KEY_FOR_USER_INFO, Arrangement.class);

    }

    public Arrangement getUserInfo() {
        return getAttributeFromSession(SESSION_KEY_FOR_USER_INFO, Arrangement.class);
    }

    public ArrangeToOfferParameters getArrangeToOfferParameters() {
        return getAttributeFromSession(SESSION_KEY_FOR_OFFER_ARRANGED, ArrangeToOfferParameters.class);

    }

    public void setArrangeToOfferParameters(ArrangeToOfferParameters offerArrangement) {
        setAttributeToSession(SESSION_KEY_FOR_OFFER_ARRANGED, offerArrangement);

    }

    public void setBranchContext(BranchContext branchContext) {
        setAttributeToSession(SESSION_KEY_FOR_BRANCH_CONTEXT, branchContext);
    }

    public BranchContext getBranchContext() {
        return getAttributeFromSession(SESSION_KEY_FOR_BRANCH_CONTEXT, BranchContext.class);
    }

    public CustomerInfo getCustomerDetails() {
        return getAttributeFromSession(SESSION_KEY_FOR_CUSTOMER_DETAILS, CustomerInfo.class);
    }

    public void setCustomerDetails(CustomerInfo cusInfo) {
        setAttributeToSession(SESSION_KEY_FOR_CUSTOMER_DETAILS, cusInfo);
    }

    public CustomerInfo getRelatedCustomerDetails() {
        return getAttributeFromSession(SESSION_KEY_FOR_RELATED_CUSTOMER_DETAILS, CustomerInfo.class);
    }

    public void setRelatedCustomerDetails(CustomerInfo cusInfo) {
        setAttributeToSession(SESSION_KEY_FOR_RELATED_CUSTOMER_DETAILS, cusInfo);
    }

    public void setSwitchingDetails(DirectDebit directDebit) {
        setAttributeToSession(SESSION_KEY_FOR_SWITCHING, directDebit);
    }

    public DirectDebit getSwitchingDetails() {
        return getAttributeFromSession(SESSION_KEY_FOR_SWITCHING, DirectDebit.class);
    }

    public void setAddPartyContext(AddParty addParty) {
        setAttributeToSession(SESSION_KEY_FOR_ADDPARTY, addParty);
    }

    public AddParty getAddPartyContext() {
        return getAttributeFromSession(SESSION_KEY_FOR_ADDPARTY, AddParty.class);
    }

    public void setPackagedAccountSessionInfo(PackageAccountSessionInfo packageAccountSessionInfo) {
        setAttributeToSession(SESSION_KEY_PAB_SESSION_INFO, packageAccountSessionInfo);
    }

    public PackageAccountSessionInfo getPackagedAccountSessionInfo() {
        return getAttributeFromSession(SESSION_KEY_PAB_SESSION_INFO, PackageAccountSessionInfo.class);
    }

    public void setAllPartyDetailsSessionInfo(Map<String, PartyDetails> allPartyDetails) {
        setAttributeToSession(SESSION_KEY_ALL_PARTY_DETAILS_SESSION_INFO, allPartyDetails);
    }

    public Map<String, PartyDetails> getAllPartyDetailsSessionInfo() {
        return getAttributeFromSession(SESSION_KEY_ALL_PARTY_DETAILS_SESSION_INFO, Map.class);
    }

    public void setAvailableUpgradeOptions(Map<String, UpgradeOption> availableUpgradeOptions) {
        setAttributeToSession(SESSION_KEY_AVAILABLE_UPGRADE_OPTIONS_SESSION_INFO, availableUpgradeOptions);
    }

    public Map<String, UpgradeOption> getAvailableUpgradeOptions() {
        return getAttributeFromSession(SESSION_KEY_AVAILABLE_UPGRADE_OPTIONS_SESSION_INFO, Map.class);
    }

    public void setAccountToConvertInContext(SelectedAccount account) {
        setAttributeToSession(SESSION_KEY_FOR_SELECTED_ACCOUNT, account);
    }

    public SelectedAccount getAccountToConvertInContext() {
        return getAttributeFromSession(SESSION_KEY_FOR_SELECTED_ACCOUNT, SelectedAccount.class);
    }

    public void setTriadDetails(TriadResultDTO resultDTO) {
        setAttributeToSession(TRIAD_SESSION_INFO, resultDTO);

    }

    public TriadResultDTO getTriadDetails() {
        return getAttributeFromSession(TRIAD_SESSION_INFO, TriadResultDTO.class);
    }

    public void setMaxOverDraftLimit(String maxOverDraftLimit) {
        setAttributeToSession(MAX_OVERDRAFT_LIMIT, maxOverDraftLimit);

    }

    public String getMaxOverDraftLimit() {
        return getAttributeFromSession(MAX_OVERDRAFT_LIMIT, String.class);
    }

    public void setInUserMap(Object key, Object value) {
        ConcurrentHashMap<Object, Object> userContextMap = getAttributeFromSession(USER_MAP, ConcurrentHashMap.class);
        if (userContextMap == null) {
            userContextMap = new ConcurrentHashMap<Object, Object>();
        }
        userContextMap.put(key, value);
        setAttributeToSession(USER_MAP, userContextMap);
    }

    public Object getFromUserMap(Object key) {
        ConcurrentHashMap<Object, Object> userContextMap = getAttributeFromSession(USER_MAP, ConcurrentHashMap.class);
        if (userContextMap == null) {
            return null;
        }
        return userContextMap.get(key);
    }

    public void setPrimaryPartyOcisIdMap(final Map<String, String> primaryPartyOcsiIdMap) {
        setAttributeToSession(SESSION_KEY_PRIMARY_PARTY_OCIS_ID_MAP, primaryPartyOcsiIdMap);
    }

    public Map<String, String> getPrimaryPartyOcisIdMap() {
        return getAttributeFromSession(SESSION_KEY_PRIMARY_PARTY_OCIS_ID_MAP, Map.class);
    }

    public void setEmailRetryCount(AtomicInteger retryCount) {
        setAttributeToSession(SESSION_KEY_EMAIL_RETRY_COUNT, retryCount);
    }

    public AtomicInteger getEmailRetryCount() {
        return getAttributeFromSession(SESSION_KEY_EMAIL_RETRY_COUNT, AtomicInteger.class);
    }

    public String getTraceRequestFlag() {
        return getAttributeFromSession(SharedConstants.ENABLE_LOGGING, String.class);
    }

    public void setTraceRequestFlag(String value) {
        setAttributeToSession(SharedConstants.ENABLE_LOGGING, value);
    }

	public String getPartyCity() {
		return getAttributeFromSession(CITY_NAME, String.class);
	}

	public void setPartyCity(String cityName) {
		setAttributeToSession(CITY_NAME, cityName);
	}

    public Double getDemandedOD() {
        return getAttributeFromSession(SESSION_KEY_DEMANDED_OD, Double.class);
    }

    public void setDemandedOD(Double demandedOD) {
        setAttributeToSession(SESSION_KEY_DEMANDED_OD, demandedOD);
    }
}
