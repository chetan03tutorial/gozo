package com.lbg.ib.api.sales.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.sales.dao.content.ContentDAO;
import com.lbg.ib.api.sales.sortcode.service.SortCodeCacheServiceImpl;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;

@RunWith(MockitoJUnitRunner.class)
public class SortCodeCacheServiceImplTest {

	@Mock
	private ContentDAO contentDAO;

	@Mock
	private LoggerDAO logger;

	@InjectMocks
	private SortCodeCacheServiceImpl service;

	@Mock
	private ApiServiceProperties properties;

	@Test
	public void testGetLloydsSortCodemaperDefault() {
		service.getLloydsSortCodemaperDefault();

	}

	@Test
	public void testGetHalifaxSortCodemaperDefault() {
		service.getHalifaxSortCodemaperDefault();
	}

	@Test
	public void testGetBosSortCodemaperDefault() {
		service.getBosSortCodemaperDefault();
	}

	@Test
	public void testGetLloydsSortCodemaper() {
		service.getLloydsSortCodemaper();
	}

	@Test
	public void testGetHalifaxSortCodemaper() {
		service.getHalifaxSortCodemaper();
	}

	@Test
	public void testGetBosSortCodemaper() {
		service.getBosSortCodemaper();
	}

	@Test
	public void testSortCodeLoader() {

		Mockito.when(properties.getFilePath("", "")).thenReturn("");

		service.sortCodeLoader();
	}

}
