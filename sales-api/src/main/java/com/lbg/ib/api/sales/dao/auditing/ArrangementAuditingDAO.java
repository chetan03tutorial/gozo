package com.lbg.ib.api.sales.dao.auditing;

public interface ArrangementAuditingDAO<T> {

    void audit(T payload, String auditEvent);
}
