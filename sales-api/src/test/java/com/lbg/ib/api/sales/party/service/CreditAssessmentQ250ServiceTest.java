package com.lbg.ib.api.sales.party.service;

import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.party.TriadCFCResultDTO;
import com.lbg.ib.api.sales.dto.party.TriadRequestDTO;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.invoker.SOAInvoker;
import com.lbg.ib.api.sales.shared.soap.header.SoapHeaderGenerator;
import com.lbg.ib.api.sales.shared.util.HeaderServiceUtil;
import com.lbg.ib.api.sales.shared.util.SessionServiceUtil;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ResultCondition;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.SeverityCode;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lloydsbanking.xml.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerRegistry;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreditAssessmentQ250ServiceTest {

    @Mock
    private ModuleContext beanLoader;

    @Mock
    private SOAInvoker soaInvoker;

    @InjectMocks
    private CreditAssessmentQ250Service service;

    @Mock
    private SessionManagementDAO sessionManager;

    @Mock
    private SoapHeaderGenerator soapHeaderGenerator;

    @Mock
    private HandlerRegistry handleRegistry;

    @Mock
    private LoggerDAO logger;

    @Mock
    private Q250_Enq_Triad_Results_ServiceLocator serviceLocator;
    private static final String brand = "LTB";

    @Before
    public void setup() {

        when(serviceLocator.getQ250_Enq_Triad_ResultsWSDDPortName()).thenReturn("scheduleCommunication");
        when(serviceLocator.getHandlerRegistry()).thenReturn(handleRegistry);
        when(serviceLocator.getServiceName()).thenReturn(new QName("q250"));
        when(sessionManager.getUserContext()).thenReturn(SessionServiceUtil.prepareUserContext(brand));
        when(soapHeaderGenerator.prepareHeaderData(anyString(), anyString())).thenReturn(HeaderServiceUtil.genericHeaderData());
        when(beanLoader.getService(Q250_Enq_Triad_Results_ServiceLocator.class)).thenReturn(serviceLocator);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testQ250Success() {
        when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class))).thenReturn(externalSuccessServiceResponse());
        TriadRequestDTO triadRequestDTO = new TriadRequestDTO();
        triadRequestDTO.setOcisId("1234567");
        TriadCFCResultDTO triadCFCResultDTO = service.retrieveCreditAssessmentData(triadRequestDTO);

        assertEquals(triadCFCResultDTO.getTriadResultDTOList().size(), 1);
        assertEquals(triadCFCResultDTO.getTriadResultDTOList().get(0).getActionID(), "0106");
        assertEquals(triadCFCResultDTO.getTriadResultDTOList().get(0).getActionValue(), "98");

        assertEquals(triadCFCResultDTO.getCfcNewLimitResultDTOS().size(), 1);
        assertEquals(triadCFCResultDTO.getCfcNewLimitResultDTOS().get(0).getCfcNewLimitId(), "+0106");
        assertEquals(triadCFCResultDTO.getCfcNewLimitResultDTOS().get(0).getCfcNewlimitValue(), "+0005000");
    }

    @Test(expected = ServiceException.class)
    public void testQ250Fail() {
        when(soaInvoker.invoke(any(Class.class), anyString(), any(Class[].class), any(Object[].class))).thenReturn(externalFailServiceResponse());
        TriadRequestDTO triadRequestDTO = new TriadRequestDTO();
        triadRequestDTO.setOcisId("1234567");
        service.retrieveCreditAssessmentData(triadRequestDTO);
    }

    private Q250Resp externalSuccessServiceResponse() {
        Q250Resp externalServiceResponse = new Q250Resp();
        Q250Result q250Result = new Q250Result();
        ResultCondition resultCondition = new ResultCondition();
        resultCondition.setSeverityCode(SeverityCode.value1);
        q250Result.setResultCondition(resultCondition);
        externalServiceResponse.setQ250Result(q250Result);
        Results results = new Results();
        TriadOMDMResult triadOMDMResult = new TriadOMDMResult();
        TriadCFCResult[] triadCFCResults = new TriadCFCResult[1];
        triadCFCResults[0] = new TriadCFCResult();
        CFCActionResult[] cFCActionResults = new CFCActionResult[1];
        cFCActionResults[0] = new CFCActionResult();
        cFCActionResults[0].setCFCActionID("0106");
        cFCActionResults[0].setCFCActionVl("98");

        CFCNewLimitResult[] cFCNewLimitResults = new CFCNewLimitResult[1];
        cFCNewLimitResults[0] = new CFCNewLimitResult();
        cFCNewLimitResults[0].setCFCNewLimitID("+0106");
        cFCNewLimitResults[0].setCFCNewLimitVl("+0005000");
        triadCFCResults[0].setCFCNewLimitResult(cFCNewLimitResults);
        triadCFCResults[0].setCFCActionResult(cFCActionResults);
        triadOMDMResult.setTriadCFCResult(triadCFCResults);

        results.setTriadOMDMResult(triadOMDMResult);
        externalServiceResponse.setResults(results);
        return externalServiceResponse;
    }

    private Q250Resp externalFailServiceResponse() {
        Q250Resp externalServiceResponse = new Q250Resp();
        Q250Result q250Result = new Q250Result();
        ResultCondition resultCondition = new ResultCondition();
        resultCondition.setSeverityCode(SeverityCode.value2);
        resultCondition.setReasonCode(1);
        q250Result.setResultCondition(resultCondition);
        externalServiceResponse.setQ250Result(q250Result);
        return externalServiceResponse;
    }
}
