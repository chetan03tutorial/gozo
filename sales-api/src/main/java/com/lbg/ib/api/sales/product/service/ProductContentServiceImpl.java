package com.lbg.ib.api.sales.product.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.product.util.ContentExecutor;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.service.reference.ReferenceDataServiceDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lloydstsb.ea.referencedata.ReferenceDataItem;
import com.lloydstsb.ea.referencedata.exceptions.ReferenceDataException;

@Component
public class ProductContentServiceImpl implements ProductContentService {

    private static final String PRODUCT_GROUP_MNEMONICS = "ProductGroupMnemonics";

    private ReferenceDataServiceDAO referenceDataServiceDAO;

    private ConfigurationDAO configuration;

    private ContentExecutor contentExecutor;

    private ChannelBrandingDAO channelBrandingDAO;

    private LoggerDAO logger;

    private GalaxyErrorCodeResolver resolver;

    private Map<String, List<Object>> brandProductMap = new HashMap<String, List<Object>>();

    @Autowired
    public ProductContentServiceImpl(ReferenceDataServiceDAO referenceDataServiceDAO, ConfigurationDAO configuration,
            ContentExecutor contentExecutor, ChannelBrandingDAO channelBrandingDAO, LoggerDAO logger,
            GalaxyErrorCodeResolver resolver) {
        this.referenceDataServiceDAO = referenceDataServiceDAO;
        this.configuration = configuration;
        this.contentExecutor = contentExecutor;
        this.channelBrandingDAO = channelBrandingDAO;
        this.logger = logger;
        this.resolver = resolver;
    }

    /*
     * (non-Javadoc)
     * @see com.lbg.ib.api.pao.product.service.ProductContentService# getAllProductContent()
     */
    public Map<String, String> getAllProductContent(String path) throws ReferenceDataException {
        List<String> groupMnemonics = getGroupMnemonics();
        DAOResponse<ChannelBrandDTO> response = channelBrandingDAO.getChannelBrand();
        ChannelBrandDTO channelBrandDTO = response.getResult();
        String channelId = channelBrandDTO.getChannel();
        List<ReferenceDataItem> refDataItems = referenceDataServiceDAO.getProductMnemonics(groupMnemonics);
        logger.logDebug(this.getClass(), "Fetching Product Content Resource" + refDataItems, null);
        Map<String, String> allContentRepsone = new HashMap<String, String>();
        String brand = getBrand();
        if (!brandProductMap.containsKey(brand)) {
            Map<String, Object> map = configuration.getConfigurationItems(getBrand() + "_PRODUCTS_LIST");
            List<Object> mnemonics = new ArrayList<Object>(map.values());
            brandProductMap.put(brand, mnemonics);
        }
        List<Object> list = brandProductMap.get(brand);
        List<List<Object>> itemList = ListUtils.partition(list, 5);
        for (List<Object> refItemList : itemList) {
            Map<String, String> responseMap = contentExecutor.requestContent(refItemList, channelId, path);
            allContentRepsone.putAll(responseMap);
        }
        return allContentRepsone;
    }

    public String getBrand() throws ServiceException {
        DAOResponse<ChannelBrandDTO> brandDto = channelBrandingDAO.getChannelBrand();
        DAOResponse.DAOError error = brandDto.getError();
        if (error != null) {
            throw new ServiceException(resolver.resolve(error.getErrorCode()));
        }
        return brandDto.getResult().getBrand();
    }

    /**
     * Returns the list of group mnemonics that are configured for savings
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> getGroupMnemonics() {
        Map<String, Object> map = configuration.getConfigurationItems(PRODUCT_GROUP_MNEMONICS);
        List<Object> mnemonics = new ArrayList<Object>(map.values());
        CollectionUtils.filter(mnemonics, PredicateUtils.notNullPredicate());
        return (List<String>) (Object) mnemonics;
    }

}
