package com.lbg.ib.api.sales.dao.mapper;

import com.lbg.ib.api.sales.dto.product.offer.EmploymentDTO;
import com.lbg.ib.api.sales.dto.product.offer.OfferProductDTO;
import com.lbg.ib.api.sales.dto.product.offer.PhoneDTO;
import com.lbg.ib.api.sales.dto.product.offer.address.EmploymentDetailsDTO;
import com.lbg.ib.api.sales.product.domain.lifestyle.CreateServiceArrangement;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.*;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Employer;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.PostalAddress;
import com.lbg.ib.api.sales.soapapis.servicearrangementsetupservice.messages.*;
import com.lbg.ib.api.sales.soapapis.servicearrangementsetupservice.messages.Customer;
import com.lbg.ib.api.sales.soapapis.servicearrangementsetupservice.messages.Individual;
import com.lbg.ib.api.sales.soapapis.servicearrangementsetupservice.messages.IndividualName;
import com.lbg.ib.api.sales.soapapis.servicearrangementsetupservice.messages.ProductArrangement;
import com.lbg.ib.api.sales.soapapis.servicearrangementsetupservice.messages.TelephoneNumber;
import com.lbg.ib.api.shared.validation.AccountType;
import com.lbg.ib.api.sso.domain.mca.BranchContext;
import com.lloydstsb.www.Schema.Enterprise.IFWXML_Extended_Classes.RequestHeader;
import com.lbg.ib.api.sales.soapapis.servicearrangementsetupservice.messages.ArrangementType;


import org.apache.commons.collections.CollectionUtils;
import org.apache.cxf.common.util.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.Arrays.asList;

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
public class CreateArrangementSetupRequestMapper {

    public static final String SERVICE_ARRANGEMENT_TYPE_LIFE_STYLE_BENEFIT = "AVB";
    public static final String ARRANGEMENT_TYPE_ADDED_VALUE_BENEFIT = "ADDED_VALUE_BENEFIT";
    public static final String ADDED_VALUE_BENEFIT_TYPE_LIFE_STYLE_BENEFIT = "001";
    public static final String ACTIVITY_PENDING_SELECTION = "PENDING_SELECTION";
    public static final Map<String, String> PHONE_DEVICE_TYPE_MAP         = new HashMap<String, String>();

    static {
        PHONE_DEVICE_TYPE_MAP.put("Mobile", "7");
        PHONE_DEVICE_TYPE_MAP.put("Fixed", "1");
        PHONE_DEVICE_TYPE_MAP.put("Work", "4");
    }

    public ProductArrangement getRelatedArrangement(CreateServiceArrangement createServiceArrangement) {
        ProductArrangement arrangement = new ProductArrangement();
        ProductArrangementIdentifier productArrangementIdentifier = new ProductArrangementIdentifier();
        productArrangementIdentifier.setAccountNumber(createServiceArrangement.getAccountNumber());
        AlternateID alternateID = new AlternateID();
        alternateID.setValue(createServiceArrangement.getSortCode());
        productArrangementIdentifier.setAlternateId(new AlternateID[]{alternateID});
        arrangement.setObjectReference(productArrangementIdentifier);
        return arrangement;
    }

    public ServiceArrangement getServiceArrangement(CreateServiceArrangement createServiceArrangement, String benefitLookupDesc) {
        ServiceArrangement serviceArrangement = new ServiceArrangement();
        ArrangementType arrangementType = new ArrangementType();
        arrangementType.setName(SERVICE_ARRANGEMENT_TYPE_LIFE_STYLE_BENEFIT);
        serviceArrangement.setHasArrangementType(arrangementType);
        ArrangementAssociation arrangementAssociation = new ArrangementAssociation();
        BenefitArrangement benefitArrangement = new BenefitArrangement();
        benefitArrangement.setName(ARRANGEMENT_TYPE_ADDED_VALUE_BENEFIT);
        ArrangementType arrangementType1 = new ArrangementType();
        arrangementType1.setName(ADDED_VALUE_BENEFIT_TYPE_LIFE_STYLE_BENEFIT);
        benefitArrangement.setHasArrangementType(arrangementType1);
        benefitArrangement.setRelatedEvents(new Activity[]{getActivity(benefitLookupDesc)});
        arrangementAssociation.setRelatedArrangement(benefitArrangement);
        Customer customer = getCustomer(createServiceArrangement);
        serviceArrangement.setArrangementAssociations(new ArrangementAssociation[]{arrangementAssociation});
        serviceArrangement.setRoles(new Customer[] {customer});
        return serviceArrangement;
    }



    private Activity getActivity(String benefitLookupDesc) {
        Activity activity = new Activity();
        Category category = new Category();
        category.setName(benefitLookupDesc);
        activity.setEventCategory(category);

        ObjectReference objectReference = new ObjectReference();
        objectReference.setId(ACTIVITY_PENDING_SELECTION);
        activity.setObjectReference(objectReference);
        return activity;
    }

    private Customer getCustomer(CreateServiceArrangement createServiceArrangement) {
        Customer customer = new Customer();
        Individual individual = new Individual();
        Individual individual1 = playedBy(createServiceArrangement);
        if (individual1 != null) {
            if (individual1.getBirthDate() != null) {
                individual.setBirthDate(individual1.getBirthDate());
            }
            if (individual1.getName()==null) {
                individual.setName(new IndividualName[]{individualName(createServiceArrangement)});
            }
        }
        ContactPreference contactPreference = new ContactPreference();
        ElectronicAddress electronicAddress = new ElectronicAddress();
        electronicAddress.setUserid(createServiceArrangement.getEmailAddress());
        TelephoneNumber[] telephoneNumbers = telephones(createServiceArrangement);
        contactPreference.setContactPoints(new ContactPoint[]{electronicAddress,telephoneNumbers[0]});
        individual.setContactPreferences(new ContactPreference[]{contactPreference});
        customer.setInvolvedParty(individual);

        return customer;
    }


    private TelephoneNumber[] telephones(final CreateServiceArrangement createServiceArrangement) {
        final List<TelephoneNumber> numbers = new ArrayList<TelephoneNumber>();
        return telephones(
                asList(createServiceArrangement.getMobilePhone(), createServiceArrangement.getHomePhone(), createServiceArrangement.getWorkPhone()),
                numbers);
    }

    private TelephoneNumber[] telephones(final List<PhoneDTO> phoneNumbers, final List<TelephoneNumber> numbers) {
        for (final PhoneDTO phone : phoneNumbers) {
            if (phone == null) {
                continue;
            }
            final TelephoneNumber telephoneNumber = new TelephoneNumber();
            telephoneNumber.setCountryPhoneCode(phone.countryCode());
            if (!StringUtils.isEmpty(phone.telephoneAreaCode())) {
                telephoneNumber.setAreaCode(phone.telephoneAreaCode());
            }

            telephoneNumber.setLocalNumber(phone.phoneNumber());
            if (!StringUtils.isEmpty(phone.getExtNumber())) {
                telephoneNumber.setExtension(phone.getExtNumber());
            }
            telephoneNumber.setDeviceTypeCode(phone.type());
            telephoneNumber.setPhoneTypeCode(PHONE_DEVICE_TYPE_MAP.get(phone.type()));

            if ("7".equalsIgnoreCase(telephoneNumber.getPhoneTypeCode())) {
                telephoneNumber.setPhoneSequenceNumber(phone.phoneNumber());
            }

            numbers.add(telephoneNumber);
        }
        return numbers.toArray(new TelephoneNumber[numbers.size()]);

    }

    private Individual playedBy(final CreateServiceArrangement createServiceArrangement) {
        final Individual individual = new Individual();


        individual.setName(new IndividualName[] { individualName(createServiceArrangement) });
        if(createServiceArrangement.getBirthDate()!=null) {
            individual.setBirthDate(calendar(createServiceArrangement.getBirthDate()));
        }
        if(createServiceArrangement.getGender()!=null) {
            individual.setGender(Gender.fromValue(createServiceArrangement.getGender()));
        }
        if(createServiceArrangement.getMaritalStatus()!=null) {
            individual.setMaritalStatus(MaritalStatus.fromValue(createServiceArrangement.getMaritalStatus()));
        }
        individual.setDependentChildren(createServiceArrangement.getNumberOfDependants());
        if(createServiceArrangement.getEmploymentStatus()!=null) {
            individual.setEmploymentStatus(EmploymentStatus.fromValue(createServiceArrangement.getEmploymentStatus()));
        }
        employer(individual, createServiceArrangement);

        Place place = new Place();
        place.setName(createServiceArrangement.getBirthCity());
        individual.setBirthPlace(place);
        return individual;
    }

    private IndividualName individualName(final CreateServiceArrangement createServiceArrangement) {
        final IndividualName name = new IndividualName();
        name.setPrefixTitle(InvolvedPartyNamePrefixType.fromValue(createServiceArrangement.getPrefixTitleName()));//
        name.setFirstName(createServiceArrangement.getFirstName());//
        name.setLastName(createServiceArrangement.getLastName());//
        if (!StringUtils.isEmpty(createServiceArrangement.getMiddleName())) {
            name.setMiddleNames(new String[] { createServiceArrangement.getMiddleName() });
        }
        return name;
    }
    private void employer(final Individual individual, final CreateServiceArrangement createServiceArrangement) {
        final EmploymentDTO employment = createServiceArrangement.getEmploymentDTO();
        String employmentDuration = "0000";
        if (employment!=null && employment.details() != null) {
            final Employer employer = new Employer();
            employer.setName(employment.details().employerName());
            final PostalAddress postalAddress = new PostalAddress();
            final UnstructuredAddress unstructuredAddress = new UnstructuredAddress();
            unstructuredAddress.setAddressLine1(employment.details().employerAddressLine1());
            unstructuredAddress.setAddressLine2(employment.details().employerAddressLine2());
            unstructuredAddress.setPostCode(employment.details().employerPostcode());
            postalAddress.setUnstructuredAddress(unstructuredAddress);
            postalAddress.setIsBFPOAddress(false);
            postalAddress.setIsPAFFormat(false);
            employer.setHasPostalAddress(new PostalAddress[] { postalAddress });
            individual.setCurrentEmployer(employer.getName());

        }
    }

    private Calendar calendar(final Date date) {
        final Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance;
    }

}
