package com.lbg.ib.api.sales.cbs.service.header;
/*
Created by Rohit.Soni at 04/06/2018 13:35
*/

import com.lbg.ib.api.sales.header.CustomSoapHeader;


import javax.xml.soap.*;

public class CbsAppGrpHeader implements CustomSoapHeader {

    private static final String HEADER_NAME = "CBSAppGrp";
    private static final String CBS_APPLCN_GROUP_LOOKUP_ID = "CBS_Applcn_Group_Lookup_Id";
    private String orginatingSortCode;

    public boolean addAdditionalHeaders(SOAPPart soapPart) throws SOAPException {
        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
        SOAPHeader soapHeader = soapEnvelope.getHeader();
        SOAPElement elem_cbs_grp = soapHeader.addHeaderElement(soapEnvelope.createName(HEADER_NAME, null, "http://www.lloydstsb.com/Schema/Infrastructure/SOAP"));
        SOAPElement selem_contact_point_type = elem_cbs_grp.addChildElement(soapEnvelope.createName(CBS_APPLCN_GROUP_LOOKUP_ID, null, "http://www.lloydstsb.com/Schema/Infrastructure/SOAP"));
        if(this.getOrginatingSortCode()!=null){
            selem_contact_point_type.setValue(this.getOrginatingSortCode());
            return true;
        }else{
            return false;
        }
    }

    public String getHeaderName() {
        return HEADER_NAME;
    }

    public String getOrginatingSortCode() {
        return orginatingSortCode;
    }

    public void setOrginatingSortCode(String orginatingSortCode) {
        this.orginatingSortCode = orginatingSortCode;
    }
}
