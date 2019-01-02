
package com.lbg.ib.api.sales.dao.product.suspended;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.Assert;

import com.lbg.ib.api.sso.domain.user.UserContext;
import com.lbg.ib.api.sales.configuration.ApiServiceProperties;
import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.sales.dao.MCAHeaderUtility;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.product.pendingarrangement.ProductArrangementDTO;
import com.lbg.ib.api.sales.product.domain.pending.ArrangementId;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Customer;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Product;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductArrangement;
import com.lbg.ib.api.sales.soapapis.commonapi.faults.ExternalBusinessError;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.ResponseHeader;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.SOAPHeader;
import com.lbg.ib.api.sales.soapapis.retrievearrangement.ia_pendingarrangementmaster.IA_PendingArrangementMaster;
import com.lbg.ib.api.sales.soapapis.retrievearrangement.messages.RetrieveProductArrangementDetailsRequest;
import com.lbg.ib.api.sales.soapapis.retrievearrangement.messages.RetrieveProductArrangementDetailsResponse;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 *
 * @author Atul Choudhary
 * @version 1.0
 * @since 8thSeptember2016
 ***********************************************************************/
@RunWith(MockitoJUnitRunner.class)
public class RetrieveProductArrangementDAOImplTest {

    @Mock
    private ApiServiceProperties              properties;

    @InjectMocks
    private RetrieveProductArrangementDAOImpl dao;

    @Mock
    private DAOExceptionHandler               daoExceptionHandler;

    @Mock
    private SessionManagementDAO              session;

    @Mock
    private UserContext                       context;
    @Mock
    private GBOHeaderUtility                  gboHeaderUtility;
    @Mock
    private MCAHeaderUtility                  mcaHeaderUtility;

    @Mock
    private IA_PendingArrangementMaster       service;

    @Mock
    private LoggerDAO                         logger;

    private static final SOAPHeader           LLOYDS_SOAP_HEADER = new SOAPHeader();

    @Before
    public void setup() {
        when(session.getUserContext()).thenReturn(context);
        when(gboHeaderUtility.prepareSoapHeader("retrieveProductConditions", "retrieveProductConditions"))
                .thenReturn(asList(LLOYDS_SOAP_HEADER));
    }

    @Test
    public void shouldRetrieveProductFeaturesWhenTheBackendReturnsWithProductOptions() throws Exception {
        ProductArrangement reference = new ProductArrangement();
        reference.setArrangementId("121366");
        when(service.retrieveProductArrangementDetails(Mockito.any(RetrieveProductArrangementDetailsRequest.class)))
                .thenReturn(testResponse());
        DAOResponse<ProductArrangementDTO> pendingProdArr = dao
                .retrieveProductArrangementPending(new ArrangementId("121366"));
        Assert.isTrue(pendingProdArr.getResult().getArrangementId().equals(reference.getArrangementId()));
        Assert.isTrue(pendingProdArr.getResult().getPrimaryInvolvedParty() != null);

    }

    @Test
    public void shouldReturnErrorWhenServiceReturnsError() throws Exception {
        ProductArrangement reference = new ProductArrangement();
        reference.setArrangementId("121366");
        when(service.retrieveProductArrangementDetails(Mockito.any(RetrieveProductArrangementDetailsRequest.class)))
                .thenReturn(testErrorResponse());
        DAOResponse<ProductArrangementDTO> pendingProdArr = dao
                .retrieveProductArrangementPending(new ArrangementId("121366"));
        Assert.isTrue(pendingProdArr.getError().getErrorCode().equals("813003"));
        Assert.isTrue(pendingProdArr.getResult() == null);

    }

    @Test
    public void shouldReturnErrorFromExceptionHandlerWhenServiceThrowsError() throws Exception {
        ProductArrangement reference = new ProductArrangement();
        reference.setArrangementId("121366");
        ExternalBusinessError externalBusinessError = new ExternalBusinessError(new ResponseHeader(),
                "External business error description", "0007", "External business error text");
        DAOError error = new DAOError("0007", "External business error text");
        when(service.retrieveProductArrangementDetails(Mockito.any(RetrieveProductArrangementDetailsRequest.class)))
                .thenThrow(externalBusinessError);
        when(daoExceptionHandler.handleException(externalBusinessError, RetrieveProductArrangementDAOImpl.class,
                "retrieveProduct", new ArrangementId("121366"))).thenReturn(error);
        DAOResponse<ProductArrangementDTO> pendingProdArr = dao
                .retrieveProductArrangementPending(new ArrangementId("121366"));
        Assert.isTrue(pendingProdArr.getResult() == null);
        Assert.isTrue(pendingProdArr.getError().getErrorCode().equals("0007"));

    }

    private RetrieveProductArrangementDetailsResponse testResponse() {
        RetrieveProductArrangementDetailsResponse resp = new RetrieveProductArrangementDetailsResponse();
        ProductArrangement prodArrangement = new ProductArrangement();
        prodArrangement.setArrangementId("121366");
        Product pdt = new Product();
        pdt.setProductName("eSavings");
        prodArrangement.setAssociatedProduct(pdt);
        prodArrangement.setPrimaryInvolvedParty(new Customer());
        resp.setProductArrangement(prodArrangement);
        return resp;
    }

    private RetrieveProductArrangementDetailsResponse testErrorResponse() {
        RetrieveProductArrangementDetailsResponse resp = new RetrieveProductArrangementDetailsResponse();
        Product pdt = new Product();
        pdt.setProductName("eSavings");
        return resp;
    }

}
