package com.lbg.ib.api.sales.shared.soap.header;

import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.sales.dao.MCAHeaderUtility;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.shared.util.HeaderServiceUtil;
import com.lbg.ib.api.sales.shared.util.SessionServiceUtil;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SoapHeaderGeneratorTest {

    @InjectMocks
    private SoapHeaderGenerator  soapGenerator;

    @Mock
    private SessionManagementDAO session;

    @Mock
    private GBOHeaderUtility     headerUtility;

    @Mock
    private MCAHeaderUtility     mcaheaderUtility;

    List<SOAPHeader> soapHeaders = new LinkedList<SOAPHeader>();
    

    @Test
    public void testGenerateHeaderForDigitalJourney() {
        when(session.getBranchContext()).thenReturn(null);
        when(headerUtility.prepareSoapHeader(anyString(), anyString()))
                .thenReturn(HeaderServiceUtil.prepareSoapHeaders());
        assertNotNull(soapGenerator.prepareHeaderData("serviceAction", "serviceName"));
    }

    @Test
    public void testGenerateHeaderForMCAJourney() {
        when(session.getBranchContext()).thenReturn(SessionServiceUtil.prepareBranchContext());
        when(mcaheaderUtility.prepareSoapHeader(anyString(), anyString()))
                .thenReturn(HeaderServiceUtil.prepareSoapHeaders());
        assertNotNull(soapGenerator.prepareHeaderData("serviceAction", "serviceName"));
    }
    
    @Test
    public void testContactPointHeader() {
        SOAPHeader soap = HeaderServiceUtil.contactPointHeader();
        soapHeaders.add(soap);
        when(session.getBranchContext()).thenReturn(null);
        when(headerUtility.prepareSoapHeader("serviceAction", "serviceName", false))
        .thenReturn(soapHeaders);
        assertNotNull(soapGenerator.prepareHeaderData("serviceAction", "serviceName"));
    }
    
    @Test
    public void testServicePointHeader() {
        SOAPHeader soap = HeaderServiceUtil.servicePointHeader();
        soapHeaders.add(soap);
        when(session.getBranchContext()).thenReturn(null);
        when(headerUtility.prepareSoapHeader("serviceAction", "serviceName", false))
        .thenReturn(soapHeaders);
        assertNotNull(soapGenerator.prepareHeaderData("serviceAction", "serviceName"));
    }
    
    @Test
    public void testSecurityTypetHeader() {
        SOAPHeader soap = HeaderServiceUtil.securityTypeHeader();
        soapHeaders.add(soap);
        when(session.getBranchContext()).thenReturn(null);
        when(headerUtility.prepareSoapHeader("serviceAction", "serviceName", false))
        .thenReturn(soapHeaders);
        assertNotNull(soapGenerator.prepareHeaderData("serviceAction", "serviceName"));
    }
    
    @Test
    public void testBapiHeader() {
        SOAPHeader soap = HeaderServiceUtil.bapiHeader();
        soapHeaders.add(soap);
        when(session.getBranchContext()).thenReturn(null);
        when(headerUtility.prepareSoapHeader("serviceAction", "serviceName", false))
        .thenReturn(soapHeaders);
        assertNotNull(soapGenerator.prepareHeaderData("serviceAction", "serviceName"));
    }
}
