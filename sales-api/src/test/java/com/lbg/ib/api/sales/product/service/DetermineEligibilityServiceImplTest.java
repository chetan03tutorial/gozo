/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.service;

import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.Assert;

import com.lbg.ib.api.sso.domain.address.PostalAddress;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.validation.AccountType;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.product.eligibility.EligibiltyDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.product.eligibility.EligibilityRequestDTO;
import com.lbg.ib.api.shared.domain.TinDetails;
import com.lbg.ib.api.sales.product.domain.ArrangeToActivateParameters;
import com.lbg.ib.api.sso.domain.product.arrangement.ContactNumber;
import com.lbg.ib.api.sso.domain.product.arrangement.EmployerDetails;
import com.lbg.ib.api.sso.domain.product.arrangement.PostalAddressComponent;
import com.lbg.ib.api.sales.product.domain.arrangement.PrimaryInvolvedParty;
import com.lbg.ib.api.sales.product.domain.eligibility.DetermineEligibilityRequest;
import com.lbg.ib.api.sales.product.domain.eligibility.DetermineEligibilityResponse;
import com.lbg.ib.api.sales.product.domain.eligibility.EligibilityDetails;

@RunWith(MockitoJUnitRunner.class)
public class DetermineEligibilityServiceImplTest {

    @InjectMocks
    private DetermineEligibilityServiceImpl service;

    @Mock
    private EligibiltyDAO                   dao;

    @Mock
    private GalaxyErrorCodeResolver         resolver;

    @Mock
    private LoggerDAO                       logger;

    @Mock
    private SessionManagementDAO            session;

    @Mock
    private ConfigurationDAO                configurationService;

    @Before
    public void setUp() {
    }

    @Test
    public void shouldReturnServiceResponseWhenDaoReturnsWithResult() throws Exception {

        DetermineEligibilityRequest request = new DetermineEligibilityRequest();
        request.setArrangementType(AccountType.CA);
        List<String> mnemonicList = new ArrayList<String>();
        mnemonicList.add("P_REWARD");
        request.setMnemonic(mnemonicList);
        // request.setDob(new GregorianCalendar().getTime());

        PrimaryInvolvedParty primaryInvolvedParty = setPrimaryInvolvedParty();
        request.setPrimaryInvolvedParty(primaryInvolvedParty);
        HashMap<String, EligibilityDetails> productMap = getExpectedResult();
        EligibilityRequestDTO requestDTO = new EligibilityRequestDTO("CA", primaryInvolvedParty,
                new String[] { "P_REWARD" }, null);

        when(dao.determineEligibility(requestDTO)).thenReturn(withResult(productMap));
        DetermineEligibilityResponse response = service.determineEligiblity(request);
        Assert.notNull(response.getMsg());
        Assert.isTrue(response.getMsg().equals("Successfully fetched the eligibility details."));
        Assert.isTrue(response.getEligibilityDetails().get(0).getMnemonic().equals("P_REWARD"));
        Assert.isTrue(response.getEligibilityDetails().get(0).getIsEligible().equals(true));
    }

    @Test
    public void shouldReturnServiceResponseWhenDaoReturnsWithResultWithVantage() throws Exception {

        DetermineEligibilityRequest request = new DetermineEligibilityRequest();
        request.setArrangementType(AccountType.CA);
        List<String> mnemonicList = new ArrayList<String>();
        mnemonicList.add("P_REWARD");
        mnemonicList.add("VANTAGE");
        request.setMnemonic(mnemonicList);
        // request.setDob(new GregorianCalendar().getTime());

        PrimaryInvolvedParty primaryInvolvedParty = setPrimaryInvolvedParty();
        request.setPrimaryInvolvedParty(primaryInvolvedParty);
        HashMap<String, EligibilityDetails> productMap = getExpectedVantageResult();
        EligibilityRequestDTO requestDTO = new EligibilityRequestDTO("CA", primaryInvolvedParty,
                new String[] { "P_REWARD" }, null);

        ArrangeToActivateParameters arrangeToActivateParameters = new ArrangeToActivateParameters();
        arrangeToActivateParameters.setAlternateVantageMnemonic("VANTAGE");
        when(session.getArrangeToActivateParameters()).thenReturn(arrangeToActivateParameters);
        when(dao.determineEligibility(any(EligibilityRequestDTO.class))).thenReturn(withResult(productMap));

        service.setSession(session);
        DetermineEligibilityResponse response = service.determineEligiblity(request);
        Assert.notNull(response.getMsg());
        Assert.isTrue(response.getMsg().equals("Successfully fetched the eligibility details."));
        Assert.isTrue(response.getEligibilityDetails().get(0).getMnemonic().equals("P_REWARD"));
        Assert.isTrue(response.getEligibilityDetails().get(0).getIsEligible().equals(true));
    }

    private PrimaryInvolvedParty setPrimaryInvolvedParty() {
        PrimaryInvolvedParty pip = new PrimaryInvolvedParty();
        pip.setDob(new GregorianCalendar().getTime());
        PostalAddressComponent component = new PostalAddressComponent();
        component.setDurationOfStay("129");
        component.setIsBFPOAddress(false);
        component.setIsPAFFormat(true);
        PostalAddress postalAddress = new PostalAddress();
        postalAddress.setBuildingNumber("45");
        postalAddress.setBuildingName("East Village");
        List<String> addresList = new ArrayList<String>();

        addresList.add("22 Egremont House");
        postalAddress.setAddressLines(addresList);

        component.setStructuredAddress(postalAddress);
        pip.setCurrentAddress(component);
        pip.setEmail("test@gmail.com");
        EmployerDetails details = new EmployerDetails("test org", "line 1", "line 2", "E201BF", 3, 2);
        pip.setEmployer(details);
        pip.setEmploymentStatus("employmentStatus");
        pip.setExptdMntlyDepAmt(new Double(23.23));
        pip.setFirstName("Test");
        pip.setFundSource("fundSource");
        pip.setGender("003");
        ContactNumber number = new ContactNumber("44", null, "1323343434", null);
        pip.setHomePhone(number);
        pip.setLastName("User");
        pip.setMaintnCost(324);
        pip.setMaritalStatus("married");
        pip.setMobileNumber(number);
        pip.setNumberOfDependents(2);
        pip.setOccupnType("002");
        pip.setPurpose("purpose");
        pip.setSavingsAmount(320);
        TinDetails tDetails = new TinDetails();
        tDetails.setBirthCountry("GBR");
        LinkedHashSet<String> set = new LinkedHashSet<String>();
        set.add("GBR");
        tDetails.setNationalities(set);
        pip.setTinDetails(tDetails);
        return pip;
    }

    private HashMap<String, EligibilityDetails> getExpectedResult() {
        HashMap<String, EligibilityDetails> productMap = new HashMap<String, EligibilityDetails>();
        EligibilityDetails details = new EligibilityDetails();
        details.setMnemonic("P_REWARD");
        details.setIsEligible(true);
        details.setIsVantageEligible(null);
        productMap.put("P_REWARD", details);

        return productMap;
    }

    private HashMap<String, EligibilityDetails> getExpectedVantageResult() {

        HashMap<String, EligibilityDetails> productMap = new HashMap<String, EligibilityDetails>();
        EligibilityDetails details = new EligibilityDetails();
        details.setMnemonic("P_REWARD");
        details.setIsEligible(true);
        details.setIsVantageEligible(null);
        productMap.put("P_REWARD", details);

        EligibilityDetails vantageDetails = new EligibilityDetails();
        vantageDetails.setMnemonic("VANTAGE");
        vantageDetails.setIsEligible(true);
        vantageDetails.setIsVantageEligible(null);
        productMap.put("VANTAGE", vantageDetails);

        return productMap;
    }

    @Test
    public void shouldThrowServiceExceptionWhenDaoReturnsWithError() throws Exception {

        DetermineEligibilityRequest request = new DetermineEligibilityRequest();
        request.setArrangementType(AccountType.CA);
        PrimaryInvolvedParty primaryInvolvedParty = new PrimaryInvolvedParty();
        primaryInvolvedParty.setDob(new GregorianCalendar().getTime());
        List<String> mnemonicList = new ArrayList<String>();
        mnemonicList.add("P_REWARD");
        request.setMnemonic(mnemonicList);
        EligibilityRequestDTO requestDTO = new EligibilityRequestDTO("CA", primaryInvolvedParty,
                new String[] { "P_REWARD" }, null);
        // request.setDob(new GregorianCalendar().getTime());
        when(dao.determineEligibleCustomerInstructions(requestDTO))
                .thenReturn(DAOResponse.<HashMap<String, String>> withError(new DAOError("1100017", "message")));
        when(configurationService.getConfigurationStringValue("1100017", "IB_ERROR_CODE")).thenReturn("1100017");

        DetermineEligibilityResponse response = service.determineEligiblity(request);
        Assert.isNull(response);
    }

}