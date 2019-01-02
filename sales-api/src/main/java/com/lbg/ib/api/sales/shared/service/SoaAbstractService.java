/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.shared.service;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.handler.HandlerRegistry;

import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.sales.header.CustomWSHeaderDataHandler;
import com.lbg.ib.api.sales.header.PCASoapHeaderDataHandler;
import com.lbg.ib.api.sales.header.TraceRequestHandler;
import com.lbg.ib.api.sales.shared.invoker.SOAInvoker;
import com.lbg.ib.api.sales.shared.soap.header.SoapHeaderGenerator;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;
import com.lloydstsb.ea.context.ClientContext;
import com.lloydstsb.ea.dao.enums.DAOConditionType;
import com.lloydstsb.ea.dao.header.HeaderData;
import com.lloydstsb.ea.lcsm.BAPIHeader;
import com.lloydstsb.ea.lcsm.BapiInformation;
import com.lloydstsb.ea.lcsm.HostInformation;
import com.lloydstsb.ea.lcsm.OperationalVariables;
import com.lloydstsb.ea.logging.helper.ApplicationRequestContext;
import com.lloydstsb.ea.webservices.handler.WSHeaderDataHandler;

public abstract class SoaAbstractService {

    @Autowired
    private SOAInvoker soaInvoker;

    @Autowired
    private SoapHeaderGenerator soapHeaderGenerator;

    public abstract Class<?> getPort();

    public Object invoke(String operation, Object... params) {
        Class[] argTypes = new Class[params.length];
        int count = 0;
        while (count < params.length) {
            argTypes[count] = params[count].getClass();
            count++;
        }
        return soaInvoker.invoke(getPort(), operation, argTypes, params);
    }

    public BapiInformation bapiInfomation(
            com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.BapiInformation bapiInformationHeader) {
        BapiInformation bapiInformation = new BapiInformation();
        bapiInformation.setBAPIId(bapiInformationHeader.getBAPIId());
        bapiInformation
                .setBAPIOperationalVariables(operationalVariables(bapiInformationHeader.getBAPIOperationalVariables()));
        bapiInformation.setBAPIHeader(bapiHeader(bapiInformationHeader.getBAPIHeader()));
        return bapiInformation;
    }

    public OperationalVariables operationalVariables(
            com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.OperationalVariables operationalVariablesHeader) {
        OperationalVariables operationalVariables = new OperationalVariables();
        operationalVariables.setBBatchRetry(operationalVariablesHeader.isBBatchRetry());
        operationalVariables.setBForceHostCall(operationalVariablesHeader.isBForceHostCall());
        operationalVariables.setBPopulateCache(operationalVariablesHeader.isBPopulateCache());
        return operationalVariables;
    }

    public BAPIHeader bapiHeader(
            com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.BAPIHeader bapiHeaderRequest) {
        BAPIHeader bapiHeader = new BAPIHeader();
        bapiHeader.setChanid(bapiHeaderRequest.getChanid());
        bapiHeader.setChansecmode(bapiHeaderRequest.getChansecmode());
        bapiHeader.setSessionid(bapiHeaderRequest.getSessionid());
        bapiHeader.setIpAddressCaller(bapiHeaderRequest.getIpAddressCaller());
        bapiHeader.setUserAgent(bapiHeaderRequest.getUserAgent());
        bapiHeader.setAcceptLanguage(bapiHeaderRequest.getAcceptLanguage());
        bapiHeader.setInboxidClient(bapiHeaderRequest.getInboxidClient());
        bapiHeader.setUseridAuthor("AAGATEWAY");
        bapiHeader.setStpartyObo(hostInformation(bapiHeaderRequest.getStpartyObo()));
        bapiHeader.setIpAddressCaller(bapiHeaderRequest.getIpAddressCaller());
        return bapiHeader;
    }

    public HostInformation hostInformation(
            com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.HostInformation hostInformtionHeader) {
        HostInformation hostInformation = new HostInformation();
        hostInformation.setHost(hostInformtionHeader.getHost());
        hostInformation.setOcisid(hostInformtionHeader.getOcisid());
        hostInformation.setPartyid(hostInformtionHeader.getPartyid());
        return hostInformation;
    }

    public void setDataHandler(QName portName, HandlerRegistry handlerRegistry) {
        List<HandlerInfo> handlerList = new ArrayList<HandlerInfo>();
        handlerList.add(new HandlerInfo(WSHeaderDataHandler.class, null, null));
        handlerRegistry.setHandlerChain(portName, handlerList);
    }

    public void setCustomDataHandler(QName portName, HandlerRegistry handlerRegistry) {
        List<HandlerInfo> handlerList = new ArrayList<HandlerInfo>();
        handlerList.add(new HandlerInfo(CustomWSHeaderDataHandler.class, null, null));
        handlerRegistry.setHandlerChain(portName, handlerList);
    }

    public void setSoapHeader(ClientContext clientContext, String actionName, String serviceName) {
        setSoapHeader(clientContext, actionName, serviceName, false);
    }

    public void setSoapHeader(ClientContext clientContext, String actionName, String serviceName,
            boolean useServiceName) {
        ApplicationRequestContext.set(DAOConditionType.CLIENT_CTX_SOAP_HEADER.code(), clientContext);
        HeaderData headers = soapHeaderGenerator.prepareHeaderData(actionName, serviceName, useServiceName);
        ApplicationRequestContext.set(DAOConditionType.HEADER_DATA_SOAP_HEADER.code(), headers);
        List<SOAPHeader> defaultSoapHeaders = soapHeaderGenerator.getGenericSoapHeader(actionName, serviceName,
                useServiceName);
        for (SOAPHeader soap : defaultSoapHeaders) {
            if ("bapiInformation".equalsIgnoreCase(soap.getName())) {
                BapiInformation bapiInformation = bapiInfomation(
                        (com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.BapiInformation) soap
                                .getValue());
                ApplicationRequestContext.set(DAOConditionType.BAPI_INFO_SOAP_HEADER.code(), bapiInformation);
            }
        }
    }

    public void registerHeaderDataHandler(QName portName, HandlerRegistry handlerRegistry) {
        List<HandlerInfo> handlerList = new ArrayList<HandlerInfo>();
        handlerList.add(0, new HandlerInfo(PCASoapHeaderDataHandler.class, null, null));
        handlerList.add(1, new HandlerInfo(TraceRequestHandler.class, null, null));
        handlerRegistry.setHandlerChain(portName, handlerList);
    }

}
