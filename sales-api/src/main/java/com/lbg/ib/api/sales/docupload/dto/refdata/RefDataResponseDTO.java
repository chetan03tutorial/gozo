/**
 * 
 */
package com.lbg.ib.api.sales.docupload.dto.refdata;

/**
 * @author 8735182
 *
 */
public class RefDataResponseDTO {
    private String     serviceResponse;
    private ProcessDTO docUploadRefDataForProcess;

    /**
     * @return the serviceResponse
     */
    public String getServiceResponse() {
        return serviceResponse;
    }

    /**
     * @param serviceResponse
     *            the serviceResponse to set
     */
    public void setServiceResponse(String serviceResponse) {
        this.serviceResponse = serviceResponse;
    }

    /**
     * @return the docUploadRefDataForProcess
     */
    public ProcessDTO getDocUploadRefDataForProcess() {
        return docUploadRefDataForProcess;
    }

    /**
     * @param docUploadRefDataForProcess
     *            the docUploadRefDataForProcess to set
     */
    public void setDocUploadRefDataForProcess(ProcessDTO docUploadRefDataForProcess) {
        this.docUploadRefDataForProcess = docUploadRefDataForProcess;
    }

}
