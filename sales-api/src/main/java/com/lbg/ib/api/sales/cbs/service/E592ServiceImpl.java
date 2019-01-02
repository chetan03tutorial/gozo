package com.lbg.ib.api.sales.cbs.service;

import java.util.ArrayList;
import java.util.List;

import com.lbg.ib.api.sales.cbs.service.header.CbsAppGrpHeader;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.dao.constants.CommonConstant;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.header.CustomSoapHeader;
import com.lbg.ib.api.sales.product.domain.eligibility.UpgradeOption;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.service.SoaAbstractService;
import com.lbg.ib.api.sales.shared.soap.header.SoapHeaderGenerator;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ResultCondition;
import com.lbg.ib.api.sales.user.domain.SelectedAccount;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lloydsbanking.xml.*;
import com.lloydstsb.ea.context.ClientContext;
import com.lloydstsb.ea.dao.enums.DAOConditionType;
import com.lloydstsb.ea.dao.header.HeaderData;
import com.lloydstsb.ea.lcsm.BapiInformation;
import com.lloydstsb.ea.logging.helper.ApplicationRequestContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;

/**
 * Service layer for Convert Account API's
 *
 * @author tkhann
 */
@Component
public class E592ServiceImpl extends SoaAbstractService implements E592Service {
    /**
     * Object of SessionManagementDAO
     */
    @Autowired
    private SessionManagementDAO sessionManager;
    /**
     * Object of ConfigurationDAO
     */
    @Autowired
    private ConfigurationDAO configManager;
    /**
     * Object of ModuleContext
     */
    @Autowired
    private ModuleContext beanLoader;
    /**
     * Object of LoggerDAO
     */
    @Autowired
    private LoggerDAO logger;
    /**
     * Object of Galaxy Error Code.
     */
    @Autowired
    private GalaxyErrorCodeResolver resolver;

    @Autowired
    private SoapHeaderGenerator soapHeaderGenerator;

    @Autowired
    private SessionManagementDAO session;

    /*
     * (non-Javadoc)
     *
     * @see
     * com.lbg.ib.api.sales.cbs.service.E592Service#convertProduceE592(java.
     * lang.String)
     */
    @TraceLog
    public void convertProductE592(SelectedAccount account, UpgradeOption upgradeOption) {
        String methodName = "convertProduceE592";
        E592_ChaCBSTrnsfrProd_ServiceLocator serviceLocator = beanLoader.getService(E592_ChaCBSTrnsfrProd_ServiceLocator.class);
        ClientContext clientContext = sessionManager.getUserContext()
                .toClientContext();
        ApplicationRequestContext.set(DAOConditionType.CLIENT_CTX_SOAP_HEADER.code(), clientContext);
        HeaderData headers = soapHeaderGenerator.prepareHeaderData("e592",
                serviceLocator.getServiceName().getNamespaceURI());
        headers.getContactPointHeader().setApplicationId(CommonConstant.EPORTAL_APPLICATION_ID);
        ApplicationRequestContext.set(DAOConditionType.HEADER_DATA_SOAP_HEADER.code(), headers);
        List<SOAPHeader> defaultSoapHeaders = soapHeaderGenerator
                .getGenericSoapHeader("e592", serviceLocator.getServiceName()
                        .getNamespaceURI(), false);
        for (SOAPHeader soap : defaultSoapHeaders) {
            if ("bapiInformation".equalsIgnoreCase(soap.getName())) {
                BapiInformation bapiInformation = bapiInfomation((com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.BapiInformation) soap
                        .getValue());
                ApplicationRequestContext.set(DAOConditionType.BAPI_INFO_SOAP_HEADER.code(),bapiInformation);
            }
        }
        //AOB-1509 Adding new custom soap header
        if (null != session.getBranchContext()) {
            logger.traceLog(this.getClass(), "Adding custom soap header");
            List<CustomSoapHeader> customHeaderList= new ArrayList<CustomSoapHeader>();
            final CbsAppGrpHeader cbsAppGrpHeader = new CbsAppGrpHeader();
            cbsAppGrpHeader.setOrginatingSortCode(session.getBranchContext().getOriginatingSortCode());
            customHeaderList.add(cbsAppGrpHeader);
            ApplicationRequestContext.set("CustomHeadersList", customHeaderList);
            setCustomDataHandler(new QName(serviceLocator.getE592_ChaCBSTrnsfrProdWSDDPortName()),
                    serviceLocator.getHandlerRegistry());
        }else{
            setDataHandler(new QName(serviceLocator.getE592_ChaCBSTrnsfrProdWSDDPortName()),
                    serviceLocator.getHandlerRegistry());
        }
        E592Resp e592Resp = (E592Resp) invoke("e592",buildE592Req(account, upgradeOption));
        validate(e592Resp,account);
        logger.traceLog(this.getClass(), methodName + CommonConstant.END);
    }

    /**
     * Build the request for E592
     *
     * @param account
     * @param upgradeOption
     * @return E592Req
     */

    private E592Req buildE592Req(SelectedAccount account,
            UpgradeOption upgradeOption) {
        E592Req e592Req = new E592Req();
        e592Req.setCBSAccountNoId(account.getSortCode()
                + account.getAccountNumber());
        e592Req.setCBSProdNoFlagId(1);
        e592Req.setCBSProdNoId(Integer.valueOf(upgradeOption.getCbsProductIds()
                .get(0).substring(0, 4))); // Send only first 4 char
        e592Req.setTariffId(Integer.valueOf(upgradeOption.getTariff()));
        e592Req.setTariffFlagId(1);
        CBSRequestGp2 cbsRequestGp2 = new CBSRequestGp2();
        cbsRequestGp2.setInputOfficerFlagStatusCd(0);
        cbsRequestGp2.setInputOfficerStatusCd(0);
        cbsRequestGp2.setOverrideDetailsCd(0);
        cbsRequestGp2.setOverridingOfficerStaffNo("0");
        //Adding originating sort code as per CBS . Refer Jira:  AOB-825
        if (null != session.getBranchContext()) {
            cbsRequestGp2.setSourceId(session.getBranchContext().getOriginatingSortCode());
        }
        e592Req.setCBSRequestGp2(cbsRequestGp2);
        return e592Req;
    }

    /**
     * Validate the response.
     *
     * @param response
     */
    @TraceLog
    private void validate(E592Resp response, SelectedAccount account) {
        String methodName = "validate";
        logger.traceLog(this.getClass(), methodName);
        if (null == response) {
            logger.traceLog(this.getClass(), "Error in response of E592Resp");
            throw new ServiceException(
                    resolver.resolve(ResponseErrorConstants.SERVICE_UNAVAILABLE));
        } else if (null != response.getE592Result().getResultCondition()
                .getSeverityCode()
                && response.getE592Result().getResultCondition()
                .getSeverityCode().getValue() == 2) {
            ResultCondition resultCondition = response.getE592Result()
                    .getResultCondition();
            String errorMonitoringString = "Error in conversion, serviceErrorCode="+ resultCondition.getReasonCode().toString()
                    + ", serviceErrorMessage=" + resultCondition.getReasonText()
                    + ", convertedAccountNumber=" + account.getAccountNumber()
                    + ", convertedSortCode=" + account.getSortCode();
            logger.traceLog(this.getClass(), errorMonitoringString);
            throw new ServiceException(new ResponseError(resultCondition
                    .getReasonCode().toString(),
                    resultCondition.getReasonText()));
        }
    }

    @Override
    public Class<?> getPort() {
        return E592_ChaCBSTrnsfrProd_PortType.class;
    }

}
