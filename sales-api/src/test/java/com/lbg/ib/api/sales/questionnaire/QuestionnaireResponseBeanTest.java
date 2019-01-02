package com.lbg.ib.api.sales.questionnaire;

import org.junit.Test;

import com.lbg.ib.api.sales.questionnaire.domain.message.QuestionnaireResponseBean;

public class QuestionnaireResponseBeanTest {

    @Test
    public void testResponseBean() {
        QuestionnaireResponseBean bean = new QuestionnaireResponseBean();
        bean.setReasonCode(0);
        assert (bean.getReasonCode() == 0);

    }
}
