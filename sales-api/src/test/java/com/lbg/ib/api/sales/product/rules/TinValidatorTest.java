/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.product.rules;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.sales.party.domain.ClassifiedPartyDetails;
import com.lbg.ib.api.shared.domain.TaxResidencyDetails;
import com.lbg.ib.api.shared.domain.TinDetails;
import com.lbg.ib.api.sales.party.service.TinDetailsService;
import com.lbg.ib.api.sales.product.domain.arrangement.PrimaryInvolvedParty;

@RunWith(MockitoJUnitRunner.class)
public class TinValidatorTest {

    @InjectMocks
    private TinValidator         tinValidator = new TinValidator();
    @Mock
    private TinDetailsService    classifyPartyResidencyService;

    @Mock
    private PrimaryInvolvedParty mockPrimaryInvolvedParty;

    @Test
    public void shouldReturnErrorWhenTinNumberNotEntered() throws Exception {
        TinDetails tinDetails = setTinDetails(null);
        List<ClassifiedPartyDetails> classifiedPartyDetailsList = setClassifiedPartyDetails(null);

        when(mockPrimaryInvolvedParty.getTinDetails()).thenReturn(tinDetails);
        when(classifyPartyResidencyService.identify(tinDetails)).thenReturn(classifiedPartyDetailsList);

        assertThat(tinValidator.validateTinNumber(mockPrimaryInvolvedParty),
                is(new ValidationError("Please enter the TIN number as it is mandatory for the country GBR")));
    }

    @Test
    public void shouldReturnErrorWhenTinNumberNoDoesNotMatchTheRegex() throws Exception {
        TinDetails tinDetails = setTinDetails("ABC");
        List<ClassifiedPartyDetails> classifiedPartyDetailsList = setClassifiedPartyDetails("[0-9]{9}");

        when(mockPrimaryInvolvedParty.getTinDetails()).thenReturn(tinDetails);
        when(classifyPartyResidencyService.identify(tinDetails)).thenReturn(classifiedPartyDetailsList);

        assertThat(tinValidator.validateTinNumber(mockPrimaryInvolvedParty), is(new ValidationError(
                "The format of the TIN number entered is not correct. Please enter the TIN number in the format [0-9]{9}")));
    }


    @Test
    public void shouldReturnErrorWhenTinNumberNoDoesNotMatchTheRegex2() throws Exception {
        TinDetails tinDetails = setTinDetails("ABC");
        List<ClassifiedPartyDetails> classifiedPartyDetailsList = setClassifiedPartyDetails2("[0-9]{9}");

        when(mockPrimaryInvolvedParty.getTinDetails()).thenReturn(tinDetails);
        when(classifyPartyResidencyService.identify(tinDetails)).thenReturn(classifiedPartyDetailsList);

        Assert.assertNull(tinValidator.validateTinNumber(mockPrimaryInvolvedParty));
    }


    private TinDetails setTinDetails(String tinNumber) {
        LinkedHashSet<String> nationalities = new LinkedHashSet<String>();
        nationalities.add("GBR");
        nationalities.add("ALA");
        LinkedHashSet<TaxResidencyDetails> taxResidencyDetails = new LinkedHashSet<TaxResidencyDetails>();
        TaxResidencyDetails taxDetails = new TaxResidencyDetails();
        taxDetails.setTaxResidency("GBR");
        if (tinNumber != null) {
            taxDetails.setTinNumber(tinNumber);
        }
        taxResidencyDetails.add(taxDetails);
        TinDetails tinDetails = new TinDetails();
        tinDetails.setBirthCountry("United Kingdom");
        tinDetails.setNationalities(nationalities);
        tinDetails.setTaxResidencies(taxResidencyDetails);
        return tinDetails;
    }

    private List<ClassifiedPartyDetails> setClassifiedPartyDetails(String regex) {
        ClassifiedPartyDetails classifiedPartyDetails = new ClassifiedPartyDetails();
        classifiedPartyDetails.setCountryName("GBR");
        if (regex != null) {
            classifiedPartyDetails.setRegex(regex);
        }
        classifiedPartyDetails.setTaxResidencyType("CRS");
        classifiedPartyDetails.setTinRequired(true);
        List<ClassifiedPartyDetails> classifiedPartyDetailsList = new ArrayList<ClassifiedPartyDetails>();
        classifiedPartyDetailsList.add(classifiedPartyDetails);
        return classifiedPartyDetailsList;
    }

    private List<ClassifiedPartyDetails> setClassifiedPartyDetails2(String regex) {
        ClassifiedPartyDetails classifiedPartyDetails = new ClassifiedPartyDetails();
        classifiedPartyDetails.setCountryName("GBR");
       /* if (regex != null) {
            classifiedPartyDetails.setRegex(regex);
        }*/
        classifiedPartyDetails.setTaxResidencyType("CRS");
        classifiedPartyDetails.setTinRequired(true);
        List<ClassifiedPartyDetails> classifiedPartyDetailsList = new ArrayList<ClassifiedPartyDetails>();
        classifiedPartyDetailsList.add(classifiedPartyDetails);
        return classifiedPartyDetailsList;
    }
}
