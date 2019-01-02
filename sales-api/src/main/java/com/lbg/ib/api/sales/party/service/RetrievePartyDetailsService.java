package com.lbg.ib.api.sales.party.service;

import com.lbg.ib.api.sales.party.domain.response.RetrievePartyDetailsResponse;
import com.lbg.ib.api.sales.user.domain.PartyDetails;

/**
 * Service Interface for the RetrievePartyDetails API
 * @author 8903735
 *
 */
public interface RetrievePartyDetailsService {
	
	/**
	 * Returns the party details retrieved from OCIS
	 * @param ocisId
	 * @return {@link RetrievePartyDetailsResponse}
	 */
	public RetrievePartyDetailsResponse retrievePartyDetails(String ocisId);
	public PartyDetails getPartyDetails(String ocisId);
}
