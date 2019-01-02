/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 *
 * All Rights Reserved.
 ***********************************************************************/

package integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.net.URL;

import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.dao.GBOHeaderUtility;
import com.lbg.ib.api.sales.dao.product.retrieve.RetrieveProductDAOImpl;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.product.ProductDTO;
import com.lbg.ib.api.shared.domain.ProductIdDTO;
import com.lbg.ib.api.sales.soapapis.retrieveproduct.conditions.IA_RetrieveProductConditions;
import com.lbg.ib.api.sales.soapapis.retrieveproduct.conditions.binding.Exp_IA_RetrieveProductConditions_SOAP_IA_RetrieveProductConditionsHttpServiceLocator;

@RunWith(MockitoJUnitRunner.class)
public class RetrieveProductDAOImplIT extends BaseIT {

    @InjectMocks
    private RetrieveProductDAOImpl       retrieveProductDAOImpl;

    @Mock
    private IA_RetrieveProductConditions service;

    @Mock
    private DAOExceptionHandler          daoExceptionHandler;

    @Mock
    private GBOHeaderUtility             headerUtility;

    @Mock
    private SessionManagementDAO         session;

    private static final String          PRODUCT_ID = "20022";

    @Ignore
    @Test
    public void shouldRetrieveProductArrangement() throws Exception {
        Exp_IA_RetrieveProductConditions_SOAP_IA_RetrieveProductConditionsHttpServiceLocator serviceLocator = new Exp_IA_RetrieveProductConditions_SOAP_IA_RetrieveProductConditionsHttpServiceLocator();
        service = serviceLocator.getExp_IA_RetrieveProductConditionsExport_SOAP_IA_RetrieveProductConditionsHttpPort(
                new URL("http://10.245.211.251:22910/wps/sales/v1.0/retrieveProductConditions"));
        retrieveProductDAOImpl.setRetrieveService(service);

        when(headerUtility.prepareSoapHeader("retrieveProductConditions", "RetrieveProductConditions"))
                .thenReturn(populateRequestHeader());

        DAOResponse<ProductDTO> response = retrieveProductDAOImpl.retrieveProduct(testRequest());
        assertNotNull(response);
        assertNull(response.getError());
        assertNotNull(response.getResult());
    }

    private ProductIdDTO testRequest() {
        return new ProductIdDTO(PRODUCT_ID);
    }

}