package com.lbg.ib.api.sales.dao.kyc;

import com.lbg.ib.api.shared.domain.DAOResponse;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public interface FO75KYCReviewDAO {
    DAOResponse<String> nextReviewDate(String partyId);
}
