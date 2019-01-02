/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.dao.product.offercrosssell;

import static com.lbg.ib.api.sales.dao.constants.DAOErrorConstants.BUSSINESS_ERROR;
import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;

import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.mapper.PromotionalCustomerInstructionRequestMapper;
import com.lbg.ib.api.sales.dto.product.eligibility.EligibilityRequestDTO;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.InstructionDetails;
import com.lbg.ib.api.sales.soapapis.determinepromotional.conditions.IA_DeterminePromotionalCustomerInstructions;
import com.lbg.ib.api.sales.soapapis.determinepromotional.reqres.DeterminePromotionalCustomerInstructionsRequest;
import com.lbg.ib.api.sales.soapapis.determinepromotional.reqres.DeterminePromotionalCustomerInstructionsResponse;

@Component
public class CrossSellEligibilityDAOImpl implements CrossSellEligibilityDAO {

    public static final Class<CrossSellEligibilityDAOImpl> CLASS_NAME  = CrossSellEligibilityDAOImpl.class;

    public static final String                             METHOD_NAME = "determineCrossSellEligibilityForCustomer";

    @Autowired
    private PromotionalCustomerInstructionRequestMapper    requestMapper;

    @Autowired
    private LoggerDAO                                      logger;

    @Autowired
    private IA_DeterminePromotionalCustomerInstructions    promotionalCustomerInstructionsService;

    @Autowired
    private DAOExceptionHandler                            exceptionHandler;

    @TraceLog
    public DAOResponse<TreeMap<String, String>> determineCrossSellEligibilityForCustomer(
            EligibilityRequestDTO promotionalEligibilityrequest) {
        try {
            DeterminePromotionalCustomerInstructionsResponse response = null;
            DeterminePromotionalCustomerInstructionsRequest request = requestMapper
                    .mapRequest(promotionalEligibilityrequest);
            response = promotionalCustomerInstructionsService.determinePromotionalCustomerInstructions(request);
            DAOError error = validateResponse(response);
            if (error == null) {
                TreeMap<String, String> instructionDetailsMap = new TreeMap<String, String>();
                if (response != null && response.getInstructionDetails() != null) {
                    InstructionDetails[] instructionDetails = response.getInstructionDetails();
                    if (instructionDetails != null && instructionDetails.length > 0) {
                        populateInstructionMap(instructionDetailsMap,
								instructionDetails);
                        return withResult(instructionDetailsMap);
                    }
                }
            } else {
                return withError(error);
            }
        } catch (Exception ex) {
            DAOError daoError = exceptionHandler.handleException(ex, CLASS_NAME, METHOD_NAME,
                    promotionalEligibilityrequest);
            return withError(daoError);
        }
        return null;

    }

	/**
	 * @param instructionDetailsMap
	 * @param instructionDetails
	 */
	private void populateInstructionMap(
			TreeMap<String, String> instructionDetailsMap,
			InstructionDetails[] instructionDetails) {
		for (InstructionDetails instructions : instructionDetails) {
		    String instructionPriority = instructions.getPriority();
		    String instructionMnemonic = instructions.getInstructionMnemonic();
		    instructionDetailsMap.put(instructionPriority, instructionMnemonic);
		}
	}

    private DAOError validateResponse(DeterminePromotionalCustomerInstructionsResponse response) {
        if (response != null && response.getInstructionDetails() == null) {
            DAOError error = new DAOError(BUSSINESS_ERROR,
                    "Cross sell product details cannot found in determine promotional response");
            logger.logError(error.getErrorCode(), error.getErrorMessage(), this.getClass());
            return error;
        }
        return null;
    }

    public void setRequestMapper(PromotionalCustomerInstructionRequestMapper requestMapper) {
        this.requestMapper = requestMapper;
    }

    public void setService(IA_DeterminePromotionalCustomerInstructions promotionalCustomerInstructionsService) {
        this.promotionalCustomerInstructionsService = promotionalCustomerInstructionsService;
    }
}
