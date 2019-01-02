package com.lbg.ib.api.sales.common.util;

import com.lloydstsb.ea.dao.header.HeaderData;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by debashish on 5/23/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class SOAPMCAHeaderUtilityTest {

    @InjectMocks
    SOAPMCAHeaderUtility headerUtility = null;

    @Test
    public void testPrepareHeaderData() {
        HeaderData headerData = headerUtility.prepareHeaderData("aServiceName", "anAction");
        Assert.assertTrue(headerData.getHeader() != null);
        Assert.assertTrue(headerData.getServiceRequestHeader() != null);
    }
}
