package com.lbg.ib.api.sales.questionnaire.service;

import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.questionnaire.domain.message.QuestionnaireRequestBean;
import com.lbg.ib.api.sales.questionnaire.domain.message.QuestionnaireResponseBean;

public interface QuestionnaireService {

    public QuestionnaireResponseBean recordQuestionnaire(QuestionnaireRequestBean requestBean) throws ServiceException;
}
