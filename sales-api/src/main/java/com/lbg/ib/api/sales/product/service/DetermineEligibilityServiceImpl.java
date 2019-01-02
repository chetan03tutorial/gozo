/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.product.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.dao.product.eligibility.EligibiltyDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.product.eligibility.EligibilityRequestDTO;
import com.lbg.ib.api.sales.product.domain.arrangement.PrimaryInvolvedParty;
import com.lbg.ib.api.sales.product.domain.eligibility.DetermineEligibilityRequest;
import com.lbg.ib.api.sales.product.domain.eligibility.DetermineEligibilityResponse;
import com.lbg.ib.api.sales.product.domain.eligibility.EligibilityDetails;

@Service
public class DetermineEligibilityServiceImpl implements DetermineEligibilityService {

    public static final String   APPLICATION_STATUS_DECLINED = "1004";

    public static final String   IB_ERROR_CODE               = "IB_ERROR_CODE";

    private static final String  BLANK                       = "";

    private static final String  SUCCESS_MSG                 = "Successfully fetched the eligibility details.";

    private EligibiltyDAO        customerEligibiltyDAO;

    @Autowired
    private SessionManagementDAO session;

    @Autowired
    public DetermineEligibilityServiceImpl(EligibiltyDAO customerEligibiltyDAO) {
        this.customerEligibiltyDAO = customerEligibiltyDAO;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.lbg.ib.api.sales.product.service.DetermineEligibilityService#
     * determineEligiblity (com.lbg.ib.api.sales.product.domain.eligibility.
     * DetermineEligibilityRequest )
     */
    @TraceLog
    public DetermineEligibilityResponse determineEligiblity(DetermineEligibilityRequest determineEligibilityRequest)
            throws ServiceException {
        String defaultMnemonicVantage = "P_CLSCVTG";
        DetermineEligibilityResponse serviceResponse = null;
        DAOResponse<HashMap<String, EligibilityDetails>> productMap;
        Boolean isVantageEligiblie = false;
        if (null != session && null != session.getArrangeToActivateParameters()
                && null != session.getArrangeToActivateParameters().getAlternateVantageMnemonic()
                && !BLANK.equals(session.getArrangeToActivateParameters().getAlternateVantageMnemonic())) {

            defaultMnemonicVantage = session.getArrangeToActivateParameters().getAlternateVantageMnemonic();
            determineEligibilityRequest.getMnemonic()
                    .add(session.getArrangeToActivateParameters().getAlternateVantageMnemonic());
        }
        productMap = customerEligibiltyDAO.determineEligibility(populateCustomerInstructionReqDTO(
                determineEligibilityRequest.getPrimaryInvolvedParty(),
                determineEligibilityRequest.getArrangementType().name(), determineEligibilityRequest.getMnemonic()));
        if (null != productMap) {
            if (null == productMap.getError() && null != productMap.getResult() && !productMap.getResult().isEmpty()) {
                serviceResponse = new DetermineEligibilityResponse();
                serviceResponse.setMsg(SUCCESS_MSG);
                List<EligibilityDetails> eligibilityDetailsList = new ArrayList<EligibilityDetails>();
                if (productMap.getResult().containsKey(defaultMnemonicVantage)) {
                    isVantageEligiblie = productMap.getResult().get(defaultMnemonicVantage).getIsEligible();
                    for (Map.Entry<String, EligibilityDetails> entry : productMap.getResult().entrySet()) {
                        if (!entry.getKey().equalsIgnoreCase(defaultMnemonicVantage)) {
                            entry.getValue().setIsVantageEligible(isVantageEligiblie);
                            eligibilityDetailsList.add(entry.getValue());
                            break;
                        }
                    }
                } else {
                    eligibilityDetailsList.addAll(productMap.getResult().values());

                }

                serviceResponse.setEligibilityDetails(eligibilityDetailsList);
            }
        }
        return serviceResponse;
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
    private EligibilityRequestDTO populateCustomerInstructionReqDTO(PrimaryInvolvedParty primaryInvolvedParty,
            String arrangementType, List<String> mnemonic) {
        String[] mnenomicArray = mnemonic.toArray(new String[mnemonic.size()]);
        return new EligibilityRequestDTO(arrangementType, primaryInvolvedParty, mnenomicArray, null);
    }

    public void setSession(SessionManagementDAO session) {
        this.session = session;
    }

}
