package com.lbg.ib.api.sales.mca.services;

import com.lbg.ib.api.sales.mca.domain.ColleagueContext;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sso.domain.mca.BranchContext;

public interface BranchContextService {

    public ColleagueContext setBranchContextToSession(BranchContext branchContext) throws ServiceException;

    public boolean checkSnRRole(BranchContext branchContext);
}
