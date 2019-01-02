package com.lbg.ib.api.sales.questionnaire;

import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static com.lbg.ib.api.sales.testing.TestingHelper.contentOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.questionnaire.dao.QuestionnaireDaoImpl;
import com.lbg.ib.api.sales.questionnaire.domain.message.QuestionnaireRequestBean;
import com.lbg.ib.api.sales.questionnaire.domain.message.QuestionnaireResponseBean;
import com.lbg.ib.api.sales.questionnaire.dto.QuestionnaireRequestDTO;
import com.lbg.ib.api.sales.questionnaire.dto.QuestionnaireResponseDTO;
import com.lbg.ib.api.sales.questionnaire.service.QuestionnaireServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class QuestionnaireServiceTest {

    @InjectMocks
    private QuestionnaireServiceImpl questionnaireService;

    @Mock
    private QuestionnaireDaoImpl     questionnaireDao;

    @Mock
    private GalaxyErrorCodeResolver  exceptionResolver;

    @Mock
    private LoggerDAO                logger;

    private QuestionnaireRequestBean questionnaireRequestBean() {
        String jsonRequest = contentOf("/questionnaireRequest.json");
        ObjectMapper objectMapper = new ObjectMapper();
        QuestionnaireRequestBean requestBean = null;
        try {
            requestBean = objectMapper.readValue(jsonRequest, QuestionnaireRequestBean.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return requestBean;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Ignore
    @Test
    public void testValidityOfRequestDTO() throws ServiceException {

        QuestionnaireRequestBean questionnaireRequestBean = questionnaireRequestBean();
        Mockito.when(questionnaireDao.recordQuestionnaire(any(QuestionnaireRequestDTO.class)))
                .thenReturn(sendResponseWithSuccess());
        questionnaireService.recordQuestionnaire(questionnaireRequestBean);
        ArgumentCaptor<QuestionnaireRequestDTO> argument = ArgumentCaptor.forClass(QuestionnaireRequestDTO.class);
        Mockito.verify(questionnaireDao).recordQuestionnaire(argument.capture());
        QuestionnaireRequestDTO requestDTO = argument.getValue();
        String productIdentifier = requestDTO.getProductArrangement().getAssociatedProduct().getProductIdentifier();
        assertThat(productIdentifier, is(questionnaireRequestBean.getProduct().getProductIdentifier()));

    }

    @Ignore
    @Test
    public void testToCheckWhetherRecordQuestionnaireServiceIsCalledAndReturnZeroReasonCode() throws ServiceException {
        Mockito.when(questionnaireDao.recordQuestionnaire(Mockito.any(QuestionnaireRequestDTO.class)))
                .thenReturn(sendResponseWithSuccess());
        QuestionnaireResponseBean response = questionnaireService.recordQuestionnaire(questionnaireRequestBean());
        assertThat(response.getReasonCode(), is(0));
    }

    @Test(expected = ServiceException.class)
    public void testToCheckWhetherRecordQuestionnaireServiceIsCalledAndReturnNonZeroReasonCode()
            throws ServiceException {
        Mockito.when(questionnaireDao.recordQuestionnaire(Mockito.any(QuestionnaireRequestDTO.class)))
                .thenReturn(sendResponseWithFailure());
        questionnaireService.recordQuestionnaire(questionnaireRequestBean());
        Mockito.verify(exceptionResolver, times(1)).resolve(eq("200"));
    }

    private DAOResponse<QuestionnaireResponseDTO> sendResponseWithSuccess() {
        QuestionnaireResponseDTO response = new QuestionnaireResponseDTO();
        response.setReasonCode("0");
        return withResult(response);
    }

    private DAOResponse<QuestionnaireResponseDTO> sendResponseWithFailure() {
        DAOError error = new DAOError("200", "Error");
        return withError(error);
    }
}
