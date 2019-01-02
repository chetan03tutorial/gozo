package com.lbg.ib.api.sales.party.resources;

import com.lbg.ib.api.sales.party.domain.response.RetrievePartyDetailsResponse;
import com.lbg.ib.api.sales.party.service.RetrievePartyDetailsService;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RetrievePartyDetailsResourceTest {
	
	@InjectMocks
	private RetrievePartyDetailsResource retrievePartyDetailsResource = new RetrievePartyDetailsResource();
	
	@Mock
	private RetrievePartyDetailsService retrievePartyDetailsService;
	
	@Mock
	private LoggerDAO logger;
	
	@Test
	public void testRetrievePartyDetails() throws InvalidFormatException {
		String partyId = "1234";
		RetrievePartyDetailsResponse response = mock(RetrievePartyDetailsResponse.class);
		when(retrievePartyDetailsService.retrievePartyDetails(partyId)).thenReturn(response);
		Response actualResponse = retrievePartyDetailsResource.retrievePartyDetails(partyId);
		
		assertEquals(response, actualResponse.getEntity());
	}
	
	@Test
	public void testRetrievePartyDetails_null_reponse() throws InvalidFormatException {
		String partyId = "1234";
		RetrievePartyDetailsResponse response = null;
		when(retrievePartyDetailsService.retrievePartyDetails(partyId)).thenReturn(response);
		Response actualResponse = retrievePartyDetailsResource.retrievePartyDetails(partyId);

		assertNull(actualResponse.getEntity());
	}
	
	@Test(expected=InvalidFormatException.class)
	public void testRetrievePartyDetails_invalid_party_id() throws InvalidFormatException {
		String partyId = "";
		RetrievePartyDetailsResponse response = null;
		Response actualResponse = retrievePartyDetailsResource.retrievePartyDetails(partyId);
	}

}
