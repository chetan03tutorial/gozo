/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.dao.product.offercrosssell;

import java.util.TreeMap;

import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.dto.product.eligibility.EligibilityRequestDTO;

public interface CrossSellEligibilityDAO {

    DAOResponse<TreeMap<String, String>> determineCrossSellEligibilityForCustomer(EligibilityRequestDTO request)
            throws Exception;

}
