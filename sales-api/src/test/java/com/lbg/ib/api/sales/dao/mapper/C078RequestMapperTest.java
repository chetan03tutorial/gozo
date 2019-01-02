package com.lbg.ib.api.sales.dao.mapper;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

import com.lbg.ib.api.sales.asm.domain.AppScoreRequest;
import com.lbg.ib.api.sales.asm.domain.ApplicationType;
import com.lbg.ib.api.sales.utils.CommonUtils;
import com.lloydstsb.www.C078Req;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.lbg.ib.api.sales.common.constant.Constants.C078Constant.APPLICATION_SOURCE_CD_BRANCH;
import static com.lbg.ib.api.sales.common.constant.Constants.C078Constant.APPLICATION_SOURCE_CD_DIGITAL;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by Debashish Bhattacharjee on 29/05/2018.
 */

@RunWith(MockitoJUnitRunner.class)
public class C078RequestMapperTest {

    @Mock
    CommonUtils commonUtils;
    @InjectMocks
    C078RequestMapper mapper;

    @Test
    public void testCreateRequestWhenBranchContextIsTrue(){
        AppScoreRequest request = new AppScoreRequest();
        request.setApplicationType(ApplicationType.CAAS);
        request.setCreditScoreRequestNo("123456");
        when(commonUtils.isBranchContext()).thenReturn(true);
        C078Req req = mapper.create(request);
        assertTrue(APPLICATION_SOURCE_CD_BRANCH.equals(req.getApplicationSourceCd()));
    }


    @Test
    public void testCreateRequestWhenBranchContextIsFalse(){
        AppScoreRequest request = new AppScoreRequest();
        request.setApplicationType(ApplicationType.CAAS);
        request.setCreditScoreRequestNo("123456");
        when(commonUtils.isBranchContext()).thenReturn(false);
        C078Req req = mapper.create(request);
        assertTrue(APPLICATION_SOURCE_CD_DIGITAL.equals(req.getApplicationSourceCd()));
    }
}
