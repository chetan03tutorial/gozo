/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 * Class Name: DocUploadDelegateImplTest
 *
 * Author(s):8768724
 *
 * Date: 29 Oct 2015
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.delegate.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.docupload.dao.DocUploadDAO;
import com.lbg.ib.api.sales.docupload.delegate.DocUploadDelegate;
import com.lbg.ib.api.sales.docupload.dto.refdata.ProcessDTO;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;

@RunWith(MockitoJUnitRunner.class)
public class DocUploadDelegateImplTest {

    @InjectMocks
    DocUploadDelegate               docUploadDelegateImpl = new DocUploadDelegateImpl();

    @Mock
    DocUploadDAO                    docUploadDAO;

    @Mock
    private LoggerDAO               logger;

    @Mock
    private ResponseErrorCodeMapper resolver;

    @Test
    public void returnCorrectDocUploadRefTypeDtoWhenRetrieveDocUploadProcessRefDataIsCalledWithCorrectProcessId() {
        ProcessDTO processDto = new ProcessDTO();
        processDto.setUploadSizeLimit(4);
        Mockito.when(docUploadDAO.retrieveDocUploadProcessRefData(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(processDto);
        ProcessDTO processDtoRet = docUploadDelegateImpl.retrieveDocUploadProcessRefData("1");
        Assert.assertEquals(processDtoRet.getUploadSizeLimit(), processDto.getUploadSizeLimit());
    }

    @Test
    public void returnCorrectDocUploadRefTypeDtoWhenRetrieveDocUploadProcessRefDataIsCalledWithIncorrectProcessId() {

        ProcessDTO processDto = new ProcessDTO();
        processDto.setUploadSizeLimit(0);
        Mockito.when(docUploadDAO.retrieveDocUploadProcessRefData(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(processDto);

        ProcessDTO processDtoRet = docUploadDelegateImpl.retrieveDocUploadProcessRefData("6");
        Assert.assertEquals(new Integer(0), processDtoRet.getUploadSizeLimit());
    }

    @Test(expected = DocUploadServiceException.class)
    public void exceptionCaseTestWhenResultIsNull() {

        Mockito.when(docUploadDAO.retrieveDocUploadProcessRefData(Mockito.anyString(), Mockito.anyString()))
                .thenThrow(new IllegalArgumentException());

        ProcessDTO processDtoRet = docUploadDelegateImpl.retrieveDocUploadProcessRefData("6");
        Assert.assertEquals(new Integer(0), processDtoRet.getUploadSizeLimit());
    }

}