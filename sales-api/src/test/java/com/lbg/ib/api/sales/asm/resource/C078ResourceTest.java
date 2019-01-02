package com.lbg.ib.api.sales.asm.resource;

import com.lbg.ib.api.sales.asm.domain.AppScoreRequest;
import com.lbg.ib.api.sales.asm.service.C078Service;
import com.lbg.ib.api.sales.dao.mapper.C078RequestMapper;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lloydstsb.www.C078Resp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Response;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by Debashish Bhattacharjee on 29/05/2018.
 */

@RunWith(MockitoJUnitRunner.class)
public class C078ResourceTest {
    @Mock
    private C078Service service;

    @InjectMocks
    private C078Resource resource;

    @Test
    public void testFetchAppScoreInformation(){
        AppScoreRequest request = new AppScoreRequest();
        C078Resp response = new C078Resp();
        when(service.invokeC078(any(AppScoreRequest.class))).thenReturn(response);

        Response actualResponse = resource.fetchAppScoreInformation(request);
        assertNotNull(actualResponse);
    }
}
