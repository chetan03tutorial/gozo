package com.lbg.ib.api.sales.questionnaire;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.rmi.RemoteException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.questionnaire.dao.QuestionnaireDaoImpl;
import com.lbg.ib.api.sales.questionnaire.domain.QuestionnaireRequestBuilder;
import com.lbg.ib.api.sales.questionnaire.dto.QuestionnaireRequestDTO;
import com.lbg.ib.api.sales.questionnaire.dto.QuestionnaireResponseDTO;

import LIB_SIM_ArrangementNegotiation.ID_ArrangementSetup;
import LIB_SIM_ArrangementNegotiation.RecordArrangementQuestionnaireResultRequest;
import LIB_SIM_ArrangementNegotiation.RecordArrangementQuestionnaireResultResponse;
import LIB_SIM_BO.ResultCondition;
import LIB_SIM_GMO.ExternalBusinessError;
import LIB_SIM_GMO.ExternalServiceError;
import LIB_SIM_GMO.InternalServiceError;
import LIB_SIM_GMO.ResourceNotAvailableError;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

@RunWith(MockitoJUnitRunner.class)
public class QuestionnaireDaoImplTest {

    @InjectMocks
    private QuestionnaireDaoImpl        questionnaireDao;

    @Mock()
    private ID_ArrangementSetup         service;

    @Mock
    private QuestionnaireRequestBuilder requestBuilder;

    @Mock
    private ApiServiceProperties        properties;

    @Mock
    private LoggerDAO                   logger;

    @Mock
    private SessionManagementDAO        session;

    @Mock
    private DAOExceptionHandler         daoExceptionHandler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        // logger = new ServiceLogger();
    }

    private RecordArrangementQuestionnaireResultResponse createResponse(String value) {
        RecordArrangementQuestionnaireResultResponse response = new RecordArrangementQuestionnaireResultResponse();
        ResultCondition resultCondition = new ResultCondition();
        resultCondition.setReasonCode(value);
        response.setResultCondition(resultCondition);
        return response;
    }

    private QuestionnaireResponseDTO createResponseDTO(RecordArrangementQuestionnaireResultResponse response) {

        QuestionnaireResponseDTO responseDTO = new QuestionnaireResponseDTO();
        responseDTO.setReasonCode(response.getResultCondition().getReasonCode());
        responseDTO.setReasonText(response.getResultCondition().getReasonText());
        return responseDTO;
    }

    @Test
    public void testSuccessfulReturnFromWebService() throws Exception {
        RecordArrangementQuestionnaireResultResponse response = createResponse("0");
        when(properties.getUriEndpointForRecordQuestionnaire()).thenReturn(new URI("WEB_SERVICE_URL"));
        when(service.recordArrangementQuestionnaireResult(any(RecordArrangementQuestionnaireResultRequest.class)))
                .thenReturn(response);
        when(requestBuilder.buildResponse(response)).thenReturn(createResponseDTO(response));
        DAOResponse<QuestionnaireResponseDTO> daoResponse = questionnaireDao
                .recordQuestionnaire(new QuestionnaireRequestDTO());
        assert (daoResponse.getError() == null);
    }

    @Test
    public void testSuccessfulReturnWithSavingFailureInWebService() throws Exception {
        RecordArrangementQuestionnaireResultResponse response = createResponse("1");
        when(properties.getUriEndpointForRecordQuestionnaire()).thenReturn(new URI("WEB_SERVICE_URL"));
        when(service.recordArrangementQuestionnaireResult(any(RecordArrangementQuestionnaireResultRequest.class)))
                .thenReturn(response);
        when(requestBuilder.buildResponse(response)).thenReturn(createResponseDTO(response));
        DAOResponse<QuestionnaireResponseDTO> daoResponse = questionnaireDao
                .recordQuestionnaire(new QuestionnaireRequestDTO());
        assert (daoResponse.getError() == null);
    }

    @Test
    public void testFailureInWebServiceWithInternalServiceError() throws Exception {

        RecordArrangementQuestionnaireResultResponse response = createResponse("1");
        when(properties.getUriEndpointForRecordQuestionnaire()).thenReturn(new URI("WEB_SERVICE_URL"));
        InternalServiceError e = new InternalServiceError(null, "BaseFault_description", "reasonCode", "reasonText");
        when(service.recordArrangementQuestionnaireResult(any(RecordArrangementQuestionnaireResultRequest.class)))
                .thenThrow(e);
        when(requestBuilder.buildResponse(response)).thenReturn(createResponseDTO(response));
        QuestionnaireRequestDTO request = new QuestionnaireRequestDTO();
        DAOError error = new DAOError("813003", "something went wrong");
        when(daoExceptionHandler.handleException(any(InternalServiceError.class), eq(QuestionnaireDaoImpl.class),
                any(String.class), any())).thenReturn(error);
        DAOResponse<QuestionnaireResponseDTO> daoResponse = questionnaireDao.recordQuestionnaire(request);
        assertThat(daoResponse.getError().getErrorCode(), is("9182301"));

    }

    @Test
    public void testFailureInWebServiceWithExternalBusinessError() throws Exception {

        RecordArrangementQuestionnaireResultResponse response = createResponse("1");
        when(properties.getUriEndpointForRecordQuestionnaire()).thenReturn(new URI("WEB_SERVICE_URL"));
        ExternalBusinessError e = new ExternalBusinessError(null, "BaseFault_description", "reasonCode", "reasonText");
        when(service.recordArrangementQuestionnaireResult(any(RecordArrangementQuestionnaireResultRequest.class)))
                .thenThrow(e);
        when(requestBuilder.buildResponse(response)).thenReturn(createResponseDTO(response));
        QuestionnaireRequestDTO request = new QuestionnaireRequestDTO();
        DAOError error = new DAOError("813003", "something went wrong");
        when(daoExceptionHandler.handleException(any(InternalServiceError.class), eq(QuestionnaireDaoImpl.class),
                any(String.class), any())).thenReturn(error);
        DAOResponse<QuestionnaireResponseDTO> daoResponse = questionnaireDao.recordQuestionnaire(request);
        assertThat(daoResponse.getError().getErrorCode(), is("9182301"));

    }

    @Test
    public void testFailureInWebServiceWithExternalServiceError() throws Exception {

        RecordArrangementQuestionnaireResultResponse response = createResponse("1");
        when(properties.getUriEndpointForRecordQuestionnaire()).thenReturn(new URI("WEB_SERVICE_URL"));
        ExternalServiceError e = new ExternalServiceError(null, "BaseFault_description", "reasonCode", "reasonText");
        when(service.recordArrangementQuestionnaireResult(any(RecordArrangementQuestionnaireResultRequest.class)))
                .thenThrow(e);
        when(requestBuilder.buildResponse(response)).thenReturn(createResponseDTO(response));
        QuestionnaireRequestDTO request = new QuestionnaireRequestDTO();
        DAOError error = new DAOError("813003", "something went wrong");
        when(daoExceptionHandler.handleException(any(InternalServiceError.class), eq(QuestionnaireDaoImpl.class),
                any(String.class), any())).thenReturn(error);
        DAOResponse<QuestionnaireResponseDTO> daoResponse = questionnaireDao.recordQuestionnaire(request);
        assertThat(daoResponse.getError().getErrorCode(), is("9182301"));

    }

    @Test
    public void testFailureInWebServiceWithResourceNotAvailableError() throws Exception {

        RecordArrangementQuestionnaireResultResponse response = createResponse("1");
        when(properties.getUriEndpointForRecordQuestionnaire()).thenReturn(new URI("WEB_SERVICE_URL"));
        ResourceNotAvailableError e = new ResourceNotAvailableError(null, "BaseFault_description");
        when(service.recordArrangementQuestionnaireResult(any(RecordArrangementQuestionnaireResultRequest.class)))
                .thenThrow(e);
        when(requestBuilder.buildResponse(response)).thenReturn(createResponseDTO(response));
        QuestionnaireRequestDTO request = new QuestionnaireRequestDTO();
        DAOError error = new DAOError("813003", "something went wrong");
        when(daoExceptionHandler.handleException(any(InternalServiceError.class), eq(QuestionnaireDaoImpl.class),
                any(String.class), any())).thenReturn(error);
        DAOResponse<QuestionnaireResponseDTO> daoResponse = questionnaireDao.recordQuestionnaire(request);
        assertThat(daoResponse.getError().getErrorCode(), is("9182301"));

    }

    @Test
    public void testFailureInWebServiceWithRemoteException() throws Exception {

        RecordArrangementQuestionnaireResultResponse response = createResponse("1");
        when(properties.getUriEndpointForRecordQuestionnaire()).thenReturn(new URI("WEB_SERVICE_URL"));
        RemoteException e = new RemoteException("reasonText");
        when(service.recordArrangementQuestionnaireResult(any(RecordArrangementQuestionnaireResultRequest.class)))
                .thenThrow(e);
        when(requestBuilder.buildResponse(response)).thenReturn(createResponseDTO(response));
        QuestionnaireRequestDTO request = new QuestionnaireRequestDTO();
        DAOError error = new DAOError("813003", "something went wrong");
        when(daoExceptionHandler.handleException(any(InternalServiceError.class), eq(QuestionnaireDaoImpl.class),
                any(String.class), any())).thenReturn(error);
        DAOResponse<QuestionnaireResponseDTO> daoResponse = questionnaireDao.recordQuestionnaire(request);
        assertThat(daoResponse.getError().getErrorCode(), is("9182301"));

    }

}
