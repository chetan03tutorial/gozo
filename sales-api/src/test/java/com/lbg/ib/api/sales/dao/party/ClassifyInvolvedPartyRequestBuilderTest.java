package com.lbg.ib.api.sales.dao.party;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.dao.SalsaGBOHeaderUtility;
import com.lbg.ib.api.sales.dao.SalsaMCAHeaderUtility;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.sales.dao.party.classify.ClassifyInvolvedPartyRequestBuilder;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.party.ClassifyPartyRequestDTO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lbg.ib.api.shared.domain.TaxResidencyDetails;
import com.lloydstsb.ib.salsa.crs.messages.ClassifyInvolvedPartyRequest;
import com.lloydstsb.ib.salsa.crs.messages.SOAPHeader;

@RunWith(MockitoJUnitRunner.class)
public class ClassifyInvolvedPartyRequestBuilderTest {

    @InjectMocks
    private ClassifyInvolvedPartyRequestBuilder requestBuilder;
    @Mock
    private SessionManagementDAO                session;
    @Mock
    private SalsaGBOHeaderUtility               gboHeaderUtility;
    @Mock
    private SalsaMCAHeaderUtility               mcaheaderUtility;
    @Mock
    private ConfigurationDAO                    configuration;

    private static final String                 sessionId = "2678o66";
    private Map<String, Object>                 channelIdMap;

    /*
     * private Map<String, Object> prepareChannelIdMap(){ channelIdMap = new
     * HashMap<String, Object>(); channelIdMap.put("LBG", "LBG");
     * channelIdMap.put("HLX", "HLX"); channelIdMap.put("BOS", "BOS"); return
     * channelIdMap; }
     */

    @Before
    public void setup() {
        when(mcaheaderUtility.prepareSoapHeader(anyString(), anyString())).thenReturn(new LinkedList<SOAPHeader>());
        when(gboHeaderUtility.prepareSoapHeader(anyString(), anyString())).thenReturn(new LinkedList<SOAPHeader>());
        when(session.getSessionId()).thenReturn(sessionId);
        when(configuration.getConfigurationItems("ChannelIdMapping")).thenReturn(prepareChannelIdMap());
    }

    @Test
    public void testBuildRequestForSandbox() {
        when(session.getUserContext()).thenReturn(prepareUserContext("LBG"));
        ClassifyInvolvedPartyRequest rq = requestBuilder.build(prepareRequestDto());
        assertNotNull(rq);
    }

    @Test
    public void testBuildRequestForBranchContext() {
        when(session.getBranchContext()).thenReturn(prepareBranchContext());
        ClassifyInvolvedPartyRequest rq = requestBuilder.build(prepareRequestDto());
        assertNotNull(rq);
    }

    private ClassifyPartyRequestDTO prepareRequestDto() {
        ClassifyPartyRequestDTO reqDto = new ClassifyPartyRequestDTO();
        reqDto.setBirthCountry("Britain");

        LinkedHashSet<String> nationalities = new LinkedHashSet<String>();
        nationalities.add("British");
        reqDto.setNationalities(nationalities);

        LinkedHashSet<TaxResidencyDetails> taxResidencies = new LinkedHashSet<TaxResidencyDetails>();
        TaxResidencyDetails taxResidency = new TaxResidencyDetails();
        taxResidency.setTaxResidency("Hackney");
        taxResidencies.add(taxResidency);
        reqDto.setTaxResidencies(taxResidencies);
        return reqDto;
    }

    private Map<String, Object> prepareChannelIdMap() {
        Map<String, Object> channelIdMap = new HashMap<String, Object>();
        channelIdMap.put("LBG", "LBG");
        channelIdMap.put("HLX", "HLX");
        channelIdMap.put("BOS", "BOS");
        return channelIdMap;
    }

    private BranchContext prepareBranchContext() {
        BranchContext branchContext = new BranchContext();
        branchContext.setColleagueId("871515");
        branchContext.setDomain("TEST01GLOBAL");
        List<String> roleList = new LinkedList<String>();
        roleList.add("BranchAdmin");
        branchContext.setRoles(roleList);
        branchContext.setOriginatingSortCode("EC1Y4XX");
        branchContext.setTellerId("tellerId");
        return branchContext;
    }

    private UserContext prepareUserContext(String brand) {
        return new UserContext("871515", "192.168.1.1", sessionId, "1098", "1204629", brand, "chansecMode", "Chrome",
                "En", "inboxIdClient", "host");
    }

}
