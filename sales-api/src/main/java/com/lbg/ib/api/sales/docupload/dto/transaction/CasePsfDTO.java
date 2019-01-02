/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * All Rights Reserved.
 * Class Name: CasePsfDTO
 * Author(s): 8768724
 * Date: 12 Dec 2015
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.dto.transaction;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CasePsfDTO {

    private String casePSFName;

    private String casePSFValue;

    public String getCasePSFName() {
        return casePSFName;
    }

    public void setCasePSFName(String casePSFName) {
        this.casePSFName = casePSFName;
    }

    public String getCasePSFValue() {
        return casePSFValue;
    }

    public void setCasePSFValue(String casePSFValue) {
        this.casePSFValue = casePSFValue;
    }
}
