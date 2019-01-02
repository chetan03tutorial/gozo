package com.lbg.ib.api.sales.mca.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.application.Base;
import com.lbg.ib.api.sales.colleagues.involvedparty.service.RetrieveInvolvedPartyService;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.mca.domain.ColleagueContext;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sso.domain.mca.BranchContext;

/**
 * @author ssama1
 */
@Component
public class BranchContextServiceImpl extends Base implements BranchContextService {

    private SessionManagementDAO         session;
    private RetrieveInvolvedPartyService retrieveInvolvedPartyService;

    private static final Class           CLASS_NAME = BranchContextServiceImpl.class;

    @Autowired
    public BranchContextServiceImpl(SessionManagementDAO session,
            RetrieveInvolvedPartyService retrieveInvolvedPartyService) {
        this.session = session;
        this.retrieveInvolvedPartyService = retrieveInvolvedPartyService;
    }

    @TraceLog
    public boolean checkSnRRole(BranchContext branchContext){
        if(!retrieveInvolvedPartyService.retrieveSnRRolesForInvolvedParty(branchContext)){
            ServiceException serviceException = new ServiceException(new ResponseError(
                    ResponseErrorConstants.USER_NOT_AUTHORIZED, "User not authroized for this operation"));
            throw serviceException;
        }
        return true;
    }
    /*
     * (non-Javadoc)
     *
     * @see com.lbg.ib.api.pao.mca.services.BranchContextService#
     * setBranchContextToSession(com.lbg.ib.api.pao.mca.domain.BranchContext)
     */
    public ColleagueContext setBranchContextToSession(BranchContext branchContext) throws ServiceException {
        // fetch colleague roles
        session.setBranchContext(branchContext);
        logger.traceLog(this.getClass(), "Invocation of retrieveRolesForInvolvedParty.");
        ColleagueContext colleagueContext = retrieveInvolvedPartyService.retrieveRolesForInvolvedParty();
        if (colleagueContext != null) {
            logger.traceLog(this.getClass(), "colleagueContext is  " + colleagueContext.getColleagueId());
        }
        return colleagueContext;
    }

}
