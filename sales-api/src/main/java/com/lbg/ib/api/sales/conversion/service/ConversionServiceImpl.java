/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.conversion.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.activatebenefitarrangement.service.ActivateBenefitArrangementService;
import com.lbg.ib.api.sales.cbs.service.E592Service;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.communication.domain.ScheduleEmailSmsRequest;
import com.lbg.ib.api.sales.communication.domain.ScheduleEmailSmsResponse;
import com.lbg.ib.api.sales.communication.service.CommunicationDispatchService;
import com.lbg.ib.api.sales.conversion.domain.AccountConversionRequest;
import com.lbg.ib.api.sales.conversion.domain.AccountConversionResponse;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.party.C542RequestDTO;
import com.lbg.ib.api.sales.dto.party.CardUpgrade;
import com.lbg.ib.api.sales.dto.party.CreditCardReqDTO;
import com.lbg.ib.api.sales.dto.product.offer.PhoneDTO;
import com.lbg.ib.api.sales.party.dto.CardOrderResponse;
import com.lbg.ib.api.sales.party.helper.PartyHelper;
import com.lbg.ib.api.sales.party.service.CardOrderService;
import com.lbg.ib.api.sales.party.service.OcisC542Service;
import com.lbg.ib.api.sales.product.domain.eligibility.UpgradeOption;
import com.lbg.ib.api.sales.product.domain.lifestyle.CreateServiceArrangement;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.user.domain.SelectedAccount;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import com.lbg.ib.api.sso.domain.user.PrimaryInvolvedParty;
import com.lloydstsb.ea.enums.BrandValue;

/**
 * Interface for product conversion.
 * @author cshar8/tkhann/amit
 */
@Component
public class ConversionServiceImpl implements ConversionService {
    /**
     * Object of moduleContext.
     */
    @Autowired
    private ModuleContext beansLoader;
    /**
     * Session object.
     */
    @Autowired
    private SessionManagementDAO session;
    /**
     * Logger object.
     */
    @Autowired
    private LoggerDAO logger;
    /**
     * GalaxyErrorCodeResolver object
     */
    @Autowired
    private GalaxyErrorCodeResolver resolver;
    /**
     * GalaxyErrorCodeResolver object
     */
    @Autowired
    private CardOrderService cardUpgradeService;

    @Autowired
    private ActivateBenefitArrangementService activateBenefitArrangementService;
    
    @Autowired
    private ChannelBrandingDAO channelBrandingService;
    
    private static Map<String,String> channelDefaultCbsMap;
    
    static {
        channelDefaultCbsMap=new HashMap<String,String>();
        channelDefaultCbsMap.put(BrandValue.LLOYDS.getBrand(),"30");
        channelDefaultCbsMap.put(BrandValue.HALIFAX.getBrand(),"11");
        channelDefaultCbsMap.put(BrandValue.BOS.getBrand(),"80");
    }

    /*
     * (non-Javadoc)
     * @see
     * com.lbg.ib.api.sales.conversion.service.ConversionService#convert(com.lbg.ib.api.sales.conversion
     * .domain.AccountConversionRequest)
     */
    @TraceLog
    public AccountConversionResponse convert(AccountConversionRequest conversionRequest, String idType) {
        AccountConversionResponse conversionResponse = new AccountConversionResponse();
        SelectedAccount selectedAccount = session.getAccountToConvertInContext();
        UpgradeOption upgradeOption = getUpgradeOption(conversionRequest, selectedAccount);
        logger.traceLog(this.getClass(), "convert(): conversion request for accountNumber: " + selectedAccount.getAccountNumber() + " and sortCode: " + selectedAccount.getSortCode());
        logger.traceLog(this.getClass(), "convert(): Product mnemonic from conversionRequest: " + conversionRequest.getProductMnemonic());
        logger.traceLog(this.getClass(), "convert(): Available upgrade options are: " + upgradeOption.getCbsProductIds().toString());
        doCallE592(selectedAccount, upgradeOption, conversionResponse);
        doCallC542ToUpdateOcis(selectedAccount, upgradeOption, conversionResponse, idType);
        if (conversionRequest.isLifeStyleReq()) {
            doLifeStyleBenefit(selectedAccount, upgradeOption, conversionResponse);
        }
        if (conversionRequest.isCardUpgradeReq()) {
            doCardOrder(selectedAccount, upgradeOption, conversionResponse);
        }
        doCallScheduleCommunication(conversionRequest, conversionResponse);
        return conversionResponse;
    }

    /**
     * Calling lifestyle benefit API.
     */
    private void doLifeStyleBenefit(SelectedAccount account, UpgradeOption upgradeOption, AccountConversionResponse conversionResponse) {
        try {
            logger.traceLog(this.getClass(), "doLifeStyleBenfit(): entered");
            Arrangement arrangement = (Arrangement) session.getUserInfo();
            if (null != arrangement && null != arrangement.getPrimaryInvolvedParty()) {
                PrimaryInvolvedParty primaryInvolvedParty = arrangement.getPrimaryInvolvedParty();
                CreateServiceArrangement createServiceArrangement = new CreateServiceArrangement();
                createServiceArrangement.setAccountNumber(account.getAccountNumber());
                createServiceArrangement.setSortCode(account.getSortCode());
                createServiceArrangement.setBirthCity(primaryInvolvedParty.getBirthCity());
                if (StringUtils.isNotEmpty(primaryInvolvedParty.getDob())) {
                    DateFormat formatter = new SimpleDateFormat("YYYY-MM-DD");
                    Date birthDate = formatter.parse(primaryInvolvedParty.getDob());
                    createServiceArrangement.setBirthDate(birthDate);
                }
                createServiceArrangement.setEmailAddress(primaryInvolvedParty.getEmail());
                createServiceArrangement.setFirstName(primaryInvolvedParty.getFirstName());
                createServiceArrangement.setLastName(primaryInvolvedParty.getLastName());
                createServiceArrangement.setPrefixTitleName(primaryInvolvedParty.getTitle());
                createServiceArrangement.setMobilePhone(new PhoneDTO(null, null, primaryInvolvedParty.getMobileNumber().getNumber(), null, null));
                activateBenefitArrangementService.createArrangementSetupService(createServiceArrangement);
                conversionResponse.setLifeStyleDone(true);
            }
        } catch (Exception e) {
            logger.traceLog(this.getClass(), "Ignore error response from LifeStyle API: " + e.getMessage());
            logger.logException(this.getClass(), e);
            conversionResponse.setLifeStyleDone(false);
        }
    }

    /**
     * Method to retrieve value and validate it from session.
     * @param conversionRequest
     * @param selectedAccount
     * @return
     */
    @TraceLog
    private UpgradeOption getUpgradeOption(AccountConversionRequest conversionRequest, SelectedAccount selectedAccount) {
        UpgradeOption upgradeOption = null;
        Map<String, UpgradeOption> upgradeOptionMap = session.getAvailableUpgradeOptions();
        if (null != selectedAccount && null != upgradeOptionMap) {
            logger.traceLog(this.getClass(), " getUpgradeOption(): Product Identifier:" + conversionRequest.getProductMnemonic());
            upgradeOption = upgradeOptionMap.get(conversionRequest.getProductMnemonic());
            if (upgradeOption == null || !upgradeOption.getEligible()) {
                logger.traceLog(this.getClass(), "getUpgradeOption(): Invalid product mnemonic in request");
                throw new ServiceException(resolver.resolve(ResponseErrorConstants.INVALID_PRODUCT_MNEMONIC));
            }
        } else {
            logger.traceLog(this.getClass(), "getUpgradeOption(): Invalid request ");
            throw new ServiceException(resolver.resolve(ResponseErrorConstants.BAD_REQUEST_FORMAT));
        }
        return upgradeOption;
    }

    /**
     * Invoking of E592 CBS API.
     * @param account
     * @param upgradeOption
     * @param conversionResponse
     */
    @TraceLog
    private void doCallE592(SelectedAccount account, UpgradeOption upgradeOption, AccountConversionResponse conversionResponse) {
        E592Service e592Service = beansLoader.getService(E592Service.class);
        e592Service.convertProductE592(account, upgradeOption);
        logger.traceLog(this.getClass(), "doCallE592: Account upgraded in CBS");
        conversionResponse.setUpgradeDone(true);
    }

    /**
     * Invoking of G542 Ocis API to refresh the AOV page.
     * @param selectedAccount
     * @param upgradeOption
     * @param conversionResponse
     */
    @TraceLog
    private void doCallC542ToUpdateOcis(SelectedAccount selectedAccount, UpgradeOption upgradeOption, AccountConversionResponse conversionResponse, String idType) {
        try {
            logger.traceLog(this.getClass(), "doCallC542ToUpdateOcis(): entered");
            Arrangement arrangement = session.getUserInfo();
            OcisC542Service c542Service = beansLoader.getService(OcisC542Service.class);
            C542RequestDTO requestDTO = new C542RequestDTO();
            requestDTO.setAccountNumber(selectedAccount.getAccountNumber());
            requestDTO.setSortCode(selectedAccount.getSortCode());
            if(upgradeOption!=null && CollectionUtils.isNotEmpty(upgradeOption.getCbsProductIds())){
                String cbsId=getCbsProductIdByChannel(upgradeOption.getCbsProductIds());
                logger.traceLog(this.getClass(), ":::Sending CBSID as:::"+cbsId);
                requestDTO.setCbsProductId(cbsId);
            }
            if ("PREFETCH".equalsIgnoreCase(idType)) {
                Map<String, String> primaryOcisIdMap = session.getPrimaryPartyOcisIdMap();
                String prefetchOcisId = primaryOcisIdMap.get(PartyHelper.PREFETCH_OCIS_ID);
                logger.traceLog(this.getClass(), "doCallC542ToUpdateOcis(): prefetcOcisId: " + prefetchOcisId);
                requestDTO.setOcisId(prefetchOcisId);
            } else {
                requestDTO.setOcisId(arrangement.getOcisId());
                logger.traceLog(this.getClass(), "doCallC542ToUpdateOcis(): q226OcisId: " + requestDTO.getOcisId());
            }

            c542Service.updateOcisRecords(requestDTO);
            conversionResponse.setUpdateOcisDone(true);
            logger.traceLog(this.getClass(), "doCallC542ToUpdateOcis(): account upgraded in OCIS");
        } catch (Exception e) {
            logger.traceLog(this.getClass(), "Ignore error response from C542: " + e.getMessage());
            logger.logException(this.getClass(), e);
            conversionResponse.setUpdateOcisDone(false);
        }
    }
    
    private String getCbsProductIdByChannel(final List<String> cbsProductIds) {
        String defaultCode = getDefaultBankCode();
        for (String cbsProductId : cbsProductIds) {
            if (ifContains(cbsProductId, defaultCode)) {
                return cbsProductId;
            }
        }
        return cbsProductIds.get(0);
    }
    
    
    private boolean ifContains(String cbsProductId, String defultCode) {
        logger.traceLog(this.getClass(), ":::Going to Matched cbsid for  " + cbsProductId + " with " + defultCode);
        String midterm = null;
        if (StringUtils.isNotBlank(cbsProductId) && cbsProductId.length() == 10) {
            midterm = cbsProductId.substring(4, 6);
            if (midterm.equals(defultCode)) {
                logger.traceLog(this.getClass(), ":::Matched cbsid for  " + cbsProductId);
                return true;
            }
        }
        logger.traceLog(this.getClass(), ":::Not Matched cbsid for  " + cbsProductId);
        return false;
    }
    
    
    private String getDefaultBankCode() {
        String brand = channelBrandingService.getChannelBrand().getResult().getBrand();
        return channelDefaultCbsMap.get(brand);
    }
    
    

    /**
     * Send the email and ignore any exception if any
     * @param conversionRequest
     * @return
     */
    private AccountConversionResponse doCallScheduleCommunication(AccountConversionRequest conversionRequest, AccountConversionResponse conversionResponse) {
        ScheduleEmailSmsResponse communicationResponse = new ScheduleEmailSmsResponse();
        CommunicationDispatchService service = beansLoader.getService(CommunicationDispatchService.class);
        try {
            communicationResponse = service.scheduleCommunication(buildScheduleCommunicationRequest(conversionRequest));
        } catch (Exception ex) {
            /* Log and Ignore the exception and return successful response */
            logger.traceLog(CommunicationDispatchService.class, ex);
        }
        conversionResponse.setCommunication(communicationResponse);
        return conversionResponse;
    }

    /**
     * Generate email request.
     * @param conversionRequest
     * @return
     */
    private ScheduleEmailSmsRequest buildScheduleCommunicationRequest(AccountConversionRequest conversionRequest) {
        ScheduleEmailSmsRequest emailRequest = new ScheduleEmailSmsRequest();
        String communicationType = conversionRequest.getCommunicationType();
        if(StringUtils.isEmpty(communicationType)) {
        	communicationType = "Email";
        }
        emailRequest.setCommunicationMedia(communicationType);
        emailRequest.setEmailTokens(conversionRequest.getEmailTokens());
        emailRequest.setProductMnemonic(conversionRequest.getProductMnemonic());
        emailRequest.setTemplateName(conversionRequest.getTemplateName());
        emailRequest.setGroupEmailId(conversionRequest.getGroupEmailId());
        emailRequest.setOpsEmail(conversionRequest.getOpsEmail());
        emailRequest.setAdditionalPartyDetailsList(conversionRequest.getAdditionalPartyDetailsList());
        return emailRequest;
    }

    /**
     * Invoking of CMAS API to order card.
     * @param selectedAccount
     * @param upgradeOption
     * @param conversionResponse
     */
    @TraceLog
    private void doCardOrder(SelectedAccount selectedAccount, UpgradeOption upgradeOption, AccountConversionResponse conversionResponse) {
        CardOrderResponse responseDTO = new CardOrderResponse();
        try {
            logger.traceLog(this.getClass(), "doCardOrder(): entered Account No" + selectedAccount.getAccountNumber() + "Sort Code:" + selectedAccount.getSortCode());
            Arrangement arrangement = session.getUserInfo();
            CreditCardReqDTO requestDTO = new CreditCardReqDTO();
            CardUpgrade cardUpgrade = new CardUpgrade();
            logger.traceLog(this.getClass(), "selectedAccount.getAccountNumber()" + selectedAccount.getAccountNumber());
            cardUpgrade.setAccount(selectedAccount.getAccountNumber());
            logger.traceLog(this.getClass(), "selectedAccount.getSortCode()" + selectedAccount.getSortCode());
            cardUpgrade.setSortCode(selectedAccount.getSortCode());
            for (Account account : arrangement.getAccounts()) {
                if (account.getAccountNumber().equalsIgnoreCase(selectedAccount.getAccountNumber()) && account.getSortCode().equalsIgnoreCase(selectedAccount.getSortCode())) {
                    logger.traceLog(this.getClass(), "account.getAccountType().substring(1, 5)" + account.getAccountType().substring(1, 5));
                    cardUpgrade.setCbsFromProductCode(account.getAccountType().substring(1, 5));
                    break;
                }
            }
            logger.traceLog(this.getClass(), "upgradeOption.getCbsProductIds().get(0).substring(0, 4)" + upgradeOption.getCbsProductIds().get(0).substring(0, 4));
            cardUpgrade.setCbsToProductCode(upgradeOption.getCbsProductIds().get(0).substring(0, 4));
            if(session.getTriadDetails() != null) {
                logger.traceLog(this.getClass(), "session.getTriadDetails().getActionValue()" + session.getTriadDetails().getActionValue());
                cardUpgrade.setCreditRiskScore(session.getTriadDetails().getActionValue());
            } else {
                cardUpgrade.setCreditRiskScore("");
            }
            cardUpgrade.setCancelOldOrder("Y");
            logger.traceLog(this.getClass(), "cardUpgrade.getAccount() " + cardUpgrade.getAccount());
            logger.traceLog(this.getClass(), "cardUpgrade.getCancelOldOrder() " + cardUpgrade.getCancelOldOrder());
            logger.traceLog(this.getClass(), "cardUpgrade.getCBSFromProductCode() " + cardUpgrade.getCbsFromProductCode());
            logger.traceLog(this.getClass(), "cardUpgrade.getCBSToProductCode() " + cardUpgrade.getCbsToProductCode());
            logger.traceLog(this.getClass(), "cardUpgrade.getCreditRiskScore() " + cardUpgrade.getCreditRiskScore());
            logger.traceLog(this.getClass(), "cardUpgrade.getSortCode() " +cardUpgrade.getSortCode());
            requestDTO.setCardUpgrade(cardUpgrade);
            responseDTO = cardUpgradeService.upgradeCard(requestDTO);
            logger.traceLog(this.getClass(), "doCardOrder(): CMAS API called successfully");
        } catch (Exception e) {
            responseDTO.setStatusCode(ResponseErrorConstants.SERVICE_UNAVAILABLE);
            responseDTO.setResponseText("Error response from CMAS: " + e.getMessage());
            logger.traceLog(this.getClass(), "doCardOrder(): CMAS API in Error" + e.getMessage());
            logger.logException(this.getClass(), e);
        }
        conversionResponse.setCardOrderDetails(responseDTO);
    }
}
