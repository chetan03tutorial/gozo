/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.communication.resources;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.sales.common.validation.FieldValidator;
import com.lbg.ib.api.sales.communication.domain.InvolvedPartyDetails;
import com.lbg.ib.api.sales.communication.service.CommunicationPartyService;
import com.lbg.ib.api.shared.util.request.resolvers.RequestBodyResolver;

public class CommunicationPartyResourceTest {

    @InjectMocks
    CommunicationPartyResource        communicationPartyResource;

    @Mock
    private RequestBodyResolver       resolver;
    @Mock
    private FieldValidator            fieldValidator;
    @Mock
    private CommunicationPartyService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testToCheckWhetherTheValuesAreSetInSession() throws Exception {
        when(resolver.resolve(any(String.class), eq(InvolvedPartyDetails.class)))
                .thenReturn(new InvolvedPartyDetails());
        service.savePartyDetailsForSendCommunication(new InvolvedPartyDetails());
        Response response = communicationPartyResource.populateDetailsInSession("Request");
        assertEquals(response.getStatus(), 200);
    }

    @Test(expected = InvalidFormatException.class)
    public void shouldThrowInvalidFormatExceptionIfValidationErrorIsPresent() throws Exception {
        InvolvedPartyDetails partyDetails = settingPartyDetails();
        when(resolver.resolve(any(String.class), eq(InvolvedPartyDetails.class))).thenReturn(partyDetails);
        when(fieldValidator.validateInstanceFields(partyDetails))
                .thenReturn(new ValidationError("Invalid Format Exception"));
        communicationPartyResource.populateDetailsInSession("Request");

    }

    private InvolvedPartyDetails settingPartyDetails() {
        InvolvedPartyDetails partyDetails = new InvolvedPartyDetails();
        partyDetails.setFirstName("Joy");
        partyDetails.setLastName("Peter");
        partyDetails.setPostCode("E18EP");
        partyDetails.setEmail("joy@lloydsBanking.co.uk");
        return partyDetails;
    }

}
