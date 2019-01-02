/**
 *
 */
package com.lbg.ib.api.sales.application.service;

import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.application.ApplicationResource;
import com.lbg.ib.api.sales.application.Base;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;

import java.util.Map;

@Component
public class ApplicationServiceImpl extends Base implements ApplicationService {

    private static final Class   CLASS_NAME = ApplicationResource.class;

    @Autowired
    private SessionManagementDAO sessionManagementDAO;

    @Autowired
    private ChannelBrandingDAO   channelBrandingDAO;

    @Autowired
    private ConfigurationDAO configurationDAO;

    public String getCurrentBrand() {
        String brand = channelBrandingDAO.getChannelBrand().getResult().getBrand();
        logger.traceLog(this.getClass(), "The Brand is " + brand);
        return brand;
    }

    public void clearPipeLineData() {
        sessionManagementDAO.clearSessionAttributeForPipelineChasing();

    }

    public String getReverseBrandName() {
        Map<String, Object> map = configurationDAO.getConfigurationItems("REVERSE_PRODUCT_BRAND");
        Object value = map.get(getCurrentBrand());
        if (value != null) {
            return (String) value;
        }
        return null;
    }

}
