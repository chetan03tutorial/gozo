package com.lbg.ib.api.sales.party.service;

import com.google.common.collect.Lists;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.common.rest.client.RestContext;
import com.lbg.ib.api.sales.common.rest.util.OcisRestHeaderUtil;
import com.lbg.ib.api.sales.dao.restclient.ExternalRestApiClientDAO;
import com.lbg.ib.api.sales.docupload.util.SalsaEndpoints;
import com.lbg.ib.api.sales.docupload.util.UriResolver;
import com.lbg.ib.api.sales.party.domain.response.ElectronicAddress;
import com.lbg.ib.api.sales.party.domain.response.PartyDetail;
import com.lbg.ib.api.sales.party.domain.response.PostalAddressDetails;
import com.lbg.ib.api.sales.party.domain.response.RetrievePartyDetailsResponse;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RetrievePartyDetailsServiceImplTest {

	@InjectMocks
	private RetrievePartyDetailsService retrievePartyDetailsService = new RetrievePartyDetailsServiceImpl();
	
	@Mock
    private LoggerDAO logger;

	@Mock
	private ExternalRestApiClientDAO externalRestApiClientDAO;

	@Mock
    private UriResolver ocisUriResolver;

	@Mock
    private OcisRestHeaderUtil ocisRestHeaderUtil;
	
	@Mock
    private GalaxyErrorCodeResolver resolver;
	
	@Test
	public void test_retrievePartyDetails_success() {
		String partyId = "123456";
		when(ocisUriResolver.getEndpoint(any(SalsaEndpoints.class))).thenReturn("endpoint");
		Map<String, Object> headers = new HashMap<String, Object>();
		when(ocisRestHeaderUtil.getSalsaHeader()).thenReturn(headers);
		RetrievePartyDetailsResponse response = mock(RetrievePartyDetailsResponse.class);
		when(externalRestApiClientDAO.customGet(any(RestContext.class), any(Class.class))).thenReturn(response);
		RetrievePartyDetailsResponse actualResponse = retrievePartyDetailsService.retrievePartyDetails(partyId);
		
		assertEquals(response, actualResponse);
	}

	@Test
	public void test_PartyDetails_success_with_two_email() {
		String partyId = "123456";
		when(ocisUriResolver.getEndpoint(any(SalsaEndpoints.class))).thenReturn("endpoint");
		Map<String, Object> headers = new HashMap<String, Object>();
		when(ocisRestHeaderUtil.getSalsaHeader()).thenReturn(headers);
		RetrievePartyDetailsResponse response = new RetrievePartyDetailsResponse();
		PartyDetail party = new PartyDetail();
		party.setFirstName("First");
		party.setLastName("last");
		party.setTitle("Mr");
		party.setBirthDate("02/09/1984");
		List<PostalAddressDetails> addresses = Lists.newArrayList();
		PostalAddressDetails postalAddressDetails = new PostalAddressDetails();
		postalAddressDetails.setContactPointStatus("ACTIVE");
		postalAddressDetails.setPostalAddressType("RESIDENTIAL");
		postalAddressDetails.setPostCode("HA9 0EY");
		postalAddressDetails.setStartDate(null);
		postalAddressDetails.setEndDate(null);
		postalAddressDetails.setAddressLines(Lists.newArrayList("address1","address2", "address3", "address4"));
		addresses.add(postalAddressDetails);
		List<ElectronicAddress> electronicAddresses = Lists.newArrayList();
		ElectronicAddress electronicAddress_1 = new ElectronicAddress();
		ElectronicAddress electronicAddress_2 = new ElectronicAddress();
		electronicAddress_1.setAddress("first@abc.com");
		electronicAddress_1.setContactPointStatus("ACTIVE");
		electronicAddress_1.setElectronicAddressType("EMAIL");
		electronicAddress_1.setElectronicPartyAddressType("PERSONAL");
		electronicAddress_2.setAddress("second@abc.com");
		electronicAddress_2.setContactPointStatus("ACTIVE");
		electronicAddress_2.setElectronicAddressType("EMAIL");
		electronicAddress_2.setElectronicPartyAddressType("BUSINESS");
		electronicAddresses.add(electronicAddress_1);
		electronicAddresses.add(electronicAddress_2);
		response.setParty(party);
		response.setPostalAddress(addresses);
		response.setElectronicAddress(electronicAddresses);
		when(externalRestApiClientDAO.customGet(any(RestContext.class), any(Class.class))).thenReturn(response);
		PartyDetails actualResponse = retrievePartyDetailsService.getPartyDetails(partyId);

		assertEquals("first@abc.com", actualResponse.getEmail());
		assertEquals("HA9 0EY", actualResponse.getPostalCode());
		assertEquals("First", actualResponse.getFirstName());
		assertEquals("last", actualResponse.getSurname());
		assertEquals("02/09/1984", actualResponse.getDob());
	}

	@Test
	public void test_PartyDetails_success_with_one_email() {
		String partyId = "123456";
		when(ocisUriResolver.getEndpoint(any(SalsaEndpoints.class))).thenReturn("endpoint");
		Map<String, Object> headers = new HashMap<String, Object>();
		when(ocisRestHeaderUtil.getSalsaHeader()).thenReturn(headers);
		RetrievePartyDetailsResponse response = new RetrievePartyDetailsResponse();
		PartyDetail party = new PartyDetail();
		party.setFirstName("First");
		party.setLastName("last");
		party.setTitle("Mr");
		party.setBirthDate("02/09/1984");
		List<PostalAddressDetails> addresses = Lists.newArrayList();
		PostalAddressDetails postalAddressDetails = new PostalAddressDetails();
		postalAddressDetails.setContactPointStatus("ACTIVE");
		postalAddressDetails.setPostalAddressType("RESIDENTIAL");
		postalAddressDetails.setPostCode("HA9 0EY");
		postalAddressDetails.setStartDate(null);
		postalAddressDetails.setEndDate(null);
		postalAddressDetails.setAddressLines(Lists.newArrayList("address1","address2", "address3", "address4"));
		addresses.add(postalAddressDetails);
		List<ElectronicAddress> electronicAddresses = Lists.newArrayList();
		ElectronicAddress electronicAddress_1 = new ElectronicAddress();
		electronicAddress_1.setAddress("second@abc.com");
		electronicAddress_1.setContactPointStatus("ACTIVE");
		electronicAddress_1.setElectronicAddressType("EMAIL");
		electronicAddress_1.setElectronicPartyAddressType("BUSINESS");
		electronicAddresses.add(electronicAddress_1);
		response.setParty(party);
		response.setPostalAddress(addresses);
		response.setElectronicAddress(electronicAddresses);
		when(externalRestApiClientDAO.customGet(any(RestContext.class), any(Class.class))).thenReturn(response);
		PartyDetails actualResponse = retrievePartyDetailsService.getPartyDetails(partyId);

		assertEquals("second@abc.com", actualResponse.getEmail());
		assertEquals("HA9 0EY", actualResponse.getPostalCode());
		assertEquals("First", actualResponse.getFirstName());
		assertEquals("last", actualResponse.getSurname());
		assertEquals("02/09/1984", actualResponse.getDob());
	}
	
	@Test
	public void test_retrievePartyDetails_null_response() {
		String partyId = "123456";
		when(ocisUriResolver.getEndpoint(any(SalsaEndpoints.class))).thenReturn("endpoint");
		Map<String, Object> headers = new HashMap<String, Object>();
		when(ocisRestHeaderUtil.getSalsaHeader()).thenReturn(headers);
		when(externalRestApiClientDAO.customGet(any(RestContext.class), any(Class.class))).thenReturn(null);
		RetrievePartyDetailsResponse actualResponse = retrievePartyDetailsService.retrievePartyDetails(partyId);

		assertNull(actualResponse);

	}
	
	
	@Test
    public void test_getPartyDetailsServiceException() {
        String otherpartyOcisId = "123456";
        try {
            when(resolver.resolve(ResponseErrorConstants.FAILED_TO_GET_OTHER_PARTY_DETAIL)).thenReturn(new ResponseError());
            when(ocisUriResolver.getEndpoint(any(SalsaEndpoints.class))).thenReturn("endpoint");
            retrievePartyDetailsService.getPartyDetails(otherpartyOcisId);
        }
        catch (ServiceException x) {
            assertNotNull(x);
        }
       
    }

}
