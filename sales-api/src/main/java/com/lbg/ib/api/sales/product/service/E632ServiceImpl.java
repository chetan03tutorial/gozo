package com.lbg.ib.api.sales.product.service;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.sales.dao.constants.CommonConstant;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.product.domain.E632Response;
import com.lbg.ib.api.sales.product.domain.eligibility.PcciOverdraftRequest;
import com.lbg.ib.api.sales.product.domain.eligibility.UpgradeOption;
import com.lbg.ib.api.sales.product.mapper.E632MessageMapper;
import com.lbg.ib.api.sales.shared.domain.ModuleContext;
import com.lbg.ib.api.sales.shared.service.SoaAbstractService;
import com.lbg.ib.api.sales.shared.soap.header.SoapHeaderGenerator;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ResultCondition;
import com.lbg.ib.api.sales.user.domain.SelectedAccount;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sso.domain.user.Account;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import com.lloydsbanking.xml.E632Resp;
import com.lloydsbanking.xml.E632_ODIntDetails_PortType;
import com.lloydsbanking.xml.E632_ODIntDetails_ServiceLocator;
import com.lloydstsb.ea.context.ClientContext;
import com.lloydstsb.ea.dao.enums.DAOConditionType;
import com.lloydstsb.ea.dao.header.HeaderData;
import com.lloydstsb.ea.lcsm.BapiInformation;
import com.lloydstsb.ea.logging.helper.ApplicationRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.List;
import java.util.Map;

@Component
public class E632ServiceImpl extends SoaAbstractService implements E632Service {

    @Autowired
    private SessionManagementDAO sessionManager;

    @Autowired
    private GalaxyErrorCodeResolver errorResolver;

    @Autowired
    private ModuleContext beanLoader;

    @Autowired
    private LoggerDAO logger;

    @Autowired
    private SoapHeaderGenerator soapHeaderGenerator;
    
    private void prepareSoapRequest() {
        E632_ODIntDetails_ServiceLocator serviceLocator = beanLoader.getService(E632_ODIntDetails_ServiceLocator.class);
        setDataHandler(new QName(serviceLocator.getE632_ODIntDetailsWSDDPortName()), serviceLocator.getHandlerRegistry());
        ClientContext clientContext = sessionManager.getUserContext().toClientContext();
        ApplicationRequestContext.set(DAOConditionType.CLIENT_CTX_SOAP_HEADER.code(), clientContext);
        HeaderData headers = soapHeaderGenerator.prepareHeaderData("E632", serviceLocator.getServiceName().getNamespaceURI());
        headers.getContactPointHeader().setApplicationId(CommonConstant.EPORTAL_APPLICATION_ID);
        ApplicationRequestContext.set(DAOConditionType.HEADER_DATA_SOAP_HEADER.code(), headers);
        List<SOAPHeader> defaultSoapHeaders = soapHeaderGenerator.getGenericSoapHeader("E632", serviceLocator.getServiceName().getNamespaceURI(), false);
        for (SOAPHeader soap : defaultSoapHeaders) {
            if ("bapiInformation".equalsIgnoreCase(soap.getName())) {
                BapiInformation bapiInformation = bapiInfomation((com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.BapiInformation) soap.getValue());
                ApplicationRequestContext.set(DAOConditionType.BAPI_INFO_SOAP_HEADER.code(), bapiInformation);
            }
        }
    }

    public E632Response e632(String productMnemonic) {
        UpgradeOption toProduct = toProduct(productMnemonic);
        Account fromAccount = fromProduct();
        prepareSoapRequest();
        E632Resp e632ServiceResponse = (E632Resp) invoke("e632", E632MessageMapper.buildE632Request(fromAccount, toProduct, null));
        validateE632Response(e632ServiceResponse);
        return extractE632Response(e632ServiceResponse);
    }
    
    public E632Response e632WithOverDraftOption(PcciOverdraftRequest overDraftOption) {
        Account fromAccount = fromProduct();
        prepareSoapRequest();
        E632Resp e632ServiceResponse = (E632Resp) invoke("e632", E632MessageMapper.buildE632Request(fromAccount, null, overDraftOption));
        validateE632Response(e632ServiceResponse);
        return extractE632Response(e632ServiceResponse);
    }

    private Account fromProduct() {
        Account fromAccount = null;
        SelectedAccount accountToConvert = sessionManager.getAccountToConvertInContext();
        Arrangement arrangement = sessionManager.getUserInfo();
        List<Account> productHoldingList = arrangement.getAccounts();
        for (Account account : productHoldingList) {
            if (account.getAccountNumber().equalsIgnoreCase(accountToConvert.getAccountNumber()) && account.getSortCode().equalsIgnoreCase(accountToConvert.getSortCode())) {
                fromAccount = account;
            }
        }
        if (fromAccount == null) {
            throw new ServiceException(errorResolver.resolve(ResponseErrorConstants.NO_PRODUCT_TO_ACTIVATE_SERVICE_EXCEPTION));
        }
        return fromAccount;
    }

    public E632Response extractE632Response(E632Resp serviceResponse) {
        E632Response e632Response = new E632Response();

        e632Response.setFeeFreeOverdraftAmount(serviceResponse.getFeeFreeOvrdrtAm());
        e632Response.setDailyOverdraftAmount(serviceResponse.getDailyOvrdrtApbFe());
        e632Response.setDailyStep(serviceResponse.getDailyStepCg());
        e632Response.setDailyCharge(serviceResponse.getDailyChargeAm());
        e632Response.setCurrency(serviceResponse.getCurrencyCd());
        e632Response.setExcessFeeAmount(serviceResponse.getExcessFeeAm());
        e632Response.setExcessFeeBalance(serviceResponse.getExcessFeeBalIncAm());
        e632Response.setInterestFeeWaiverAmount(serviceResponse.getIntFeeWaiverAm());
        e632Response.setPlannedOverdraftFee(serviceResponse.getPlannedOverdraftFe());
        e632Response.setTotalCostOfCreditAmount(serviceResponse.getTotalCostOfCreditAm());
        e632Response.setUnauthorisedAnnualInterestRate(serviceResponse.getUnauthEquivAnnualIntRt());
        e632Response.setUnauthrorisedGrossProductInterestRate(serviceResponse.getUnauthGrossPrdIntRt());
        e632Response.setUnauthorisedOverdraftRate(serviceResponse.getUnauthOverdraftRt());
        e632Response.setUsageFee(serviceResponse.getUsageFe());

        return e632Response;
    }

    public void validateE632Response(E632Resp serviceResponse) {
        if (serviceResponse == null || serviceResponse.getE632Result() == null) {
            throw new ServiceException(errorResolver.resolve(ResponseErrorConstants.SERVICE_UNAVAILABLE));

        } else if (serviceResponse.getE632Result().getResultCondition() != null &&
                serviceResponse.getE632Result().getResultCondition().getSeverityCode().getValue() != 0) {

                ResultCondition resultCondition = serviceResponse.getE632Result().getResultCondition();
                logger.traceLog(this.getClass(), "Error E632 response: " + serviceResponse.getE632Result().getResultCondition().getSeverityCode().getValue());
                throw new ServiceException(
                        new ResponseError(resultCondition.getReasonCode().toString(), resultCondition.getReasonText()));
            }
    }

    private UpgradeOption toProduct(String productMnemonic) {
        UpgradeOption toAccount = null;
        Map<String, UpgradeOption> upgradeOptions = sessionManager.getAvailableUpgradeOptions();
        if(productMnemonic != null && upgradeOptions != null) {
            toAccount = upgradeOptions.get(productMnemonic);
            if (toAccount == null) {
                throw new ServiceException(errorResolver.createResponseError(ResponseErrorConstants.INVALID_PRODUCT_MNEMONIC));
            }
        }
        return toAccount;
    }

    @Override
    public Class<?> getPort() {
        return E632_ODIntDetails_PortType.class;
    }
}
