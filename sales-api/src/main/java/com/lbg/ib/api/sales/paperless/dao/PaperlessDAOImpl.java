/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.paperless.dao;

import java.math.BigInteger;
import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import com.lbg.ib.api.sso.domain.user.PrimaryInvolvedParty;
import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.dao.FoundationServerUtil;
import com.lbg.ib.api.sales.dao.constants.CommonConstants;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.paperless.dto.PersonalDetailsDTO;
import com.lbg.ib.api.sales.paperless.dto.UserMandateInfoDTO;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StError;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StHeader;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StParty;
import com.lbg.ib.api.sales.soapapis.wsbridge.user.StB298AUserSetPersDetails;
import com.lbg.ib.api.sales.soapapis.wsbridge.user.StB298BUserSetPersDetails;
import com.lbg.ib.api.sales.soapapis.wsbridge.user.StB818AUserInfoByOcisId;
import com.lbg.ib.api.sales.soapapis.wsbridge.user.StB818BUserInfoByOcisId;
import com.lbg.ib.api.sales.soapapis.wsbridge.user.UserPortType;

/**
 * Class for paperless API's
 * @author tkhann
 */
@Component
public class PaperlessDAOImpl implements PaperlessDAO {
    /**
     * Object of UserPortType.
     */
    @Autowired
    private UserPortType userPort;

    /**
     * Object of FoundationServerUtil.
     */
    @Autowired
    private FoundationServerUtil foundationServerUtil;
    /**
     * Object of SessionManagementDAO.
     */
    @Autowired
    private SessionManagementDAO sessionManagementDAO;
    /**
     * Object of DAOExceptionHandler.
     */
    @Autowired
    private DAOExceptionHandler exceptionHandler;

    private static final Class<PaperlessDAOImpl> CLASS_NAME = PaperlessDAOImpl.class;
    /**
     * Object of Galaxy Error Code.
     */
    @Autowired
    private GalaxyErrorCodeResolver resolver;

    /**
     * Object of Logger.
     */
    @Autowired
    private LoggerDAO logger;

    /*
     * (non-Javadoc)
     * @see com.lbg.ib.api.sales.paperless.dao.PaperlessDAO#getUserMandateData(java.
     * math.BigInteger)
     */
    @TraceLog
    public DAOResponse<UserMandateInfoDTO> getUserMandateData(BigInteger ocisId) {
        String methodName = "DAO: getUserMandateData";
        logger.traceLog(this.getClass(), methodName + "OcisId:" + ocisId);
        StB818AUserInfoByOcisId userInfoReq = new StB818AUserInfoByOcisId();
        UserMandateInfoDTO mandateInfoDTO = new UserMandateInfoDTO();
        UserContext clientContext = sessionManagementDAO.getUserContext();
        userInfoReq.setOcisid(ocisId);
        userInfoReq.setStheader(foundationServerUtil.createStHeader(clientContext));
        try {
            StB818BUserInfoByOcisId b818Resp = userPort.b818UserInfoByOcisId(userInfoReq);
            if (null == b818Resp) {
                throw new ServiceException(resolver.resolve(ResponseErrorConstants.SERVICE_UNAVAILABLE));
            } else if (null != b818Resp.getSterror() && b818Resp.getSterror().getErrorno() != 0) {
                StError sterror = b818Resp.getSterror();
                throw new ServiceException(
                        new ResponseError(Integer.toString(sterror.getErrorno()), sterror.getErrormsg()));
            }
            // update internal user id in the user context
            clientContext.setAuthorId(b818Resp.getUserid());
            mandateInfoDTO.setUserid(b818Resp.getUserid());
            mandateInfoDTO.setDateFirstLogon(b818Resp.getDateFirstLogon());
            mandateInfoDTO.setDateLastLogon(b818Resp.getDateLastLogon());
            mandateInfoDTO.setUserregstatecode(b818Resp.getUserregstatecode());
            mandateInfoDTO.setUserregstatedesc(b818Resp.getUserregstatedesc());
        } catch (RemoteException e) {
            DAOResponse.DAOError daoError = exceptionHandler.handleException(e, CLASS_NAME, methodName, ocisId);
            logger.logError(daoError.getErrorCode(), daoError.getErrorMessage(), this.getClass());
            throw new ServiceException(new ResponseError(daoError.getErrorCode(), daoError.getErrorMessage()));
        }
        return DAOResponse.withResult(mandateInfoDTO);
    }

    /*
     * Specific to update email but can be extended to update further details. (non-Javadoc)
     * @see com.lbg.ib.api.sales.paperless.dao.PaperlessDAO#getUserMandateData(java.
     * math.BigInteger)
     */
    @TraceLog
    public DAOResponse<String> updatePersonalDetails(PersonalDetailsDTO details) {
        String methodName = "DAO: updatePersonalDetails";
        logger.traceLog(this.getClass(), methodName + "OcisId:" + details.getOcisId());
        StB298AUserSetPersDetails userInfoReq = new StB298AUserSetPersDetails();
        UserContext clientContext = sessionManagementDAO.getUserContext();
        StHeader stHeader = foundationServerUtil.createStHeader(clientContext);
        stHeader.setUseridAuthor("AAGATEWAY");
        userInfoReq.setStheader(stHeader);
        userInfoReq.setBChangeEmail(details.isChangeEmail());
        userInfoReq.setUniqueid(BigInteger.valueOf(0));
        userInfoReq.setEmailaddr(details.getEmailaddr());
        userInfoReq.setMktgindEmail(details.getMktgindEmail());
        StParty stparty = new StParty();
        stparty.setHost(CommonConstants.HOST);
        stparty.setOcisid(new BigInteger(details.getOcisId()));
        stparty.setPartyid(details.getPartyId());
        userInfoReq.setStparty(stparty);
        try {
            StB298BUserSetPersDetails b298Resp = userPort.b298UserSetPersonalDetails(userInfoReq);
            if (null == b298Resp) {
                throw new ServiceException(resolver.resolve(ResponseErrorConstants.SERVICE_UNAVAILABLE));
            } else if (null != b298Resp.getSterror() && b298Resp.getSterror().getErrorno() != 0) {
                StError sterror = b298Resp.getSterror();
                throw new ServiceException(
                        new ResponseError(Integer.toString(sterror.getErrorno()), sterror.getErrormsg()));
            }
        } catch (RemoteException e) {
            DAOResponse.DAOError daoError = exceptionHandler.handleException(e, CLASS_NAME, methodName, details);
            logger.logError(daoError.getErrorCode(), daoError.getErrorMessage(), this.getClass());
            throw new ServiceException(new ResponseError(daoError.getErrorCode(), daoError.getErrorMessage()));
        }
        Arrangement arrangement = (Arrangement) sessionManagementDAO.getUserInfo();
        if (null != arrangement && null != arrangement.getPrimaryInvolvedParty()) {
            PrimaryInvolvedParty primaryInvolvedParty = arrangement.getPrimaryInvolvedParty();
            primaryInvolvedParty.setEmail(details.getEmailaddr());
        }
        return null;
    }
}
