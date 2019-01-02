package com.lbg.ib.api.sales.bankwizard.dao;


import com.lbg.ib.api.sales.bankwizard.domain.BankWizardExtractResponse;
import com.lbg.ib.api.sales.dto.bankwizard.ValidateBankDetailstResponseDTO;
import com.lbg.ib.api.sales.soapapis.bw.arrangement.DepositArrangement;
import com.lbg.ib.api.sales.soapapis.bw.arrangement.ProductArrangement;
import com.lbg.ib.api.sales.soapapis.bw.arrangementnegotiation.VerifyProductArrangementDetailsResponse;
import com.lbg.ib.api.sales.soapapis.bw.common.AttributeCondition;
import com.lbg.ib.api.sales.soapapis.bw.common.AttributeConditionValue;
import com.lbg.ib.api.sales.soapapis.bw.common.Condition;
import com.lbg.ib.api.sales.soapapis.bw.common.RuleCondition;
import com.lbg.ib.api.sales.soapapis.bw.involvedparty.InvolvedParty;
import com.lbg.ib.api.sales.soapapis.bw.involvedparty.InvolvedPartyRole;
import com.lbg.ib.api.sales.soapapis.bw.involvedparty.InvolvedPartyRoleType;
import com.lbg.ib.api.sales.soapapis.bw.involvedparty.OrganizationUnit;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * @author Debashish Bhattacharjee
 * @version 1.0
 * @since 16thFeb018
 ***********************************************************************/
@RunWith(MockitoJUnitRunner.class)
public class BankWizardExtractResponseTest {

    @Mock
    LoggerDAO logerDao;

    @Mock
    private ChannelBrandingDAO brandingDAO;

    @Mock
    private GalaxyErrorCodeResolver resolver;

    @InjectMocks
    BankWizardExtractResponse bankWizardExtractResponse;

    @Test
    public void testExtractResponseForBOSWithBankNotInCASSIntraBank(){
        when(brandingDAO.getChannelBrand())
                .thenReturn(withResult(new ChannelBrandDTO("BosRetail", "BOS", "IBS")));
        VerifyProductArrangementDetailsResponse verifyProductArrangementDetailsResponse = new VerifyProductArrangementDetailsResponse();
        ProductArrangement productArrangement = new DepositArrangement();
        RuleCondition condition = new RuleCondition();
        condition.setName("CONDITIONS");

        AttributeCondition attributeConditionOne = new AttributeCondition();
        attributeConditionOne.setDataItem("WARNINGS");

        AttributeConditionValue attributeConditionValue = new AttributeConditionValue();
        attributeConditionValue.setCode("79");
        attributeConditionOne.setHasAttributeConditionValues(new AttributeConditionValue[]{attributeConditionValue});
        condition.setRuleAttributes(new AttributeCondition[]{attributeConditionOne});
        productArrangement.setHasObjectConditions(new Condition[]{condition});
        verifyProductArrangementDetailsResponse.setVerificationResult(new ProductArrangement[]{productArrangement});

        InvolvedPartyRole involvedPartyRole = new InvolvedPartyRole();
        InvolvedPartyRoleType involvedPartyRoleType = new InvolvedPartyRoleType();
        involvedPartyRoleType.setValue("BRANCH");
        involvedPartyRole.setType(involvedPartyRoleType);

        InvolvedParty involvedParty = new OrganizationUnit();
        RuleCondition involvedPartyCondition = new RuleCondition();
        involvedPartyCondition.setName("BRANCH_DATA");

        AttributeCondition attributeCondition = new AttributeCondition();
        attributeConditionValue = new AttributeConditionValue();
        attributeConditionValue.setValue("BANK OF SCOTLAND");
        attributeConditionValue.setCode("1006");
        attributeCondition.setHasAttributeConditionValues(new AttributeConditionValue[]{attributeConditionValue});
        involvedPartyCondition.setRuleAttributes(new AttributeCondition[]{attributeCondition});
        involvedParty.setHasObjectConditions(new Condition[]{involvedPartyCondition});
        involvedPartyRole.setInvolvedParty(involvedParty);
        productArrangement.setRoles(new InvolvedPartyRole[]{involvedPartyRole});

        ValidateBankDetailstResponseDTO validateBankDetailstResponseDTO = bankWizardExtractResponse.extractResponse(verifyProductArrangementDetailsResponse);
        assertThat(validateBankDetailstResponseDTO.isIntraBrandSwitching(), is(true));
        assertThat(validateBankDetailstResponseDTO.getBankName(), is("BANK OF SCOTLAND"));
        assertThat(validateBankDetailstResponseDTO.getBankInCASS().getIsBankInCASS(),is(false));
    }

    @Test
    public void testExtractResponseForBOSWithBankInCASSIntraBank(){
        when(brandingDAO.getChannelBrand())
                .thenReturn(withResult(new ChannelBrandDTO("BosRetail", "BOS", "IBS")));
        VerifyProductArrangementDetailsResponse verifyProductArrangementDetailsResponse = new VerifyProductArrangementDetailsResponse();
        ProductArrangement productArrangement = new DepositArrangement();
        RuleCondition condition = new RuleCondition();
        condition.setName("CONDITIONS");

        AttributeCondition attributeConditionOne = new AttributeCondition();
        attributeConditionOne.setDataItem("WARNINGS");

        AttributeConditionValue attributeConditionValue = new AttributeConditionValue();
        attributeConditionValue.setCode("81");
        attributeConditionOne.setHasAttributeConditionValues(new AttributeConditionValue[]{attributeConditionValue});
        condition.setRuleAttributes(new AttributeCondition[]{attributeConditionOne});
        productArrangement.setHasObjectConditions(new Condition[]{condition});
        verifyProductArrangementDetailsResponse.setVerificationResult(new ProductArrangement[]{productArrangement});

        InvolvedPartyRole involvedPartyRole = new InvolvedPartyRole();
        InvolvedPartyRoleType involvedPartyRoleType = new InvolvedPartyRoleType();
        involvedPartyRoleType.setValue("BRANCH");
        involvedPartyRole.setType(involvedPartyRoleType);

        InvolvedParty involvedParty = new OrganizationUnit();
        RuleCondition involvedPartyCondition = new RuleCondition();
        involvedPartyCondition.setName("BRANCH_DATA");

        AttributeCondition attributeCondition = new AttributeCondition();
        attributeConditionValue = new AttributeConditionValue();
        attributeConditionValue.setValue("BANK OF SCOTLAND");
        attributeConditionValue.setCode("1006");
        attributeCondition.setHasAttributeConditionValues(new AttributeConditionValue[]{attributeConditionValue});
        involvedPartyCondition.setRuleAttributes(new AttributeCondition[]{attributeCondition});
        involvedParty.setHasObjectConditions(new Condition[]{involvedPartyCondition});
        involvedPartyRole.setInvolvedParty(involvedParty);
        productArrangement.setRoles(new InvolvedPartyRole[]{involvedPartyRole});

        ValidateBankDetailstResponseDTO validateBankDetailstResponseDTO = bankWizardExtractResponse.extractResponse(verifyProductArrangementDetailsResponse);
        assertThat(validateBankDetailstResponseDTO.isIntraBrandSwitching(), is(true));
        assertThat(validateBankDetailstResponseDTO.getBankName(), is("BANK OF SCOTLAND"));
        assertThat(validateBankDetailstResponseDTO.getBankInCASS().getIsBankInCASS(),is(true));
    }


    @Test
    public void testExtractResponseForBOSWithBankInCASSInterBank(){
        when(brandingDAO.getChannelBrand())
                .thenReturn(withResult(new ChannelBrandDTO("BosRetail", "BOS", "IBS")));
        VerifyProductArrangementDetailsResponse verifyProductArrangementDetailsResponse = new VerifyProductArrangementDetailsResponse();
        ProductArrangement productArrangement = new DepositArrangement();
        RuleCondition condition = new RuleCondition();
        condition.setName("CONDITIONS");

        AttributeCondition attributeConditionOne = new AttributeCondition();
        attributeConditionOne.setDataItem("WARNINGS");

        AttributeConditionValue attributeConditionValue = new AttributeConditionValue();
        attributeConditionValue.setCode("81");
        attributeConditionOne.setHasAttributeConditionValues(new AttributeConditionValue[]{attributeConditionValue});
        condition.setRuleAttributes(new AttributeCondition[]{attributeConditionOne});
        productArrangement.setHasObjectConditions(new Condition[]{condition});
        verifyProductArrangementDetailsResponse.setVerificationResult(new ProductArrangement[]{productArrangement});

        InvolvedPartyRole involvedPartyRole = new InvolvedPartyRole();
        InvolvedPartyRoleType involvedPartyRoleType = new InvolvedPartyRoleType();
        involvedPartyRoleType.setValue("BRANCH");
        involvedPartyRole.setType(involvedPartyRoleType);

        InvolvedParty involvedParty = new OrganizationUnit();
        RuleCondition involvedPartyCondition = new RuleCondition();
        involvedPartyCondition.setName("BRANCH_DATA");

        AttributeCondition attributeCondition = new AttributeCondition();
        attributeConditionValue = new AttributeConditionValue();
        attributeConditionValue.setValue("HALIFAX");
        attributeConditionValue.setCode("1006");
        attributeCondition.setHasAttributeConditionValues(new AttributeConditionValue[]{attributeConditionValue});
        involvedPartyCondition.setRuleAttributes(new AttributeCondition[]{attributeCondition});
        involvedParty.setHasObjectConditions(new Condition[]{involvedPartyCondition});
        involvedPartyRole.setInvolvedParty(involvedParty);
        productArrangement.setRoles(new InvolvedPartyRole[]{involvedPartyRole});

        ValidateBankDetailstResponseDTO validateBankDetailstResponseDTO = bankWizardExtractResponse.extractResponse(verifyProductArrangementDetailsResponse);
        assertThat(validateBankDetailstResponseDTO.isIntraBrandSwitching(), is(false));
        assertThat(validateBankDetailstResponseDTO.getBankName(), is("HALIFAX"));
        assertThat(validateBankDetailstResponseDTO.getBankInCASS().getIsBankInCASS(),is(true));
    }
}
