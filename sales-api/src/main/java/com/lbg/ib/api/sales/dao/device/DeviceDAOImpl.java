package com.lbg.ib.api.sales.dao.device;

import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.device.DeviceDTO;
import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static com.lbg.ib.api.shared.util.logger.ServiceLogger.formatMessage;
import static java.lang.String.format;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
@Component
public class DeviceDAOImpl implements DeviceDAO {
    public static final String         NO_MARTCHING_URL       = "nomatchingurl";
    public static final String         NO_MARTCHING_TM_ORG_ID = "nomatchingtmorgid";
    public static final String         NO_SESSION_ID          = "nosessionid";
    public static final String         INVALID_SESSION_ID     = "invalidsessionid";
    private final ApiServiceProperties properties;
    private LoggerDAO                  logger;
    private SessionManagementDAO       sessionManagementDAO;

    @Autowired
    public DeviceDAOImpl(ApiServiceProperties properties, SessionManagementDAO sessionManagementDAO, LoggerDAO logger) {
        this.logger = logger;
        this.sessionManagementDAO = sessionManagementDAO;
        this.properties = properties;
    }

    public DAOResponse<DeviceDTO> getDevice(ChannelBrandDTO channelBrandDto) {
        DAOResponse<DeviceDTO> query = query(channelBrandDto);
        DAOError error = query.getError();
        if (error != null) {
            logger.logError(error.getErrorCode(), formatMessage(error.getErrorMessage(), "getService", channelBrandDto),
                    this.getClass());
        }
        return query;
    }

    private DAOResponse<DeviceDTO> query(ChannelBrandDTO channelBrandDto) {
        String sessionId = sessionManagementDAO.getSessionId();
        if (sessionId == null) {
            return withError(new DAOError(NO_SESSION_ID, "No session id found"));
        }
        if (sessionId.length() < 8) {
            return withError(new DAOError(INVALID_SESSION_ID,
                    format("'%s' is not a valid session id (less than 4 chars)", sessionId)));
        }
        String id = sessionId.substring(0, 8);
        String url = properties.getDeviceUrl(channelBrandDto.getBrand());
        if (url == null) {
            return withError(
                    new DAOError(NO_MARTCHING_URL, format("No matching url for key '%s'", channelBrandDto.getBrand())));
        }
        String org = properties.getThreatMatrixDetailsFromConfig("ThreatMetrixOrgID", channelBrandDto.getBrand());
        if (org == null) {
            return withError(new DAOError(NO_MARTCHING_TM_ORG_ID,
                    format("No matching threat metrix org id for key '%s'", channelBrandDto.getBrand())));
        }
        return withResult(new DeviceDTO(url, org, id));
    }
}
