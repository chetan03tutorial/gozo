package com.lbg.ib.api.sales.common.rest.util;

import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OcisRestHeaderUtilTest {

	@InjectMocks
	private OcisRestHeaderUtil ocisHeaderUtility;

	@Mock
	private ConfigurationDAO configurationService;

	@Mock
	private SessionManagementDAO sessionService;

	private static Map<String, Object> headerMap;
	private static BranchContext branchContext;
	private static final String colleagueId = "CT145639";

	@Before
	public void setup() {
		headerMap = new HashMap<String, Object>();
		headerMap.put("x-lbg-user-id", "19");

		branchContext = new BranchContext();
		branchContext.setColleagueId(colleagueId);

		when(sessionService.getBranchContext()).thenReturn(branchContext);
		when(configurationService.getConfigurationItems(anyString())).thenReturn(headerMap);

		//when(ocisHeaderUtility.getSalsaHeader()).thenReturn(headerMap);
	}
	
	@Test
	public void testOcisHeaderGeneration(){
		Map<String, Object> headerData = ocisHeaderUtility.getSalsaHeader();
		String colleagueIdentifier = (String)headerData.get("X-LBG-USER-ID");
		assertTrue(colleagueId.equalsIgnoreCase(colleagueIdentifier));
	}
}
