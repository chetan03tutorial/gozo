package com.lbg.ib.api.sales.content.service;

import javax.ws.rs.core.Response;

import com.lbg.ib.api.sales.dao.constants.CommonConstant;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.dao.content.ContentDAO;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
@Component
public class ContentServiceImpl implements ContentService {

    private ContentDAO              contentDAO;
    private ChannelBrandingDAO      channelBrandingDAO;
    private GalaxyErrorCodeResolver resolver;
    
    @Autowired
    private LoggerDAO logger;
    @Autowired
    private ConfigurationDAO configDAO;


    @Autowired
    public ContentServiceImpl(ContentDAO contentDAO, ChannelBrandingDAO channelBrandingDAO,
            GalaxyErrorCodeResolver resolver) {
        this.contentDAO = contentDAO;
        this.channelBrandingDAO = channelBrandingDAO;
        this.resolver = resolver;
    }

    @TraceLog
    public Response content(String key) throws ServiceException {
        DAOResponse<ChannelBrandDTO> response = channelBrandingDAO.getChannelBrand();
        handleErrorByThrowingServiceException(response);

        return fetchBrandedContent(response.getResult().getChannel(), key);
    }

    private Response fetchBrandedContent(String channel, String key) throws ServiceException {
        DAOResponse<Response> response = contentDAO.content(channel, key);
        handleErrorByThrowingServiceException(response);

        return response.getResult();
    }
    
    private Response fetchBrandedContentManager(String channel, String key) throws ServiceException {
        DAOResponse<Response> response = contentDAO.contentManager(channel, key);
        handleErrorByThrowingServiceException(response);

        return response.getResult();
    }

    private void handleErrorByThrowingServiceException(DAOResponse<?> response) throws ServiceException {
        if (response.getError() != null) {
            throw new ServiceException(resolver.resolve(response.getError().getErrorCode()));
        }
    }

    public Response contentManager(String path) throws ServiceException {
        DAOResponse<ChannelBrandDTO> response = channelBrandingDAO.getChannelBrand();
        handleErrorByThrowingServiceException(response);

        return fetchBrandedContentManager(response.getResult().getChannel(), path);
    }
    
    
    @TraceLog
    public <T> T genericContent(String key, Class<T> t) throws ServiceException {
        String contentContextUri = configDAO.
                getConfigurationStringValue(CommonConstant.CONTENT_SERVER_CONTEXT, CommonConstant.CONTENT_SERVER_CONTEXT_URI);
        return fetchGenericContent("lloyds.client.web.application.container.uri", contentContextUri.concat(key), t);
    }

    private <T> T fetchGenericContent(String tag, String key, Class<T> t) throws ServiceException {
        logger.traceLog(this.getClass(), ":::Going to fetch Generic Content for tag::: "+tag+": key:"+key);
        T response = contentDAO.getContent(tag, key, t);
        return response;
    }
    

}
