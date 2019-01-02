package com.lbg.ib.api.sales.docmanager.mapper;

import com.lbg.ib.api.sales.common.constant.Constants;
import com.lbg.ib.api.sales.docmanager.domain.CustomerDocumentInfo;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerDocument;
import com.lbg.ib.api.sales.soapapis.documentservice.manager.RequestHeader;
import com.lbg.ib.api.sales.soapapis.documentservice.manager.involvedparty.Individual;
import com.lbg.ib.api.sales.soapapis.documentservice.manager.involvedparty.InvolvedPartyRole;
import com.lbg.ib.api.sales.soapapis.documentservice.manager.involvedparty.InvolvedPartyType;
import com.lbg.ib.api.sales.soapapis.documentservice.manager.resourceitem.*;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lloydsbanking.xml.*;
import com.lloydstsb.www.RecordDocumentMetaContentRequest;
import com.lloydstsb.www.RetrieveDocumentMetaContentRequest;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * This class is used to map the requests to SOA request from REST domain
 * @author 8903735
 *
 */
@Component
public class DocumentMetaContentRequestMapper {

    private static final String DATA_SOURCE_NAME    = "19";

    public static final String  PLACE_OF_BIRTH      = "Place of Birth";
    public static final String  CONTENT_NAME        = "100";
    public static final String  DOC_CONTENT_VERSION = "1";
    public static final String  INVOLVED_PARTY_DESC = "Source";
    public static final String  INVOLVED_PARTY_NAME = "001";

    @Autowired
    private LoggerDAO logger;

    /**
     * Maps the REST domain request to the SOA request object
     * @param ocisId
     * @return {@link RetrieveDocumentMetaContentRequest}
     */
    public RetrieveDocumentMetaContentRequest prepareRetrieveDocumentMetaContentRequest(String ocisId) {
        RetrieveDocumentMetaContentRequest retrieveDocumentMetaContentRequest = new RetrieveDocumentMetaContentRequest();
        RequestHeader requestHeader = new RequestHeader();
        requestHeader.setDatasourceName(DATA_SOURCE_NAME);
        retrieveDocumentMetaContentRequest.setRequestHeader(requestHeader);

        Individual involvedParty = new Individual();
        ObjectReference objectReference = new ObjectReference();
        objectReference.setIdentifier(ocisId);
        involvedParty.setObjectReference(objectReference);
        retrieveDocumentMetaContentRequest.setInvolvedParty(involvedParty);

        return retrieveDocumentMetaContentRequest;
    }


    public RecordDocumentMetaContentRequest prepareRecordDocumentMetaContentRequest(CustomerDocumentInfo customerDocumentInfo) {
        logger.traceLog(this.getClass(), "Inside prepareRecordDocumentMetaContentRequest.");
        RecordDocumentMetaContentRequest recordDocumentMetaContentRequest = new RecordDocumentMetaContentRequest();

        RequestHeader requestHeader = new RequestHeader();
        requestHeader.setDatasourceName(DATA_SOURCE_NAME);
        recordDocumentMetaContentRequest.setRequestHeader(requestHeader);

        List<DocumentContent> listDocumentContent = new ArrayList<DocumentContent>();
        listDocumentContent.addAll(prepareDocumentContentList(customerDocumentInfo.getOcisId(),
                customerDocumentInfo.getCustomerDocuments()));

        recordDocumentMetaContentRequest.setDocumentContent(listDocumentContent.toArray(new DocumentContent[0]));
        return recordDocumentMetaContentRequest;

    }

    private List<DocumentContent> prepareDocumentContentList(String ocisId, List<CustomerDocument> customerDocuments) {
        List<DocumentContent> documentContents = new ArrayList<DocumentContent>();
        if (CollectionUtils.isNotEmpty(customerDocuments)) {
            logger.traceLog(this.getClass(), "Size of the customer document list is : " + customerDocuments.size());
            for (CustomerDocument customerDocument : customerDocuments) {

                DocumentContent documentContent = new DocumentContent();

                ContentType defaultContentType = new ContentType();
                defaultContentType.setDescription(PLACE_OF_BIRTH);
                defaultContentType.setName(CONTENT_NAME);
                documentContent.setContentType(defaultContentType);
                documentContent.setVersion(DOC_CONTENT_VERSION);
                documentContent.setActedOnBy(getDefaultInvolvedParty(ocisId));
                DocumentContent documentContentDesc = new DocumentContent();

                InformationContent[] informationContentList = new InformationContent[1];
                informationContentList[0] = documentContentDesc;
                documentContent.setIncludesContent(informationContentList);

                documentContent.setDocument(getDocumentationItem());
                setOcisIndexing(documentContent, customerDocument);
                documentContents.add(documentContent);
            }
        }
        return documentContents;
    }

    private void setOcisIndexing(DocumentContent documentContentNew, CustomerDocument customerDocument) {
        logger.traceLog(this.getClass(), "Set the indexing details in the request.");
        if (customerDocument != null) {
            DocumentLocation documentLocation = new DocumentLocation();
            LocationType locationType = new LocationType();
            LocationObjectReference objectReference = new LocationObjectReference();
            locationType.setName("001");

            objectReference.setIdentifier(customerDocument.getDocumentReferenceIndex());
            Calendar cal = Calendar.getInstance();

            cal.setTime(new Date());
            documentLocation.setStartTime(cal);
            documentLocation.setLocationType(new LocationType[] { locationType });
            documentLocation.setObjectReference(objectReference);

            DocumentationForm documentationForm = new DocumentationForm();
            documentationForm.setLocation(new DocumentLocation[] { documentLocation });
            documentationForm.setStorageType("002");
            documentationForm.setFormat("001");
            documentationForm.setStorageMedium("005");
            DocumentationItem documentationItem = new DocumentationItem();
            documentationItem.setHasTypes(new DocumentationForm[] { documentationForm, documentationForm });

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());

            documentationItem.setReceivedTime(calendar);

            Note note = new Note();
            note.setText(customerDocument.getDocumentAdditionalInfo()
                    + Constants.RecordContant.CUSTOMER_DOCUMENT_TYPE_FIELD + customerDocument.getDocumentType());

            documentContentNew.setNotes(new Note[] { note });

            ContentType contentType = new ContentType();
            contentType.setName("001");
            documentContentNew.setContentType(contentType);

            documentContentNew.setMaterializedAs(new DocumentationItem[] { documentationItem });
        }
    }

    private InvolvedPartyRole getDefaultInvolvedParty(String customerId) {
        Individual individual = new Individual();
        ObjectReference objectReference = new ObjectReference();
        objectReference.setIdentifier(customerId);

        individual.setObjectReference(objectReference);
        InvolvedPartyType involvedPartyType = new InvolvedPartyType();
        InvolvedPartyRole involvedPartyRole = new InvolvedPartyRole();

        involvedPartyType.setDescription(INVOLVED_PARTY_DESC);
        involvedPartyType.setName(INVOLVED_PARTY_NAME);
        individual.setHasPartyType(involvedPartyType);
        Individual individualUpper = new Individual();

        individualUpper.setAssociatedInvolvedParties(new Individual[]{individual});
        involvedPartyRole.setIsPlayedByParty(new Individual[]{individualUpper});

        return involvedPartyRole;
    }

    private DocumentationItem getDocumentationItem() {
        DocumentRegistration registration = new DocumentRegistration();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        registration.setStartTime(cal);

        DocumentationItem documentationItem = new DocumentationItem();
        documentationItem.setDocuments(registration);
        return documentationItem;
    }


}
