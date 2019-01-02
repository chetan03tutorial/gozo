
package com.lbg.ib.sales.stubs.stubbackend.handler.conditions.logical;

import com.lbg.ib.sales.stubs.stubbackend.handler.conditions.Condition;
import com.sun.net.httpserver.HttpExchange;


/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class AlwaysMatchCondition implements Condition {
    public boolean matches(HttpExchange httpExchange) {
        return true;
    }

    public static AlwaysMatchCondition alwaysMatch() {
        return new AlwaysMatchCondition();
    }
}
