/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * 
 * All Rights Reserved.
 * 
 * Class Name: DocumentDTO
 *   
 * Author(s): 8768724
 *  
 * Date: 19 Oct 2015
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.dto.refdata;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentDTO {

    private String  code;

    private String  name;

    private Integer uploadFileLimit;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUploadFileLimit() {
        return uploadFileLimit;
    }

    public void setUploadFileLimit(Integer uploadFileLimit) {
        this.uploadFileLimit = uploadFileLimit;
    }

}
