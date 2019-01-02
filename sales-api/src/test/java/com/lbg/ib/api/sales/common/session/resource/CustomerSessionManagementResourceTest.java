package com.lbg.ib.api.sales.common.session.resource;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.common.domain.MessageResponse;
import com.lbg.ib.api.sales.common.session.dto.CustomerInfo;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lbg.ib.api.sales.product.domain.activate.CustomerDocument;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;

@RunWith(MockitoJUnitRunner.class)
public class CustomerSessionManagementResourceTest {

    @InjectMocks
    CustomerSessionManagementResource customerSessionManagementResource = null;

    @Mock
    private RequestBodyResolver       resolver;

    @Mock
    HttpServletRequest                req;

    @Mock
    private LoggerDAO                 logger                            = mock(LoggerDAO.class);

    @Mock
    private SessionManagementDAO      session;

    @Test
    public void setCustomerInfoTestToUpdateSessionCustomerDetails() throws Exception {
        CustomerInfo reqCustomerInfo = new CustomerInfo();
        List<CustomerDocument> customerDocuments = new ArrayList<CustomerDocument>();
        CustomerDocument customerDocument = new CustomerDocument();
        customerDocument.setDocumentAdditionalInfo("documentAdditionalInfo");
        customerDocument.setDocumentCountryOfIssue("documentCountryOfIssue");
        customerDocument.setDocumentPurpose("documentPurpose");
        customerDocument.setDocumentReferenceIndex("documentReferenceIndex");
        customerDocument.setDocumentReferenceText("documentReferenceText");
        customerDocument.setDocumentType("documentType");
        customerDocuments.add(customerDocument);
        reqCustomerInfo.setCustomerDocuments(customerDocuments);

        CustomerInfo sessionCustomerInfo = new CustomerInfo();
        sessionCustomerInfo.setAccountNumber("accountNumber");
        sessionCustomerInfo.setArrangementId("arrangementId");
        sessionCustomerInfo.setSortCode("sortCode");
        sessionCustomerInfo.setSurName("surName");
        sessionCustomerInfo.setTitle("title");

        BranchContext context = new BranchContext();
        context.setOriginatingSortCode("1234");
        context.setColleagueId("2345");

        String requestString = "{\"customerDocuments\":\"779122\"}";
        when(resolver.resolve(requestString, CustomerInfo.class)).thenReturn(reqCustomerInfo);
        when(session.getCustomerDetails()).thenReturn(sessionCustomerInfo);
        when(session.getBranchContext()).thenReturn(context);
        Response response = customerSessionManagementResource.setCustomerInfo(req, requestString);

        assertNotNull(response);
        assertTrue(response.getStatus() == 200);
        assertTrue(((MessageResponse) response.getEntity()).getSuccess());
    }

    @Test
    public void setCustomerInfoTestForSessionHasNoCustomerDetails() throws Exception {
        CustomerInfo reqCustomerInfo = new CustomerInfo();
        List<CustomerDocument> customerDocuments = new ArrayList<CustomerDocument>();
        CustomerDocument customerDocument = new CustomerDocument();
        customerDocument.setDocumentAdditionalInfo("documentAdditionalInfo");
        customerDocument.setDocumentCountryOfIssue("documentCountryOfIssue");
        customerDocument.setDocumentPurpose("documentPurpose");
        customerDocument.setDocumentReferenceIndex("documentReferenceIndex");
        customerDocument.setDocumentReferenceText("documentReferenceText");
        customerDocument.setDocumentType("documentType");
        customerDocuments.add(customerDocument);
        reqCustomerInfo.setCustomerDocuments(customerDocuments);
        String requestString = "{\"customerDocuments\":\"779122\"}";
        when(resolver.resolve(requestString, CustomerInfo.class)).thenReturn(reqCustomerInfo);
        when(session.getCustomerDetails()).thenReturn(null);
        Response response = customerSessionManagementResource.setCustomerInfo(req, requestString);
        assertNotNull(response);
        assertTrue(response.getStatus() == 200);
        assertTrue(((MessageResponse) response.getEntity()).getSuccess());
    }

    @Test
    public void getCustomerInfoSuccessTest() throws Exception {
        CustomerInfo sessionCustomerInfo = new CustomerInfo();
        List<CustomerDocument> customerDocuments = new ArrayList<CustomerDocument>();
        CustomerDocument customerDocument = new CustomerDocument();
        customerDocument.setDocumentAdditionalInfo("documentAdditionalInfo");
        customerDocument.setDocumentCountryOfIssue("documentCountryOfIssue");
        customerDocument.setDocumentPurpose("documentPurpose");
        customerDocument.setDocumentReferenceIndex("documentReferenceIndex");
        customerDocument.setDocumentReferenceText("documentReferenceText");
        customerDocument.setDocumentType("documentType");
        customerDocuments.add(customerDocument);
        sessionCustomerInfo.setCustomerDocuments(customerDocuments);
        sessionCustomerInfo.setAccountNumber("accountNumber");
        sessionCustomerInfo.setArrangementId("arrangementId");
        sessionCustomerInfo.setSortCode("sortCode");
        sessionCustomerInfo.setSurName("surName");
        sessionCustomerInfo.setTitle("title");
        BranchContext context = new BranchContext();
        context.setOriginatingSortCode("1234");
        context.setColleagueId("2345");
        when(session.getCustomerDetails()).thenReturn(sessionCustomerInfo);
        when(session.getBranchContext()).thenReturn(context);
        Response response = customerSessionManagementResource.getCustomerInfo();
        assertNotNull(response);
        assertTrue(response.getStatus() == 200);
        assertTrue(((CustomerInfo) response.getEntity()).getArrangementId().equals("arrangementId"));
    }

    @Test
    public void getCustomerInfoFailureTest() throws Exception {

        when(session.getCustomerDetails()).thenReturn(null);
        when(session.getBranchContext()).thenReturn(null);
        Response response = customerSessionManagementResource.getCustomerInfo();
        assertNotNull(response);
        assertTrue(response.getStatus() == 200);
        // assertFalse(((MessageResponse) response.getEntity()).getSuccess());
    }

}
