package com.lbg.ib.api.sales.user.service;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.dao.FoundationServerUtil;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.paperless.dto.Account;
import com.lbg.ib.api.sales.shared.service.SoaAbstractService;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StAccountListDetail;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StError;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StHeader;
import com.lbg.ib.api.sales.soapapis.wsbridge.user.StB162AUserAccReadList;
import com.lbg.ib.api.sales.soapapis.wsbridge.user.StB162BUserAccReadList;
import com.lbg.ib.api.sales.soapapis.wsbridge.user.UserPortType;
import com.lbg.ib.api.sales.user.mapper.UserProductHoldingMessageMapper;

@Component
public class UserProductHoldingServiceImpl extends SoaAbstractService implements UserProductHoldingService {

    @Autowired
    private FoundationServerUtil    foundationServerUtil;

    @Autowired
    private SessionManagementDAO    sessionManagementDAO;

    @Autowired
    private GalaxyErrorCodeResolver resolver;

    @Override
    public Class<UserPortType> getPort() {
        return UserPortType.class;
    }

    public List<Account> retrieveProductHolding(String partyId, BigInteger ocisId) {
        List<StAccountListDetail> accountList = doFetchRecurrsively(buildRequest(partyId, ocisId));
        if (accountList.isEmpty()) {
            ResponseError responseError = resolver.resolve(ResponseErrorConstants.PRODUCT_NOT_FOUND);
            throw new ServiceException(responseError);
        }
        return UserProductHoldingMessageMapper.buildResponse(accountList);
    }

    public List<StAccountListDetail> doFetchRecurrsively(StB162AUserAccReadList request) {
        List<StAccountListDetail> accountList = new LinkedList<StAccountListDetail>();
        StB162BUserAccReadList b162Resp = (StB162BUserAccReadList) invoke("b162UserAccReadList", request);
        StError sterror = b162Resp.getSterror();
        if (sterror.getErrorno() != 0) {
            throw new ServiceException(
                    new ResponseError(Integer.toString(sterror.getErrorno()), sterror.getErrormsg()));
        }
        accountList.addAll(extractResponse(b162Resp));
        String moreResult = b162Resp.getMoreind();
        if ("Y".equalsIgnoreCase(moreResult)) {
            request.setAccmorekey(b162Resp.getAccmorekey());
            accountList.addAll(doFetchRecurrsively(request));
        }
        return accountList;
    }

    public List<StAccountListDetail> extractResponse(StB162BUserAccReadList b162Resp) {
        List<StAccountListDetail> paperlessAccountList = new LinkedList<StAccountListDetail>();
        if (null != b162Resp.getAstacclistdetail()) {
            for (StAccountListDetail productDetail : b162Resp.getAstacclistdetail()) {
                paperlessAccountList.add(productDetail);
            }
        }
        return paperlessAccountList;
    }

    public StB162AUserAccReadList buildRequest(String partyId, BigInteger ocisId) {
        StB162AUserAccReadList userAccReadList = new StB162AUserAccReadList();
        UserContext clientContext = sessionManagementDAO.getUserContext();
        StHeader stHeader = foundationServerUtil.createStHeader(clientContext);
        stHeader.setUseridAuthor("AAGATEWAY");
        userAccReadList.setStheader(stHeader);
        userAccReadList.getStheader().getStpartyObo().setPartyid(partyId);
        userAccReadList.getStheader().getStpartyObo().setOcisid(ocisId);
        userAccReadList.setAccmorekey(new BigInteger("0"));
        userAccReadList.setBForceHostCall(true);
        return userAccReadList;
    }

}
