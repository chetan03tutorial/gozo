package com.lbg.ib.api.sales.product.resources;

import com.lbg.ib.api.sales.dao.product.modify.ModifyProductArrangementDAO;
import com.lbg.ib.api.sales.product.domain.activate.AssessmentEvidence;
import com.lbg.ib.api.sales.product.domain.pending.ModifyProductArrangement;
import com.lbg.ib.api.sales.product.service.ModifyProductArrangementServiceImpl;

import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerDocument;

import java.util.ArrayList;
import java.util.List;


import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;



@RunWith(MockitoJUnitRunner.class)
public class ModifyProductArrangementServiceImplTest {
    @Mock
    private ModifyProductArrangementDAO modifyProductArrangementDAO;
    @Mock
    private GalaxyErrorCodeResolver     resolver;
    @Mock
    private LoggerDAO                   logger;
    @InjectMocks
    private ModifyProductArrangementServiceImpl modifyProductArrangementService;

    @Test
    public void testModifyProductArrangementWithValidRequest(){
        ModifyProductArrangement productArrangement = new ModifyProductArrangement();
        productArrangement.setArrangementId("30082677");
        productArrangement.setArrangementType("CA");

        List<CustomerDocument> documents = new ArrayList<CustomerDocument>();
        productArrangement.setCustomerDocuments(documents);

        productArrangement.setParentAssessmentEvidence(new AssessmentEvidence());
        productArrangement.setPrimaryAssessmentEvidence(new AssessmentEvidence());

        when(modifyProductArrangementDAO.modifyProductArrangement(productArrangement)).thenReturn(withResult(productArrangement));

        ModifyProductArrangement response = modifyProductArrangementService.modifyProductArrangement(productArrangement);
        assertTrue(response.getArrangementId().equals("30082677"));
    }

    @Test(expected=ServiceException.class)
    public void testModifyProductArrangementWithInValidRequest(){
        ModifyProductArrangement productArrangement = new ModifyProductArrangement();
        productArrangement.setArrangementId("30082677");
        productArrangement.setArrangementType("CA");

        List<CustomerDocument> documents = new ArrayList<CustomerDocument>();
        productArrangement.setCustomerDocuments(documents);

        productArrangement.setParentAssessmentEvidence(new AssessmentEvidence());
        productArrangement.setPrimaryAssessmentEvidence(new AssessmentEvidence());

        DAOResponse.DAOError daoError = new DAOResponse.DAOError("dummy","dummy");
        when(modifyProductArrangementDAO.modifyProductArrangement(productArrangement)).thenReturn(DAOResponse.<ModifyProductArrangement> withError(daoError));
        modifyProductArrangementService.modifyProductArrangement(productArrangement);
    }





}
