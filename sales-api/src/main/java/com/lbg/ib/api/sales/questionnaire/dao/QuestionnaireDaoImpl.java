package com.lbg.ib.api.sales.questionnaire.dao;

import static com.lbg.ib.api.sales.dao.constants.DAOErrorConstants.BUSSINESS_ERROR;
import static com.lbg.ib.api.sales.dao.constants.DAOErrorConstants.GENERAL_EXCEPTION;
import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import LIB_SIM_ArrangementNegotiation.ID_ArrangementSetup;
import LIB_SIM_ArrangementNegotiation.RecordArrangementQuestionnaireResultResponse;
import LIB_SIM_GMO.ExternalBusinessError;
import LIB_SIM_GMO.ExternalServiceError;
import LIB_SIM_GMO.InternalServiceError;
import LIB_SIM_GMO.ResourceNotAvailableError;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.questionnaire.domain.QuestionnaireRequestBuilder;
import com.lbg.ib.api.sales.questionnaire.dto.QuestionnaireRequestDTO;
import com.lbg.ib.api.sales.questionnaire.dto.QuestionnaireResponseDTO;

@Component
public class QuestionnaireDaoImpl implements QuestionnaireDao {

    @Autowired
    private LoggerDAO                   logger;

    @Autowired
    private ID_ArrangementSetup         questionnaireWPS;

    @Autowired
    private QuestionnaireRequestBuilder requestBuilder;

    @Autowired
    private SessionManagementDAO        session;

    @Autowired
    private DAOExceptionHandler         daoExceptionHandler;

    @TraceLog
    public DAOResponse<QuestionnaireResponseDTO> recordQuestionnaire(QuestionnaireRequestDTO requestDTO) {

        DAOError error = null;
        RecordArrangementQuestionnaireResultResponse response = null;
        try {
            response = questionnaireWPS.recordArrangementQuestionnaireResult(
                    requestBuilder.buildRequest(requestDTO, session.getUserContext()));
        } catch (ExternalBusinessError e) {
            error = daoExceptionHandler.handleException(e, this.getClass(), "recordQuestionnaire", requestDTO);
        } catch (ExternalServiceError e) {
            error = daoExceptionHandler.handleException(e, this.getClass(), "recordQuestionnaire", requestDTO);
        } catch (ResourceNotAvailableError e) {
            error = daoExceptionHandler.handleException(e, this.getClass(), "recordQuestionnaire", requestDTO);
        } catch (InternalServiceError e) {
            error = daoExceptionHandler.handleException(e, this.getClass(), "recordQuestionnaire", requestDTO);
        } catch (RemoteException e) {
            error = daoExceptionHandler.handleException(e, this.getClass(), "recordQuestionnaire", requestDTO);
        }
        error = validateResponse(response);

        return processResponse(response, error);
    }

    private DAOResponse<QuestionnaireResponseDTO> processResponse(RecordArrangementQuestionnaireResultResponse response,
            DAOError daoError) {
        if (daoError != null) {
            return withError(daoError);
        } else {
            return withResult(requestBuilder.buildResponse(response));
        }
    }

    private DAOError validateResponse(RecordArrangementQuestionnaireResultResponse response) {
        DAOError error = null;
        if (response == null) {
            error = new DAOError(GENERAL_EXCEPTION, "Error in recording the questionnaire");
            logger.logError(error.getErrorCode(), error.getErrorMessage(), this.getClass());
        }
        return error;
    }

}
