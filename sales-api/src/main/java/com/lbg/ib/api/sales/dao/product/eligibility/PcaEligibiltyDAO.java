/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.dao.product.eligibility;

import java.util.HashMap;

import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.dto.product.eligibility.EligibilityRequestDTO;
import com.lbg.ib.api.sales.dto.product.eligibility.PcaEligibilityRequestDTO;
import com.lbg.ib.api.sales.product.domain.eligibility.EligibilityDetails;

public interface PcaEligibiltyDAO {
    DAOResponse<HashMap<String, EligibilityDetails>> determineEligibility(
            PcaEligibilityRequestDTO customerInstructionReqDTO);

    DAOResponse<HashMap<String, EligibilityDetails>> determineEligibilityAuth(
            PcaEligibilityRequestDTO customerInstructionReqDTO);
}
