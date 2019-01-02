package com.lbg.ib.api.sales.product.domain.pending;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * @author Atul Choudhary
 * @version 1.0
 * @since 8thSeptember2016
 ***********************************************************************/
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerName implements Serializable {

    private String   firstName;
    private String   lastName;
    // @JsonIgnore
    private String[] middleNames;
    private String   prefixTitle;
    @JsonIgnore
    private String   salutation;
    @JsonIgnore
    private String   shortFirstName;

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName
     *            the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName
     *            the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the middleNames
     */
    public String[] getMiddleNames() {
        return middleNames;
    }

    /**
     * @param strings
     *            the middleNames to set
     */
    public void setMiddleNames(String[] strings) {
        this.middleNames = strings;
    }

    /**
     * @return the prefixTitle
     */
    public String getPrefixTitle() {
        return prefixTitle;
    }

    /**
     * @param prefixTitle
     *            the prefixTitle to set
     */
    public void setPrefixTitle(String prefixTitle) {
        this.prefixTitle = prefixTitle;
    }

    /**
     * @return the salutation
     */
    public String getSalutation() {
        return salutation;
    }

    /**
     * @param salutation
     *            the salutation to set
     */
    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    /**
     * @return the shortFirstName
     */
    public String getShortFirstName() {
        return shortFirstName;
    }

    /**
     * @param shortFirstName
     *            the shortFirstName to set
     */
    public void setShortFirstName(String shortFirstName) {
        this.shortFirstName = shortFirstName;
    }

}
