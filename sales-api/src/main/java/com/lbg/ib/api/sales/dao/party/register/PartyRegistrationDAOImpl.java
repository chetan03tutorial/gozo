
package com.lbg.ib.api.sales.dao.party.register;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.sales.dao.FoundationServerUtil;
import com.lbg.ib.api.sales.dao.constants.CommonConstants;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.party.ChannelDTO;
import com.lbg.ib.api.sales.dto.party.PartyRequestDTO;
import com.lbg.ib.api.sales.dto.party.PartyResponseDTO;
import com.lbg.ib.api.sales.soapapis.wsbridge.application.ApplicationPortType;
import com.lbg.ib.api.sales.soapapis.wsbridge.application.StB801AResolveProdToPty;
import com.lbg.ib.api.sales.soapapis.wsbridge.application.StB801BResolveProdToPty;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StAccount;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StChannelDescription;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StError;
import com.lbg.ib.api.sales.soapapis.wsbridge.bapi.StParty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static com.lbg.ib.api.shared.util.logger.ServiceLogger.formatMessage;
import static java.lang.String.format;

@Component
public class PartyRegistrationDAOImpl implements PartyRegistrationDAO {

    private static final Class<PartyRegistrationDAOImpl> CLASS_NAME  = PartyRegistrationDAOImpl.class;

    private static final String                          METHOD_NAME = "retrievePartyMandate";

    @Autowired
    private ApplicationPortType                          applicationPortType;

    @Autowired
    private LoggerDAO                                    logger;

    @Autowired
    private FoundationServerUtil                         foundationServerUtil;

    @Autowired
    private SessionManagementDAO                         sessionManagementDAO;

    @Autowired
    private DAOExceptionHandler                          exceptionHandler;

    private StB801AResolveProdToPty creatResolveProdToPtyRequest(PartyRequestDTO partyRequestDTO) {

        StB801AResolveProdToPty request = new StB801AResolveProdToPty();

        StAccount stacc = new StAccount();
        stacc.setHost(CommonConstants.HOST);
        stacc.setProdtype(partyRequestDTO.getAccountType());
        request.setStacc(stacc);

        StParty stParty = new StParty();
        stParty.setHost(CommonConstants.HOST);
        stParty.setOcisid(new BigInteger(sessionManagementDAO.getUserContext().getOcisId()));
        stParty.setPartyid(partyRequestDTO.getPartyId());
        request.setStpartySalesUnauth(stParty);

        request.setPostcode(partyRequestDTO.getPostCode());
        request.setTitle(partyRequestDTO.getTitle());
        request.setFirstname(partyRequestDTO.getFirstName());
        request.setSurname(partyRequestDTO.getSurName());
        request.setDateOfBirth(partyRequestDTO.getDateOfBirth());

        request.setChanid(sessionManagementDAO.getUserContext().getChannelId());

        request.setStheader(foundationServerUtil.createStHeader(sessionManagementDAO.getUserContext()));
        return request;

    }

    @TraceLog
    public DAOResponse<PartyResponseDTO> retrievePartyMandate(PartyRequestDTO partyRequestDTO) {

        try {
            PartyResponseDTO responseDto = null;
            StB801AResolveProdToPty request = creatResolveProdToPtyRequest(partyRequestDTO);
            StB801BResolveProdToPty response = applicationPortType.b801ResolveProdToPty(request);
            StError sterror = response.getSterror();
            if (sterror != null) {
                logger.logError(Integer.toString(sterror.getErrorno()),
                        formatMessage(format("B801 Error Message: %s", sterror.getErrormsg()), "validate", responseDto),
                        this.getClass());
                return withError(new DAOError(String.valueOf(sterror.getErrorno()), sterror.getErrormsg()));
            }

            // Success response handling
            StChannelDescription[] stChannels = response.getStchan();
            if (stChannels == null) {
                return null;
            }
            List<ChannelDTO> channels = new ArrayList<ChannelDTO>();
            for (StChannelDescription stChannel : stChannels) {
                ChannelDTO channelDto = new ChannelDTO(stChannel.getChanid(), stChannel.getChandesc());
                channels.add(channelDto);
            }
            responseDto = new PartyResponseDTO(response.getPwdstate(), channels, true);
            return withResult(responseDto);
        } catch (Exception e) {
            DAOError daoError = exceptionHandler.handleException(e, CLASS_NAME, METHOD_NAME, partyRequestDTO);
            return withError(daoError);
        }
    }

}
