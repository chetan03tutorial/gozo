/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.colleagues.involvedparty.mapper;

import java.util.ArrayList;
import java.util.List;

import com.lbg.ib.api.sales.paperless.dto.Account;
import com.lbg.ib.api.sales.paperless.dto.UserPreferences;
import com.lbg.ib.api.sales.shared.mapper.BaserMapper;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lloydstsb.lcsm.arrangement.DepositArrangement;
import com.lloydstsb.lcsm.arrangement.FinanceServiceArrangement;
import com.lloydstsb.lcsm.arrangement.ProductArrangement;
import com.lloydstsb.lcsm.common.AlternateId;
import com.lloydstsb.lcsm.common.Condition;
import com.lloydstsb.lcsm.common.ObjectReference;
import com.lloydstsb.lcsm.common.RuleCondition;
import com.lloydstsb.lcsm.involvedparty.CommunicationProfile;
import com.lloydstsb.lcsm.involvedparty.ContactMedium;
import com.lloydstsb.lcsm.involvedparty.ContactPoint;
import com.lloydstsb.lcsm.involvedparty.ContactPreference;
import com.lloydstsb.lcsm.involvedparty.ContactPurpose;
import com.lloydstsb.lcsm.involvedparty.ElectronicAddress;
import com.lloydstsb.lcsm.involvedparty.Individual;
import com.lloydstsb.lcsm.involvedparty.InvolvedParty;
import com.lloydstsb.lcsm.involvedparty.InvolvedPartyName;
import com.lloydstsb.lcsm.involvedparty.InvolvedPartyRole;
import com.lloydstsb.lcsm.involvedparty.InvolvedPartyRoleType;
import com.lloydstsb.lcsm.involvedparty.Organization;
import com.lloydstsb.lcsm.involvedpartymanagement.ModifyCommunicationProfilesRequest;
import com.lloydstsb.lcsm.product.Product;
import com.lloydstsb.lcsm.product.ProductType;

/**
 * Mapper of Communication Profile.
 *
 * @author tkhann
 */
public class ModifyCommProfileMessageMapper extends BaserMapper {

    public static final String CUSTOMER                        = "CUSTOMER";
    public static final String COMMUNICATION_PREFERENCE_OPTION = "COMMUNICATION_PREFERENCE_OPTION";
    public static final String CHANGE_REASON_CODE              = "CHANGE_REASON_CODE";
    public static final String CUSTOMER_REQUEST                = "Customer Request";

    private ModifyCommProfileMessageMapper() {
    }

    /**
     * Request creation for Updating preferences.
     *
     * @param preferences
     * @param ocisId
     * @param partyId
     * @return
     */
    @TraceLog
    public static ModifyCommunicationProfilesRequest createModifyCommunicationProfilesRequest(
            UserPreferences preferences, String ocisId, String partyId, String nameText) {
        ModifyCommunicationProfilesRequest modifyCommunicationProfilesRequest = new ModifyCommunicationProfilesRequest();
        Individual indPartyId = setPartyDetails(ocisId, partyId);
        InvolvedPartyName brandInvName = new InvolvedPartyName();
        brandInvName.setNameText(nameText);
        Organization brandOrg = new Organization();
        brandOrg.setName(brandInvName);
        indPartyId.setAssociatedInvolvedParties(new InvolvedParty[] { brandOrg });
        setCommuniationPreferenceModify(indPartyId, preferences.getEmailAddress());
        List<ProductArrangement> prodArrangementList = new ArrayList<ProductArrangement>();
        for (Account account : preferences.getAccounts()) {
            setAccountDetails(prodArrangementList, account);
        }
        ProductArrangement[] prodArrangementArray = (ProductArrangement[]) prodArrangementList
                .toArray((new ProductArrangement[prodArrangementList.size()]));
        RuleCondition switchRulCondition = new RuleCondition();
        switchRulCondition.setName("PAPERLESS_SWITCH_ON_INDICATOR");
        switchRulCondition.setResult("1");
        CommunicationProfile correspondenceSwitchCommProfile = new CommunicationProfile();
        correspondenceSwitchCommProfile.setHasObjectConditions(new Condition[] { switchRulCondition });
        RuleCondition salsaRuleCondition = new RuleCondition();
        salsaRuleCondition.setUtiliseRule(true);
        salsaRuleCondition.setName("IS_CALL_SALSA");
        indPartyId.setHasObjectConditions(new Condition[] { salsaRuleCondition });
        modifyCommunicationProfilesRequest
                .setCommunicationProfiles(new CommunicationProfile[] { correspondenceSwitchCommProfile });
        modifyCommunicationProfilesRequest.setSuppliedArrangements(prodArrangementArray);
        modifyCommunicationProfilesRequest.setInvolvedParty(indPartyId);
        return (modifyCommunicationProfilesRequest);
    }

    /**
     * Method to update account details.
     *
     * @param prodArrangementList
     * @param account
     */
    private static void setAccountDetails(List<ProductArrangement> prodArrangementList, Account account) {
        ProductArrangement productArrangement = null;
        InvolvedPartyRole invPartyRole = new InvolvedPartyRole();
        InvolvedPartyRoleType invRoleType = new InvolvedPartyRoleType();
        invRoleType.setValue(CUSTOMER);
        invPartyRole.setType(invRoleType);
        CommunicationProfile statementProfile = new CommunicationProfile();
        CommunicationProfile correspondenceCommProfile = new CommunicationProfile();
        ObjectReference externalObjRef = new ObjectReference();
        setProductIdentifers(account, externalObjRef);
        if (null != account.getCardNumber()) {
            productArrangement = new FinanceServiceArrangement();
            setCardNumber(account, externalObjRef, productArrangement);
        } else {
            productArrangement = new DepositArrangement();
            setAccountDetails(account, externalObjRef, productArrangement, invPartyRole);
        }
        if (null != account.getStatementType()) {
            setStatementProfile(account, statementProfile);
        }
        if (null != account.getCorrespondanceType()) {
            setCorrespondenceProfile(account, correspondenceCommProfile);
        }
        invPartyRole.setContactPreferences(new ContactPreference[] { statementProfile, correspondenceCommProfile });
        productArrangement.setRoles(new InvolvedPartyRole[] { invPartyRole });
        prodArrangementList.add(productArrangement);
    }

    /**
     * @param account
     * @param correspondenceCommProfile
     */
    private static void setCorrespondenceProfile(Account account, CommunicationProfile correspondenceCommProfile) {
        RuleCondition rulCondition = new RuleCondition();
        rulCondition.setName(COMMUNICATION_PREFERENCE_OPTION);
        RuleCondition rulConditionReasonCode = new RuleCondition();
        rulConditionReasonCode.setName(CHANGE_REASON_CODE);
        rulConditionReasonCode.setResult(CUSTOMER_REQUEST);
        correspondenceCommProfile.setContactInstructions("CORRESPONDENCE");
        correspondenceCommProfile.setAllowedPurposes(new ContactPurpose[] { ContactPurpose.value4 });
        rulCondition.setResult(account.getCorrespondanceType());
        correspondenceCommProfile.setHasObjectConditions(new Condition[] { rulCondition, rulConditionReasonCode });
    }

    /**
     * @param account
     * @param statementProfile
     */
    private static void setStatementProfile(Account account, CommunicationProfile statementProfile) {
        RuleCondition rulCondition = new RuleCondition();
        rulCondition.setName(COMMUNICATION_PREFERENCE_OPTION);
        RuleCondition rulConditionReasonCode = new RuleCondition();
        rulConditionReasonCode.setName(CHANGE_REASON_CODE);
        rulConditionReasonCode.setResult(CUSTOMER_REQUEST);
        statementProfile.setContactInstructions("Statement");
        statementProfile.setAllowedPurposes(new ContactPurpose[] { ContactPurpose.value4 });
        rulCondition.setResult(account.getStatementType());
        statementProfile.setHasObjectConditions(new Condition[] { rulCondition, rulConditionReasonCode });
    }

    /**
     * populate Modify Statement Profile Request.
     *
     * @param aIndividual
     *            aInvolvedPartyDTO - An instance of Individual and
     *            InvolvedPartyDTO.
     * @param aInvolvedPartyDTO
     *            InvolvedPartyDTO.
     */
    @TraceLog
    private static void setCommuniationPreferenceModify(Individual aIndividual, String emailAddress) {
        ContactPoint communicationConPoint = new ElectronicAddress();
        communicationConPoint.setContactPointID(emailAddress);
        aIndividual.setContactPoint(new ContactPoint[] { communicationConPoint });
        ContactPreference conPreference = new ContactPreference();
        conPreference.setPreferredMedium(ContactMedium.value1);
        if (null != aIndividual.getInvolvedPartyRole()) {
            aIndividual.getInvolvedPartyRole().setContactPreferences(new ContactPreference[] { conPreference });
        }
    }

    /**
     * Setting party details.
     *
     * @return Individual
     */
    @TraceLog
    private static Individual setPartyDetails(String ocisId, String partyId) {
        AlternateId[] alternateIds = new AlternateId[2];
        alternateIds[0] = buildAlternateId("CUSTOMER_IDENTIFIER", ocisId);
        alternateIds[1] = buildAlternateId("INTERNET_BANKING_PARTY_IDENTIFIER", partyId);
        InvolvedPartyRole partyIdInvRole = new InvolvedPartyRole();
        partyIdInvRole.setObjectReference(buildObjectReference("InvolvedPartyRole", alternateIds));
        partyIdInvRole.setType(new InvolvedPartyRoleType());
        partyIdInvRole.getType().setValue(CUSTOMER);
        Individual indPartyId = new Individual();
        indPartyId.setInvolvedPartyRole(partyIdInvRole);
        return (indPartyId);
    }

    /**
     * populate Modify Statement Profile Request.
     *
     * @param aArrangementDTO
     *            aObjectReference aProductArrangement- An instance of
     *            ArrangementDTO ObjectReference ProductArrangement.
     * @param aArrangementDTO
     *            ArrangementDTO.
     * @param aObjectReference
     *            aProductArrangement- An instance of.
     * @param aProductArrangement
     *            ProductArrangement.
     */
    @TraceLog
    private static void setCardNumber(Account account, ObjectReference aObjectReference,
            ProductArrangement aProductArrangement) {
        Product creditCardProduct = new Product();
        creditCardProduct.setObjectReference(aObjectReference);
        creditCardProduct.setName(account.getName());
        AlternateId creditCardNumber = buildAlternateId("CARD_NUMBER", account.getCardNumber());
        ProductType hasProductType = new ProductType();
        hasProductType.setName("CARD");
        ObjectReference creditCardObj = new ObjectReference();
        creditCardObj.setAlternateId(new AlternateId[] { creditCardNumber });
        creditCardProduct.setHasProductType(hasProductType);
        aProductArrangement.setProduct(creditCardProduct);
        aProductArrangement.setObjectReference(creditCardObj);
    }

    /**
     * populate Modify Statement Profile Request.
     *
     * @param Account
     *            accountent- An instance of ArrangementDTO
     * @param ObjectReference
     *            aObjectReference.
     * @param aProductArrangement
     *            ObjectReference.
     * @param anInvPartyRole
     *            ProductArrangement.
     */
    @TraceLog
    private static void setAccountDetails(Account account,
            ObjectReference aObjectReference, ProductArrangement aProductArrangement,
            InvolvedPartyRole anInvPartyRole) {
        if (null != account) {
            AlternateId extPartyIdTx = buildAlternateId("EXTERNAL_PARTY_IDENTIFIER_TEXT", account.getExternalPartyIdentifierText());
            ObjectReference partyIDObj = new ObjectReference();
            partyIDObj.setAlternateId(new AlternateId[] { extPartyIdTx });
            Individual individal = new Individual();
            individal.setObjectReference(partyIDObj);
            anInvPartyRole.setInvolvedParty(individal);
            ObjectReference objRefSortCode = new ObjectReference();
            AlternateId sortCodeNumber = buildAlternateId("SORT_CODE", account.getSortCode());
            objRefSortCode.setAlternateId(new AlternateId[] { sortCodeNumber });
            anInvPartyRole.setObjectReference(objRefSortCode);
            Product accountTypeProduct = new Product();
            accountTypeProduct.setObjectReference(aObjectReference);
            accountTypeProduct.setName(account.getName());
            AlternateId accountNumber = buildAlternateId("ACCOUNT_NUMBER", account.getAccountNumber());
            ObjectReference accountObj = new ObjectReference();
            accountObj.setAlternateId(new AlternateId[] { accountNumber });
            ProductType hasProductType = new ProductType();
            accountTypeProduct.setHasProductType(hasProductType);
            hasProductType.setName("ACCOUNT");
            aProductArrangement.setProduct(accountTypeProduct);
            aProductArrangement.setObjectReference(accountObj);
        }
    }

    /**
     * Populate Modify Statement Profile Request.
     *
     * @param Account
     *            account - An instance of ArrangementDTO and ObjectReference.
     */
    @TraceLog
    private static void setProductIdentifers(Account account, ObjectReference aObjectReference) {
        if (null != account) {
            AlternateId externalSys = new AlternateId();
            externalSys.setAttributeString("PRODUCT_IDENTIFIER");
            externalSys.setSourceLogicalId(String.valueOf(account.getExternalSystem()));
            AlternateId externalAltId = new AlternateId();
            externalAltId.setAttributeString("EXTERNAL_SYSTEM_PRODUCT_IDENTIFIER");
            String accountType = account.getType();
            externalAltId.setValue(accountType.substring(1));
            externalAltId.setSourceLogicalId(String.valueOf(account.getExternalSystem()));
            AlternateId productIdAlt = buildAlternateId("EXTERNAL_SYSTEM_PRODUCT_HELD_IDENTIFIER",
                    account.getExternalSystemProductHeldId());
            aObjectReference.setAlternateId(new AlternateId[] { externalAltId, productIdAlt, externalSys });
        }
    }
}