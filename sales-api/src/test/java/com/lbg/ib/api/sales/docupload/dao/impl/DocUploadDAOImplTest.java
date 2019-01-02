package com.lbg.ib.api.sales.docupload.dao.impl;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lbg.ib.api.sales.dao.restclient.ExternalRestApiClientDAO;
import com.lbg.ib.api.sales.docupload.dto.refdata.ProcessDTO;
import com.lbg.ib.api.sales.docupload.dto.refdata.ProcessRefDataResponseDTO;
import com.lbg.ib.api.sales.docupload.dto.refdata.RefDataResponseDTO;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;
import com.lbg.ib.api.sales.docupload.util.DocuploadUtils;

/**
 * @author chandra kachhawaha
 *
 */
@PrepareForTest(com.lbg.ib.api.sales.docupload.util.DocuploadUtils.class)
@RunWith(MockitoJUnitRunner.class)
public class DocUploadDAOImplTest {

    private static String            ENDPOINT                 = "http://myDevServer.com";

    private static String            ENDPOINT1                = "http:/!myDevServer.com";

    private static final String      PROCESSCD                = "1";

    @InjectMocks
    DocUploadDAOImpl                 docUploadDAOImpl;

    @InjectMocks
    DocUploadDAOImpl                 docUploadDAOImpl1;

    @Mock
    private LoggerDAO                logger;

    @Mock
    private DocuploadUtils           utils1;

    @Mock
    private DocuploadUtils           utils                    = Mockito.mock(DocuploadUtils.class);

    private ExternalRestApiClientDAO externalRestApiClientDAO = Mockito.mock(ExternalRestApiClientDAO.class);

    @Mock
    private ResponseErrorCodeMapper  resolver;

    @Before
    public void setUp() throws Exception {
        java.net.URL url = new java.net.URL(ENDPOINT);
        docUploadDAOImpl = new DocUploadDAOImpl(logger, externalRestApiClientDAO, url, resolver);
    }

    @Test
    public void validretrieveDocUploadProcessRefData() throws URISyntaxException, MalformedURLException {
        ProcessRefDataResponseDTO salsaResponse = new ProcessRefDataResponseDTO();
        ProcessDTO dataForProcess = new ProcessDTO();
        dataForProcess.setProcessCd(PROCESSCD);
        salsaResponse.setDocUploadRefDataResponse(new RefDataResponseDTO());
        salsaResponse.getDocUploadRefDataResponse().setDocUploadRefDataForProcess(dataForProcess);
        salsaResponse.getDocUploadRefDataResponse().setServiceResponse(DocUploadConstant.SUCCESS);
        ENDPOINT = ENDPOINT + DocUploadConstant.SLASH_CHAR + PROCESSCD;
        Mockito.when(externalRestApiClientDAO.getRefDataValue(Mockito.eq(ENDPOINT),
                Mockito.eq(ProcessRefDataResponseDTO.class), Mockito.eq(true), Mockito.any(String.class)))
                .thenReturn(salsaResponse);
        ProcessDTO response = docUploadDAOImpl.retrieveDocUploadProcessRefData(PROCESSCD, null);
        Assert.assertEquals(PROCESSCD, response.getProcessCd());
    }

    @Test
    public void validretrieveDocUploadProcessRefDataFromScheduler() {
        ProcessRefDataResponseDTO salsaResponse = new ProcessRefDataResponseDTO();
        ProcessDTO dataForProcess = new ProcessDTO();
        dataForProcess.setProcessCd(PROCESSCD);
        salsaResponse.setDocUploadRefDataResponse(new RefDataResponseDTO());
        salsaResponse.getDocUploadRefDataResponse().setDocUploadRefDataForProcess(dataForProcess);
        salsaResponse.getDocUploadRefDataResponse().setServiceResponse(DocUploadConstant.SUCCESS);
        ENDPOINT = ENDPOINT + DocUploadConstant.SLASH_CHAR + PROCESSCD;
        Mockito.when(externalRestApiClientDAO.getRefDataValue(Mockito.eq(ENDPOINT),
                Mockito.eq(ProcessRefDataResponseDTO.class), Mockito.eq(true), Mockito.any(String.class)))
                .thenReturn(salsaResponse);
        ProcessDTO response = docUploadDAOImpl.retrieveDocUploadProcessRefData(PROCESSCD, "LBT");
        Assert.assertEquals(PROCESSCD, response.getProcessCd());
    }

    @Test(expected = DocUploadServiceException.class)
    public void nullResponseCase() {
        ProcessRefDataResponseDTO salsaResponse = new ProcessRefDataResponseDTO();
        salsaResponse.setDocUploadRefDataResponse(new RefDataResponseDTO());
        salsaResponse.getDocUploadRefDataResponse().setDocUploadRefDataForProcess(null);
        Mockito.when(externalRestApiClientDAO.getRefDataValue(Mockito.eq(ENDPOINT),
                Mockito.eq(ProcessRefDataResponseDTO.class), Mockito.eq(true), Mockito.any(String.class)))
                .thenReturn(salsaResponse);
        docUploadDAOImpl.retrieveDocUploadProcessRefData(PROCESSCD, null);
    }

    @Test(expected = MalformedURLException.class)
    public void withMalformedURLFailureCase() throws MalformedURLException {

        java.net.URL url = new java.net.URL("ENDPOINT1");
        docUploadDAOImpl1 = new DocUploadDAOImpl(logger, externalRestApiClientDAO, url, resolver);

        Mockito.when(docUploadDAOImpl1.retrieveDocUploadProcessRefData(PROCESSCD, null)).thenThrow(new Throwable());

        docUploadDAOImpl.retrieveDocUploadProcessRefData(PROCESSCD, null);
    }

    @Test(expected = DocUploadServiceException.class)
    public void withuriSyntaxFailureCase() throws Exception {

        java.net.URL url = new java.net.URL("http:// myDevServer.com/1/1/1");
        docUploadDAOImpl1 = new DocUploadDAOImpl(logger, externalRestApiClientDAO, url, resolver);
        Mockito.when(docUploadDAOImpl1.retrieveDocUploadProcessRefData(PROCESSCD, null)).thenThrow(new Throwable());

        docUploadDAOImpl.retrieveDocUploadProcessRefData(PROCESSCD, null);
    }

    @Test(expected = DocUploadServiceException.class)
    public void withclassCastingFailureCase() {

        Mockito.when(externalRestApiClientDAO.getRefDataValue("http://myDevServer.com/1/1/1",
                ProcessRefDataResponseDTO.class, true, null)).thenThrow(new ClassCastException());
        docUploadDAOImpl.retrieveDocUploadProcessRefData(PROCESSCD, null);
    }

}
