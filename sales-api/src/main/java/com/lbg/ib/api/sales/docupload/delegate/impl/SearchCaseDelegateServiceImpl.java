package com.lbg.ib.api.sales.docupload.delegate.impl;

import com.lbg.ib.api.sales.application.service.ApplicationService;
import com.lbg.ib.api.sales.common.constant.Constants;
import com.lbg.ib.api.sales.common.rest.client.RestContext;
import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.sales.dao.restclient.ExternalRestApiClientDAO;
import com.lbg.ib.api.sales.docupload.delegate.SearchCaseDelegateService;
import com.lbg.ib.api.sales.docupload.dto.transaction.CaseResponseDTO;
import com.lbg.ib.api.sales.docupload.util.SalsaEndpoints;
import com.lbg.ib.api.sales.docupload.util.UriResolver;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;

@Component
public class SearchCaseDelegateServiceImpl implements SearchCaseDelegateService {

    @Autowired
    private LoggerDAO logger;

    @Autowired
    private ExternalRestApiClientDAO externalRestClientInvoker;

    @Autowired
    private UriResolver ocisUriResolver;

    @Autowired
    ApplicationService applicationService;

    @Autowired
    private DAOExceptionHandler exceptionHandler;

    static final String METHOD_NAME = "searchCaseDeleteService";

    @TraceLog
    public DAOResponse<CaseResponseDTO> searchCaseDeleteService(String caseReferenceNo, String processingCode) {
        CaseResponseDTO response = null;
        // prepare the url
        String endpoint = ocisUriResolver.getEndpoint(SalsaEndpoints.SEARCH_CASE_ENDPOINT);

        Map<String, String> pathParams = new HashMap<String, String>();
        pathParams.put(Constants.SearchCaseDelegate.PROCESS_CODE, processingCode);
        pathParams.put(Constants.SearchCaseDelegate.CASE_REFERENCE_NO, caseReferenceNo);

        //Salsa in the name of the files is due to the use of existing apis
        RestContext externalRestContext = buildRequest(pathParams, endpoint);

        try{
            response = externalRestClientInvoker.customPost(externalRestContext,
                    CaseResponseDTO.class);


        }catch(Exception ex){
            logger.logException(this.getClass(), ex);
            DAOError daoError =
                    exceptionHandler.handleException(ex, this.getClass(), METHOD_NAME, caseReferenceNo);
            return withError(daoError);
        }


        return withResult(response);
    }


    /**
     * Builds the request for rest call
     * @param pathParameters
     * @param endpoint
     * @return {@link RestContext}
     */
    private RestContext buildRequest(Map<String, String> pathParameters, String endpoint) {
        Map<String, Object> requestHeader = new HashMap<String, Object>();
        requestHeader.put(Constants.SearchCaseDelegate.REQUEST_HEADER,applicationService.getReverseBrandName());
        return RestContext.SalsaRestContextBuilder.getBuilder(endpoint).requestHeaders(requestHeader).requestBody(pathParameters)
                .build();
    }

}
