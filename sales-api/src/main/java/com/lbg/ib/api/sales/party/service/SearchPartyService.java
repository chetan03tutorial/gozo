package com.lbg.ib.api.sales.party.service;

import com.lbg.ib.api.sales.party.domain.IBParties;
import com.lbg.ib.api.sales.party.domain.SearchPartyRequest;

import java.util.List;

public interface SearchPartyService {
    List<IBParties> retrieveParty(SearchPartyRequest partyDetails);
}
