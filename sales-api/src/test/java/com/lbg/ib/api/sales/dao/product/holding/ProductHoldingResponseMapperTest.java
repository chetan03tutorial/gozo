package com.lbg.ib.api.sales.dao.product.holding;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.mapper.ProductHoldingResponseMapper;
import com.lbg.ib.api.sales.dao.product.holding.domain.Arrangement;
import com.lbg.ib.api.sales.dao.product.holding.domain.ArrangementAdditionalDetails;
import com.lbg.ib.api.sso.domain.product.Category;
import com.lbg.ib.api.sales.dao.product.holding.domain.Customer;
import com.lbg.ib.api.sso.domain.product.Event;
import com.lbg.ib.api.sso.domain.product.Indicator;
import com.lbg.ib.api.sales.dao.product.holding.domain.ProductArrangementLifecycleStatus;
import com.lbg.ib.api.sales.dao.product.holding.domain.ProductHolding;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
public class ProductHoldingResponseMapperTest {

    private LoggerDAO                    logger        = mock(LoggerDAO.class);
    private ConfigurationDAO             configuration = mock(ConfigurationDAO.class);
    private ProductHolding               productHolding;
    private ProductHoldingResponseMapper responseMapper;
    private Map<String, Object>          remittanceEvents;

    @Before
    public void setUp() {
        populateRemittanceEvents();
        populateProductHolding();
        responseMapper = new ProductHoldingResponseMapper();
        responseMapper.setLoggerDAO(logger);
        responseMapper.setConfiguration(configuration);
    }

    @Test
    public void shouldResponseWithProductHolding() throws Exception {
        when(configuration.getConfigurationItems(any(String.class))).thenReturn(remittanceEvents);
        com.lbg.ib.api.sales.product.domain.ProductHolding products = responseMapper
                .populateProductHoldingResponse(productHolding);
        assertTrue("123".equalsIgnoreCase(products.getProducts().get(0).getAccountNumber()));
    }

    @Test
    public void shouldResponseWithProductHoldingForDormant() throws Exception {
        productHolding = populateProductHoldingForDormant();
        when(configuration.getConfigurationItems(any(String.class))).thenReturn(remittanceEvents);
        com.lbg.ib.api.sales.product.domain.ProductHolding products = responseMapper
                .populateProductHoldingResponse(productHolding);
        assertTrue("123".equalsIgnoreCase(products.getProducts().get(0).getAccountNumber()));
    }

    private ProductHolding populateProductHolding() {
        productHolding = new ProductHolding();
        Arrangement arrangement = new Arrangement();
        arrangement.setAccountNumber("123");
        arrangement.setAccountName("Club Saver");
        arrangement.setAccountOpenedDate("Mon Oct 10 00:00:00 BST 2014");
        arrangement.setSortCode("123");
        arrangement.setAccountType("T12346");
        Event event1 = new Event();
        event1.setEventId("25");
        Event event2 = new Event();
        event2.setEventId("24");
        List<Event> events = new ArrayList<Event>();
        events.add(event1);
        events.add(event2);
        arrangement.setEvents(events);
        arrangement.setProductArrangementLifecycleStatus(ProductArrangementLifecycleStatus.O);
        ArrangementAdditionalDetails additionalDetails = new ArrangementAdditionalDetails();
        additionalDetails.setLifecycleStatus("Effective");
        additionalDetails.setAccGroupType("C");
        List<Indicator> indicators = new ArrayList<Indicator>();
        Indicator indicator = new Indicator();
        indicator.setIndicatorCode("665");
        indicators.add(indicator);
        additionalDetails.setIndicators(indicators);
        arrangement.setArrangementDetail(additionalDetails);
        Customer customer = new Customer();
        List<Category> categories = new ArrayList<Category>();
        Category category = new Category();
        category.setCategoryId("467");
        categories.add(category);
        customer.setCategories(categories);
        customer.setHost("T");
        customer.setLastLoginDate("Mon Oct 10 00:00:00 BST 2016");
        List<Arrangement> arrangements = new ArrayList<Arrangement>();
        arrangements.add(arrangement);
        productHolding.setArrangements(arrangements);
        productHolding.setCustomer(customer);
        return productHolding;
    }

    private ProductHolding populateProductHoldingForDormant() {
        productHolding = new ProductHolding();
        Arrangement arrangement = new Arrangement();
        arrangement.setAccountNumber("123");
        arrangement.setAccountName("Club Saver");
        arrangement.setAccountOpenedDate("-10-10");
        arrangement.setSortCode("123");
        Event event1 = new Event();
        event1.setEventId("25");
        Event event2 = new Event();
        event2.setEventId("24");
        List<Event> events = new ArrayList<Event>();
        events.add(event1);
        events.add(event2);
        arrangement.setEvents(events);
        ArrangementAdditionalDetails additionalDetails = new ArrangementAdditionalDetails();
        additionalDetails.setLifecycleStatus("Dormant");
        additionalDetails.setAccGroupType("C");
        List<Indicator> indicators = new ArrayList<Indicator>();
        Indicator indicator = new Indicator();
        indicator.setIndicatorCode("665");
        indicators.add(indicator);
        additionalDetails.setIndicators(indicators);
        arrangement.setArrangementDetail(additionalDetails);
        Customer customer = new Customer();
        List<Category> categories = new ArrayList<Category>();
        Category category = new Category();
        category.setCategoryId("467");
        categories.add(category);
        customer.setCategories(categories);
        customer.setLastLoginDate("Mon Oct 10 00:00:00 BST 2016");
        List<Arrangement> arrangements = new ArrayList<Arrangement>();
        arrangements.add(arrangement);
        productHolding.setArrangements(arrangements);
        productHolding.setCustomer(customer);
        return productHolding;
    }

    private Map<String, Object> populateRemittanceEvents() {
        remittanceEvents = new HashMap<String, Object>();
        remittanceEvents.put("C", "25");
        return remittanceEvents;
    }

}