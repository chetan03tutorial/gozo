package com.lbg.ib.api.sales.party.service;

import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.party.C542RequestDTO;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.invoker.SOAInvoker;
import com.lbg.ib.api.sales.shared.soap.header.SoapHeaderGenerator;
import com.lbg.ib.api.sales.shared.util.HeaderServiceUtil;
import com.lbg.ib.api.sales.shared.util.SessionServiceUtil;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ResultCondition;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.SeverityCode;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lloydstsb.www.C542Resp;
import com.lloydstsb.www.C542Result;
import com.lloydstsb.www.C542_ChaExtPrdIDTx_ServiceLocator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerRegistry;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OcisC542ServiceTest {

    @Mock
    private ModuleContext beanLoader;

    @Mock
    private SOAInvoker soaInvoker;

    @InjectMocks
    private OcisC542Service service;

    @Mock
    private SessionManagementDAO sessionManager;

    @Mock
    private SoapHeaderGenerator soapHeaderGenerator;

    @Mock
    private HandlerRegistry handleRegistry;

    @Mock
    private LoggerDAO logger;

    @Mock
    private C542_ChaExtPrdIDTx_ServiceLocator serviceLocator;
    private static final String brand = "LTB";

    @Before
    public void setup() {

        when(serviceLocator.getC542_ChaExtPrdIDTxWSDDPortName()).thenReturn("scheduleCommunication");
        when(serviceLocator.getHandlerRegistry()).thenReturn(handleRegistry);
        when(serviceLocator.getServiceName()).thenReturn(new QName("c542"));
        when(sessionManager.getUserContext()).thenReturn(SessionServiceUtil.prepareUserContext(brand));
        when(soapHeaderGenerator.prepareHeaderData(anyString(), anyString())).thenReturn(HeaderServiceUtil.genericHeaderData());
        when(beanLoader.getService(C542_ChaExtPrdIDTx_ServiceLocator.class)).thenReturn(serviceLocator);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testC542Success() {
        when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class))).thenReturn(externalSuccessServiceResponse());
        C542RequestDTO c542RequestDTO = new C542RequestDTO();
        c542RequestDTO.setAccountNumber("33333");
        c542RequestDTO.setSortCode("11111");
        c542RequestDTO.setCbsProductId("1234");
        c542RequestDTO.setOcisId("1234567");
        service.updateOcisRecords(c542RequestDTO);
    }

    @Test(expected = ServiceException.class)
    public void testC542Fail() {
        when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class))).thenReturn(externalFailServiceResponse());
        C542RequestDTO c542RequestDTO = new C542RequestDTO();
        c542RequestDTO.setAccountNumber("33333");
        c542RequestDTO.setSortCode("11111");
        c542RequestDTO.setCbsProductId("1234");
        c542RequestDTO.setOcisId("1234567");
        service.updateOcisRecords(c542RequestDTO);
    }

    private C542Resp externalSuccessServiceResponse() {
        C542Resp externalServiceResponse = new C542Resp();
        C542Result q250Result = new C542Result();
        ResultCondition resultCondition = new ResultCondition();
        resultCondition.setSeverityCode(SeverityCode.value1);
        q250Result.setResultCondition(resultCondition);
        externalServiceResponse.setC542Result(q250Result);
        return externalServiceResponse;
    }

    private C542Resp externalFailServiceResponse() {
        C542Resp externalServiceResponse = new C542Resp();
        C542Result q250Result = new C542Result();
        ResultCondition resultCondition = new ResultCondition();
        resultCondition.setSeverityCode(SeverityCode.value2);
        resultCondition.setReasonCode(1);
        q250Result.setResultCondition(resultCondition);
        externalServiceResponse.setC542Result(q250Result);
        return externalServiceResponse;
    }
}
