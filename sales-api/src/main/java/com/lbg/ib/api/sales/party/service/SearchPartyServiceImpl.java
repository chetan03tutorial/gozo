package com.lbg.ib.api.sales.party.service;

import com.lbg.ib.api.sales.common.rest.client.RestContext;
import com.lbg.ib.api.sales.common.rest.util.OcisRestHeaderUtil;
import com.lbg.ib.api.sales.dao.restclient.ExternalRestApiClientDAO;
import com.lbg.ib.api.sales.docupload.util.SalsaEndpoints;
import com.lbg.ib.api.sales.docupload.util.UriResolver;
import com.lbg.ib.api.sales.party.constants.PartyType;
import com.lbg.ib.api.sales.party.domain.Address;
import com.lbg.ib.api.sales.party.domain.IBParties;
import com.lbg.ib.api.sales.party.domain.SearchPartyRequest;
import com.lbg.ib.api.sales.party.dto.SearchPartiesSalsaResponse;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class SearchPartyServiceImpl implements SearchPartyService {
    
    @Autowired
    private ExternalRestApiClientDAO  externalRestApiClientDAO;
    
    @Autowired
    private UriResolver               uriResolver;
    
    @Autowired
    private OcisRestHeaderUtil        ocisHeaderUtility;
    
    @TraceLog
    public List<IBParties> retrieveParty(SearchPartyRequest partyDetails) {
        
        Map<String, String> queryParameters = new HashMap<String, String>();
        queryParameters.put("agreementIdentifier", partyDetails.getAgreementIdentifier());
        queryParameters.put("agreementType", partyDetails.getAgreementType());
        queryParameters.put("agreementRole", partyDetails.getPartyRole());
        
        String endpoint = uriResolver.getEndpoint(SalsaEndpoints.Q226_ENDPOINT);
        SearchPartiesSalsaResponse[] response = externalRestApiClientDAO.customGet(buildSearchRequest(queryParameters, endpoint), SearchPartiesSalsaResponse[].class);
        if (null != response) {
            return buildSearchResponse(response);
        }
        return new LinkedList<IBParties>();
    }
    
    private List<IBParties> buildSearchResponse(SearchPartiesSalsaResponse[] adapaResponse) {
        
        LinkedList<IBParties> parties = new LinkedList<IBParties>();
        IBParties party;
        for (int index = 0; index < adapaResponse.length; index++) {
            if (null != adapaResponse[index].getParty()
                    && PartyType.INDIVIDUAL.name().equalsIgnoreCase(adapaResponse[index].getParty().getPartyType())) {
                party = new IBParties();
                parties.add(party);
                party.setAgreementRole(adapaResponse[index].getAgreementRole());
                party.setBirthDate(adapaResponse[index].getParty().getBirthDate());
                party.setDeathNotifiedDate(adapaResponse[index].getParty().getDeathNotifiedDate());
                party.setFirstName(adapaResponse[index].getParty().getFirstName());
                party.setGender(adapaResponse[index].getParty().getGender());
                party.setGenerationSuffix(adapaResponse[index].getParty().getGenerationSuffix());
                party.setInitials(adapaResponse[index].getParty().getInitials());
                party.setLastName(adapaResponse[index].getParty().getLastName());
                party.setPartyId(adapaResponse[index].getParty().getPartyId());
                party.setPartyType(adapaResponse[index].getParty().getPartyType());
                Address[] addresses = adapaResponse[index].getPostalAddress();
                if (addresses != null) {
                    party.setPostalAddress(adapaResponse[index].getPostalAddress()[0]);
                }
                party.setSecondName(adapaResponse[index].getParty().getSecondName());
                party.setTitle(adapaResponse[index].getParty().getTitle());
            }
        }
        return parties;
    }
    
    private RestContext buildSearchRequest(Map<String, String> queryParameters, String endpoint) {
        Map<String, Object> headers = ocisHeaderUtility.getSalsaHeader();
        return RestContext.SalsaRestContextBuilder.getBuilder(endpoint).requestHeaders(headers).queryParameters(queryParameters)
                .build();
    }
    
}
