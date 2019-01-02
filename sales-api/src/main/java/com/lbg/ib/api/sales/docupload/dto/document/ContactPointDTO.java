/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 * Class Name: ContactPointDTO
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
public class ContactPointDTO {

    private String initialOriginatorId;

    private String initialOriginatorType;

    private String contactPointId;

    private String contactPointType;

    private String applicationId;

    private String operatorType;

    /**
     * @return the initialOriginatorId
     */
    public String getInitialOriginatorId() {
        return initialOriginatorId;
    }

    /**
     * @param initialOriginatorId
     *            the initialOriginatorId to set
     */
    public void setInitialOriginatorId(String initialOriginatorId) {
        this.initialOriginatorId = initialOriginatorId;
    }

    /**
     * @return the initialOriginatorType
     */
    public String getInitialOriginatorType() {
        return initialOriginatorType;
    }

    /**
     * @param initialOriginatorType
     *            the initialOriginatorType to set
     */
    public void setInitialOriginatorType(String initialOriginatorType) {
        this.initialOriginatorType = initialOriginatorType;
    }

    /**
     * @return the contactPointId
     */
    public String getContactPointId() {
        return contactPointId;
    }

    /**
     * @param contactPointId
     *            the contactPointId to set
     */
    public void setContactPointId(String contactPointId) {
        this.contactPointId = contactPointId;
    }

    /**
     * @return the contactPointType
     */
    public String getContactPointType() {
        return contactPointType;
    }

    /**
     * @param contactPointType
     *            the contactPointType to set
     */
    public void setContactPointType(String contactPointType) {
        this.contactPointType = contactPointType;
    }

    /**
     * @return the applicationId
     */
    public String getApplicationId() {
        return applicationId;
    }

    /**
     * @param applicationId
     *            the applicationId to set
     */
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * @return the operatorType
     */
    public String getOperatorType() {
        return operatorType;
    }

    /**
     * @param operatorType
     *            the operatorType to set
     */
    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

}
