package com.lbg.ib.api.sales.dao.mapper;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.sales.dto.bankwizard.BankAccountDetailsRequestDTO;
import com.lbg.ib.api.sales.soapapis.bw.arrangementnegotiation.VerifyProductArrangementDetailsRequest;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.BAPIHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.BapiInformation;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.HostInformation;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ContactPoint;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ServiceRequest;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.security.SecurityHeaderType;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.security.UsernameToken;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * @author Atul Choudhary
 * @version 1.0
 * @since 29thJuly2016
 ***********************************************************************/
@RunWith(MockitoJUnitRunner.class)
public class BankWizardValidateAccountRequestMapperTest {

    private static final String            SALSA_SERVICE_ACTION = "verifyProductArrangementDetails";

    private static final String            SALSA_SERVICE_NAME   = "http://www.lloydstsb.com/Schema/Enterprise/LCSM_ArrangementNegotiation/ArrangementSetupService";

    @InjectMocks
    BankWizardValidateAccountRequestMapper bankWizardValidateAccountRequestMapper;

    @Mock
    private GBOHeaderUtility               gboHeaderUtility;

    List<SOAPHeader>                       soapHeaders          = new ArrayList<SOAPHeader>();

    @Before
    public void setUp() {

        SOAPHeader contactSoapHeader = new SOAPHeader();
        contactSoapHeader.setName("ContactPoint");
        contactSoapHeader.setValue(new ContactPoint());
        SOAPHeader securitySoapHeader = setSecurityHeader();
        SOAPHeader serviceRequestHeader = new SOAPHeader();
        serviceRequestHeader.setName("ServiceRequest");
        serviceRequestHeader.setValue(new ServiceRequest());
        SOAPHeader bapiRequestHeader = setBapiHeader();
        soapHeaders.add(bapiRequestHeader);
        soapHeaders.add(serviceRequestHeader);
        soapHeaders.add(securitySoapHeader);
        soapHeaders.add(contactSoapHeader);
        soapHeaders.add(new SOAPHeader());
    }

    @Test
    public void testMapRequest() {
        final BankAccountDetailsRequestDTO bankAccountDetailsDTO = new BankAccountDetailsRequestDTO("accountNo",
                "sortCode");
        VerifyProductArrangementDetailsRequest expectedRequest = bankWizardValidateAccountRequestMapper
                .mapRequestAttribute(bankAccountDetailsDTO);

        assertTrue(expectedRequest != null);
    }

    @Test
    public void testPrepareHeader() {
        bankWizardValidateAccountRequestMapper.prepareHeader();
        when(gboHeaderUtility.prepareSoapHeader(SALSA_SERVICE_ACTION, SALSA_SERVICE_NAME)).thenReturn(soapHeaders);

        bankWizardValidateAccountRequestMapper.prepareHeader();

        assertTrue(bankWizardValidateAccountRequestMapper.getBapiInformationHeader() != null);
        assertTrue(bankWizardValidateAccountRequestMapper.getSecurityHeader() != null);
        assertTrue(bankWizardValidateAccountRequestMapper.getServiceRequestHeader() != null);
        assertTrue(bankWizardValidateAccountRequestMapper.getContactPointHeader() != null);
    }

    private SOAPHeader setBapiHeader() {
        SOAPHeader bapiRequestHeader = new SOAPHeader();
        bapiRequestHeader.setName("bapiInformation");
        BapiInformation bapiInformation = new BapiInformation();
        BAPIHeader bapiHeader = new BAPIHeader();
        HostInformation stpartyObo = new HostInformation();
        bapiHeader.setStpartyObo(stpartyObo);
        bapiInformation.setBAPIHeader(bapiHeader);
        bapiRequestHeader.setValue(bapiInformation);
        return bapiRequestHeader;
    }

    private SOAPHeader setSecurityHeader() {
        SOAPHeader securitySoapHeader = new SOAPHeader();
        SecurityHeaderType securityHeaderType = new SecurityHeaderType();
        UsernameToken usernameToken = new UsernameToken();
        securityHeaderType.setUsernameToken(usernameToken);
        securitySoapHeader.setValue(securityHeaderType);
        securitySoapHeader.setName("Security");
        return securitySoapHeader;
    }

}
