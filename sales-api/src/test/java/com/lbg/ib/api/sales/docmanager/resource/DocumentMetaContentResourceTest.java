package com.lbg.ib.api.sales.docmanager.resource;

import com.lbg.ib.api.sales.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.docmanager.domain.*;
import com.lbg.ib.api.sales.docmanager.service.DocumentMetaContentService;

import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DocumentMetaContentResourceTest {
    
    @InjectMocks
    private DocumentMetaContentResource docMetaContentResource = new DocumentMetaContentResource();
    
    @Mock
    private DocumentMetaContentService documentMetaContentService;

    @Mock
    private GalaxyErrorCodeResolver errorResolver;
    
    @Mock
    protected LoggerDAO logger;
    
    @Mock
    private RequestBodyResolver resolver;
    
    @Test
    public void testRetrieveDocumentMetaContent() {
        String partyId = "1234";
        DocumentMetaContentResponse docMetaContentResponse = mock(DocumentMetaContentResponse.class);
        when(documentMetaContentService.retrieveDocumentMetaContent(partyId)).thenReturn(docMetaContentResponse);
        Response response = docMetaContentResource.retrieveDocumentMetaContent(partyId);
        
        assertEquals(docMetaContentResponse, response.getEntity());
    }
    
    @Test
	public void testRecordDocumentMetaContent() {
		CustomerDocumentInfoResponse customerDocumentInfoResponse = mock(CustomerDocumentInfoResponse.class);
		CustomerDocumentInfo customerDocumentInfo = mock(CustomerDocumentInfo.class);
		List<CustomerDocumentInfo> customerDocumentInfoList = new ArrayList<CustomerDocumentInfo>();
		when(customerDocumentInfo.getOcisId()).thenReturn("2344");
		customerDocumentInfoList.add(customerDocumentInfo);
		RecordMetaDataContent recordMetaDataContent = mock(RecordMetaDataContent.class);
		when(recordMetaDataContent.getCustomerDocumentInfo()).thenReturn(customerDocumentInfoList);
		when(documentMetaContentService.recordDocumentMetaContent(any(CustomerDocumentInfo.class)))
				.thenReturn(customerDocumentInfoResponse);
		
		
		Response response = docMetaContentResource.recordDocumentMetaContent(recordMetaDataContent);
		Object responseEntity = response.getEntity();
		RecordMetaDataContentResponse recordResponse = (RecordMetaDataContentResponse) responseEntity;
		assertTrue(recordResponse.getResponse().contains(customerDocumentInfoResponse));

	}

	public void testRecordDocumentMetaContent_null_partyId() {
		CustomerDocumentInfo customerDocumentInfo = mock(CustomerDocumentInfo.class);
		when(customerDocumentInfo.getOcisId()).thenReturn(null);
		
		RecordMetaDataContent recordMetaDataContent = mock(RecordMetaDataContent.class);
		List<CustomerDocumentInfo> customerDocumentInfoList = new ArrayList<CustomerDocumentInfo>();
		customerDocumentInfoList.add(customerDocumentInfo);
		when(recordMetaDataContent.getCustomerDocumentInfo()).thenReturn(customerDocumentInfoList);
		Response response = docMetaContentResource.recordDocumentMetaContent(recordMetaDataContent);
		Object responseEntity = response.getEntity();
		RecordMetaDataContentResponse recordResponse = (RecordMetaDataContentResponse) responseEntity;
		List<CustomerDocumentInfoResponse> response2 = recordResponse.getResponse();
		CustomerDocumentInfoResponse customerDocumentInfoResponse = response2.get(0);
		assertFalse(customerDocumentInfoResponse.getIsRecorded());
		assertEquals("Party Identifier cannot be null or empty.", customerDocumentInfoResponse.getMessage());
	}
    
    @Test
    public void testRetrievePartyDetails_null_reponse() {
        String partyId = "1234";
        DocumentMetaContentResponse response = null;
        when(documentMetaContentService.retrieveDocumentMetaContent(partyId)).thenReturn(response);
        Response actualResponse = docMetaContentResource.retrieveDocumentMetaContent(partyId);
        
        assertNull(actualResponse.getEntity());
    }
    
    @Test(expected=InvalidFormatException.class)
    public void testRetrievePartyDetails_invalid_party_id() {
        String partyId = "";
        DocumentMetaContentResponse response = null;
        Response actualResponse = docMetaContentResource.retrieveDocumentMetaContent(partyId);
    }
    
}
