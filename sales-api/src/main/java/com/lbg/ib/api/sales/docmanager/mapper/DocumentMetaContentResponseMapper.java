package com.lbg.ib.api.sales.docmanager.mapper;

import com.lbg.ib.api.sales.common.constant.Constants.RecordContant;
import com.lbg.ib.api.sales.docmanager.domain.DocumentMetaContent;
import com.lbg.ib.api.sales.docmanager.domain.DocumentMetaContentResponse;
import com.lbg.ib.api.sales.soapapis.documentservice.manager.resourceitem.DocumentContent;
import com.lbg.ib.api.sales.soapapis.documentservice.manager.resourceitem.DocumentationForm;
import com.lbg.ib.api.sales.soapapis.documentservice.manager.resourceitem.DocumentationItem;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lloydsbanking.xml.DocumentLocation;
import com.lloydsbanking.xml.Note;
import com.lloydsbanking.xml.ObjectReference;
import com.lloydstsb.www.RetrieveDocumentMetaContentResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper class to map the document meta content response from OCIS
 * @author 8903735
 *
 */
@Component
public class DocumentMetaContentResponseMapper {

    @Autowired
    private LoggerDAO logger;

    /**
     * Maps the document meta content response 
     * @param retrieveDocumentMetaContentResponse
     * @return {@link DocumentMetaContentResponse}
     */
    public DocumentMetaContentResponse prepareDocumentMetaContentResponse(
            RetrieveDocumentMetaContentResponse retrieveDocumentMetaContentResponse) {
        DocumentMetaContentResponse response = new DocumentMetaContentResponse();
        DocumentContent[] documentContentsFromSOA = retrieveDocumentMetaContentResponse.getDocumentContent();
        DocumentMetaContent document = null;
        List<DocumentMetaContent> documents = null;

        if (ArrayUtils.isNotEmpty(documentContentsFromSOA)) {
            documents = new ArrayList<DocumentMetaContent>();

            for (DocumentContent documentContentFromSOA : documentContentsFromSOA) {
                document = new DocumentMetaContent();

                // set the document additional info (activation date) & document
                // type
                Note[] notes = documentContentFromSOA.getNotes();
                if (ArrayUtils.isNotEmpty(notes)) {
                    if (notes[0] != null) {
                        String[] splitNotes = notes[0].getText().split(RecordContant.CUSTOMER_DOCUMENT_TYPE_FIELD);
                        if (ArrayUtils.isNotEmpty(splitNotes)) {
                            document.setAdditionalInfo(splitNotes[0]);
                            if (splitNotes.length > 1) {
                                document.setDocumentType(splitNotes[1]);
                            }
                        }
                    }
                }

                // set the document reference index
                DocumentationItem[] materializedDocumentationItems = documentContentFromSOA.getMaterializedAs();
                if (ArrayUtils.isNotEmpty(materializedDocumentationItems)) {
                    document.setDocumentReferenceIndex(getDocumentReferenceIndex(materializedDocumentationItems[0]));
                }
                documents.add(document);
            }
        }
        response.setDocuments(documents);
        return response;
    }

    private String getDocumentReferenceIndex(DocumentationItem documentationItem) {
        DocumentationForm[] hasTypes = documentationItem.getHasTypes();
        String docReferenceIndex = null;
        if (ArrayUtils.isNotEmpty(hasTypes)) {
            for (DocumentationForm hasType : hasTypes) {
                DocumentLocation[] locations = hasType.getLocation();
                if (ArrayUtils.isNotEmpty(locations)) {
                    ObjectReference objectReference = locations[0].getObjectReference();
                    docReferenceIndex = objectReference != null ? objectReference.getIdentifier() : null;
                    break;
                }
            }
        }
        return docReferenceIndex;
    }

}
