package com.lbg.ib.api.sales.docupload.delegate;

import com.lbg.ib.api.sales.docupload.dto.transaction.CaseResponseDTO;
import com.lbg.ib.api.shared.domain.DAOResponse;

public interface SearchCaseDelegateService {
    public DAOResponse<CaseResponseDTO> searchCaseDeleteService(String caseReferenceNo, String processingCode);
}
