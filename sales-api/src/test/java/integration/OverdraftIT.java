package integration;

import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.rpc.ServiceException;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.dao.FoundationServerUtil;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.product.overdraft.OverdraftDAOImpl;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.sales.dto.product.overdraft.OverdraftRequestDTO;
import com.lbg.ib.api.sales.dto.product.overdraft.OverdraftResponseDTO;
import com.lbg.ib.api.sales.soapapis.wsbridge.account.AccountLocator;
import com.lbg.ib.api.sales.soapapis.wsbridge.account.AccountPortType;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StHeader;

@RunWith(MockitoJUnitRunner.class)
public class OverdraftIT {

    @InjectMocks
    OverdraftDAOImpl             overdraftDAOImpl;

    @Mock
    private LoggerDAO            logger;

    @Mock
    private AccountPortType      accountPort;

    @Mock
    private FoundationServerUtil foundationServerUtil;

    @Mock
    private SessionManagementDAO session;

    @Mock
    private ChannelBrandingDAO   channelService;

    @Mock
    private ConfigurationDAO     configurationService;

    @Ignore
    @Test
    public void shouldReturnTheAcutualResponseFromBapiWhenRequestIsPassed()
            throws MalformedURLException, ServiceException {
        AccountLocator accountLocator = new AccountLocator();
        accountPort = accountLocator.getAccount(new URL("http://10.245.211.251:22910/fserv/"));
        overdraftDAOImpl.setAccountPort(accountPort);
        when(configurationService.getConfigurationStringValue("CONTACT_POINT_ID", "IBL")).thenReturn("0000777505");
        when(channelService.getChannelBrand()).thenReturn(channelBrandValues());
        when(session.getUserContext()).thenReturn(settingUserContext());
        when(foundationServerUtil.createStHeader(session.getUserContext())).thenReturn(setheaders("IBL"));
        StHeader stHead = setheaders("IBL");
        DAOResponse<OverdraftResponseDTO> mockResponse = overdraftDAOImpl.fetchOverdraftDetails(requestDTO(stHead));
        assertNotNull(mockResponse);

    }

    private UserContext settingUserContext() {
        return new UserContext("UNAUTHSALE", null, null, null, null, "IBL", null, null, null, null, null);
    }

    DAOResponse<ChannelBrandDTO> channelBrandValues() {

        return withResult(new ChannelBrandDTO("IBL", "LLOYDS", "IBL"));
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

    private StHeader setheaders(String chanId) {
        StHeader stHeader = new StHeader();
        stHeader.setChanid(chanId);
        stHeader.setUseridAuthor("UNAUTHSALE");
        return stHeader;
    }

}
