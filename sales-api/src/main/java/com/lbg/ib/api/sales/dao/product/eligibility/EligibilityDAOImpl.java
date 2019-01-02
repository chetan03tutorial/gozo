/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.dao.product.eligibility;

import static com.lbg.ib.api.sales.dao.constants.DAOErrorConstants.BUSSINESS_ERROR;
import static com.lbg.ib.api.sales.dao.constants.DAOErrorConstants.REMOTE_EXCEPTION;
import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;

import java.rmi.RemoteException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.mapper.EligibilityRequestMapper;
import com.lbg.ib.api.sales.dto.product.eligibility.EligibilityRequestDTO;
import com.lbg.ib.api.sales.product.domain.eligibility.EligibilityDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Product;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductEligibilityDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ReasonCode;
import com.lbg.ib.api.sales.soapapis.determineeligibility.reqres.DetermineEligibleCustomerInstructionsRequest;
import com.lbg.ib.api.sales.soapapis.determineeligibility.reqres.DetermineEligibleCustomerInstructionsResponse;
import com.lbg.ib.sales.soapapis.determineeligibility.conditions.IA_DetermineEligibleCustomerInstructions;

@Component
public class EligibilityDAOImpl implements EligibiltyDAO {

    public static final Class<EligibilityDAOImpl>    CLASS_NAME = EligibilityDAOImpl.class;

    @Autowired
    private EligibilityRequestMapper                 requestMapper;

    @Autowired
    private LoggerDAO                                logger;

    @Autowired
    private IA_DetermineEligibleCustomerInstructions eligibilityService;

    @TraceLog
    public DAOResponse<HashMap<String, EligibilityDetails>> determineEligibility(
            EligibilityRequestDTO customerInstructionReqDTO) {
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
    public DAOResponse<HashMap<String, String>> determineEligibleCustomerInstructions(
            EligibilityRequestDTO customerInstructionReqDTO) {
        try {

            DetermineEligibleCustomerInstructionsResponse response = null;

            DetermineEligibleCustomerInstructionsRequest request = requestMapper.mapRequest(customerInstructionReqDTO);
            response = eligibilityService.determineEligibleCustomerInstructions(request);
            DAOError error = validateResponse(response);
            if (error == null) {
                return withResult(mapDetermineEligibleCustomersFromResponse(response));
            } else {
                return withError(error);
            }
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
    private HashMap<String, String> mapDetermineEligibleCustomersFromResponse(
            DetermineEligibleCustomerInstructionsResponse response) {
        HashMap<String, String> eligibleProductsMap = new HashMap<String, String>();
        ProductEligibilityDetails[] productDetails = response.getProductEligibilityDetails();
        String mnemonic = null;
        String eligibleProduct = null;
        /**
         * This for loop iterates the ProductEligibilityDetails and gets the
         * eligible product and Product[]
         */
        for (ProductEligibilityDetails productEligibilityDetails : productDetails) {
            eligibleProduct = productEligibilityDetails.getIsEligible();
            Product[] products = productEligibilityDetails.getProduct();
            // This for loop iterates the Instruction Mnemonic from Product[] //
            for (Product product : products) {
                mnemonic = product.getInstructionDetails().getInstructionMnemonic();
                eligibleProductsMap.put(mnemonic, eligibleProduct);
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
		for (ProductEligibilityDetails productEligibilityDetails : productDetails) {
			details = new EligibilityDetails();
			details.setIsEligible(new Boolean(productEligibilityDetails
					.getIsEligible()));
			if (response != null
					&& response.getProductEligibilityDetails().length > 0
					&& response.getProductEligibilityDetails(0) != null
					&& response.getProductEligibilityDetails(0)
							.getDeclineReasons() != null) {
				if (response.getProductEligibilityDetails(0)
						.getDeclineReasons().length > 0) {
					ReasonCode reason = response
							.getProductEligibilityDetails(0)
							.getDeclineReasons()[0];
					details.setCode(reason.getCode());
					details.setDesc(reason.getDescription());

				}
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

    private DAOError validateResponse(DetermineEligibleCustomerInstructionsResponse response) {
        if (response != null && response.getProductEligibilityDetails() == null) {
            DAOError error = new DAOError(BUSSINESS_ERROR,
                    "Product eligiblity details cannot found in determine eligiblity response");
            logger.logError(error.getErrorCode(), error.getErrorMessage(), this.getClass());
            return error;
        }
        if (response != null
				&& response.getProductEligibilityDetails().length > 0
				&& response.getProductEligibilityDetails(0) != null
				&& response.getProductEligibilityDetails(0).getDeclineReasons() != null) {
			if (response.getProductEligibilityDetails(0).getDeclineReasons().length > 0) {
				ReasonCode reason = response.getProductEligibilityDetails(0)
						.getDeclineReasons()[0];
				logger.logError(reason.getCode(), reason.getDescription(),
						this.getClass());
				return new DAOError(reason.getCode(), reason.getDescription());

			}

		}
        if (response != null && response.getResultCondition() != null
                && response.getResultCondition().getReasonCode() != null) {
            DAOError error = new DAOError(response.getResultCondition().getReasonCode(),
                    response.getResultCondition().getReasonText());
            logger.logError(response.getResultCondition().getReasonCode(),
                    response.getResultCondition().getReasonText(), this.getClass());
            return error;
        }
        return null;
    }


    public void setRequestMapper(EligibilityRequestMapper requestMapper) {
        this.requestMapper = requestMapper;
    }

    public void setEligibilityService(IA_DetermineEligibleCustomerInstructions eligibilityService) {
        this.eligibilityService = eligibilityService;
    }
}
