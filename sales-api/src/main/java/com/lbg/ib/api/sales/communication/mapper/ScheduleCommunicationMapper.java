/**********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.  
 *
 * All Rights Reserved.   
 ***********************************************************************/

package com.lbg.ib.api.sales.communication.mapper;

import com.lbg.ib.api.sales.common.constant.Constants.CommunicationConstants;
import com.lbg.ib.api.sales.communication.domain.ScheduleEmailSmsResponse;
import com.lbg.ib.api.sales.communication.dto.ScheduleEmailDTO;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lloydstsb.ea.lcsm.RequestHeader;
import com.lloydstsb.lcsm.common.*;
import com.lloydstsb.lcsm.communication.Communication;
import com.lloydstsb.lcsm.communication.CommunicationContent;
import com.lloydstsb.lcsm.communicationmanagement.ScheduleCommunicationRequest;
import com.lloydstsb.lcsm.event.ScheduledEventDescriptor;
import com.lloydstsb.lcsm.involvedparty.*;
import com.lloydstsb.lcsm.resourceitem.SpecifiedContent;
import com.lloydstsb.lcsm.resourceitem.SpecifiedContentDescriptor;
import com.lloydstsb.lcsm.resourceitem.SpecifiedContentDescriptorType;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class ScheduleCommunicationMapper {

    private ScheduleCommunicationMapper() {

    }

    public static ScheduleCommunicationRequest buildRequest(ScheduleEmailDTO dto) {
        ScheduleCommunicationRequest request = new ScheduleCommunicationRequest();
        Communication communication = getScheduleCommunicationDetails(dto);
        request.setRequestHeader(new RequestHeader());
        request.setCommunication(communication);
        return request;
    }

    public static ScheduleEmailSmsResponse buildResponse(Map<String,Map<String, List<String>>> emailSentInfo){
        ScheduleEmailSmsResponse response = new ScheduleEmailSmsResponse();
        response.setEmailSentInfo(emailSentInfo);
        return response;
    }

    public static Communication getScheduleCommunicationDetails(ScheduleEmailDTO dto) {

        Map<String, String> contentKeys = dto.getContentKeys();
        Communication communication = new Communication();

        ContactMedium contactMedium = ContactMedium.fromString(dto.getMedium());
        communication.setMedium(contactMedium);

        String customerType = contentKeys.get("IB.Product.Mnemonic");

        ContactPoint contactPoint = setContactPoint(contactMedium, dto.getEmail());
        /*contactPoint = setContactPoint(contactMedium, userRequest.getContactNumber(), party.getMobileNumber());*/
        communication.setIsSentVia(contactPoint);

        AlternateId[] alternateIds = setTemplateId(dto.getTemplateId());

        Organization anInvolvedParty = new Organization();
        InvolvedPartyName involvedPartyName = buildInvolvedPartyName(customerType, dto.getChannel());
        anInvolvedParty.setName(involvedPartyName);

        InvolvedPartyRole brandRole = buildBrandRole(anInvolvedParty);
        brandRole.setInvolvedParty(anInvolvedParty);

        communication.setTargettedRole(new InvolvedPartyRole[]{brandRole});

        CommunicationContent[] communicationContentArray = setContentKeys(contentKeys, buildSpecifiedContentDescriptor(SpecifiedContentDescriptorType.TemplateId, buildObjectReference(alternateIds)));
        communication.setCommunicationContent(communicationContentArray);

        Calendar calGMT = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calGMT.set(Calendar.SECOND, calGMT.get(Calendar.SECOND) + 300);
        ScheduledEventDescriptor scheduledEventDescriptor = new ScheduledEventDescriptor();
        scheduledEventDescriptor.setScheduledStartTime(calGMT);
        communication.setScheduleDetails(new ScheduledEventDescriptor[]{scheduledEventDescriptor});

        communication.setHasObjectConditions(setTriggerCondition());

        return communication;
    }

    private static Condition[] setTriggerCondition() {

        RuleCondition smsRuleCondition = buildRuleCondition("SOURCE", "MOBILE");
        RuleCondition triggeredRuleCondition = buildRuleCondition("TRIGGER", "SCHEDULE_COMMUNICATION");
        return new Condition[]{smsRuleCondition, triggeredRuleCondition};

    }

    private static ContactPoint setContactPoint(final ContactMedium contactMedium, final String email) {
        ContactPoint contactPoint = buildContactPointReference(contactMedium);
        contactPoint.setContactPointID(email);
        return contactPoint;
    }

    private static ContactPoint buildContactPointReference(ContactMedium contactMedium) {
        ContactPoint contactPoint = null;
        if (ContactMedium.value1.equals(contactMedium) || ContactMedium.value22.equals(contactMedium)) {
            contactPoint = new ElectronicAddress();
        } else if (ContactMedium.value6.equals(contactMedium)) {
            contactPoint = new TelephoneNumber();
        } else {
            throw new ServiceException(new ResponseError("1220230", "Invalid Contact medium"));
        }
        return contactPoint;
    }

    private static InvolvedPartyName buildInvolvedPartyName(String customerType, String brandName) {
        InvolvedPartyName involvedPartyName = new InvolvedPartyName();
        if ((CommunicationConstants.PRODUCT_MNEMONIC_SME.equalsIgnoreCase(customerType) || CommunicationConstants.PRODUCT_MNEMONIC_RBB.equalsIgnoreCase(customerType)) && CommunicationConstants.BRAND_LLOYDS.equalsIgnoreCase(brandName)) {
            involvedPartyName.setNameText(CommunicationConstants.CHANNEL_STL);
        } else if ((CommunicationConstants.PRODUCT_MNEMONIC_SME.equalsIgnoreCase(customerType) || CommunicationConstants.PRODUCT_MNEMONIC_RBB.equalsIgnoreCase(customerType)) && CommunicationConstants.BRAND_BOS.equalsIgnoreCase(brandName)) {
            involvedPartyName.setNameText(CommunicationConstants.CHANNEL_STS);
        } else {
            involvedPartyName.setNameText(brandName);
        }
        return involvedPartyName;
    }

    private static CommunicationContent[] setContentKeys(Map<String, String> contentKeys, SpecifiedContent anSpecContent) {

        CommunicationContent[] commContentArr;
        if (null != contentKeys && (contentKeys.size() > 0)) {

            commContentArr = new CommunicationContent[contentKeys.size()];

            String[] aKey = contentKeys.keySet().toArray(new String[]{});

            for (int index = 0; index < contentKeys.size(); index++) {
                commContentArr[index] = new CommunicationContent();
                AttributeConditionValue anAttrCondValue = new AttributeConditionValue();

                anAttrCondValue.setCode(aKey[index]);
                anAttrCondValue.setValue(contentKeys.get(aKey[index]));

                RuleCondition ruleCond = new RuleCondition();
                ruleCond.setName("CONTENT_KEY");
                AttributeCondition attrCond = new AttributeCondition();
                attrCond.setHasAttributeConditionValues(new AttributeConditionValue[]{anAttrCondValue});
                ruleCond.setRuleAttributes(new AttributeCondition[]{attrCond});
                if (index == 0) {

                    anSpecContent.setHasObjectConditions(new Condition[]{ruleCond});
                    commContentArr[index].setSpecifiedContent(anSpecContent);
                } else {
                    SpecifiedContent specContent = new SpecifiedContent();
                    specContent.setHasObjectConditions(new Condition[]{ruleCond});
                    commContentArr[index].setSpecifiedContent(specContent);
                }

                commContentArr[index].setOrder(BigInteger.ZERO);

            }
        } else {
            commContentArr = new CommunicationContent[]{new CommunicationContent()};
            commContentArr[0].setSpecifiedContent(anSpecContent);
        }
        return (commContentArr);
    }

    private static AlternateId buildAlternateId(String identifier, String value) {
        final AlternateId alternateId = new AlternateId();
        alternateId.setAttributeString(identifier);
        alternateId.setValue(value);
        return alternateId;
    }

    private static SpecifiedContent buildSpecifiedContentDescriptor(SpecifiedContentDescriptorType type, ObjectReference reference) {
        SpecifiedContent specifiedContent = new SpecifiedContent();
        SpecifiedContentDescriptor specifiedContentDescriptor = new SpecifiedContentDescriptor();
        specifiedContentDescriptor.setType(type);
        specifiedContent.setObjectReference(reference);
        specifiedContent.setHasDescriptors(new SpecifiedContentDescriptor[]{specifiedContentDescriptor});
        return specifiedContent;
    }

    private static ObjectReference buildObjectReference(AlternateId[] alternateIds) {
        final ObjectReference anObjectReference = new ObjectReference();
        anObjectReference.setAlternateId(alternateIds);
        return anObjectReference;
    }

    private static RuleCondition buildRuleCondition(String name, String result) {
        RuleCondition anRuleCond = new RuleCondition();
        anRuleCond.setName(name);
        anRuleCond.setResult(result);
        return anRuleCond;
    }

    private static AlternateId[] setTemplateId(String templateId) {
        return new AlternateId[]{buildAlternateId("TEMPLATE_IDENTIFIER", templateId)};
    }

    private static InvolvedPartyRole buildBrandRole(Organization anInvolvedParty) {
        InvolvedPartyRole brandRole = new InvolvedPartyRole();
        InvolvedPartyRoleType brandroleType = new InvolvedPartyRoleType();
        brandroleType.setValue("ORGANIZATION");
        brandRole.setType(brandroleType);
        return brandRole;
    }
}
