package com.lbg.ib.api.sales.party.service;

import com.lbg.ib.api.sales.common.rest.client.RestContext;
import com.lbg.ib.api.sales.common.rest.util.OcisRestHeaderUtil;
import com.lbg.ib.api.sales.dao.restclient.ExternalRestApiClientDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.docupload.util.SalsaEndpoints;
import com.lbg.ib.api.sales.docupload.util.UriResolver;
import com.lbg.ib.api.sales.party.AbstractSearchPartySetup;
import com.lbg.ib.api.sales.party.domain.IBParties;
import com.lbg.ib.api.sales.party.domain.SearchPartyRequest;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchPartyServiceTest extends AbstractSearchPartySetup {
    
    @InjectMocks
    private SearchPartyServiceImpl     service;
    
    @Mock
    private UriResolver                uriResolver;
    
    @Mock
    private ExternalRestApiClientDAO   externalRestApiClientDAO;
    
    @Mock
    private ConfigurationDAO           configurationService;
    
    @Mock
    private SessionManagementDAO       sessionService;
    
    @Mock
    private OcisRestHeaderUtil         ocisHeaderUtil;
    
    private static Map<String, Object> headerMap;
    private static BranchContext       branchContext;
    
    @Before
    public void setup() {
        headerMap = new HashMap<String, Object>();
        headerMap.put("x-lbg-user-id", "19");
        
        branchContext = new BranchContext();
        branchContext.setColleagueId("CT145639");
        
        when(sessionService.getBranchContext()).thenReturn(branchContext);
        when(configurationService.getConfigurationItems(anyString())).thenReturn(headerMap);
        
        when(ocisHeaderUtil.getSalsaHeader()).thenReturn(headerMap);
        
        when(uriResolver.getEndpoint(SalsaEndpoints.Q226_ENDPOINT)).thenReturn("Q226_ENDPOINT");
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void fetchMultiplePartiesSuccessfully() {
        when(externalRestApiClientDAO.customGet(any(RestContext.class), any(Class.class))).thenReturn(searchPartySalsaResponse);
        SearchPartyRequest partyDetails = new SearchPartyRequest(validSixteenDigitAgreementIdentifier);
        List<IBParties> apiResponse = service.retrieveParty(partyDetails);
        assertEquals(apiResponse.size(), 1);
    }
}
