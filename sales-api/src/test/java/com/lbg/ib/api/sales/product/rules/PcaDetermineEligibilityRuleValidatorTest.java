/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.product.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sales.common.error.ValidationError;
import com.lbg.ib.api.sales.product.domain.eligibility.PcaDetermineEligibilityRequest;

@RunWith(MockitoJUnitRunner.class)
public class PcaDetermineEligibilityRuleValidatorTest {
    @InjectMocks
    PcaDetermineEligibilityRuleValidator pcaDetermineEligibilityRuleValidator = null;
    PcaDetermineEligibilityRuleValidator validator                            = new PcaDetermineEligibilityRuleValidator();

    @Test
    public void testValidateRules() {

        PcaDetermineEligibilityRequest determineEligibilityRequest = new PcaDetermineEligibilityRequest();
        List<String> instructions = new ArrayList<String>();
        determineEligibilityRequest.setCandidateInstructions(instructions);
        determineEligibilityRequest.setExistingCustomer(false);
        determineEligibilityRequest.setDob("2016-01-01");
        assertNull(validator.validateRules(determineEligibilityRequest, true));
    }

    @Test
    public void testValidateRulesForNullExistingCustomer() {

        PcaDetermineEligibilityRequest determineEligibilityRequest = new PcaDetermineEligibilityRequest();
        List<String> instructions = new ArrayList<String>();
        determineEligibilityRequest.setCandidateInstructions(instructions);
        determineEligibilityRequest.setExistingCustomer(null);
        assertNull(validator.validateRules(determineEligibilityRequest, true));
    }

    @Test
    public void testValidateRulesForExistingCustomer() {

        PcaDetermineEligibilityRequest determineEligibilityRequest = new PcaDetermineEligibilityRequest();
        List<String> instructions = new ArrayList<String>();
        determineEligibilityRequest.setCandidateInstructions(instructions);
        determineEligibilityRequest.setExistingCustomer(true);
        assertNull(validator.validateRules(determineEligibilityRequest, true));
    }

    @Test
    public void testValidateRulesForDoB() {

        PcaDetermineEligibilityRequest determineEligibilityRequest = new PcaDetermineEligibilityRequest();
        List<String> instructions = new ArrayList<String>();
        determineEligibilityRequest.setCandidateInstructions(instructions);
        determineEligibilityRequest.setExistingCustomer(false);
        determineEligibilityRequest.setDob("2016/01/01");
        ValidationError error = validator.validateRules(determineEligibilityRequest, true);
        assertEquals(error.getMessage(), "Invalid date format");
    }

    @Test
    public void testValidateRulesForCandidateInstructions() {

        PcaDetermineEligibilityRequest determineEligibilityRequest = new PcaDetermineEligibilityRequest();
        determineEligibilityRequest.setCandidateInstructions(null);
        determineEligibilityRequest.setExistingCustomer(true);
        determineEligibilityRequest.setDob("2016-01-01");
        ValidationError error = validator.validateRules(determineEligibilityRequest, false);
        assertEquals(error.getMessage(), "Candidate Insturctions are Mandatory");
    }

    @Test
    public void testValidateRulesWithValidRequest() {
        PcaDetermineEligibilityRequest determineEligibilityRequest = new PcaDetermineEligibilityRequest();
        determineEligibilityRequest.setDob("2016-01-01");
        determineEligibilityRequest.setExistingCustomer(true);
        assertNull(pcaDetermineEligibilityRuleValidator.validateDob(determineEligibilityRequest));
    }

    @Test
    public void testValidateRulesWithInValidRequest() {
        PcaDetermineEligibilityRequest determineEligibilityRequest = new PcaDetermineEligibilityRequest();
        determineEligibilityRequest.setDob("2016/01/01");
        determineEligibilityRequest.setExistingCustomer(true);
        assertTrue(pcaDetermineEligibilityRuleValidator.validateDob(determineEligibilityRequest) != null);
    }

    @Test
    public void testValidateDob() {
        PcaDetermineEligibilityRequest determineEligibilityRequest = new PcaDetermineEligibilityRequest();
        determineEligibilityRequest.setDob("2016-01-01");
        assertNull(pcaDetermineEligibilityRuleValidator.validateDob(determineEligibilityRequest));
    }

    @Test
    public void testInValidateDob() {
        PcaDetermineEligibilityRequest determineEligibilityRequest = new PcaDetermineEligibilityRequest();
        determineEligibilityRequest.setDob("2016/01/01");
        assertTrue(pcaDetermineEligibilityRuleValidator.validateDob(determineEligibilityRequest) != null);
    }
}