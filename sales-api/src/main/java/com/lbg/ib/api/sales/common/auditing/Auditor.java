package com.lbg.ib.api.sales.common.auditing;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public interface Auditor<T> {
    void audit(T auditee);
}
