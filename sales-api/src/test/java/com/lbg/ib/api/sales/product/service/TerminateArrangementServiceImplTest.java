package com.lbg.ib.api.sales.product.service;


import com.lbg.ib.api.sales.dao.product.suspended.TerminateArrangementDAO;
import com.lbg.ib.api.sales.dto.product.pendingarrangement.TerminateArrangementDTO;
import com.lbg.ib.api.sales.product.domain.domains.TerminateArrangement;
import com.lbg.ib.api.sales.product.domain.domains.TerminateArrangementResponse;
import com.lbg.ib.api.sales.terminate.service.TerminateArrangementServiceImpl;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TerminateArrangementServiceImplTest {
    
    @Mock
    private GalaxyErrorCodeResolver errorResolver;
    
    @Mock
    private LoggerDAO logger;
    
    @Mock
    private TerminateArrangementDAO terminateArrangementDAO;
    
    @InjectMocks
    private TerminateArrangementServiceImpl terminateArrangementServiceImpl;
    
    @Test
    public void testTerminateArrangementValidRequest(){
        
        TerminateArrangement terminateArrangement = new TerminateArrangement("30082677","CT067484","Cancel","110");
        TerminateArrangementDTO terminateArrangementDTO = new TerminateArrangementDTO();
        terminateArrangementDTO.setArrangementId("30082677");
        when(terminateArrangementDAO.terminateArrangement(terminateArrangement)).thenReturn(withResult(terminateArrangementDTO));
        
        TerminateArrangementResponse terminateArrangementResponse = terminateArrangementServiceImpl.terminateArrangement(terminateArrangement);
        assertTrue(terminateArrangementResponse.getArrangementId().equals("30082677"));
    }
    
    
    @Test(expected=ServiceException.class)
    public void testTerminateArrangementInValidRequest(){
        TerminateArrangement temp = new TerminateArrangement();
        TerminateArrangement terminateArrangement = new TerminateArrangement("30082677",null,"Cancel","110");
        DAOResponse.DAOError daoError = new DAOResponse.DAOError("dummy","dummy");
        when(terminateArrangementDAO.terminateArrangement(terminateArrangement)).thenReturn(DAOResponse.<TerminateArrangementDTO> withError(daoError));
        
        terminateArrangementServiceImpl.terminateArrangement(terminateArrangement);
    }
    
}
