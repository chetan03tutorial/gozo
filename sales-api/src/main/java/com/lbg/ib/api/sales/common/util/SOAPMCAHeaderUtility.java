package com.lbg.ib.api.sales.common.util;

import org.springframework.stereotype.Component;

import com.lloydstsb.ea.dao.enums.ApplicationAttributes;
import com.lloydstsb.ea.dao.header.BAPISOAPHeader;
import com.lloydstsb.ea.dao.header.ContextBAPIHeader;
import com.lloydstsb.ea.dao.header.HeaderData;
import com.lloydstsb.ea.logging.constants.ApplicationAttribute;
import com.lloydstsb.ea.logging.helper.ApplicationRequestContext;

@Component
public class SOAPMCAHeaderUtility {

    /**
     * @param aServiceName
     *            -String
     * @param anAction
     *            -String
     * @return Prepares headerdata for SOAPHeader
     */
    public HeaderData prepareHeaderData(String aServiceName, String anAction) {
        HeaderData headerData = new HeaderData();
        headerData.setServiceName(aServiceName);
        headerData.setAction(anAction);
        BAPISOAPHeader bapiSOAPHeader = new BAPISOAPHeader();
        ContextBAPIHeader contextBAPIHeader = new ContextBAPIHeader();
        if (ApplicationRequestContext.get(ApplicationAttributes.DEVICE_LANGUAGE) != null) {
            contextBAPIHeader
                    .setAcceptLanguage(ApplicationRequestContext.get(ApplicationAttributes.DEVICE_LANGUAGE).toString());
        }
        if (ApplicationRequestContext.get(ApplicationAttribute.DEVICE_IP) != null) {
            contextBAPIHeader
                    .setIpaddressCaller(ApplicationRequestContext.get(ApplicationAttribute.DEVICE_IP).toString());
        }
        bapiSOAPHeader.setContextBAPIHeader(contextBAPIHeader);
        headerData.setHeader(bapiSOAPHeader);
        return headerData;
    }

}
