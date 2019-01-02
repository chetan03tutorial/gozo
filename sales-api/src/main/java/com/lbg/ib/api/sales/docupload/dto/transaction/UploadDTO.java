/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * All Rights Reserved.
 * Class Name: UploadDTO
 * Author(s): 8768724
 * Timestamp: 12 Dec 2015
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.dto.transaction;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UploadDTO {

    private Integer uploadSequenceNo;

    private String  evidenceTypeCode;

    private String  documentCode;

    private String  overrideEvidenceType;

    private String  overrideDocument;

    private String  fileName;

    private Integer fileSize;

    private String  contentType;

    private String  fileComments;

    private String  status;

    private Integer previewCount;

    private String  trgSysFileRefNum;

    private String  tmpSysFileRefNum;

    private String  createdBy;

    // Set CreatedDate as String for display only at UI
    private String  createdDate;

    // Set UpdateDate as String for display only at UI
    private String  updateDate;

    private String  srcSessionId;

    private boolean uploadedInCurrentSession;

    public Integer getUploadSequenceNo() {
        return uploadSequenceNo;
    }

    public void setUploadSequenceNo(Integer uploadSequenceNo) {
        this.uploadSequenceNo = uploadSequenceNo;
    }

    public String getEvidenceTypeCode() {
        return evidenceTypeCode;
    }

    public void setEvidenceTypeCode(String evidenceTypeCode) {
        this.evidenceTypeCode = evidenceTypeCode;
    }

    public String getDocumentCode() {
        return documentCode;
    }

    public void setDocumentCode(String documentCode) {
        this.documentCode = documentCode;
    }

    public String getOverrideEvidenceType() {
        return overrideEvidenceType;
    }

    public void setOverrideEvidenceType(String overrideEvidenceType) {
        this.overrideEvidenceType = overrideEvidenceType;
    }

    public String getOverrideDocument() {
        return overrideDocument;
    }

    public void setOverrideDocument(String overrideDocument) {
        this.overrideDocument = overrideDocument;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileComments() {
        return fileComments;
    }

    public void setFileComments(String fileComments) {
        this.fileComments = fileComments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPreviewCount() {
        return previewCount;
    }

    public void setPreviewCount(Integer previewCount) {
        this.previewCount = previewCount;
    }

    public String getTrgSysFileRefNum() {
        return trgSysFileRefNum;
    }

    public void setTrgSysFileRefNum(String trgSysFileRefNum) {
        this.trgSysFileRefNum = trgSysFileRefNum;
    }

    public String getTmpSysFileRefNum() {
        return tmpSysFileRefNum;
    }

    public void setTmpSysFileRefNum(String tmpSysFileRefNum) {
        this.tmpSysFileRefNum = tmpSysFileRefNum;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getSrcSessionId() {
        return srcSessionId;
    }

    public void setSrcSessionId(String srcSessionId) {
        this.srcSessionId = srcSessionId;
    }

    public boolean isUploadedInCurrentSession() {
        return uploadedInCurrentSession;
    }

    public void setUploadedInCurrentSession(boolean uploadedInCurrentSession) {
        this.uploadedInCurrentSession = uploadedInCurrentSession;
    }
}
