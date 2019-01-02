package com.lbg.ib.api.sales.dao.product.modify;

import com.lbg.ib.api.sales.asm.domain.ModifyArrangementOperationType;
import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.product.activate.ActivateProductDTO;
import com.lbg.ib.api.sales.product.domain.*;
import com.lbg.ib.api.sales.product.domain.Condition;
import com.lbg.ib.api.sales.product.domain.pending.ModifyProductArrangement;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.*;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.BaseRequest;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.RequestHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;
import com.lbg.ib.api.sales.soapapis.retrievearrangement.ia_pendingarrangementmaster.IA_PendingArrangementMaster;
import com.lbg.ib.api.sales.soapapis.retrievearrangement.messages.ModifyProductArrangementRequest;
import com.lbg.ib.api.sales.soapapis.retrievearrangement.messages.ModifyProductArrangementResponse;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import static com.lbg.ib.api.sales.common.constant.Constants.ModifyProductArrangementConstants.SERVICE_ACTION;
import static com.lbg.ib.api.sales.common.constant.Constants.ModifyProductArrangementConstants.SERVICE_NAME;
import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;

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
public class ModifyProductArrangementDAOImpl implements ModifyProductArrangementDAO {
    
    private IA_PendingArrangementMaster modifyService;
    
    private GBOHeaderUtility headerUtility;
    
    private SessionManagementDAO session;
    
    private DAOExceptionHandler daoExceptionHandler;
    
    private ConfigurationDAO configDAO;
    
    private LoggerDAO logger;
    

    private SimpleDateFormat dateFormatter  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    
    private static final String VALID_STRING   = "VALID";
    
    @Autowired
    public ModifyProductArrangementDAOImpl(IA_PendingArrangementMaster modifyService, GBOHeaderUtility headerUtility,
            SessionManagementDAO session, DAOExceptionHandler daoExceptionHandler, ConfigurationDAO configDAO,
            LoggerDAO logger) {
        this.modifyService = modifyService;
        this.headerUtility = headerUtility;
        this.session = session;
        this.daoExceptionHandler = daoExceptionHandler;
        this.configDAO = configDAO;
        this.logger = logger;
        
    }
    
    @TraceLog
    public DAOResponse<ModifyProductArrangement> modifyProductArrangement(
            ModifyProductArrangement modifyProductArrangement) {
        
        try {
            ModifyProductArrangementRequest modifyProductArrangementRequest = createRequest(modifyProductArrangement);
            ModifyProductArrangementResponse response = modifyService
                    .modifyProductArrangement(modifyProductArrangementRequest);
            ModifyProductArrangement responseDTO = mapResponseStructure(response);
            return withResult(responseDTO);
        } catch (Exception ex) {
            DAOResponse.DAOError daoError = daoExceptionHandler.handleException(ex, this.getClass(), "modifyProductArrangement",
                    modifyProductArrangement);
            return withError(daoError);
        }
    }
    
    private ModifyProductArrangement mapResponseStructure(ModifyProductArrangementResponse response) {
        ModifyProductArrangement modifyProductArrangement = new ModifyProductArrangement();
        if (response.getProductArrangement() != null) {
            modifyProductArrangement.setArrangementId(response.getProductArrangement().getArrangementId());
            modifyProductArrangement.setArrangementType(response.getProductArrangement().getArrangementType());
        }
        return modifyProductArrangement;
    }
    
    private ModifyProductArrangementRequest createRequest(ModifyProductArrangement modifyProductArrangement)
            throws URISyntaxException {
        ModifyProductArrangementRequest modifyProductArrangementRequest = new ModifyProductArrangementRequest();
        modifyProductArrangementRequest.setHeader(populateHeaders().getHeader());
        
        DepositArrangement depositArrangement = new DepositArrangement();
        
        depositArrangement.setArrangementType(modifyProductArrangement.getArrangementType());
        depositArrangement.setArrangementId(modifyProductArrangement.getArrangementId());
        if(modifyProductArrangement.getConditions()!=null){
            depositArrangement.setConditions(mapProductConditions(modifyProductArrangement));
        }
        modifyProductArrangementRequest.setOperation(fetchOperation(modifyProductArrangement));
        modifyProductArrangementRequest.setProductArrangement(depositArrangement);
        return modifyProductArrangementRequest;
    }

    private RuleCondition[] mapProductConditions(ModifyProductArrangement modifyProductArrangement) {
        Condition[] conditions = modifyProductArrangement.getConditions();
        List<RuleCondition> ruleConditions = new ArrayList<RuleCondition>();
        for(Condition condition : conditions){
            RuleCondition ruleCondition = new RuleCondition();

            ruleCondition.setResult(condition.getValue());
            ruleCondition.setName(condition.getName());
            ruleConditions.add(ruleCondition);
        }

        return ruleConditions.toArray(new RuleCondition[ruleConditions.size()]);
    }

    private String fetchOperation(ModifyProductArrangement modifyProductArrangement){
        if(modifyProductArrangement.getConditions()!=null){
            return ModifyArrangementOperationType.OD_OPERATION.getValue();
        }else{
            return ModifyArrangementOperationType.EIDV_OPERATION.getValue();
        }
    }
    

    private BaseRequest populateHeaders() throws URISyntaxException {
        
        RequestHeader requestHeader = new RequestHeader();
        List<SOAPHeader> soapHeaders = null;
        soapHeaders = headerUtility.prepareSoapHeader(SERVICE_ACTION, SERVICE_NAME);
        if (null != soapHeaders) {
            SOAPHeader[] soapHeaderArray = soapHeaders.toArray(new SOAPHeader[soapHeaders.size()]);
            SOAPHeader[] both = (SOAPHeader[]) ArrayUtils.addAll(requestHeader.getLloydsHeaders(), soapHeaderArray);
            requestHeader.setLloydsHeaders(both);
        }
        
        requestHeader.setBusinessTransaction("modifyProductArrangement");
        requestHeader.setInteractionId(session.getSessionId());
        BaseRequest baseRequest = new BaseRequest();
        baseRequest.setHeader(requestHeader);
        return baseRequest;
    }
    
}
