package com.lbg.ib.api.sales.communication.dao;

import com.lbg.ib.api.sales.communication.dto.CommunicationRequestDTO;
import com.lbg.ib.api.sales.communication.dto.CommunicationResponseDTO;
import com.lbg.ib.api.shared.domain.DAOResponse;

/**
 * Created by rabaja on 16/11/2016.
 */
public interface CommunicationDAO {

    DAOResponse<CommunicationResponseDTO> sendEmailCommunictaion(CommunicationRequestDTO sendCommunication);
}