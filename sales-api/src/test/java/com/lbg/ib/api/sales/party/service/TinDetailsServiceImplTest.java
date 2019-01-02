/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.party.service;

import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.party.classify.ClassifyInvolvedPartyDAO;
import com.lbg.ib.api.sales.dto.party.ClassifyPartyRequestDTO;
import com.lbg.ib.api.sales.dto.party.ClassifyPartyResponseDTO;
import com.lbg.ib.api.sales.party.domain.ClassifiedPartyDetails;
import com.lbg.ib.api.shared.domain.TaxResidencyDetails;
import com.lbg.ib.api.shared.domain.TinDetails;

@RunWith(MockitoJUnitRunner.class)
public class TinDetailsServiceImplTest {

    @InjectMocks
    private TinDetailsServiceImpl                       service;

    @Mock
    private GalaxyErrorCodeResolver                     resolver;

    @Mock
    private LoggerDAO                                   logger;

    @Mock
    private ClassifyInvolvedPartyDAO                    classifyInvolvedPartyDAO;

    private static final List<ClassifyPartyResponseDTO> RESPONSEDTO                    = new ArrayList<ClassifyPartyResponseDTO>();

    private static final ResponseError                  RESPONSE_ERROR                 = new ResponseError(
            ResponseErrorConstants.SERVICE_UNAVAILABLE, "Service Unavailable");

    private LinkedHashSet<String>                       nationalities                  = new LinkedHashSet<String>();

    private LinkedHashSet<TaxResidencyDetails>          taxResidencies                 = new LinkedHashSet<TaxResidencyDetails>();

    private TinDetails                                  tinDetails                     = new TinDetails();

    private List<ClassifiedPartyDetails>                classifiedPartyDetailsActual   = new ArrayList<ClassifiedPartyDetails>();

    private List<ClassifiedPartyDetails>                classifiedPartyDetailsExpected = new ArrayList<ClassifiedPartyDetails>();

    @Test
    public void shouldReturnTINDetailsWhenServiceReturnsResponse() throws Exception {

        ClassifyPartyResponseDTO response = new ClassifyPartyResponseDTO();
        response.setCountryName("ALA");
        response.setTinRequired(false);
        response.setRegex("TIN Optional");
        RESPONSEDTO.add(response);
        when(classifyInvolvedPartyDAO.classify(any(ClassifyPartyRequestDTO.class))).thenReturn(withResult(RESPONSEDTO));

        tinDetails.setBirthCountry("GBR");
        nationalities.add("IND");
        tinDetails.setNationalities(nationalities);
        TaxResidencyDetails taxResidencyDetails = new TaxResidencyDetails();
        taxResidencyDetails.setTaxResidency("ALA");
        taxResidencies.add(taxResidencyDetails);
        tinDetails.setTaxResidencies(taxResidencies);

        classifiedPartyDetailsActual = service.identify(tinDetails);

        ClassifiedPartyDetails classifiedPartyDetails = new ClassifiedPartyDetails();
        classifiedPartyDetails.setCountryName("ALA");
        classifiedPartyDetails.setTinRequired(false);
        classifiedPartyDetails.setRegex("TIN Optional");
        classifiedPartyDetailsExpected.add(classifiedPartyDetails);

        assertThat(classifiedPartyDetailsActual, is(classifiedPartyDetailsExpected));
    }

    @Test
    public void shouldThrowServiceExceptionWhenDAOReturnsNull() throws Exception {
        when(classifyInvolvedPartyDAO.classify(any(ClassifyPartyRequestDTO.class))).thenReturn(null);
        try {
            service.identify(tinDetails);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponseError(),
                    is(resolver.resolve(ResponseErrorConstants.CLASSIFY_INVOLVED_PARTY_NOT_FOUND)));
        }
    }

    @Test
    public void shouldResolveDaoErrorAndThrowServiceExceptionWhenDAOReturnsWithAnErrorResult() throws Exception {
        when(resolver.resolve("code")).thenReturn(RESPONSE_ERROR);
        when(classifyInvolvedPartyDAO.classify(any(ClassifyPartyRequestDTO.class))).thenReturn(
                DAOResponse.<List<ClassifyPartyResponseDTO>> withError(new DAOResponse.DAOError("code", "message")));

        tinDetails.setBirthCountry("GBR");
        nationalities.add("IND");
        tinDetails.setNationalities(nationalities);
        TaxResidencyDetails taxResidencyDetails = new TaxResidencyDetails();
        taxResidencyDetails.setTaxResidency("ALA");
        taxResidencies.add(taxResidencyDetails);
        tinDetails.setTaxResidencies(taxResidencies);

        try {
            service.identify(tinDetails);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponseError(), is(RESPONSE_ERROR));
        }
    }


    @Test
    public void shouldResolveDaoErrorAndThrowServiceExceptionWhenDAOReturnsWithAnErrorResultForFrance() throws Exception {
        when(resolver.resolve("code")).thenReturn(RESPONSE_ERROR);
        when(classifyInvolvedPartyDAO.classify(any(ClassifyPartyRequestDTO.class))).thenReturn(
                DAOResponse.<List<ClassifyPartyResponseDTO>> withError(new DAOResponse.DAOError("code", "message")));

        tinDetails.setBirthCountry("GBR");
        nationalities.add("IND");
        tinDetails.setNationalities(nationalities);
        TaxResidencyDetails taxResidencyDetails = new TaxResidencyDetails();
        taxResidencyDetails.setTaxResidency("FRA");
        taxResidencies.add(taxResidencyDetails);
        tinDetails.setTaxResidencies(taxResidencies);

        classifiedPartyDetailsActual = service.identify(tinDetails);

        assertTrue(classifiedPartyDetailsActual.get(0)!=null);

    }


    @Test
    public void shouldResolveDaoReturnsEmptyResultWhenDAOReturnsWithAnErrorResultForFrance() throws Exception {
        List<ClassifyPartyResponseDTO> emptyResponse  = new ArrayList<ClassifyPartyResponseDTO>();
        when(classifyInvolvedPartyDAO.classify(any(ClassifyPartyRequestDTO.class))).thenReturn(withResult(emptyResponse));

        tinDetails.setBirthCountry("GBR");
        nationalities.add("IND");
        tinDetails.setNationalities(nationalities);
        TaxResidencyDetails taxResidencyDetails = new TaxResidencyDetails();
        taxResidencyDetails.setTaxResidency("FRA");
        taxResidencies.add(taxResidencyDetails);
        tinDetails.setTaxResidencies(taxResidencies);

        classifiedPartyDetailsActual = service.identify(tinDetails);

        assertTrue(classifiedPartyDetailsActual.get(0)!=null);

    }
}
