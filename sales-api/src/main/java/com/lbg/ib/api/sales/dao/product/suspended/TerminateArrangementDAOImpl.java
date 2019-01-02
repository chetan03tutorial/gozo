package com.lbg.ib.api.sales.dao.product.suspended;

import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;

import java.net.URISyntaxException;
import java.util.List;

import com.lbg.ib.api.sales.product.domain.domains.TerminateArrangement;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.product.pendingarrangement.TerminateArrangementDTO;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ReasonCode;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.BaseRequest;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.RequestHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;
import com.lbg.ib.api.sales.soapapis.terminatearrangement.ia_terminatearrangementmaster.IA_TerminateArrangement;

@Component
public class TerminateArrangementDAOImpl implements TerminateArrangementDAO{

    public static final String          METHOD_NAME    = "terminateArrangement";
    private static final String         SERVICE_NAME   = "terminateArrangement";
    private static final String         SERVICE_ACTION = "terminateArrangement";
    
    private IA_TerminateArrangement terminateArrangementSerive;
    
    private GBOHeaderUtility            headerUtility;
    
    private SessionManagementDAO        session;
    
    private DAOExceptionHandler         daoExceptionHandler;
    
    private ConfigurationDAO configDAO;
    
    private LoggerDAO logger;

    
    @Autowired
    public TerminateArrangementDAOImpl(IA_TerminateArrangement terminateArrangementSerive,
            GBOHeaderUtility headerUtility, SessionManagementDAO session, DAOExceptionHandler daoExceptionHandler,
            ConfigurationDAO configDAO, LoggerDAO logger) {
        this.terminateArrangementSerive = terminateArrangementSerive;
        this.headerUtility = headerUtility;
        this.session = session;
        this.daoExceptionHandler = daoExceptionHandler;
        this.configDAO = configDAO;
        this.logger = logger;
    }
    
    @TraceLog
    public DAOResponse<TerminateArrangementDTO> terminateArrangement(TerminateArrangement terminateArrangement) {
      try{
          com.lbg.ib.api.sales.soapapis.terminatearrangement.messages.TerminateArrangementRequest terminateArrangementRequest = createTerminateArrangementRequest(terminateArrangement); 
          com.lbg.ib.api.sales.soapapis.terminatearrangement.messages.TerminateArrangementResponse terminateArrangementResponse = terminateArrangementSerive.terminateArrangement(terminateArrangementRequest);
        
          TerminateArrangementDTO terminateArrangementDTO = mapResponseStructure(terminateArrangementResponse);
          return withResult(terminateArrangementDTO);
      } catch (Exception ex) {
        DAOResponse.DAOError daoError = daoExceptionHandler.handleException(ex, this.getClass(),
                METHOD_NAME, terminateArrangement);
        return withError(daoError);
      }
    }


    @TraceLog
    public TerminateArrangementDTO mapResponseStructure(com.lbg.ib.api.sales.soapapis.terminatearrangement.messages.TerminateArrangementResponse terminateArrangementResponse){
        TerminateArrangementDTO terminateArrangementDTO = new TerminateArrangementDTO();
        if(terminateArrangementResponse.getHeader()!=null){
            terminateArrangementDTO.setArrangementId(terminateArrangementResponse.getHeader().getArrangementId());
        }
        return terminateArrangementDTO;
    }
    
    public com.lbg.ib.api.sales.soapapis.terminatearrangement.messages.TerminateArrangementRequest createTerminateArrangementRequest(TerminateArrangement terminateArrangement) throws URISyntaxException{
        com.lbg.ib.api.sales.soapapis.terminatearrangement.messages.TerminateArrangementRequest terminateArrangementRequest = new com.lbg.ib.api.sales.soapapis.terminatearrangement.messages.TerminateArrangementRequest();
        
        terminateArrangementRequest.setArrangementId(terminateArrangement.getArrangementId());
        ReasonCode reasonCode = new ReasonCode();
        reasonCode.setCode(terminateArrangement.getReasonCode());
        terminateArrangementRequest.setReasonCode(reasonCode);
        terminateArrangementRequest.setAction(terminateArrangement.getAction());
        terminateArrangementRequest.setUserId(terminateArrangement.getUserId());
        terminateArrangementRequest.setHeader(populateHeaders().getHeader());
        
        return terminateArrangementRequest; 
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
        
        requestHeader.setBusinessTransaction("retrieveProductArrangement");
        requestHeader.setInteractionId(session.getSessionId());
        BaseRequest baseRequest = new BaseRequest();
        baseRequest.setHeader(requestHeader);
        return baseRequest;
    }
    
}
