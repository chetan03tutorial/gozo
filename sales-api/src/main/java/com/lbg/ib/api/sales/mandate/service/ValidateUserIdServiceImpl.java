package com.lbg.ib.api.sales.mandate.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.mandate.ValidateUserIdDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.sales.dto.mandate.ValidateUserIdDTO;
import com.lbg.ib.api.sales.mandate.domain.UserIdValidated;
import com.lbg.ib.api.sales.mandate.domain.UserIdValidation;
import com.lbg.ib.api.sales.product.domain.ArrangeToActivateParameters;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
@Component
public class ValidateUserIdServiceImpl implements ValidateUserIdService {

    private final ValidateUserIdDAO       validateUserIdDAO;
    private ChannelBrandingDAO            channel;
    private SessionManagementDAO          session;
    private final GalaxyErrorCodeResolver resolver;
    private LoggerDAO                     logger;

    @Autowired
    public ValidateUserIdServiceImpl(ValidateUserIdDAO validateUserIdDAO, ChannelBrandingDAO channel,
            SessionManagementDAO session, GalaxyErrorCodeResolver resolver, LoggerDAO logger) {
        this.validateUserIdDAO = validateUserIdDAO;
        this.channel = channel;
        this.session = session;
        this.resolver = resolver;
        this.logger = logger;
    }

    @TraceLog
    public UserIdValidated validate(UserIdValidation validation) throws ServiceException {
        try {
            ArrangeToActivateParameters params = session.getArrangeToActivateParameters();
            DAOResponse<List<String>> validate = validateUserIdDAO.validate(dto(validation, params, channel));
            if (validate.getError() != null) {
                throw new ServiceException(resolver.resolve(validate.getError().getErrorCode()));
            }
            return validated(validate.getResult());
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            logger.logException(this.getClass(), e);
            throw new ServiceException(resolver.resolve(ResponseErrorConstants.UNDEFINED_ERROR));
        }
    }

    private UserIdValidated validated(List<String> result) {
        if (result == null || result.isEmpty()) {
            return new UserIdValidated(true, null);
        } else {
            return new UserIdValidated(false, pick3(result));
        }

    }

    private List<String> pick3(List<String> result) {
        if (result.size() < 3) {
            return result;
        } else {
            return result.subList(0, 3);
        }
    }

    private ValidateUserIdDTO dto(UserIdValidation validation, ArrangeToActivateParameters params,
            ChannelBrandingDAO channel) throws ServiceException {
        DAOResponse<ChannelBrandDTO> channelBrandingDAO = channel.getChannelBrand();
        if (channelBrandingDAO.getError() != null) {
            throw new ServiceException(resolver.resolve(channelBrandingDAO.getError().getErrorCode()));
        }
        return new ValidateUserIdDTO(params.getOcisId(), params.getPartyId(),
                channelBrandingDAO.getResult().getChannelId(), validation.getPassword(), validation.getUsername());
    }
}
