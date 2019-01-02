package com.lbg.ib.api.sales.dao.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.sales.dto.bankwizard.BankAccountDetailsRequestDTO;
import com.lbg.ib.api.sales.soapapis.bw.arrangement.DepositArrangement;
import com.lbg.ib.api.sales.soapapis.bw.arrangement.ProductArrangement;
import com.lbg.ib.api.sales.soapapis.bw.arrangementnegotiation.VerifyProductArrangementDetailsRequest;
import com.lbg.ib.api.sales.soapapis.bw.common.AlternateId;
import com.lbg.ib.api.sales.soapapis.bw.common.ConditionParameter;
import com.lbg.ib.api.sales.soapapis.bw.common.ObjectReference;
import com.lbg.ib.api.sales.soapapis.bw.common.RuleCondition;
import com.lbg.ib.api.sales.soapapis.bw.involvedparty.Individual;
import com.lbg.ib.api.sales.soapapis.bw.involvedparty.InvolvedPartyRole;
import com.lbg.ib.api.sales.soapapis.bw.involvedparty.InvolvedPartyRoleType;
import com.lbg.ib.api.sales.soapapis.bw.involvedparty.Organization;
import com.lbg.ib.api.sales.soapapis.bw.lcsm.BAPIHeader;
import com.lbg.ib.api.sales.soapapis.bw.lcsm.HostInformation;
import com.lbg.ib.api.sales.soapapis.bw.schema.infrastructure.UNPMechanismTypeEnum;
import com.lbg.ib.api.sales.soapapis.bw.schema.infrastructure.soap.security.UsernameToken;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.BapiInformation;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ContactPoint;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ServiceRequest;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.security.SecurityHeaderType;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * @author Atul Choudhary
 * @version 1.0
 * @since 29thJuly2016
 ***********************************************************************/
@Component
public class BankWizardValidateAccountRequestMapper {

    private static final String ISO_COUNTRY_CODE      = "ISO_COUNTRY_CODE";
    private static final String CUSTOMER              = "CUSTOMER";
    private static final String UNAUTHSALE            = "UNAUTHSALE";
    private static final String BANK_ACCOUNT_NUMBER   = "BANK_ACCOUNT_NUMBER";
    private static final String FINANCIAL_INSTITUTION = "FINANCIAL_INSTITUTION";
    private static final String SALSA_SERVICE_NAME    = "http://www.lloydstsb.com/Schema/Enterprise/LCSM_ArrangementNegotiation/ArrangementSetupService";
    private static final String SALSA_SERVICE_ACTION  = "verifyProductArrangementDetails";
    private static final String BANK_SORT_CODE        = "BANK_SORT_CODE";
    private static final String BANK_WIZARD           = "BANK_WIZARD";
    private static final String WSDL_VERSION          = "WSDL_VERSION";
    private static final String USER_IDENTIFIER       = "USER_IDENTIFIER";

    @Autowired
    private GBOHeaderUtility    gboHeaderUtility;

    private ServiceRequest      serviceRequest;
    private ContactPoint        contactPoint;
    private SecurityHeaderType  securityHeaderType;
    private BapiInformation     bapiInformation;

    public void prepareHeader() {
        List<SOAPHeader> headersList = gboHeaderUtility.prepareSoapHeader(SALSA_SERVICE_ACTION, SALSA_SERVICE_NAME);
        for (SOAPHeader soap : headersList) {
            if ("ContactPoint".equalsIgnoreCase(soap.getName())) {
                contactPoint = (ContactPoint) soap.getValue();
                continue;
            }

            if ("ServiceRequest".equalsIgnoreCase(soap.getName())) {
                serviceRequest = (ServiceRequest) soap.getValue();
                continue;
            }

            if ("Security".equalsIgnoreCase(soap.getName())) {
                securityHeaderType = (SecurityHeaderType) soap.getValue();
                continue;
            }

            if ("bapiInformation".equalsIgnoreCase(soap.getName())) {
                bapiInformation = (BapiInformation) soap.getValue();
                continue;
            }
        }
    }

    public VerifyProductArrangementDetailsRequest mapRequestAttribute(
            BankAccountDetailsRequestDTO bankAccountDetailsDTO) {
        VerifyProductArrangementDetailsRequest detailsRequest = new VerifyProductArrangementDetailsRequest();
        List<DepositArrangement> depositArranegementList = new ArrayList<DepositArrangement>();
        DepositArrangement depositArrangement = new DepositArrangement();
        List<RuleCondition> conditionList = mappingConditionList();
        conditionList.add(getISOCountryCodeRuleCondition());
        RuleCondition[] ruleCondition = new RuleCondition[conditionList.size()];
        conditionList.toArray(ruleCondition);
        depositArrangement.setHasObjectConditions(ruleCondition);
        ObjectReference objectReference = populateAlternateId(BANK_ACCOUNT_NUMBER,
                bankAccountDetailsDTO.getAccountNo());
        depositArrangement.setObjectReference(objectReference);

        InvolvedPartyRole[] roles = new InvolvedPartyRole[3];
        InvolvedPartyRole individualRole = new InvolvedPartyRole();
        individualRole.setInvolvedParty(getCustomerRole());
        individualRole.setType(populateType(CUSTOMER));
        roles[0] = individualRole;
        InvolvedPartyRole bankWizardRole = new InvolvedPartyRole();
        bankWizardRole.setInvolvedParty(getBankWizardRole());
        bankWizardRole.setType(populateType(BANK_WIZARD));
        roles[1] = bankWizardRole;
        roles[2] = getFinancialInstitutionRole(bankAccountDetailsDTO.getSortCode());

        depositArrangement.setRoles(roles);
        depositArranegementList.add(depositArrangement);
        ProductArrangement[] arrangementToVerify = new ProductArrangement[depositArranegementList.size()];
        depositArranegementList.toArray(arrangementToVerify);
        detailsRequest.setArrangementToVerify(arrangementToVerify);
        return detailsRequest;
    }

    private RuleCondition getISOCountryCodeRuleCondition() {
        RuleCondition ruleCondition = new RuleCondition();
        ruleCondition.setName(ISO_COUNTRY_CODE);
        ConditionParameter conditionParameter = new ConditionParameter();
        conditionParameter.setValue("GB");
        List<ConditionParameter> conditionParameterList = new ArrayList<ConditionParameter>();
        conditionParameterList.add(conditionParameter);
        ConditionParameter[] hasInputParameters = new ConditionParameter[conditionParameterList.size()];
        conditionParameterList.toArray(hasInputParameters);
        conditionParameter.setValue("GB");
        ruleCondition.setHasInputParameters(hasInputParameters);
        return ruleCondition;
    }

    private ObjectReference populateAlternateId(String attributeString, String value) {
        ObjectReference objectReference = new ObjectReference();
        List<AlternateId> alternateIdList = new ArrayList<AlternateId>();
        AlternateId alternateId = new AlternateId();
        alternateId.setAttributeString(attributeString);
        alternateId.setValue(value);
        alternateIdList.add(alternateId);
        AlternateId[] alternateIds = new AlternateId[alternateIdList.size()];
        alternateIdList.toArray(alternateIds);
        objectReference.setAlternateId(alternateIds);
        return objectReference;
    }

    private InvolvedPartyRole getFinancialInstitutionRole(String sortCode) {
        InvolvedPartyRole involvedPartyRole = populateInvolvedPartyRole(FINANCIAL_INSTITUTION);
        involvedPartyRole.setObjectReference(populateAlternateId(BANK_SORT_CODE, sortCode));
        return involvedPartyRole;
    }

    private Organization getBankWizardRole() {
        Organization organization = new Organization();
        ObjectReference objectReference = populateAlternateId(WSDL_VERSION, "1.0");
        organization.setObjectReference(objectReference);
        return organization;
    }

    private InvolvedPartyRole populateInvolvedPartyRole(String value) {
        InvolvedPartyRole involvedPartyRole = new InvolvedPartyRole();
        InvolvedPartyRoleType involvedPartyRoleType = new InvolvedPartyRoleType();
        involvedPartyRoleType.setValue(value);
        involvedPartyRole.setType(involvedPartyRoleType);
        return involvedPartyRole;
    }

    private InvolvedPartyRoleType populateType(String value) {
        InvolvedPartyRoleType involvedPartyRoleType = new InvolvedPartyRoleType();
        involvedPartyRoleType.setValue(value);
        return involvedPartyRoleType;
    }

    private Individual getCustomerRole() {
        Individual individual = new Individual();
        ObjectReference objectReference = populateAlternateId(USER_IDENTIFIER, UNAUTHSALE);
        individual.setObjectReference(objectReference);
        return individual;
    }

    private List<RuleCondition> mappingConditionList() {
        List<RuleCondition> conditionList = new ArrayList<RuleCondition>();
        for (Map.Entry<String, String> entry : getTokenisedRuleConditionMap().entrySet()) {
            RuleCondition ruleCondition = new RuleCondition();
            ruleCondition.setName(entry.getKey());
            ruleCondition.setResult(entry.getValue());
            conditionList.add(ruleCondition);
        }
        return conditionList;
    }

    private Map<String, String> getTokenisedRuleConditionMap() {
        Map<String, String> ruleConditonMap = new HashMap<String, String>();
        ruleConditonMap.put("CHECK_TYPE", "VALIDATE_ONLY");
        ruleConditonMap.put("TRIGGER", "DATA_VALIDATION_REQUEST");
        return ruleConditonMap;
    }

    public com.lbg.ib.api.sales.soapapis.bw.schema.infrastructure.soap.security.SecurityHeaderType getSecurityHeader() {
        com.lbg.ib.api.sales.soapapis.bw.schema.infrastructure.soap.security.SecurityHeaderType bwSecurityHeaderType = new com.lbg.ib.api.sales.soapapis.bw.schema.infrastructure.soap.security.SecurityHeaderType();
        bwSecurityHeaderType.setMustReturn(securityHeaderType.isMustReturn());
        UsernameToken usernameToken = new UsernameToken();
        usernameToken.setId(securityHeaderType.getUsernameToken().getId());
        usernameToken.setUsername(securityHeaderType.getUsernameToken().getUsername());
        usernameToken.setUserType(securityHeaderType.getUsernameToken().getUserType());
        usernameToken.setUNPMechanismType(UNPMechanismTypeEnum.NTLM);
        bwSecurityHeaderType.setUsernameToken(usernameToken);
        return bwSecurityHeaderType;
    }

    public com.lbg.ib.api.sales.soapapis.bw.schema.infrastructure.soap.ServiceRequest getServiceRequestHeader() {
        com.lbg.ib.api.sales.soapapis.bw.schema.infrastructure.soap.ServiceRequest bwServiceRequest = new com.lbg.ib.api.sales.soapapis.bw.schema.infrastructure.soap.ServiceRequest();
        bwServiceRequest.setAction(serviceRequest.getAction());
        bwServiceRequest.setMessageId(serviceRequest.getMessageId());
        bwServiceRequest.setMustReturn(serviceRequest.isMustReturn());
        bwServiceRequest.setServiceName(serviceRequest.getServiceName());
        bwServiceRequest.setFrom(serviceRequest.getFrom());
        bwServiceRequest.setServiceName(serviceRequest.getFrom());
        return bwServiceRequest;
    }

    public com.lbg.ib.api.sales.soapapis.bw.schema.infrastructure.soap.ContactPoint getContactPointHeader() {
        com.lbg.ib.api.sales.soapapis.bw.schema.infrastructure.soap.ContactPoint bwContactPoint = new com.lbg.ib.api.sales.soapapis.bw.schema.infrastructure.soap.ContactPoint();
        bwContactPoint.setApplicationId(contactPoint.getApplicationId());
        bwContactPoint.setContactPointId(contactPoint.getContactPointId());
        bwContactPoint.setContactPointType(contactPoint.getContactPointType());
        bwContactPoint.setInitialOriginatorId(contactPoint.getInitialOriginatorId());
        bwContactPoint.setInitialOriginatorType(contactPoint.getInitialOriginatorType());
        return bwContactPoint;
    }

    public com.lbg.ib.api.sales.soapapis.bw.lcsm.BapiInformation getBapiInformationHeader() {
        com.lbg.ib.api.sales.soapapis.bw.lcsm.BapiInformation bwBapiInformation = new com.lbg.ib.api.sales.soapapis.bw.lcsm.BapiInformation();
        bwBapiInformation.setBAPIId(bapiInformation.getBAPIId());
        BAPIHeader bapiHeader = new BAPIHeader();
        com.lbg.ib.api.sales.soapapis.commonapi.schema.enterprise.lcsm.BAPIHeader commonBapiHeader = bapiInformation
                .getBAPIHeader();
        bapiHeader.setUseridAuthor(commonBapiHeader.getUseridAuthor());
        bapiHeader.setUserAgent(commonBapiHeader.getUserAgent());
        bapiHeader.setChansecmode(commonBapiHeader.getChansecmode());
        bapiHeader.setInboxidClient("GX");
        bapiHeader.setChanid(commonBapiHeader.getChanid());
        bapiHeader.setSessionid(commonBapiHeader.getSessionid());
        bapiHeader.setIpAddressCaller(commonBapiHeader.getIpAddressCaller());
        bapiHeader.setAcceptLanguage("0");
        bapiHeader.setChansecmode(commonBapiHeader.getChansecmode());
        HostInformation stpartyObo = new HostInformation();
        stpartyObo.setHost(commonBapiHeader.getStpartyObo().getHost());
        stpartyObo.setOcisid(commonBapiHeader.getStpartyObo().getOcisid());
        stpartyObo.setPartyid(commonBapiHeader.getStpartyObo().getPartyid());
        bapiHeader.setStpartyObo(stpartyObo);
        bwBapiInformation.setBAPIHeader(bapiHeader);
        return bwBapiInformation;
    }

}
