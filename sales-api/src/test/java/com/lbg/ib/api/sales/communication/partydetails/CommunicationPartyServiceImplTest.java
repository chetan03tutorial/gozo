/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.communication.partydetails;

import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.communication.domain.InvolvedPartyDetails;
import com.lbg.ib.api.sales.communication.service.CommunicationPartyServiceImpl;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.dao.communicationmanagement.CommunicationMangementDAO;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.sales.dto.communicationmanagement.CommunicationManagementDTO;
import com.lbg.ib.api.sales.dto.communicationmanagement.CommunicationManagementResponseDTO;
import com.lbg.ib.api.sales.dto.communicationparty.CommunicationPartyDetailsDTO;

public class CommunicationPartyServiceImplTest {

    private static final String   POST_CODE          = "E18EP";

    private static final String   EMAIL              = "testmail@lloydsbanking.co.uk";

    private static final String   FIRST_NAME         = "Joy";

    private static final String   LAST_NAME          = "Peter";

    private static final String   TITLE              = "Mr";

    private static final String   BRAND              = "LBG";

    private static final String   KEY_FOR_TITLE      = "IB.Customer.Title";

    private static final String   KEY_FOR_FIRST_NAME = "IB.Customer.FirstName";

    private static final String   KEY_FOR_LAST_NAME  = "IB.Customer.LastName";

    private static final String   KEY_FOR_POST_CODE  = "IB.Customer.Addr.PostCode";

    private static final String   TEMPLATE_ID        = "EM_PCA_Pipeline_chasing";

    private static final String   KEY_FOR_MNEMONIC   = "IB.Product.Mnemonic";

    @InjectMocks
    CommunicationPartyServiceImpl communicationPartyServiceImpl;

    @Mock
    SessionManagementDAO          session;

    @Mock
    CommunicationMangementDAO     communicationManagementDAO;

    @Mock
    ConfigurationDAO              configDAO;

    @Mock
    LoggerDAO                     logger;

    private UserContext           userContext        = new UserContext("userId", "ipAddress", "sessionId", "partyId",
            "ocisId", "channelId", "chansecMode", "userAgent", "language", "inboxIdClient", "host");

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testToCheckWhetherThePartyDetailsAreSetInSession() {
        InvolvedPartyDetails partyDetails = mapInvolvedPartyDetails();
        communicationPartyServiceImpl.savePartyDetailsForSendCommunication(partyDetails);
        ArgumentCaptor<CommunicationPartyDetailsDTO> argument = ArgumentCaptor
                .forClass(CommunicationPartyDetailsDTO.class);
        Mockito.verify(session).setSendCommunicationPartyDetails(argument.capture());
        CommunicationPartyDetailsDTO communicationPartyDetails = argument.getValue();
        assertThat(communicationPartyDetails.getPostCode(), is(POST_CODE));
        assertThat(communicationPartyDetails.getEmail(), is(EMAIL));
        assertThat(communicationPartyDetails.getFirstName(), is(FIRST_NAME));
        assertThat(communicationPartyDetails.getLastName(), is(LAST_NAME));
        assertThat(communicationPartyDetails.getTitle(), is(TITLE));

    }

    @Test
    public void testToCheckWhetherTheSendCommunicationServiceIsCalledAndReturnedTrue() {

        Mockito.when(configDAO.getConfigurationStringValue("BRAND_NAME_MAPPING", "IBL")).thenReturn("LBG");
        Mockito.when(communicationManagementDAO.sendEmailCommunictaion(Mockito.any(CommunicationManagementDTO.class),
                Mockito.any(UserContext.class))).thenReturn(sendCommunicationResposneWithSucessMessageTrue());
        communicationPartyServiceImpl.sendEmailCommunictaion(mapCommunicationPartyDetails(), "P_CLASSIC", userContext);
        Mockito.verify(logger).logDebug(CommunicationPartyServiceImpl.class,
                "SendCommunication API call is completed sucessfully and returned:", true);

    }

    @Test
    public void testToCheckWhetherTheSendCommunicationServiceIsCalledAndReturnedFalse() {

        Mockito.when(configDAO.getConfigurationStringValue("BRAND_NAME_MAPPING", "IBL")).thenReturn("LBG");
        Mockito.when(communicationManagementDAO.sendEmailCommunictaion(Mockito.any(CommunicationManagementDTO.class),
                Mockito.any(UserContext.class))).thenReturn(sendCommunicationResposneWithSucessMessageFalse());
        communicationPartyServiceImpl.sendEmailCommunictaion(mapCommunicationPartyDetails(), "P_CLASSIC", userContext);
        Mockito.verify(logger).logDebug(CommunicationPartyServiceImpl.class,
                "SendCommunication API call is completed sucessfully and returned:", false);

    }

    public DAOResponse<CommunicationManagementResponseDTO> sendCommunicationResposneWithSucessMessageTrue() {
        CommunicationManagementResponseDTO communicationManagementResponseDTO = new CommunicationManagementResponseDTO();
        communicationManagementResponseDTO.setIsSuccessful(true);

        return withResult(communicationManagementResponseDTO);
    }

    public DAOResponse<CommunicationManagementResponseDTO> sendCommunicationResposneWithSucessMessageFalse() {
        CommunicationManagementResponseDTO communicationManagementResponseDTO = new CommunicationManagementResponseDTO();
        communicationManagementResponseDTO.setIsSuccessful(false);

        return withResult(communicationManagementResponseDTO);
    }

    public DAOResponse<ChannelBrandDTO> mapChannelBrandDTO() {
        return withResult(new ChannelBrandDTO("BRANCH", "HLX", "HALIFAX"));
    }

    private CommunicationPartyDetailsDTO mapCommunicationPartyDetails() {

        CommunicationPartyDetailsDTO communicationPartyDetailsDTO = new CommunicationPartyDetailsDTO();
        communicationPartyDetailsDTO.setEmail(EMAIL);
        communicationPartyDetailsDTO.setFirstName(FIRST_NAME);
        communicationPartyDetailsDTO.setLastName(LAST_NAME);
        communicationPartyDetailsDTO.setPostCode(POST_CODE);
        communicationPartyDetailsDTO.setTitle(TITLE);
        return communicationPartyDetailsDTO;
    }

    private InvolvedPartyDetails mapInvolvedPartyDetails() {
        InvolvedPartyDetails partyDetails = new InvolvedPartyDetails();
        partyDetails.setPostCode(POST_CODE);
        partyDetails.setEmail(EMAIL);
        partyDetails.setFirstName(FIRST_NAME);
        partyDetails.setLastName(LAST_NAME);
        partyDetails.setTitle(TITLE);
        return partyDetails;
    }

    CommunicationManagementDTO mapSendCommunicationRequestDTO(CommunicationPartyDetailsDTO partyDetails,
            String mnemonic) {
        CommunicationManagementDTO requestDTO = new CommunicationManagementDTO();
        HashMap<String, String> tokenisedMap = populateTokenisedMap(partyDetails, mnemonic);
        requestDTO.setTemplateID(TEMPLATE_ID);
        requestDTO.setContactPointID(partyDetails.getEmail());
        requestDTO.setBrand("LBG");
        requestDTO.setTokenisedMap(tokenisedMap);
        return requestDTO;
    }

    private HashMap<String, String> populateTokenisedMap(CommunicationPartyDetailsDTO partyDetails, String mnemonic) {
        HashMap<String, String> tokenisedMap = new HashMap<String, String>();
        tokenisedMap.put(KEY_FOR_TITLE, partyDetails.getTitle());
        tokenisedMap.put(KEY_FOR_FIRST_NAME, partyDetails.getFirstName());
        tokenisedMap.put(KEY_FOR_LAST_NAME, partyDetails.getLastName());
        tokenisedMap.put(KEY_FOR_POST_CODE, partyDetails.getPostCode());
        tokenisedMap.put(KEY_FOR_MNEMONIC, mnemonic);
        return tokenisedMap;
    }

}
