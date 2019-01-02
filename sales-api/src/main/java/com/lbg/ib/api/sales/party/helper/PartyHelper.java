package com.lbg.ib.api.sales.party.helper;

import com.lbg.ib.api.sales.party.domain.IBParties;
import com.lbg.ib.api.sales.user.domain.PartyDetails;
import com.lbg.ib.api.sso.domain.user.Arrangement;
import com.lbg.ib.api.sso.domain.user.PrimaryInvolvedParty;

import java.util.List;
import java.util.Map;

/**
 * Created by 8782861 on 05/07/2018.
 */
public class PartyHelper {

    public static final String ACCOUNT_TYPE_PRIMARY = "PRIMARY";
    public static final String ACCOUNT_TYPE_JOINT = "JOINT";

    public static final String PREFETCH_OCIS_ID = "PREFETCH_OCIS_ID";
    public static final String Q226_OCIS_ID = "Q226_OCIS_ID";

    public static boolean isPartyMatchingWithPrimaryPartyFromSession(final IBParties src, final PrimaryInvolvedParty dest)
    {
        boolean check = true;

        if (dest.getFirstName() != null && !dest.getFirstName().equalsIgnoreCase(src.getFirstName())) {
            check = false;
        }
        if (dest.getLastName() != null && !dest.getLastName().equalsIgnoreCase(src.getLastName())) {
            check = false;
        }
        if (dest.getTitle() != null && !dest.getTitle().equalsIgnoreCase(src.getTitle())) {
            check = false;
        }
        if (dest.getDob()!= null && !dest.getDob().equalsIgnoreCase(src.getBirthDate())) {
            check = false;
        }
        return check;
    }

    public static PartyDetails getPrimaryPartyDetails(Map<String, String> allPartiesOcisIds, Arrangement arrangement) {
        PartyDetails partyDetails = new PartyDetails();
        if (arrangement.getPrimaryInvolvedParty() != null) {
            partyDetails.setTitle(arrangement.getPrimaryInvolvedParty().getTitle());
            partyDetails.setFirstName(arrangement.getPrimaryInvolvedParty().getFirstName());
            partyDetails.setSurname(arrangement.getPrimaryInvolvedParty().getLastName());
            partyDetails.setEmail(arrangement.getPrimaryInvolvedParty().getEmail());
            partyDetails.setDob(arrangement.getPrimaryInvolvedParty().getDob());
            if (null != arrangement.getPrimaryInvolvedParty() && null != arrangement.getPrimaryInvolvedParty().getCurrentAddress()
                    && null != arrangement.getPrimaryInvolvedParty().getCurrentAddress().getUnstructuredAddress()) {

                partyDetails.setPostalCode(arrangement.getPrimaryInvolvedParty().getCurrentAddress().getUnstructuredAddress().getPostcode());
                String[] addressLines = new String[8];
                addressLines[0] = arrangement.getPrimaryInvolvedParty().getCurrentAddress().getUnstructuredAddress().getAddressLine1();
                addressLines[1] = arrangement.getPrimaryInvolvedParty().getCurrentAddress().getUnstructuredAddress().getAddressLine2();
                addressLines[2] = arrangement.getPrimaryInvolvedParty().getCurrentAddress().getUnstructuredAddress().getAddressLine3();
                addressLines[3] = arrangement.getPrimaryInvolvedParty().getCurrentAddress().getUnstructuredAddress().getAddressLine4();
                addressLines[4] = arrangement.getPrimaryInvolvedParty().getCurrentAddress().getUnstructuredAddress().getAddressLine5();
                addressLines[5] = arrangement.getPrimaryInvolvedParty().getCurrentAddress().getUnstructuredAddress().getAddressLine6();
                addressLines[6] = arrangement.getPrimaryInvolvedParty().getCurrentAddress().getUnstructuredAddress().getAddressLine7();
                addressLines[7] = arrangement.getPrimaryInvolvedParty().getCurrentAddress().getUnstructuredAddress().getAddressLine8();
                partyDetails.setAddressLines(addressLines);
            }
        }
        partyDetails.setIsPrimaryParty(true);
        if (allPartiesOcisIds.size() > 1) {
            partyDetails.setJoint(true);
        } else {
            partyDetails.setJoint(false);
        }
        return partyDetails;
    }

    public static void populatePrimayAndJointParties(final Arrangement arrangement, final Map<String, String> allPartiesOcisIds, final List<IBParties> partiesRetrieved) {
        for (IBParties party : partiesRetrieved) {
            if (PartyHelper.isPartyMatchingWithPrimaryPartyFromSession(party, arrangement.getPrimaryInvolvedParty())) {
                allPartiesOcisIds.put(ACCOUNT_TYPE_PRIMARY, party.getPartyId());
            } else {
                allPartiesOcisIds.put(ACCOUNT_TYPE_JOINT, party.getPartyId());
            }
        }
    }
}
