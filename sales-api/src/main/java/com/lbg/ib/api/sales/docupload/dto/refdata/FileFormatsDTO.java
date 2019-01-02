/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * 
 * All Rights Reserved.
 * 
 * Class Name: FileFormatsDTO 
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
public class FileFormatsDTO {

    private String code;

    private String contentType;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

}
