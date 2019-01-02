package com.lbg.ib.api.sales.docmanager.resource;

import com.lbg.ib.api.sales.application.Base;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.docmanager.domain.*;
import com.lbg.ib.api.sales.docmanager.service.DocumentMetaContentService;
import com.lbg.ib.api.sales.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Resource to Retrieve/Update the document meta content
 * 
 * @author 8903735
 *
 */
@Path("/party/document")
public class DocumentMetaContentResource extends Base {
    
    private static final String NULL_STRING = "null";

	@Autowired
    private DocumentMetaContentService documentMetaContentService;
    
    @Autowired
    private GalaxyErrorCodeResolver errorResolver;
    
    /**
     * Retrieves the document information for given ocis id
     * 
     * @param ocisId
     * @return response
     */
    @GET
    @Path("/{ocisId}")
    @Produces(APPLICATION_JSON)
    @TraceLog
    public Response retrieveDocumentMetaContent(@PathParam("ocisId") String ocisId) {
        logger.logDebug(this.getClass(), "Retrieve Document Meta Content for ocis id - %s",
                ocisId);
        validate(ocisId);
        // call service to submit the request to OCIS to update the KYC
        DocumentMetaContentResponse response = documentMetaContentService.retrieveDocumentMetaContent(ocisId);
        
        if (response != null) {
            logger.traceLog(this.getClass(),
                    "Inside the DocumentMetaContentResource. Output Response is : " + response);
            return respond(Status.OK, response);
        }
        logger.traceLog(this.getClass(), "Inside the DocumentMetaContentResource. Output Response has error. ");
        return respond(Status.OK, errorResolver.createResponseError(ResponseErrorConstants.SERVICE_UNAVAILABLE));
    }
    
    /**
     * Indexes the Waiver,Notes,Document Reference
     * 
     * @param recordMetaDataContent
     * @return response
     */
    @POST
    @Path("/recordMetaData")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @TraceLog
	public Response recordDocumentMetaContent(@RequestBody RecordMetaDataContent recordMetaDataContent) {
		List<CustomerDocumentInfo> customerDocumentInfoList = recordMetaDataContent.getCustomerDocumentInfo();
		RecordMetaDataContentResponse recordMetaDataContentResponse = null;
		
		//handling of documents for multiple parties
		if (CollectionUtils.isNotEmpty(customerDocumentInfoList)) {
			recordMetaDataContentResponse = new RecordMetaDataContentResponse();
			CustomerDocumentInfoResponse customerDocumentInfoResponse = null;
			List<CustomerDocumentInfoResponse> customerDocumentInfoResponses = new ArrayList<CustomerDocumentInfoResponse>();
			for (CustomerDocumentInfo customerDocumentInfo : customerDocumentInfoList) {
				String ocisId = customerDocumentInfo.getOcisId();
				try {
					validate(ocisId);
					customerDocumentInfoResponse = documentMetaContentService.recordDocumentMetaContent(customerDocumentInfo);
				} catch (Exception e) {
					logger.logException(this.getClass(), e);
					String message = getMessageFromException(e);
					customerDocumentInfoResponse = new CustomerDocumentInfoResponse(ocisId, message, false);
				}
				customerDocumentInfoResponses.add(customerDocumentInfoResponse);
			}
			recordMetaDataContentResponse.setResponse(customerDocumentInfoResponses);
		}

		if (recordMetaDataContentResponse != null
				&& CollectionUtils.isNotEmpty(recordMetaDataContentResponse.getResponse())) {
			logger.traceLog(this.getClass(),
					"Inside the recordDocumentMetaContent. Output Response is : " + recordMetaDataContentResponse);
			return respond(Status.OK, recordMetaDataContentResponse);
		}
		
		logger.traceLog(this.getClass(), "Inside the DocumentMetaContentResource. Output Response has error. ");
		return respond(Status.OK, errorResolver.createResponseError(ResponseErrorConstants.SERVICE_UNAVAILABLE));
	}

	private String getMessageFromException(Exception e) {
		String message = null;
		if (e instanceof ServiceException) {
			ServiceException serviceException = (ServiceException) e;
			ResponseError responseError = serviceException.getResponseError();
			if (responseError != null) {
				message = responseError.getMessage();
			}
		} else {
			message = e.getMessage();
		}
		return message;
	}
    
    /**
     * Validates the param
     * 
     * @param ocisId
     */
    private void validate(String ocisId) {
        if (StringUtils.isBlank(ocisId) || NULL_STRING.equalsIgnoreCase(ocisId)) {
            throw new InvalidFormatException("Party Identifier cannot be null or empty.");
        }
    }
    
    /**
     * Creates and returns response
     * 
     * @param status
     * @param content
     * @return response
     */
    private Response respond(Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }
}
