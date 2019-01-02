/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 * Class Name: ResponseHeaderDTO
 *
 * Author(s):Parameshwaran Kangamuthu(1146728)
 *
 * Date: 18 Mar 2016
 * 
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.dto.document;

/**
 * @author 1146728 This documentation DTO is used for SOA services to retrieve
 *         service response
 * 
 */
public class ResponseHeaderDTO {

    private String             returnCode;

    private String             errorCode;

    private String             cmdStatus;

    private ResultConditionDTO resultCondition;

    /**
     * @return the resultCondition
     */
    public ResultConditionDTO getResultCondition() {
        return resultCondition;
    }

    /**
     * @param resultCondition
     *            the resultCondition to set
     */
    public void setResultCondition(ResultConditionDTO resultCondition) {
        this.resultCondition = resultCondition;
    }

    /**
     * @return the returnCode
     */
    public String getReturnCode() {
        return returnCode;
    }

    /**
     * @param returnCode
     *            the returnCode to set
     */
    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    /**
     * @return the errorCode
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode
     *            the errorCode to set
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @return the cmdStatus
     */
    public String getCmdStatus() {
        return cmdStatus;
    }

    /**
     * @param cmdStatus
     *            the cmdStatus to set
     */
    public void setCmdStatus(String cmdStatus) {
        this.cmdStatus = cmdStatus;
    }

}
