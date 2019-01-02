package com.lbg.ib.api.sales.dao.product.terminate;

import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.sales.dao.product.suspended.TerminateArrangementDAOImpl;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.product.pendingarrangement.TerminateArrangementDTO;
import com.lbg.ib.api.sales.product.domain.domains.TerminateArrangement;
import com.lbg.ib.api.sales.soapapis.terminatearrangement.ia_terminatearrangementmaster.IA_TerminateArrangement;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TerminateArrangementDAOImplTest {
    
    @Mock
    private IA_TerminateArrangement terminateArrangementSerive;
    
    @Mock
    private GBOHeaderUtility headerUtility;
    
    @Mock
    private SessionManagementDAO session;
    
    @Mock
    private DAOExceptionHandler daoExceptionHandler;
    
    @Mock
    ConfigurationDAO configDAO;
    
    @Mock
    private LoggerDAO logger;
    
    @InjectMocks
    private TerminateArrangementDAOImpl terminateArrangementDAOImpl;
    
    
    @Test
    public void testTerminateArrangementWithValidRequest() throws Exception{
        TerminateArrangement terminateArrangement = new TerminateArrangement("30082677","CT067484","Cancel","110");
        com.lbg.ib.api.sales.soapapis.terminatearrangement.messages.TerminateArrangementRequest terminateArrangementRequest = terminateArrangementDAOImpl.createTerminateArrangementRequest(terminateArrangement);
        when(terminateArrangementSerive.terminateArrangement(any(com.lbg.ib.api.sales.soapapis.terminatearrangement.messages.TerminateArrangementRequest.class))).thenReturn(new com.lbg.ib.api.sales.soapapis.terminatearrangement.messages.TerminateArrangementResponse());
        DAOResponse<TerminateArrangementDTO> response = terminateArrangementDAOImpl.terminateArrangement(terminateArrangement);
        assertTrue(response.getResult()!=null);
        
    }
    
    @Test
    public void testTerminateArrangementWithInvalidRequest() throws Exception{
        TerminateArrangement terminateArrangement = new TerminateArrangement("30082677","CT067484","Cancel","110");
        com.lbg.ib.api.sales.soapapis.terminatearrangement.messages.TerminateArrangementRequest terminateArrangementRequest = terminateArrangementDAOImpl.createTerminateArrangementRequest(terminateArrangement);
        when(terminateArrangementSerive.terminateArrangement(any(com.lbg.ib.api.sales.soapapis.terminatearrangement.messages.TerminateArrangementRequest.class))).thenReturn(null);
        DAOResponse<TerminateArrangementDTO> response = terminateArrangementDAOImpl.terminateArrangement(terminateArrangement);
        assertTrue(response.getResult()==null);
        
    }
}
