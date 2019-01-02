/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * All Rights Reserved.
 * Class Name: CaseResponseDTO
 * Author(s): 8768724
 * Date: 12 Jan 2016
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.dto.transaction;

import java.util.List;

/**
 * @author 8768724
 *
 */
public class CaseResponseDTO {
    
    private String        serviceResponse;
    
    private List<CaseDTO> caseDetails;

    private String code;

    private String errorStatus;

    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(String errorStatus) {
        this.errorStatus = errorStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getServiceResponse() {
        return serviceResponse;
    }
    
    public void setServiceResponse(String serviceResponse) {
        this.serviceResponse = serviceResponse;
    }
    
    /**
     * @return the caseDetails
     */
    public List<CaseDTO> getCaseDetails() {
        return caseDetails;
    }
    
    /**
     * @param caseDetails
     *            the caseDetails to set
     */
    public void setCaseDetails(List<CaseDTO> caseDetails) {
        this.caseDetails = caseDetails;
    }
    
}
