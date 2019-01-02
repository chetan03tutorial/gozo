/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.dao.mapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sso.domain.address.PostalAddress;
import com.lbg.ib.api.sso.domain.address.UnstructuredPostalAddress;
import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.sales.dao.MCAHeaderUtility;

import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;

import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.product.eligibility.EligibilityRequestDTO;
import com.lbg.ib.api.sso.domain.product.arrangement.ContactNumber;
import com.lbg.ib.api.sso.domain.product.arrangement.EmployerDetails;
import com.lbg.ib.api.sso.domain.product.arrangement.PostalAddressComponent;
import com.lbg.ib.api.sales.product.domain.arrangement.PrimaryInvolvedParty;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CurrencyAmount;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Customer;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Employer;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Individual;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.IndividualName;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductArrangement;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.StructuredAddress;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.TaxResidencyDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.TelephoneNumber;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.UnstructuredAddress;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.RequestHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;
import com.lbg.ib.api.sales.soapapis.determineeligibility.reqres.DetermineEligibleCustomerInstructionsRequest;

@Component
public class EligibilityRequestMapper {

    private static final String  SERVICE_NAME     = "DetermineEligibleCustomerInstructions";

    private static final String  SERVICE_ACTION   = "determineEligibleCustomerInstructions";

    private static final String  PARTY_IDENTIFIER = "AAGATEWAY";

    private static final String  CURRENT_ADDRESS  = "CURRENT";

    private static final String  PREVIOUS_ADDRESS = "PREVIOUS";

    private static final String  WORK_PHONE       = "Work";

    private static final String  WORK_PHONE_TYPE  = "4";

    private static final String  MOB_PHONE        = "Mobile";

    private static final String  MOB_PHONE_TYPE   = "7";

    private static final String  FIXED_PHONE      = "Fixed";

    private static final String  FIXED_PHONE_TYPE = "1";

    private static final String  COMMA            = ",";

    private static final String  BLANK            = " ";

    private static final String  CLOSING_TAG      = "]";

    @Autowired
    private SessionManagementDAO session;

    @Autowired
    private GBOHeaderUtility     gboHeaderUtility;

    @Autowired
    private ConfigurationDAO     configuration;

    @Autowired
    private MCAHeaderUtility     mcaHeaderUtility;

    /**
     * @param determineEligibleCustomerInstructionsRequestDTO
     * @return
     */
    public DetermineEligibleCustomerInstructionsRequest mapRequest(
            EligibilityRequestDTO determineEligibleCustomerInstructionsRequestDTO) {
        DetermineEligibleCustomerInstructionsRequest rq = populateHeadersAndProductArrangements(
                determineEligibleCustomerInstructionsRequestDTO);
        Customer customerDetails = new Customer();
        Individual isPlayedBy = new Individual();
        isPlayedBy.setBirthDate(calendar(determineEligibleCustomerInstructionsRequestDTO.birthDate()));
        customerDetails.setIsPlayedBy(isPlayedBy);
        customerDetails.setCustomerIdentifier(determineEligibleCustomerInstructionsRequestDTO.customerIdentifier());
        rq.setCustomerDetails(customerDetails);
        return rq;
    }

    /**
     * This method is to populate the customer details with Primary involved
     * party information.
     *
     * @param requestDTO
     * @return DetermineEligibleCustomerInstructionsRequest
     */
    public DetermineEligibleCustomerInstructionsRequest populateRequest(EligibilityRequestDTO requestDTO) {
        DetermineEligibleCustomerInstructionsRequest rq = populateHeadersAndProductArrangements(requestDTO);
        Customer customerDetails = populateCustomerDetails(requestDTO);
        rq.setCustomerDetails(customerDetails);
        return rq;
    }

    /*
     * This method populates the header information and productArrangement for
     * SOAP request.
     */
    private DetermineEligibleCustomerInstructionsRequest populateHeadersAndProductArrangements(
            EligibilityRequestDTO requestDTO) {
        DetermineEligibleCustomerInstructionsRequest rq = new DetermineEligibleCustomerInstructionsRequest();
        rq.setHeader(prepareSoapHeaders());
        rq.setArrangementType(requestDTO.arrangementType());
        String[] candidateInstructions = requestDTO.candidateInstructions();
        rq.setCandidateInstructions(candidateInstructions);
        if (requestDTO.existingProductArrangements() != null && requestDTO.existingProductArrangements().length != 0) {
            rq.setExistingProductArrangments((ProductArrangement[]) (requestDTO.existingProductArrangements()));
        }
        return rq;
    }

    /*
     * This method populates the Customer Details object from
     * PrimaryInvolvedParty
     */
    private Customer populateCustomerDetails(EligibilityRequestDTO requestDTO) {
        PrimaryInvolvedParty primaryInvParty = requestDTO.getPrimaryInvolvedParty();
        Customer customerDetails = new Customer();
        customerDetails.setPartyIdentifier(PARTY_IDENTIFIER);
        customerDetails.setEmailAddress(primaryInvParty.getEmail());
        customerDetails.setPostalAddress(setAddressInformation(primaryInvParty).toArray(
                new com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.PostalAddress[setAddressInformation(
                        primaryInvParty).size()]));
        customerDetails.setTelephoneNumber(
                setPhoneDetails(primaryInvParty).toArray(new TelephoneNumber[setPhoneDetails(primaryInvParty).size()]));
        customerDetails.setIsPlayedBy(populateIsPlayedBy(requestDTO, primaryInvParty));
        com.lbg.ib.api.shared.domain.TaxResidencyDetails taxDetails = primaryInvParty.getTinDetails()
                .getTaxResidencies().iterator().next();
        TaxResidencyDetails taxResidencyDetails = new TaxResidencyDetails();
        taxResidencyDetails.setTaxPayerIdNumber(taxDetails.getTinNumber());
        taxResidencyDetails.setTaxResidencyCountries(new String[] { taxDetails.getTaxResidency() });
        customerDetails.setTaxResidencyDetails(taxResidencyDetails);
        customerDetails.setCustomerIdentifier(requestDTO.customerIdentifier());
        return customerDetails;
    }

    private List<com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.PostalAddress> setAddressInformation(
            PrimaryInvolvedParty primaryInvParty) {

        List<com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.PostalAddress> list = new ArrayList<com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.PostalAddress>();
        list.add(setPostalAddress(primaryInvParty.getCurrentAddress(), CURRENT_ADDRESS));
        if (null != primaryInvParty.getPreviousAddress()) {
            list.add(setPostalAddress(primaryInvParty.getPreviousAddress(), PREVIOUS_ADDRESS));

        }
        return list;
    }

    /*
     * This method populates the Individual Details from PrimaryInvolvedParty
     */
    private Individual populateIsPlayedBy(EligibilityRequestDTO requestDTO, PrimaryInvolvedParty primaryInvParty) {
        Individual isPlayedBy = new Individual();
        isPlayedBy.setIndividualName(new IndividualName[] { setIndividualName(primaryInvParty) });
        isPlayedBy.setBirthDate(calendar(primaryInvParty.getDob()));
        isPlayedBy.setGender(primaryInvParty.getGender());
        isPlayedBy.setResidentialStatus(primaryInvParty.getResidentialStatus());
        if (null != primaryInvParty.getNumberOfDependents()) {
            isPlayedBy.setNumberOfDependents(BigInteger.valueOf(primaryInvParty.getNumberOfDependents()));
        }
        Set<String> nationalities = primaryInvParty.getTinDetails().getNationalities();

        isPlayedBy.setNationality(
                nationalities.toString().substring(1, nationalities.toString().lastIndexOf(CLOSING_TAG)).toString());
        isPlayedBy.setPlaceOfBirth(primaryInvParty.getBirthCity());
        isPlayedBy.setCountryOfBirth(primaryInvParty.getTinDetails().getBirthCountry());
        isPlayedBy.setMaritalStatus(primaryInvParty.getMaritalStatus());
        isPlayedBy.setEmploymentStatus(primaryInvParty.getEmploymentStatus());
        if (null != primaryInvParty.getEmployer()) {
            isPlayedBy.setCurrentEmployer(populateEmployerDetails(primaryInvParty));
        }
        isPlayedBy.setOccupation(primaryInvParty.getOccupnType());
        setCurrentyDetails(primaryInvParty, isPlayedBy);
        return isPlayedBy;
    }

    /*
     * This method is used to set the currency Details
     */
    private void setCurrentyDetails(PrimaryInvolvedParty primaryInvParty, Individual isPlayedBy) {
        CurrencyAmount amount = null;
        if (null != primaryInvParty.getSavingsAmount()) {
            amount = new CurrencyAmount();
            amount.setAmount(BigDecimal.valueOf(primaryInvParty.getSavingsAmount()));
            isPlayedBy.setTotalSavingsAmount(amount);

        }
        if (null != primaryInvParty.getIncome()) {
            amount = new CurrencyAmount();
            amount.setAmount(BigDecimal.valueOf(primaryInvParty.getIncome()));
            isPlayedBy.setNetMonthlyIncome(amount);
        }

        if (null != primaryInvParty.getRentMortgCost()) {
            amount = new CurrencyAmount();
            amount.setAmount(BigDecimal.valueOf(primaryInvParty.getRentMortgCost()));
            isPlayedBy.setMonthlyMortgageAmount(amount);

        }
    }

    /*
     * This method populates the Employer Details from PrimaryInvolvedParty
     */

    private Employer populateEmployerDetails(PrimaryInvolvedParty primaryInvParty) {
        Employer employer = new Employer();
        EmployerDetails details = primaryInvParty.getEmployer();
        employer.setName(details.getName());
        com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.PostalAddress postalAddress = new com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.PostalAddress();
        if (null != details.getAddressLine1()) {
            UnstructuredAddress unstructuredAddress = new UnstructuredAddress();
            unstructuredAddress.setAddressLine1(details.getAddressLine1());
            unstructuredAddress.setAddressLine2(details.getAddressLine2());
            unstructuredAddress.setPostCode(details.getPostcode());
            postalAddress.setUnstructuredAddress(unstructuredAddress);
        }
        employer.setHasPostalAddress(
                new com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.PostalAddress[] { postalAddress });
        return employer;
    }

    /*
     * This method populates the IndividualName Details from
     * PrimaryInvolvedParty
     */
    private IndividualName setIndividualName(PrimaryInvolvedParty primaryInvParty) {
        IndividualName name = new IndividualName();
        name.setPrefixTitle(primaryInvParty.getTitle());
        name.setFirstName(primaryInvParty.getFirstName());
        name.setLastName(primaryInvParty.getLastName());
        if (null != primaryInvParty.getMiddleName()) {
            if (primaryInvParty.getMiddleName().contains(COMMA)) {
                primaryInvParty.getMiddleName().split(COMMA);
                name.setMiddleNames(primaryInvParty.getMiddleName().split(COMMA));
            } else {
                name.setMiddleNames(new String[] { primaryInvParty.getMiddleName() });
            }
        }
        return name;
    }

    /*
     * This method populates the TelephoneDetails from PrimaryInvolvedParty
     */
    private List<TelephoneNumber> setPhoneDetails(PrimaryInvolvedParty primaryInvParty) {
        List<TelephoneNumber> phones = new ArrayList<TelephoneNumber>();
        if (null != primaryInvParty.getMobileNumber()) {
            phones.add(mapTelephoneNumber(primaryInvParty.getMobileNumber(), MOB_PHONE, MOB_PHONE_TYPE));
        }
        if (null != primaryInvParty.getWorkPhone()) {
            phones.add(mapTelephoneNumber(primaryInvParty.getWorkPhone(), WORK_PHONE, WORK_PHONE_TYPE));
        }
        if (null != primaryInvParty.getHomePhone()) {
            phones.add(mapTelephoneNumber(primaryInvParty.getHomePhone(), FIXED_PHONE, FIXED_PHONE_TYPE));
        }
        return phones;
    }

    /*
     * This method maps the Telephone number based on device type and telephone
     * type.
     */
    private TelephoneNumber mapTelephoneNumber(ContactNumber details, String deviceType, String telephoneType) {
        TelephoneNumber number = new TelephoneNumber();
        number.setCountryPhoneCode(details.getCountryCode());
        number.setAreaCode(details.getAreaCode());
        number.setDeviceType(deviceType);
        number.setExtensionNumber(details.getExtNumber());
        number.setPhoneNumber(details.getNumber());
        number.setTelephoneType(telephoneType);
        return number;
    }

    /*
     * This method populates the PostalAddress from PrimaryInvolvedParty. type.
     */
    private com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.PostalAddress setPostalAddress(
            PostalAddressComponent postaladdressComponent, String addressType) {
        com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.PostalAddress postalAddress = new com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.PostalAddress();
        postalAddress.setStatusCode(addressType);
        postalAddress.setIsPAFFormat(postaladdressComponent.getIsPAFFormat());
        postalAddress.setDurationofStay(postaladdressComponent.getDurationOfStay());
        postalAddress.setIsBFPOAddress(postaladdressComponent.getIsBFPOAddress());
        if (null != postaladdressComponent.getStructuredAddress()) {
            postalAddress.setStructuredAddress(populateStructuredAddress(postaladdressComponent));
        } else if (null != postaladdressComponent.getUnstructuredAddress()) {
            UnstructuredPostalAddress address = postaladdressComponent.getUnstructuredAddress();
            UnstructuredAddress usAddress = new UnstructuredAddress();
            usAddress.setAddressLine1(address.getAddressLine1());
            usAddress.setAddressLine2(address.getAddressLine2());
            usAddress.setAddressLine3(address.getAddressLine3());
            usAddress.setAddressLine4(address.getAddressLine4());
            usAddress.setAddressLine5(address.getAddressLine5());
            usAddress.setAddressLine6(address.getAddressLine6());
            usAddress.setAddressLine7(address.getAddressLine7());
            usAddress.setAddressLine8(address.getAddressLine8());
            usAddress.setPointSuffix(address.getDeliveryPointSuffix());
            usAddress.setPostCode(address.getPostcode());
            postalAddress.setUnstructuredAddress(usAddress);
        }
        return postalAddress;
    }

    /*
     * This method populates the StructuredAddress from PrimaryInvolvedParty.
     */
    private StructuredAddress populateStructuredAddress(PostalAddressComponent postaladdressComponent) {
        StructuredAddress sAddress = new StructuredAddress();
        PostalAddress structuredAddress = postaladdressComponent.getStructuredAddress();
        sAddress.setOrganisation(structuredAddress.getOrganisationName());
        sAddress.setBuilding(structuredAddress.getBuildingName());
        sAddress.setBuildingNumber(structuredAddress.getBuildingNumber());
        sAddress.setHouseName(structuredAddress.getBuildingNumber());
        if (null != structuredAddress.getSubBuildingName()) {
            sAddress.setSubBuilding(structuredAddress.getSubBuildingName());
        }
        sAddress.setAddressLinePAFData(
                structuredAddress.getAddressLines().toArray(new String[structuredAddress.getAddressLines().size()]));
        sAddress.setPostTown(structuredAddress.getTown());
        String inBoundPostCode = null;
        String outBoundPostCode = null;
        if (structuredAddress.getPostcode().contains(BLANK)) {
            inBoundPostCode = structuredAddress.getPostcode().split(BLANK)[0];
            outBoundPostCode = structuredAddress.getPostcode().split(BLANK)[1];
        } else if (structuredAddress.getPostcode().length() == 7) {
            inBoundPostCode = structuredAddress.getPostcode().substring(0, 4);
            outBoundPostCode = structuredAddress.getPostcode().substring(4, structuredAddress.getPostcode().length());
        } else if (structuredAddress.getPostcode().length() == 6) {
            inBoundPostCode = structuredAddress.getPostcode().substring(0, 3);
            outBoundPostCode = structuredAddress.getPostcode().substring(3, structuredAddress.getPostcode().length());
        } else if (structuredAddress.getPostcode().length() == 5) {
            inBoundPostCode = structuredAddress.getPostcode().substring(0, 2);
            outBoundPostCode = structuredAddress.getPostcode().substring(2, structuredAddress.getPostcode().length());
        }
        sAddress.setPostCodeOut(outBoundPostCode);
        sAddress.setPostCodeIn(inBoundPostCode);
        sAddress.setPointSuffix(structuredAddress.getDeliveryPointSuffix());
        if (null != structuredAddress.getDistrict()) {
            sAddress.setDistrict(structuredAddress.getDistrict());
        }
        if (null != structuredAddress.getCounty()) {
            sAddress.setCounty(structuredAddress.getCounty());
        }
        return sAddress;
    }

    private RequestHeader prepareSoapHeaders() {
        RequestHeader requestHeader = new RequestHeader();

        List<SOAPHeader> soapHeaders = null;
        if (null != session.getBranchContext()) {
            soapHeaders = mcaHeaderUtility.prepareSoapHeader(SERVICE_ACTION, SERVICE_NAME);

        } else {
            soapHeaders = gboHeaderUtility.prepareSoapHeader(SERVICE_ACTION, SERVICE_NAME);
        }

        if (null != session.getUserContext()) {
            Map<String, Object> map = configuration.getConfigurationItems("ChannelIdMapping");
            requestHeader.setChannelId(map.get(session.getUserContext().getChannelId()).toString());
        }

        requestHeader.setLloydsHeaders(soapHeaders.toArray(new SOAPHeader[soapHeaders.size()]));
        requestHeader.setBusinessTransaction(SERVICE_NAME);
        requestHeader.setInteractionId(session.getSessionId());
        return requestHeader;
    }

    private Calendar calendar(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance;
    }

    public void setSession(SessionManagementDAO session) {
        this.session = session;
    }

    public void setGboHeaderUtility(GBOHeaderUtility gboHeaderUtility) {
        this.gboHeaderUtility = gboHeaderUtility;
    }
}
