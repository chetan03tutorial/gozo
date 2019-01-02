/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.dao.mandate;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.sales.dao.FoundationServerUtil;
import com.lbg.ib.api.sales.dao.constants.CommonConstants;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.mandate.ValidateUserIdDTO;
import com.lbg.ib.api.sales.soapapis.wsbridge.application.ApplicationPortType;
import com.lbg.ib.api.sales.soapapis.wsbridge.application.StB802AUseridValidate;
import com.lbg.ib.api.sales.soapapis.wsbridge.application.StB802BUseridValidate;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StError;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StParty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static com.lbg.ib.api.shared.util.logger.ServiceLogger.formatMessage;
import static java.lang.String.format;

@Component
public class ValidateUserIdDAOImpl implements ValidateUserIdDAO {

    @Autowired
    private ApplicationPortType  applicationPort;

    @Autowired
    private LoggerDAO            logger;

    @Autowired
    private FoundationServerUtil foundationServerUtil;

    @Autowired
    private SessionManagementDAO sessionManagementDAO;

    public static final String   CANNOT_CONNECT_TO_REMOTE_POINT = "cannotConnectToRemote";

    public static final String   GENERAL_EXCEPTION              = "generalExceptionOccured";

    @TraceLog
    public DAOResponse<List<String>> validate(ValidateUserIdDTO dto) {
        try {
            StB802BUseridValidate response = sendRequest(buildRequest(dto));
            StError sterror = response.getSterror();
            if (successfulResponse(sterror)) {
                return withResult(suggestions(response.getAstpartyUseridSuggested()));
            } else {
                logger.logError(Integer.toString(sterror.getErrorno()),
                        formatMessage(format("For %s, error message: %s", dto, sterror.getErrormsg()), "validate", dto),
                        this.getClass());
                return withError(new DAOError(Integer.toString(sterror.getErrorno()), sterror.getErrormsg()));
            }
        } catch (RemoteException e) {
            logger.logException(this.getClass(), e);
            return withError(new DAOError(CANNOT_CONNECT_TO_REMOTE_POINT,
                    "Cannot connect to remote location for ValidateUserID service"));
        } catch (Exception e) {
            logger.logException(this.getClass(), e);
            return withError(new DAOError(GENERAL_EXCEPTION, "General Exception Occured for ValidateUserID service"));
        }
    }

    private boolean successfulResponse(StError sterror) {
        return sterror == null || sterror.getErrorno() == 0;
    }

    private List<String> suggestions(StParty[] suggestions) {
        List<String> suggestedNames = new ArrayList<String>();
        if (suggestions == null) {
            return suggestedNames;
        }
        for (StParty suggestion : suggestions) {
            suggestedNames.add(suggestion.getPartyid());
        }
        return suggestedNames;
    }

    private StB802BUseridValidate sendRequest(StB802AUseridValidate request) throws RemoteException {
        return applicationPort.b802UseridValidate(request);
    }

    @TraceLog
    private StB802AUseridValidate buildRequest(ValidateUserIdDTO dto) {
        StB802AUseridValidate request = new StB802AUseridValidate();
        request.setChanid(dto.getChannelId());
        request.setStheader(foundationServerUtil.createStHeader(sessionManagementDAO.getUserContext()));
        request.setBConnectedMandate(false);
        request.setStpartyResolved(setUserIdentifier(dto));
        request.setStpartyUseridEntered(setUserDetails(dto));
        if (dto.getPassword() != null) {
            request.setPasswordClear(dto.getPassword());
        }
        return request;
    }

    private StParty setUserDetails(ValidateUserIdDTO dto) {
        // TODO Auto-generated method stub
        StParty userId = new StParty();
        userId.setHost(CommonConstants.HOST);
        userId.setPartyid(dto.getUsername());
        userId.setOcisid(new BigInteger(dto.getOcisId().trim()));
        return userId;
    }

    private StParty setUserIdentifier(ValidateUserIdDTO dto) {
        StParty userId = new StParty();
        userId.setHost(CommonConstants.HOST);
        userId.setOcisid(new BigInteger(dto.getOcisId().trim()));
        userId.setPartyid(dto.getPartyId().trim());
        return userId;
    }

    public void setValidateService(ApplicationPortType applicationPort) {
        this.applicationPort = applicationPort;
    }
}
