package com.lbg.ib.api.sales.common.filter;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
@Aspect
@Component
public class ColleagueRoleValidator {

    private SessionManagementDAO session;

    private LoggerDAO            loggerDAO;

    @Autowired
    public ColleagueRoleValidator(SessionManagementDAO session, LoggerDAO loggerDAO) {
        this.session = session;
        this.loggerDAO = loggerDAO;
    }

    @Before("execution(* *.*(..)) && @annotation(roleValidator)")
    public void validateColleagueRole(JoinPoint joinpoint, RoleValidator roleValidator) throws ServiceException {
        BranchContext context = session.getBranchContext();
        if (null != context) {
            List<String> roles = context.getColleagueRoles();
            if (null == roles) {
                ServiceException serviceException = new ServiceException(new ResponseError(
                        ResponseErrorConstants.USER_NOT_AUTHORIZED, "User not authroized for this operation"));
                loggerDAO.logException(this.getClass(), serviceException);
                throw serviceException;
            }
        }

    }

}
