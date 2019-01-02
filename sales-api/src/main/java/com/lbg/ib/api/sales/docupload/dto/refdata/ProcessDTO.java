/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * 
 * All Rights Reserved.
 * 
 * Class Name: Process   
 *   
 * Author(s): 8768724
 *  
 * Date: 21 Oct 2015
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.dto.refdata;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.stereotype.Component;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Component
public class ProcessDTO {

    private String                processCd;

    private String                businessProcessName;

    private String                brand;

    private Integer               uploadSizeLimit;

    private Integer               docUploadFileLimit;

    private String                tempStoragePath;

    private String                allowUndefinedDocument;

    private Integer               defaultExpiryPeriod;

    private String                targetApplication;

    private List<EvidenceTypeDTO> evidenceType;

    private List<FileFormatsDTO>  fileFormat;

    private List<ProcessPsfDTO>   processSpecificField;

    private String                businessProcessTypeName;

    /**
     * @return the processCd
     */
    public String getProcessCd() {
        return processCd;
    }

    /**
     * @param processCd
     *            the processCd to set
     */
    public void setProcessCd(String processCd) {
        this.processCd = processCd;
    }

    /**
     * @return the businessProcessName
     */
    public String getBusinessProcessName() {
        return businessProcessName;
    }

    /**
     * @param businessProcessName
     *            the businessProcessName to set
     */
    public void setBusinessProcessName(String businessProcessName) {
        this.businessProcessName = businessProcessName;
    }

    /**
     * @return the brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * @param brand
     *            the brand to set
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * @return the uploadSizeLimit
     */
    public Integer getUploadSizeLimit() {
        return uploadSizeLimit;
    }

    /**
     * @param uploadSizeLimit
     *            the uploadSizeLimit to set
     */
    public void setUploadSizeLimit(Integer uploadSizeLimit) {
        this.uploadSizeLimit = uploadSizeLimit;
    }

    /**
     * @return the docUploadFileLimit
     */
    public Integer getDocUploadFileLimit() {
        return docUploadFileLimit;
    }

    /**
     * @param docUploadFileLimit
     *            the docUploadFileLimit to set
     */
    public void setDocUploadFileLimit(Integer docUploadFileLimit) {
        this.docUploadFileLimit = docUploadFileLimit;
    }

    /**
     * @return the tempStoragePath
     */
    public String getTempStoragePath() {
        return tempStoragePath;
    }

    /**
     * @param tempStoragePath
     *            the tempStoragePath to set
     */
    public void setTempStoragePath(String tempStoragePath) {
        this.tempStoragePath = tempStoragePath;
    }

    /**
     * @return the allowUndefinedDocument
     */
    public String getAllowUndefinedDocument() {
        return allowUndefinedDocument;
    }

    /**
     * @param allowUndefinedDocument
     *            the allowUndefinedDocument to set
     */
    public void setAllowUndefinedDocument(String allowUndefinedDocument) {
        this.allowUndefinedDocument = allowUndefinedDocument;
    }

    /**
     * @return the defaultExpiryPeriod
     */
    public Integer getDefaultExpiryPeriod() {
        return defaultExpiryPeriod;
    }

    /**
     * @param defaultExpiryPeriod
     *            the defaultExpiryPeriod to set
     */
    public void setDefaultExpiryPeriod(Integer defaultExpiryPeriod) {
        this.defaultExpiryPeriod = defaultExpiryPeriod;
    }

    /**
     * @return the targetApplication
     */
    public String getTargetApplication() {
        return targetApplication;
    }

    /**
     * @param targetApplication
     *            the targetApplication to set
     */
    public void setTargetApplication(String targetApplication) {
        this.targetApplication = targetApplication;
    }

    /**
     * @return the evidenceType
     */
    public List<EvidenceTypeDTO> getEvidenceType() {
        return evidenceType;
    }

    /**
     * @param evidenceType
     *            the evidenceType to set
     */
    public void setEvidenceType(List<EvidenceTypeDTO> evidenceType) {
        this.evidenceType = evidenceType;
    }

    /**
     * @return the fileFormat
     */
    public List<FileFormatsDTO> getFileFormat() {
        return fileFormat;
    }

    /**
     * @param fileFormat
     *            the fileFormat to set
     */
    public void setFileFormat(List<FileFormatsDTO> fileFormat) {
        this.fileFormat = fileFormat;
    }

    /**
     * @return the processSpecificField
     */
    public List<ProcessPsfDTO> getProcessSpecificField() {
        return processSpecificField;
    }

    /**
     * @param processSpecificField
     *            the processSpecificField to set
     */
    public void setProcessSpecificField(List<ProcessPsfDTO> processSpecificField) {
        this.processSpecificField = processSpecificField;
    }

    /**
     * @return the businessProcessTypeName
     */
    public String getBusinessProcessTypeName() {
        return businessProcessTypeName;
    }

    /**
     * @param businessProcessTypeName
     *            the businessProcessTypeName to set
     */
    public void setBusinessProcessTypeName(String businessProcessTypeName) {
        this.businessProcessTypeName = businessProcessTypeName;
    }

}
