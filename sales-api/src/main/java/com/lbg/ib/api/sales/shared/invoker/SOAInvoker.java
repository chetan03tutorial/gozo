package com.lbg.ib.api.sales.shared.invoker;

public interface SOAInvoker {

    public <T> Object invoke(final Class<T> portType, String methodName, Class[] argType, Object[] args);

}
