package com.lbg.ib.api.sales.overdraft.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.overdraft.domain.CbsRequest;
import com.lbg.ib.api.sales.overdraft.domain.ExternalServiceErrorResponse;
import com.lbg.ib.api.sales.overdraft.domain.ExternalServiceError;
import com.lbg.ib.api.sales.shared.exception.ServiceError;
import com.lbg.ib.api.sales.shared.service.SoaAbstractService;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.Condition;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ResultCondition;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;

@Component
public abstract class AbstractCbsService extends SoaAbstractService {

	@Autowired
	protected LoggerDAO logger;

	@Autowired
	protected SessionManagementDAO sessionManager;

	public <T> T getMessageMapper(Class<T> serviceClass) {

		T instance = null;
		try {
			instance = serviceClass.newInstance();
		} catch (InstantiationException e) {
			logger.logException(this.getClass(), e);
			logger.traceLog(this.getClass(), "unable to initialize the message mapper");
		} catch (IllegalAccessException e) {
			logger.logException(this.getClass(), e);
			logger.traceLog(this.getClass(), "unable to initialize the message mapper");
		}
		return instance;
	}

	public void setOriginatingSortCode(CbsRequest serviceRequest) {
		String originatingSortCode = null;
		if (sessionManager.getBranchContext() != null) {
			logger.traceLog(this.getClass(), "Invoking the service in branch context");
			originatingSortCode = sessionManager.getBranchContext().getOriginatingSortCode();
			serviceRequest.setOriginatingSortcode(originatingSortCode);
			logger.traceLog(this.getClass(), "Invoking the service in branch context with originating sortcode as "
					+ serviceRequest.getOriginatingSortcode());
		}
	}
}
