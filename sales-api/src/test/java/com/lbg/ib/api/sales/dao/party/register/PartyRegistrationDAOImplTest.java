package com.lbg.ib.api.sales.dao.party.register;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.sales.dao.FoundationServerUtil;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.party.PartyRequestDTO;
import com.lbg.ib.api.sales.dto.party.PartyResponseDTO;
import com.lbg.ib.api.sales.soapapis.wsbridge.application.ApplicationPortType;
import com.lbg.ib.api.sales.soapapis.wsbridge.application.StB801AResolveProdToPty;
import com.lbg.ib.api.sales.soapapis.wsbridge.application.StB801BResolveProdToPty;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StChannelDescription;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StError;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StHeader;

@RunWith(MockitoJUnitRunner.class)
public class PartyRegistrationDAOImplTest {

    @InjectMocks
    private PartyRegistrationDAOImpl PartyRegistrationDAOImpl;

    @Mock
    private ApplicationPortType      applicationPortType;

    @Mock
    private LoggerDAO                logger;

    @Mock
    private FoundationServerUtil     foundationServerUtil;

    @Mock
    private SessionManagementDAO     sessionManagementDAO;

    @Mock
    private DAOExceptionHandler      exceptionHandler;

    @Mock
    private UserContext              context;

    @Test
    public void testRetrievePartyMandate() throws Exception {
        Calendar cal = Calendar.getInstance();
        PartyRequestDTO partyRequestDTO = new PartyRequestDTO(null, "postCode", "title", "firstName", "surName", cal,
                "cvv", "accountType", "partyId");
        when(sessionManagementDAO.getUserContext()).thenReturn(context);
        when(context.getOcisId()).thenReturn("12345");
        when(context.getChannelId()).thenReturn("channelId");
        StHeader stHeader = new StHeader();
        when(foundationServerUtil.createStHeader(context)).thenReturn(stHeader);
        StB801BResolveProdToPty response = new StB801BResolveProdToPty();
        StChannelDescription[] stchan = new StChannelDescription[1];
        StChannelDescription stChannelDescription = new StChannelDescription();
        stChannelDescription.setChandesc("chandesc");
        stChannelDescription.setChanid("chanid");
        stchan[0] = stChannelDescription;
        response.setStchan(stchan);
        response.setPwdstate("pwdstate");
        when(applicationPortType.b801ResolveProdToPty(Mockito.any(StB801AResolveProdToPty.class))).thenReturn(response);
        DAOResponse<PartyResponseDTO> daoResonse = PartyRegistrationDAOImpl.retrievePartyMandate(partyRequestDTO);
        assertEquals(daoResonse.getResult().getPwdState(), "pwdstate");
    }

    @Test
    public void testRetrievePartyMandateWhenStChannelDescriptionIsNull() throws Exception {
        Calendar cal = Calendar.getInstance();
        PartyRequestDTO partyRequestDTO = new PartyRequestDTO(null, "postCode", "title", "firstName", "surName", cal,
                "cvv", "accountType", "partyId");
        when(sessionManagementDAO.getUserContext()).thenReturn(context);
        when(context.getOcisId()).thenReturn("12345");
        when(context.getChannelId()).thenReturn("channelId");
        StHeader stHeader = new StHeader();
        when(foundationServerUtil.createStHeader(context)).thenReturn(stHeader);
        StB801BResolveProdToPty response = new StB801BResolveProdToPty();
        response.setStchan(null);
        response.setPwdstate("pwdstate");
        when(applicationPortType.b801ResolveProdToPty(Mockito.any(StB801AResolveProdToPty.class))).thenReturn(response);
        DAOResponse<PartyResponseDTO> daoResonse = PartyRegistrationDAOImpl.retrievePartyMandate(partyRequestDTO);
        assertNull(daoResonse);
    }

    @Test
    public void testRetrievePartyMandateForErrorResponse() throws Exception {
        Calendar cal = Calendar.getInstance();
        PartyRequestDTO partyRequestDTO = new PartyRequestDTO(null, "postCode", "title", "firstName", "surName", cal,
                "cvv", "accountType", "partyId");
        when(sessionManagementDAO.getUserContext()).thenReturn(context);
        when(context.getOcisId()).thenReturn("12345");
        when(context.getChannelId()).thenReturn("channelId");
        StHeader stHeader = new StHeader();
        when(foundationServerUtil.createStHeader(context)).thenReturn(stHeader);
        StB801BResolveProdToPty response = new StB801BResolveProdToPty();
        StError sterror = new StError();
        sterror.setErrormsg("errormsg");
        sterror.setErrorno(12345);
        response.setSterror(sterror);
        when(applicationPortType.b801ResolveProdToPty(Mockito.any(StB801AResolveProdToPty.class))).thenReturn(response);
        DAOResponse<PartyResponseDTO> daoResonse = PartyRegistrationDAOImpl.retrievePartyMandate(partyRequestDTO);
        assertEquals(daoResonse.getError().getErrorMessage(), "errormsg");
    }

    @Test
    public void testRetrievePartyMandateForException() throws Exception {
        Calendar cal = Calendar.getInstance();
        PartyRequestDTO partyRequestDTO = new PartyRequestDTO(null, "postCode", "title", "firstName", "surName", cal,
                "cvv", "accountType", "partyId");
        when(sessionManagementDAO.getUserContext()).thenReturn(context);
        when(context.getOcisId()).thenReturn("12345");
        when(context.getChannelId()).thenReturn("channelId");
        StHeader stHeader = new StHeader();
        when(foundationServerUtil.createStHeader(context)).thenReturn(stHeader);
        Exception e = new RuntimeException("message");
        when(applicationPortType.b801ResolveProdToPty(Mockito.any(StB801AResolveProdToPty.class))).thenThrow(e);
        DAOError error = new DAOError("errorCode", "errorMsg");
        when(exceptionHandler.handleException(e, PartyRegistrationDAOImpl.class, "retrievePartyMandate",
                partyRequestDTO)).thenReturn(error);
        DAOResponse<PartyResponseDTO> daoResonse = PartyRegistrationDAOImpl.retrievePartyMandate(partyRequestDTO);
        assertEquals(daoResonse.getError().getErrorMessage(), "errorMsg");
    }

}
