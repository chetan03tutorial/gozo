package com.lbg.ib.api.sales.party.resources;

import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.party.AbstractSearchPartySetup;
import com.lbg.ib.api.sales.party.domain.SearchPartyRequest;
import com.lbg.ib.api.sales.party.service.SearchPartyService;
import com.lbg.ib.api.shared.domain.TinDetails;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.lbg.ib.api.sales.util.HttpStatus;

import javax.ws.rs.core.Response;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchPartyResourceTest extends AbstractSearchPartySetup {

    @InjectMocks
    private SearchPartyResource        resource;

    @Mock
    private SearchPartyService         searchPartyService;

    @Mock
    private RequestBodyResolver        requestResolver;

    @Mock
    private FieldValidator             fieldValidator;

    @Mock
    private SessionManagementDAO       session;

    @Mock
    private ConfigurationDAO           configurationService;

    @Mock
    private LoggerDAO                  logger;

    private SearchPartyRequest searchPartyRequest(String agreementIdentifier) {
        return new SearchPartyRequest(agreementIdentifier);
    }

    @Test
    public void fetchPartiesSuccessfullyWithValidAgreementIdentifier() {
        when(searchPartyService.retrieveParty(any(SearchPartyRequest.class)))
                .thenReturn(Arrays.asList(successResponseWithMultipleParties));
        when(fieldValidator.validateInstanceFields(any(SearchPartyRequest.class))).thenReturn(null);
        Response result = resource.searchParty(searchPartyRequest(validSixteenDigitAgreementIdentifier), null, null);
        assertEquals(result.getStatus(), HttpStatus.OK.status());
    }

    @Test(expected = InvalidFormatException.class)
    public void fetchPartiesWithInvalidAgreementIdentifier() {
        when(fieldValidator.validateInstanceFields(any(TinDetails.class)))
                .thenReturn(new ValidationError(""));
        resource.searchParty(searchPartyRequest(invalidAgreementIdentifier), null, null);
    }
}
