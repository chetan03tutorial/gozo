/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 *
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.product.service;

import com.lbg.ib.api.sales.asm.service.B274Service;
import com.lbg.ib.api.sales.common.auditing.FraudAuditor;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.session.dto.CustomerInfo;
import com.lbg.ib.api.sales.dao.constants.CommonConstant;
import com.lbg.ib.api.sales.dao.product.activate.ActivateProductDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dao.switches.SwitchesManagementDAO;
import com.lbg.ib.api.sales.dto.b274.B274RequestDTO;
import com.lbg.ib.api.sales.dto.product.ConditionDTO;
import com.lbg.ib.api.sales.dto.product.activate.ActivateProductDTO;
import com.lbg.ib.api.sales.dto.product.activate.ActivateProductResponseDTO;
import com.lbg.ib.api.sales.dto.product.overdraft.OverdraftResponseDTO;
import com.lbg.ib.api.sales.product.domain.ArrangeToActivateParameters;
import com.lbg.ib.api.sales.product.domain.Condition;
import com.lbg.ib.api.sales.product.domain.SelectedProduct;
import com.lbg.ib.api.sales.product.domain.activate.AccountSwitching;
import com.lbg.ib.api.sales.product.domain.activate.Activated;
import com.lbg.ib.api.sales.product.domain.activate.Activation;
import com.lbg.ib.api.sales.product.domain.activate.ArrangementId;
import com.lbg.ib.api.sales.product.domain.arrangement.AccountDetails;
import com.lbg.ib.api.sales.product.domain.arrangement.Overdraft;
import com.lbg.ib.api.sales.product.domain.arrangement.OverdraftIntrestRates;
import com.lbg.ib.api.sales.user.domain.AddParty;
import com.lbg.ib.api.sales.utils.CommonUtils;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.shared.service.reference.ReferenceDataServiceDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.validation.AccountType;
import com.lloydstsb.ea.enums.BrandValue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Component
public class ActivateProductServiceImpl extends ActivateProductServiceBase implements ActivateProductService {

    private static final String BLANK = "";

    @Autowired
    private ChannelBrandingDAO channelService;

    @Autowired
    private SwitchesManagementDAO switchesDAO;

    @Autowired
    private ActivateProductDAO activateProductDAO;

    @Autowired
    private ReferenceDataServiceDAO referenceDataDAO;

    @Autowired
    private SessionManagementDAO session;

    @Autowired
    private GalaxyErrorCodeResolver resolver;

    @Autowired
    private LoggerDAO logger;

    @Autowired
    private FraudAuditor fraudAuditor;

    @Autowired
    private ChannelBrandingDAO channelBrandingService;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private B274Service b274Service;

    public static final String SIRA_WORKFLOW_NAME_LTB = "LBG_ULLO_RT_WF1_RULES2";

    public static final String SIRA_WORKFLOW_NAME_HBOS = "LBG_UHBC_RT_WF1_RULES2";

    @Autowired
    private ActivateSortCodeService activateSortCodeService;

    @TraceLog
    public Activated activateProduct(ArrangementId arrangementId, Activation activation) throws ServiceException {
        try {
            ActivateProductDTO requestDTO = populateActivateProductDto(arrangementId, activation);

            DAOResponse<ActivateProductResponseDTO> responseDTO = activateProductDAO.activateProduct(requestDTO);
            if (responseDTO.getError() != null) {

                CustomerInfo custInfo = session.getCustomerDetails();
                if (custInfo != null) {
                    if (activation.getAccountSwitchingDetails() != null) {
                        custInfo.setSwitchSuccess(false);
                        custInfo.setSwitchApplied(true);
                    }
                }

                if (responseDTO.getError().getErrorMessage() != null) {
                    throw new ServiceException(resolver.customResolve(responseDTO.getError().getErrorCode(),
                            responseDTO.getError().getErrorMessage()));
                } else {
                    throw new ServiceException(resolver.resolve(responseDTO.getError().getErrorCode()));
                }
            }

            ActivateProductResponseDTO result = responseDTO.getResult();

            // Update customer details in session with sortcode and account
            // number
            CustomerInfo custInfo = session.getCustomerDetails();
            if (custInfo != null) {
                custInfo.setAccountNumber(result.getAccountNo());
                custInfo.setSortCode(result.getSortCode());
                custInfo.setConditions(activation.getConditions());
                custInfo.setCustomerNumber(result.getCustomerNumber());

                if (activation.getAccountSwitchingDetails() != null) {
                    custInfo.setSwitchingDate(activation.getAccountSwitchingDetails().getSwitchingDate());
                    custInfo.setSwitchSuccess(true);
                    custInfo.setSwitchApplied(true);
                }
                if (null != activation.getOverDraftDetails()) {
                    custInfo.setOverdraftAmount(activation.getOverDraftDetails().getAmount());
                }
                if (null != session.getBranchContext()) {
                    custInfo.setOriginatingSortCode(session.getBranchContext().getOriginatingSortCode());
                    custInfo.setColleagueId(session.getBranchContext().getColleagueId());
                }
                if (null != session.getUserContext()) {
                    custInfo.setOcisId(session.getUserContext().getOcisId());
                }
                if (activation.getInvolvedPartyRole() != null)
                    custInfo.setUserName(activation.getInvolvedPartyRole().getUserName());
            }
            ArrangeToActivateParameters arrangeToActivateParam = session.getArrangeToActivateParameters();
            if (arrangeToActivateParam != null) {
                arrangeToActivateParam.setActivationStatus(result.getApplicationStatus());
            }

            auditSiraResponse(requestDTO, result);

            // Pipeline chasing changes -- To clear the session attribute if
            // activate is successfully
            // completed.
            // Code commented as it is redundant code
            /*
             * if (result != null) {
             * session.clearSessionAttributeForPipelineChasing(); }
             */

            Activated activated = new Activated(result.getProductName(), result.getMnemonic(),
                    result.getApplicationStatus(), result.getApplicationSubStatus(), result.getArrangementId(),
                    populateAccountDetails(result), getCrossSellProductName(result.getApplicationStatus()),
                    result.getCustomerNumber(), checkErrorFailureCase(result));

            activated.setCustomerDocuments(result.getCustomerDocuments());
            return activated;

        } catch (ServiceException e) {
            logger.logException(this.getClass(), e);
            throw e;
        } catch (Exception e) {
            logger.logException(this.getClass(), e);
            throw new ServiceException(resolver.resolve(ResponseErrorConstants.SERVICE_EXCEPTION));
        }

    }

    private void auditSiraResponse(ActivateProductDTO requestDTO, ActivateProductResponseDTO result) {
        if ((AccountType.CA.toString()).equalsIgnoreCase(requestDTO.getArrangementType())
                && commonUtils.getSiraSwitchStatus(channelService, switchesDAO, session)) {
            result.getSiraScoreDTO().setSiraWorkFlowName(requestDTO.getSiraWorkFlowName());
            fraudAuditor.audit(result);
        }
    }

    public void setSIRAWorkflowName(ActivateProductDTO offerProductDTO) {
        String workFlowName = null;
        DAOResponse<ChannelBrandDTO> channel = channelBrandingService.getChannelBrand();
        String brand = channel.getResult().getBrand();
        if ((BrandValue.LLOYDS.getBrand()).equalsIgnoreCase(brand)) {
            workFlowName = SIRA_WORKFLOW_NAME_LTB;
        } else {
            workFlowName = SIRA_WORKFLOW_NAME_HBOS;
        }
        offerProductDTO.setSiraWorkFlowName(workFlowName);
    }

    public List<Condition> checkErrorFailureCase(ActivateProductResponseDTO result) {
        List<Condition> faliureCondition = null;
        if (result.getConditionDTO() != null && !result.getConditionDTO().isEmpty()) {
            faliureCondition = new ArrayList<Condition>();
            for (ConditionDTO condition : result.getConditionDTO()) {
                ResponseError error = resolver.resolve(condition.getName());
                if (error.getCode() != null) {
                    faliureCondition.add(new Condition(error.getCode(), null, error.getMessage()));
                }
            }
        }
        return faliureCondition;
    }

    private ActivateProductDTO populateActivateProductDto(ArrangementId aid, Activation activation) throws Exception {

        ArrangeToActivateParameters params = session.getArrangeToActivateParameters();
        String productId = activation.getProductId();
        ActivateProductDTO dto = populateProductToActivate(params, productId);

        dto.setArrangementId(aid.getValue());
        dto.setLocation(activation.getLocation());

        dto.setAccountingSortCode(getAccountingSortCode(activation));
        dto.setAlternateSortCode(activation.getAlternateSortCode());

        List<ConditionDTO> conditionDTOList = new ArrayList<ConditionDTO>();
        if (activation.getConditions() != null) {
            for (Condition condition : activation.getConditions()) {
                Integer key = condition.getKey();
                conditionDTOList.add(new ConditionDTO(condition.getName(), key == null ? null : key.toString(),
                        condition.getValue()));
            }
        }
        dto.setConditions(conditionDTOList);

        if (activation.getInvolvedPartyRole() != null) {
            dto.setUsername(activation.getInvolvedPartyRole().getUserName());
            dto.setPassword(activation.getInvolvedPartyRole().getPassword());
        }

        // OD amount opted from UI
        BigDecimal odAmountOpted = null;
        Overdraft overdraftFeatures = activation.getOverDraftDetails();
        if (overdraftFeatures != null) {
            odAmountOpted = overdraftFeatures.getAmount();
        }
        dto.setOdAmountOpted(odAmountOpted);

        boolean overDraftOpted = (odAmountOpted != null);
        dto.setOverDraftOpted(overDraftOpted);

        if (overDraftOpted && session.getAddPartyContext() == null) {
            // Calling b274 to get interest on revised OD amount
            B274RequestDTO b274RequestDTO = b274Service.mapB274Request(odAmountOpted);
            DAOResponse<OverdraftResponseDTO> overdraftResponseFromBAPI274 = b274Service
                    .retrieveOverdraftInterstRates(b274RequestDTO);
            if (overdraftResponseFromBAPI274 != null) {
                final OverdraftResponseDTO responseFromBAPI274 = overdraftResponseFromBAPI274.getResult();
                OverdraftIntrestRates overdraftIntrestRates = b274Service
                        .mappingIntrestRatesToDomainObject(responseFromBAPI274, odAmountOpted);
                dto.setOverdraftIntrestRates(overdraftIntrestRates);
                logger.traceLog(this.getClass(), "Recalculating interest for odAmountOpted:" + odAmountOpted + ":is:"
                        + overdraftIntrestRates.getIntrateBase());
            }

        } else {
            logger.traceLog(this.getClass(),
                    "Calculating the interest from the session for arrangement id::" + aid.getValue());
            OverdraftIntrestRates overdraftIntrestRates = params.getOverdraftIntrestRates();
            dto.setOverdraftIntrestRates(overdraftIntrestRates);
        }

        AccountSwitching switchDetails = activation.getAccountSwitchingDetails();
        dto.setSwitchDetails(switchDetails);

        Boolean isVantageOpted = activation.getIsVantageOpted();

        // Vantage Prd Id from session
        String vantagePrdIdentifier = params.getVantagePrdIdentifier();

        // The product identifier is being changed to Vantage product
        // Identifier. The vantage eligibility check is already
        // done when the vantagePrdIdentifier is being populated in the session.
        if (isVantageOpted != null && isVantageOpted) {
            if (vantagePrdIdentifier != null && !BLANK.equalsIgnoreCase(vantagePrdIdentifier)) {
                dto.setProductIdentifier(vantagePrdIdentifier);
            }
        }

        if (activation.getCustomerDocuments() != null) {
            dto.setCustomerDocuments(activation.getCustomerDocuments());
        }

        dto.setPrimaryAssessmentEvidence(activation.getPrimaryAssessmentEvidence());
        dto.setParentAssessmentEvidence(activation.getParentAssessmentEvidence());
        dto.setOriginatingSortCode(activation.getOriginatingSortCode());
        dto.setSnr(activation.isSnr());
        dto.setColleagueId(activation.getColleagueId());

        String cbsProductId = activation.getCbsProductId();
        String sellerLegalEntity = activation.getSellerLegalEntity();
        String accNumber = activation.getAccountNumber();
        String reqSortCode = activation.getSortCode();

        if (!StringUtils.isEmpty(cbsProductId) && !StringUtils.isEmpty(sellerLegalEntity)) {
            AddParty addParyContext = session.getAddPartyContext();
            if (addParyContext != null) {
                addParyContext.setCbsProductId(cbsProductId);
                addParyContext.setSellerLegalEntity(sellerLegalEntity);
                addParyContext.setAccountNumber(accNumber);
                addParyContext.setSortCode(reqSortCode);
            }
        }

        AddParty addParyContext = session.getAddPartyContext();
        if (addParyContext != null) {
            cbsProductId = addParyContext.getCbsProductId();
            sellerLegalEntity = addParyContext.getSellerLegalEntity();
            accNumber = addParyContext.getAccountNumber();
            reqSortCode = addParyContext.getSortCode();
        }

        dto.setAccountNumber(accNumber);

        if (reqSortCode != null && !BLANK.equals(reqSortCode)) {
            dto.setSortCode(reqSortCode);
        }

        dto.setCbsProductId(cbsProductId);
        dto.setSellerLegalEntity(sellerLegalEntity);
        return dto;
    }

    /**
     * Setting default branch sort code if not able to find from latitude
     * longitude
     * 
     * @param activation
     * @return
     */
    private String getAccountingSortCode(Activation activation) {
        logger.traceLog(this.getClass(), ":::Going to fetch Sort Code:::");

        String accountingSortCode = null;
        try {

            if (session.getBranchContext() == null
                    && (activation.getLocation() == null || StringUtils.isBlank(activation.getLocation().getLat())
                            || StringUtils.isBlank(activation.getLocation().getLng()))) {

                String currentBrand = activateSortCodeService.getBrand();
                String cityName = session.getPartyCity();

                logger.traceLog(this.getClass(), ":::Going to fetch town as not found by location for:::" + cityName
                        + " for the brand " + currentBrand);
                if (cityName != null) {
                    accountingSortCode = activateSortCodeService.getSortCodeByTown(currentBrand, cityName);
                }
            } else {
                logger.traceLog(this.getClass(), ":::Location is not null for no need to get accounting sort code:::");
            }
            logger.traceLog(this.getClass(), ":::Sending sort Code:::" + accountingSortCode);
        } catch (Exception x) {
            logger.traceLog(this.getClass(), ":::Error While fatching Sort Code:::");
            logger.logException(this.getClass(), x);
        }
        return accountingSortCode;
    }

    private ActivateProductDTO populateProductToActivate(ArrangeToActivateParameters params, String productId)
            throws ServiceException {
        String identifier = null;
        String productName = null;
        String pdtFamilyIdentifier = null;

        ActivateProductDTO dto = new ActivateProductDTO();

        SelectedProduct selectedProduct = session.getSelectedProduct();

        if (selectedProduct == null) {
            throw new ServiceException(
                    resolver.resolve(ResponseErrorConstants.NO_PRODUCT_TO_ACTIVATE_SERVICE_EXCEPTION));
        }

        // URCA Downsell journey
        if (!("".equalsIgnoreCase(productId) || productId == null)) {
            if (productId.equalsIgnoreCase(selectedProduct.getIdentifier())) {
                identifier = params.getProductId();
                productName = params.getProductName();
                pdtFamilyIdentifier = params.getProductFamilyIdentifier();
            } else if (selectedProduct.hasAlternate(productId)) {
                SelectedProduct alternateProduct = selectedProduct.getAlternateProduct(productId);
                identifier = alternateProduct.getIdentifier();
                productName = alternateProduct.getName();
                pdtFamilyIdentifier = alternateProduct.getPdtFamilyIdentifier();
                // Changes to set the down sold Vantage Product mnemonic
                if (params.getAlternateVantagePrdIdentifier() != null
                        && !BLANK.equals(params.getAlternateVantagePrdIdentifier())) {
                    params.setVantagePrdIdentifier(params.getAlternateVantagePrdIdentifier());
                }
            } else {
                throw new ServiceException(
                        resolver.resolve(ResponseErrorConstants.NO_PRODUCT_TO_ACTIVATE_SERVICE_EXCEPTION));
            }
        } else {
            identifier = params.getProductId();
            productName = params.getProductName();
            pdtFamilyIdentifier = params.getProductFamilyIdentifier();
        }

        dto.setProductIdentifier(identifier);
        dto.setProductName(productName);
        dto.setProductFamilyIdentifier(pdtFamilyIdentifier);

        dto.setArrangementType(params.getArrangementType());
        dto.setAppStatus(params.getAppStatus());
        dto.setApplicationType(params.getApplicationType());

        dto.setSortCode(mapSortCode(params));
        if ((AccountType.CA.toString()).equalsIgnoreCase(params.getArrangementType())) {
            dto.setSiraEnabledSwitch(commonUtils.getSiraSwitchStatus(channelService, switchesDAO, session));
            setSIRAWorkflowName(dto);
        }
        return dto;
    }

    private String mapSortCode(ArrangeToActivateParameters params) {
        /*
         * String sortCode = null; if (params.getSortcode() != null) { sortCode
         * = params.getSortcode(); } else { sortCode = null; }
         */
        return params.getSortcode();
    }

    private AccountDetails populateAccountDetails(ActivateProductResponseDTO responseDTO) {
        return (new AccountDetails(responseDTO.getSortCode(), responseDTO.getAccountNo()));
    }

    /**
     * This method fetches the mnemonic map from the session, that got set
     * during crossSell eligibility call after offer service. And perform the
     * refDB calls to get the corresponding productName, for the mnemonic with
     * higher priority.
     *
     * @return - String
     */
    private String getCrossSellProductName(String applicationStatus) {
        TreeMap<String, String> crossSellMnemonicMap = session.getArrangeToActivateParameters()
                .getCrossSellMnemonicMap();

        if (CommonConstant.APPLICATION_SUCESS_STATUS.equalsIgnoreCase(applicationStatus) && crossSellMnemonicMap != null
                && !crossSellMnemonicMap.isEmpty()) {
            String highPriorityMnemonic = crossSellMnemonicMap.get(crossSellMnemonicMap.firstKey());
            return (referenceDataDAO.getProductName(highPriorityMnemonic));
        }
        return null;
    }

    public void setChannelBrandingService(ChannelBrandingDAO channelBrandingService) {
        this.channelBrandingService = channelBrandingService;
    }

}
