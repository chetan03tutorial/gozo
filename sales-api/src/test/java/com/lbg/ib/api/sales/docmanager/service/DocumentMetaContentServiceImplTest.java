package com.lbg.ib.api.sales.docmanager.service;

import com.lbg.ib.api.sales.common.constant.Constants;
import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.docmanager.dao.DocumentMetaContentDAO;
import com.lbg.ib.api.sales.docmanager.domain.CustomerDocumentInfo;
import com.lbg.ib.api.sales.docmanager.domain.CustomerDocumentInfoResponse;
import com.lbg.ib.api.sales.docmanager.domain.DocumentMetaContentResponse;
import com.lbg.ib.api.sales.docmanager.domain.RecordMetaDataContent;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerDocument;
import com.lbg.ib.api.sales.soapapis.documentservice.manager.ErrorInfo;
import com.lloydstsb.www.RetrieveDocumentMetaContentResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DocumentMetaContentServiceImplTest {
    
    @InjectMocks
    private DocumentMetaContentServiceImpl documentMetaContentService = new DocumentMetaContentServiceImpl();
    
    @Mock
    private DocumentMetaContentDAO documentMetaContentDAO;
    
    @Mock
    private GalaxyErrorCodeResolver resolver;
    
    @Mock
    private DAOExceptionHandler daoExceptionHandler;
    
    @Mock
    protected LoggerDAO logger;
    
    @Test
    public void testRetrieveDocumentMetaContent() {
        String partyId = "1234";
        DAOResponse<DocumentMetaContentResponse> docMetaContentDaoResponse = mock(DAOResponse.class);
        DocumentMetaContentResponse docResponse = mock(DocumentMetaContentResponse.class);
        when(docMetaContentDaoResponse.getResult()).thenReturn(docResponse);
        when(documentMetaContentDAO.retrieveDocumentMetaContent(partyId)).thenReturn(docMetaContentDaoResponse);
        DocumentMetaContentResponse documentMetaContent = documentMetaContentService
                .retrieveDocumentMetaContent(partyId);
        assertEquals(docResponse, documentMetaContent);
    }
    
    @Test
    public void testRecordDocumentMetaContentWithHeaderNull() throws RemoteException, ErrorInfo {
        
        when(documentMetaContentDAO.retrieveDocumentMetaContentFromSOA(any(String.class)))
                .thenReturn(new RetrieveDocumentMetaContentResponse());
        DAOResponse<CustomerDocumentInfoResponse> customerDocumentResponse = mock(DAOResponse.class);
        when(documentMetaContentDAO.recordDocumentMetaContentFromSOA(any(CustomerDocumentInfo.class))).thenReturn(customerDocumentResponse);
        CustomerDocumentInfoResponse recordResponse = new CustomerDocumentInfoResponse("ocisId", Constants.RecordContant.SUCCESS_MSG, true);
        when(customerDocumentResponse.getResult()).thenReturn(recordResponse);
        CustomerDocumentInfo info = new CustomerDocumentInfo();
        List<CustomerDocument> customerDocuments = new ArrayList<CustomerDocument>();
        CustomerDocument c = new CustomerDocument();
        c.setDocumentAdditionalInfo("VALID");
        customerDocuments.add(c);
        c = new CustomerDocument();
        c.setDocumentAdditionalInfo("INVALID");
        customerDocuments.add(c);
        info.setCustomerDocuments(customerDocuments);
        CustomerDocumentInfoResponse response = documentMetaContentService
                .recordDocumentMetaContent(info);
        assertEquals(response.getMessage(), Constants.RecordContant.SUCCESS_MSG);
    }
    
    
    @Test(expected = ServiceException.class)
    public void testRecordDocumentMetaContentRecordServiceException() throws RemoteException, ErrorInfo {
        
        when(documentMetaContentDAO.retrieveDocumentMetaContentFromSOA(any(String.class)))
                .thenReturn(new RetrieveDocumentMetaContentResponse());
        DAOResponse<CustomerDocumentInfoResponse> recordMetaContentDaoResponse = mock(DAOResponse.class);
        DAOError daoError = mock(DAOError.class);
        when(recordMetaContentDaoResponse.getError()).thenReturn(daoError);
        when(documentMetaContentDAO.recordDocumentMetaContentFromSOA(any(CustomerDocumentInfo.class))).thenReturn(recordMetaContentDaoResponse);
        CustomerDocumentInfoResponse response = documentMetaContentService
                .recordDocumentMetaContent(new CustomerDocumentInfo());
    }
    
    @Test(expected=ServiceException.class)
    public void testRecordDocumentMetaContentRecordException() throws RemoteException, ErrorInfo {
        RecordMetaDataContent recordMetaDataContent = new RecordMetaDataContent();
        when(documentMetaContentDAO.retrieveDocumentMetaContentFromSOA(any(String.class)))
                .thenReturn(new RetrieveDocumentMetaContentResponse());
        DAOResponse<CustomerDocumentInfoResponse> daoResponse = mock(DAOResponse.class);
        DAOError doaError = mock(DAOError.class);
        when(daoResponse.getError()).thenReturn(doaError);
        when(documentMetaContentDAO.recordDocumentMetaContentFromSOA(any(CustomerDocumentInfo.class))).thenReturn(daoResponse);
        CustomerDocumentInfoResponse response = documentMetaContentService
                .recordDocumentMetaContent(new CustomerDocumentInfo());
        
        
    }
    
    @Test(expected = ServiceException.class)
    public void testRetrieveDocumentMetaContent_with_error() {
        String partyId = "1234";
        DAOResponse<DocumentMetaContentResponse> docMetaContentDaoResponse = mock(DAOResponse.class);
        DAOError daoError = mock(DAOError.class);
        when(docMetaContentDaoResponse.getError()).thenReturn(daoError);
        when(documentMetaContentDAO.retrieveDocumentMetaContent(partyId)).thenReturn(docMetaContentDaoResponse);
        DocumentMetaContentResponse documentMetaContent = documentMetaContentService
                .retrieveDocumentMetaContent(partyId);
    }
    
}
