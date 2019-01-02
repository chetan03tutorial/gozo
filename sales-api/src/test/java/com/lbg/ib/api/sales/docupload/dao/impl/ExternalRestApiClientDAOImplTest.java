/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 * Class Name: ExternalRestApiClientDAOImplTest
 *
 * Author(s):8735182
 *
 * Date: 29 Dec 2015
 *
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.dao.impl;

import static org.mockito.Matchers.any;

import javax.servlet.http.HttpServletRequest;

import com.lbg.ib.api.sales.dao.restclient.ExternalRestApiClientDAOImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lbg.ib.api.sales.docupload.dto.refdata.ProcessDTO;
import com.lbg.ib.api.sales.docupload.dto.refdata.ProcessRefDataResponseDTO;
import com.lbg.ib.api.sales.docupload.dto.refdata.RefDataResponseDTO;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorVO;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;
import com.lloydstsb.ea.config.ConfigurationService;

/**
 * @author 8735182
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ExternalRestApiClientDAOImplTest {

    private static final String          ENDPOINT  = "http://myDevServer.com";

    private static final String          PROCESSCD = "1";

    private static final String          ERROR_MSG = "Error";

    @Mock
    private RestTemplate                 restTemplate;

    @Mock
    private LoggerDAO                    logger;

    @Mock
    private HttpServletRequest           httRequest;

    @Mock
    private ResponseErrorCodeMapper      resolver;

    @Mock
    private ConfigurationService         configurationService;

    @InjectMocks
    private ExternalRestApiClientDAOImpl externalRestApiClientDAOImpl;

    @Test(expected = Exception.class)
    public void testSetRequestBodyWithInvalidRequest() throws Exception {
        externalRestApiClientDAOImpl.setRequestBody("http://example.com", true, "IBL");
    }

    @Test
    public void testSetRequestBodyWithInvalidRequestIsDefHeaderFalse() throws Exception {
        Assert.assertTrue(externalRestApiClientDAOImpl.setRequestBody("DUMMY", false, "IBL") != null);
    }

    @Test
    public void validResponseCaseOfGet() {
        ProcessRefDataResponseDTO responseBody = new ProcessRefDataResponseDTO();
        ProcessDTO dataForProcess = new ProcessDTO();
        dataForProcess.setProcessCd(PROCESSCD);
        responseBody.setDocUploadRefDataResponse(new RefDataResponseDTO());
        responseBody.getDocUploadRefDataResponse().setDocUploadRefDataForProcess(dataForProcess);
        ResponseEntity<ProcessRefDataResponseDTO> forEntity = new ResponseEntity<ProcessRefDataResponseDTO>(
                responseBody, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(Mockito.eq(ENDPOINT), Mockito.eq(HttpMethod.GET),
                Mockito.any(HttpEntity.class), Mockito.eq(ProcessRefDataResponseDTO.class))).thenReturn(forEntity);
        Mockito.when(httRequest.getAttribute(DocUploadConstant.BRAND_VALUE)).thenReturn("IBL");
        Mockito.when(httRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER)).thenReturn("ddd");
        Mockito.when(httRequest.getRemoteAddr()).thenReturn("12.33");
        Mockito.when(httRequest.getAttribute(DocUploadConstant.BRAND_DISPLAY_VALUE)).thenReturn("LTB");
        Mockito.when(configurationService.getConfigurationValueAsString("RequestHeaderDetails", "XBrandId"))
                .thenReturn("x-lbg-brand");
        Mockito.when(configurationService.getConfigurationValueAsString("RequestHeaderDetails", "ContentType"))
                .thenReturn("Content-Type");
        Mockito.when(configurationService.getConfigurationValueAsString("RequestHeaderDetails", "XChannel"))
                .thenReturn("x-lbg-channel");
        Mockito.when(configurationService.getConfigurationValueAsString("RequestHeaderDetails", "XSessionId"))
                .thenReturn("x-lbg-sessionId");
        Mockito.when(configurationService.getConfigurationValueAsString("RequestHeaderDetails", "XDeviceIp"))
                .thenReturn("x-lbg-deviceIP");
        ProcessRefDataResponseDTO response = externalRestApiClientDAOImpl.get(ENDPOINT, ProcessRefDataResponseDTO.class,
                true);
        Assert.assertEquals(PROCESSCD,
                response.getDocUploadRefDataResponse().getDocUploadRefDataForProcess().getProcessCd());
    }

    @Test
    public void validResponseCaseOfGetRefData() {
        ProcessRefDataResponseDTO responseBody = new ProcessRefDataResponseDTO();
        ProcessDTO dataForProcess = new ProcessDTO();
        dataForProcess.setProcessCd(PROCESSCD);
        responseBody.setDocUploadRefDataResponse(new RefDataResponseDTO());
        responseBody.getDocUploadRefDataResponse().setDocUploadRefDataForProcess(dataForProcess);
        ResponseEntity<ProcessRefDataResponseDTO> forEntity = new ResponseEntity<ProcessRefDataResponseDTO>(
                responseBody, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(Mockito.eq(ENDPOINT), Mockito.eq(HttpMethod.GET),
                Mockito.any(HttpEntity.class), Mockito.eq(ProcessRefDataResponseDTO.class))).thenReturn(forEntity);
        Mockito.when(httRequest.getRemoteAddr()).thenReturn("12.33");
        Mockito.when(configurationService.getConfigurationValueAsString("RequestHeaderDetails", "XBrandId"))
                .thenReturn("x-lbg-brand");
        Mockito.when(configurationService.getConfigurationValueAsString("RequestHeaderDetails", "ContentType"))
                .thenReturn("Content-Type");
        Mockito.when(configurationService.getConfigurationValueAsString("RequestHeaderDetails", "XChannel"))
                .thenReturn("x-lbg-channel");
        Mockito.when(configurationService.getConfigurationValueAsString("RequestHeaderDetails", "XSessionId"))
                .thenReturn("x-lbg-sessionId");
        Mockito.when(configurationService.getConfigurationValueAsString("RequestHeaderDetails", "XDeviceIp"))
                .thenReturn("x-lbg-deviceIP");
        ProcessRefDataResponseDTO response = externalRestApiClientDAOImpl.getRefDataValue(ENDPOINT,
                ProcessRefDataResponseDTO.class, true, "IBL");
        Assert.assertEquals(PROCESSCD,
                response.getDocUploadRefDataResponse().getDocUploadRefDataForProcess().getProcessCd());
    }

    @Test(expected = DocUploadServiceException.class)
    public void validResponseCaseOfGetRefDataThrowHttpMessageConversionException() {
        ProcessRefDataResponseDTO responseBody = new ProcessRefDataResponseDTO();
        ProcessDTO dataForProcess = new ProcessDTO();
        dataForProcess.setProcessCd(PROCESSCD);
        responseBody.setDocUploadRefDataResponse(new RefDataResponseDTO());
        responseBody.getDocUploadRefDataResponse().setDocUploadRefDataForProcess(dataForProcess);
        ResponseEntity<ProcessRefDataResponseDTO> forEntity = new ResponseEntity<ProcessRefDataResponseDTO>(
                responseBody, HttpStatus.OK);
        Mockito.when(resolver.resolve(any(String.class))).thenReturn(new ResponseErrorVO());
        Mockito.when(restTemplate.exchange(Mockito.eq(ENDPOINT), Mockito.eq(HttpMethod.GET),
                Mockito.any(HttpEntity.class), Mockito.eq(ProcessRefDataResponseDTO.class)))
                .thenThrow(new HttpMessageConversionException("dummy message"));
        Mockito.when(httRequest.getRemoteAddr()).thenReturn("12.33");
        Mockito.when(configurationService.getConfigurationValueAsString("RequestHeaderDetails", "XBrandId"))
                .thenReturn("x-lbg-brand");
        Mockito.when(configurationService.getConfigurationValueAsString("RequestHeaderDetails", "ContentType"))
                .thenReturn("Content-Type");
        Mockito.when(configurationService.getConfigurationValueAsString("RequestHeaderDetails", "XChannel"))
                .thenReturn("x-lbg-channel");
        Mockito.when(configurationService.getConfigurationValueAsString("RequestHeaderDetails", "XSessionId"))
                .thenReturn("x-lbg-sessionId");
        Mockito.when(configurationService.getConfigurationValueAsString("RequestHeaderDetails", "XDeviceIp"))
                .thenReturn("x-lbg-deviceIP");
        ProcessRefDataResponseDTO response = externalRestApiClientDAOImpl.getRefDataValue(ENDPOINT,
                ProcessRefDataResponseDTO.class, true, "IBL");
        Assert.assertEquals(PROCESSCD,
                response.getDocUploadRefDataResponse().getDocUploadRefDataForProcess().getProcessCd());
    }

    @Test(expected = DocUploadServiceException.class)
    public void validResponseCaseOfGetRefDataThrowResourceAccessException() {
        ProcessRefDataResponseDTO responseBody = new ProcessRefDataResponseDTO();
        ProcessDTO dataForProcess = new ProcessDTO();
        dataForProcess.setProcessCd(PROCESSCD);
        responseBody.setDocUploadRefDataResponse(new RefDataResponseDTO());
        responseBody.getDocUploadRefDataResponse().setDocUploadRefDataForProcess(dataForProcess);
        ResponseEntity<ProcessRefDataResponseDTO> forEntity = new ResponseEntity<ProcessRefDataResponseDTO>(
                responseBody, HttpStatus.OK);
        Mockito.when(resolver.resolve(any(String.class))).thenReturn(new ResponseErrorVO());
        Mockito.when(restTemplate.exchange(Mockito.eq(ENDPOINT), Mockito.eq(HttpMethod.GET),
                Mockito.any(HttpEntity.class), Mockito.eq(ProcessRefDataResponseDTO.class)))
                .thenThrow(new ResourceAccessException("dummy message"));
        Mockito.when(httRequest.getRemoteAddr()).thenReturn("12.33");
        Mockito.when(configurationService.getConfigurationValueAsString("RequestHeaderDetails", "XBrandId"))
                .thenReturn("x-lbg-brand");
        Mockito.when(configurationService.getConfigurationValueAsString("RequestHeaderDetails", "ContentType"))
                .thenReturn("Content-Type");
        Mockito.when(configurationService.getConfigurationValueAsString("RequestHeaderDetails", "XChannel"))
                .thenReturn("x-lbg-channel");
        Mockito.when(configurationService.getConfigurationValueAsString("RequestHeaderDetails", "XSessionId"))
                .thenReturn("x-lbg-sessionId");
        Mockito.when(configurationService.getConfigurationValueAsString("RequestHeaderDetails", "XDeviceIp"))
                .thenReturn("x-lbg-deviceIP");
        ProcessRefDataResponseDTO response = externalRestApiClientDAOImpl.getRefDataValue(ENDPOINT,
                ProcessRefDataResponseDTO.class, true, "IBL");
        Assert.assertEquals(PROCESSCD,
                response.getDocUploadRefDataResponse().getDocUploadRefDataForProcess().getProcessCd());
    }

    @Test(expected = DocUploadServiceException.class)
    public void validResponseCaseOfGetRefDataThrowRestClientException() {
        ProcessRefDataResponseDTO responseBody = new ProcessRefDataResponseDTO();
        ProcessDTO dataForProcess = new ProcessDTO();
        dataForProcess.setProcessCd(PROCESSCD);
        responseBody.setDocUploadRefDataResponse(new RefDataResponseDTO());
        responseBody.getDocUploadRefDataResponse().setDocUploadRefDataForProcess(dataForProcess);
        ResponseEntity<ProcessRefDataResponseDTO> forEntity = new ResponseEntity<ProcessRefDataResponseDTO>(
                responseBody, HttpStatus.OK);
        Mockito.when(resolver.resolve(any(String.class))).thenReturn(new ResponseErrorVO());
        Mockito.when(restTemplate.exchange(Mockito.eq(ENDPOINT), Mockito.eq(HttpMethod.GET),
                Mockito.any(HttpEntity.class), Mockito.eq(ProcessRefDataResponseDTO.class)))
                .thenThrow(new RestClientException("dummy message"));
        Mockito.when(httRequest.getRemoteAddr()).thenReturn("12.33");
        Mockito.when(configurationService.getConfigurationValueAsString("RequestHeaderDetails", "XBrandId"))
                .thenReturn("x-lbg-brand");
        Mockito.when(configurationService.getConfigurationValueAsString("RequestHeaderDetails", "ContentType"))
                .thenReturn("Content-Type");
        Mockito.when(configurationService.getConfigurationValueAsString("RequestHeaderDetails", "XChannel"))
                .thenReturn("x-lbg-channel");
        Mockito.when(configurationService.getConfigurationValueAsString("RequestHeaderDetails", "XSessionId"))
                .thenReturn("x-lbg-sessionId");
        Mockito.when(configurationService.getConfigurationValueAsString("RequestHeaderDetails", "XDeviceIp"))
                .thenReturn("x-lbg-deviceIP");
        ProcessRefDataResponseDTO response = externalRestApiClientDAOImpl.getRefDataValue(ENDPOINT,
                ProcessRefDataResponseDTO.class, true, "IBL");
        Assert.assertEquals(PROCESSCD,
                response.getDocUploadRefDataResponse().getDocUploadRefDataForProcess().getProcessCd());
    }
    /*
     * @Test public void validResponseCaseOfPost() { JwtValidationResponseDTO
     * responseBody = new JwtValidationResponseDTO();
     * responseBody.setValidationResponse(DocUploadConstant.SUCCESS);
     * ResponseEntity<JwtValidationResponseDTO> forEntity = new
     * ResponseEntity<JwtValidationResponseDTO>( responseBody, HttpStatus.OK);
     * Mockito.when( restTemplate.exchange(Mockito.eq(ENDPOINT),
     * Mockito.eq(HttpMethod.POST), Mockito.any(HttpEntity.class),
     * Mockito.eq(JwtValidationResponseDTO.class))) .thenReturn(forEntity);
     *
     * JwtValidationResponseDTO response =
     * externalRestApiClientDAOImpl.post(ENDPOINT,
     * JwtValidationResponseDTO.class, new JwtValidationRequestDTO(), false);
     * Assert.assertEquals(DocUploadConstant.SUCCESS,
     * response.getValidationResponse()); }
     *
     * @Test public void validResponseCaseOfPostWithDefualtHeaderTrue() {
     * JwtValidationResponseDTO responseBody = new JwtValidationResponseDTO();
     * responseBody.setValidationResponse(DocUploadConstant.SUCCESS);
     * ResponseEntity<JwtValidationResponseDTO> forEntity = new
     * ResponseEntity<JwtValidationResponseDTO>( responseBody, HttpStatus.OK);
     * Mockito.when( restTemplate.exchange(Mockito.eq(ENDPOINT),
     * Mockito.eq(HttpMethod.POST), Mockito.any(HttpEntity.class),
     * Mockito.eq(JwtValidationResponseDTO.class))) .thenReturn(forEntity);
     * Mockito.when(httRequest.getAttribute(DocUploadConstant.BRAND_VALUE)).
     * thenReturn("IBL");
     * Mockito.when(httRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER)).
     * thenReturn("ddd");
     * Mockito.when(httRequest.getRemoteAddr()).thenReturn("12.33");
     * Mockito.when(httRequest.getAttribute(DocUploadConstant.
     * BRAND_DISPLAY_VALUE)).thenReturn( "LTB"); Mockito.when(
     * configurationService.getConfigurationValueAsString(
     * "RequestHeaderDetails", "XBrandId")) .thenReturn("x-lbg-brand");
     * Mockito.when( configurationService.getConfigurationValueAsString(
     * "RequestHeaderDetails", "ContentType")).thenReturn("Content-Type");
     * Mockito.when( configurationService.getConfigurationValueAsString(
     * "RequestHeaderDetails", "XChannel")) .thenReturn("x-lbg-channel");
     * Mockito.when( configurationService
     * .getConfigurationValueAsString("RequestHeaderDetails",
     * "XSessionId")).thenReturn( "x-lbg-sessionId"); Mockito .when(
     * configurationService.getConfigurationValueAsString(
     * "RequestHeaderDetails", "XDeviceIp")).thenReturn("x-lbg-deviceIP");
     * JwtValidationResponseDTO response =
     * externalRestApiClientDAOImpl.post(ENDPOINT,
     * JwtValidationResponseDTO.class, new JwtValidationRequestDTO(), true);
     * Assert.assertEquals(DocUploadConstant.SUCCESS,
     * response.getValidationResponse()); }
     *
     * @Test(expected = DocUploadServiceException.class) public void
     * InValidStatusCodeReturnCase() { JwtValidationResponseDTO responseBody =
     * new JwtValidationResponseDTO();
     * responseBody.setValidationResponse(DocUploadConstant.SUCCESS);
     * ResponseEntity<JwtValidationResponseDTO> forEntity = new
     * ResponseEntity<JwtValidationResponseDTO>( responseBody,
     * HttpStatus.BAD_GATEWAY); Mockito.when(
     * restTemplate.exchange(Mockito.eq(ENDPOINT), Mockito.eq(HttpMethod.POST),
     * Mockito.any(HttpEntity.class),
     * Mockito.eq(JwtValidationResponseDTO.class))) .thenReturn(forEntity);
     * Mockito .doThrow( new DocUploadServiceException( new ResponseErrorVO(
     * ResponseErrorConstants.EXTERNAL_SERVICE_DOWN, ERROR_MSG,
     * HttpStatus.SERVICE_UNAVAILABLE.value()))).when(restTemplate)
     * .setErrorHandler(Mockito.any(ExternalRestApiErrorHandler.class));
     * externalRestApiClientDAOImpl.post(ENDPOINT,
     * JwtValidationResponseDTO.class, new JwtValidationRequestDTO(), false); }
     *
     * @Test(expected = DocUploadServiceException.class) public void
     * httpMessageConversionExceptionCase() { Mockito.when(
     * restTemplate.exchange(Mockito.eq(ENDPOINT), Mockito.eq(HttpMethod.POST),
     * Mockito.any(HttpEntity.class),
     * Mockito.eq(JwtValidationResponseDTO.class))) .thenThrow(new
     * HttpMessageConversionException( ERROR_MSG));
     * Mockito.when(resolver.resolve(ResponseErrorConstants.
     * EXTERNAL_SERVICE_DOWN)).thenReturn( new ResponseErrorVO(
     * ResponseErrorConstants.EXTERNAL_SERVICE_DOWN, ERROR_MSG,
     * HttpStatus.SERVICE_UNAVAILABLE.value()));
     * externalRestApiClientDAOImpl.post(ENDPOINT,
     * JwtValidationResponseDTO.class, new JwtValidationRequestDTO(), false); }
     *
     * @Test(expected = DocUploadServiceException.class) public void
     * resourceAccessExceptionCase() { Mockito.when(
     * restTemplate.exchange(Mockito.eq(ENDPOINT), Mockito.eq(HttpMethod.POST),
     * Mockito.any(HttpEntity.class),
     * Mockito.eq(JwtValidationResponseDTO.class))) .thenThrow(new
     * ResourceAccessException( ERROR_MSG));
     *
     * externalRestApiClientDAOImpl.post(ENDPOINT,
     * JwtValidationResponseDTO.class, new JwtValidationRequestDTO(), false); }
     *
     * @Test(expected = DocUploadServiceException.class) public void
     * restClientExceptionCase() { Mockito.when(
     * restTemplate.exchange(Mockito.eq(ENDPOINT), Mockito.eq(HttpMethod.POST),
     * Mockito.any(HttpEntity.class),
     * Mockito.eq(JwtValidationResponseDTO.class))) .thenThrow(new
     * RestClientException( ERROR_MSG));
     *
     * externalRestApiClientDAOImpl.post(ENDPOINT,
     * JwtValidationResponseDTO.class, new JwtValidationRequestDTO(), false); }
     */
}
