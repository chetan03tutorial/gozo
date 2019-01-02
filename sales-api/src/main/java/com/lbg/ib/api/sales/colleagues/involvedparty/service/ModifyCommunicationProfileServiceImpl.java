/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.colleagues.involvedparty.service;

import static com.lbg.ib.api.sales.colleagues.involvedparty.mapper.ModifyCommProfileMessageMapper.createModifyCommunicationProfilesRequest;

import javax.xml.namespace.QName;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.paperless.dto.UserPreferences;
import com.lbg.ib.api.sales.shared.service.SoaAbstractService;
import com.lbg.ib.api.sales.user.service.PartyDetailServiceImpl;
import com.lloydstsb.ea.context.ClientContext;
import com.lloydstsb.ea.infrastructure.soap.ResultCondition;
import com.lloydstsb.lcsm.arrangement.Arrangement;
import com.lloydstsb.lcsm.common.Condition;
import com.lloydstsb.lcsm.common.RuleCondition;
import com.lloydstsb.lcsm.involvedparty.ContactPreference;
import com.lloydstsb.lcsm.involvedparty.InvolvedPartyRole;
import com.lloydstsb.lcsm.involvedpartymanagement.InvolvedPartyManagement;
import com.lloydstsb.lcsm.involvedpartymanagement.InvolvedPartyManagementServiceLocator;
import com.lloydstsb.lcsm.involvedpartymanagement.ModifyCommunicationProfilesResponse;

/**
 * Implementation of communication profiles.
 * @author tkhann
 */
@Component
public class ModifyCommunicationProfileServiceImpl extends SoaAbstractService
        implements ModifyCommunicationProfileService {
    /**
     * Object of session management.
     */
    @Autowired
    private SessionManagementDAO session;
    /**
     * Object of Service locator.
     */
    @Autowired
    @Qualifier("involvedPartyManagementServicingLocatorFactory")
    private InvolvedPartyManagementServiceLocator serviceLocator;
    /**
     * Object of management.
     */
    @Autowired
    @Qualifier("involvedPartyManagementServicing")
    private InvolvedPartyManagement involvedPartyManagement;

    /**
     * Object of ChannelBranding.
     */
    @Autowired
    private ChannelBrandingDAO channelService;

    /**
     * Object of Galaxy Error Code.
     */
    @Autowired
    private GalaxyErrorCodeResolver resolver;

    /**
     * Object of Logger.
     */
    @Autowired
    private LoggerDAO logger;

    public static final String EXTERNAL_SERVICE_UNAVAILABLE = "600140";
    public static final String EXTERNAL_SERVICE_UNAVAILABLE_MESSAGE = "Remote Connection | Error while invoking external(InvolvedPartyManagementServicing) service";

    /*
     * (non-Javadoc)
     * @see com.lbg.ib.api.sales.colleagues.involvedparty.service.
     * ModifyCommunicationProfileService#
     * updatePreferences(com.lbg.ib.api.sales.paperless.dto.UserPreferences, java.lang.String,
     * java.lang.String)
     */
    @TraceLog
    public void updatePreferences(UserPreferences preferences, String ocisId, String partyId) {
        String methodName = "ModifyCommunicationProfileServiceImpl: updatePreferences";
        logger.traceLog(this.getClass(), methodName + "OcisId:" + ocisId);
        if (null != preferences && !CollectionUtils.isEmpty(preferences.getAccounts())) {
            UserContext context = session.getUserContext();
            ClientContext clientContext = context.toClientContext();
            String internalUserId = context.getAuthorId();
            if (!clientContext.getPartyId().equalsIgnoreCase(internalUserId)) {
                clientContext.setHost("T");
            }
            clientContext.setUserId("AAGATEWAY");
            setDataHandler(new QName(serviceLocator.getInvolvedPartyManagementSOAPPortWSDDPortName()),
                    serviceLocator.getHandlerRegistry());
            setSoapHeader(clientContext, "modifyStatementProfile", serviceLocator.getServiceName().getNamespaceURI());
            String brandNameText = channelService.getChannelBrand().getResult().getBrand();

            ModifyCommunicationProfilesResponse daoResponse = null;
            try {
                daoResponse = involvedPartyManagement.modifyCommunicationProfiles(createModifyCommunicationProfilesRequest(preferences, ocisId, partyId, brandNameText));
            } catch (Exception ex) {
                logger.logException(PartyDetailServiceImpl.class, ex);
                throw new ServiceException(
                        new ResponseError(EXTERNAL_SERVICE_UNAVAILABLE, EXTERNAL_SERVICE_UNAVAILABLE_MESSAGE));
            }
            validate(daoResponse);
        }
    }

    /**
     * Validate the response.
     * @param response
     */
    @TraceLog
    private void validate(ModifyCommunicationProfilesResponse response) {
        if (null == response) {
            throw new ServiceException(resolver.resolve(ResponseErrorConstants.SERVICE_UNAVAILABLE));
        }
        ResultCondition resultCondition = response.getResponseHeader().getResultCondition();
        validateExternalServiceResponse(resultCondition);
        Arrangement[] aProductArrangementDetails = response.getModifiedArrangements();
        updateIBErrorAfterupdate(aProductArrangementDetails);
    }

    /**
     * Validate the external Service response.
     * @param resultCondition ResultCondition
     */
    @TraceLog
    private void validateExternalServiceResponse(ResultCondition resultCondition) {
        String methodName = "ModifyCommunicationProfileServiceImpl: validateExternalServiceResponse";
        logger.traceLog(this.getClass(), methodName);
        com.lloydstsb.ea.infrastructure.soap.Condition[] conditions = resultCondition.getExtraConditions();
        for (com.lloydstsb.ea.infrastructure.soap.Condition condition : conditions) {
            if (0 != condition.getReasonCode()) {
                throw new ServiceException(
                        new ResponseError(condition.getReasonCode().toString(), condition.getReasonText()));
            }
        }
    }

    /**
     * Updating error after Service Call.
     * @param aProductArrangementDetails Arrangement
     */
    @TraceLog
    private void updateIBErrorAfterupdate(Arrangement[] aProductArrangementDetails) {
        for (Arrangement list : aProductArrangementDetails) {
            getErrorDescription(list);
        }
    }

    /**
     * Method to get Error Description.
     * @param aList - Error.
     * @return String for Error.
     */
    @TraceLog
    public void getErrorDescription(Arrangement aList) {
        String methodName = "ModifyCommunicationProfileServiceImpl: getErrorDescription";
        logger.traceLog(this.getClass(), methodName);
        InvolvedPartyRole[] roleList = aList.getRoles();
        if (null != roleList && (null != roleList[0])) {
            ContactPreference[] conPrefList = roleList[0].getContactPreferences();
            if (null != conPrefList) {
                for (ContactPreference conPref : conPrefList) {
                    for (Condition condition : conPref.getHasObjectConditions()) {
                        validateConditions(condition);
                    }
                }
            }
        }
    }

    /**
     * @param condition
     */
    @TraceLog
    private void validateConditions(Condition condition) {
        String methodName = "ModifyCommunicationProfileServiceImpl: validateConditions";
        logger.traceLog(this.getClass(), methodName);
        String errorDescription = null;
        String errorCode = null;
        if (null != condition && condition instanceof RuleCondition) {
            RuleCondition ruleCondition = (RuleCondition) condition;
            if ("REASON_CODE".equalsIgnoreCase(ruleCondition.getName())) {
                if (null != ruleCondition.getRuleAttributes()[0].getHasAttributeConditionValues()) {
                    errorDescription = ruleCondition.getRuleAttributes()[0].getHasAttributeConditionValues()[0]
                            .getValue();
                    errorCode = ruleCondition.getRuleAttributes()[0].getHasAttributeConditionValues()[0].getCode();
                    if (null != errorDescription && null != errorCode) {
                        logger.logError(errorCode, errorDescription, this.getClass());
                        throw new ServiceException(new ResponseError(errorCode, errorDescription));
                    }
                }
            }
        }
    }

    @Override
    public Class<InvolvedPartyManagement> getPort() {
        return InvolvedPartyManagement.class;
    }
}
