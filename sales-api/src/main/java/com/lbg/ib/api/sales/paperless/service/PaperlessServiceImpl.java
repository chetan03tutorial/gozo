/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.paperless.service;

import java.math.BigInteger;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.paperless.dao.PaperlessDAO;
import com.lbg.ib.api.sales.paperless.dto.PersonalDetails;
import com.lbg.ib.api.sales.paperless.dto.PersonalDetailsDTO;
import com.lbg.ib.api.sales.paperless.dto.UserMandateInfoDTO;
import com.lbg.ib.api.sales.paperless.dto.UserMandateInfoResult;

/**
 * Service layer for Paperless API's
 * @author tkhann
 */
@Component
public class PaperlessServiceImpl implements PaperlessService {
    /**
     * Object of PaperlessDAO
     */
    @Autowired
    private PaperlessDAO paperlessDAO;
    /**
     * Constant
     */
    private static final String END = "End";
    /**
     * Logger Object
     */
    @Autowired
    private LoggerDAO logger;

    /*
     * (non-Javadoc)
     * @see com.lbg.ib.api.sales.paperless.service.PaperlessService#
     * getUserMandateInfo(java.lang.String, java.lang.String)
     */
    @TraceLog
    public UserMandateInfoResult getUserMandateInfo(String ocisId) {
        String methodName = "getUserMandateInfo";
        logger.traceLog(this.getClass(), methodName + "OcisId:" + ocisId);
        DAOResponse<UserMandateInfoDTO> userMandateInfoDTO = paperlessDAO
                .getUserMandateData(new BigInteger(ocisId));
        UserMandateInfoResult result = new UserMandateInfoResult();
        if (null != userMandateInfoDTO
                && null != userMandateInfoDTO.getResult()) {
            if (null != userMandateInfoDTO.getResult().getDateFirstLogon()) {
                result.setDateFirstLogon(userMandateInfoDTO.getResult()
                        .getDateFirstLogon().getTime().toString());
            }
            if (null != userMandateInfoDTO.getResult().getDateLastLogon()) {
                result.setDateLastLogon(userMandateInfoDTO.getResult()
                        .getDateLastLogon().getTime().toString());
            }
            result.setUserRegStateCode(userMandateInfoDTO.getResult()
                    .getUserregstatecode());
            result.setUserRegStateDesc(userMandateInfoDTO.getResult()
                    .getUserregstatedesc());
            result.setLastLoginInMins(minsBetween(userMandateInfoDTO
                    .getResult().getDateLastLogon(), Calendar.getInstance()));
        }
        logger.traceLog(this.getClass(), methodName + END);
        return result;
    }

    /*
     * (non-Javadoc)
     * @see com.lbg.ib.api.sales.paperless.service.PaperlessService#
     * getUserMandateInfo(java.lang.String, java.lang.String)
     */
    @TraceLog
    public void updateEmail(PersonalDetails personalDetails, String ocisId,
            String partyId) {
        String methodName = "updateEmail";
        logger.traceLog(this.getClass(), methodName + "OcisId:" + ocisId);
        if (null != personalDetails.getEmailAddress()
                && !StringUtils.isEmpty(personalDetails.getEmailAddress())) {
            PersonalDetailsDTO details = new PersonalDetailsDTO();
            details.setEmailaddr(personalDetails.getEmailAddress());
            details.setChangeEmail(Boolean.TRUE);
            details.setMktgindEmail(1);
            details.setOcisId(ocisId);
            details.setPartyId(partyId.trim());
            paperlessDAO.updatePersonalDetails(details);
        }
        logger.traceLog(this.getClass(), methodName + END);
    }

    /**
     * Method to calculate total no of minutes in two dates.
     * @param startCal Calendar
     * @param endCal Calendar
     * @return long
     */
    private long minsBetween(Calendar startCal, Calendar endCal) {
        long totalMin = 0;
        if (null != startCal && null != endCal) {
            DateTime dt1 = new DateTime(startCal.getTime());
            DateTime dt2 = new DateTime(endCal.getTime());
            totalMin = Minutes.minutesBetween(dt1, dt2).getMinutes();
        }
        return totalMin;
    }
}
