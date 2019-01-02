package com.lbg.ib.api.sales.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.lbg.ib.api.sales.dao.constants.CommonConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sso.domain.address.PostalAddress;
import com.lbg.ib.api.sales.address.domain.Postcode;
import com.lbg.ib.api.sso.domain.address.UnstructuredPostalAddress;
import com.lbg.ib.api.sales.address.service.AddressService;
import com.lbg.ib.api.sales.common.constant.Constants.OfferConstants;
import com.lbg.ib.api.shared.exception.InvalidFormatException;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dao.switches.SwitchesManagementDAO;
import com.lbg.ib.api.sales.dto.product.offer.PhoneDTO;
import com.lbg.ib.api.sales.dto.product.offer.address.PostalAddressComponentDTO;
import com.lbg.ib.api.sales.dto.product.offer.address.StructuredPostalAddressDTO;
import com.lbg.ib.api.sales.dto.product.offer.address.UnstructuredPostalAddressDTO;
import com.lbg.ib.api.sso.domain.product.arrangement.ContactNumber;
import com.lbg.ib.api.sso.domain.product.arrangement.PostalAddressComponent;
import com.lbg.ib.api.sales.product.domain.arrangement.Arrangement;


import static org.apache.commons.lang3.StringUtils.substring;

/**
 * Created by dbhatt on 9/19/2016.
 */
@Component
public class CommonUtils {

    public static final String   SIRA_SWITCH = "SW_EnSIRAFrdChk";

    @Autowired
    private SessionManagementDAO session;
    @Autowired
    private AddressService       addressService;

    private static final String  SPACE       = " ";

    @Autowired
    LoggerDAO                    logger;

    public CommonUtils(SessionManagementDAO session) {
        this.session = session;
    }

    public CommonUtils(SessionManagementDAO session, AddressService addressService) {
        this.session = session;
        this.addressService = addressService;
    }

    public CommonUtils() {
        // Not required
    }

    public PhoneDTO phone(ContactNumber number, String type) {
        if (number == null) {
            return null;
        }
        return new PhoneDTO(number.getCountryCode(), number.getAreaCode(), number.getNumber(), number.getExtNumber(),
                type);
    }

    public PostalAddress fetchStructuredAddress(UnstructuredPostalAddress uaddress) {
        try {
            Postcode postCode = new Postcode(uaddress.getPostcode());

            List<PostalAddress> postalAddresses = addressService.check(postCode);
            String houseNumber = null;
            String houseName = null;
            if (StringUtils.isNotEmpty(uaddress.getAddressLine1())
                    && Character.isDigit(uaddress.getAddressLine1().charAt(0))) {
                String[] parts = uaddress.getAddressLine1().split(SPACE);
                houseNumber = parts[0];
            } else {
                houseName = uaddress.getAddressLine1();
            }
            List<PostalAddress> addressList = new ArrayList<PostalAddress>();
            if (null != postalAddresses) {
                for (PostalAddress postalAddress : postalAddresses) {
                    if (houseNumber != null && houseNumber.equals(postalAddress.getBuildingNumber())) {
                        addressList.add(postalAddress);
                    } else if (houseName != null && houseName.equalsIgnoreCase(postalAddress.getBuildingName())) {
                        addressList.add(postalAddress);
                    }
                }
            }
            if (addressList.size() == 1) {
                return addressList.get(0);
            }
        } catch (ServiceException e) {
            logger.logException(this.getClass(), e);
        } catch (InvalidFormatException e) {
            logger.logException(this.getClass(), e);
        } catch (Exception e) {
            logger.logException(this.getClass(), e);
        }

        return null;
    }

    public PostalAddressComponentDTO address(PostalAddressComponent address, String type,boolean isAddPartyJourney) {
        if (address == null) {
            return null;
        }
        if (address.getUnstructuredAddress() != null) {
            UnstructuredPostalAddress uaddress = address.getUnstructuredAddress();
            UnstructuredPostalAddressDTO unstructuredPostalAddressDTO = null;
            PostalAddress saddress = null;
            if (isAuthJourney() || isExistingAddParty() || address.getIsBFPOAddress()) {
                saddress = fetchStructuredAddress(uaddress);
                if (saddress == null) {
                    if(isAddPartyJourney && !isExistingAddParty()) {
                        unstructuredPostalAddressDTO = populateUnstructuredAddressDTO(uaddress);
                    }else{
                        unstructuredPostalAddressDTO = populateAuthUnstructuredAddressDTO(uaddress);
                    }
                }
            } else {
                unstructuredPostalAddressDTO = populateUnstructuredAddressDTO(uaddress);
            }

            if (saddress == null) {
                return new PostalAddressComponentDTO(unstructuredPostalAddressDTO, address.getDurationOfStay(), type,
                        uaddress.getDeliveryPointSuffix(), uaddress.getPostcode(), address.getIsPAFFormat(),
                        address.getIsBFPOAddress());
            } else {
                return new PostalAddressComponentDTO(populateStructuredAddress(saddress), address.getDurationOfStay(),
                        type, saddress.getDeliveryPointSuffix(), saddress.getPostcode(), address.getIsPAFFormat(),
                        address.getIsBFPOAddress());
            }

        } else if (address.getStructuredAddress() != null) {
            com.lbg.ib.api.sso.domain.address.PostalAddress saddress = address.getStructuredAddress();
            return new PostalAddressComponentDTO(populateStructuredAddress(saddress), address.getDurationOfStay(), type,
                    saddress.getDeliveryPointSuffix(), saddress.getPostcode(), address.getIsPAFFormat(),
                    address.getIsBFPOAddress());
        }
        return null;
    }

    private StructuredPostalAddressDTO populateStructuredAddress(
            com.lbg.ib.api.sso.domain.address.PostalAddress address) {
        return new StructuredPostalAddressDTO(address.getDistrict(), address.getTown(), address.getCounty(),
                address.getOrganisationName(), address.getSubBuildingName(), address.getBuildingName(),
                address.getBuildingNumber(), address.getAddressLines());
    }

    public UnstructuredPostalAddressDTO populateUnstructuredAddressDTO(UnstructuredPostalAddress address) {
        /*
         * return new UnstructuredPostalAddressDTO(address.getAddressLine1(),
         * address.getAddressLine2(), address.getAddressLine3(),
         * address.getAddressLine4(), address.getAddressLine5(),
         * address.getAddressLine6(), address.getAddressLine7(),
         * address.getAddressLine8());
         */

        String addressLineOne;
        String addressLineTwo;
        String addressLineThree;
        String addressLineFour;

        // Code to Avoid Null Pointer Exceptions
        if (address.getAddressLine1() == null) {
            address.setAddressLine1(OfferConstants.EMPTY_STRING);
        }
        if (address.getAddressLine2() == null) {
            address.setAddressLine2(OfferConstants.EMPTY_STRING);
        }
        if (address.getAddressLine3() == null) {
            address.setAddressLine3(OfferConstants.EMPTY_STRING);
        }
        if (address.getAddressLine4() == null) {
            address.setAddressLine4(OfferConstants.EMPTY_STRING);
        }
        if (address.getAddressLine5() == null) {
            address.setAddressLine5(OfferConstants.EMPTY_STRING);
        }
        if (address.getAddressLine6() == null) {
            address.setAddressLine6(OfferConstants.EMPTY_STRING);
        }
        if (address.getAddressLine7() == null) {
            address.setAddressLine7(OfferConstants.EMPTY_STRING);
        }
        if (address.getAddressLine8() == null) {
            address.setAddressLine8(OfferConstants.EMPTY_STRING);
        }

        // Code to Avoid Null Pointer Exceptions
        if (StringUtils.isEmpty(address.getAddressLine3()) && StringUtils.isEmpty(address.getAddressLine2())) {
            addressLineOne = new StringBuilder(address.getAddressLine1()).append(OfferConstants.STRING_SPACE)
                    .append(address.getAddressLine4()).toString();
            if (StringUtils.isEmpty(address.getAddressLine5())) {
                addressLineTwo = address.getAddressLine6();
                addressLineThree = address.getAddressLine7();
                addressLineFour = null;

            } else {
                addressLineTwo = address.getAddressLine5();
                addressLineThree = address.getAddressLine6();
                addressLineFour = address.getAddressLine7();

            }

        } else {
            if (StringUtils.isEmpty(address.getAddressLine2())) {
                addressLineOne = new StringBuilder(OfferConstants.FLAT).append(OfferConstants.STRING_SPACE)
                        .append(address.getAddressLine3()).toString();
            } else {
                addressLineOne = StringUtils.isEmpty(address.getAddressLine3()) ? address.getAddressLine2()
                        : new StringBuilder(address.getAddressLine3()).append(OfferConstants.STRING_SPACE)
                                .append(address.getAddressLine2()).toString();
            }

            addressLineTwo = new StringBuilder(address.getAddressLine1()).append(OfferConstants.STRING_SPACE)
                    .append(address.getAddressLine4()).toString();

            if (StringUtils.isEmpty(address.getAddressLine5())) {
                addressLineThree = address.getAddressLine6();
                addressLineFour = address.getAddressLine7();

            } else {
                addressLineThree = address.getAddressLine5();
                addressLineFour = new StringBuilder(address.getAddressLine6()).toString();
                if (!StringUtils.isEmpty(address.getAddressLine7())) {
                    addressLineFour = new StringBuilder(addressLineFour).append(address.getAddressLine7()).toString();
                }
            }

        }

        addressLineOne = addressLineOne != null ? addressLineOne.trim() : null;
        addressLineTwo = addressLineTwo != null ? addressLineTwo.trim() : null;
        addressLineThree = addressLineThree != null ? addressLineThree.trim() : null;
        addressLineFour = addressLineFour != null ? addressLineFour.trim() : null;

        return new UnstructuredPostalAddressDTO(addressLineOne, addressLineTwo, addressLineThree, addressLineFour,
                address.getAddressLine8());
    }

    public UnstructuredPostalAddressDTO populateAuthUnstructuredAddressDTO(UnstructuredPostalAddress uaddress) {
        return new UnstructuredPostalAddressDTO(uaddress.getAddressLine1(), uaddress.getAddressLine2(),
                uaddress.getAddressLine3(), uaddress.getAddressLine4(), uaddress.getAddressLine5(),
                uaddress.getAddressLine6(), uaddress.getAddressLine7(), uaddress.getAddressLine8());
    }

    private boolean isAuthJourney() {
        return null != session.getUserInfo();
    }


    public PostalAddressComponentDTO relatedAddress(PostalAddressComponent address, String type) {
        if (address == null) {
            return null;
        }
        if (address.getUnstructuredAddress() != null) {
            UnstructuredPostalAddress uaddress = address.getUnstructuredAddress();
            UnstructuredPostalAddressDTO unstructuredPostalAddressDTO = populateAuthUnstructuredAddressDTO(uaddress);
            return new PostalAddressComponentDTO(unstructuredPostalAddressDTO, address.getDurationOfStay(), type,
                    uaddress.getDeliveryPointSuffix(), uaddress.getPostcode(), address.getIsPAFFormat(),
                    address.getIsBFPOAddress());
        } else if (address.getStructuredAddress() != null) {
            com.lbg.ib.api.sso.domain.address.PostalAddress saddress = address.getStructuredAddress();
            return new PostalAddressComponentDTO(populateStructuredAddress(saddress), address.getDurationOfStay(), type,
                    saddress.getDeliveryPointSuffix(), saddress.getPostcode(), address.getIsPAFFormat(),
                    address.getIsBFPOAddress());
        }
        return null;
    }

    public boolean getSiraSwitchStatus(ChannelBrandingDAO channelService, SwitchesManagementDAO switchesDAO,
            SessionManagementDAO session) {

        if (null != session.getBranchContext()) {
            return false;
        }
        boolean switchStatus = false;
        String channel = channelService.getChannelBrand().getResult().getChannelId();
        DAOResponse<HashMap<String, Boolean>> switchesResponse = switchesDAO.getSwitches(channel);
        if (switchesResponse != null) {
            HashMap<String, Boolean> globalApplicationSwitches = switchesDAO.getSwitches(channel).getResult();
            if (globalApplicationSwitches != null) {
                Boolean switchState = globalApplicationSwitches.get(SIRA_SWITCH);
                if (switchState != null) {
                    switchStatus = switchState;
                }
            }
        }
        return switchStatus;
    }

    public boolean isBranchContext() {
        return null != session.getBranchContext();
    }


    private boolean isExistingAddParty() {
        return session.getAddPartyContext() != null && session.getAddPartyContext().isExistingParty();
    }

    public boolean isAuth(SessionManagementDAO session) {
        return null != session.getUserInfo();
    }

    public Integer defaultZero(Integer input) {
        return input == null ? 0 : input;
    }

    public void setSession(SessionManagementDAO session) {
        this.session = session;
    }

    public static Date convert(final String dateString) throws ParseException {
        return convert(dateString, "yyyy-MM-dd");
    }

    public static Date convert(final String dateString, final String dateFormat) throws ParseException {
        final DateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.parse(dateString);
    }

    public static Calendar convert(final Date date){
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static String stripStringToMaxLength(String input,int maxLength){
        return substring(input,0,maxLength);
    }

    public boolean isAddPartyJourney(Arrangement arrangement){
        return arrangement.getRelatedInvolvedParty()!=null;
    }

    public String[] getNormalizedAddressLines (PostalAddressComponent addressComponent) {
        String[] addressLines = new String[4];
        if (null != addressComponent && null != addressComponent.getUnstructuredAddress()) {

            UnstructuredPostalAddress unStructuredAddress = addressComponent.getUnstructuredAddress();
            UnstructuredPostalAddressDTO unstructuredAddressDTO = populateUnstructuredAddressDTO(unStructuredAddress);
            addressLines[0] = unstructuredAddressDTO.unstructuredAddressLine(0) != null ? unstructuredAddressDTO.unstructuredAddressLine(0) : CommonConstant.EMPTY_STRING;
            addressLines[1] = unstructuredAddressDTO.unstructuredAddressLine(1) != null ? unstructuredAddressDTO.unstructuredAddressLine(1) : CommonConstant.EMPTY_STRING;
            addressLines[2] = unstructuredAddressDTO.unstructuredAddressLine(2) != null ? unstructuredAddressDTO.unstructuredAddressLine(2) : CommonConstant.EMPTY_STRING;
            addressLines[3] = unstructuredAddressDTO.unstructuredAddressLine(3) != null ? unstructuredAddressDTO.unstructuredAddressLine(3) : CommonConstant.EMPTY_STRING;

        } else if (null != addressComponent && null != addressComponent.getStructuredAddress()) {

            PostalAddress structuredAddress = addressComponent.getStructuredAddress();
            addressLines[0] = structuredAddress.getSubBuildingName() + CommonConstant.EMPTY_STRING + structuredAddress.getBuildingName();
            addressLines[1] = structuredAddress.getBuildingNumber() + CommonConstant.EMPTY_STRING + StringUtils.join(structuredAddress.getAddressLines(), CommonConstant.EMPTY_STRING);
            addressLines[2] = structuredAddress.getDistrict() + CommonConstant.EMPTY_STRING + structuredAddress.getTown();
            addressLines[3] = structuredAddress.getCounty();
        }
        return addressLines;
    }

}
