package com.lbg.ib.api.sales.header;
/*
Created by Rohit.Soni at 04/06/2018 11:34
*/


import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.ServiceLogger;
import com.lloydstsb.ea.logging.helper.ApplicationRequestContext;
import com.lloydstsb.ea.webservices.handler.WSHeaderDataHandler;


import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.*;
import java.util.List;


public class CustomWSHeaderDataHandler extends WSHeaderDataHandler {
    //Initialising like this as this class is not loaded by Spring
    LoggerDAO serviceLogger = new ServiceLogger();

    @Override
    public boolean handleRequest(MessageContext msgContext) {
        serviceLogger.traceLog(this.getClass(), "Adding custom soap headers");
        boolean isAddingCustomHeaderSuccessful = true;
        isAddingCustomHeaderSuccessful=  super.handleRequest(msgContext);
        List<CustomSoapHeader> customHeaderList= (List<CustomSoapHeader>) ApplicationRequestContext.get("CustomHeadersList");
        if(isAddingCustomHeaderSuccessful && customHeaderList!=null){
            SOAPPart soapPart = ((SOAPMessageContext)msgContext).getMessage().getSOAPPart();
            try {
                final SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
                final SOAPHeader soapHeader = soapEnvelope.getHeader();
                if (soapHeader != null) {
                    for(CustomSoapHeader customSoapHeader: customHeaderList){
                        boolean result = customSoapHeader.addAdditionalHeaders(soapPart);
                        if(!result){
                            serviceLogger.traceLog(this.getClass(),"Not able to add custom soap header");
                            isAddingCustomHeaderSuccessful = false;
                            break;
                        }
                    }
                }else{
                    serviceLogger.traceLog(this.getClass(),"Soap header in the soap envelope is empty");
                    isAddingCustomHeaderSuccessful = false;
                }
            } catch (SOAPException soapExcep) {
                serviceLogger.logException(this.getClass(), soapExcep);
                throw new ServiceException(new ResponseError(ResponseErrorConstants.ERROR_ADDING_HEADERS, "Error occured while adding soap headers"));
            } catch (Exception exception) {
                serviceLogger.logException(this.getClass(), exception);
                throw new ServiceException(new ResponseError(ResponseErrorConstants.ERROR_ADDING_HEADERS, "Error occured while adding soap headers"));
            }
        }
        serviceLogger.traceLog(this.getClass(), "Adding custom soap headers ended successfully");
        return isAddingCustomHeaderSuccessful;
    }
}
