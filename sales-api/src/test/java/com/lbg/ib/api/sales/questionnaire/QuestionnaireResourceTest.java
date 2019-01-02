package com.lbg.ib.api.sales.questionnaire;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;
import com.lbg.ib.api.sales.questionnaire.domain.QuestionnaireRequestBuilder;
import com.lbg.ib.api.sales.questionnaire.domain.message.QuestionnaireRequestBean;
import com.lbg.ib.api.sales.questionnaire.domain.message.QuestionnaireResponseBean;
import com.lbg.ib.api.sales.questionnaire.resource.QuestionnaireResource;
import com.lbg.ib.api.sales.questionnaire.service.QuestionnaireServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class QuestionnaireResourceTest {

    @InjectMocks
    QuestionnaireResource               resource;

    @Mock
    private RequestBodyResolver         resolver;

    @Mock
    private GalaxyErrorCodeResolver     codeResolver;

    @Mock
    private QuestionnaireRequestBuilder requestBuilder;

    @Mock
    private FieldValidator              fieldValidator;

    @Mock
    private QuestionnaireServiceImpl    service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private ValidationError validationError() {
        return new ValidationError("Invalid Format Exception");
    }

    @Test(expected = InvalidFormatException.class)
    public void testInValidRequest() throws Exception {
        QuestionnaireRequestBean questionnaireRequestBean = new QuestionnaireRequestBean();
        when(resolver.resolve(any(String.class), eq(QuestionnaireRequestBean.class)))
                .thenReturn(questionnaireRequestBean);
        when(fieldValidator.validateInstanceFields(questionnaireRequestBean)).thenReturn(validationError());
        resource.recordQuestionnaire("request");
    }

    @Test(expected = ServiceException.class)
    public void testServiceException() throws Exception {
        QuestionnaireRequestBean questionnaireRequestBean = new QuestionnaireRequestBean();
        ServiceException exception = new ServiceException(new ResponseError("9200001 ", "Service Unavailable "));
        when(resolver.resolve(any(String.class), eq(QuestionnaireRequestBean.class)))
                .thenReturn(questionnaireRequestBean);
        when(fieldValidator.validateInstanceFields(any(QuestionnaireRequestBean.class))).thenReturn(null);
        when(service.recordQuestionnaire(any(QuestionnaireRequestBean.class))).thenThrow(exception);
        resource.recordQuestionnaire("Request");
    }

    @Test()
    public void testServiceExceptionWithNoResponse() throws Exception {
        QuestionnaireRequestBean questionnaireRequestBean = new QuestionnaireRequestBean();
        ServiceException exception = new ServiceException(new ResponseError("9200001 ", "Service Unavailable "));
        when(resolver.resolve(any(String.class), eq(QuestionnaireRequestBean.class)))
                .thenReturn(questionnaireRequestBean);
        when(fieldValidator.validateInstanceFields(any(QuestionnaireRequestBean.class))).thenReturn(null);
        when(service.recordQuestionnaire(any(QuestionnaireRequestBean.class))).thenReturn(null);
        resource.recordQuestionnaire("Request");
    }

    @Test
    public void testOkResponse() throws Exception {
        when(resolver.resolve(any(String.class), eq(QuestionnaireRequestBean.class)))
                .thenReturn(new QuestionnaireRequestBean());
        when(fieldValidator.validateInstanceFields(any(QuestionnaireRequestBean.class))).thenReturn(null);
        when(service.recordQuestionnaire(any(QuestionnaireRequestBean.class)))
                .thenReturn(new QuestionnaireResponseBean());
        Response response = resource.recordQuestionnaire("Request");
        assertEquals(200, response.getStatus());
    }

}
