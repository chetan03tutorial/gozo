/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 * Class Name: RequestHeaderDTO
 *
 * Author(s):Parameshwaran Kangamuthu(1146728)
 *
 * Date: 18 Mar 2016
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.dto.document;

/**
 * @author 1146728 This documentation DTO is used for SOA services to retrieve
 *         service response
 * 
 */
public class RetrieveRequestHeaderDTO {

    private String id;
    private String functionCode;
    private String externalApplicationId;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the functionCode
     */
    public String getFunctionCode() {
        return functionCode;
    }

    /**
     * @param functionCode
     *            the functionCode to set
     */
    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    /**
     * @return the externalApplicationId
     */
    public String getExternalApplicationId() {
        return externalApplicationId;
    }

    /**
     * @param externalApplicationId
     *            the externalApplicationId to set
     */
    public void setExternalApplicationId(String externalApplicationId) {
        this.externalApplicationId = externalApplicationId;
    }

}
