/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.common.validation;

/**
 * Determines the type of section that is applying for the product currently
 * there are 3 types of sections:
 * <ul>
 * <li>Personal Details</li>
 * <li>IB_REG</li>
 * <li>Address</li>Self
 * </ul>
 */

public enum SectionType {

    PERSONAL_DETAILS("personaldetails"),

    IB_REG("ibreg"),

    POSTAL_ADDRESS_COMPONENT("postalAddressComponent"),

    POSTAL_ADDRESS("postalAddress"),

    UNSTRUCTURED_POSTAL_ADDRESS("unstructuredAddress"),

    CONTACT_NUMBER("contactNumber"),

    ACCOUNT_SETUP("accountSetup"),

    INVOLVED_PARTY("involvedParty"),

    ACCOUNT_SWITCHING("accountSwitching"),

    CHILS_APPLICATION("childApplication"),

    TIN_DETAILS("tinDetails");

    private String sectionType;

    SectionType(String sectionType) {
        this.sectionType = sectionType;
    }

    public String getSectionType() {
        return sectionType;
    }

    public void setSectionType(String sectionType) {
        this.sectionType = sectionType;
    }

}
