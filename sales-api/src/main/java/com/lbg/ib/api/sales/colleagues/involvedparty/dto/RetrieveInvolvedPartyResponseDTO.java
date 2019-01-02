/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.colleagues.involvedparty.dto;

import java.util.Set;

/**
 * RetrieveInvolvedPartyResponseDTO
 */
public class RetrieveInvolvedPartyResponseDTO {

    private String      reasonCode;

    private String      reasonText;

    private String      involvedPartyRoleValue;

    private Set<String> roles;

    public RetrieveInvolvedPartyResponseDTO() {
        // Zero Argument Constructor to comments to avoid Sonar Violations.
    }

    public RetrieveInvolvedPartyResponseDTO(String reasonCode, String reasonText, String involvedPartyRoleValue,
            Set<String> roles) {
        this.reasonCode = reasonCode;
        this.reasonText = reasonText;
        this.involvedPartyRoleValue = involvedPartyRoleValue;
        this.roles = roles;
    }

    public String getInvolvedPartyRoleValue() {
        return involvedPartyRoleValue;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public String getReasonText() {
        return reasonText;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "RetrieveInvolvedPartyResponseDTO [reasonCode=" + reasonCode + ", reasonText=" + reasonText
                + ", involvedPartyRoleValue=" + involvedPartyRoleValue + ", roles=" + roles + "]";
    }

}
