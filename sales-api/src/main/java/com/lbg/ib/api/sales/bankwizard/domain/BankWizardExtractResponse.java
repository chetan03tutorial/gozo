package com.lbg.ib.api.sales.bankwizard.domain;

import com.lbg.ib.api.sales.soapapis.bw.involvedparty.InvolvedParty;
import com.lbg.ib.api.sales.soapapis.bw.involvedparty.InvolvedPartyRole;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dto.bankwizard.ValidateBankDetailstResponseDTO;
import com.lbg.ib.api.sales.soapapis.bw.arrangement.ProductArrangement;
import com.lbg.ib.api.sales.soapapis.bw.arrangementnegotiation.VerifyProductArrangementDetailsResponse;
import com.lbg.ib.api.sales.soapapis.bw.common.AttributeCondition;
import com.lbg.ib.api.sales.soapapis.bw.common.AttributeConditionValue;
import com.lbg.ib.api.sales.soapapis.bw.common.Condition;
import com.lbg.ib.api.sales.soapapis.bw.common.RuleCondition;

import java.util.HashMap;
import java.util.Map;

import static com.lbg.ib.api.sales.common.constant.Constants.BankWizardConstants.*;

@Component
public class BankWizardExtractResponse {

    @Autowired
    LoggerDAO logerDao;

    @Autowired
    private ChannelBrandingDAO brandingDAO;

    @Autowired
    private GalaxyErrorCodeResolver resolver;

    private static Map<String,String> bankNameMap = new HashMap<String,String>();

    static {
        bankNameMap.put("LLOYDS","LLOYDS");
        bankNameMap.put("HALIFAX","HALIFAX");
        bankNameMap.put("BOS","Bank of Scotland");
    }

    @TraceLog
    public ValidateBankDetailstResponseDTO extractResponse(
            VerifyProductArrangementDetailsResponse verifyProductArrangementDetailsResponse) {
        boolean isvalid = true;
        ProductArrangement verificationResult = verifyProductArrangementDetailsResponse.getVerificationResult(0);
        Condition[] conditions = verificationResult.getHasObjectConditions();
        BankInCASS bankInCASS = null;
        for (Condition condition : conditions) {
            if (CONDITIONS.equals(condition.getName())) {
                RuleCondition ruleConditon = (RuleCondition) condition;
                isvalid = evaluateConditions(ruleConditon);
                bankInCASS = validateBankInCASS(ruleConditon);
            }
        }

        String bankName = fetchBankName(verifyProductArrangementDetailsResponse);
        boolean isIntraBrandSwitch = intraBandSwitching(bankName);
        return new ValidateBankDetailstResponseDTO(isvalid,bankInCASS,bankName,isIntraBrandSwitch);
    }

    public boolean intraBandSwitching(String bankName){
        String brand = getBrand();
        String bankNamePattern = bankNameMap.get(brand);
        if(bankName!=null && bankName.toLowerCase().contains(bankNamePattern.toLowerCase())){
            return true;
        }
        return false;
    }
    public String getBrand() throws ServiceException {
        DAOResponse<ChannelBrandDTO> brandDto = brandingDAO.getChannelBrand();
        DAOResponse.DAOError error = brandDto.getError();
        if (error != null) {
            throw new ServiceException(resolver.resolve(error.getErrorCode()));
        }
        return brandDto.getResult().getBrand();
    }

    public String fetchBankName(VerifyProductArrangementDetailsResponse verifyProductArrangementDetailsResponse){
        ProductArrangement verificationResult = verifyProductArrangementDetailsResponse.getVerificationResult(0);
        InvolvedPartyRole[] roles = verificationResult.getRoles();
        String bankName = null;
        for(InvolvedPartyRole role : roles){
            if(role.getType()!=null && BRANCH.equalsIgnoreCase(role.getType().getValue())) {
                InvolvedParty involvedParty = role.getInvolvedParty();
                Condition[] conditions =  involvedParty.getHasObjectConditions();
                if (ArrayUtils.isNotEmpty(conditions)) {
                    bankName = extractBankNameFromConditions(conditions);
                }
            }
            if(bankName!=null){
                break;
            }
        }
        return bankName;
    }


    public String extractBankNameFromConditions(Condition[] conditions){
        String bankName = null;
        for (Condition condition : conditions) {
            if(condition instanceof RuleCondition) {
                RuleCondition ruleCondition = (RuleCondition) condition;
                if (BRANCH_DATA.equals(condition.getName())) {
                    AttributeCondition[] attributeConditions = ruleCondition.getRuleAttributes();
                    if (ArrayUtils.isNotEmpty(attributeConditions)) {
                        AttributeCondition attributeCondition = attributeConditions[0];
                        AttributeConditionValue[] attributeConditionValues = attributeCondition.getHasAttributeConditionValues();
                        bankName = extractBankNameFromAttributConditionValues(attributeConditionValues);
                    }
                }
                if (bankName != null) {
                    break;
                }
            }
        }
        return bankName;
    }

    public String extractBankNameFromAttributConditionValues(AttributeConditionValue[] attributeConditionValues){
        String bankName = null;
        if (ArrayUtils.isNotEmpty(attributeConditionValues)) {
            for(AttributeConditionValue attributeConditionValue : attributeConditionValues) {
                if (BANK_NAME_CODE.equalsIgnoreCase(attributeConditionValue.getCode())) {
                    bankName = attributeConditionValue.getValue();
                    break;
                }
            }
        }
        return bankName;
    }
    private boolean evaluateConditions(RuleCondition ruleConditon) {
        boolean isvalid = true;
        if (ruleConditon.getRuleAttributes() != null) {
            for (AttributeCondition ruleAttribute : ruleConditon.getRuleAttributes()) {
                isvalid = checkErrors(ruleAttribute, FATAL_ERRORS);
                isvalid = isvalid && checkErrors(ruleAttribute, ERROR);
                isvalid = isvalid && checkWarnings(ruleAttribute);
                isvalid = isvalid && checkInformation(ruleAttribute);
                if (!isvalid) {
                    break;
                }
            }
        }
        return isvalid;
    }

    private boolean checkInformation(AttributeCondition ruleAttribute) {
        if (ruleAttribute.getHasAttributeConditionValues() != null && INFORMATION.equals(ruleAttribute.getDataItem())) {
            for (AttributeConditionValue attributeConditionValue : ruleAttribute.getHasAttributeConditionValues()) {
                String code = attributeConditionValue.getCode();
                if (logerDao.debugEnabled()) {
                    logerDao.logDebug(BankWizardExtractResponse.class,
                            code + ": is the information code and the value is: " + attributeConditionValue.getValue());
                }
                if (code.equals(BRANCH_NOT_WITH_IN_PARENT_COUUNTRY)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkWarnings(AttributeCondition ruleAttribute) {

        if (ruleAttribute.getHasAttributeConditionValues() != null && WARNINGS.equals(ruleAttribute.getDataItem())) {
            for (AttributeConditionValue attributeConditionValue : ruleAttribute.getHasAttributeConditionValues()) {
                String code = attributeConditionValue.getCode();
                if (logerDao.debugEnabled()) {
                    logerDao.logDebug(BankWizardExtractResponse.class,
                            code + ": is the warning code and the value is: " + attributeConditionValue.getValue());
                }
                if (code.equals(UNSUPPORTED_DD_ON_ACCOUNT) || code.equals(UNSUPPORTED_DD_ON_BRANCH)
                        || code.equals(FOREIGN_CURRENCY_ACCOUNT)) {
                    return false;

                }
            }
        }
        return true;
    }

    public BankInCASS validateBankInCASS(RuleCondition ruleConditon){

        BankInCASS bankInCASS = new BankInCASS();
        if (ruleConditon.getRuleAttributes() != null) {
            extractBankInCASSInformation(ruleConditon,bankInCASS);
        }
        return bankInCASS;
    }

    public void extractBankInCASSInformation(RuleCondition ruleConditon,BankInCASS bankInCASS){
        for (AttributeCondition ruleAttribute : ruleConditon.getRuleAttributes()) {
            if (ruleAttribute.getHasAttributeConditionValues() != null && WARNINGS.equals(ruleAttribute.getDataItem())) {
                for (AttributeConditionValue attributeConditionValue : ruleAttribute.getHasAttributeConditionValues()) {
                    String code = attributeConditionValue.getCode();
                    if (BANK_NOT_IN_CASS.contains(code)) {
                        bankInCASS.setIsBankInCASS(false);
                        bankInCASS.setCode(code);
                        break;
                    }
                }
            }
        }
    }

    private boolean checkErrors(AttributeCondition ruleAttribute, String dataItem) {
        if (ruleAttribute.getHasAttributeConditionValues() != null && dataItem.equals(ruleAttribute.getDataItem())) {
            for (AttributeConditionValue attributeConditionValue : ruleAttribute.getHasAttributeConditionValues()) {
                String code = attributeConditionValue.getCode();
                if (logerDao.debugEnabled()) {
                    logerDao.logDebug(BankWizardExtractResponse.class,
                            code + ": is the error code and the value is: " + attributeConditionValue.getValue());
                }
            }
            return ruleAttribute.getHasAttributeConditionValues() != null ? false : true;
        }
        return true;
    }

}
