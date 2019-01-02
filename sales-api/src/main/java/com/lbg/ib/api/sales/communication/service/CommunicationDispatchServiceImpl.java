/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.communication.service;

import com.lbg.ib.api.sales.common.constant.Constants;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.util.DateUtil;
import com.lbg.ib.api.sales.communication.domain.ScheduleEmailSmsRequest;
import com.lbg.ib.api.sales.communication.domain.ScheduleEmailSmsResponse;
import com.lbg.ib.api.sales.communication.dto.ScheduleEmailDTO;
import com.lbg.ib.api.sales.communication.mapper.SendCommunicationMapper;
import com.lbg.ib.api.sales.conversion.domain.AdditionalPartyDetails;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.shared.service.SoaAbstractService;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.sales.user.domain.SelectedAccount;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lloydstsb.lcsm.communicationmanagement.CommunicationDispatch;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.Map.Entry;

import static com.lbg.ib.api.sales.communication.mapper.ScheduleCommunicationMapper.buildRequest;
import static com.lbg.ib.api.sales.communication.mapper.ScheduleCommunicationMapper.buildResponse;

/**
 * Account CommunicationDispatch Service.
 * @author csharma8
 * Change history:
 * @Change#1: Conversion Email functionlity changes.
 * @author:  Rohit.Soni
 */
@Component
public class CommunicationDispatchServiceImpl extends SoaAbstractService implements CommunicationDispatchService {

    @Autowired
    private SessionManagementDAO sessionManager;

    @Autowired
    private ConfigurationDAO configManager;

    @Autowired
    private LoggerDAO logger;

    @Autowired
    private ScheduleCommunicationService scheduleCommunicationService;

    @Autowired
    private CommunicationService communicationService;

    @Autowired
    private SendCommunicationMapper sendCommunicationMapper;

    @Autowired
    private GalaxyErrorCodeResolver errorResolver;

    private static final String PRIMARY_PARTY_TYPE = "Primary";
    private static final String SECONDARY_PARTY_TYPE = "Secondary";
    private static final String SUCCESS_EMAIL_LIST = "SuccessEmailList";
    private static final String FAILED_EMAIL_LIST = "FailedEmailList";

    @TraceLog
    public ScheduleEmailSmsResponse scheduleCommunication(ScheduleEmailSmsRequest userRequest) {
        final Map<String, PartyDetails> parties = sessionManager.getAllPartyDetailsSessionInfo();
        final SelectedAccount accountToConvertInContext = sessionManager.getAccountToConvertInContext();
        final List<AdditionalPartyDetails> additionalPartyDetailsList = validateRequest(userRequest, parties, accountToConvertInContext);
        return doScheduleCommunication(userRequest, parties, accountToConvertInContext, additionalPartyDetailsList);
    }

    @TraceLog
    public ScheduleEmailSmsResponse sendCommunication(ScheduleEmailSmsRequest userRequest) {
        final Map<String, PartyDetails> parties = sessionManager.getAllPartyDetailsSessionInfo();
        final SelectedAccount accountToConvertInContext = sessionManager.getAccountToConvertInContext();
        final List<AdditionalPartyDetails> additionalPartyDetailsList = validateRequest(userRequest, parties,
                accountToConvertInContext);
        return doSendCommunication(userRequest, parties, accountToConvertInContext, additionalPartyDetailsList);
    }

    private List<AdditionalPartyDetails> validateRequest(ScheduleEmailSmsRequest userRequest,
                                                         Map<String, PartyDetails> parties,
                                                         SelectedAccount accountToConvertInContext) {
        if(accountToConvertInContext == null){
            logger.traceLog(this.getClass(),"Account to convert not present in session");
            throw new ServiceException(errorResolver.createResponseError(ResponseErrorConstants.SERVICE_UNAVAILABLE));
        }
        final List<AdditionalPartyDetails> additionalPartyDetailsList = userRequest.getAdditionalPartyDetailsList();
        if((additionalPartyDetailsList!=null) &&(parties!=null)&&(additionalPartyDetailsList.size()!=parties.size())){
            logger.traceLog(this.getClass(), "The additionalPartyDetails does not match the parties list in session. Email functionality will be impacted");
            //Dont have to stop the journey in this case
        }
        return additionalPartyDetailsList;
    }

    private ScheduleEmailSmsResponse doScheduleCommunication(ScheduleEmailSmsRequest userRequest, final Map<String, PartyDetails> parties,
                                                             final SelectedAccount accountToConvertInContext,
                                                             final List<AdditionalPartyDetails> additionalPartyDetailsList) {
        logger.traceLog(this.getClass(), "Inside doScheduleCommunication");
        Map<String,Map<String, List<String>>> emailSentInfo = new HashMap<String,Map<String, List<String>>>();
        for(Map.Entry<String, PartyDetails> partyEntrySet : parties.entrySet()){
            String partyOcisId = partyEntrySet.getKey();
            PartyDetails party = partyEntrySet.getValue();
            String partyType = (party.isPrimaryParty()? PRIMARY_PARTY_TYPE : SECONDARY_PARTY_TYPE);
            String detailForLogs = " Details : "+" Party: "+ partyType+ ", OcisId: "+ partyOcisId +", Name: "+party.getFirstName()+" "+party.getSurname()
                    + ", Account: "+accountToConvertInContext.getAccountNumber()+", Sort Code: "+accountToConvertInContext.getSortCode();

            List<String> failedEmailListForParty = new ArrayList<String>();
            List<String> successEmailListForParty = new ArrayList<String>();
            Map<String, String> emailTokens = prepareTemplateContentKeys(userRequest.getEmailTokens(), party);
            AdditionalPartyDetails additionalPartyDetails = null;
            //Sending mail for each party- In all cases
            if(StringUtils.isNotEmpty(party.getEmail())){
                invokeScheduleCommunication(userRequest, party.getEmail(), successEmailListForParty,
                        failedEmailListForParty, emailTokens, partyType, detailForLogs);
            }else{
                logger.traceLog(this.getClass(),"Skipping email for "+ detailForLogs +" as email is not present");
            }
            if(additionalPartyDetailsList!=null){
                additionalPartyDetails = getAdditionalDetailsForParty(party, additionalPartyDetailsList, partyType);
            }
            //Send to Group
            if(shouldSendGroupEmail(additionalPartyDetailsList,additionalPartyDetails,userRequest)){
                logger.traceLog(this.getClass(), "Sending group email for "+ detailForLogs);
                invokeScheduleCommunication(userRequest, userRequest.getGroupEmailId(), successEmailListForParty,
                        failedEmailListForParty, emailTokens, partyType, detailForLogs);
            }
            //Send to Ops
            if(shouldSendOpsEmail(party, additionalPartyDetails, userRequest, additionalPartyDetailsList)){
                logger.traceLog(this.getClass(), "Sending Ops email for "+ detailForLogs);
                invokeScheduleCommunication(userRequest, userRequest.getOpsEmail(), successEmailListForParty,
                        failedEmailListForParty, emailTokens, partyType, detailForLogs);
            }
            Map<String, List<String>> emailResultForParty = new HashMap<String, List<String>>();
            emailResultForParty.put(SUCCESS_EMAIL_LIST,successEmailListForParty);
            emailResultForParty.put(FAILED_EMAIL_LIST,failedEmailListForParty);
            logger.traceLog(this.getClass(), "Emailing results for "+ detailForLogs + " are success for: "+ successEmailListForParty + " and failed for : "+ failedEmailListForParty);
            emailSentInfo.put(partyType,emailResultForParty);
        }
        return buildResponse(emailSentInfo);
    }

    private ScheduleEmailSmsResponse doSendCommunication(ScheduleEmailSmsRequest userRequest, final Map<String, PartyDetails> parties,
                                                         final SelectedAccount accountToConvertInContext,
                                                         final List<AdditionalPartyDetails> additionalPartyDetailsList) {
        logger.traceLog(this.getClass(), "Inside doSendCommunication");
        Map<String,Map<String, List<String>>> emailSentInfo = new HashMap<String,Map<String, List<String>>>();
        for(Map.Entry<String, PartyDetails> partyEntrySet : parties.entrySet()){
            String partyOcisId = partyEntrySet.getKey();
            PartyDetails party = partyEntrySet.getValue();
            if(party.isPrimaryParty()) {
                party.setEmail(sessionManager.getUserInfo().getPrimaryInvolvedParty().getEmail());
            }
            String partyType = (party.isPrimaryParty()? PRIMARY_PARTY_TYPE : SECONDARY_PARTY_TYPE);
            String detailForLogs = " Details : "+" Party: "+ partyType+ ", OcisId: "+ partyOcisId +", Name: "+party.getFirstName()+" "+party.getSurname()
                    + ", Account: "+accountToConvertInContext.getAccountNumber()+", Sort Code: "+accountToConvertInContext.getSortCode();
            List<String> failedEmailListForParty = new ArrayList<String>();
            List<String> successEmailListForParty = new ArrayList<String>();
            Map<String, String> emailTokens = prepareTemplateContentKeys(userRequest.getEmailTokens(), party);
            AdditionalPartyDetails additionalPartyDetails = null;
            if(additionalPartyDetailsList != null){
                additionalPartyDetails = getAdditionalDetailsForParty(party, additionalPartyDetailsList, partyType);
            }
            // Send mail to Ops if email retry reaches max limit for digital
            if(sessionManager.getBranchContext() == null &&
                    sessionManager.getEmailRetryCount() != null &&
                    sessionManager.getEmailRetryCount().intValue() == Constants.MAX_EMAIL_RETRY_LIMIT.intValue()) {
                logger.traceLog(this.getClass(), "doSendCommunication(): Retry limit reached so send email to back office."+ detailForLogs);
                sendEmailToOps(userRequest, additionalPartyDetails, detailForLogs, failedEmailListForParty, successEmailListForParty, emailTokens);
            }
            //Sending mail for each party- In all cases
            else if(StringUtils.isNotEmpty(party.getEmail())) {
                if(!invokeSendEmail(userRequest, party.getEmail(), emailTokens, detailForLogs)) {
                    failedEmailListForParty.add(party.getEmail());
                    logger.traceLog(this.getClass(), "doSendCommunication(): Failed to send email to "+partyType+" party now send email to back office if party not present");
                    if(shouldEmailToOps(additionalPartyDetails)) {
                        sendEmailToOps(userRequest, additionalPartyDetails, detailForLogs, failedEmailListForParty, successEmailListForParty, emailTokens);
                    } else {
                        logger.traceLog(this.getClass(),"Nothing to do since email sent to back office is FALSE");
                    }
                } else {
                    successEmailListForParty.add(party.getEmail());
                    logger.traceLog(this.getClass(),"doSendCommunication(): Email successfully sent to "+ partyType+ " party."+ detailForLogs);
                }
            } else {
                logger.traceLog(this.getClass(),"doSendCommunication(): Email is not present for " + partyType+ " party now send email to back office if party not present");
                if(shouldEmailToOps(additionalPartyDetails)) {
                    sendEmailToOps(userRequest, additionalPartyDetails, detailForLogs, failedEmailListForParty, successEmailListForParty, emailTokens);
                } else {
                    logger.traceLog(this.getClass(),"Nothing to do since email sent to back office is FALSE");
                }
            }

            Map<String, List<String>> emailResultForParty = new HashMap<String, List<String>>();
            emailResultForParty.put(SUCCESS_EMAIL_LIST,successEmailListForParty);
            emailResultForParty.put(FAILED_EMAIL_LIST,failedEmailListForParty);
            logger.traceLog(this.getClass(), "doSendCommunication(): Emailing results for "+ detailForLogs + " are success for: "+ successEmailListForParty + " and failed for : "+ failedEmailListForParty);
            emailSentInfo.put(partyType,emailResultForParty);
        }
        return buildResponse(emailSentInfo);
    }
    /*
    Send email to OPS if party not present and OPs email send failed then add event to Splunk dashboard
     */
    private void sendEmailToOps(ScheduleEmailSmsRequest userRequest, AdditionalPartyDetails additionalPartyDetails, String detailForLogs,
    		List<String> failedEmailListForParty, List<String> successEmailListForParty, Map<String, String> emailTokens) {
    	logger.traceLog(this.getClass(),"Sending email to back office." + detailForLogs);
    	if(StringUtils.isNotEmpty(userRequest.getOpsEmail())) {
    		if(!invokeSendEmail(userRequest, userRequest.getOpsEmail(), emailTokens, detailForLogs)) {
    			failedEmailListForParty.add(userRequest.getOpsEmail());
    			// Add splunk logging
    			logger.traceLog(this.getClass(),"Add this event to splunk dashboard. Failed to send email to back office." + detailForLogs);
    		} else {
    			successEmailListForParty.add(userRequest.getOpsEmail());
    			logger.traceLog(this.getClass(),"Email successfully sent to Back office.  "+ detailForLogs);
    		}
    	} else {
    		logger.traceLog(this.getClass(),"Add this event to splunk dashboard. Back office email is blank." + detailForLogs);
    	}
    }

    private boolean shouldEmailToOps(AdditionalPartyDetails additionalPartyDetails) {
        if(additionalPartyDetails != null && sessionManager.getBranchContext() != null) {
            if ((!additionalPartyDetails.isPartyPresent())) {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    /*SEND TO OPS
        Send if additional details are coming and then the following eligibility rules:
            a. If branch, party present false and email available false
            b. If digital, email available false
    */
    private boolean shouldSendOpsEmail(PartyDetails party, AdditionalPartyDetails additionalPartyDetails, ScheduleEmailSmsRequest userRequest, List<AdditionalPartyDetails> additionalPartyDetailsList){
        if(StringUtils.isNotEmpty(userRequest.getOpsEmail())){
            if((additionalPartyDetails != null)){
                if(sessionManager.getBranchContext()!=null) {
                    if ((!additionalPartyDetails.isPartyPresent()) && ((StringUtils.isEmpty(party.getEmail())))) {
                        return true;
                    }
                }else{
                    if((StringUtils.isEmpty(party.getEmail()))){
                        return true;
                    }
                }
            } else if((additionalPartyDetails == null) && (additionalPartyDetailsList!=null)){   //This check so that even if we can not map the party, we should send mail to OPS
                return true;
            }
        }
        return false;
    }

    /*SEND to GROUP SCENARIOS
    Send to group email based on - First if the additional details are not coming and the group email id is present- Send to Group- BAU
    Send to group email- Second scenario: If the additional Details are coming and the recording failed for the party.- Send to group
    */
    private boolean shouldSendGroupEmail(List<AdditionalPartyDetails> additionalPartyDetailsList, AdditionalPartyDetails additionalPartyDetails, ScheduleEmailSmsRequest userRequest){
        if(StringUtils.isNotEmpty(userRequest.getGroupEmailId())){
            if((additionalPartyDetailsList == null)||((additionalPartyDetails != null)&&(!(additionalPartyDetails.isRecordingSuccessful())))){
                return true;
            }
        }
        return false;
    }

    /*
        This method is for mapping the party recording and additional details with the party
     */
    private AdditionalPartyDetails getAdditionalDetailsForParty(PartyDetails party, List<AdditionalPartyDetails> additionalPartyDetailsList, String partyType){
        logger.traceLog(this.getClass(),"Inside getAdditionalDetailsForParty method. Retrieving additional details for party: "+partyType);
        AdditionalPartyDetails additionalPartyDetails = null;
        final ListIterator<AdditionalPartyDetails> additionalPartyDetailsListIterator= additionalPartyDetailsList.listIterator();
        while(additionalPartyDetailsListIterator.hasNext()){
            AdditionalPartyDetails additionalPartyDetailsTemp=additionalPartyDetailsListIterator.next();
            if(party.isPrimaryParty()==additionalPartyDetailsTemp.isPrimaryParty()){
                additionalPartyDetails = additionalPartyDetailsTemp;
                break;
            }
        }
        if(!additionalPartyDetailsList.isEmpty() && additionalPartyDetails==null) {
            logger.traceLog(this.getClass(),"The specified party can not be matched with the party from session response.");
        } else {
            logger.traceLog(this.getClass(),"GetAdditionalDetailsForParty completed successfully for party: "+ partyType);
        }
        return additionalPartyDetails;
    }

    private void invokeScheduleCommunication(ScheduleEmailSmsRequest userRequest, String emailId,List<String> successEmailList, List<String> failedEmailList, Map<String, String> emailTokens, String partyType, String additionalLogs){
        logger.traceLog(this.getClass(), "Invoking schedule communication for email account: "+emailId + "with "+additionalLogs);
        ScheduleEmailDTO dto = buildScheduleEmailDTO(userRequest, emailId, emailTokens);
        try{
            scheduleCommunicationService.invokeScheduleCommunication(buildRequest(dto));
            logger.traceLog(this.getClass(), "Schedule communication is successful for email account: "+emailId);
            successEmailList.add(emailId);
        }catch(Exception e) {
            logger.logException(this.getClass(),e);
            logger.traceLog(this.getClass(),"Schedule communication failed email account: "+emailId + "with "+additionalLogs +" .Will be retried with send communication");
            try{
                boolean isSendSuccess = communicationService.sendEmailCommunication(sendCommunicationMapper.buildSendCommunicationDTO(userRequest,emailTokens,Arrays.asList(emailId)));
                if(isSendSuccess){
                    logger.traceLog(this.getClass(), "Send communication is successful for email account: "+emailId + "with "+additionalLogs);
                    successEmailList.add(emailId);
                }else{
                    logger.traceLog(this.getClass(), "Send communication failed for email account: "+emailId + "with "+additionalLogs);
                    failedEmailList.add(emailId);
                }
            }catch(Exception ex){
                logger.logException(this.getClass(), ex);
                logger.traceLog(this.getClass(), "Exception occured in send communication for email account: "+emailId + "with "+additionalLogs);
                failedEmailList.add(emailId);
            }
        }
    }

    private boolean invokeSendEmail(ScheduleEmailSmsRequest userRequest, String emailId, Map<String, String> emailTokens, String additionalLogs){
        logger.traceLog(this.getClass(), "Invoking send email for email account: "+emailId + "with "+additionalLogs);
        try{
            boolean isSendSuccess = communicationService.sendEmailCommunication(sendCommunicationMapper.buildSendCommunicationDTO(userRequest,emailTokens,Arrays.asList(emailId)));
            if(isSendSuccess){
                logger.traceLog(this.getClass(), "invokeSendEmail(): Send email is successful for email account: "+emailId + "with "+additionalLogs);
                return true;
            }else{
                logger.traceLog(this.getClass(), "invokeSendEmail(): Send email failed for email account: "+emailId + "with "+additionalLogs);
                return false;
            }
        }catch(Exception ex){
            logger.logException(this.getClass(), ex);
            logger.traceLog(this.getClass(), "Exception occurred in send email for email account: "+emailId + "with "+additionalLogs);
            return false;
        }
    }

    @TraceLog
    private Map<String, String> prepareTemplateContentKeys(Map<String, String> emailTokens, PartyDetails party) {
        SelectedAccount accountToConvert = sessionManager.getAccountToConvertInContext();
        Map<String, String> contentKeys = new HashMap<String, String>();
        setContentKey(contentKeys, "IB.Customer.LastName", party.getSurname());
        setContentKey(contentKeys, "IB.Customer.Title", party.getTitle());
        setContentKey(contentKeys, "IB.Customer.FirstName", party.getFirstName());
        setContentKey(contentKeys, "IB.Customer.Addr.PostCodeMasked", maskValue(party.getPostalCode()));
        setContentKey(contentKeys, "IB.Customer.Addr.PostCode", party.getPostalCode());
        setContentKey(contentKeys, "IB.Product.AccountNumberMasked", maskValue(accountToConvert.getAccountNumber()));
        setContentKey(contentKeys, "IB.Product.PCA.AccountNumber", accountToConvert.getAccountNumber());
        setContentKey(contentKeys, "IB.Product.PCA.SortCode", accountToConvert.getSortCode());
        setContentKey(contentKeys, "IB.Customer.IssueDate", DateUtil.getCurrentUkDateOnlyAsString());
        Map<String, String> addressLineMap = prepareAddressLinesMap(party.getAddressLines());
        for (Entry<String, String> addressToken : addressLineMap.entrySet()) {
            contentKeys.put(addressToken.getKey(), addressToken.getValue());
        }
        if(emailTokens != null) {
            for (Entry<String, String> token : emailTokens.entrySet()) {
                contentKeys.put(token.getKey(), token.getValue());
            }
        }
        return contentKeys;
    }

    private Map<String, String> prepareAddressLinesMap(String[] addressLines){
        Map<String, String> addressLineMap = new HashMap<String, String>();
        int addressLineNumber = 0;
        if(addressLines != null){
            for(String addrLine: addressLines){
                if(StringUtils.isNotEmpty(addrLine)){
                    addressLineNumber++;
                    addressLineMap.put("IB.Product.Addr.AddressLine"+addressLineNumber, addrLine);
                }
            }
        }
        //Stuff blank lines for missing lines
        while(addressLineNumber < 8){
            addressLineNumber++;
            addressLineMap.put("IB.Product.Addr.AddressLine"+addressLineNumber, "");
        }
        return addressLineMap;
    }

    private Map<String, String> setContentKey(Map<String, String> contentKeys, String key, String value) {
        if (StringUtils.isNotEmpty(value)) {
            contentKeys.put(key, value);
        }
        return contentKeys;
    }

    @Override
    public Class<?> getPort() {
        return CommunicationDispatch.class;
    }

    private ScheduleEmailDTO buildScheduleEmailDTO(ScheduleEmailSmsRequest userRequest, String emailId, Map<String, String> contentKeys) {
        String channel = configManager.getConfigurationStringValue("BRAND_NAME_MAPPING", sessionManager.getUserContext().getChannelId());
        ScheduleEmailDTO dto = new ScheduleEmailDTO();
        dto.setChannel(channel);
        dto.setTemplateId(userRequest.getTemplateName());
        dto.setContentKeys(contentKeys);
        dto.setMedium(userRequest.getCommunicationMedia());
        dto.setEmail(emailId);
        return dto;
    }

    private String maskValue(String value) {
        if (value == null) {
            return null;
        }
        String maskedValue = null;
        char[] characters = null;
        if (StringUtils.isNotEmpty(value)) {
            characters = value.trim().replaceAll(" ", "").toCharArray();
            for (int index = 0; index < characters.length / 2; index++) {
                characters[index] = 'X';
            }
        }
        maskedValue = String.valueOf(characters);
        return maskedValue;
    }

}
