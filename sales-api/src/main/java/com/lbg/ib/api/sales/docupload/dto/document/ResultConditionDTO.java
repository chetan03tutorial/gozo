/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 * Class Name: ResultConditionDTO
 *
 * Author(s):Parameshwaran Kangamuthu(1146728)
 *
 * Date: 18 Mar 2016
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.dto.document;

import java.util.List;

/**
 * @author 1146728 This documentation DTO is used for SOA services to retrieve
 *         service response
 * 
 */
public class ResultConditionDTO {
    private String                  reasonCode;

    private String                  reasonText;

    private String                  severityCode;

    private List<ExtraConditionDTO> extraCondition;

    /**
     * @return the extraCondition
     */
    public List<ExtraConditionDTO> getExtraCondition() {
        return extraCondition;
    }

    /**
     * @param extraCondition
     *            the extraCondition to set
     */
    public void setExtraCondition(List<ExtraConditionDTO> extraCondition) {
        this.extraCondition = extraCondition;
    }

    /**
     * @return the reasonCode
     */
    public String getReasonCode() {
        return reasonCode;
    }

    /**
     * @param reasonCode
     *            the reasonCode to set
     */
    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    /**
     * @return the reasonText
     */
    public String getReasonText() {
        return reasonText;
    }

    /**
     * @param reasonText
     *            the reasonText to set
     */
    public void setReasonText(String reasonText) {
        this.reasonText = reasonText;
    }

    /**
     * @return the severityCode
     */
    public String getSeverityCode() {
        return severityCode;
    }

    /**
     * @param severityCode
     *            the severityCode to set
     */
    public void setSeverityCode(String severityCode) {
        this.severityCode = severityCode;
    }

}
