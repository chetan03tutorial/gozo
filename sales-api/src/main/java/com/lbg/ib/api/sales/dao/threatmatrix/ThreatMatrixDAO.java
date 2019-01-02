package com.lbg.ib.api.sales.dao.threatmatrix;

import com.lbg.ib.api.shared.domain.DAOResponse;

import java.util.Map;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public interface ThreatMatrixDAO {
    DAOResponse<Map<String, String>> retrieveThreadMatrixResults(String applicationId, String applicationType,
            String ocisId);
}