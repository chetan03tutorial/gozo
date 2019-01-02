package com.lbg.ib.api.sales.questionnaire.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;
import com.lbg.ib.api.sales.questionnaire.domain.message.QuestionnaireRequestBean;
import com.lbg.ib.api.sales.questionnaire.domain.message.QuestionnaireResponseBean;
import com.lbg.ib.api.sales.questionnaire.service.QuestionnaireService;

@Path("/questionnaire/record")
public class QuestionnaireResource {

    @Autowired
    private RequestBodyResolver     resolver;

    @Autowired
    private FieldValidator          fieldValidator;

    @Autowired
    private QuestionnaireService    service;

    @Autowired
    private GalaxyErrorCodeResolver exceptionResolver;

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    @TraceLog
    public Response recordQuestionnaire(String requestBody) throws InvalidFormatException {
        QuestionnaireRequestBean requestBean = resolver.resolve(requestBody, QuestionnaireRequestBean.class);
        QuestionnaireResponseBean responseBean = null;
        validateRequest(requestBean);
        responseBean = service.recordQuestionnaire(requestBean);

        if (responseBean != null) {
            return respond(Status.OK, responseBean);
        }
        return respond(Status.OK, exceptionResolver.resolve(ResponseErrorConstants.SERVICE_UNAVAILABLE));
    }

    private void validateRequest(QuestionnaireRequestBean requestBean) throws InvalidFormatException {
        ValidationError validationError = fieldValidator.validateInstanceFields(requestBean);
        if (validationError != null) {
            throw new InvalidFormatException(validationError.getMessage());
        }
    }

    private Response respond(Response.Status status, Object content) {
        return new ResponseBuilderImpl().status(status).entity(content).build();
    }
}
