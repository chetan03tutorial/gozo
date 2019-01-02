package com.lbg.ib.api.sales.product.overdraft;

import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.rmi.RemoteException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.dao.FoundationServerUtil;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.product.overdraft.OverdraftDAOImpl;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.sales.dto.product.overdraft.OverdraftRequestDTO;
import com.lbg.ib.api.sales.dto.product.overdraft.OverdraftResponseDTO;
import com.lbg.ib.api.sales.soapapis.wsbridge.account.AccountPortType;
import com.lbg.ib.api.sales.soapapis.wsbridge.account.StB274AODGetInterestRates;
import com.lbg.ib.api.sales.soapapis.wsbridge.account.StB274BODGetInterestRates;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StError;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StHeader;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StODRates;

@RunWith(MockitoJUnitRunner.class)
public class OverdraftDAOImplTest {

    public static final String                CANNOT_CONNECT_TO_REMOTE_POINT = "cannotConnectToRemote";

    @InjectMocks
    OverdraftDAOImpl                          overdraftDAOImpl;

    @Mock
    private LoggerDAO                         logger;

    @Mock
    private AccountPortType                   accountPort;

    @Mock
    private FoundationServerUtil              foundationServerUtil;

    private DAOResponse<OverdraftResponseDTO> overdraftOffer;

    @Test
    public void shouldReturnTheOverdraftResponseWhenCorrecrtRequestIsPopulated() throws RemoteException {
        when(accountPort.b274ODGetInterestRates(Mockito.any(StB274AODGetInterestRates.class)))
                .thenReturn(responseDTO());
        when(foundationServerUtil.createStHeader(settingUserContext())).thenReturn(setheaders("IBL"));
        StHeader stHead = setheaders("IBL");
        overdraftOffer = overdraftDAOImpl.fetchOverdraftDetails(requestDTO(stHead));
        OverdraftResponseDTO response = overdraftOffer.getResult();
        assertThat(response.getAmtExcessFee(), is(responseDTO().getStodrates().getAmtExcessFee()));
        assertThat(response.getAmtExcessFeeBalIncr(), is(responseDTO().getStodrates().getAmtExcessFeeBalIncr()));
        assertThat(response.getAmtIntFreeOverdraft(), is(responseDTO().getStodrates().getAmtIntFreeOverdraft()));

    }

    @Test
    public void ShouldReturnErrorCodeWhenRemoteExceptionIsThrownByService() throws RemoteException {

        when(accountPort.b274ODGetInterestRates(Mockito.any(StB274AODGetInterestRates.class)))
                .thenThrow(RemoteException.class);
        StHeader stHead = setheaders("IBL");
        DAOResponse<OverdraftResponseDTO> response = overdraftDAOImpl.fetchOverdraftDetails(requestDTO(stHead));
        verify(logger).logException(eq(OverdraftDAOImpl.class), any(RemoteException.class));
        assertThat(response.getError().getErrorCode(), is(CANNOT_CONNECT_TO_REMOTE_POINT));
    }

    @Test
    public void ShouldReturnErrorCodeWhenBackendReturnedWithError() throws RemoteException {
        when(accountPort.b274ODGetInterestRates(Mockito.any(StB274AODGetInterestRates.class)))
                .thenReturn(responseWithError());
        StHeader stHead = setheaders("IBL");
        DAOResponse<OverdraftResponseDTO> response = overdraftDAOImpl.fetchOverdraftDetails(requestDTO(stHead));
        //verify(logger).logError("800001", "Response has returend with error", OverdraftDAOImpl.class);
        assertThat(response.getError().getErrorCode(), is("800001"));

    }

    private StB274BODGetInterestRates responseDTO() {

        StB274BODGetInterestRates odResponse = new StB274BODGetInterestRates();
        StODRates stODRates = new StODRates();
        stODRates.setAmtExcessFee(new BigDecimal("30"));
        stODRates.setAmtExcessFeeBalIncr(new BigDecimal("10"));
        stODRates.setAmtIntFreeOverdraft(new BigDecimal("25"));
        stODRates.setAmtPlannedOverdraftFee(new BigDecimal("0"));
        stODRates.setAmtTotalCreditCost(new BigDecimal("31.94"));
        stODRates.setIntrateAuthEAR("1.5");
        stODRates.setIntrateAuthMnthly("19.94");
        odResponse.setStodrates(stODRates);
        return odResponse;
    }

    private StB274BODGetInterestRates responseWithError() {

        StB274BODGetInterestRates odResponse = new StB274BODGetInterestRates();
        StError stError = new StError();

        stError.setErrormsg("System cannot fetch the overdraft values");
        stError.setErrorno(800001);
        odResponse.setSterror(stError);
        return odResponse;
    }

    private UserContext settingUserContext() {
        return new UserContext("UNAUTHSALE", null, null, null, null, "IBL", null, null, null, null, null);
    }

    DAOResponse<ChannelBrandDTO> channelBrandValues() {

        return withResult(new ChannelBrandDTO("IBL", "LLOYDS", "IBL"));
    }

    private StHeader setheaders(String chanId) {
        StHeader stHeader = new StHeader();
        stHeader.setChanid(chanId);
        stHeader.setUseridAuthor("UNAUTHSALE");
        return stHeader;
    }

    private OverdraftRequestDTO requestDTO(StHeader stHeaders) {

        OverdraftRequestDTO odReq = new OverdraftRequestDTO();
        odReq.setStheader(stHeaders);
        odReq.setSortcode("777505");
        odReq.setAmtOverdraft(new BigDecimal(200));
        odReq.setCbsprodnum(new BigInteger("0071"));
        odReq.setCbstariff("1");
        return odReq;
    }
}
