package com.lbg.ib.api.sales.common.util;

import java.io.IOException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by pbabb1 on 5/23/2016.
 */
public class ApplicationPropertiesTest {
    @Test
    public void returnValueIfTheKeyIsValid() throws IOException {
        Properties applicationProperties = ApplicationProperties.getProperties();
        String value = applicationProperties.getProperty("sales-api.icobs.urca.arrangement.question.ids");
        Assert.assertEquals("WWTI,MOPI,AAUR,HECP,UQ1,UQ2,UQ6", value);
    }

    @Test
    public void returnNullIfTheKeyIsValid() throws IOException {
        Properties applicationProperties = ApplicationProperties.getProperties();
        String value = applicationProperties.getProperty("some.key");
        Assert.assertEquals(null, value);
    }

    @Test
    public void returnSamePropertiesInstanceOnMultipleInvocations() throws IOException {
        Properties applicationProperties = ApplicationProperties.getProperties();
        Assert.assertEquals(applicationProperties, ApplicationProperties.getProperties());
    }

    @Test
    public void returnValueOfPropertyWithoutPropertiesInstance() throws IOException {
        String value = ApplicationProperties.getPropValue("sales-api.icobs.urca.arrangement.question.ids");
        Assert.assertEquals("WWTI,MOPI,AAUR,HECP,UQ1,UQ2,UQ6", value);
    }
}
