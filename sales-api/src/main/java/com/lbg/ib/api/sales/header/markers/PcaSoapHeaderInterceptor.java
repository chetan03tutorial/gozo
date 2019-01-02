package com.lbg.ib.api.sales.header.markers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.header.builder.AbstractBaseHeader;
import com.lbg.ib.api.sales.header.builder.ServiceRequestHeader;
import com.lbg.ib.api.sales.header.common.fields.ClientContext;
import com.lbg.ib.api.sales.shared.context.RequestContext;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sso.domain.user.UserContext;

@Component
@Aspect
public class PcaSoapHeaderInterceptor {

	@Autowired
	private ModuleContext beanLoader;

	@Autowired
	private SessionManagementDAO sessionManager;

	static final String SOAP_HEADER_HANDLERS = "soapHeaderHandlers";

	@Before("@annotation(com.lbg.ib.api.sales.header.markers.PcaSoapHeaders)")
	public void handleSoapHeader(JoinPoint joinPoint) {
		PcaSoapHeaders soaHeaders = null;
		final Signature signature = joinPoint.getStaticPart().getSignature();
		if (signature instanceof MethodSignature) {

			MethodSignature ms = (MethodSignature) signature;
			Method method = ms.getMethod();
			Annotation[] annotations = method.getAnnotations();
			for (Annotation annotation : annotations) {
				if (PcaSoapHeaders.class.isAssignableFrom(annotation.getClass())) {
					soaHeaders = (PcaSoapHeaders) annotation;
				}
			}
		}
		UserContext user = sessionManager.getUserContext();
		ClientContext clientContext = toClientContext(user);
		RequestContext.setInRequestContext("ClientContext", clientContext);

		if (soaHeaders == null) {
			return;
		}
		Set<AbstractBaseHeader> headerSet = new LinkedHashSet<AbstractBaseHeader>();
		Class<? extends AbstractBaseHeader>[] clazzes = soaHeaders.headers();
		for (Class<? extends AbstractBaseHeader> clazz : clazzes) {
			AbstractBaseHeader builder = beanLoader.getService(clazz);
			if (builder.getClass().isAssignableFrom(ServiceRequestHeader.class)) {
				RequestContext.setInRequestContext("actionName", soaHeaders.serviceAction());
				RequestContext.setInRequestContext("serviceName", soaHeaders.serviceName());
			}
			builder.registerHandler(headerSet);
		}
		RequestContext.setInRequestContext(SOAP_HEADER_HANDLERS, headerSet);

	}

	public ClientContext toClientContext(UserContext user) {
		ClientContext clientContext = new ClientContext();
		clientContext.setUserId(user.getUserId());
		clientContext.setIpAddress(user.getIpAddress());
		clientContext.setSessionId(user.getSessionId());
		clientContext.setPartyId(user.getPartyId());
		clientContext.setOsisId(user.getOcisId());
		clientContext.setChannelId(user.getChannelId());
		clientContext.setChansecmode(user.getChansecmode());
		clientContext.setUserAgent(user.getUserAgent());
		clientContext.setLanguage(user.getLanguage());
		clientContext.setInboxIdClient(user.getInboxIdClient());
		clientContext.setHost(user.getHost());
		return clientContext;
	}

}
