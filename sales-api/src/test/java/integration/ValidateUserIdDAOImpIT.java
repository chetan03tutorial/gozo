/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package integration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.mandate.ValidateUserIdDAOImpl;
import com.lbg.ib.api.sales.dto.mandate.ValidateUserIdDTO;
import com.lbg.ib.api.sales.soapapis.wsbridge.application.ApplicationLocator;
import com.lbg.ib.api.sales.soapapis.wsbridge.application.ApplicationPortType;

@RunWith(MockitoJUnitRunner.class)
public class ValidateUserIdDAOImpIT {

    @InjectMocks
    private ValidateUserIdDAOImpl validateUserIdDAOImpl;

    @Mock
    private ApplicationPortType   applicationPort;

    @Mock
    private LoggerDAO             logger;

    @Ignore
    @Test
    public void shouldReturnSuggestedUsernames() throws Exception {
        applicationPort = new ApplicationLocator().getApplication(new URL("http://10.245.211.251:22910/fserv/"));
        validateUserIdDAOImpl.setValidateService(applicationPort);
        DAOResponse<List<String>> validated = validateUserIdDAOImpl
                .validate(new ValidateUserIdDTO("1090806685", "+00305488871", "IBS", null, "saurabhgarg"));
        List<String> result = validated.getResult();
        assertFalse(result.isEmpty());
    }

    @Ignore
    @Test
    public void shouldReturnNoSuggestedUsernames() throws Exception {
        applicationPort = new ApplicationLocator().getApplication(new URL("http://10.245.211.251:22910/fserv/"));
        validateUserIdDAOImpl.setValidateService(applicationPort);
        DAOResponse<List<String>> validated = validateUserIdDAOImpl
                .validate(new ValidateUserIdDTO("1090806685", "+00305488871", "IBS", null, "saurabhgargaq"));
        List<String> result = validated.getResult();
        assertTrue(result.isEmpty());
    }

}