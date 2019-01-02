/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.dao.auditing;

import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.common.auditing.ThreatAuditor;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.threatmatrix.ThreatMatrixDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.sales.dto.device.ThreatMatrixDTO;
import com.lbg.ib.api.sales.dto.product.offer.MarketingPreferenceDTO;
import com.lbg.ib.api.sales.dto.product.offer.ProductOfferedDTO;
import com.lbg.ib.api.sales.dto.product.offer.SIRAScoreDTO;

@RunWith(MockitoJUnitRunner.class)
public class ThreatAuditorTest {

    public static final String                           APPLICATION_ID = "applicationId";
    public static final DAOResponse<Map<String, String>> ERROR          = DAOResponse
            .<Map<String, String>> withError(new DAOError("c", "m"));
    private static final String                          APP_TYPE       = "prim_cur_acc";
    private static final String                          OCIS_ID        = "ocisId";
    private static final String                          CA             = "CA";
    private final Map<String, String>                          AUDITEE        = new TreeMap<String, String>() {
                                                                            {
                                                                                put("account_login", "ocis");
                                                                                put("policy", "default");
                                                                                put("reason_code", "reasonCode");
                                                                                put("policy_score", "1");
                                                                                put("request_id", "req");
                                                                                put("request_result", "req_result");
                                                                                put("service_type", "session-policy");
                                                                                put("session_id", "sessionId");
                                                                                put("unfiltered", "unfilteredValue");
                                                                            }
                                                                        };

    @Mock
    private ChannelBrandingDAO                           channelBranding;

    @Mock
    private LoggerDAO                                    logger;

    @Mock
    private ThreatMatrixDAO                              threatMatrixDao;

    @Mock
    private ArrangementAuditingDAOImpl                   arrangementAuditingDAOImpl;

    @Mock
    private SIRAScoreDTO                                 aSiraScoreDTO;

    @Mock
    private ThreatMatrixDTO                              atThreatMatrixDTO;

    @InjectMocks
    private final ThreatAuditor                                auditor        = new ThreatAuditor();

    @Test
    public void shouldCallArrangementAuditingWithCorrectFormatWhenASuccessfulCheckHappens() throws Exception {
        when(threatMatrixDao.retrieveThreadMatrixResults(APPLICATION_ID, APP_TYPE, OCIS_ID))
                .thenReturn(withResult(AUDITEE));
        when(channelBranding.getChannelBrand())
                .thenReturn(withResult(new ChannelBrandDTO("LTSBRetail", "LLOYDS", "IBL")));

        auditor.audit(productOfferedWith(APPLICATION_ID, CA));

        verify(arrangementAuditingDAOImpl)
                .audit("sessionId,ocis,IBL,default,1,reasonCode,req,req_result,prim_cur_acc,SUCCESS", "SZ03");
    }

    @Test
    public void shouldCallArrangementAuditingWithApplicationIdAndFailEventWhenTmCallFails() throws Exception {
        when(threatMatrixDao.retrieveThreadMatrixResults(APPLICATION_ID, APP_TYPE, OCIS_ID)).thenReturn(ERROR);
        when(channelBranding.getChannelBrand())
                .thenReturn(withResult(new ChannelBrandDTO("LTSBRetail", "LLOYDS", "IBL")));

        auditor.audit(productOfferedWith(APPLICATION_ID, CA));

        verify(arrangementAuditingDAOImpl).audit(",,,applicationId,prim_cur_acc,FAIL", "SZ04");
    }

    @Test
    public void anyExceptionComesFromAuditorWillBeSuppressedAndLogged() throws Exception {
        final RuntimeException EXCEPTION = new RuntimeException();
        when(threatMatrixDao.retrieveThreadMatrixResults(APPLICATION_ID, APP_TYPE, OCIS_ID)).thenThrow(EXCEPTION);

        auditor.audit(productOfferedWith(APPLICATION_ID, CA));

        verify(logger).logException(ThreatAuditor.class, EXCEPTION);
    }

    private ProductOfferedDTO productOfferedWith(final String applicationId, final String appType) {
    return new ProductOfferedDTO(
        applicationId,
        appType,
        "",
        null,
        null,
        OCIS_ID,
        "",
        "",
        "",
        "",
        null,
        AUDITEE,
        null,
        "",
        "",
        "",
        applicationId,
        null,
        null,
        false,
        null,
        applicationId,
        null,
        "pdtFamilyId",
        null,
        aSiraScoreDTO,
        atThreatMatrixDTO,
        Collections.<MarketingPreferenceDTO>emptyList());
    }
}
