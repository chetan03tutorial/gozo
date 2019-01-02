/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * 
 * All Rights Reserved.
 * 
 * Class Name: ProcessPsfDTO   
 *   
 * Author(s): 8768724
 *  
 * Date: 20 Oct 2015
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.dto.refdata;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProcessPsfDTO {

    private String  name;

    private Integer size;

    private String  format;

    private String  mandatoryFlag;

    private String  searchField;

    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the size
     */
    public Integer getSize() {
        return size;
    }

    /**
     * @param size
     *            the size to set
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getMandatoryFlag() {
        return this.mandatoryFlag;
    }

    public void setMandatoryFlag(String mandatoryFlag) {
        this.mandatoryFlag = mandatoryFlag;
    }

}
