package com.lbg.ib.api.sales.content.service;

import com.lbg.ib.api.shared.exception.ServiceException;

import javax.ws.rs.core.Response;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public interface ContentService {
    Response content(String key) throws ServiceException;
    
    Response contentManager(String path) throws ServiceException;
    
    <T> T genericContent(String key, Class<T> t) throws ServiceException;
}
