package com.lbg.ib.sales.stubs.stubbackend.handler.conditions.url;

import com.lbg.ib.sales.stubs.stubbackend.handler.conditions.Condition;
import com.sun.net.httpserver.HttpExchange;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class UrlPathCondition implements Condition {
    private String pathEndsWith;

    public UrlPathCondition(String pathEndsWith) {
        this.pathEndsWith = pathEndsWith;
    }

    public boolean matches(HttpExchange httpExchange) {
        return httpExchange.getRequestURI().toString().endsWith(pathEndsWith);
    }

    public static Condition urlPathEndsWith(String key) {
        return new UrlPathCondition(key);
    }
}
