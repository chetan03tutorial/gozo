package com.lbg.ib.api.sales.docgen.mapper;
/*
Created by 8601769 at 02/05/2018 11:49
This class maps the Generate Document request to the request required by Gen-Doc Salsa service
*/


import com.google.common.collect.Lists;
import com.lbg.ib.api.sales.common.util.DateUtil;
import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.docgen.domain.DocGenAndSaveRequest;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.service.SoaAbstractService;
import com.lbg.ib.api.sales.shared.soap.header.SoapHeaderGenerator;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.DocumentFormat;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.DocumentationContent;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.DocumentationItem;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.InformationContent;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.RequestHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;
import com.lbg.ib.api.sales.soapapis.gendoc.IA_GenerateDocument;
import com.lbg.ib.api.sales.soapapis.gendoc.reqrsp.GenerateDocumentRequest;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.sales.user.domain.SelectedAccount;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sso.domain.user.UserContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GenerateDocumentMapper extends SoaAbstractService {
    private static final Logger LOG = Logger.getLogger(GenerateDocumentMapper.class);

    @Autowired
    private LoggerDAO logger;

    @Autowired
    private GBOHeaderUtility gboHeaderUtility;

    @Autowired
    private SessionManagementDAO sessionManager;

    @Autowired
    private SoapHeaderGenerator soapHeaderGenerator;

    @Autowired
    private ModuleContext beanLoader;

    private static final String BUSINESS_TRANSACTION = "generateDocument";
    private static final String SERVICE_ACTION = "generateDocument";
    private static final String SERVICE_NAME = "generateDocument";
    private static final int MAX_ADDRESS_LINES = 8;

    public GenerateDocumentRequest creatGenerateDocumentRequest(DocGenAndSaveRequest docGenAndSaveRequest, PartyDetails party){
        logger.traceLog(this.getClass(), "Entering create@CreateCaseRequestMapper");
        RequestHeader requestHeader = new RequestHeader();
        final GenerateDocumentRequest request = new GenerateDocumentRequest();
        request.setHeader(prepareSoapHeaders());
        // request.setCommunicationType();
        DocumentationItem documentationItem = new DocumentationItem();
        documentationItem.setFormat(DocumentFormat.PDF);
        documentationItem.setHasContent(createDocumentContent(docGenAndSaveRequest, party));
        request.setDocumentationItem(documentationItem);
        logger.traceLog(this.getClass(), "Exiting creatGenerateDocumentRequest");
        return request;
    }

    private RequestHeader prepareSoapHeaders() {
        RequestHeader requestHeader = new RequestHeader();
        List<SOAPHeader> soapHeaders = gboHeaderUtility.prepareSoapHeader(SERVICE_ACTION, SERVICE_NAME);
        requestHeader.setLloydsHeaders(soapHeaders.toArray(new SOAPHeader[soapHeaders.size()]));
        requestHeader.setBusinessTransaction(BUSINESS_TRANSACTION);
        UserContext userContext = sessionManager.getUserContext();
        if (userContext != null) {
            requestHeader.setChannelId(userContext.getChannelId());
        }
        requestHeader.setInteractionId(sessionManager.getSessionId());
        return requestHeader;
    }

    private DocumentationContent createDocumentContent(DocGenAndSaveRequest docGenAndSaveRequest, PartyDetails party){
        DocumentationContent documentationContent = new DocumentationContent();
        SelectedAccount accountToConvert = sessionManager.getAccountToConvertInContext();
        documentationContent.setContentTemplateId(docGenAndSaveRequest.getTemplateName());

        List<InformationContent> informationContents = Lists.newArrayList();
        informationContents.add(setInformationContent("IB.Product.Mnemonic",docGenAndSaveRequest.getProductMnemonic(),0));
        informationContents.add(setInformationContent("IB.Customer.LastName",party.getSurname(),0));
        informationContents.add(setInformationContent("IB.Customer.Title",party.getTitle(),0));
        informationContents.add(setInformationContent("IB.Customer.FirstName",party.getFirstName(),0));
        informationContents.add(setInformationContent("IB.Customer.Addr.PostCode",party.getPostalCode(),0));
        informationContents.add(setInformationContent("IB.Customer.Addr.PostCodeMasked",maskValue(party.getPostalCode()),0));
        informationContents.add(setInformationContent("IB.Product.AccountNumberMasked",maskValue(accountToConvert.getAccountNumber()),0));
        informationContents.add(setInformationContent("IB.Product.PCA.AccountNumber",accountToConvert.getAccountNumber(),0));
        informationContents.add(setInformationContent("IB.Product.PCA.SortCode",accountToConvert.getSortCode(),0));
        addAddressLinesToContent(informationContents, party.getAddressLines());
        informationContents.add(setInformationContent("IB.Customer.IssueDate", DateUtil.getCurrentUkDateOnlyAsString(),0));
        if(docGenAndSaveRequest.getEmailTokens() != null && !docGenAndSaveRequest.getEmailTokens().isEmpty()) {
            for (Map.Entry<String, String> token : docGenAndSaveRequest.getEmailTokens().entrySet()) {
                informationContents.add(setInformationContent(token.getKey(), token.getValue(), 0));
            }
        }
        InformationContent[] hasSpecifiedContent = informationContents.toArray(new InformationContent[0]);
        documentationContent.setHasSpecifiedContent(hasSpecifiedContent);
        return documentationContent;
    }

    private void addAddressLinesToContent(List<InformationContent> informationContents, String[] addressLines){
        int addressLineNumber = 0;
        if(addressLines != null){
            for(String addrLine: addressLines){
                if(StringUtils.isNotEmpty(addrLine)){
                    addressLineNumber++;
                    informationContents.add(setInformationContent("IB.Product.Addr.AddressLine"+addressLineNumber, addrLine,0));
                }
            }
        }
        //Stuff blank lines for missing lines
        while(addressLineNumber < MAX_ADDRESS_LINES){
            addressLineNumber++;
            informationContents.add(setInformationContent("IB.Product.Addr.AddressLine"+addressLineNumber,"",0));
        }
    }

    private InformationContent setInformationContent(String key, String value, Integer order){
        InformationContent informationContent = new InformationContent();
        informationContent.setKey(key);
        informationContent.setValue(value);
        informationContent.setOrder(order);
        return informationContent;
    }

    public Class<?> getPort() {
        return IA_GenerateDocument.class;
    }

    private String maskValue(String value) {
        if (value == null) {
            return null;
        }
        String maskedValue = null;
        char[] characters = null;
        if (StringUtils.isNotEmpty(value)) {
            characters = value.trim().replaceAll(" ", "").toCharArray();
            for (int index = 0; index < characters.length / 2; index++) {
                characters[index] = 'X';
            }
        }
        maskedValue = String.valueOf(characters);
        return maskedValue;
    }
}
