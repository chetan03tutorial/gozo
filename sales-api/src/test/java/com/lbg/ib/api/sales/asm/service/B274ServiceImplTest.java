package com.lbg.ib.api.sales.asm.service;

import com.lbg.ib.api.sales.dao.product.overdraft.OverdraftDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.b274.B274RequestDTO;
import com.lbg.ib.api.sales.dto.product.overdraft.OverdraftRequestDTO;
import com.lbg.ib.api.sales.dto.product.overdraft.OverdraftResponseDTO;
import com.lbg.ib.api.sales.product.domain.ArrangeToActivateParameters;
import com.lbg.ib.api.sales.product.domain.arrangement.OverdraftIntrestRates;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by 8796528 on 27/07/2018.
 */

@RunWith(MockitoJUnitRunner.class)
public class B274ServiceImplTest {

    @Mock
    private OverdraftDAO overdraftDAO;

    @Mock
    private ChannelBrandingDAO channelService;

    @Mock
    private SessionManagementDAO session;

    @Mock
    private ConfigurationDAO configurationService;

    @Mock
    private LoggerDAO logger;

    @InjectMocks
    B274ServiceImpl b274ServiceImpl;

    private static final ChannelBrandDTO CHANNEL  = new ChannelBrandDTO("ch","br", "cid");
    @Before
    public void setUp(){
        ArrangeToActivateParameters arrangeToActivateParameters = new ArrangeToActivateParameters();
        when(session.getArrangeToActivateParameters()).thenReturn(arrangeToActivateParameters);
        when(configurationService.getConfigurationStringValue("TRIMMED_CONTACT_POINT_ID", "cid"))
                .thenReturn("contactpoitnId");
    }

    @Test
    public void testMapB274Request(){
        BigDecimal updatedOverDraftAmount = new BigDecimal("1.0");
        B274RequestDTO b274RequestDTO = b274ServiceImpl.mapB274Request(updatedOverDraftAmount);
        assertNotNull(b274RequestDTO);
    }


    @Test
    public void testRetrieveOverdraftInterstRatesWhenResponseIsNull(){
        B274RequestDTO requestDTO = new B274RequestDTO();
        requestDTO.setProductOptions(new HashMap<String,String>());
        assertNull(b274ServiceImpl.retrieveOverdraftInterstRates(requestDTO));
    }


    @Test
    public void testRetrieveOverdraftInterstRatesWhenResponseIsNullHavingChannel(){
        when(channelService.getChannelBrand()).thenReturn(withResult(CHANNEL));
        B274RequestDTO requestDTO = new B274RequestDTO();
        requestDTO.setProductOptions(new HashMap<String,String>());
        assertNull(b274ServiceImpl.retrieveOverdraftInterstRates(requestDTO));
    }

    @Test
    public void testRetrieveOverdraftInterstRatesWhenResponseIsValidHavingChannel() {
        when(overdraftDAO.fetchOverdraftDetails((Mockito.any(OverdraftRequestDTO.class))))
                .thenReturn(DAOResponse.withResult(responseDTO()));

        B274RequestDTO requestDTO = new B274RequestDTO();
        requestDTO.setProductOptions(new HashMap<String,String>());
        assertNotNull(b274ServiceImpl.retrieveOverdraftInterstRates(requestDTO));
    }

    @Test
    public void testUpdateB274ResponseToSessionWithValidResponse(){
        when(overdraftDAO.fetchOverdraftDetails((Mockito.any(OverdraftRequestDTO.class))))
                .thenReturn(DAOResponse.withResult(responseDTO()));

        B274RequestDTO requestDTO = new B274RequestDTO();
        requestDTO.setProductOptions(new HashMap<String,String>());
        DAOResponse<OverdraftResponseDTO> response = b274ServiceImpl.retrieveOverdraftInterstRates(requestDTO);
        b274ServiceImpl.updateB274ResponseToSession(response,new BigDecimal("100"));
    }
    
    @Test
    public void mappingIntrestRatesToDomainObjectTest() {
    	OverdraftResponseDTO overdraftResponseDTO=new OverdraftResponseDTO();
    	BigDecimal overdraft=new BigDecimal("1234");
    	OverdraftIntrestRates overdraftIntrestRates=b274ServiceImpl.mappingIntrestRatesToDomainObject(overdraftResponseDTO,overdraft);
    	assertNotNull(overdraftIntrestRates);
    }

    private OverdraftResponseDTO responseDTO() {

        final OverdraftResponseDTO odResponse = new OverdraftResponseDTO();
        odResponse.setAmtIntFreeOverdraft(new BigDecimal("25"));
        odResponse.setAmtExcessFee(new BigDecimal("30"));
        odResponse.setAmtExcessFeeBalIncr(new BigDecimal("10"));
        odResponse.setAmtTotalCreditCost(new BigDecimal("114"));
        odResponse.setIntrateBase("0");
        odResponse.setIntrateMarginOBR("18.3");
        odResponse.setIntrateUnauthEAR("19.94");
        odResponse.setIntrateUnauthMnthly("1.53");
        odResponse.setNExcessFeeCap(new BigInteger("3"));
        odResponse.setIntrateAuthMnthly("0000000");
        odResponse.setIntrateAuthEAR("0000000");
        odResponse.setAmtUsageFeeOverdraft(new BigDecimal("0"));
        return odResponse;
    }


}
