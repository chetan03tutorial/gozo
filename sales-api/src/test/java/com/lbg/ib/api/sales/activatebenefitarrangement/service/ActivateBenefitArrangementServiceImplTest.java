package com.lbg.ib.api.sales.activatebenefitarrangement.service;

import com.lbg.ib.api.sales.dao.mapper.CreateArrangementSetupRequestMapper;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.arrangementsetup.ArrangementSetUpResponse;
import com.lbg.ib.api.sales.product.domain.lifestyle.CreateServiceArrangement;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.invoker.SOAInvoker;
import com.lbg.ib.api.sales.shared.soap.header.SoapHeaderGenerator;
import com.lbg.ib.api.sales.shared.util.HeaderServiceUtil;
import com.lbg.ib.api.sales.shared.util.SessionServiceUtil;
import com.lbg.ib.api.sales.soapapis.servicearrangementsetupservice.CreateServiceArrangementResponse;
import com.lbg.ib.api.sales.soapapis.servicearrangementsetupservice.ServiceArrangementSetupServiceLocator;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;

import com.lloydstsb.www.Schema.Enterprise.IFWXML.ResultCondition;
import com.lloydstsb.www.Schema.Enterprise.IFWXML_Extended_Classes.ResponseHeader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerRegistry;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/*
Created by Rohit.Soni at 25/06/2018 15:17
*/

@RunWith(MockitoJUnitRunner.class)
public class ActivateBenefitArrangementServiceImplTest {

    private static final String SOAP_PORT = "SOAP_PORT";
    private static final String brand = "LTB";
    private static final String DUMMY_NAMESPACE = "http://www.lloydsbanking.com";
    private static final String REASON_CODE = "1111";
    private static final String REASON_SUCCESS_TEXT = "SUCCESS";
    private static final String REASON_FAILURE_TEXT = "FAILURE";

    @Mock
    private SessionManagementDAO sessionManager;

    @Mock
    private ConfigurationDAO configManager;

    @Mock
    private ModuleContext beanLoader;

    @Mock
    private LoggerDAO logger;

    @Mock
    CreateArrangementSetupRequestMapper createArrangementSetupRequestMapper;

    @Mock
    CreateServiceArrangement createServiceArrangement;

    @Mock
    ServiceArrangementSetupServiceLocator serviceLocator;

    @Mock
    private HandlerRegistry handlerRegistry;

    @Mock
    private SOAInvoker soaInvoker;

    @Mock
    private CreateServiceArrangementResponse createServiceArrangementResponse;

    @Mock
    private SoapHeaderGenerator soapHeaderGenerator;

    @InjectMocks
    ActivateBenefitArrangementService classUnderTest = new ActivateBenefitArrangementServiceImpl();

    @BeforeClass
    public static void setup(){
        MockitoAnnotations.initMocks(ActivateBenefitArrangementServiceImplTest.class);
    }

    @Before
    public void setupForEach(){
        when(beanLoader.getService(ServiceArrangementSetupServiceLocator.class)).thenReturn(serviceLocator);
        when(serviceLocator.getServiceArrangementSetupSOAPPortWSDDPortName()).thenReturn(SOAP_PORT);
        when(serviceLocator.getHandlerRegistry()).thenReturn(handlerRegistry);
        when(sessionManager.getUserContext()).thenReturn(SessionServiceUtil.prepareUserContext(brand));
        when(serviceLocator.getServiceName()).thenReturn(new QName("createServiceArrangement"));
        when(soapHeaderGenerator.prepareHeaderData(anyString(), anyString())).thenReturn(HeaderServiceUtil.genericHeaderData());
    }

    @Test
    public void testCreateArrangementSetupServiceSuccess(){
        ResultCondition[] resultConditions = new ResultCondition[1];
        resultConditions[0] = createResultCondition(null,REASON_CODE,REASON_SUCCESS_TEXT);
        ResponseHeader responseHeader = new ResponseHeader();
        responseHeader.setResultConditions(resultConditions);
        when(createServiceArrangementResponse.getResponseHeader()).thenReturn(responseHeader);
        when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class))).thenReturn(createServiceArrangementResponse);
        final ArrangementSetUpResponse arrangementSetupService = classUnderTest.createArrangementSetupService(createServiceArrangement);
        Assert.assertEquals(REASON_CODE, arrangementSetupService.getReasonCode());
        Assert.assertEquals(REASON_SUCCESS_TEXT, arrangementSetupService.getReasonText());
    }

    @Test
    public void testCreateArrangementSetupServiceFail(){
        ResultCondition[] resultConditions = new ResultCondition[1];
        resultConditions[0] = createResultCondition("EXC","0",REASON_FAILURE_TEXT);
        ResponseHeader responseHeader = new ResponseHeader();
        responseHeader.setResultConditions(resultConditions);
        when(createServiceArrangementResponse.getResponseHeader()).thenReturn(responseHeader);
        when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class))).thenReturn(createServiceArrangementResponse);
        final ArrangementSetUpResponse arrangementSetupService = classUnderTest.createArrangementSetupService(createServiceArrangement);
        Assert.assertEquals("0", arrangementSetupService.getReasonCode());
        Assert.assertEquals(REASON_FAILURE_TEXT, arrangementSetupService.getReasonText());
    }

    private ResultCondition createResultCondition(String severityCode, String reasonCode,String reasonText){
        ResultCondition resultCondition = new ResultCondition();
        resultCondition.setSeverityCode(severityCode);
        resultCondition.setReasonCode(reasonCode);
        resultCondition.setReasonText(reasonText);
        return resultCondition;
    }

}
