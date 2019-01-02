package com.lbg.ib.api.sales.dao.content;

import com.lbg.ib.api.shared.domain.DAOResponse;

import javax.ws.rs.core.Response;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public interface ContentDAO {
    DAOResponse<Response> content(String channel, String key);
    DAOResponse<Response> contentManager(String channel, String key);
    
    <T> T getContent(String channel, String key, Class<T> t);

}
