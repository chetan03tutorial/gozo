package com.lbg.ib.api.sales.questionnaire.dao;

import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.questionnaire.dto.QuestionnaireRequestDTO;
import com.lbg.ib.api.sales.questionnaire.dto.QuestionnaireResponseDTO;

public interface QuestionnaireDao {

    public DAOResponse<QuestionnaireResponseDTO> recordQuestionnaire(QuestionnaireRequestDTO requestDTO);

}
