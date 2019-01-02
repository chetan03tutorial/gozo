package com.lbg.ib.api.sales.dao.mapper;

import com.lbg.ib.api.sales.common.domain.PegaSwitchTypeEnum;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.switching.domain.SwitchingAccount;
import com.lbg.ib.api.sso.domain.address.PostalAddress;
import com.lbg.ib.api.sso.domain.address.UnstructuredPostalAddress;
import com.lbg.ib.api.sso.domain.product.arrangement.PostalAddressComponent;
import com.lbg.ib.api.sales.soapapis.pega.objects.CreateCaseRequestType;
import com.lbg.ib.api.sales.switching.domain.AccountSwitchingRequest;
import com.lbg.ib.api.sales.switching.domain.SwitchingParty;
import com.lloydsbanking.xml.*;
import org.apache.cxf.common.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.lbg.ib.api.sales.switching.constants.AccountSwitchingConstants.*;
import static com.lbg.ib.api.sales.utils.CommonUtils.convert;
import static com.lbg.ib.api.sales.utils.CommonUtils.stripStringToMaxLength;
import static com.lloydsbanking.xml.SwitchTypeType.FULL;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Component
public class CreateCaseRequestMapper {
    private static final Logger LOG = Logger.getLogger(CreateCaseRequestMapper.class);

    private SessionManagementDAO     sessionManager;

    private CreateCaseAccountMapper  createCaseAccountMapper;

    @Autowired
    public CreateCaseRequestMapper(SessionManagementDAO sessionManager, CreateCaseAccountMapper createCaseAccountMapper){
        this.sessionManager = sessionManager;
        this.createCaseAccountMapper = createCaseAccountMapper;
    }

    public CreateCaseRequestType create(AccountSwitchingRequest accountSwitchingRequest){
        LOG.info("Entering create@CreateCaseRequestMapper");
        final CreateCaseRequestType request = new CreateCaseRequestType();

        final CreateCasePayloadRequestType payloadRequest = new CreateCasePayloadRequestType();
        //PCA-6997: Session ID was causing duplicate TC issues at PEGA so replaced it with combination of sort code and account number
        payloadRequest.setRequestResponseCorrelationId(createRequestResponseCorrelationId(accountSwitchingRequest.getNewAccountDetails()));

        final InitiateSwitchInType initiateSwitchIn = new InitiateSwitchInType();
        initiateSwitchIn.setRequestedByUserName(sessionManager.getUserContext().getPartyId());
        initiateSwitchIn.setRequestedBySystemId(PCA_ONLINE);
        initiateSwitchIn.setSwitchDetails(new SwitchDetailsType[1]);
        initiateSwitchIn.setSwitchDetails(0, getSwitchDetails(accountSwitchingRequest));
        payloadRequest.setInitiateSwitchIn(initiateSwitchIn);

        request.setPayload(payloadRequest);
        LOG.info("Exiting create@CreateCaseRequestMapper");
        return request;
    }

    private String createRequestResponseCorrelationId(final SwitchingAccount switchingAccount){
        return switchingAccount.getSortCode() + switchingAccount.getAccountNumber() + System.currentTimeMillis();
    }

    private SwitchDetailsType getSwitchDetails(AccountSwitchingRequest accountSwitchingRequest) {
        LOG.info("Entering getSwitchDetails@CreateCaseRequestMapper");
        final SwitchDetailsType switchDetails = new SwitchDetailsType();
        switchDetails.setSwitchScenario(IAS_RETAIL_CURRENT_FULL);
        switchDetails.setSwitchType(FULL);
        switchDetails.setSwitchDate(convert(accountSwitchingRequest.getSwitchDate()));
        switchDetails.setNewAccount(createCaseAccountMapper.createNewAccount(accountSwitchingRequest, getPostalAddress(createCaseAccountMapper.primaryParty(accountSwitchingRequest.getParties()))));
        switchDetails.setOldAccount(createCaseAccountMapper.createOldAccountType(accountSwitchingRequest,switchDetails.getNewAccount().getAccountName()));
        switchDetails.setCustomerInterviewDetails(createCustomerInterviewDetails(accountSwitchingRequest));
        LOG.info("Exiting getSwitchDetails@CreateCaseRequestMapper");
        return switchDetails;
    }

    private PartyPostalAddressType getPostalAddress(final SwitchingParty primaryInvolvedParty) {
        LOG.info("Entering getPostalAddress@CreateCaseRequestMapper");
        final PartyPostalAddressType postalAddress = new PartyPostalAddressType();
        if (primaryInvolvedParty.getPostalAddress().getIsBFPOAddress()) {
            LOG.info("BFPO address: "+primaryInvolvedParty.getPostalAddress().getIsBFPOAddress());
            postalAddress.setAddressType(AddressTypeType.MLTO);
        } else {
            LOG.info("Address type is HOME");
            postalAddress.setAddressType(AddressTypeType.HOME);
            postalAddress.setPartyPostalAddress(getAddressType(primaryInvolvedParty.getPostalAddress()));
        }
        LOG.info("Exiting getPostalAddress@CreateCaseRequestMapper");
        return postalAddress;
    }

    private AddressType getAddressType(PostalAddressComponent postalAddressReq) {
        LOG.info("Entering getAddressType@CreateCaseRequestMapper");
        final AddressType addressType = new AddressType();
        addressType.setCountry(COUNTRY);
        checkStructuredAddress(postalAddressReq, addressType);
        checkUnstructuredAddress(postalAddressReq, addressType);
        LOG.info("Exiting getAddressType@CreateCaseRequestMapper");
        return addressType;
    }

    private void checkStructuredAddress(PostalAddressComponent postalAddressReq, AddressType addressType) {
        LOG.info("Entering checkStructuredAddress@CreateCaseRequestMapper");
        final PostalAddress structuredAddress = postalAddressReq.getStructuredAddress();
        if (null != structuredAddress) {
            LOG.info("Found structured address in request...");
            if (!StringUtils.isEmpty(structuredAddress.getBuildingNumber()) && !StringUtils.isEmpty(structuredAddress.getBuildingNumber())) {
                addressType.setHouseNameBuildingNumber(structuredAddress.getBuildingNumber().concat(structuredAddress.getBuildingNumber()));
            }
            if (!StringUtils.isEmpty(structuredAddress.getPostcode())) {
                addressType.setPostCode(structuredAddress.getPostcode());
            }
            addressType.setAddressLine(new String[structuredAddress.getAddressLines().size()]);
            for(int index = 0; index < structuredAddress.getAddressLines().size(); index++) {
                addressType.setAddressLine(index, structuredAddress.getAddressLines().get(index));
            }
        }
        LOG.info("Exiting checkStructuredAddress@CreateCaseRequestMapper");
    }

    private void checkUnstructuredAddress(PostalAddressComponent postalAddressReq, AddressType addressType) {
        LOG.info("Entering checkUnstructuredAddress@CreateCaseRequestMapper");
        final UnstructuredPostalAddress unstructuredAddress = postalAddressReq.getUnstructuredAddress();
        if (null != unstructuredAddress) {
            StringBuilder houseNameBuildingNumberBuilder = new StringBuilder(EMPTY);
            if (!StringUtils.isEmpty(unstructuredAddress.getAddressLine1())) {
                houseNameBuildingNumberBuilder.append(unstructuredAddress.getAddressLine1());
            }
            if (!StringUtils.isEmpty(unstructuredAddress.getAddressLine2())) {
                houseNameBuildingNumberBuilder = new StringBuilder(unstructuredAddress.getAddressLine2()).append(houseNameBuildingNumberBuilder);
            }
            if (!StringUtils.isEmpty(unstructuredAddress.getAddressLine3())) {
                houseNameBuildingNumberBuilder = new StringBuilder(unstructuredAddress.getAddressLine3()).append(houseNameBuildingNumberBuilder);
            }

            final String houseNameBuildingNumber = stripStringToMaxLength(houseNameBuildingNumberBuilder.toString(), ADDRESS_LINE_SIZE);
            LOG.info("houseNameBuildingNumber : "+houseNameBuildingNumber);
            addressType.setHouseNameBuildingNumber(houseNameBuildingNumber);

            String concatenatedAddressLines = concatenateAddressLines(unstructuredAddress.getAddressLine4(), unstructuredAddress.getAddressLine5());
            LOG.info("concatenatedAddressLines : "+concatenatedAddressLines);
            addressType.setAddressLine(new String[2]);
            addressType.setAddressLine(0, concatenatedAddressLines);

            concatenatedAddressLines = concatenateAddressLines(unstructuredAddress.getAddressLine6(), unstructuredAddress.getAddressLine7());
            addressType.setAddressLine(1, concatenatedAddressLines);
            LOG.info("concatenatedAddressLines : "+concatenatedAddressLines);

            if (!StringUtils.isEmpty(unstructuredAddress.getPostcode())) {
                addressType.setPostCode(stripStringToMaxLength(unstructuredAddress.getPostcode(), POST_CODE_SIZE));
            }
        }
        LOG.info("Exiting checkUnstructuredAddress@CreateCaseRequestMapper");
    }


    private String concatenateAddressLines(String addressLineFirst,String addressLineSecond){
        final StringBuilder addressLines = new StringBuilder(EMPTY);
        if(!StringUtils.isEmpty(addressLineFirst)){
            addressLines.append(addressLineFirst);
        }
        if(!StringUtils.isEmpty(addressLineSecond)){
            addressLines.append(addressLineSecond);
        }
        return stripStringToMaxLength(addressLines.toString(),ADDRESS_LINE_SIZE);
    }

    private CustomerInterviewDetailsType createCustomerInterviewDetails(AccountSwitchingRequest accountSwitchingRequest) {
        LOG.info("Entering createCustomerInterviewDetails@CreateCaseRequestMapper");
        final CustomerInterviewDetailsType customerInterviewDetails = new CustomerInterviewDetailsType();
        customerInterviewDetails.setSwitchersOverdraftOfferAgreedIndicator(String.valueOf(accountSwitchingRequest.getCanBeOverDrawn()));
        customerInterviewDetails.setBranchSortCode(accountSwitchingRequest.getOldAccountDetails().getSortCode());
        customerInterviewDetails.setOldAccountProductCategory(CURRENT);
        //customerInterviewDetails.setOldAccountSoleOrJoint(isJoint(accountSwitchingRequest.getParties()) ? SoleOrJointType.Joint : SoleOrJointType.Sole);
        customerInterviewDetails.setOldAccountSoleOrJoint((PegaSwitchTypeEnum.JOINT_TO_JOINT==accountSwitchingRequest.getSwitchingType()) ? SoleOrJointType.Joint : SoleOrJointType.Sole);
        customerInterviewDetails.setChannelId(MODE_ONLINE);
        customerInterviewDetails.setStartedIn(MODE_ONLINE);
        LOG.info("Exiting createCustomerInterviewDetails@CreateCaseRequestMapper");
        return customerInterviewDetails;
    }

}
