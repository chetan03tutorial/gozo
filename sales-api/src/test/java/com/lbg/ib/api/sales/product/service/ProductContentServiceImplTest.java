package com.lbg.ib.api.sales.product.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.product.util.ContentExecutor;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.service.reference.ReferenceDataServiceDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lloydstsb.ea.referencedata.ReferenceDataItem;
import com.lloydstsb.ea.referencedata.exceptions.ReferenceDataException;

@RunWith(MockitoJUnitRunner.class)
public class ProductContentServiceImplTest {

    @InjectMocks
    ProductContentServiceImpl productContentServiceImpl;

    private ReferenceDataServiceDAO                   referenceDataServiceDAO = mock(ReferenceDataServiceDAO.class);

    private ConfigurationDAO                          configuration           = mock(ConfigurationDAO.class);

    private ContentExecutor                           contentExecutor         = mock(ContentExecutor.class);

    private GalaxyErrorCodeResolver                    resolver = mock(GalaxyErrorCodeResolver.class);

    private ChannelBrandingDAO                        channelBrandingDAO      = mock(ChannelBrandingDAO.class);
    private static final String                       PRODUCT_GROUP_MNEMONICS = "ProductGroupMnemonics";
    private static final Map<String, Object>          mnemonicsMap            = getProductMnemonics();
    private static final DAOResponse<ChannelBrandDTO> channelBrandDTO         = getChannelBrandDTO();
    private static final List<ReferenceDataItem>      refItemList             = getRefItems();
    private static final Map<String, String>          productContentDtos      = getProductContentDTOs();

    @Mock
    private LoggerDAO                                 logger;

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAllProductContent() throws ReferenceDataException {
        when(configuration.getConfigurationItems(PRODUCT_GROUP_MNEMONICS)).thenReturn(mnemonicsMap);
        when(channelBrandingDAO.getChannelBrand()).thenReturn(channelBrandDTO);
        when(referenceDataServiceDAO.getProductMnemonics(any(List.class))).thenReturn(refItemList);
        when(contentExecutor.requestContent(Mockito.anyList(), Mockito.anyString(),Mockito.anyString()))
                .thenReturn(productContentDtos);
        when(productContentServiceImpl.getAllProductContent(Mockito.anyString())).thenReturn(productContentDtos);
        //Map<String, String> productContentDTOs = new ProductContentServiceImpl(referenceDataServiceDAO, configuration,
        //        contentExecutor, channelBrandingDAO, logger, resolver).getAllProductContent();

        Map<String, String> productContentDTOs = productContentServiceImpl.getAllProductContent("path");
        assertNotNull(productContentDTOs);
//        verify(contentExecutor, Mockito.times(1)).requestContent(refItemList, channelBrandDTO.getResult().getChannel());
    }

    private static Map<String, String> getProductContentDTOs() {
        Map<String, String> contentDTOs = new HashMap<String, String>();
        contentDTOs.put("test", "testContent");
        return contentDTOs;
    }

    private static List<ReferenceDataItem> getRefItems() {
        List<ReferenceDataItem> refItemList = new ArrayList<ReferenceDataItem>();
        refItemList.add(new ReferenceDataItem());
        return refItemList;
    }

    private static DAOResponse<ChannelBrandDTO> getChannelBrandDTO() {
        ChannelBrandDTO channelBrandDTO = new ChannelBrandDTO("test", "test", "test");
        DAOResponse<ChannelBrandDTO> daoResponse = DAOResponse.withResult(channelBrandDTO);
        return daoResponse;
    }

    private static Map<String, Object> getProductMnemonics() {
        Map<String, Object> mnemonicsMap = new HashMap<String, Object>();
        mnemonicsMap.put("test", "P_ESAVER");
        return mnemonicsMap;
    }

    @Test
    public void testGetGroupMnemonics() {
        when(configuration.getConfigurationItems(PRODUCT_GROUP_MNEMONICS)).thenReturn(mnemonicsMap);
        List<String> groupMnemonics = new ProductContentServiceImpl(referenceDataServiceDAO, configuration,
                contentExecutor, channelBrandingDAO, logger, resolver).getGroupMnemonics();
        assertTrue(groupMnemonics.size() == 1);
        assertTrue(groupMnemonics.contains("P_ESAVER"));
    }

}

