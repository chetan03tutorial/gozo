package com.lbg.ib.api.sales.terminate.service;

import com.lbg.ib.api.sales.product.domain.domains.TerminateArrangementResponse;
import com.lbg.ib.api.sales.product.domain.domains.TerminateArrangement;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.dao.product.suspended.TerminateArrangementDAO;
import com.lbg.ib.api.sales.dto.product.pendingarrangement.TerminateArrangementDTO;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * @author Debashish Bhattacharjee
 * @version 1.0
 * @since 16thFeb2017
 ***********************************************************************/

@Component
public class TerminateArrangementServiceImpl implements TerminateArrangementService {
    
    private GalaxyErrorCodeResolver resolver;
    
    private LoggerDAO logger;
    
    private TerminateArrangementDAO terminateArrangementDAO;
    
    public TerminateArrangementServiceImpl(){
        //Empty Constructors to avoid voilations
    }
    
    @Autowired
    public TerminateArrangementServiceImpl(GalaxyErrorCodeResolver resolver, LoggerDAO logger,TerminateArrangementDAO terminateArrangementDAO) {
        this.resolver = resolver;
        this.logger = logger;
        this.terminateArrangementDAO = terminateArrangementDAO;
    }
    
    @TraceLog
    public TerminateArrangementResponse terminateArrangement(TerminateArrangement terminateArrangement) {
         DAOResponse<TerminateArrangementDTO> response = terminateArrangementDAO.terminateArrangement(terminateArrangement);
        
        if (response.getError() != null) {
            throw new ServiceException(
                    resolver.resolve(ResponseErrorConstants.TERMINDATE_APPLICATION_EXCEPTION));
        }
        
       return mapResponseToDTO(response);
    }
    
    public TerminateArrangementResponse mapResponseToDTO(DAOResponse<TerminateArrangementDTO> response){
        TerminateArrangementDTO terminateArrangementDTO = response.getResult(); 
        TerminateArrangementResponse terminateResponse = new TerminateArrangementResponse();
        terminateResponse.setArrangementId(terminateArrangementDTO.getArrangementId());
        return terminateResponse;
    }

    
}
