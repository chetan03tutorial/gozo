package com.lbg.ib.api.sales.docgen.mapper;
/*
Created by Rohit.Soni at 08/05/2018 09:05
*/

import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.docgen.domain.DocGenAndSaveRequest;
import com.lbg.ib.api.sales.docmanager.domain.CustomerDocumentInfo;
import com.lbg.ib.api.sales.docmanager.domain.RecordMetaDataContent;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerDocument;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RecordMetaDataContentMapper {

    @Autowired
    private SessionManagementDAO session;

    private static final String COUNTRY_OF_ISSUE = "GBR";
    private static final String DOCUMENT_ADDITIONAL_INFO = "VALID";

    public RecordMetaDataContent mapRecordCustomerDocumentRequest(String fileRefId, DocGenAndSaveRequest docGenAndSaveRequest, String partyOcisId){
        RecordMetaDataContent recordMetaDataContent = new RecordMetaDataContent();

        List<CustomerDocumentInfo> customerDocumentInfoList = new ArrayList<CustomerDocumentInfo>();
        CustomerDocumentInfo customerDocumentInfo = new CustomerDocumentInfo();

        CustomerDocument customerDocument = new CustomerDocument();
        customerDocument.setDocumentReferenceIndex(fileRefId);
        customerDocument.setDocumentType(docGenAndSaveRequest.getDocumentInfo().getDocumentCode().getDocumentCodeValue());
        customerDocument.setDocumentPurpose(docGenAndSaveRequest.getDocumentInfo().getDocumentPurpose());
        customerDocument.setDocumentCountryOfIssue(COUNTRY_OF_ISSUE);
        customerDocument.setDocumentAdditionalInfo(DOCUMENT_ADDITIONAL_INFO);

        List<CustomerDocument> customerDocuments = new ArrayList<CustomerDocument>();
        customerDocuments.add(customerDocument);

        customerDocumentInfo.setOcisId(partyOcisId);
        customerDocumentInfo.setCustomerDocuments(customerDocuments);
        customerDocumentInfoList.add(customerDocumentInfo);

        recordMetaDataContent.setCustomerDocumentInfo(customerDocumentInfoList);

        return recordMetaDataContent;
    }
}
