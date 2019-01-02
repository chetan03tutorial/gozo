package com.lbg.ib.api.sales.product.resources;

import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.sales.dao.product.modify.ModifyProductArrangementDAOImpl;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.product.domain.activate.AssessmentEvidence;
import com.lbg.ib.api.sales.product.domain.pending.ModifyProductArrangement;

import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerDocument;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductArrangement;
import com.lbg.ib.api.sales.soapapis.retrievearrangement.ia_pendingarrangementmaster.IA_PendingArrangementMaster;
import com.lbg.ib.api.sales.soapapis.retrievearrangement.messages.ModifyProductArrangementRequest;
import com.lbg.ib.api.sales.soapapis.retrievearrangement.messages.ModifyProductArrangementResponse;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ModifyProductArrangementDAOImplTest {
    
    @Mock
    private IA_PendingArrangementMaster modifyService;
    
    @Mock
    private GBOHeaderUtility headerUtility;
    
    @Mock
    private SessionManagementDAO session;
    
    @Mock
    private DAOExceptionHandler daoExceptionHandler;
    
    @Mock
    private ConfigurationDAO configDAO;
    
    @Mock
    private LoggerDAO logger;
    
    @InjectMocks
    private ModifyProductArrangementDAOImpl modifyProductArrangementDAOImpl;

    @Test
    public void testModifyProductArrangementWithValidRequestAndResponse() throws Exception {
        ModifyProductArrangement modifyProductArrangement = new ModifyProductArrangement();
        List<CustomerDocument> documents = new ArrayList<CustomerDocument>();
        modifyProductArrangement.setCustomerDocuments(documents);
        modifyProductArrangement.setParentAssessmentEvidence(new AssessmentEvidence());
        modifyProductArrangement.setPrimaryAssessmentEvidence(new AssessmentEvidence());
        ModifyProductArrangementResponse modifyProductArrangementResponse = new ModifyProductArrangementResponse();
        modifyProductArrangementResponse.setProductArrangement(new ProductArrangement());
        
        when(modifyService.modifyProductArrangement(any(ModifyProductArrangementRequest.class))).thenReturn(modifyProductArrangementResponse);
        DAOResponse<ModifyProductArrangement> response = modifyProductArrangementDAOImpl.modifyProductArrangement(modifyProductArrangement);
        assertTrue(response.getResult()!=null);
    }
    
    
    @Test
    public void testModifyProductArrangementWithValidRequestInvalidResponse() throws Exception {
        ModifyProductArrangement modifyProductArrangement = new ModifyProductArrangement();
        List<CustomerDocument> documents = new ArrayList<CustomerDocument>();
        modifyProductArrangement.setCustomerDocuments(documents);
        when(modifyService.modifyProductArrangement(any(ModifyProductArrangementRequest.class))).thenReturn(null);
        DAOResponse<ModifyProductArrangement> response = modifyProductArrangementDAOImpl.modifyProductArrangement(modifyProductArrangement);
        assertTrue(response.getResult()==null);
    }
}
