/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.dao.product.eligibility;

import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static com.lbg.ib.api.sales.dao.constants.DAOErrorConstants.REMOTE_EXCEPTION;

import java.rmi.RemoteException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.mapper.PcaEligibilityRequestMapper;
import com.lbg.ib.api.sales.dto.product.eligibility.PcaEligibilityRequestDTO;
import com.lbg.ib.api.sales.product.domain.eligibility.EligibilityDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Product;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductEligibilityDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ReasonCode;
import com.lbg.ib.api.sales.soapapis.determineeligibility.reqres.DetermineEligibleCustomerInstructionsRequest;
import com.lbg.ib.api.sales.soapapis.determineeligibility.reqres.DetermineEligibleCustomerInstructionsResponse;
import com.lbg.ib.sales.soapapis.determineeligibility.conditions.IA_DetermineEligibleCustomerInstructions;

@Component
public class PcaEligibilityDAOImpl implements PcaEligibiltyDAO {

    public static final Class<EligibilityDAOImpl>    CLASS_NAME = EligibilityDAOImpl.class;

    @Autowired
    private PcaEligibilityRequestMapper              requestMapper;

    @Autowired
    private LoggerDAO                                logger;

    @Autowired
    private IA_DetermineEligibleCustomerInstructions eligibilityService;

    @TraceLog
    public DAOResponse<HashMap<String, EligibilityDetails>> determineEligibility(
            PcaEligibilityRequestDTO customerInstructionReqDTO) {
        try {
            DetermineEligibleCustomerInstructionsResponse response = null;
            DetermineEligibleCustomerInstructionsRequest request = requestMapper
                    .populateRequest(customerInstructionReqDTO);
            response = eligibilityService.determineEligibleCustomerInstructions(request);
            return withResult(mapResponse(response));
        } catch (RemoteException e) {
            logger.logException(CLASS_NAME, e);
            return withError(new DAOError(REMOTE_EXCEPTION,
                    "Remote connection exception in determine eligible customer instructions service"));
        }
    }

    @TraceLog
    public DAOResponse<HashMap<String, EligibilityDetails>> determineEligibilityAuth(
            PcaEligibilityRequestDTO customerInstructionReqDTO) {
        try {
            DetermineEligibleCustomerInstructionsResponse response = null;
            DetermineEligibleCustomerInstructionsRequest request = requestMapper
                    .populateRequest(customerInstructionReqDTO);
            response = eligibilityService.determineEligibleCustomerInstructions(request);
            return withResult(mapResponseAuth(response));
        } catch (RemoteException e) {
            logger.logException(CLASS_NAME, e);
            return withError(new DAOError(REMOTE_EXCEPTION,
                    "Remote connection exception in determine eligible customer instructions service"));
        }
    }

    /**
     * This method maps the Instruction Mnemonics and Eligibility flag in a hash
     * map and returns it to the DAOResponse The Hash Map has the Mnemonics as
     * its key and Eligibility flag as value
     */
    private HashMap<String, EligibilityDetails> mapResponseAuth(
            DetermineEligibleCustomerInstructionsResponse response) {
        HashMap<String, EligibilityDetails> eligibleProductsMap = new HashMap<String, EligibilityDetails>();
        ProductEligibilityDetails[] productDetails = response.getProductEligibilityDetails();
        EligibilityDetails details = null;
        /**
         * This for loop iterates the ProductEligibilityDetails and gets the
         * eligible product and Product[]
         */
        for (ProductEligibilityDetails productEligibilityDetails : productDetails) {
            details = new EligibilityDetails();
            details.setIsEligible(new Boolean(productEligibilityDetails.getIsEligible()));
            if (productDetails[0].getDeclineReasons() != null && productDetails[0].getDeclineReasons().length > 0) {
                ReasonCode reason = response.getProductEligibilityDetails(0).getDeclineReasons()[0];
                details.setCode(reason.getCode());
                details.setDesc(reason.getDescription());
            }
            Product[] products = productEligibilityDetails.getProduct();
            // This for loop iterates the Instruction Mnemonic from Product[] //
            for (Product product : products) {
                details.setMnemonic(product.getInstructionDetails().getInstructionMnemonic());
                eligibleProductsMap.put(product.getInstructionDetails().getInstructionMnemonic(), details);
            }
        }
        return eligibleProductsMap;
    }

    /**
     * This method maps the Instruction Mnemonics and Eligibility flag in a hash
     * map and returns it to the DAOResponse The Hash Map has the Mnemonics as
     * its key and Eligibility flag as value
     */
    private HashMap<String, EligibilityDetails> mapResponse(DetermineEligibleCustomerInstructionsResponse response) {
        HashMap<String, EligibilityDetails> eligibleProductsMap = new HashMap<String, EligibilityDetails>();
        ProductEligibilityDetails[] productDetails = response.getProductEligibilityDetails();
        EligibilityDetails details = null;
        /**
         * This for loop iterates the ProductEligibilityDetails and gets the
         * eligible product and Product[]
         */
        if (productDetails != null) {
            for (ProductEligibilityDetails productEligibilityDetails : productDetails) {
                details = new EligibilityDetails();
                details.setIsEligible(new Boolean(productEligibilityDetails.getIsEligible()));
                Product[] products = productEligibilityDetails.getProduct();
                // This for loop iterates the Instruction Mnemonic from
                // Product[] //
                for (Product product : products) {
                    details.setMnemonic(product.getInstructionDetails().getInstructionMnemonic());
                    ReasonCode[] reasonCodes = productEligibilityDetails.getDeclineReasons();
                    if (reasonCodes != null && reasonCodes.length > 0) {
                        details.setCode(reasonCodes[0].getCode());
                        details.setDesc(reasonCodes[0].getDescription());
                    }
                    // Special Case for Basic Handling.
                    if ("P_NEW_BASIC".equals(details.getMnemonic())) {
                        details.setOnlineProduct(false);
                    }
                    eligibleProductsMap.put(product.getInstructionDetails().getInstructionMnemonic(), details);
                }
            }
        }
        return eligibleProductsMap;
    }

    public void setRequestMapper(PcaEligibilityRequestMapper requestMapper) {
        this.requestMapper = requestMapper;
    }

    public void setEligibilityService(IA_DetermineEligibleCustomerInstructions eligibilityService) {
        this.eligibilityService = eligibilityService;
    }
}
