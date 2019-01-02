/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.rules;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.party.domain.ClassifiedPartyDetails;
import com.lbg.ib.api.shared.domain.TaxResidencyDetails;
import com.lbg.ib.api.sales.party.service.TinDetailsService;
import com.lbg.ib.api.sales.product.domain.arrangement.PrimaryInvolvedParty;

@Component
public class TinValidator {

    @Autowired
    private TinDetailsService classifyPartyResidencyService;

    /**
     * This method used to validate the Tin number entered
     *
     * @param primary
     *            involved party
     * @return
     * @throws ServiceException
     */
    public ValidationError validateTinNumber(PrimaryInvolvedParty primaryInvolvedParty) throws ServiceException {

        if ((primaryInvolvedParty.getTinDetails() != null
                && primaryInvolvedParty.getTinDetails().getTaxResidencies() != null
                && !primaryInvolvedParty.getTinDetails().getTaxResidencies().isEmpty())) {

            List<ClassifiedPartyDetails> classifiedPartyDetailsList = classifyPartyResidencyService
                    .identify(primaryInvolvedParty.getTinDetails());
            TaxResidencyDetails taxDetails = new TaxResidencyDetails();
            for (Iterator<TaxResidencyDetails> it = primaryInvolvedParty.getTinDetails().getTaxResidencies()
                    .iterator(); it.hasNext();) {
                taxDetails = it.next();
                if (classifiedPartyDetailsList != null && !classifiedPartyDetailsList.isEmpty()) {
                    ClassifiedPartyDetails classifiedPartyDetails = new ClassifiedPartyDetails();
                    for (Iterator<ClassifiedPartyDetails> it1 = classifiedPartyDetailsList.iterator(); it1.hasNext();) {
                        classifiedPartyDetails = it1.next();
                        if (classifiedPartyDetails.getCountryName().equalsIgnoreCase(taxDetails.getTaxResidency())) {
                            if (classifiedPartyDetails.isTinRequired() && taxDetails.getTinNumber() == null) {
                                return new ValidationError(
                                        "Please enter the TIN number as it is mandatory for the country "
                                                + classifiedPartyDetails.getCountryName());
                            } else if (taxDetails.getTinNumber() != null && classifiedPartyDetails.getRegex() != null) {
                                if (!(taxDetails.getTinNumber().matches(classifiedPartyDetails.getRegex()))) {
                                    return new ValidationError(
                                            "The format of the TIN number entered is not correct. Please enter the TIN number in the format "
                                                    + classifiedPartyDetails.getRegex());
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        return null;
    }
}
