package com.lbg.ib.api.sales.product.service;

import com.lbg.ib.api.sales.dao.product.modify.ModifyProductArrangementDAO;
import com.lbg.ib.api.sales.product.domain.pending.ModifyProductArrangement;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * @author Debashish Bhattacharjee
 * @version 1.0
 * @since 4thApril2017
 ***********************************************************************/
@Component
public class ModifyProductArrangementServiceImpl implements ModifyProductArrangementService {
    
    private ModifyProductArrangementDAO modifyProductArrangementDAO;
    private GalaxyErrorCodeResolver resolver;
    private LoggerDAO logger;
    
    @Autowired
    public ModifyProductArrangementServiceImpl(ModifyProductArrangementDAO modifyProductArrangementDAO,
            GalaxyErrorCodeResolver resolver, LoggerDAO logger) {
        this.modifyProductArrangementDAO = modifyProductArrangementDAO;
        this.resolver = resolver;
        this.logger = logger;
    }
    
    @TraceLog
    public ModifyProductArrangement modifyProductArrangement(ModifyProductArrangement modifyProductArrangement)
            throws ServiceException {
        DAOResponse<ModifyProductArrangement> modifyProductResponse = modifyProductArrangementDAO
                .modifyProductArrangement(modifyProductArrangement);
        if (modifyProductResponse.getError() != null) {
            throw new ServiceException(resolver.customResolve(modifyProductResponse.getError().getErrorCode(),
                    modifyProductResponse.getError().getErrorMessage()));
        }
        return modifyProductResponse.getResult();
    }
}
