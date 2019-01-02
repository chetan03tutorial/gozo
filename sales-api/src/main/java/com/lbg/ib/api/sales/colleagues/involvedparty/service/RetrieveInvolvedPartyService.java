/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.colleagues.involvedparty.service;

import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lbg.ib.api.sales.mca.domain.ColleagueContext;

public interface RetrieveInvolvedPartyService {
    public ColleagueContext retrieveRolesForInvolvedParty() throws ServiceException;

    public boolean retrieveSnRRolesForInvolvedParty(BranchContext branchContext) throws ServiceException;
}
