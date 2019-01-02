
package com.lbg.ib.api.sales.dao.product.overdraft;

import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;

import java.math.BigInteger;
import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.dao.FoundationServerUtil;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.product.overdraft.OverdraftRequestDTO;
import com.lbg.ib.api.sales.dto.product.overdraft.OverdraftResponseDTO;
import com.lbg.ib.api.sales.soapapis.wsbridge.account.AccountPortType;
import com.lbg.ib.api.sales.soapapis.wsbridge.account.StB274AODGetInterestRates;
import com.lbg.ib.api.sales.soapapis.wsbridge.account.StB274BODGetInterestRates;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StError;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StHeader;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StODRates;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;

@Component
public class OverdraftDAOImpl implements OverdraftDAO {

    public static final String   CANNOT_CONNECT_TO_REMOTE_POINT = "cannotConnectToRemote";

    @Autowired
    private LoggerDAO            logger;

    @Autowired
    private SessionManagementDAO session;

    @Autowired
    private FoundationServerUtil foundationServerUtil;

    @Autowired
    private AccountPortType      accountPort;

    public DAOResponse<OverdraftResponseDTO> fetchOverdraftDetails(OverdraftRequestDTO request) {

        try {
            StB274BODGetInterestRates response = sendRequest(buildRequest(request));
            return handleOverdraftResponse(response);
        } catch (RemoteException e) {
            logger.logException(this.getClass(), e);
            return withError(new DAOError(CANNOT_CONNECT_TO_REMOTE_POINT,
                    "Cannot connect to remote location for ValidateUserID service"));
        }

    }

    private DAOResponse<OverdraftResponseDTO> handleOverdraftResponse(StB274BODGetInterestRates response) {
        StError sterror = response.getSterror();
        if (successfulResponse(sterror)) {
            return withResult(mapOverdraftResponse(response));
        } else {
            logger.logError(Integer.toString(sterror.getErrorno()), "Response has returend with error",
                    OverdraftDAOImpl.class);
            return withError(new DAOError(Integer.toString(sterror.getErrorno()), sterror.getErrormsg()));
        }
    }

    private boolean successfulResponse(StError sterror) {
        return sterror == null || sterror.getErrorno() == 0;
    }

    private StB274BODGetInterestRates sendRequest(StB274AODGetInterestRates buildRequest) throws RemoteException {
        return accountPort.b274ODGetInterestRates(buildRequest);
    }

    private StB274AODGetInterestRates buildRequest(OverdraftRequestDTO overdraftRequestDTO) {
        StB274AODGetInterestRates overdraftRequest = new StB274AODGetInterestRates();
        StHeader stHeader = foundationServerUtil.createStHeader(overdraftRequestDTO.getUserContext());// TODO
                                                                                                      // emove
                                                                                                      // refe
                                                                                                      // obj
        
        overdraftRequest.setStheader(stHeader);
        
        if (overdraftRequestDTO.getUserContext() != null) {

            BigInteger ocsId = new BigInteger(overdraftRequestDTO.getUserContext().getOcisId());
            overdraftRequest.getStheader().getStpartyObo().setOcisid(ocsId);
            String partyId=overdraftRequestDTO.getUserContext().getPartyId();
            overdraftRequest.getStheader().getStpartyObo().setPartyid(partyId);

        }
        overdraftRequest.setSortcode(overdraftRequestDTO.getSortcode());
        overdraftRequest.setAmtOverdraft(overdraftRequestDTO.getAmtOverdraft());
        overdraftRequest.setCbsprodnum(overdraftRequestDTO.getCbsprodnum());
        overdraftRequest.setCbstariff(overdraftRequestDTO.getCbstariff());
        return overdraftRequest;
    }

    private OverdraftResponseDTO mapOverdraftResponse(StB274BODGetInterestRates response) {
        OverdraftResponseDTO overdraftResponseDTO = new OverdraftResponseDTO();
        if (response != null) {
            StODRates overdraftRates = response.getStodrates();
            if (overdraftRates != null) {
                overdraftResponseDTO.setAmtIntFreeOverdraft(overdraftRates.getAmtIntFreeOverdraft());
                overdraftResponseDTO.setIntrateAuthEAR(overdraftRates.getIntrateAuthEAR());
                overdraftResponseDTO.setIntrateAuthMnthly(overdraftRates.getIntrateAuthMnthly());
                overdraftResponseDTO.setAmtUsageFeeOverdraft(overdraftRates.getAmtUsageFeeOverdraft());
                overdraftResponseDTO.setAmtExcessFee(overdraftRates.getAmtExcessFee());
                overdraftResponseDTO.setAmtExcessFeeBalIncr(overdraftRates.getAmtExcessFeeBalIncr());
                overdraftResponseDTO.setNExcessFeeCap(overdraftRates.getNExcessFeeCap());
                overdraftResponseDTO.setAmtTotalCreditCost(overdraftRates.getAmtTotalCreditCost());
                overdraftResponseDTO.setIntrateBase(overdraftRates.getIntrateBase());
                overdraftResponseDTO.setIntrateMarginOBR(overdraftRates.getIntrateMarginOBR());
                overdraftResponseDTO.setIntrateUnauthEAR(overdraftRates.getIntrateUnauthEAR());
                overdraftResponseDTO.setCurrencycode(overdraftRates.getCurrencycode());
                overdraftResponseDTO.setIntrateUnauthMnthly(overdraftRates.getIntrateUnauthMnthly());
                overdraftResponseDTO.setDateExpires(overdraftRates.getDateExpires());
                overdraftResponseDTO.setBTemporary(overdraftRates.isBTemporary());
            }

        }
        return overdraftResponseDTO;
    }

    public void setAccountPort(AccountPortType accountPort) {
        this.accountPort = accountPort;
    }

}
