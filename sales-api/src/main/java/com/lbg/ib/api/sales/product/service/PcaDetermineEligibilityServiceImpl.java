/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.product.service;

import com.google.common.collect.Lists;
import com.lbg.ib.api.sales.application.Base;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.sales.dao.product.eligibility.PcaEligibiltyDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.sales.dto.product.eligibility.PcaEligibilityRequestDTO;
import com.lbg.ib.api.sales.dto.product.suitability.SuitabilityProductOptions;
import com.lbg.ib.api.sso.domain.product.Indicator;
import com.lbg.ib.api.sales.product.domain.eligibility.DetermineEligibilityResponse;
import com.lbg.ib.api.sales.product.domain.eligibility.EligibilityDetails;
import com.lbg.ib.api.sales.product.domain.eligibility.PcaDetermineEligibilityRequest;
import com.lbg.ib.api.sales.product.domain.eligibility.PcaDetermineEligibilityResponse;
import com.lbg.ib.api.sales.product.domain.suitability.ProductQualiferOptions;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import com.lbg.ib.api.sso.domain.user.PrimaryInvolvedParty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class PcaDetermineEligibilityServiceImpl extends Base implements PcaDetermineEligibilityService {

    public static final String            APPLICATION_STATUS_DECLINED = "1004";

    public static final String            IB_ERROR_CODE               = "IB_ERROR_CODE";

    private static final String           SUCCESS_MSG                 = "Successfully fetched the eligibility details.";

    private PcaEligibiltyDAO              customerEligibiltyDAO;

    private SessionManagementDAO          session;

    private final GalaxyErrorCodeResolver resolver;

    private ConfigurationDAO              configuration;

    private ChannelBrandingDAO            brandingDAO;

    private static final Class            CLASS_VALUE                 = PcaDetermineEligibilityServiceImpl.class;


    @Autowired
    public PcaDetermineEligibilityServiceImpl(PcaEligibiltyDAO customerEligibiltyDAO, SessionManagementDAO session,
            GalaxyErrorCodeResolver resolver, ConfigurationDAO configuration, ChannelBrandingDAO brandingDAO) {
        this.customerEligibiltyDAO = customerEligibiltyDAO;
        this.session = session;
        this.resolver = resolver;
        this.configuration = configuration;
        this.brandingDAO = brandingDAO;
    }

    @TraceLog
    public PcaDetermineEligibilityResponse determineEligiblitySuitability(
            PcaDetermineEligibilityRequest determineEligibilityRequest) throws ServiceException {
        logger.traceLog(this.getClass(), "About to perform determineEligiblity ");
        String brand = getBrand();
        PcaDetermineEligibilityResponse serviceResponse = determineEligiblity(determineEligibilityRequest, brand);
        logger.traceLog(this.getClass(), "Completed determineEligiblity ");

        logger.traceLog(this.getClass(), "About to perfrom performSuitability ");
        // removed as suitability is performed at client side now
        List<ProductQualiferOptions> productOptions = determineEligibilityRequest.getProductOptions();
        if(productOptions!=null && productOptions.get(0)!=null){
            performSuitability(productOptions.get(0),serviceResponse,brand);
        }

        boolean bankruptcyIndicator = checkBankruptcyIndicator(session.getUserInfo());
        serviceResponse.setBankruptcyIndicator(bankruptcyIndicator);
        logger.traceLog(this.getClass(), "Completed performSuitability ");

        // Filtering the Response
        // removed as suitability is performed at client side now
        if(productOptions!=null && productOptions.get(0)!=null){
            filterProducts(serviceResponse);
        }
        // End of Filtering the Response
        return serviceResponse;
    }

    public void filterProducts(PcaDetermineEligibilityResponse serviceResponse){
        List<String> productList = serviceResponse.getSuitableProducts();
        List<EligibilityDetails> list = serviceResponse.getProducts();
        for(EligibilityDetails eligibilityDetails : list){
           if(productList.contains(eligibilityDetails.getMnemonic())){
               eligibilityDetails.setIsSuitalble(true);
           }
        }
   }

    public void performSuitability(ProductQualiferOptions productOptions,PcaDetermineEligibilityResponse serviceResponse,String brand)throws ServiceException {
        boolean bankruptcyIndicator = checkBankruptcyIndicator(session.getUserInfo());
        if(productOptions!=null){
            List<String> values = productOptions.getValues();
            if(values!=null){
                String productValue = values.get(0);

                if(serviceResponse!=null) {
                    List<String> productList = determineSuitability(productValue, serviceResponse.getProducts(), brand,bankruptcyIndicator);
                    serviceResponse.setSuitableProducts(productList);
                }
            }else{
                throw new ServiceException(resolver.resolve("9900010"));
            }
        }else{
            throw new ServiceException(resolver.resolve("9900020"));
        }
    }


    public List<String> determineSuitability(String productValue, List<EligibilityDetails> eligibilityDetailsList,
            String brand, boolean bankruptcyIndicator) throws ServiceException {
        SuitabilityProductOptions suitabilityProductOptions = SuitabilityProductOptions
                .getProductSuitabilityOption(brand);
        if (suitabilityProductOptions == null) {
            throw new ServiceException(resolver.resolve("9900032"));
        }
        suitabilityProductOptions.setConfiguration(configuration);
        return suitabilityProductOptions.getSuitabilityProduct(suitabilityProductOptions, eligibilityDetailsList, brand,
                productValue, bankruptcyIndicator);

    }

    @TraceLog
    public PcaDetermineEligibilityResponse determineEligiblity(
            PcaDetermineEligibilityRequest determineEligibilityRequest, String brand) throws ServiceException {

        Arrangement arrangement = null;
        if (null != determineEligibilityRequest.getExistingCustomer()
                && determineEligibilityRequest.getExistingCustomer()) {
            arrangement = session.getUserInfo();
            if (null == arrangement) {
                throw new ServiceException(resolver.resolve("9900010"));
            } else if (!eligibilityPreCheck(arrangement)) {
                throw new ServiceException(resolver.resolve("9900020"));
            }
        } else {
            arrangement = new Arrangement();
            PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
            primaryInvolvedParty.setDob(determineEligibilityRequest.getDob());
            arrangement.setPrimaryInvolvedParty(primaryInvolvedParty);
        }
        return getPcaDetermineEligibilityResponse(determineEligibilityRequest, brand, arrangement,
                arrangement.getAccounts(), null);
    }

    @TraceLog
    public PcaDetermineEligibilityResponse determineUpgradeEligibility(PcaDetermineEligibilityRequest determineEligibilityRequest,
                                                                       Arrangement arrangement, List<Account> accounts, List<String> mnemonics) throws ServiceException {
        String brand = getBrand();
        return getPcaDetermineEligibilityResponse(determineEligibilityRequest, brand, arrangement, accounts, mnemonics);
    }

    private PcaDetermineEligibilityResponse getPcaDetermineEligibilityResponse(
            PcaDetermineEligibilityRequest determineEligibilityRequest, String brand,
            Arrangement arrangement,  List<Account> accounts, List<String> mnemonics) {
        PcaDetermineEligibilityResponse serviceResponse = null;
        DAOResponse<HashMap<String, EligibilityDetails>> productMap = null;
        productMap = customerEligibiltyDAO.determineEligibility(
                populateCustomerInstructionReqDTO(determineEligibilityRequest, arrangement, accounts, brand, mnemonics));
        if (null != productMap) {
            if (null == productMap.getError() && null != productMap.getResult() && !productMap.getResult().isEmpty()) {
                serviceResponse = new PcaDetermineEligibilityResponse();
                serviceResponse.setMsg(SUCCESS_MSG);

                List<EligibilityDetails> eligibilityDetailsList = new ArrayList<EligibilityDetails>();
                eligibilityDetailsList.addAll(productMap.getResult().values());
                serviceResponse.setProducts(eligibilityDetailsList);
            }
        }
        return serviceResponse;
    }

    public String getBrand() throws ServiceException {
        DAOResponse<ChannelBrandDTO> brandDto = brandingDAO.getChannelBrand();
        DAOResponse.DAOError error = brandDto.getError();
        if (error != null) {
            throw new ServiceException(resolver.resolve(error.getErrorCode()));
        }
        return brandDto.getResult().getBrand();
    }

    /*
     * Method to populate EligibilityRequestDTO
     *
     * @param arrangement
     *
     * @param result
     *
     * @param vantageMnemonic
     *
     * @return EligibilityRequestDTO
     */
    private PcaEligibilityRequestDTO populateCustomerInstructionReqDTO(
            PcaDetermineEligibilityRequest determineEligibilityRequest, Arrangement arrangement,
            List<Account> accounts, String brand, List<String> inputMnemonics) {

        List<Object> mnemonics;
        if(inputMnemonics == null || inputMnemonics.isEmpty()) {
            Map<String, Object> map = configuration.getConfigurationItems("ProductGroupMnemonics");
            mnemonics = new ArrayList<Object>(map.values());
        } else {
            mnemonics = new ArrayList<Object>();
            for(String mnemonic : inputMnemonics) {
                mnemonics.add(mnemonic);
            }
        }

        String[] mnenomicArray = new String[mnemonics.size()];
        int i = 0;
        for (Object mnemonic : mnemonics) {
            mnenomicArray[i++] = mnemonic.toString();
        }
        PcaEligibilityRequestDTO pcaEligibilityRequestDTO = new PcaEligibilityRequestDTO(
                determineEligibilityRequest.getArrangementType().toString(), arrangement.getPrimaryInvolvedParty(),
                mnenomicArray, accounts);
        pcaEligibilityRequestDTO.setBrand(brand);
        return pcaEligibilityRequestDTO;
    }

    @TraceLog
    public DetermineEligibilityResponse determineAuthEligiblity(
            PcaDetermineEligibilityRequest determineEligibilityRequest) throws ServiceException {

        String brand = getBrand();
        DetermineEligibilityResponse serviceResponse = null;
        Arrangement arrangement = null;
        if (null != determineEligibilityRequest.getExistingCustomer()
                && determineEligibilityRequest.getExistingCustomer()) {
            arrangement = session.getUserInfo();
            if (null == arrangement) {
                throw new ServiceException(resolver.resolve("9900010"));
            }
        } else {
            arrangement = new Arrangement();
            PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
            primaryInvolvedParty.setDob(determineEligibilityRequest.getDob());
            arrangement.setPrimaryInvolvedParty(primaryInvolvedParty);
        }

        DAOResponse<HashMap<String, EligibilityDetails>> productMap = null;
        productMap = customerEligibiltyDAO.determineEligibilityAuth(
                populateAuthCustomerInstructionReqDTO(determineEligibilityRequest, arrangement, brand));
        if (null != productMap) {
            if (null == productMap.getError() && null != productMap.getResult() && !productMap.getResult().isEmpty()) {
                serviceResponse = new DetermineEligibilityResponse();
                serviceResponse.setMsg(SUCCESS_MSG);

                List<EligibilityDetails> eligibilityDetailsList = new ArrayList<EligibilityDetails>();
                eligibilityDetailsList.addAll(productMap.getResult().values());
                serviceResponse.setEligibilityDetails(eligibilityDetailsList);
            }
        }
        return serviceResponse;
    }

    public PcaEligibilityRequestDTO populateAuthCustomerInstructionReqDTO(
            PcaDetermineEligibilityRequest determineEligibilityRequest, Arrangement arrangement, String brand) {

        PcaEligibilityRequestDTO pcaEligibilityRequestDTO = new PcaEligibilityRequestDTO(
                determineEligibilityRequest.getArrangementType().toString(), arrangement.getPrimaryInvolvedParty(),
                determineEligibilityRequest.getCandidateInstructions()
                        .toArray(new String[determineEligibilityRequest.getCandidateInstructions().size()]),
                arrangement.getAccounts());
        pcaEligibilityRequestDTO.setBrand(brand);
        return pcaEligibilityRequestDTO;
    }

    public boolean eligibilityPreCheck(Arrangement arrangement) {
        Map<String, Object> map = configuration.getConfigurationItems("IndicatorsForDetrmineEligibility");
        if (arrangement.getAccounts() != null) {
            for (Account account : arrangement.getAccounts()) {
                if (account.getIndicators() != null) {
                    for (Indicator indicator : account.getIndicators()) {
                        if (map.containsValue(indicator.getIndicatorCode())) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean checkBankruptcyIndicator(Arrangement arrangement) {
        if (arrangement == null) {
            return false;
        }
        Map<String, Object> map = configuration.getConfigurationItems("BankruptcyDetrmineEligibility");

        if (arrangement.getAccounts() != null) {
            for (Account account : arrangement.getAccounts()) {
                if (account.getIndicators() != null) {
                    for (Indicator indicator : account.getIndicators()) {
                        if (map.containsValue(indicator.getIndicatorCode())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
