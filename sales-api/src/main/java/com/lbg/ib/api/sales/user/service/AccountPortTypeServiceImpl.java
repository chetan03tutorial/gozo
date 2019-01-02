package com.lbg.ib.api.sales.user.service;

import static com.lbg.ib.api.sales.common.constant.ResponseErrorConstants.USER_NOT_FOUND;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.lbg.ib.api.shared.validation.AccountType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.dao.FoundationServerUtil;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.shared.service.SoaAbstractService;
import com.lbg.ib.api.sales.shared.validator.business.user.UserInfoValidator;
import com.lbg.ib.api.sales.soapapis.wsbridge.account.AccountPortType;
import com.lbg.ib.api.sales.soapapis.wsbridge.account.StB277AAccPartiesReadJP;
import com.lbg.ib.api.sales.soapapis.wsbridge.account.StB277BAccPartiesReadJP;
import com.lbg.ib.api.sales.soapapis.wsbridge.account.TAccPartyOtherJP;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StAccount;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StError;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StHeader;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import com.lbg.ib.api.sso.domain.user.UserContext;

/**
 * Implementation of Account details.
 * @author tkhann
 */
@Component
public class AccountPortTypeServiceImpl extends SoaAbstractService implements AccountPortTypeService {
    /**
     * Method to invoke class.
     */
    @Override
    public Class<AccountPortType> getPort() {
        return AccountPortType.class;
    }

    /**
     * Object of foundationServerUtil.
     */
    @Autowired
    private FoundationServerUtil foundationServerUtil;
    /**
     * Object of session management.
     */
    @Autowired
    private SessionManagementDAO session;
    /**
     * Object of resolver.
     */
    @Autowired
    private GalaxyErrorCodeResolver resolver;
    /**
     * Object of logger.
     */
    @Autowired
    private LoggerDAO logger;

    /**
     * Constant
     */
    private static final String END = "End";

    /*
     * (non-Javadoc)
     * @see
     * com.lbg.ib.api.sales.user.service.AccountPortTypeService#retrieveJointPartyDetails(com.lbg
     * .ib.api.sales.user.domain.Account)
     */
    @TraceLog
    public List<String> retrieveJointPartyOcisIds(Account account) {
        String methodName = "retrieveJointPartyDetails";
        Arrangement arrangement = (Arrangement) session.getUserInfo();
        List<String> otherPartiesOcisIds;
        if (null != arrangement) {
            logger.traceLog(this.getClass(), methodName + "OcisId:" + arrangement.getOcisId());
            BigInteger ocisId = UserInfoValidator.validateOcisId(arrangement.getOcisId());
            UserInfoValidator.validateJointAccountReq(account);
            StB277AAccPartiesReadJP accPartiesReadJP = buildRequest(arrangement.getPartyId(), ocisId, account);
            StB277BAccPartiesReadJP b277bAccPartiesReadJP = (StB277BAccPartiesReadJP) invoke("b277AccPartiesReadListOtherJP", accPartiesReadJP);
            StError sterror = b277bAccPartiesReadJP.getSterror();
            if (sterror.getErrorno() != 0) {
                throw new ServiceException(
                        new ResponseError(Integer.toString(sterror.getErrorno()), sterror.getErrormsg()));
            }
            otherPartiesOcisIds = extractResponse(b277bAccPartiesReadJP, arrangement);
        } else {
            throw new ServiceException(resolver.resolve(USER_NOT_FOUND));
        }
        logger.traceLog(this.getClass(), methodName + END);
        return otherPartiesOcisIds;
    }

    /**
     * Extract BAPI response in object.
     * @param b277Resp
     * @return PartyDetails
     */
    private List<String> extractResponse(StB277BAccPartiesReadJP b277Resp, Arrangement arrangement) {
        List<String> osicIds = new ArrayList<String>();

        if (null != b277Resp.getAstaccpartyOtherJP()) {
            for (TAccPartyOtherJP otherPartyDetails : b277Resp.getAstaccpartyOtherJP()) {
                BigInteger otherPartyOsicId = otherPartyDetails.getStparty().getOcisid();
                if (otherPartyOsicId != null) {
                    osicIds.add(otherPartyOsicId.toString());
                }
            }
        }
        return osicIds;
    }

    /**
     * Prepare request for B277
     * @param partyId
     * @param ocisId
     * @param account
     * @return StB277AAccPartiesReadJP
     */
    private StB277AAccPartiesReadJP buildRequest(String partyId, BigInteger ocisId, Account account) {
        String methodName = "buildRequest";
        logger.traceLog(this.getClass(), methodName + "OcisId:" + ocisId);
        StB277AAccPartiesReadJP accPartiesReadJP = new StB277AAccPartiesReadJP();
        UserContext clientContext = session.getUserContext();
        StHeader stHeader = foundationServerUtil.createStHeader(clientContext);
        stHeader.setUseridAuthor("AAGATEWAY");
        accPartiesReadJP.setStheader(stHeader);
        accPartiesReadJP.getStheader().getStpartyObo().setPartyid(partyId);
        accPartiesReadJP.getStheader().getStpartyObo().setOcisid(ocisId);
        StAccount stacc = new StAccount();
        stacc.setProdtype("A");
        stacc.setHost("T");
        stacc.setAccno(account.getAccountNumber());
        stacc.setSortcode(account.getSortCode());
        accPartiesReadJP.setStacc(stacc);
        logger.traceLog(this.getClass(), methodName + END);
        return accPartiesReadJP;
    }

}
