/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.dao.product.eligibility;

import java.util.HashMap;

import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.dto.product.eligibility.EligibilityRequestDTO;
import com.lbg.ib.api.sales.product.domain.eligibility.EligibilityDetails;

public interface EligibiltyDAO {
    DAOResponse<HashMap<String, String>> determineEligibleCustomerInstructions(
            EligibilityRequestDTO customerInstructionReqDTO);

    DAOResponse<HashMap<String, EligibilityDetails>> determineEligibility(
            EligibilityRequestDTO customerInstructionReqDTO);
}
