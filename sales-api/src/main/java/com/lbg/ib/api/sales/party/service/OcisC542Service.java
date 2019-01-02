package com.lbg.ib.api.sales.party.service;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.party.C542RequestDTO;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.service.SoaAbstractService;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ResultCondition;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lloydstsb.ea.context.ClientContext;
import com.lloydstsb.www.C542Req;
import com.lloydstsb.www.C542Resp;
import com.lloydstsb.www.C542_ChaExtPrdIDTx_PortType;
import com.lloydstsb.www.C542_ChaExtPrdIDTx_ServiceLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;

@Component
public class OcisC542Service extends SoaAbstractService {

    @Autowired
    private SessionManagementDAO sessionManager;

    @Autowired
    private ModuleContext beanLoader;
    @Autowired
    private GalaxyErrorCodeResolver resolver;

    @Autowired
    private LoggerDAO logger;

    /**
     *
     * @return
     * @throws ServiceException
     */
    @TraceLog
    public void updateOcisRecords(C542RequestDTO requestDTO) throws ServiceException {
        logger.traceLog(this.getClass(), "updateOcisRecords: Enter");
        C542_ChaExtPrdIDTx_ServiceLocator serviceLocator = beanLoader.getService(C542_ChaExtPrdIDTx_ServiceLocator.class);
        setDataHandler(new QName(serviceLocator.getC542_ChaExtPrdIDTxWSDDPortName()), serviceLocator.getHandlerRegistry());
        ClientContext clientContext = sessionManager.getUserContext().toClientContext();
        setSoapHeader(clientContext, "c542", serviceLocator.getServiceName().getNamespaceURI());

        C542Req request = createRequest(requestDTO);
        logger.traceLog(this.getClass(), "updateOcisRecords: Calling C542 API");
        C542Resp c542Resp = (C542Resp) invoke("c542", request);
        logger.traceLog(this.getClass(), "updateOcisRecords: Response received from C542 API");
        validateServiceResponse(c542Resp);
    }

    @TraceLog
    private C542Req createRequest(C542RequestDTO requestDTO) {
        C542Req request = new C542Req();
        request.setMaxRepeatGroupQy(0);
        if(sessionManager.getBranchContext()!= null) {
        	request.setExtSysId((short)94);
        }else {
        	request.setExtSysId((short)19);
        }
        
        request.setProductExtSysId((short)4);
        request.setExtProdHeldIdTx(requestDTO.getSortCode()+requestDTO.getAccountNumber()+"00000"); // sortCode+accountNumber+00000
        request.setExtProdIdTx(requestDTO.getCbsProductId());
        request.setPartyId(Long.valueOf(requestDTO.getOcisId()));
        request.setPartyExtSysId((short)0);
        request.setExtPartyIdTx("");
        return request;
    }

    @TraceLog
    private void validateServiceResponse(C542Resp c542Resp) {
        if (c542Resp == null || c542Resp.getC542Result() == null) {
            throw new ServiceException(resolver.resolve(ResponseErrorConstants.SERVICE_UNAVAILABLE));
        } else if (c542Resp.getC542Result().getResultCondition() != null &&
                c542Resp.getC542Result().getResultCondition().getSeverityCode().getValue() != 0) {

            ResultCondition resultCondition = c542Resp.getC542Result().getResultCondition();
            logger.traceLog(this.getClass(), "Error C542 response: " + c542Resp.getC542Result().getResultCondition().getSeverityCode().getValue());
            throw new ServiceException(
                    new ResponseError(resultCondition.getReasonCode().toString(), resultCondition.getReasonText()));
        }
    }

    @Override
    public Class<?> getPort() {
        return C542_ChaExtPrdIDTx_PortType.class;
    }

}
