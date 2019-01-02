/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.cache;

import static com.lbg.ib.api.sales.dao.constants.CommonConstant.KEY_USER_CONTEXT;
import static com.lbg.ib.api.sales.dao.constants.CommonConstant.SESSION_KEY_FOR_COMMUNICATION_PARTY_DETAILS;
import static com.lbg.ib.api.sales.dao.constants.CommonConstant.SESSION_KEY_FOR_PRODUCT_DETAILS;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.lbg.ib.api.sales.common.SpringContextHolder;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.communication.service.CommunicationPartyService;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.ApplicationSessionManagement;
import com.lbg.ib.api.sales.dto.communicationparty.CommunicationPartyDetailsDTO;
import com.lbg.ib.api.sales.product.domain.SelectedProduct;

public class SessionExpiryListener implements HttpSessionListener {

    private static final String CLASSNAME  = SessionExpiryListener.class.getName();

    private static final String ERROR_CODE = "1100017";

    /**
     * Session has just been created.
     *
     * @param anEvent
     *            - Reference to http session.
     */
    public void sessionCreated(HttpSessionEvent anEvent) {
        // Session created - nothing to do

    }

    /**
     * sessionDestroyed.
     *
     * @param anEvent
     *            - Reference to http session.
     */
    @TraceLog
    public void sessionDestroyed(HttpSessionEvent anEvent) {
        LoggerDAO logger = SpringContextHolder.getBean(LoggerDAO.class);

        HttpSession session = anEvent.getSession();
        if (session != null) {
            sendPipelineChaserEmail(session, logger);
        } else {
            logger.logError(ERROR_CODE, "Session is not present", SessionExpiryListener.class);
        }

    }

    /**
     * @param sendPipelineChaserEmail
     *            method-- This method gets the customer details and product
     *            details from session and calls the sendCommunicationWPS
     *            service to send a mail.
     */
    private void sendPipelineChaserEmail(HttpSession session, LoggerDAO logger) {
        CommunicationPartyService communicationPartService = SpringContextHolder
                .getBean(CommunicationPartyService.class);
        CommunicationPartyDetailsDTO partyDetails = (CommunicationPartyDetailsDTO) session.getAttribute(
                ApplicationSessionManagement.buildNameSpaceKey(SESSION_KEY_FOR_COMMUNICATION_PARTY_DETAILS));
        SelectedProduct product = (SelectedProduct) session
                .getAttribute(ApplicationSessionManagement.buildNameSpaceKey(SESSION_KEY_FOR_PRODUCT_DETAILS));
        UserContext userContext = (UserContext) session
                .getAttribute(ApplicationSessionManagement.buildNameSpaceKey(KEY_USER_CONTEXT));
        if (partyDetails != null && product != null && userContext != null) {
            communicationPartService.sendEmailCommunictaion(partyDetails, product.getMnemonic(), userContext);

        } else {
            logger.logError(ERROR_CODE,
                    "The session attribute values are null " + "partyDetails is null : " + (partyDetails != null)
                            + "product is null : " + (product != null) + "userContext is null : "
                            + (userContext != null),
                    SessionExpiryListener.class);
        }
    }
}
