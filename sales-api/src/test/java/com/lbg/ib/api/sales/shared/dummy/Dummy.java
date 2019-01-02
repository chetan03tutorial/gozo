package com.lbg.ib.api.sales.shared.dummy;

import java.lang.reflect.InvocationTargetException;

public class Dummy {

    public String getResult(String welcomeMessage) {
        return privateMethod(welcomeMessage) + welcomeMessage;
    }

    private String privateMethod(String message) {
        return "Private Method";
    }

    public String invocationTargetException(String welcomeMessage) throws InvocationTargetException {
        throw new InvocationTargetException(new Exception(welcomeMessage));
    }

    /*
     * public String illegalArgumentException(String welcomeMessage) throws
     * IllegalArgumentException { throw new
     * IllegalArgumentException(welcomeMessage); }
     */

}
