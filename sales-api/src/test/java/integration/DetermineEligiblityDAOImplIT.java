
package integration;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import com.lbg.ib.api.sales.dao.mapper.EligibilityRequestMapper;
import com.lbg.ib.api.sales.dao.product.eligibility.EligibilityDAOImpl;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.product.eligibility.EligibilityRequestDTO;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.InstructionDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Product;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductEligibilityDetails;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;
import com.lbg.ib.api.sales.soapapis.determineeligibility.reqres.DetermineEligibleCustomerInstructionsRequest;
import com.lbg.ib.api.sales.soapapis.determineeligibility.reqres.DetermineEligibleCustomerInstructionsResponse;
import com.lbg.ib.sales.soapapis.determineeligibility.conditions.Exp_IA_DetermineEligibleCustomerInstructions_SOAP_IA_DetermineEligibleCustomerInstructionsHttpServiceLocator;
import com.lbg.ib.sales.soapapis.determineeligibility.conditions.IA_DetermineEligibleCustomerInstructions;

@RunWith(MockitoJUnitRunner.class)
public class DetermineEligiblityDAOImplIT extends BaseIT {

    @InjectMocks
    private EligibilityDAOImpl                           eligibilityDAOImpl;

    @Mock
    private LoggerDAO                                    logger;

    @Mock
    private ApiServiceProperties                         properties;

    @Mock
    private IA_DetermineEligibleCustomerInstructions     service;

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

    private List<SOAPHeader>                             SOAP_HEADERS = new ArrayList<SOAPHeader>();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

    }

    @Ignore
    @Test
    public void shouldGiveEligibleCustomerInstructionResponse() throws Exception {
        EligibilityRequestMapper requestMapper = new EligibilityRequestMapper();
        eligibilityDAOImpl.setRequestMapper(requestMapper);
        Exp_IA_DetermineEligibleCustomerInstructions_SOAP_IA_DetermineEligibleCustomerInstructionsHttpServiceLocator locator = new Exp_IA_DetermineEligibleCustomerInstructions_SOAP_IA_DetermineEligibleCustomerInstructionsHttpServiceLocator();
        service = locator
                .getExp_IA_DetermineEligibleCustomerInstructions_SOAP_IA_DetermineEligibleCustomerInstructionsHttpPort(
                        new URL("http://0.245.211.251:22910/wps/sales/v1.0/determineEligibleCustomerInstructions"));
        eligibilityDAOImpl.setEligibilityService(service);
        requestMapper.setGboHeaderUtility(headerUtility);
        requestMapper.setSession(session);
        when(headerUtility.prepareSoapHeader("determineEligibleCustomerInstructions",
                "DetermineEligibleCustomerInstructions")).thenReturn(populateRequestHeader());
        DAOResponse<HashMap<String, String>> map = eligibilityDAOImpl
                .determineEligibleCustomerInstructions(requestDTO());
        assertEquals(map.getResult(), eligibleProductsMap());
    }

    private static EligibilityRequestDTO requestDTO() throws Exception {

        String[] cnadidateInstruction = { "P_CLSCVTG" };
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        String dateInString = "01-01-1935";
        Date birthDate = sdf.parse(dateInString);

        return new EligibilityRequestDTO("CA", birthDate, "customerIdentifier", null, cnadidateInstruction, null, null);

    }

    private HashMap<String, String> eligibleProductsMap() {
        HashMap<String, String> eligibleProductsMap = new HashMap<String, String>();
        InstructionDetails instructionDetails = new InstructionDetails();
        instructionDetails.setInstructionMnemonic("P_CLSCVTG");
        String eligibleProduct = null;
        String mnemonic = null;
        Product aproduct = new Product();
        aproduct.setInstructionDetails(instructionDetails);
        mnemonic = aproduct.getInstructionDetails().getInstructionMnemonic();
        ProductEligibilityDetails productEligibilityDetails = new ProductEligibilityDetails();
        productEligibilityDetails.setProduct(new Product[] { aproduct });
        productEligibilityDetails.setIsEligible("true");
        eligibleProduct = productEligibilityDetails.getIsEligible();
        eligibleProductsMap.put(mnemonic, eligibleProduct);
        return (eligibleProductsMap);
    }

}
