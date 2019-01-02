package com.lbg.ib.api.sales.docmanager.mapper;

import com.lbg.ib.api.sales.common.constant.Constants.RecordContant;
import com.lbg.ib.api.sales.docmanager.domain.DocumentMetaContent;
import com.lbg.ib.api.sales.docmanager.domain.DocumentMetaContentResponse;
import com.lbg.ib.api.sales.soapapis.documentservice.manager.resourceitem.DocumentContent;
import com.lbg.ib.api.sales.soapapis.documentservice.manager.resourceitem.DocumentationForm;
import com.lbg.ib.api.sales.soapapis.documentservice.manager.resourceitem.DocumentationItem;
import com.lloydsbanking.xml.DocumentLocation;
import com.lloydsbanking.xml.Note;
import com.lloydsbanking.xml.ObjectReference;
import com.lloydstsb.www.RetrieveDocumentMetaContentResponse;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DocumentMetaContentResponseMapperTest {
    
    private DocumentMetaContentResponseMapper responseMapper = new DocumentMetaContentResponseMapper();
    
    @Test
    public void testPrepareDocumentMetaContentResponse() {
        RetrieveDocumentMetaContentResponse retrieveDocumentMetaContentResponse = mock(RetrieveDocumentMetaContentResponse.class);
        DocumentContent documentContent = mock(DocumentContent.class);
        String documentType = "documentType";
        
        Note note = mock(Note.class);
		String additionalInfo = "note";
		String noteStr = additionalInfo + RecordContant.CUSTOMER_DOCUMENT_TYPE_FIELD + documentType;
        when(note.getText()).thenReturn(noteStr);
        Note[] notes = { note };
        when(documentContent.getNotes()).thenReturn(notes);
        
        DocumentationItem docItem = mock(DocumentationItem.class);
        DocumentationForm hasType = mock(DocumentationForm.class);
        DocumentLocation docLocation = mock(DocumentLocation.class);
        ObjectReference objectReference = mock(ObjectReference.class);
        String fileRefId = "fileNetRefId";
        when(objectReference.getIdentifier()).thenReturn(fileRefId);
        when(docLocation.getObjectReference()).thenReturn(objectReference);
        DocumentLocation[] docLocations = { docLocation };
        when(hasType.getLocation()).thenReturn(docLocations);
        DocumentationForm[] hasTypes = { hasType };
        when(docItem.getHasTypes()).thenReturn(hasTypes);
        
        DocumentationItem[] materializedAs = { docItem };
        when(documentContent.getMaterializedAs()).thenReturn(materializedAs);
        DocumentContent[] documentContents = { documentContent };
        when(retrieveDocumentMetaContentResponse.getDocumentContent()).thenReturn(documentContents);
        DocumentMetaContentResponse documentMetaContentResponse = responseMapper.prepareDocumentMetaContentResponse(retrieveDocumentMetaContentResponse);
        
        List<DocumentMetaContent> documents = documentMetaContentResponse.getDocuments();
        assertEquals(documentContents.length, documents.size());
        assertEquals(documentType, documents.get(0).getDocumentType());
        assertEquals(additionalInfo, documents.get(0).getAdditionalInfo());
        assertEquals(fileRefId, documents.get(0).getDocumentReferenceIndex());
    }
    
    @Test
    public void testPrepareDocumentMetaContentResponse_with_null_data() {
        RetrieveDocumentMetaContentResponse retrieveDocumentMetaContentResponse = mock(RetrieveDocumentMetaContentResponse.class);
        DocumentContent documentContent = mock(DocumentContent.class);
        when(documentContent.getContentType()).thenReturn(null);
        
        Note[] notes = {  };
        when(documentContent.getNotes()).thenReturn(notes);
        
        DocumentationItem docItem = mock(DocumentationItem.class);
        DocumentationForm hasType = mock(DocumentationForm.class);
        DocumentLocation docLocation = mock(DocumentLocation.class);
        ObjectReference objectReference = mock(ObjectReference.class);
        String fileRefId = "fileNetRefId";
        when(objectReference.getIdentifier()).thenReturn(fileRefId);
        when(docLocation.getObjectReference()).thenReturn(objectReference);
        DocumentLocation[] docLocations = { docLocation };
        when(hasType.getLocation()).thenReturn(docLocations);
        DocumentationForm[] hasTypes = { };
        when(docItem.getHasTypes()).thenReturn(hasTypes);
        
        DocumentationItem[] materializedAs = { docItem };
        when(documentContent.getMaterializedAs()).thenReturn(materializedAs);
        DocumentContent[] documentContents = { documentContent };
        when(retrieveDocumentMetaContentResponse.getDocumentContent()).thenReturn(documentContents);
        DocumentMetaContentResponse documentMetaContentResponse = responseMapper.prepareDocumentMetaContentResponse(retrieveDocumentMetaContentResponse);
        
        List<DocumentMetaContent> documents = documentMetaContentResponse.getDocuments();
        assertEquals(documentContents.length, documents.size());
        assertEquals(null, documents.get(0).getDocumentType());
        assertEquals(null, documents.get(0).getAdditionalInfo());
        assertEquals(null, documents.get(0).getDocumentReferenceIndex());
    }
    
    @Test
    public void testPrepareDocumentMetaContentResponse_with_empty_materialized() {
        RetrieveDocumentMetaContentResponse retrieveDocumentMetaContentResponse = mock(RetrieveDocumentMetaContentResponse.class);
        DocumentContent documentContent = mock(DocumentContent.class);
        when(documentContent.getContentType()).thenReturn(null);
        
        Note[] notes = {  };
        when(documentContent.getNotes()).thenReturn(notes);
        
        DocumentationItem docItem = mock(DocumentationItem.class);
        DocumentationForm hasType = mock(DocumentationForm.class);
        DocumentLocation docLocation = mock(DocumentLocation.class);
        ObjectReference objectReference = mock(ObjectReference.class);
        String fileRefId = "fileNetRefId";
        when(objectReference.getIdentifier()).thenReturn(fileRefId);
        when(docLocation.getObjectReference()).thenReturn(objectReference);
        DocumentLocation[] docLocations = { docLocation };
        when(hasType.getLocation()).thenReturn(docLocations);
        DocumentationForm[] hasTypes = { };
        when(docItem.getHasTypes()).thenReturn(hasTypes);
        
        DocumentationItem[] materializedAs = { };
        when(documentContent.getMaterializedAs()).thenReturn(materializedAs);
        DocumentContent[] documentContents = { documentContent };
        when(retrieveDocumentMetaContentResponse.getDocumentContent()).thenReturn(documentContents);
        DocumentMetaContentResponse documentMetaContentResponse = responseMapper.prepareDocumentMetaContentResponse(retrieveDocumentMetaContentResponse);
        
        List<DocumentMetaContent> documents = documentMetaContentResponse.getDocuments();
        assertEquals(documentContents.length, documents.size());
        assertEquals(null, documents.get(0).getDocumentType());
        assertEquals(null, documents.get(0).getAdditionalInfo());
        assertEquals(null, documents.get(0).getDocumentReferenceIndex());
    }
    
}
