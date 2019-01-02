package com.lbg.ib.api.sales.product.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;

@RunWith(MockitoJUnitRunner.class)
public class ActivateSortCodeServiceImplTest {
	
	
	@Mock
	private ChannelBrandingDAO channelBrandingService;

	@Mock
	private LoggerDAO logger;
	
	@InjectMocks
	private ActivateSortCodeServiceImpl activateSortCodeServiceImpl;

	@Test
	public void testGetBrand() {
		DAOResponse<ChannelBrandDTO> channel = null;
		
		Mockito.when(channelBrandingService.getChannelBrand()).thenReturn(channel);
		
		
		activateSortCodeServiceImpl.getBrand();
	}

	@Test
	public void testGetSortCodeByTown() {
		activateSortCodeServiceImpl.getSortCodeByTown(null, null);
	}

}
