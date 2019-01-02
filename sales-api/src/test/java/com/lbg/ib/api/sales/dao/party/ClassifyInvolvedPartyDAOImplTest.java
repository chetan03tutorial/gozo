/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.dao.party;

import static com.lbg.ib.api.sales.dao.constants.DAOErrorConstants.BUSSINESS_ERROR;
import static com.lbg.ib.api.sales.dao.constants.DAOErrorConstants.REMOTE_EXCEPTION;
import static com.lbg.ib.api.sales.dao.party.classify.ClassifyInvolvedPartyRequestBuilder.CRS_VALUE;
import static com.lbg.ib.api.sales.party.domain.TaxResidencyType.findTaxResidencyTypeFromCode;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.sales.dao.SalsaDAOExceptionHandler;
import com.lbg.ib.api.sales.dao.constants.DAOErrorConstants;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.party.classify.ClassifyInvolvedPartyDAOImpl;
import com.lbg.ib.api.sales.dao.party.classify.ClassifyInvolvedPartyRequestBuilder;
import com.lbg.ib.api.sales.dto.party.ClassifyPartyRequestDTO;
import com.lbg.ib.api.sales.dto.party.ClassifyPartyResponseDTO;
import com.lbg.ib.api.shared.domain.TaxResidencyDetails;
import com.lloydstsb.ib.salsa.crs.faults.ExternalBusinessError;
import com.lloydstsb.ib.salsa.crs.faults.ExternalServiceError;
import com.lloydstsb.ib.salsa.crs.faults.InternalServiceError;
import com.lloydstsb.ib.salsa.crs.faults.ResourceNotAvailableError;
import com.lloydstsb.ib.salsa.crs.messages.AssociatedCategories;
import com.lloydstsb.ib.salsa.crs.messages.Classifications;
import com.lloydstsb.ib.salsa.crs.messages.ClassifyInvolvedPartyRequest;
import com.lloydstsb.ib.salsa.crs.messages.ClassifyInvolvedPartyResponse;
import com.lloydstsb.ib.salsa.crs.messages.ExceptionDetails;

import LB_GBO_Sales.IA_CommonReportingStandards;

@RunWith(MockitoJUnitRunner.class)
public class ClassifyInvolvedPartyDAOImplTest {

    @InjectMocks
    private ClassifyInvolvedPartyDAOImpl                classifyInvolvedPartyDAOImpl;

    @Mock
    private ClassifyInvolvedPartyRequest                classifyInvolvedPartyRequest;

    @Mock
    private IA_CommonReportingStandards                 classifyservice;

    @Mock
    private ClassifyInvolvedPartyRequestBuilder         reqBuilder;

    @Mock
    private LoggerDAO                                   logger;

    @Mock
    private SalsaDAOExceptionHandler                    daoExceptionHandler;

    private static final String                         ERROR_KEY      = "classify";

    private LinkedHashSet<String>                       nationalities  = new LinkedHashSet<String>();

    private LinkedHashSet<TaxResidencyDetails>          taxResidencies = new LinkedHashSet<TaxResidencyDetails>();

    private final ClassifyPartyRequestDTO               request        = sampleRequest();

    private DAOResponse<List<ClassifyPartyResponseDTO>> response;

    private String                                      ERROR_CODE     = "someReasonCode";

    private String                                      ERROR_MESSAGE  = "someReasonText";

    @Before
    public void setup() {
        when(reqBuilder.build(request)).thenReturn(classifyInvolvedPartyRequest);

    }

    @Test
    public void shouldReturnCorrectClassifyPartyResponseDTOWhenWeGetAHappyResponse() throws Exception {
        when(classifyservice.classifyInvolvedParty(classifyInvolvedPartyRequest)).thenReturn(sampleResponse());
        response = callService();
        assertThat(response.getResult(), is(expectedDto()));
        verifyNoMoreInteractions(logger);
    }

    @Test
    public void shouldReturnDAOErrorWhenNoClassificationsAreReturnedInResponse() throws Exception {
        ERROR_CODE = BUSSINESS_ERROR;
        ERROR_MESSAGE = "Classifications can not be null";
        when(classifyservice.classifyInvolvedParty(classifyInvolvedPartyRequest))
                .thenReturn(responseWithoutClassifications());
        response = callService();
        assertThat(response.getError(), is(new DAOError(ERROR_CODE, ERROR_MESSAGE)));
        verify(logger).logError(ERROR_CODE, ERROR_MESSAGE, ClassifyInvolvedPartyDAOImpl.class);
    }

    @Test
    public void shouldReturnDAOErrorWhenServiceThrowsRemoteException() throws Throwable {
        ERROR_CODE = REMOTE_EXCEPTION;
        ERROR_MESSAGE = "Remote connection exception in classify Involved party service";
        RemoteException expectedException = new RemoteException();
        when(classifyservice.classifyInvolvedParty(classifyInvolvedPartyRequest)).thenThrow(expectedException);

        DAOError expectedError = new DAOError(ERROR_CODE, ERROR_MESSAGE);
        when(daoExceptionHandler.handleException(expectedException, ClassifyInvolvedPartyDAOImpl.class, "classify",
                request)).thenReturn(expectedError);
        response = callService();
        assertThat(response.getError(), is(expectedError));

    }

    @Test
    public void shouldReturnDAOErrorWhenServiceThrowsExternalBusinessError() throws Throwable {
        ExternalBusinessError expectedException = new ExternalBusinessError(null, null, ERROR_CODE, ERROR_MESSAGE);
        when(classifyservice.classifyInvolvedParty(classifyInvolvedPartyRequest)).thenThrow(expectedException);
        DAOError expectedError = new DAOError(ERROR_CODE, ERROR_MESSAGE);
        when(daoExceptionHandler.handleException(expectedException, ClassifyInvolvedPartyDAOImpl.class, "classify",
                request)).thenReturn(expectedError);
        response = callService();
        assertThat(response.getError(), is(expectedError));

    }

    @Test
    public void shouldReturnDAOErrorWhenServiceThrowsExternalServiceError() throws Throwable {
        ExternalServiceError expectedException = new ExternalServiceError(null, null, ERROR_CODE, ERROR_MESSAGE);
        when(classifyservice.classifyInvolvedParty(classifyInvolvedPartyRequest)).thenThrow(expectedException);

        DAOError expectedError = new DAOError(ERROR_CODE, ERROR_MESSAGE);
        when(daoExceptionHandler.handleException(expectedException, ClassifyInvolvedPartyDAOImpl.class, "classify",
                request)).thenReturn(expectedError);
        response = callService();
        assertThat(response.getError(), is(expectedError));

    }

    @Test
    public void shouldReturnDAOErrorWhenServiceThrowsInternalServiceError() throws Throwable {
        InternalServiceError expectedException = new InternalServiceError(null, null, ERROR_CODE, ERROR_MESSAGE);
        when(classifyservice.classifyInvolvedParty(classifyInvolvedPartyRequest)).thenThrow(expectedException);

        DAOError expectedError = new DAOError(ERROR_CODE, ERROR_MESSAGE);
        when(daoExceptionHandler.handleException(expectedException, ClassifyInvolvedPartyDAOImpl.class, "classify",
                request)).thenReturn(expectedError);
        response = callService();
        assertThat(response.getError(), is(expectedError));

    }

    @Test
    public void shouldReturnDAOErrorWhenServiceThrowsResourceNotAvailableError() throws Throwable {
        ERROR_CODE = DAOErrorConstants.RESOURCE_NOT_AVAILABLE;
        ERROR_MESSAGE = null;
        ResourceNotAvailableError expectedException = new ResourceNotAvailableError(null, null);
        when(classifyservice.classifyInvolvedParty(classifyInvolvedPartyRequest)).thenThrow(expectedException);

        DAOError expectedError = new DAOError(ERROR_CODE, ERROR_MESSAGE);
        when(daoExceptionHandler.handleException(expectedException, ClassifyInvolvedPartyDAOImpl.class, "classify",
                request)).thenReturn(expectedError);
        response = callService();
        assertThat(response.getError(), is(expectedError));
    }

    // @Test
    public void shouldReturnDAOResponseWhenCRSAndFATCAPresentWithDifferentCountry() throws Exception {
        when(classifyservice.classifyInvolvedParty(classifyInvolvedPartyRequest))
                .thenReturn(sampleResponseWithClassifications("4", "1", "CRSCountry", "FATCACountry"));
        response = callService();
        assertThat(response.getResult().get(0).getCountryName(), is("CRSCountry"));
        assertThat(response.getResult().get(1).getCountryName(), is("FATCACountry"));
    }

    @Test
    public void shouldReturnDAOResponseWhenCRSAndFATCAPresentWithSameCountry() throws Exception {
        when(classifyservice.classifyInvolvedParty(classifyInvolvedPartyRequest))
                .thenReturn(sampleResponseWithClassifications("4", "1", "CRSCountry", "CRSCountry"));
        response = callService();
        assertThat(response.getResult().get(0).getCountryName(), is("CRSCountry"));
        assertThat(response.getResult().size(), is(1));
    }

    @Test
    public void shouldReturnDAOResponseWhenCRSNotPresent() throws Exception {
        when(classifyservice.classifyInvolvedParty(classifyInvolvedPartyRequest))
                .thenReturn(sampleResponseWithClassifications(null, "1", null, "FATCACountry"));
        response = callService();
        assertThat(response.getResult().get(0).getCountryName(), is("FATCACountry"));
    }

    @Test
    public void shouldReturnDAOResponseWhenFATCANotPresent() throws Exception {
        when(classifyservice.classifyInvolvedParty(classifyInvolvedPartyRequest))
                .thenReturn(sampleResponseWithClassifications("4", null, "CRSCountry", null));
        response = callService();
        assertThat(response.getResult().get(0).getCountryName(), is("CRSCountry"));
    }

    @Test
    public void shouldReturnNullWhenCRSAndFATCANotPresent() throws Exception {
        when(classifyservice.classifyInvolvedParty(classifyInvolvedPartyRequest))
                .thenReturn(sampleResponseWithClassifications(null, null, null, null));
        response = callService();
        assertTrue(response.getResult().size() == 0);
    }

    private DAOResponse<List<ClassifyPartyResponseDTO>> callService() throws ServiceException {
        return classifyInvolvedPartyDAOImpl.classify(request);
    }

    private ClassifyInvolvedPartyResponse sampleResponse() {

        AssociatedCategories ass = new AssociatedCategories();
        ass.setCountryName("ALA");
        ass.setCategoryName("TIN Optional");
        ass.setCategoryType("042");
        AssociatedCategories[] associatedCategories = new AssociatedCategories[1];
        associatedCategories[0] = ass;
        ExceptionDetails exceptionDetails = new ExceptionDetails();
        exceptionDetails.setAssociatedCategories(associatedCategories);
        Classifications classifications = new Classifications();
        classifications.setExceptionDetails(exceptionDetails);
        classifications.setSchemeName(CRS_VALUE);
        Classifications[] classificationsArray = new Classifications[1];
        classificationsArray[0] = classifications;
        ClassifyInvolvedPartyResponse response = new ClassifyInvolvedPartyResponse();
        response.setClassifications(classificationsArray);

        return response;
    }

    private ClassifyInvolvedPartyResponse sampleResponseWithClassifications(String crsValue, String fatcaValue,
            String crsCountry, String fatcaCountry) {
        Classifications classifications = new Classifications();
        Classifications classifications2 = new Classifications();
        AssociatedCategories ass = new AssociatedCategories();
        AssociatedCategories ass2 = new AssociatedCategories();

        if (fatcaValue != null) {
            ass2.setCountryName(fatcaCountry);
            ass2.setCategoryName("TIN Optional");
            ass2.setCategoryType("042");
            AssociatedCategories[] associatedCategories = new AssociatedCategories[1];
            associatedCategories[0] = ass2;
            ExceptionDetails exceptionDetails = new ExceptionDetails();
            exceptionDetails.setAssociatedCategories(associatedCategories);
            classifications2.setExceptionDetails(exceptionDetails);
            classifications2.setSchemeName(fatcaValue);
        }
        if (crsValue != null) {
            ass.setCountryName(crsCountry);

            ass.setCountryName(crsCountry);
            ass.setCategoryName("TIN Optional");
            ass.setCategoryType("042");
            AssociatedCategories[] associatedCategories = new AssociatedCategories[1];
            associatedCategories[0] = ass;
            ExceptionDetails exceptionDetails = new ExceptionDetails();
            exceptionDetails.setAssociatedCategories(associatedCategories);
            classifications.setExceptionDetails(exceptionDetails);
            classifications.setSchemeName(crsValue);
        }
        Classifications[] classificationsArray = new Classifications[2];
        classificationsArray[0] = classifications;
        classificationsArray[1] = classifications2;
        ClassifyInvolvedPartyResponse response = new ClassifyInvolvedPartyResponse();
        response.setClassifications(classificationsArray);

        return response;
    }

    private List<ClassifyPartyResponseDTO> expectedDto() {
        List<ClassifyPartyResponseDTO> responseDTOs = new ArrayList<ClassifyPartyResponseDTO>();
        ClassifyPartyResponseDTO responseDTO = new ClassifyPartyResponseDTO();
        responseDTO.setCountryName("ALA");
        responseDTO.setTinRequired(false);
        responseDTO.setRegex(null);
        responseDTO.setTaxResidencyType(findTaxResidencyTypeFromCode(CRS_VALUE).toString());
        responseDTOs.add(responseDTO);
        return responseDTOs;
    }

    private ClassifyPartyRequestDTO sampleRequest() {
        ClassifyPartyRequestDTO request = new ClassifyPartyRequestDTO();
        request.setBirthCountry("GBR");
        nationalities.add("IND");
        request.setNationalities(nationalities);
        TaxResidencyDetails taxResidencyDetails = new TaxResidencyDetails();
        taxResidencyDetails.setTaxResidency("ALA");
        taxResidencies.add(taxResidencyDetails);
        request.setTaxResidencies(taxResidencies);
        return request;
    }

    private ClassifyInvolvedPartyResponse responseWithoutClassifications() {
        Classifications[] classificationsArray = null;
        ClassifyInvolvedPartyResponse response = new ClassifyInvolvedPartyResponse();
        response.setClassifications(classificationsArray);

        return response;
    }

}
