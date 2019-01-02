package com.lbg.ib.api.sales.product.domain.activate;

import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
@RunWith(MockitoJUnitRunner.class)
public class ArrangementIdTest {

    @Mock
    private LoggerDAO      logger;

    @InjectMocks
    private FieldValidator fieldValidator = new FieldValidator();

    @Test
    public void shouldNotReturnErrorForValidArrangementId() throws Exception {
        ArrangementId instance = new ArrangementId("123234");
        assertThat(fieldValidator.validateInstanceFields(instance), is(nullValue()));
    }

    @Test
    public void shouldFailValidation() throws Exception {
        ArrangementId instance = new ArrangementId("asd");
        assertThat(fieldValidator.validateInstanceFields(instance).getMessage(),
                is("Arrangement ID should be a numeric value"));

        assertTrue(fieldValidator.validateInstanceFields(instance).toString() != null);
        assertTrue(fieldValidator.validateInstanceFields(instance).hashCode() != 0);
    }

}