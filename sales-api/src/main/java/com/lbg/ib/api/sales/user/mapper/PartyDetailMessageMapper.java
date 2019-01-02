package com.lbg.ib.api.sales.user.mapper;

import static com.lbg.ib.api.sales.shared.util.domain.MapperUtility.convertArrayToList;
import static com.lbg.ib.api.sales.shared.util.domain.MapperUtility.isOf;
import static com.lbg.ib.api.sales.shared.util.domain.MapperUtility.iterateForType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ResultCondition;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.RetrieveMandatePartyRequest;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.RetrieveMandatePartyResponse;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.common.AlternateId;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.common.Condition;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.common.ObjectReference;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.common.RuleCondition;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.involvedparty.ContactPoint;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.involvedparty.Customer;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.involvedparty.ElectronicAddress;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.involvedparty.Individual;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.involvedparty.IndividualName;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.involvedparty.InvolvedParty;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.involvedparty.PostalAddress;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.involvedparty.PostalAddressComponent;
import com.lbg.ib.api.sales.soapapis.involvedpartymanagement.schema.enterprise.lcsm.RequestHeader;
import com.lbg.ib.api.sales.user.domain.PartyDetails;

public class PartyDetailMessageMapper {

    public static final String ENQUIRE_CUSTOMER_DETAILS = "ENQUIRE_CUSTOMER_DETAILS";
    public static final String TRIGGER = "TRIGGER";
    public static final String ZERO_STRING = "0";
    public static final String MAXIMUM_REPEAT_GROUP_QUANTITY = "MAXIMUM_REPEAT_GROUP_QUANTITY";
    public static final String PARTY_IDENTIFIER = "PARTY_IDENTIFIER";
    public static final String EXTERNAL_SYSTEM_IDENTIFIER = "EXTERNAL_SYSTEM_IDENTIFIER";
    public static final String NINETEEN_STRING = "19";
    public static final String EXTERNAL_SYSTEM = "EXTERNAL_SYSTEM";

    private PartyDetailMessageMapper() {

    }

    private static AlternateId buildAlternateId(String key, String value, String source) {
        AlternateId alternateId = new AlternateId();
        alternateId.setAttributeString(key);
        alternateId.setSourceLogicalId(source);
        alternateId.setValue(value);
        return alternateId;
    }

    private static ObjectReference buildObjectReference(String keyGroupType, AlternateId[] alternateIds) {
        ObjectReference objectReference = new ObjectReference();
        objectReference.setAlternateId(alternateIds);
        objectReference.setKeyGroupType(keyGroupType);
        return objectReference;
    }

    public static RetrieveMandatePartyRequest buildRetrieveMandatePartyRequest(String internalUserId, String ocisId) {

        RetrieveMandatePartyRequest request = new RetrieveMandatePartyRequest();
        RequestHeader header = new RequestHeader();
        request.setRequestHeader(header);
        InvolvedParty involvedParty = new Individual();
        request.setInvolvedParty(involvedParty);

        AlternateId[] alternateIds = new AlternateId[2];
        alternateIds[0] = buildAlternateId("PARTY_IDENTIFIER", internalUserId, "I");
        alternateIds[1] = buildAlternateId("CUSTOMER_IDENTIFIER", ocisId, null);

        involvedParty.setObjectReference(buildObjectReference("InvolvedParty", alternateIds));

        List<Condition> conditionList = new ArrayList<Condition>();

        RuleCondition subRuleCondition = new RuleCondition();
        subRuleCondition.setName("EstablishTelephoneRequirement");

        RuleCondition condition = new RuleCondition();
        condition.setName("TRIGGER");
        condition.setResult("True");
        condition.setSubrules(new RuleCondition[]{subRuleCondition});

        conditionList.add(condition);
        Condition[] conditionArray = conditionList.toArray(new Condition[conditionList.size()]);
        involvedParty.setHasConditions(conditionArray);
        return request;
    }

    private static void validateExternalServiceResponse(ResultCondition resultCondition) {
        com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.Condition conditions[] = resultCondition
                .getExtraConditions();
        for (com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.Condition condition : conditions) {
            if (condition.getReasonCode() != 0) {
                throw new ServiceException(
                        new ResponseError(condition.getReasonCode().toString(), condition.getReasonText()));
            }
        }
    }

    public static PartyDetails mapMandatePartyResponse(RetrieveMandatePartyResponse response, boolean isBranch) {
        PartyDetails partyDetails = new PartyDetails();
        ResultCondition resultCondition = response.getResponseHeader().getResultCondition();
        validateExternalServiceResponse(resultCondition);
        InvolvedParty invParty = response.getInvolvedParty();
        if (invParty == null) {
            throw new ServiceException(new ResponseError(ResponseErrorConstants.USER_NOT_FOUND,
                    "Unable to retrieve Involved Party Details"));
        }
        ContactPoint[] contactPoints = invParty.getContactPoint();
        if (contactPoints == null) {
            throw new ServiceException(new ResponseError(ResponseErrorConstants.USER_NOT_FOUND,
                    "Unable to retrieve Involved Party Demographics Details"));
        }
        IndividualName partyName = getInvolvedPartyName(invParty);
        partyDetails.setFirstName(partyName.getFirstName());
        partyDetails.setLastName(partyName.getLastName());
        partyDetails.setMiddleName(partyName.getMiddleNames());
        partyDetails.setTitle(partyName.getPrefixTitle());
        partyDetails.setEmail(extractEmail(contactPoints));
        Map<String, String[]> addressMap = extractAddress(contactPoints);
        if (isBranch) {
            partyDetails.setPostalCode(addressMap.get("Postal Code")[0]);
            partyDetails.setAddressLines(addressMap.get("Address Lines"));
        } else {
            partyDetails.setPostalCode(getMaskedString(addressMap
                    .get("Postal Code")[0]));
        }
        return partyDetails;
    }

    public static Map<String, String[]> extractAddress(ContactPoint[] contactPoints) {
        Map<String, String[]> addressMap = new LinkedHashMap<String, String[]>();
        PostalAddress postalAddress = iterateForType(contactPoints, PostalAddress.class);
        List<PostalAddressComponent> addressComponents = convertArrayToList(postalAddress.getAddressComponents());
        for (PostalAddressComponent addressComponent : addressComponents) {
            addressMap.put(addressComponent.getType().getValue(), addressComponent.getValue());
        }
        return addressMap;
    }

    public static IndividualName getInvolvedPartyName(InvolvedParty party) {
        Customer customer = (Customer) party.getIsPlayingPrimaryRole();
        if (customer != null && isOf(IndividualName.class, customer.getCustomerName())) {
            return (IndividualName) customer.getCustomerName();
        }
        throw new ServiceException(new ResponseError(ResponseErrorConstants.USER_NOT_FOUND,
                "Unable to retrieve Involved Party Name Details"));

    }

    private static String extractEmail(ContactPoint[] contactPoints) {
        ElectronicAddress address = iterateForType(contactPoints, ElectronicAddress.class);
        return address == null ? null : address.getEmail();
    }

    private static String getMaskedString(String someString) {
        String maskedString = null;
        if (null != someString && someString.length() > 4) {
            int length = someString.length();
            StringBuilder newString = new StringBuilder(length);
            for (int i = 0; i < (length - 4); i++) {
                newString.append("*");
            }
            newString.append(someString.substring(length - 4, length));
            maskedString = newString.toString();
        } else {
            maskedString = someString;
        }
        return maskedString;
    }

}
