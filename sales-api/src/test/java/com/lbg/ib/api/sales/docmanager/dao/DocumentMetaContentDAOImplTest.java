package com.lbg.ib.api.sales.docmanager.dao;

import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.docmanager.domain.CustomerDocumentInfo;
import com.lbg.ib.api.sales.docmanager.domain.CustomerDocumentInfoResponse;
import com.lbg.ib.api.sales.docmanager.domain.DocumentMetaContentResponse;
import com.lbg.ib.api.sales.docmanager.mapper.DocumentMetaContentRequestMapper;
import com.lbg.ib.api.sales.docmanager.mapper.DocumentMetaContentResponseMapper;
import com.lbg.ib.api.sales.docmanager.util.DocumentManagerHeaderUtility;
import com.lbg.ib.api.sales.soapapis.documentservice.manager.ErrorInfo;
import com.lloydstsb.www.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.rpc.handler.HandlerRegistry;
import java.rmi.RemoteException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DocumentMetaContentDAOImplTest {

    @Mock
    private DocumentMetaContentRequestMapper requestMapper;

    @Mock
    private DocumentMetaContentResponseMapper responseMapper;

    @Mock
    private DocumentationManagerServiceLocator documentationManagerServiceLocator;

    @Mock
    private DocumentationManager               documentationManager;

    @Mock
    private DocumentationManager               retrieveDocumentationManager;

    @Mock
    private DocumentManagerHeaderUtility headerUtility;

    @Mock
    private DAOExceptionHandler daoExceptionHandler;

    @InjectMocks
    private DocumentMetaContentDAOImpl documentMetaContentDao = new DocumentMetaContentDAOImpl();

    private String                             serviceAction          = "retrieveDocumentMetaContent";

    @Mock
    protected LoggerDAO logger;

    @Test
    public void testRetrieveDocumentMetaContent() {
        String partyId = "1234";
        RetrieveDocumentMetaContentRequest retrieveDocMetaContentRequest = mock(
                RetrieveDocumentMetaContentRequest.class);
        when(requestMapper.prepareRetrieveDocumentMetaContentRequest(partyId))
                .thenReturn(retrieveDocMetaContentRequest);
        HandlerRegistry handlerRegistry = mock(HandlerRegistry.class);
        when(headerUtility.setupAndGetDataHandler(documentationManagerServiceLocator, serviceAction))
                .thenReturn(handlerRegistry);
        RetrieveDocumentMetaContentResponse retrieveDocMetaContentResponse = mock(
                RetrieveDocumentMetaContentResponse.class);
        try {
            when(retrieveDocumentationManager.retrieveDocumentMetaContent(retrieveDocMetaContentRequest))
                    .thenReturn(retrieveDocMetaContentResponse);
        } catch (Exception e) {
            fail("Unexpected Exception Occurred.");
        }
        DocumentMetaContentResponse expectedResponse = mock(DocumentMetaContentResponse.class);
        when(responseMapper.prepareDocumentMetaContentResponse(retrieveDocMetaContentResponse))
                .thenReturn(expectedResponse);
        DAOResponse<DocumentMetaContentResponse> documentMetaContent = documentMetaContentDao
                .retrieveDocumentMetaContent(partyId);
        DocumentMetaContentResponse actualResponse = documentMetaContent.getResult();
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testRetrieveDocumentMetaContent_throw_remote_exception() throws RemoteException, ErrorInfo {
        String partyId = "1234";
        RetrieveDocumentMetaContentRequest retrieveDocMetaContentRequest = mock(
                RetrieveDocumentMetaContentRequest.class);
        when(requestMapper.prepareRetrieveDocumentMetaContentRequest(partyId))
                .thenReturn(retrieveDocMetaContentRequest);
        HandlerRegistry handlerRegistry = mock(HandlerRegistry.class);
        when(headerUtility.setupAndGetDataHandler(documentationManagerServiceLocator, serviceAction))
                .thenReturn(handlerRegistry);
        RetrieveDocumentMetaContentResponse retrieveDocMetaContentResponse = mock(
                RetrieveDocumentMetaContentResponse.class);
        when(retrieveDocumentationManager.retrieveDocumentMetaContent(retrieveDocMetaContentRequest))
                .thenThrow(RemoteException.class);
        DAOError daoError = mock(DAOError.class);
        when(daoExceptionHandler.handleException(any(Exception.class), any(Class.class), anyString(),
                any(String.class))).thenReturn(daoError);
        DocumentMetaContentResponse expectedResponse = mock(DocumentMetaContentResponse.class);
        when(responseMapper.prepareDocumentMetaContentResponse(retrieveDocMetaContentResponse))
                .thenReturn(expectedResponse);
        DAOResponse<DocumentMetaContentResponse> documentMetaContent = documentMetaContentDao
                .retrieveDocumentMetaContent(partyId);
        DocumentMetaContentResponse actualResponse = documentMetaContent.getResult();
        assertNull(actualResponse);
        assertNotNull(documentMetaContent.getError());
    }

    @Test
    public void testRecordDocumentMetaContentFromSOA() {
        CustomerDocumentInfo customerDocumentInfo = new CustomerDocumentInfo();
        when(requestMapper.prepareRecordDocumentMetaContentRequest(any(CustomerDocumentInfo.class)))
                .thenReturn(new RecordDocumentMetaContentRequest());
        HandlerRegistry handlerRegistry = mock(HandlerRegistry.class);
        when(headerUtility.setupAndGetDataHandler(documentationManagerServiceLocator, serviceAction))
                .thenReturn(handlerRegistry);

        DAOResponse<CustomerDocumentInfoResponse> recordDocumentMetaContent = documentMetaContentDao
                .recordDocumentMetaContentFromSOA(customerDocumentInfo);

        assertNotNull(recordDocumentMetaContent);
    }

    @Test
    public void testRecordDocumentMetaContentFromSOAWithException() throws RemoteException, ErrorInfo {
        CustomerDocumentInfo customerDocumentInfo = new CustomerDocumentInfo();
        when(requestMapper.prepareRecordDocumentMetaContentRequest(any(CustomerDocumentInfo.class)))
                .thenReturn(new RecordDocumentMetaContentRequest());
        HandlerRegistry handlerRegistry = mock(HandlerRegistry.class);
        when(headerUtility.setupAndGetDataHandler(documentationManagerServiceLocator, serviceAction))
                .thenReturn(handlerRegistry);
        when(documentationManager.recordDocumentMetaContent(any(RecordDocumentMetaContentRequest.class)))
                .thenThrow(new RemoteException("Dummy Exception"));
        DAOError daoError = mock(DAOError.class);
        when(daoExceptionHandler.handleException(any(Exception.class), any(Class.class), anyString(),
                any(String.class))).thenReturn(daoError);
        DAOResponse<CustomerDocumentInfoResponse> recordDocumentMetaContent = documentMetaContentDao
                .recordDocumentMetaContentFromSOA(customerDocumentInfo);

        assertNull(recordDocumentMetaContent.getResult());
        assertNotNull(recordDocumentMetaContent.getError());
    }

}
