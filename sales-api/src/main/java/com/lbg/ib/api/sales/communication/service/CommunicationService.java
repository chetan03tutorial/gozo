package com.lbg.ib.api.sales.communication.service;

import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.communication.dto.PartyCommunicationDetails;

/**
 * Created by rabaja on 16/11/2016.
 */
public interface CommunicationService {

    Boolean sendEmailCommunication(PartyCommunicationDetails communicationDetails) throws ServiceException;
}
