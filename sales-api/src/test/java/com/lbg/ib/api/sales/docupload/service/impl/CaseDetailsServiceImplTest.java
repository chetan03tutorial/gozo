/***********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 * All Rights Reserved.
 * Class Name: CaseDetailsServiceImpl
 * Author(s):8768724
 * Date: 05 Jan 2016
 ***********************************************************************/

package com.lbg.ib.api.sales.docupload.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import com.lbg.ib.api.sales.docupload.delegate.*;
import com.lbg.ib.api.sales.docupload.dto.transaction.*;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.ContentDisposition;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;

import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.common.rest.constants.DocUploadConstant;
import com.lbg.ib.api.sales.docupload.dto.refdata.ProcessDTO;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorCodeMapper;
import com.lbg.ib.api.sales.common.rest.error.ResponseErrorVO;
import com.lbg.ib.api.sales.common.rest.constants.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.rest.error.DocUploadServiceException;
import com.lbg.ib.api.sales.docupload.mapper.CaseServiceMapper;
import com.lbg.ib.api.sales.docupload.mapper.DocUploadRefDataServiceMapper;
import com.lbg.ib.api.sales.docupload.mapper.RequestBodyMapper;
import com.lbg.ib.api.sales.docupload.validator.CaseServiceValidator;
import com.lbg.ib.api.sales.docupload.validator.DocumentValidator;
import com.lbg.ib.api.sales.docupload.validator.UploadDocumentValidator;
import com.lloydstsb.ea.config.ConfigurationService;
import com.lloydstsb.ea.config.exceptions.ConfigurationServiceException;
import com.lbg.ib.api.sales.docupload.domain.Case;
import com.lbg.ib.api.sales.docupload.domain.ServiceResponse;


import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 *
 *
 */
@RunWith(PowerMockRunner.class)
public class CaseDetailsServiceImplTest {

	@InjectMocks
	CaseDetailsServiceImpl caseServiceImpl;


	  @Mock CaseServiceValidator validator;

//	  @Mock private GetCaseDelegate getCaseDelegate;


	@Mock
	private DocUploadRefDataServiceMapper serviceHelper;

	@Mock
	private ConfigurationService configurationService;

	@Mock
	RequestBodyMapper bodyMappingHelper;

	@Mock
	private DocumentValidator documentValidator;

	@Mock
	private PreviewDocumentDelegate previewDocumentDelegate;

	@Mock
	CreateCaseDelegate caseDelegate;

	@Mock
	com.lbg.ib.api.sales.docupload.delegate.CaseDelegate caseDelegateService;

	@Mock
	ProcessDTO processDTO;

	/*
	 * @Mock SubmitCaseDelegate submitCaseDelegate;
	 */

	@Mock
	HttpServletRequest httpRequest;

	@Mock
	HttpSession session;

	@Mock
	private CaseServiceMapper caseServiceHelper;

	@Mock
	SearchCaseDelegateService searchCaseService;

	/*
	 * @Mock private DeleteDocumentDelegate deleteDocumentDelegate;
	 */

	/*
	 * @Mock private PreviewDocumentDelegate previewDocumentDelegate;
	 */

	@Mock
	private UpdateCaseDelegate updateCaseDelegate;

	@Mock
	private LoggerDAO logger;

	@Mock
	private ResponseErrorCodeMapper errorCodeMapper;

	/*
	 * @Mock private DocumentValidator documentValidator;
	 */

	@Mock
	private UploadDocumentDelegate uploadDocumentDelegate;

	@Mock
	private UploadDocumentValidator uploadDocumentValidator;

	private String requestBody = "{    \"processCode\":\"AOOIDV-ON-LTB\",    \"caseReferenceNo\":\"40/21000001-98\",    \"partyDetails\":[       {          \"title\":\"Mr\",          \"foreName\":\"Rohit\",          \"surName\":\"Varshney\", 		 \"externalSystemId\":0,          \"casePartyID\":1771,          \"evidences\":[             {                \"evidenceTypeCode\":\"POI\",                \"documents\":[                   {                      \"uploadSequenceNo\":1000,                      \"evidenceTypeCode\":\"POI\",                      \"documentCode\":\"BST\",                      \"createdBy\":\"Customer\",                      \"fileName\":\"Desert.jpg\",                      \"fileSize\":4020,                      \"contentType\":\"image/jpg\",                      \"fileComments\":\"HE IS VERY RICH\",                      \"status\":\"PL\",                      \"previewCount\":10,                      \"trgSysFileRefNum\":\"1\",                      \"tmpSysFileRefNum\":\"1\",                      \"createdDate\":\"02/02/2012\",                      \"updateDate\":\"04/04/2012\", 					 \"srcSessionId\":\"cd1b13e1-91e1-4389-88f8-094ab83c0ce8\"                                         }                ]             }          ]       }    ],    \"casePSF\":[       {          \"casePSFName\":\"Mortgage Roll Number\",          \"casePSFValue\":\"40/21000001-01\"       },       {          \"casePSFName\":\"Mortgage Agreement Number\",          \"casePSFValue\":\"40/21000001-01\"       }    ] }";

	private String requestBodyJson = "Content-Type: application/json\r\n\r\n{}";

	private static final String caseRefNo = "123";

	private static final String status = "Open";

	/*
	 * @Test public void shouldReturnCaseDataWithgGivenCaserefNoNStatus() {
	 *
	 * CaseDTO caseDto = new CaseDTO(); caseDto.setCaseReferenceNo(caseRefNo);
	 * caseDto.setCaseStatus(status);
	 * Mockito.when(validator.validateCaseReferenceNumber(caseRefNo)).thenReturn
	 * (true); Mockito.when(serviceHelper.getProcessCode()).thenReturn("abc");
	 * Mockito.when( getCaseDelegate.retrieveDocUploadCaseData(Mockito.any(
	 * SearchCaseRequestDTO.class))) .thenReturn(caseDto); CaseDTO responseDto =
	 * caseServiceImpl.getCaseDetails(caseRefNo, status);
	 * Assert.assertEquals(caseRefNo, responseDto.getCaseReferenceNo()); }
	 */

	/*
	 * @Test public void shouldReturnNullWithgInvalidCaserefNoNStatus() {
	 *
	 * Mockito.when(validator.validateCaseReferenceNumber(caseRefNo)).thenReturn
	 * (false); Assert.assertNull(caseServiceImpl.getCaseDetails(caseRefNo,
	 * status)); }
	 */

	/*
	 * @Test public void shouldReturnNullWithgInvalidCaserefNo() {
	 *
	 * Mockito.when(validator.validateCaseReferenceNumber(caseRefNo)).thenReturn
	 * (false); Assert.assertNull(caseServiceImpl.getCaseDetails(caseRefNo)); }
	 */
	/*
	 * @Test public void shouldReturnCaseDataWithgGivenCaserefNo() {
	 *
	 * CaseDTO caseDto = new CaseDTO(); caseDto.setCaseReferenceNo(caseRefNo);
	 * Mockito.when(validator.validateCaseReferenceNumber(caseRefNo)).thenReturn
	 * (true); Mockito.when(serviceHelper.getProcessCode()).thenReturn("abc");
	 * when(
	 * configurationService.getConfigurationValueAsString((Mockito.anyString()),
	 * (Mockito.anyString()))).thenReturn("OP"); Mockito.when(
	 * getCaseDelegate.retrieveDocUploadCaseData(Mockito.any(
	 * SearchCaseRequestDTO.class))) .thenReturn(caseDto); CaseDTO responseDto =
	 * caseServiceImpl.getCaseDetails(caseRefNo); Assert.assertEquals(caseRefNo,
	 * responseDto.getCaseReferenceNo()); }
	 */

	@Test
	public void shouldReturnExpectedResult() {
		CaseDTO caseDTO = new CaseDTO();
		caseDTO.setBrand("IBL");
		caseDTO.setCaseId(123);
		caseDTO.setCreatorID("");
		String brandValue = "LTB";

		Case caseDetails = new Case();

		PowerMockito.when(bodyMappingHelper.mapRequestBody(requestBody, Case.class)).thenReturn(caseDetails);
		PowerMockito.when(caseServiceHelper.mapToCaseDTO(any(Case.class))).thenReturn(caseDTO);
//		PowerMockito.
		when(httpRequest.getAttribute(DocUploadConstant.BRAND_DISPLAY_VALUE)).thenReturn(brandValue);
		/*
		 * Mockito.when(validator.validateCaseDtoWithPartyDtls(Mockito.any(
		 * CaseDTO.class))) .thenReturn(true);
		 */
		ProcessDTO processDTO = new ProcessDTO();
		processDTO.setDefaultExpiryPeriod(100);
		when(serviceHelper.getProcessDto()).thenReturn(processDTO);

		  when(validator.validationForCreateCase(any(CaseDTO.
		  class))) .thenReturn(true);

		when(caseDelegateService.createCase(any(CreateCaseRequestDTO.class))).thenReturn(caseDTO);

		when(httpRequest.getSession()).thenReturn(session);

		  ServiceResponse<Case> response =
		  caseServiceImpl.createCase(requestBody);
		  Assert.assertNotNull(response);
		  /*Assert.assertEquals(caseDTO.getBrand(), response.getResult().getBrand());
		  Assert.assertEquals(caseDTO.getCaseId(),
		  response.getResult().getCaseId());*/

	}

//	@SuppressWarnings("unchecked")
//	@Test(expected = DocUploadServiceException.class)
	@Test
	public void testShouldThrowDocUploadServiceException() {
		String errCode = "6000034";
		String errMsg = "No process Code set";
		String brandValue = "LTB";
		when(httpRequest.getAttribute(DocUploadConstant.BRAND_DISPLAY_VALUE)).thenReturn(brandValue);
		int errStatus = 400;
		CaseDTO caseDTO = new CaseDTO();
		caseDTO.setCreatorID("abc");
		caseDTO.setBrand(brandValue);
		when(serviceHelper.getProcessDto()).thenReturn(processDTO);
		when(processDTO.getDefaultExpiryPeriod()).thenReturn(100);
		/*
		 * Mockito.when(validator.validationForCreateCase(caseDTO)).thenReturn(
		 * true); Mockito.when(validator.isOnline()).thenReturn(true);
		 */
		when(bodyMappingHelper.mapRequestBody(requestBody, CaseDTO.class)).thenReturn(caseDTO);
		when(httpRequest.getSession()).thenReturn(session);
//		Mockito.when(caseServiceImpl.createCase(requestBody)).thenThrow(DocUploadServiceException.class);
		ResponseErrorVO responseErrorVO = new ResponseErrorVO(errCode, errMsg, errStatus);

		when(validator.validationForCreateCase(any(CaseDTO.class))).thenThrow( new DocUploadServiceException(responseErrorVO));


		 ServiceResponse response = caseServiceImpl.createCase(requestBody);


		Assert.assertNotNull(response);

//		ResponseErrorVO actualErrorVO = (ResponseErrorVO) response.g);
		Assert.assertEquals(responseErrorVO.getMessage(), response.getError().getMessage());




	}

	@SuppressWarnings("unchecked")
	/*@Test(expected = DocUploadServiceException.class)*/
	@Test
	public void ShouldThrowDocUploadServiceExceptionWrongCreater() {

		CaseResponseDTO caseresponseDTO = new CaseResponseDTO();
		caseresponseDTO.setServiceResponse("Success");
		when(searchCaseService.searchCaseDeleteService(any(String.class),any(String.class))).thenReturn(withResult(caseresponseDTO));
		String errCode = "6000034";
		String errMsg = "No process Code set";
		String brandValue = "LTB";
		when(httpRequest.getAttribute(DocUploadConstant.BRAND_DISPLAY_VALUE)).thenReturn(brandValue);
		int errStatus = 400;
		CaseDTO caseDTO = new CaseDTO();
		caseDTO.setCreatorID("abc");
		caseDTO.setBrand(brandValue);
		when(serviceHelper.getProcessDto()).thenReturn(processDTO);
		when(processDTO.getDefaultExpiryPeriod()).thenReturn(100);
		/*
		 * Mockito.when(validator.validationForCreateCase(caseDTO)).thenReturn(
		 * true); Mockito.when(validator.isOnline()).thenReturn(true);
		 */
//		Mockito.when(bodyMappingHelper.mapRequestBody(requestBody, CaseDTO.class)).thenReturn(caseDTO);
		PowerMockito.when(caseServiceHelper.mapToCaseDTO(any(Case.class))).thenReturn(caseDTO);
		when(httpRequest.getSession()).thenReturn(session);
//		Mockito.when(caseServiceImpl.createCase(requestBody)).thenThrow(DocUploadServiceException.class);
		ResponseErrorVO responseErrorVO = new ResponseErrorVO(errCode, errMsg, errStatus);
		when(validator.validationForCreateCase(any(CaseDTO.
				  class))) .thenReturn(true);

		when(errorCodeMapper.resolve(ResponseErrorConstants.INVALID_CREATOR_ID))
		.thenReturn(responseErrorVO);
//		 Mockito.when(validator.validateCaseDtoWithPartyDtls(Mockito.any(CaseDTO.class))).thenThrow( new DocUploadServiceException(responseErrorVO));


		 ServiceResponse response = caseServiceImpl.createCase(requestBody);


		Assert.assertNotNull(response);

//		ResponseErrorVO actualErrorVO = (ResponseErrorVO) response.g);
//		Assert.assertEquals(responseErrorVO.getMessage(), response.getMessage());




	}

	/*
	 * @Test public void testSubmitCase() {
	 *
	 * CaseDTO caseDTO = new CaseDTO(); caseDTO.setCaseId(123);
	 * Mockito.when(bodyMappingHelper.mapRequestBody(requestBody,
	 * CaseDTO.class)).thenReturn( caseDTO);
	 * Mockito.when(validator.validateSubmitCaseDto(Mockito.any(CaseDTO.class)))
	 * .thenReturn(true);
	 * Mockito.when(serviceHelper.getProcessCode()).thenReturn("abc");
	 * Mockito.when(submitCaseDelegate.submitCaseData(Mockito.any(CaseDTO.class)
	 * )).thenReturn( DocUploadConstant.SUCCESS); Map<String, String> response =
	 * caseServiceImpl.submitCase(requestBody);
	 * Assert.assertEquals(response.get(DocUploadConstant.SUBMIT_RESPONSE_KEY),
	 * DocUploadConstant.SUCCESS); }
	 */

	/*
	 * @Test public void testDeleteDocument() {
	 *
	 * CaseDTO caseDTO = new CaseDTO(); caseDTO.setCaseId(123); List<PartyDTO>
	 * partyList = new ArrayList<PartyDTO>(); PartyDTO party = new PartyDTO();
	 * party.setCasePartyID(1); List<UploadDTO> uploadList = new
	 * ArrayList<UploadDTO>(); UploadDTO upload = new UploadDTO();
	 * upload.setStatus("DL"); uploadList.add(upload);
	 * party.setAttachmentDetails(uploadList); partyList.add(party);
	 * caseDTO.setPartyDetails(partyList);
	 * Mockito.when(bodyMappingHelper.mapRequestBody(requestBody,
	 * CaseDTO.class)).thenReturn( caseDTO); Mockito.when(
	 * documentValidator.validateDocumentService(Mockito.any(CaseDTO.class),
	 * Mockito.any(Boolean.class),
	 * Mockito.any(CaseDTO.class))).thenReturn(true); Mockito .when(
	 * validator.validateGetCaseDetails(Mockito.any(CaseDTO.class),
	 * Mockito.any(CaseDTO.class))).thenReturn(true);
	 * Mockito.when(serviceHelper.getProcessCode()).thenReturn("abc");
	 * Mockito.when(deleteDocumentDelegate.deleteDocumentationItem(Mockito.any(
	 * CaseDTO.class))) .thenReturn(caseDTO); CaseDTO response =
	 * caseServiceImpl.deleteDocument(requestBody); String docStatus =
	 * response.getPartyDetails().get(0).getAttachmentDetails().get(0).getStatus
	 * (); Assert.assertEquals("DL", docStatus); }
	 */

	/*
	 * @Test public void testPreviewDocument() {
	 *
	 * CaseDTO caseDTO = new CaseDTO(); caseDTO.setCaseId(123); List<PartyDTO>
	 * partyList = new ArrayList<PartyDTO>(); PartyDTO party = new PartyDTO();
	 * party.setCasePartyID(1); List<UploadDTO> uploadList = new
	 * ArrayList<UploadDTO>(); UploadDTO upload = new UploadDTO();
	 * upload.setStatus("DL"); uploadList.add(upload);
	 * party.setAttachmentDetails(uploadList); partyList.add(party);
	 * caseDTO.setPartyDetails(partyList);
	 *
	 * Attachment file = null;
	 *
	 * Mockito.when(bodyMappingHelper.mapRequestBody(requestBody,
	 * CaseDTO.class)).thenReturn( caseDTO); Mockito.when(
	 * documentValidator.validateDocumentService(Mockito.any(CaseDTO.class),
	 * Mockito.any(Boolean.class),
	 * Mockito.any(CaseDTO.class))).thenReturn(true); Mockito .when(
	 * validator.validateGetCaseDetails(Mockito.any(CaseDTO.class),
	 * Mockito.any(CaseDTO.class))).thenReturn(true);
	 * Mockito.when(serviceHelper.getProcessCode()).thenReturn("abc");
	 * Mockito.when(httpRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER))
	 * .thenReturn("asfsd");
	 * Mockito.when(previewDocumentDelegate.retrieveDocumentation(Mockito.any(
	 * CaseDTO.class))) .thenReturn(file);
	 *
	 * CaseDTO updateCaseResponse = new CaseDTO(); Mockito.when(
	 * updateCaseDelegate.updateCaseDetails(Mockito.any(CaseDTO.class),
	 * Mockito.anyString())) .thenReturn(updateCaseResponse);
	 *
	 * Response response = caseServiceImpl.previewDocument("Y", requestBody);
	 *
	 * Assert.assertNotNull(response); }
	 *
	 * @Test public void testPreviewDocumentErrorCase() {
	 *
	 * CaseDTO caseDTO = new CaseDTO(); caseDTO.setCaseId(123); List<PartyDTO>
	 * partyList = new ArrayList<PartyDTO>(); PartyDTO party = new PartyDTO();
	 * party.setCasePartyID(1); List<UploadDTO> uploadList = new
	 * ArrayList<UploadDTO>(); UploadDTO upload = new UploadDTO();
	 * upload.setStatus("DL"); uploadList.add(upload);
	 * party.setAttachmentDetails(uploadList); partyList.add(party);
	 * caseDTO.setPartyDetails(partyList);
	 *
	 * Mockito.when(bodyMappingHelper.mapRequestBody(requestBody,
	 * CaseDTO.class)).thenReturn( caseDTO); Mockito.when(
	 * documentValidator.validateDocumentService(Mockito.any(CaseDTO.class),
	 * Mockito.any(Boolean.class),
	 * Mockito.any(CaseDTO.class))).thenReturn(true); Mockito .when(
	 * validator.validateGetCaseDetails(Mockito.any(CaseDTO.class),
	 * Mockito.any(CaseDTO.class))).thenReturn(true);
	 * Mockito.when(serviceHelper.getProcessCode()).thenReturn("abc");
	 * Mockito.when(httpRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER))
	 * .thenReturn("asfsd"); ResponseErrorVO responseErrorVO = new
	 * ResponseErrorVO( ResponseErrorConstants.FAILED_TO_RETRIEVE_FILE,
	 * "Failed to retrieveFile", 503);
	 * Mockito.when(previewDocumentDelegate.retrieveDocumentation(Mockito.any(
	 * CaseDTO.class))) .thenThrow(new DocUploadServiceException(
	 * responseErrorVO));
	 *
	 * CaseDTO updateCaseResponse = new CaseDTO(); Mockito.when(
	 * updateCaseDelegate.updateCaseDetails(Mockito.any(CaseDTO.class),
	 * Mockito.anyString())) .thenReturn(updateCaseResponse);
	 *
	 * Response response = caseServiceImpl.previewDocument("Y", requestBody);
	 *
	 * Assert.assertNotNull(response); ResponseErrorVO actualErrorVO =
	 * (ResponseErrorVO) response.getEntity();
	 * Assert.assertEquals(responseErrorVO.getMessage(),
	 * actualErrorVO.getMessage()); }
	 *
	 * @Test public void testPreviewDocumentFileAlone() throws IOException {
	 *
	 * CaseDTO caseDTO = new CaseDTO(); caseDTO.setCaseId(123); List<PartyDTO>
	 * partyList = new ArrayList<PartyDTO>(); PartyDTO party = new PartyDTO();
	 * party.setCasePartyID(1); List<UploadDTO> uploadList = new
	 * ArrayList<UploadDTO>(); UploadDTO upload = new UploadDTO();
	 * upload.setStatus("DL"); upload.setTmpSysFileRefNum("sdfdf-dd");
	 * uploadList.add(upload); party.setAttachmentDetails(uploadList);
	 * partyList.add(party); caseDTO.setPartyDetails(partyList);
	 *
	 * Attachment file = new Attachment( "file", new ByteArrayResource( new
	 * byte[1]).getInputStream(), new ContentDisposition( "file")); ;
	 *
	 * Mockito.when(bodyMappingHelper.mapRequestBody(requestBody,
	 * CaseDTO.class)).thenReturn( caseDTO); Mockito.when(
	 * documentValidator.validateDocumentService(Mockito.any(CaseDTO.class),
	 * Mockito.any(Boolean.class),
	 * Mockito.any(CaseDTO.class))).thenReturn(true); Mockito .when(
	 * validator.validateGetCaseDetails(Mockito.any(CaseDTO.class),
	 * Mockito.any(CaseDTO.class))).thenReturn(true);
	 * Mockito.when(serviceHelper.getProcessCode()).thenReturn("abc");
	 * Mockito.when(httpRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER))
	 * .thenReturn("asfsd");
	 * Mockito.when(previewDocumentDelegate.retrieveDocumentation(Mockito.any(
	 * CaseDTO.class))) .thenReturn(file);
	 *
	 * CaseDTO updateCaseResponse = new CaseDTO(); Mockito.when(
	 * updateCaseDelegate.updateCaseDetails(Mockito.any(CaseDTO.class),
	 * Mockito.anyString())) .thenReturn(updateCaseResponse);
	 *
	 * Response response = caseServiceImpl.previewDocument("N", requestBody);
	 *
	 * Assert.assertNotNull(response); }
	 */
	@Test
	public void testUploadDocument() {
		CaseDTO casedto = new CaseDTO();
		casedto.setCaseId(12);
		casedto.setPartyDetails(new ArrayList<PartyDTO>());
		List<PartyDTO> partys = new ArrayList<PartyDTO>();
		PartyDTO partyDetail = new PartyDTO();

		Case caseDetails = new Case();

		List<UploadDTO> uploaddtos = new ArrayList<UploadDTO>();
		UploadDTO uploadDTO = new UploadDTO();
		uploadDTO.setFileSize(10);
		uploaddtos.add(uploadDTO);
		partyDetail.setAttachmentDetails(uploaddtos);
		partys.add(partyDetail);
		casedto.setPartyDetails(partys);
		when(bodyMappingHelper.mapRequestBody(Mockito.anyString(), Mockito.eq(CaseDTO.class)))
				.thenReturn(casedto);
		when(bodyMappingHelper.mapRequestBody(requestBody, Case.class)).thenReturn(caseDetails);
		when(caseServiceHelper.mapToCaseDTO(any(Case.class))).thenReturn(casedto);
		when(uploadDocumentValidator.validateUploadDetails(any(CaseDTO.class), Mockito.anyInt(),
				any(CaseDTO.class))).thenReturn(true);
		when(
				uploadDocumentValidator.validateFileUploadCount(any(CaseDTO.class), any(CaseDTO.class)))
				.thenReturn(true);
		when(serviceHelper.getProcessCode()).thenReturn("proces_cde");
		when(uploadDocumentDelegate.uploadDocument(any(CaseDTO.class), any(InputStream.class)))
				.thenReturn("docId");
		CaseDTO updateCaseResponse = new CaseDTO();
		updateCaseResponse.setCaseReferenceNo("ABC");
		when(updateCaseDelegate.updateCaseDetails(any(CaseDTO.class), Mockito.anyString()))
				.thenReturn(updateCaseResponse);
		/*
		 * Mockito.when( getCaseDelegate.retrieveDocUploadCaseData(Mockito.any(
		 * SearchCaseRequestDTO.class))) .thenReturn(casedto);
		 */
		when(httpRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER)).thenReturn("asfsd");
		when(httpRequest.getSession()).thenReturn(session);
		when(session.getAttribute("createdCase")).thenReturn(casedto);
		/*
		 * Mockito.when(httpRequest.getSession().getAttribute("createdCase"))
		 * .thenReturn(casedto);
		 */
		List<Attachment> atts = new ArrayList<Attachment>();
		Attachment jsonPart = new Attachment("id", MediaType.APPLICATION_JSON_VALUE, new CaseDTO());
		Attachment att=null;
		try {
			att = new Attachment("json", new ByteArrayResource(new byte[1]).getInputStream(),
					new ContentDisposition(DocUploadConstant.CONTENT_DISPOSITION_VALUE));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		atts.add(jsonPart);
		atts.add(att);

		  ServiceResponse<Case> response = caseServiceImpl.uploadDocument(requestBodyJson, new MultipartBody(
		  atts));


		  Assert.assertNotNull(response);
		 /* Assert.assertEquals(updateCaseResponse.getCaseReferenceNo(),
		  response.getResult().getCaseReferenceNo());*/

	}

	/*@SuppressWarnings("unchecked")
	@Test(expected = DocUploadServiceException.class)*/
	@SuppressWarnings("unchecked")
	@Test
	public void testUploadDocumentWithInvalidAttach() {
		CaseDTO casedto = new CaseDTO();
		casedto.setCaseId(12);
		casedto.setPartyDetails(new ArrayList<PartyDTO>());
		List<PartyDTO> partys = new ArrayList<PartyDTO>();
		PartyDTO partyDetail = new PartyDTO();

		List<UploadDTO> uploaddtos = new ArrayList<UploadDTO>();
		UploadDTO uploadDTO = new UploadDTO();
		uploadDTO.setFileSize(10);
		uploaddtos.add(uploadDTO);
		partyDetail.setAttachmentDetails(uploaddtos);
		partys.add(partyDetail);
		casedto.setPartyDetails(partys);
		when(bodyMappingHelper.mapRequestBody(Mockito.anyString(), Mockito.eq(CaseDTO.class)))
				.thenReturn(casedto);
//		Mockito.when(uploadDocumentValidator.validateToUpdateScanStatus(Mockito.any(CaseDTO.class))).thenReturn(true);
		when(uploadDocumentValidator.validateUploadDetails(any(CaseDTO.class), Mockito.anyInt(),
				any(CaseDTO.class))).thenReturn(true);
		when(
				uploadDocumentValidator.validateFileUploadCount(any(CaseDTO.class), any(CaseDTO.class)))
				.thenReturn(true);


		when(serviceHelper.getProcessCode()).thenReturn("proces_cde");


		/*
		 * Mockito.when( getCaseDelegate.retrieveDocUploadCaseData(Mockito.any(
		 * SearchCaseRequestDTO.class))) .thenReturn(casedto);
		 */

//		Mockito.when(httpRequest.getSession().getAttribute("createdCase")).thenReturn(casedto);
		List<Attachment> atts = new ArrayList<Attachment>();
		Attachment att=null;
		try {
			att = new Attachment("json", new ByteArrayResource(new byte[1]).getInputStream(),
					new ContentDisposition(DocUploadConstant.CONTENT_DISPOSITION_VALUE));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		atts.add(att);
		when(errorCodeMapper.resolve(ResponseErrorConstants.INVALID_REQUEST_STRUCTURE))
				.thenReturn(new ResponseErrorVO());
		when(caseServiceImpl.uploadDocument(requestBody, new MultipartBody(atts))).thenThrow(DocUploadServiceException.class);
		System.out.println("here 1");
		System.out
				.println("caseServiceImpl" + caseServiceImpl + "\nrequestBodyJson" + requestBodyJson + "\natts" + atts);
//		caseServiceImpl.uploadDocument(requestBodyJson, new MultipartBody(atts));
	}


	@SuppressWarnings("unchecked")
	@Test(expected = IOException.class)
//	@Test
	public void testUploadDocumentWithInvalidAttachIO() throws IOException{
		CaseDTO casedto = new CaseDTO();
		casedto.setCaseId(12);
		casedto.setPartyDetails(new ArrayList<PartyDTO>());
		List<PartyDTO> partys = new ArrayList<PartyDTO>();
		PartyDTO partyDetail = new PartyDTO();

		List<UploadDTO> uploaddtos = new ArrayList<UploadDTO>();
		UploadDTO uploadDTO = new UploadDTO();
		uploadDTO.setFileSize(10);
		uploaddtos.add(uploadDTO);
		partyDetail.setAttachmentDetails(uploaddtos);
		partys.add(partyDetail);
		casedto.setPartyDetails(partys);
		when(bodyMappingHelper.mapRequestBody(Mockito.anyString(), Mockito.eq(CaseDTO.class)))
				.thenReturn(casedto);
//		Mockito.when(uploadDocumentValidator.validateToUpdateScanStatus(Mockito.any(CaseDTO.class))).thenReturn(true);
		when(uploadDocumentValidator.validateUploadDetails(any(CaseDTO.class), Mockito.anyInt(),
				any(CaseDTO.class))).thenReturn(true);
		when(
				uploadDocumentValidator.validateFileUploadCount(any(CaseDTO.class), any(CaseDTO.class)))
				.thenReturn(true);
		when(serviceHelper.getProcessCode()).thenReturn("proces_cde");


		/*
		 * Mockito.when( getCaseDelegate.retrieveDocUploadCaseData(Mockito.any(
		 * SearchCaseRequestDTO.class))) .thenReturn(casedto);
		 */

//		Mockito.when(httpRequest.getSession().getAttribute("createdCase")).thenReturn(casedto);
		List<Attachment> atts = new ArrayList<Attachment>();
		Attachment att=null;
		try {
			att = new Attachment("json", new ByteArrayResource(new byte[1]).getInputStream(),
					new ContentDisposition(DocUploadConstant.CONTENT_DISPOSITION_VALUE));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		atts.add(att);
		when(errorCodeMapper.resolve(ResponseErrorConstants.INVALID_REQUEST_STRUCTURE))
				.thenReturn(new ResponseErrorVO());
		when(caseServiceImpl.uploadDocument(requestBody, new MultipartBody(atts))).thenThrow(IOException.class);
		System.out.println("here 1");
		System.out
				.println("caseServiceImpl" + caseServiceImpl + "\nrequestBodyJson" + requestBodyJson + "\natts" + atts);
		caseServiceImpl.uploadDocument(requestBodyJson, new MultipartBody(atts));
	}



	@SuppressWarnings("unchecked")
	@Test(expected = ConfigurationServiceException.class)
//	@Test
	public void testUploadDocumentWithInvalidAttachConfig() throws IOException{
		CaseDTO casedto = new CaseDTO();
		casedto.setCaseId(12);
		casedto.setPartyDetails(new ArrayList<PartyDTO>());
		List<PartyDTO> partys = new ArrayList<PartyDTO>();
		PartyDTO partyDetail = new PartyDTO();

		List<UploadDTO> uploaddtos = new ArrayList<UploadDTO>();
		UploadDTO uploadDTO = new UploadDTO();
		uploadDTO.setFileSize(10);
		uploaddtos.add(uploadDTO);
		partyDetail.setAttachmentDetails(uploaddtos);
		partys.add(partyDetail);
		casedto.setPartyDetails(partys);
		when(bodyMappingHelper.mapRequestBody(Mockito.anyString(), Mockito.eq(CaseDTO.class)))
				.thenReturn(casedto);
//		Mockito.when(uploadDocumentValidator.validateToUpdateScanStatus(Mockito.any(CaseDTO.class))).thenReturn(true);
		when(uploadDocumentValidator.validateUploadDetails(any(CaseDTO.class), Mockito.anyInt(),
				any(CaseDTO.class))).thenReturn(true);
		when(
				uploadDocumentValidator.validateFileUploadCount(any(CaseDTO.class), any(CaseDTO.class)))
				.thenReturn(true);
		when(serviceHelper.getProcessCode()).thenReturn("proces_cde");


		/*
		 * Mockito.when( getCaseDelegate.retrieveDocUploadCaseData(Mockito.any(
		 * SearchCaseRequestDTO.class))) .thenReturn(casedto);
		 */

//		Mockito.when(httpRequest.getSession().getAttribute("createdCase")).thenReturn(casedto);
		List<Attachment> atts = new ArrayList<Attachment>();
		Attachment att=null;;
		try {
			att = new Attachment("json", new ByteArrayResource(new byte[1]).getInputStream(),
					new ContentDisposition(DocUploadConstant.CONTENT_DISPOSITION_VALUE));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		atts.add(att);
		when(errorCodeMapper.resolve(ResponseErrorConstants.INVALID_REQUEST_STRUCTURE))
				.thenReturn(new ResponseErrorVO());
		when(caseServiceImpl.uploadDocument(requestBody, new MultipartBody(atts))).thenThrow(ConfigurationServiceException.class);
		System.out.println("here 1");
		System.out
				.println("caseServiceImpl" + caseServiceImpl + "\nrequestBodyJson" + requestBodyJson + "\natts" + atts);
		caseServiceImpl.uploadDocument(requestBodyJson, new MultipartBody(atts));
	}


	/*@SuppressWarnings("unchecked")
	@Test(expected = IOException.class)
	public void testconvertFilesToByesIOException(){
		Attachment att=null;;
		try {
			att = new Attachment("json", new ByteArrayResource(new byte[1]).getInputStream(),
					new ContentDisposition(DocUploadConstant.CONTENT_DISPOSITION_VALUE));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Mockito.when(caseServiceImpl.convertFilesToByes(att, null)).thenThrow(IOException.class);
		Mockito.when(errorCodeMapper.resolve(ResponseErrorConstants.INVALID_REQUEST_STRUCTURE))
				.thenReturn(new ResponseErrorVO());

		caseServiceImpl.convertFilesToByes(att, null);
	}*/

	@Test
	public void testPreviewDocument() {

		CaseDTO caseDTO = new CaseDTO();
		caseDTO.setCaseId(123);
		List<PartyDTO> partyList = new ArrayList<PartyDTO>();
		PartyDTO party = new PartyDTO();
		party.setCasePartyID(1);
		List<UploadDTO> uploadList = new ArrayList<UploadDTO>();
		UploadDTO upload = new UploadDTO();
		upload.setStatus("DL");
		uploadList.add(upload);
		party.setAttachmentDetails(uploadList);
		partyList.add(party);
		caseDTO.setPartyDetails(partyList);

		Attachment file=null;
		try {
			file = new Attachment("file", new ByteArrayResource(new byte[1]).getInputStream(),
					new ContentDisposition("file"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		when(bodyMappingHelper.mapRequestBody(requestBody, CaseDTO.class)).thenReturn(caseDTO);
		when(documentValidator.validateDocumentService(any(CaseDTO.class), any(Boolean.class)))
				.thenReturn(true);
		when(serviceHelper.getProcessCode()).thenReturn("abc");
		when(httpRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER)).thenReturn("asfsd");
		when(previewDocumentDelegate.retrieveDocumentation(any(CaseDTO.class))).thenReturn(file);
		Response response = caseServiceImpl.previewDocument(requestBody);

		Assert.assertNotNull(response);
		Assert.assertEquals(response.getStatus(), 200);
	}

	@Test
	public void testPreviewDocumentErrorCase() {

		CaseDTO caseDTO = new CaseDTO();
		caseDTO.setCaseId(123);
		List<PartyDTO> partyList = new ArrayList<PartyDTO>();
		PartyDTO party = new PartyDTO();
		party.setCasePartyID(1);
		List<UploadDTO> uploadList = new ArrayList<UploadDTO>();
		UploadDTO upload = new UploadDTO();
		upload.setStatus("DL");
		uploadList.add(upload);
		party.setAttachmentDetails(uploadList);
		partyList.add(party);
		caseDTO.setPartyDetails(partyList);

		when(bodyMappingHelper.mapRequestBody(requestBody, CaseDTO.class)).thenReturn(caseDTO);
		when(documentValidator.validateDocumentService(any(CaseDTO.class), any(Boolean.class)))
				.thenReturn(true);
		when(serviceHelper.getProcessCode()).thenReturn("abc");
		when(httpRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER)).thenReturn("asfsd");
		ResponseErrorVO responseErrorVO = new ResponseErrorVO(ResponseErrorConstants.FAILED_TO_RETRIEVE_FILE,
				"Failed to retrieveFile", 200);
		when(previewDocumentDelegate.retrieveDocumentation(any(CaseDTO.class)))
				.thenThrow(new DocUploadServiceException(responseErrorVO));
		Response response = caseServiceImpl.previewDocument(requestBody);

		Assert.assertNotNull(response);

		ResponseErrorVO actualErrorVO = (ResponseErrorVO) response.getEntity();
		Assert.assertEquals(responseErrorVO.getMessage(), actualErrorVO.getMessage());

	}

	@Test
	public void testPreviewDocumentForNullResponse() {

		CaseDTO caseDTO = new CaseDTO();
		caseDTO.setCaseId(123);
		List<PartyDTO> partyList = new ArrayList<PartyDTO>();
		PartyDTO party = new PartyDTO();
		party.setCasePartyID(1);
		List<UploadDTO> uploadList = new ArrayList<UploadDTO>();
		UploadDTO upload = new UploadDTO();
		upload.setStatus("DL");
		upload.setTmpSysFileRefNum("sdfdf-dd");
		uploadList.add(upload);
		party.setAttachmentDetails(uploadList);
		partyList.add(party);
		caseDTO.setPartyDetails(partyList);
		Attachment file = null;
		when(bodyMappingHelper.mapRequestBody(requestBody, CaseDTO.class)).thenReturn(caseDTO);
		when(documentValidator.validateDocumentService(any(CaseDTO.class), any(Boolean.class)))
				.thenReturn(true);
		when(serviceHelper.getProcessCode()).thenReturn("abc");
		when(httpRequest.getAttribute(DocUploadConstant.JWT_SID_HEADER)).thenReturn("asfsd");
		when(errorCodeMapper.resolve(ResponseErrorConstants.FAILED_TO_RETRIEVE_FILE))
				.thenReturn(new ResponseErrorVO("600300", "FAILED_TO_RETRIEVE_FILE", 404));
		when(previewDocumentDelegate.retrieveDocumentation(any(CaseDTO.class))).thenReturn(file);
		Response response = caseServiceImpl.previewDocument(requestBody);
		Assert.assertNotNull(response);
		ResponseErrorVO actualErrorVO = (ResponseErrorVO) response.getEntity();
		Assert.assertEquals("FAILED_TO_RETRIEVE_FILE", actualErrorVO.getMessage());
	}



}
