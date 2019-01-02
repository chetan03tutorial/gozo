package com.lbg.ib.api.sales.docgen.mapper;

import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.docgen.domain.DocGenAndSaveRequest;
import com.lbg.ib.api.sales.docupload.domain.Case;
import com.lbg.ib.api.sales.docupload.domain.Document;
import com.lbg.ib.api.sales.docupload.domain.Evidence;
import com.lbg.ib.api.sales.docupload.domain.Party;
import com.lbg.ib.api.sales.docupload.dto.transaction.CasePsfDTO;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.sales.user.domain.SelectedAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/*
Created by Rohit.Soni at 08/05/2018 10:39
*/
@Component
public class CreateCaseMapper {

    @Autowired
    private SessionManagementDAO sessionManager;

    private static final String FILE_NAME = "WelcomeMessage.pdf";
    private static final String PSF_NAME = "sort_code_account_number";

    public Case createCaseCreateRequest(DocGenAndSaveRequest docGenAndSaveRequest, PartyDetails party){
        Case caseDetails = new Case();
        caseDetails.setProcessCode(docGenAndSaveRequest.getEvidenceInfo().getProcessCode());
        caseDetails.setCaseReferenceNo(docGenAndSaveRequest.getCaseReferenceNo());
        caseDetails.setPartyDetails(createParties(docGenAndSaveRequest,party));
        caseDetails.setCasePSF(createCasePsfDTO());
        return caseDetails;
    }

    private List<CasePsfDTO> createCasePsfDTO(){
        SelectedAccount accountToConvert = sessionManager.getAccountToConvertInContext();
        List<CasePsfDTO> casePsfDTOS = new ArrayList<CasePsfDTO>();
        CasePsfDTO casePsfDTO = new CasePsfDTO();
        casePsfDTO.setCasePSFName(PSF_NAME);
        casePsfDTO.setCasePSFValue(accountToConvert.getSortCode()+accountToConvert.getAccountNumber());
        casePsfDTOS.add(casePsfDTO);
        return casePsfDTOS;
    }

    private List<Party> createParties(DocGenAndSaveRequest docGenAndSaveRequest, PartyDetails partyDetails){
        List<Party> parties = new ArrayList<Party>();
        Party party = new Party();
        party.setEvidences(createEvidences(docGenAndSaveRequest));
        party.setTitle(partyDetails.getTitle());
        party.setForeName(partyDetails.getFirstName());
        party.setSurName(partyDetails.getSurname());
        party.setTitle(partyDetails.getTitle());
        parties.add(party);
        return parties;
    }

    private List<Evidence> createEvidences(DocGenAndSaveRequest docGenAndSaveRequest){
        List<Evidence> evidences = new ArrayList<Evidence>();
        Evidence evidence = new Evidence();
        List<Document> documents = new ArrayList<Document>();
        documents.add(createDocument(docGenAndSaveRequest));
        evidence.setDocuments(documents);
        evidence.setEvidenceTypeCode(docGenAndSaveRequest.getEvidenceInfo().getEvidenceTypeCode());
        evidences.add(evidence);
        return evidences;
    }

    private Document createDocument(DocGenAndSaveRequest docGenAndSaveRequest){
        Document document = new Document();
        document.setDocumentCode(docGenAndSaveRequest.getDocumentInfo().getDocumentCode().getDocumentCodeValue());
        document.setEvidenceTypeCode(docGenAndSaveRequest.getEvidenceInfo().getEvidenceTypeCode());
        document.setFileName(FILE_NAME);
        return document;
    }

}
