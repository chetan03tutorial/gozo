/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package integration;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import LB_GBO_Sales.IA_CommonReportingStandards;
import LB_GBO_Sales.IA_CommonReportingStandardsHttpServiceLocator;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.sales.dao.SalsaGBOHeaderUtility;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.party.classify.ClassifyInvolvedPartyDAOImpl;
import com.lbg.ib.api.sales.dao.party.classify.ClassifyInvolvedPartyRequestBuilder;
import com.lbg.ib.api.sales.dto.party.ClassifyPartyRequestDTO;
import com.lbg.ib.api.sales.dto.party.ClassifyPartyResponseDTO;
import com.lbg.ib.api.shared.domain.TaxResidencyDetails;
import com.lloydstsb.ib.salsa.crs.messages.ClassifyInvolvedPartyRequest;
import com.lloydstsb.ib.salsa.crs.messages.ClassifyInvolvedPartyResponse;

@RunWith(MockitoJUnitRunner.class)
public class ClassifyInvolvedPartyDAOImplIT extends SalsaRequestHeaderForIT {

    @InjectMocks
    private ClassifyInvolvedPartyDAOImpl              classifyPartyDAOImpl;

    @Mock
    private LoggerDAO                                 logger;

    @Mock
    private ApiServiceProperties                      properties;

    @Mock
    private IA_CommonReportingStandards               service;

    @Mock
    private ClassifyInvolvedPartyRequest              request;

    @Mock
    ClassifyInvolvedPartyResponse                     response;

    @Mock
    private DAOError                                  error;

    @Mock
    private SalsaGBOHeaderUtility                     headerUtility;

    @Mock
    private UserContext                               context;

    @Mock
    private SessionManagementDAO                      session;

    private static LinkedHashSet<String>              nationalities  = new LinkedHashSet<String>();

    private static LinkedHashSet<TaxResidencyDetails> taxResidencies = new LinkedHashSet<TaxResidencyDetails>();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

    }

    @Ignore
    @Test
    public void shouldGiveClassifiedPartyDetailsResponse() throws Exception {
        ClassifyInvolvedPartyRequestBuilder requestBuilder = new ClassifyInvolvedPartyRequestBuilder();
        classifyPartyDAOImpl.setReqBuilder(requestBuilder);
        IA_CommonReportingStandardsHttpServiceLocator locator = new IA_CommonReportingStandardsHttpServiceLocator();
        service = locator.getIA_CommonReportingStandardsHttpPort(
                new URL("http://10.245.211.251:22904/salsa/servicing/crs/v1_0/ClassifyInvolvedPartyService/service"));
        classifyPartyDAOImpl.setClassifyservice(service);
        Whitebox.setInternalState(requestBuilder, "session", session);
        requestBuilder.setGboHeaderUtility(headerUtility);
        when(headerUtility.prepareSoapHeader("classifyInvolvedParty", "CharacterEvaluationService"))
                .thenReturn(populateRequestHeader());
        DAOResponse<List<ClassifyPartyResponseDTO>> response = classifyPartyDAOImpl.classify(classifyPartyRequestDTO());
        assertEquals(response.getResult(), classifiedPartyResponse());
    }

    private static ClassifyPartyRequestDTO classifyPartyRequestDTO() {
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

    private static List<ClassifyPartyResponseDTO> classifiedPartyResponse() {
        List<ClassifyPartyResponseDTO> responseDTOs = new ArrayList<ClassifyPartyResponseDTO>();
        ClassifyPartyResponseDTO responseDTO = new ClassifyPartyResponseDTO();
        responseDTO.setCountryName("ALA");
        responseDTO.setTinRequired(false);
        responseDTO.setRegex(null);
        responseDTOs.add(responseDTO);
        return responseDTOs;
    }
}
