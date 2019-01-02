package com.lbg.ib.api.sales.dao.product.suspended;

import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static com.lbg.ib.api.sales.dao.constants.DAOErrorConstants.BUSSINESS_ERROR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ArrangementHistory;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Customer;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerDocument;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Product;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductArrangement;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.BaseRequest;
import com.lbg.ib.api.sales.soapapis.commonapi.messages.RequestHeader;
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
@Component
public class RetrieveProductArrangementDAOImpl implements RetrieveProductArrangementDAO {
    public static final String                                    METHOD_NAME    = "retrieveProduct";
    private static final String                                   ERROR_KEY      = "retrievePendingProduct";
    private static final String                                   SERVICE_NAME   = "retrieveProductArrangement";
    private static final String                                   SERVICE_ACTION = "retrieveProductArrangement";
    private static final Class<RetrieveProductArrangementDAOImpl> CLASS_NAME     = RetrieveProductArrangementDAOImpl.class;
    // @Autowired private IAPendingArrangementMaster retrieveService;
    @Autowired
    private LoggerDAO                                             logger;
    @Autowired
    private SessionManagementDAO                                  session;
    @Autowired
    private ApiServiceProperties                                  properties;
    @Autowired
    private DAOExceptionHandler                                   daoExceptionHandler;
    @Autowired
    private IA_PendingArrangementMaster                           pendingArrangementSerive;
    @Autowired
    private GBOHeaderUtility                                      gboHeaderUtility;
    @Autowired
    private MCAHeaderUtility                                      mcaHeaderUtility;

    public DAOResponse<ProductArrangementDTO> retrieveProductArrangementPending(ArrangementId referenceId) {
        try {
            RetrieveProductArrangementDetailsRequest request = createRequest(referenceId);
            RetrieveProductArrangementDetailsResponse response = pendingArrangementSerive
                    .retrieveProductArrangementDetails(request);

            DAOError error = validateResponse(response);
            if (error == null) {
                return withResult(mapResponseStructure(response));
            } else {
                return withError(error);
            }

        } catch (Exception ex) {
            DAOError daoError = daoExceptionHandler.handleException(ex, CLASS_NAME, METHOD_NAME, referenceId);
            return withError(daoError);
        }

    }

    private RetrieveProductArrangementDetailsRequest createRequest(ArrangementId referenceId) {
        RetrieveProductArrangementDetailsRequest request = new RetrieveProductArrangementDetailsRequest();
        request.setHeader(populateHeaders().getHeader());

        request.setProductArrangement(createProductArrangement(referenceId));
        return request;
    }

    private ProductArrangement createProductArrangement(ArrangementId referenceId) {
        String productRef = referenceId.getValue();
        ProductArrangement productArr = new ProductArrangement();
        productArr.setArrangementId(productRef);
        return productArr;
    }

    private BaseRequest populateHeaders() {
        RequestHeader requestHeader = new RequestHeader();
        List<SOAPHeader> soapHeaders = null;
        soapHeaders = gboHeaderUtility.prepareSoapHeader(SERVICE_ACTION, SERVICE_NAME);
        if (null != soapHeaders) {
            // if (null != requestHeader.getLloydsHeaders()) {
            SOAPHeader[] soapHeaderArray = soapHeaders.toArray(new SOAPHeader[soapHeaders.size()]);
            // requestHeader.getLloydsHeaders().addAll(soapHeaders);
            SOAPHeader[] both = (SOAPHeader[]) ArrayUtils.addAll(requestHeader.getLloydsHeaders(), soapHeaderArray);
            requestHeader.setLloydsHeaders(both);
        }
        requestHeader.setBusinessTransaction("retrieveProductArrangement");
        requestHeader.setInteractionId(session.getSessionId());
        BaseRequest baseRequest = new BaseRequest();
        baseRequest.setHeader(requestHeader);
        return baseRequest;
    }

    private DAOError validateResponse(RetrieveProductArrangementDetailsResponse response) {
        if (response != null && response.getProductArrangement() != null) {
            return null;
        } else {
            DAOError error = new DAOError(BUSSINESS_ERROR, "ProductArrangement can not be null");
            logger.logError(error.getErrorCode(), error.getErrorMessage(), this.getClass());
            return error;
        }
    }

    private ProductArrangementDTO mapResponseStructure(RetrieveProductArrangementDetailsResponse response) {
        ProductArrangementDTO productArrangementDTO = new ProductArrangementDTO();
        if (response != null && response.getProductArrangement() != null) {
            ProductArrangement productArr = response.getProductArrangement();
            productArrangementDTO.setArrangementId(productArr.getArrangementId());
            productArrangementDTO.setAccountNumber(productArr.getAccountNumber());
            productArrangementDTO.setAccountPurpose(productArr.getAccountPurpose());
            if (productArr.getPrimaryInvolvedParty() != null) {
                Customer cust = productArr.getPrimaryInvolvedParty();
                productArrangementDTO.setPrimaryInvolvedParty(cust);
            }
            if (productArr.getAssociatedProduct() != null) {
                Product pdtAssociated = productArr.getAssociatedProduct();
                productArrangementDTO.setAssociatedProduct(pdtAssociated);
            }

            if (productArr.getCustomerDocuments() != null) {
                CustomerDocument[] customerDocuments = productArr.getCustomerDocuments();
                productArrangementDTO.setCustomerDocuments(customerDocuments);
            }
            if (productArr.getFinancialInstitution() != null) {
                productArrangementDTO.setFinancialInstitution(productArr.getFinancialInstitution());
            }

            if (productArr.getArrangementHistory() != null) {
                productArrangementDTO.getArrangementHistory()
                        .addAll(new ArrayList<ArrangementHistory>(Arrays.asList(productArr.getArrangementHistory())));
            }
        }
        return productArrangementDTO;
    }

}
