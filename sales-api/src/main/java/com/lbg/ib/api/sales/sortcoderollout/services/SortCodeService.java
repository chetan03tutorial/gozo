package com.lbg.ib.api.sales.sortcoderollout.services;

import com.lbg.ib.api.shared.exception.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public interface SortCodeService {
    Response getSortCodes(HttpServletRequest requestPath) throws ServiceException;
}
