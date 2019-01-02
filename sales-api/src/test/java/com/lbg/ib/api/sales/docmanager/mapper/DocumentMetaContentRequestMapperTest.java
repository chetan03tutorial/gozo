package com.lbg.ib.api.sales.docmanager.mapper;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.docmanager.domain.CustomerDocumentInfo;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerDocument;
import com.lloydstsb.www.RecordDocumentMetaContentRequest;
import com.lloydstsb.www.RetrieveDocumentMetaContentRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class DocumentMetaContentRequestMapperTest {
	@Mock
    protected LoggerDAO logger;
	
	@InjectMocks
    private DocumentMetaContentRequestMapper requestMapper = new DocumentMetaContentRequestMapper();
    
    @Test
    public void testPrepareRetrieveDocumentMetaContentRequest() {
        String partyId = "12345";
        RetrieveDocumentMetaContentRequest retrieveDocumentMetaContentRequest = requestMapper
                .prepareRetrieveDocumentMetaContentRequest(partyId);
        assertEquals(partyId,
                retrieveDocumentMetaContentRequest.getInvolvedParty().getObjectReference().getIdentifier());
        assertEquals("19", retrieveDocumentMetaContentRequest.getRequestHeader().getDatasourceName());
    }
    
    
    @Test
    public void testPrepareRecordDocumentMetaContentRequest(){
        CustomerDocumentInfo customerDocumentInfo = new CustomerDocumentInfo();
        customerDocumentInfo.setOcisId("736368835");
        
        List<CustomerDocument> customerDocuments = new ArrayList<CustomerDocument>();
        CustomerDocument customerDocument = new CustomerDocument();
        customerDocument.setDocumentPurpose("identity verification");
        customerDocument.setDocumentType("151");
        customerDocument.setDocumentReferenceText("123456");
        customerDocument.setDocumentReferenceIndex("{B3EA35xxxxxx-D871-4C8E-B92B-37428B468010}");
        customerDocument.setDocumentCountryOfIssue("GBR");
        customerDocument.setDocumentAdditionalInfo("Hello World Again !!!");
        customerDocuments.add(customerDocument);
        customerDocumentInfo.setCustomerDocuments(customerDocuments);
        RecordDocumentMetaContentRequest request = requestMapper.prepareRecordDocumentMetaContentRequest(customerDocumentInfo);
        assert(request.getDocumentContent().length==1);
    }
    
    
    @Test
    public void testPrepareRecordDocumentMetaContentRequestWithNoDocuments(){
        CustomerDocumentInfo customerDocumentInfo = new CustomerDocumentInfo();
        customerDocumentInfo.setOcisId("736368835");
        
        List<CustomerDocument> customerDocuments = null;
        customerDocumentInfo.setCustomerDocuments(customerDocuments);
        RecordDocumentMetaContentRequest request = requestMapper.prepareRecordDocumentMetaContentRequest(customerDocumentInfo);
        assert(request.getDocumentContent()!=null);
    }
    
}
