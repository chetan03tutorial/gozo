package com.lbg.ib.api.sales.docgen.service;
/*
Created by Rohit.Soni at 02/05/2018 11:08
*/

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.docgen.mapper.GenerateDocumentMapper;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.docgen.domain.DocGenAndSaveRequest;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.soap.header.SoapHeaderGenerator;
import com.lbg.ib.api.sales.soapapis.gendoc.IA_GenerateDocument;
import com.lbg.ib.api.sales.soapapis.gendoc.reqrsp.GenerateDocumentRequest;
import com.lbg.ib.api.sales.soapapis.gendoc.reqrsp.GenerateDocumentResponse;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenerateDocumentServiceImpl implements GenerateDocumentService {

    @Autowired
    private SessionManagementDAO sessionManager;

    @Autowired
    private ConfigurationDAO configManager;

    @Autowired
    private ModuleContext beanLoader;

    @Autowired
    private LoggerDAO logger;

    @Autowired
    private GenerateDocumentMapper generateDocumentMapper;

    @Autowired
    private GalaxyErrorCodeResolver resolver;

    @Autowired
    private SoapHeaderGenerator soapHeaderGenerator;

    @Autowired
    private IA_GenerateDocument generateDocument;

    @TraceLog
    public byte[] generateDocument(DocGenAndSaveRequest docGenAndSaveRequest, PartyDetails party){
        logger.traceLog(this.getClass(), "Entered generateDocument");
        try{
            final GenerateDocumentRequest generateDocumentRequest =   generateDocumentMapper.creatGenerateDocumentRequest(docGenAndSaveRequest , party);
            final GenerateDocumentResponse generateDocumentResponse = generateDocument.generateDocument(generateDocumentRequest);
            return extractAndValidateServiceResponse(generateDocumentResponse);
        }catch(Exception exception){
            logger.logException(this.getClass(), exception);
            logger.traceLog(this.getClass(), "Error in response of Generate Document");
            throw new ServiceException(new ResponseError(ResponseErrorConstants.ERROR_GENERATING_DOCUMENT, "Error in response of Generate Document"));
        }
    }
    public Class<?> getPort() {
        return IA_GenerateDocument.class;
    }

    @TraceLog
    private byte[] extractAndValidateServiceResponse(GenerateDocumentResponse response) {
        logger.traceLog(this.getClass(), "extractAndValidateServiceResponse starts");
        if (null == response || response.getDocumentationItem()==null || response.getDocumentationItem().getDocument()==null) {
            logger.traceLog(this.getClass(), "Error while validating response of generate document");
            throw new ServiceException(
                    new ResponseError(ResponseErrorConstants.ERROR_GENERATING_DOCUMENT, "Error while validating response of generate document"));
        }
        final byte[] document = response.getDocumentationItem().getDocument();
        return document;
    }
}
