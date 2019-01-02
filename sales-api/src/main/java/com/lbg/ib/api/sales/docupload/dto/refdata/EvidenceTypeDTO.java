/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * 
 * All Rights Reserved.
 * 
 * Class Name: EvidenceTypeDTO   
 *   
 * Author(s): 8768724
 *  
 * Date: 19 Oct 2015
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.dto.refdata;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EvidenceTypeDTO {

    private String            code;

    private String            name;

    private String            longDescription;

    private List<DocumentDTO> document;

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

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public List<DocumentDTO> getDocument() {
        if (document == null) {
            document = new ArrayList<DocumentDTO>();
        }
        return document;
    }

    public void setDocument(List<DocumentDTO> document) {
        this.document = document;
    }
}
