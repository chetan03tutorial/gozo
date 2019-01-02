/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package integration;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.mapper.PromotionalCustomerInstructionRequestMapper;
import com.lbg.ib.api.sales.dao.product.offercrosssell.CrossSellEligibilityDAOImpl;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.product.eligibility.EligibilityRequestDTO;
import com.lbg.ib.api.sales.soapapis.determineeligibility.reqres.DetermineEligibleCustomerInstructionsRequest;
import com.lbg.ib.api.sales.soapapis.determineeligibility.reqres.DetermineEligibleCustomerInstructionsResponse;
import com.lbg.ib.api.sales.soapapis.determinepromotional.conditions.Exp_IA_DeterminePromotionalCustomerInstructions_SOAP_IA_DeterminePromotionalCustomerInstructionsHttpServiceLocator;
import com.lbg.ib.api.sales.soapapis.determinepromotional.conditions.IA_DeterminePromotionalCustomerInstructions;

@RunWith(MockitoJUnitRunner.class)
public class CrossSellEligibilityDAOImplIT {

    @InjectMocks
    private CrossSellEligibilityDAOImpl                  eligibilityDAOImpl;

    @Mock
    private LoggerDAO                                    logger;

    @Mock
    private ApiServiceProperties                         properties;

    @Mock
    private IA_DeterminePromotionalCustomerInstructions  promotionalCustomerInstructionsService;

    @Mock
    private DetermineEligibleCustomerInstructionsRequest determineEligibleCustomerInstructionsReq;

    @Mock
    DetermineEligibleCustomerInstructionsResponse        response;

    @Mock
    private DAOError                                     error;

    @Mock
    private SessionManagementDAO                         session;

    @Mock
    private GBOHeaderUtility                             headerUtility;

    @Mock
    private UserContext                                  context;

    BaseIT                                               baseIT = new BaseIT();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

    }

    @Ignore
    @Test
    public void shouldGiveEligibleCustomerInstructionResponse() throws Exception {
        PromotionalCustomerInstructionRequestMapper requestMapper = new PromotionalCustomerInstructionRequestMapper();
        eligibilityDAOImpl.setRequestMapper(requestMapper);
        Exp_IA_DeterminePromotionalCustomerInstructions_SOAP_IA_DeterminePromotionalCustomerInstructionsHttpServiceLocator locator = new Exp_IA_DeterminePromotionalCustomerInstructions_SOAP_IA_DeterminePromotionalCustomerInstructionsHttpServiceLocator();
        promotionalCustomerInstructionsService = locator
                .getExp_IA_DeterminePromotionalCustomerInstructions_SOAP_IA_DeterminePromotionalCustomerInstructionsHttpPort(
                        new URL("http://10.245.211.251:22910/wps/sales/v1.0/determinePromotionalCustomerInstructions"));
        eligibilityDAOImpl.setService(promotionalCustomerInstructionsService);
        requestMapper.setGboHeaderUtility(headerUtility);
        requestMapper.setSession(session);
        when(headerUtility.prepareSoapHeader("determinePromotionalCustomerInstructions",
                "DeterminePromotionalCustomerInstructions")).thenReturn(baseIT.populateRequestHeader());
        DAOResponse<TreeMap<String, String>> instructionMnemonic = eligibilityDAOImpl
                .determineCrossSellEligibilityForCustomer(requestDTO());
        assertEquals(instructionMnemonic.getResult(), eligibleProductsMap());
    }

    private static EligibilityRequestDTO requestDTO() throws Exception {

        String[] cnadidateInstruction = { "P_CLUB" };
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        String dateInString = "01-01-1935";
        Date birthDate = sdf.parse(dateInString);

        return new EligibilityRequestDTO("CA", birthDate, "678375072", null, cnadidateInstruction, "1001776000",
                "777505");

    }

    private HashMap<String, String> eligibleProductsMap() {
        HashMap<String, String> eligibleProductsMap = new HashMap<String, String>();
        eligibleProductsMap.put("1", "P_EXC_SVR");
        return (eligibleProductsMap);
    }

}
