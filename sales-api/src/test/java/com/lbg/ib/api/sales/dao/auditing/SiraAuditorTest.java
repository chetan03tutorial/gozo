package com.lbg.ib.api.sales.dao.auditing;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.common.auditing.SiraAuditor;
import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.sales.dto.product.activate.ActivateProductResponseDTO;
import com.lbg.ib.api.sales.dto.product.offer.ProductOfferedDTO;
import com.lbg.ib.api.sales.dto.product.offer.SIRAScoreDTO;

@RunWith(MockitoJUnitRunner.class)
public class SiraAuditorTest {

    @Mock
    SessionManagementDAO           session;

    @Mock
    ArrangementAuditingDAO<String> arrangementAuditingDAO;

    @Mock
    ChannelBrandingDAO             channelBranding;

    @InjectMocks
    SiraAuditor                    siraAuditor;

    @Test
    public void testAuditWithNoSiraDTO() {
        ProductOfferedDTO productOfferedDTO = new ProductOfferedDTO();
        siraAuditor.audit(productOfferedDTO);

        verify(arrangementAuditingDAO, times(2)).audit(any(String.class), any(String.class));
    }

    @Test
    public void testAuditWithSiraDTOWithConnectionFlagTrue() {
        UserContext userContext = new UserContext(null, null, null, "dummyPartyId", null, null, null, null, null, null,
                null);
        when(session.getUserContext()).thenReturn(userContext);
        when(session.getSessionId()).thenReturn("dummySessionId");
        ChannelBrandDTO channelBrandDTO = new ChannelBrandDTO(null, null, null);
        when(channelBranding.getChannelBrand()).thenReturn(DAOResponse.withResult(channelBrandDTO));
        ProductOfferedDTO productOfferedDTO = new ProductOfferedDTO();
        SIRAScoreDTO siraScoreDTO = new SIRAScoreDTO();
        siraScoreDTO.setSiraConnectionErrorFlag(true);
        productOfferedDTO.setSiraScoreDTO(siraScoreDTO);
        siraAuditor.audit(productOfferedDTO);

        verify(channelBranding, times(1)).getChannelBrand();
    }

    @Test
    public void testAuditWithSiraDTOWithConnectionFlagFalse() {
        UserContext userContext = new UserContext(null, null, null, "dummyPartyId", null, null, null, null, null, null,
                null);
        when(session.getUserContext()).thenReturn(userContext);
        when(session.getSessionId()).thenReturn("dummySessionId");
        ChannelBrandDTO channelBrandDTO = new ChannelBrandDTO(null, null, null);
        when(channelBranding.getChannelBrand()).thenReturn(DAOResponse.withResult(channelBrandDTO));
        ProductOfferedDTO productOfferedDTO = new ProductOfferedDTO();
        SIRAScoreDTO siraScoreDTO = new SIRAScoreDTO();
        siraScoreDTO.setSiraConnectionErrorFlag(false);
        productOfferedDTO.setSiraScoreDTO(siraScoreDTO);
        siraAuditor.audit(productOfferedDTO);

        verify(arrangementAuditingDAO, times(2)).audit(any(String.class), any(String.class));
    }

    @Test
    public void testAuditWithNoSiraDTOWithActivateProductResponseDTO() {
        ActivateProductResponseDTO activateProductResponseDTO = new ActivateProductResponseDTO();
        siraAuditor.audit(activateProductResponseDTO);

        verify(arrangementAuditingDAO, times(2)).audit(any(String.class), any(String.class));
    }

    @Test
    public void testAuditWithNoSiraDTOWithActivateProductResponseDTOWithConnectionFlagFalse() {
        ActivateProductResponseDTO activateProductResponseDTO = new ActivateProductResponseDTO();
        SIRAScoreDTO siraScoreDTO = new SIRAScoreDTO();
        siraScoreDTO.setSiraConnectionErrorFlag(false);
        activateProductResponseDTO.getSiraScoreDTO();
        siraAuditor.audit(activateProductResponseDTO);

        verify(arrangementAuditingDAO, times(2)).audit(any(String.class), any(String.class));
    }

    @Test
    public void testAuditWithNoSiraDTOWithActivateProductResponseDTOWithConnectionFlagTrue() {
        UserContext userContext = new UserContext(null, null, null, "dummyPartyId", null, null, null, null, null, null,
                null);
        when(session.getUserContext()).thenReturn(userContext);
        when(session.getSessionId()).thenReturn("dummySessionId");
        ChannelBrandDTO channelBrandDTO = new ChannelBrandDTO(null, null, null);
        when(channelBranding.getChannelBrand()).thenReturn(DAOResponse.withResult(channelBrandDTO));

        ActivateProductResponseDTO activateProductResponseDTO = new ActivateProductResponseDTO();
        SIRAScoreDTO siraScoreDTO = new SIRAScoreDTO();
        siraScoreDTO.setSiraConnectionErrorFlag(true);
        activateProductResponseDTO.setSiraScoreDTO(siraScoreDTO);
        siraAuditor.audit(activateProductResponseDTO);

        verify(arrangementAuditingDAO, times(1)).audit(any(String.class), any(String.class));
    }
}
