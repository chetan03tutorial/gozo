package com.lbg.ib.sales.stubs.stubbackend.handler.conditions;

import com.sun.net.httpserver.HttpExchange;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public interface Condition {
    boolean matches(HttpExchange httpExchange);
}
