package com.lbg.ib.api.sales.common.validation.enums;

import static com.lbg.ib.api.sales.common.constant.Constants.CHILD_APPLICATION;
import static com.lbg.ib.api.sales.common.constant.Constants.CONTACT_NUMBER;
import static com.lbg.ib.api.sales.common.constant.Constants.MSG_14;
import static com.lbg.ib.api.sales.common.constant.Constants.MSG_21;
import static com.lbg.ib.api.sales.common.constant.Constants.MSG_26;
import static com.lbg.ib.api.sales.common.constant.Constants.MSG_27;
import static com.lbg.ib.api.sales.common.constant.Constants.MSG_56;
import static com.lbg.ib.api.sales.common.constant.Constants.MSG_80;
import static com.lbg.ib.api.sales.common.constant.Constants.MSG_87;
import static com.lbg.ib.api.sales.common.constant.Constants.PERSONAL_DETAILS;
import static com.lbg.ib.api.sales.common.constant.Constants.POSTAL_ADDRESS;
import static com.lbg.ib.api.sales.common.constant.Constants.POSTAL_ADDRESS_COMPONENT;
import static com.lbg.ib.api.sales.common.constant.Constants.POSTAL_ADDRESS_UNSTRUCTURED;
import static com.lbg.ib.api.sales.common.constant.Constants.PSTCD;
import static com.lbg.ib.api.sales.common.constant.Constants.TIN_DETAILS;

public enum ValidationSectionIdentifier {

    DURATION(POSTAL_ADDRESS_COMPONENT, "durationOfStay", "MSG_46", MSG_21, null, "MSG_24"), POSTCODE(
            POSTAL_ADDRESS_COMPONENT, PSTCD, null, null, MSG_80,
            null), BUILDINGNAME(POSTAL_ADDRESS, "buildingName", "MSG_39", MSG_14, null, null), BUILDINGNUMBER(
                    POSTAL_ADDRESS, "buildingNumber", MSG_87, MSG_21, null,
                    null), PSTCODE(POSTAL_ADDRESS, PSTCD, "MSG_18", "MSG_20", MSG_80, null), STREET(POSTAL_ADDRESS,
                            "street", "MSG_41", MSG_14, null,
                            null), DISTRICT(POSTAL_ADDRESS, "district", null, MSG_14, null, null), TOWN(POSTAL_ADDRESS,
                                    "town", "MSG_42", MSG_14, null,
                                    null), COUNTY(POSTAL_ADDRESS, "county", null, MSG_14, null, null), ADDRESSLINEONE(
                                            POSTAL_ADDRESS_UNSTRUCTURED, "addressLine1", MSG_87, MSG_21, null,
                                            null), ADDRESSLINETWO(POSTAL_ADDRESS_UNSTRUCTURED, "addressLine2", MSG_87,
                                                    MSG_14, null, null), ADDRESSLINETHREE(POSTAL_ADDRESS_UNSTRUCTURED,
                                                            "addressLine3", null, MSG_14, null, null), ADDRESSLINEFOUR(
                                                                    POSTAL_ADDRESS_UNSTRUCTURED, "addressLine4",
                                                                    "MSG_41", MSG_14, null, null), ADDRESSLINEFIVE(
                                                                            POSTAL_ADDRESS_UNSTRUCTURED, "addressLine5",
                                                                            null, MSG_14, null, null), ADDRESSLINESIX(
                                                                                    POSTAL_ADDRESS_UNSTRUCTURED,
                                                                                    "addressLine6", "MSG_42", MSG_14,
                                                                                    null, null), ADDRESSLINESEVEN(
                                                                                            POSTAL_ADDRESS_UNSTRUCTURED,
                                                                                            "addressLine7", null,
                                                                                            MSG_14, null,
                                                                                            null), POSTCODEUNSTRUCTURED(
                                                                                                    POSTAL_ADDRESS_UNSTRUCTURED,
                                                                                                    PSTCD, "MSG_18",
                                                                                                    "MSG_20", MSG_80,
                                                                                                    null), COUNTRYCODE(
                                                                                                            CONTACT_NUMBER,
                                                                                                            "countryCode",
                                                                                                            MSG_56,
                                                                                                            MSG_21,
                                                                                                            null,
                                                                                                            null), AREACODE(
                                                                                                                    CONTACT_NUMBER,
                                                                                                                    "areaCode",
                                                                                                                    null,
                                                                                                                    MSG_21,
                                                                                                                    null,
                                                                                                                    null), EXTNUMBER(
                                                                                                                            CONTACT_NUMBER,
                                                                                                                            "extNumber",
                                                                                                                            null,
                                                                                                                            MSG_21,
                                                                                                                            null,
                                                                                                                            null), NUMBER(
                                                                                                                                    CONTACT_NUMBER,
                                                                                                                                    "number",
                                                                                                                                    MSG_56,
                                                                                                                                    MSG_21,
                                                                                                                                    "MSG_55",
                                                                                                                                    null), TITLEPERSONALDETAILS(
                                                                                                                                            PERSONAL_DETAILS,
                                                                                                                                            "title",
                                                                                                                                            "MSG_34",
                                                                                                                                            MSG_14,
                                                                                                                                            MSG_14,
                                                                                                                                            null), FIRSTNAMEPERSONALDETAILS(
                                                                                                                                                    PERSONAL_DETAILS,
                                                                                                                                                    "firstName",
                                                                                                                                                    "MSG_35",
                                                                                                                                                    MSG_14,
                                                                                                                                                    null,
                                                                                                                                                    null), MIDDLENAMEPERSONALDETAILS(
                                                                                                                                                            PERSONAL_DETAILS,
                                                                                                                                                            "middleName",
                                                                                                                                                            null,
                                                                                                                                                            MSG_14,
                                                                                                                                                            null,
                                                                                                                                                            null), LASTNAMEPERSONALDETAILS(
                                                                                                                                                                    PERSONAL_DETAILS,
                                                                                                                                                                    "lastName",
                                                                                                                                                                    "MSG_36",
                                                                                                                                                                    MSG_14,
                                                                                                                                                                    null,
                                                                                                                                                                    null), GENDERPERSONALDETAILS(
                                                                                                                                                                            PERSONAL_DETAILS,
                                                                                                                                                                            "gender",
                                                                                                                                                                            "MSG_38",
                                                                                                                                                                            null,
                                                                                                                                                                            null,
                                                                                                                                                                            null), BIRTHCITYPERSONALDETAILS(
                                                                                                                                                                                    PERSONAL_DETAILS,
                                                                                                                                                                                    "birthCity",
                                                                                                                                                                                    "MSG_49",
                                                                                                                                                                                    MSG_14,
                                                                                                                                                                                    null,
                                                                                                                                                                                    null), DOBPERSONALDETAILS(
                                                                                                                                                                                            PERSONAL_DETAILS,
                                                                                                                                                                                            "dob",
                                                                                                                                                                                            "MSG_37",
                                                                                                                                                                                            MSG_21,
                                                                                                                                                                                            "MSG_17",
                                                                                                                                                                                            null), BIRTHCOUNTRYPERSONALDETAILS(
                                                                                                                                                                                                    PERSONAL_DETAILS,
                                                                                                                                                                                                    "birthCountry",
                                                                                                                                                                                                    "MSG_48",
                                                                                                                                                                                                    null,
                                                                                                                                                                                                    null,
                                                                                                                                                                                                    null), EMPLOYMENTSTATUSPERSONALDETAILS(
                                                                                                                                                                                                            PERSONAL_DETAILS,
                                                                                                                                                                                                            "employmentStatus",
                                                                                                                                                                                                            "MSG_51",
                                                                                                                                                                                                            null,
                                                                                                                                                                                                            null,
                                                                                                                                                                                                            null), OCCUPATIONTYPEPERSONALDETAILS(
                                                                                                                                                                                                                    PERSONAL_DETAILS,
                                                                                                                                                                                                                    "occupnType",
                                                                                                                                                                                                                    "MSG_52",
                                                                                                                                                                                                                    null,
                                                                                                                                                                                                                    null,
                                                                                                                                                                                                                    null), EMAILPERSONALDETAILS(
                                                                                                                                                                                                                            PERSONAL_DETAILS,
                                                                                                                                                                                                                            "email",
                                                                                                                                                                                                                            MSG_27,
                                                                                                                                                                                                                            MSG_27,
                                                                                                                                                                                                                            MSG_27,
                                                                                                                                                                                                                            "MSG_10"), MOBILENUMBERPERSONALDETAILS(
                                                                                                                                                                                                                                    PERSONAL_DETAILS,
                                                                                                                                                                                                                                    "mobileNumber",
                                                                                                                                                                                                                                    MSG_56,
                                                                                                                                                                                                                                    MSG_21,
                                                                                                                                                                                                                                    "MSG_55",
                                                                                                                                                                                                                                    null), TINDETAILS(
                                                                                                                                                                                                                                            TIN_DETAILS,
                                                                                                                                                                                                                                            "tinDetails",
                                                                                                                                                                                                                                            MSG_26,
                                                                                                                                                                                                                                            MSG_26,
                                                                                                                                                                                                                                            MSG_26,
                                                                                                                                                                                                                                            null), DOBCHILDAPP(
                                                                                                                                                                                                                                                    CHILD_APPLICATION,
                                                                                                                                                                                                                                                    "dob",
                                                                                                                                                                                                                                                    "MSG_66",
                                                                                                                                                                                                                                                    MSG_21,
                                                                                                                                                                                                                                                    "MSG_78",
                                                                                                                                                                                                                                                    null), FIRSTNAMECHILDAPP(
                                                                                                                                                                                                                                                            CHILD_APPLICATION,
                                                                                                                                                                                                                                                            "firstName",
                                                                                                                                                                                                                                                            "MSG_64",
                                                                                                                                                                                                                                                            MSG_14,
                                                                                                                                                                                                                                                            null,
                                                                                                                                                                                                                                                            null), MIDDLENAMECHILDAPP(
                                                                                                                                                                                                                                                                    CHILD_APPLICATION,
                                                                                                                                                                                                                                                                    "middleName",
                                                                                                                                                                                                                                                                    null,
                                                                                                                                                                                                                                                                    MSG_14,
                                                                                                                                                                                                                                                                    null,
                                                                                                                                                                                                                                                                    null), LASTNAMECHILDAPP(
                                                                                                                                                                                                                                                                            CHILD_APPLICATION,
                                                                                                                                                                                                                                                                            "lastName",
                                                                                                                                                                                                                                                                            "MSG_65",
                                                                                                                                                                                                                                                                            MSG_14,
                                                                                                                                                                                                                                                                            null,
                                                                                                                                                                                                                                                                            null), GENDERCHILDAPP(
                                                                                                                                                                                                                                                                                    CHILD_APPLICATION,
                                                                                                                                                                                                                                                                                    "gender",
                                                                                                                                                                                                                                                                                    "MSG_67",
                                                                                                                                                                                                                                                                                    null,
                                                                                                                                                                                                                                                                                    null,
                                                                                                                                                                                                                                                                                    null), BIRTHCITYCHILDAPP(
                                                                                                                                                                                                                                                                                            CHILD_APPLICATION,
                                                                                                                                                                                                                                                                                            "birthCity",
                                                                                                                                                                                                                                                                                            "MSG_71",
                                                                                                                                                                                                                                                                                            "MSG_25",
                                                                                                                                                                                                                                                                                            "MSG_25",
                                                                                                                                                                                                                                                                                            null), BIRTHCOUNTRYCHILDAPP(
                                                                                                                                                                                                                                                                                                    CHILD_APPLICATION,
                                                                                                                                                                                                                                                                                                    "birthCountry",
                                                                                                                                                                                                                                                                                                    "MSG_70",
                                                                                                                                                                                                                                                                                                    null,
                                                                                                                                                                                                                                                                                                    null,
                                                                                                                                                                                                                                                                                                    null);

    private String sectionId;

    private String name;

    private String reqdFieldId;

    private String stringFieldId;

    private String invalidFieldId;
    private String otherId;

    private ValidationSectionIdentifier(String sectionId, String name, String reqdFieldId, String stringFieldId,
            String invalidFieldId, String otherId) {
        this.sectionId = sectionId;
        this.name = name;
        this.reqdFieldId = reqdFieldId;
        this.stringFieldId = stringFieldId;
        this.invalidFieldId = invalidFieldId;
        this.otherId = otherId;
    }

    public String getSectionId() {
        return sectionId;
    }

    public String getName() {
        return name;
    }

    public String getReqdFieldId() {
        return reqdFieldId;
    }

    public String getStringFieldId() {
        return stringFieldId;
    }

    public String getInvalidFieldId() {
        return invalidFieldId;
    }

    public String getOtherId() {
        return otherId;
    }

    public static ValidationSectionIdentifier getValidationIdentifierBySection(String section, String field) {
        for (ValidationSectionIdentifier identifier : ValidationSectionIdentifier.values()) {

            if (identifier.sectionId.equals(section) && identifier.name.equalsIgnoreCase(field)) {
                return identifier;
            }
        }
        return null;

    }

}
