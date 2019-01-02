package com.lbg.ib.api.sales.asm.service;

import static com.lbg.ib.api.sales.common.constant.Constants.PLDConstants.APPLICATION_APPROVED_STATUS;
import static com.lbg.ib.api.sales.common.constant.Constants.PLDConstants.APPLICATION_DECLINED_STATUS;
import static com.lbg.ib.api.sales.common.constant.Constants.PLDConstants.CBS_SYSTEM_CODE;
import static com.lbg.ib.api.sales.common.constant.Constants.PLDConstants.ICOBS;
import static com.lbg.ib.api.sales.common.constant.Constants.PLDConstants.LIFE_STYLE_BENEFITS;
import static com.lbg.ib.api.sales.common.constant.Constants.PLDConstants.MNEMONIC_SYSTEM_CODE;
import static com.lbg.ib.api.sales.common.constant.Constants.PLDConstants.NUMBER_OF_RETRIES;
import static com.lbg.ib.api.sales.common.constant.Constants.PLDConstants.OFFERED_OVERDRAFT_AMOUNT;
import static com.lbg.ib.api.sales.common.constant.Constants.PLDConstants.OVERDRAFT_FACITLITY_CODE;
import static com.lbg.ib.api.sales.common.constant.Constants.PLDConstants.REFER;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.asm.domain.AppScoreRequest;
import com.lbg.ib.api.sales.asm.domain.ApplicationType;
import com.lbg.ib.api.sales.common.constant.AsmDecisionEnum;
import com.lbg.ib.api.sales.common.constant.RuleConditionParameters;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.b274.B274RequestDTO;
import com.lbg.ib.api.sales.dto.product.RetrieveProductDTO;
import com.lbg.ib.api.sales.dto.product.overdraft.OverdraftResponseDTO;
import com.lbg.ib.api.sales.pld.request.PldAppealRequest;
import com.lbg.ib.api.sales.product.domain.ArrangeToActivateParameters;
import com.lbg.ib.api.sales.product.domain.Condition;
import com.lbg.ib.api.sales.product.domain.PackageAccountSessionInfo;
import com.lbg.ib.api.sales.product.domain.SelectedProduct;
import com.lbg.ib.api.sales.product.domain.arrangement.Arranged;
import com.lbg.ib.api.sales.product.domain.arrangement.Overdraft;
import com.lbg.ib.api.sales.product.domain.arrangement.OverdraftIntrestRates;
import com.lbg.ib.api.sales.product.domain.domains.PldAppeal;
import com.lbg.ib.api.sales.product.domain.features.PldProductInfo;
import com.lbg.ib.api.sales.product.domain.pending.ModifyProductArrangement;
import com.lbg.ib.api.sales.product.service.ModifyProductArrangementService;
import com.lbg.ib.api.sales.product.service.RetrieveProductFeaturesService;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ExtSysProdIdentifier;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Product;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductOptions;
import com.lbg.ib.api.sales.soapapis.retrieveproduct.reqresp.RetrieveProductConditionsResponse;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lloydstsb.www.C078Resp;
import com.lloydstsb.www.FacilitiesOffered;
import com.lloydstsb.www.ProductsOffered;

/**
 * Created by Debashish Bhattacharjee on 29/05/2018.
 * a) Fetch the App Score for both CAAS and QWEL
 * b) If ASM is Refer Validate using the QWEL response if decision is Updated
 * c) Identify if the Product retured is same as that of the applied Product
 * No->Update the setArrangeToActivateParameters with Updated Product Id ( Fetch the non ICOBS and Non Life Style Benefit) and Family Id
 * Yes->Update the setArrangeToActivateParameters with Updated Product Id (from Selected Product)
 * d) If downsold product same as that of returned Product do nothing.
 */
@Component
public class PldAppealServiceImpl implements PldAppealService {

    @Autowired
    private C078Service service;

    @Autowired
    private SessionManagementDAO session;

    @Autowired
    private RetrieveProductFeaturesService retrieveProductFeaturesService;

    @Autowired
    private LoggerDAO logger;

    @Autowired
    private B274Service b274Service;

    @Autowired
    ModifyProductArrangementService modifyProductArrangementService;

    /* The invokeC078 is used to invoke the CO78 to fetch the App Score response based on Arrangement Id stored in Session

    * @param        AppScoreRequest
    * @return      The C078Resp
    */
    public PldProductInfo fetchAppScoreResultsAndUpdateDecision(PldAppealRequest pldAppealRequest) {
        logger.traceLog(this.getClass(),"Started the fetchAppScoreResultsAndUpdateDecision ");
        PldProductInfo pldProductInfo = null;

        ArrangeToActivateParameters arrangeToActivateParameters = session.getArrangeToActivateParameters();
        logger.traceLog(this.getClass(),"Before C078 with CASS 012");
        C078Resp response = callC078Service(arrangeToActivateParameters.getArrangementId(), ApplicationType.CAAS);
        logger.traceLog(this.getClass(),"After C078 with CASS 012");
        logger.traceLog(this.getClass(),"Before C078 with QWEL 025");
        C078Resp qwelResponse = callC078Service(arrangeToActivateParameters.getArrangementId(), ApplicationType.QWEL);
        logger.traceLog(this.getClass(),"After C078 with QWEL 025");

        if (AsmDecisionEnum.DECLINED.getAsmDecisionResultCd().equals(qwelResponse.getASMCreditScoreResultCd())) {
            logger.traceLog(this.getClass(),"ASM Decline");
            pldProductInfo = new PldProductInfo();
            pldProductInfo.setIsAsmDecisionDecline(true);
            com.lbg.ib.api.sales.product.domain.features.Product product = new com.lbg.ib.api.sales.product.domain.features.Product(
                    arrangeToActivateParameters.getProductName(), arrangeToActivateParameters.getProductId(),
                    arrangeToActivateParameters.getProductMnemonic(), arrangeToActivateParameters.getProductFamilyIdentifier(),
                    null, null);
            pldProductInfo.setProduct(product);
        } else {
            pldProductInfo = checkAndPrepareAppealDecisionFromASM(response, qwelResponse, arrangeToActivateParameters,pldAppealRequest);
        }
        return pldProductInfo;
    }

    private C078Resp callC078Service(String arrangementId, ApplicationType applicationType) {
        AppScoreRequest appScoreRequest = new AppScoreRequest();
        appScoreRequest.setCreditScoreRequestNo(arrangementId);
        appScoreRequest.setApplicationType(applicationType);
        return service.invokeC078(appScoreRequest);
    }

    private PldProductInfo checkAndPrepareAppealDecisionFromASM(C078Resp response, C078Resp qwelResponse, ArrangeToActivateParameters arrangeToActivateParameters,PldAppealRequest pldAppealRequest) {
        logger.traceLog(this.getClass(),"Started the checkAndPrepareAppealDecisionFromASM ");
        PldProductInfo pldProductInfo = new PldProductInfo();
        com.lbg.ib.api.sales.product.domain.features.Product product = updateDecisionInSession(response, arrangeToActivateParameters);

        //Updated Refer Updated
        logger.traceLog(this.getClass(),"Setting Is Refer Updated ");
        pldProductInfo.setIsReferUpdated(checkIfReferredDecisionUpdated(arrangeToActivateParameters, qwelResponse));

        pldProductInfo.setProduct(product);
        logger.traceLog(this.getClass(),"Setting the OverDraft Amount ");
        pldProductInfo.setAmtOverdraft(fetchOverDraftAmountAndUpdateSession(response,pldAppealRequest));

        //Checking if OverDraft is Updated
        boolean isOverDraftUpdatedFlag = isOverDraftUpdated(arrangeToActivateParameters, pldProductInfo);
        pldProductInfo.setIsAmtOverdraftUpdated(isOverDraftUpdatedFlag);

        //Check if Product is Updated
        pldProductInfo.setIsProductUpdated(isProductUpdated(arrangeToActivateParameters));

        logger.traceLog(this.getClass(),"Setting the IsAmtOverdraftUpdatedFromNull ");
        pldProductInfo.setIsAmtOverdraftUpdatedFromNull(IsAmtOverdraftUpdatedFromNull(arrangeToActivateParameters));
        logger.traceLog(this.getClass(),"Setting the update Packaged Account SessionInfo ");
        updatePackagedAccountSessionInfo(pldProductInfo);

        //Additional Step to convert any prepending 0
        convertFamilyIdentifierRemovePrependZeros(arrangeToActivateParameters);

        Integer debitCardRiskCode = fetchDebitCardRiskCode(response);
        //Update the OD Amount
        updateODAmountdInParameterStatus(pldProductInfo,debitCardRiskCode,pldAppealRequest.getApplicationStatus());
        return pldProductInfo;
    }

    public Integer fetchDebitCardRiskCode(C078Resp response){
        FacilitiesOffered[] facilitiesOfferedArray = response.getFacilitiesOffered();
        String debitCardRiskCode = null;
        if(facilitiesOfferedArray==null){
            return null;
        }
        
        for(FacilitiesOffered facilitiesOffered : facilitiesOfferedArray){
            if(facilitiesOffered!=null) {
                String csFacilityOfferedCd = facilitiesOffered.getCSFacilityOfferedCd();
                if ("00".equals(csFacilityOfferedCd.substring(0, 2))) {
                    debitCardRiskCode = csFacilityOfferedCd;
                }
            }
        }

        return debitCardRiskCode!=null?Integer.valueOf(debitCardRiskCode):null;
    }

    public void updateODAmountdInParameterStatus(PldProductInfo pldProductInfo, Integer debitCardRiskCode,
            String applicationStatus) {
        List<Condition> conditionList = new ArrayList<Condition>();

        ArrangeToActivateParameters arrangeToActivateParameters = session.getArrangeToActivateParameters();
        ModifyProductArrangement modifyProductArrangement = new ModifyProductArrangement();
        modifyProductArrangement.setArrangementType("CA");
        modifyProductArrangement.setArrangementId(arrangeToActivateParameters.getArrangementId());
        String currentApplicationStatus = arrangeToActivateParameters.getAppStatus();

        if (debitCardRiskCode != null) {
            Condition debitCardCondition = new Condition();
            debitCardCondition.setName(RuleConditionParameters.DEBIT_CARD_RISK_CODE.getKey());
            debitCardCondition.setValue(debitCardRiskCode.toString());
            conditionList.add(debitCardCondition);
        }

        // Update Application Status to Approved only when Product is Updated.
        if (APPLICATION_DECLINED_STATUS.equals(currentApplicationStatus) && applicationStatus != null && pldProductInfo.getIsProductUpdated()) {
            Condition applicationStatusondition = new Condition();
            applicationStatusondition.setName("APPLICATION_STATUS");
            applicationStatusondition.setValue(APPLICATION_APPROVED_STATUS);
            conditionList.add(applicationStatusondition);
        }

        if (pldProductInfo.getAmtOverdraft() != null) {
            Condition condition = new Condition();
            condition.setName(OFFERED_OVERDRAFT_AMOUNT);
            if (arrangeToActivateParameters.getAmtOverdraft() != null) {
                condition.setValue(pldProductInfo.getAmtOverdraft().toString());
            }
            conditionList.add(condition);
        }

        modifyProductArrangement.setConditions(conditionList.toArray(new Condition[conditionList.size()]));
        try {
            modifyProductArrangementService.modifyProductArrangement(modifyProductArrangement);
        } catch (Exception e) {
            logger.traceLog(this.getClass(), "Exception occured while updating features");
            logger.logException(this.getClass(), e);
        }

    }

    private void convertFamilyIdentifierRemovePrependZeros(ArrangeToActivateParameters arrangeToActivateParameters){
        if(arrangeToActivateParameters.getProductFamilyIdentifier()!=null) {
            arrangeToActivateParameters.setProductFamilyIdentifier(Integer.parseInt(arrangeToActivateParameters.getProductFamilyIdentifier()) + "");
        }
        logger.traceLog(this.getClass(),"convertFamilyIdentifierRemovePrependZeros arrangeToActivateParameters.getProductFamilyIdentifier() "+arrangeToActivateParameters.getProductFamilyIdentifier());
    }
    private boolean IsAmtOverdraftUpdatedFromNull(ArrangeToActivateParameters arrangeToActivateParameters){
        if(arrangeToActivateParameters.getOverdraft()==null && arrangeToActivateParameters.getOverdraftIntrestRates()!=null){
            return true;
        }

        return false;
    }
    @TraceLog
    private List<Product> fetchProducts(String updatedProductFamilyId) {
        RetrieveProductDTO retrieveProduct = new RetrieveProductDTO(null, updatedProductFamilyId);
        RetrieveProductConditionsResponse response = null;
        Integer attempts = 0;
        while (true) {
            try {
                response = retrieveProductFeaturesService.retrieveProductFromFamily(retrieveProduct);
                Product[] products = response.getProduct();
                return products!=null?Arrays.asList(products):null;

            } catch (ServiceException serviceException) {
                if (attempts == NUMBER_OF_RETRIES) {
                    logger.traceLog(this.getClass(), "Maximum number of retrying attempts reached");
                    throw serviceException;
                }
                attempts++;
                logger.traceLog(this.getClass(), "Fetch Product Conditions : Failed due to " + serviceException.getResponseError().getMessage() + " Retrying Attempt: " + attempts);
            }
        }
    }

    public boolean isAsmReferred(ArrangeToActivateParameters arrangeToActivateParameters) {
        if (REFER.equals(arrangeToActivateParameters.getAsmScore())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkIfReferredDecisionUpdated(ArrangeToActivateParameters arrangeToActivateParameters, C078Resp appScoreResponse) {
        boolean isAcceptDecision = false;
        if (isAsmReferred(arrangeToActivateParameters)) {
            if (AsmDecisionEnum.ACCEPT.getAsmDecisionResultCd().equals(appScoreResponse.getASMCreditScoreResultCd())) {
                isAcceptDecision = true;
            }
        }

        return isAcceptDecision;
    }

    public void updatePackagedAccountSessionInfo(PldProductInfo pldProductInfo) {
        PackageAccountSessionInfo packageAccountSessionInfo = session.getPackagedAccountSessionInfo();
        if (packageAccountSessionInfo != null) {
            PldAppeal pldAppeal = packageAccountSessionInfo.getPldAppeal();

            pldAppeal.setIsAmtOverdraftUpdated(pldProductInfo.getIsAmtOverdraftUpdated());
            pldAppeal.setAppealSuccess(true);
            pldAppeal.setIsProductUpdated(pldProductInfo.getIsProductUpdated());
            pldAppeal.setIsReferUpdated(pldProductInfo.getIsReferUpdated());
        }
    }

    public boolean isProductUpdated(ArrangeToActivateParameters arrangeToActivateParameters) {
        if (arrangeToActivateParameters.getOfferedProductId() != null && arrangeToActivateParameters.getProductId() != null) {
            if (!arrangeToActivateParameters.getOfferedProductId().equals(arrangeToActivateParameters.getProductId())) {
                return true;
            }
        }
        return false;
    }

    public boolean isOverDraftUpdated(ArrangeToActivateParameters arrangeToActivateParameters, PldProductInfo pldProductInfo) {
        boolean isOverDraftUpdatedFlag = false;
        if (arrangeToActivateParameters == null) {
            return isOverDraftUpdatedFlag;
        }
        if (arrangeToActivateParameters.getAmtOverdraft() != null && pldProductInfo.getAmtOverdraft() != null) {
            isOverDraftUpdatedFlag = (!arrangeToActivateParameters.getAmtOverdraft().equals(pldProductInfo.getAmtOverdraft()));
        } else if (pldProductInfo.getAmtOverdraft() != null) {
            isOverDraftUpdatedFlag = true;
        }
        return isOverDraftUpdatedFlag;
    }


    private BigDecimal fetchOverDraftAmountAndUpdateSession(C078Resp response,PldAppealRequest pldAppealRequest) {
        logger.traceLog(this.getClass(),"Started fetchOverDraftAmountAndUpdateSession");
        BigDecimal overDraftAmount = null;
        FacilitiesOffered[] facilitiesOffered = response.getFacilitiesOffered();
        if (facilitiesOffered != null) {
            for (FacilitiesOffered facility : facilitiesOffered) {
                if (facility != null && OVERDRAFT_FACITLITY_CODE.equals(facility.getCSFacilityOfferedCd())) {
                    String overDraftAmountStr = facility.getCSFacilityOfferedAm();
                    overDraftAmount = new BigDecimal(overDraftAmountStr);
                    overDraftAmount = overDraftAmount.divide(new BigDecimal(100));
                    break;
                }
            }
        }

        if (overDraftAmount != null) {
            logger.traceLog(this.getClass(),"overDraftAmount "+overDraftAmount);
            ArrangeToActivateParameters arrangeToActivateParameters = session.getArrangeToActivateParameters();
            OverdraftIntrestRates overdraftIntrestRates = arrangeToActivateParameters.getOverdraftIntrestRates();
            if (overdraftIntrestRates != null) {
                overdraftIntrestRates.setAmtOverdraft(overDraftAmount);
            } else {
                setOdAmountBasedOnPldAppealRequest(overDraftAmount,pldAppealRequest);
            }
        }
        logger.traceLog(this.getClass(),"Exiting fetchOverDraftAmountAndUpdateSession");
        return overDraftAmount;
    }
    
    /**
     * If PldAppealRequest isSkipOverdraft is true then no need not call B274
     * @param overDraftAmount
     * @param pldAppealRequest
     */
    
    private void setOdAmountBasedOnPldAppealRequest(BigDecimal overDraftAmount,PldAppealRequest pldAppealRequest) {
        if(!pldAppealRequest.isSkipOverdraft()) {
            logger.traceLog(this.getClass(),":::B274 will be called for "+overDraftAmount);
            B274RequestDTO b274RequestDTO = b274Service.mapB274Request(overDraftAmount);
            DAOResponse<OverdraftResponseDTO> overdraftResponseFromBAPI274 = b274Service.retrieveOverdraftInterstRates(b274RequestDTO);
            b274Service.updateB274ResponseToSession(overdraftResponseFromBAPI274, overDraftAmount);
        }else {
            logger.traceLog(this.getClass(),":::B274 will not be called for "+overDraftAmount);
            Overdraft overdraft = new Overdraft();
            overdraft.setAmount(overDraftAmount);
            final OverdraftIntrestRates overdraftIntrestRates = new OverdraftIntrestRates();
            overdraftIntrestRates.setAmtOverdraft(overDraftAmount);
            ArrangeToActivateParameters arrangeToActivateParameters = session.getArrangeToActivateParameters();
            arrangeToActivateParameters.setOverdraftIntrestRates(overdraftIntrestRates);
            arrangeToActivateParameters.setOverdraft(overdraft);
            if(session.getPackagedAccountSessionInfo()!=null) {
                session.getPackagedAccountSessionInfo().getOfferResponse().setOverdraft(overdraft);
            }
        }
        
    }
    

    private com.lbg.ib.api.sales.product.domain.features.Product updateDecisionInSession(C078Resp response, ArrangeToActivateParameters arrangeToActivateParameters) {
        ProductsOffered[] productsOffered = response.getProductsOffered();

        boolean productUpdatedFlag = false;
        //Check if PLD no Update Occured
        if (productsOffered != null) {
            List<String> offerProductCodeList = populateOfferProductCodeList(productsOffered);
            logger.traceLog(this.getClass(), "After fetching of the offered Product Code List.");
            String pdtFamily = validateProductUpdatedIsAppliedProduct(offerProductCodeList);
            if (pdtFamily==null) {

                // Identified the Product Family is the offered Product
                //Identify is the Product is the Offered Product
                logger.traceLog(this.getClass(), "About to trigger identify Offered Belongs To PLD Response .");
                ValidProductFamilyCodes validProductFamilyCodes = identifyOfferedBelongsToPLDResponseWithNoOtherFamilies(offerProductCodeList, session.getArrangeToActivateParameters());

                if (!validProductFamilyCodes.isFamilyAvailable()) {
                    // From there using the RPC identify the Standard Product
                    // Update arrangeToActivateParameters
                    logger.traceLog(this.getClass(), "About to trigger  update Session With Standard Product.");
                    updateSessionWithUpdatedStandardProduct(arrangeToActivateParameters, validProductFamilyCodes.getFamilyCodes());
                }else{
                    arrangeToActivateParameters.setProductFamilyIdentifier(validProductFamilyCodes.getFamilyCodes().get(0));
                }
            } else {
                arrangeToActivateParameters.setProductFamilyIdentifier(pdtFamily);
                updateSessionWithSelectedProduct(arrangeToActivateParameters);
            }

        }

        logger.traceLog(this.getClass(), "Exiting updateDecisionInSession.");

        return new com.lbg.ib.api.sales.product.domain.features.Product(arrangeToActivateParameters.getProductName(),
                arrangeToActivateParameters.getProductId(), arrangeToActivateParameters.getProductMnemonic(), arrangeToActivateParameters.getProductFamilyIdentifier(),
                null, null);

    }

    private void updateSessionWithUpdatedStandardProduct(ArrangeToActivateParameters arrangeToActivateParameters, List<String> offerProductCodeList) {
        logger.traceLog(this.getClass(), "About to trigger  update Session With Standard Product.");
        Product standardProduct = null;
        String updatedFamilyIdentifier = null;
        for (String familyIdentifier : offerProductCodeList) {
            standardProduct = fetchStandardProduct(familyIdentifier);
            updatedFamilyIdentifier = familyIdentifier;
            if (standardProduct != null) {
                break;
            }
        }

        if (standardProduct == null) {
            throw new ServiceException(new ResponseError("PLD Error", "Standard Product is null as no suitable product was found "));
        }

        arrangeToActivateParameters.setProductId(standardProduct.getProductIdentifier());
        arrangeToActivateParameters.setProductName(standardProduct.getProductName());
        String mnemonic = fetchMnemonic(standardProduct);
        arrangeToActivateParameters.setProductMnemonic(mnemonic);
        arrangeToActivateParameters.setProductFamilyIdentifier(updatedFamilyIdentifier);
        ExtSysProdIdentifier[] extSysProdIdentifier = standardProduct.getExternalSystemProductIdentifier();
        if (extSysProdIdentifier != null) {
            for (ExtSysProdIdentifier sysProdIdentifier : extSysProdIdentifier) {
                if (sysProdIdentifier != null && CBS_SYSTEM_CODE.equals(sysProdIdentifier.getSystemCode())) {
                    arrangeToActivateParameters.setCbsProductNumberTrimmed(sysProdIdentifier.getProductIdentifier().substring(0, 4));
                    break;
                }
            }
        }
        updatePackagedAccountSessionInfo(standardProduct.getProductName(), mnemonic);
    }

    private void updatePackagedAccountSessionInfo(String name, String mnemonic) {
        PackageAccountSessionInfo packageAccountSessionInfo = session.getPackagedAccountSessionInfo();
        if (packageAccountSessionInfo != null) {
            Arranged arranged = packageAccountSessionInfo.getOfferResponse();
            if (arranged != null) {
                arranged.setMnemonic(mnemonic);
                arranged.setName(name);
            }
        }
    }

    private void updateSessionWithSelectedProduct(ArrangeToActivateParameters arrangeToActivateParameters) {
        SelectedProduct selectedProduct = session.getSelectedProduct();
        arrangeToActivateParameters.setProductId(selectedProduct.getIdentifier());
        arrangeToActivateParameters.setProductName(selectedProduct.getName());
        arrangeToActivateParameters.setProductMnemonic(selectedProduct.getMnemonic());
        com.lbg.ib.api.sales.product.domain.features.ExternalProductIdentifier[] extSysProdIdentifier = selectedProduct.getExternalProductIdentifiers();
        updateCbsProductNumber(arrangeToActivateParameters, extSysProdIdentifier);
        updatePackagedAccountSessionInfo(selectedProduct.getName(), selectedProduct.getMnemonic());
    }

    public void updateCbsProductNumber(ArrangeToActivateParameters arrangeToActivateParameters, com.lbg.ib.api.sales.product.domain.features.ExternalProductIdentifier[] extSysProdIdentifier) {
        if (extSysProdIdentifier == null) {
            return;
        }
        for (com.lbg.ib.api.sales.product.domain.features.ExternalProductIdentifier sysProdIdentifier : extSysProdIdentifier) {
            if (sysProdIdentifier != null && CBS_SYSTEM_CODE.equals(sysProdIdentifier.getCode())) {
                arrangeToActivateParameters.setCbsProductNumberTrimmed(sysProdIdentifier.getId().substring(0, 4));
                break;
            }
        }
    }

    private String fetchMnemonic(Product standardProduct) {
        ExtSysProdIdentifier[] extSysProdIdentifiers = standardProduct.getExternalSystemProductIdentifier();
        if (extSysProdIdentifiers != null) {
            for (ExtSysProdIdentifier extSysProdIdentifier : extSysProdIdentifiers) {
                if (extSysProdIdentifier != null && MNEMONIC_SYSTEM_CODE.equals(extSysProdIdentifier.getSystemCode())) {
                    return extSysProdIdentifier.getProductIdentifier();
                }
            }
        }
        return null;
    }

    @TraceLog
    private Product fetchStandardProduct(String updatedProductFamilyId) {
        RetrieveProductDTO retrieveProduct = new RetrieveProductDTO(null, updatedProductFamilyId);
        RetrieveProductConditionsResponse response = null;
        Integer attempts = 0;
        while (true) {
            try {
                response = retrieveProductFeaturesService.retrieveProductFromFamily(retrieveProduct);
                return fetchStandardProductFromResponse(response);
            } catch (ServiceException serviceException) {
                if (attempts == NUMBER_OF_RETRIES) {
                    logger.traceLog(this.getClass(), "Maximum number of retrying attempts reached");
                    throw serviceException;
                }
                attempts++;
                logger.traceLog(this.getClass(), "Fetch Product Conditions : Failed due to " + serviceException.getResponseError().getMessage() + " Retrying Attempt: " + attempts);
            }
        }
    }

    private Product fetchStandardProductFromResponse(RetrieveProductConditionsResponse response) {
        Product[] products = response.getProduct();
        //Check that the Product does not contain any ICOBS and LifeStyleBenefits
        if (products != null) {
            for (Product product : products) {
                if (isStandardProduct(product)) {
                    return product;
                }
            }
        }

        return null;
    }

    private boolean isStandardProduct(Product product) {
        if (product != null) {
            ProductOptions[] productOptions = product.getProductoptions();
            boolean isStandardProduct = true;
            if (productOptions != null) {
                for (ProductOptions option : productOptions) {
                    if (option != null && (ICOBS.equals(option.getOptionsDescription()) || LIFE_STYLE_BENEFITS.equals(option.getOptionsDescription()))) {
                        isStandardProduct = false;
                    }
                }
            }
            return isStandardProduct;
        }
        return false;
    }

    private ValidProductFamilyCodes identifyOfferedBelongsToPLDResponseWithNoOtherFamilies(List<String> offerProductFamilyCodes, ArrangeToActivateParameters arrangeToActivateParameters) {
        String productFamilyCodes = null;
        ValidProductFamilyCodes validProductFamilyCodes = new ValidProductFamilyCodes();
        logger.traceLog(this.getClass(),"offerProductFamilyCodes "+offerProductFamilyCodes);
        for (String offerdProductFamilyCode : offerProductFamilyCodes) {
            List<Product> productList = fetchProducts(offerdProductFamilyCode);
            if(productList==null){
                continue;
            }
            boolean isFamilyAvailable = isFamilyIdentifierAvailableInOfferResponse(productList,arrangeToActivateParameters);
            if(isFamilyAvailable){
                logger.traceLog(this.getClass(),"isFamilyAvailable "+productFamilyCodes);
                productFamilyCodes = offerdProductFamilyCode;
            }

        }

        if(productFamilyCodes!=null && offerProductFamilyCodes.size()==1){
            logger.traceLog(this.getClass(),"productFamilyCodes is null and C078 returns the offered Product "+productFamilyCodes);
            validProductFamilyCodes.setFamilyAvailable(true);
            validProductFamilyCodes.setFamilyCodes(Arrays.asList(productFamilyCodes));
        }else{
            validProductFamilyCodes.setFamilyAvailable(false);
            offerProductFamilyCodes.remove(productFamilyCodes);
            logger.traceLog(this.getClass(),"productFamilyCodes is null and C078 returns other than the offered Product "+offerProductFamilyCodes);
            validProductFamilyCodes.setFamilyCodes(offerProductFamilyCodes);
        }
        return validProductFamilyCodes;
    }

    public boolean isFamilyIdentifierAvailableInOfferResponse(List<Product> productList,ArrangeToActivateParameters arrangeToActivateParameters){
        Boolean isFamilyAvailable = false;
        for (Product product : productList) {
            if(product==null){
                continue;
            }
            if (arrangeToActivateParameters.getOfferedProductId()!=null && product.getProductIdentifier()!=null && arrangeToActivateParameters.getOfferedProductId().equals(product.getProductIdentifier())) {
                isFamilyAvailable = true;
            }
        }

        return isFamilyAvailable;
    }

    @TraceLog
    public String validateProductUpdatedIsAppliedProduct(List<String> offerProductFamilyCodeList) {

        SelectedProduct selectedProduct = session.getSelectedProduct();
        for (String offeredProductFamilyCode : offerProductFamilyCodeList) {
            List<Product> productList = fetchProducts(offeredProductFamilyCode);
            if(productList==null){
                continue;
            }
            for (Product product : productList) {
                if (selectedProduct!=null && selectedProduct.getIdentifier()!=null && product.getProductIdentifier()!=null && selectedProduct.getIdentifier().equals(product.getProductIdentifier())) {
                    return offeredProductFamilyCode;
                }
            }

        }

        return null;
    }

    @TraceLog
    public List<String> populateOfferProductCodeList(ProductsOffered[] productsOffered) {
        List<String> offeredProductCodes = new ArrayList<String>();
        for (ProductsOffered offerProduct : productsOffered) {
            if(offerProduct!=null) {
                offeredProductCodes.add(offerProduct.getCSProductsOfferedCd());
            }
        }
        return offeredProductCodes;
    }


     class ValidProductFamilyCodes{
         boolean isFamilyAvailable;
         List<String> familyCodes;

         public List<String> getFamilyCodes() {
             return familyCodes;
         }

         public void setFamilyCodes(List<String> familyCodes) {
             this.familyCodes = familyCodes;
         }


         public boolean isFamilyAvailable() {
             return isFamilyAvailable;
         }

         public void setFamilyAvailable(boolean familyAvailable) {
             isFamilyAvailable = familyAvailable;
         }
    }
}

