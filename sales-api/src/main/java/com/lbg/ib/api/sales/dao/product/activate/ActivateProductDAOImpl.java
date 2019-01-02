/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 *
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.dao.product.activate;

import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static com.lbg.ib.api.sales.dao.constants.DAOErrorConstants.BUSSINESS_ERROR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.shared.validation.AccountType;
import com.lbg.ib.api.sales.dao.DAOExceptionHandler;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.mapper.ActivateRequestMapper;
import com.lbg.ib.api.sales.dto.product.ConditionDTO;
import com.lbg.ib.api.sales.dto.product.activate.ActivateProductDTO;
import com.lbg.ib.api.sales.dto.product.activate.ActivateProductResponseDTO;
import com.lbg.ib.api.sales.dto.product.offer.SIRAScoreDTO;
import com.lbg.ib.api.sales.soapapis.activateproduct.conditions.IA_ActivateProductArrangement;
import com.lbg.ib.api.sales.soapapis.activateproduct.reqresp.ActivateProductArrangementRequest;
import com.lbg.ib.api.sales.soapapis.activateproduct.reqresp.ActivateProductArrangementResponse;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Condition;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Customer;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerDecision;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerDocument;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerScore;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ExtSysProdIdentifier;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.Product;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.ProductArrangement;

@Component
public class ActivateProductDAOImpl implements ActivateProductDAO {

    public static final Class<ActivateProductDAOImpl> CLASS_NAME  = ActivateProductDAOImpl.class;
    static final String                               MNEMONIC_ID = "00010";
    static final String                               METHOD_NAME = "activateProduct";
    private static final String                       NO_ERROR    = "0";
    private static final String                       SIRA        = "SIRA";

    @Autowired
    @Qualifier("activateService")
    private IA_ActivateProductArrangement             activateService;

    @Autowired
    @Qualifier("activatePCAService")
    private IA_ActivateProductArrangement             activatePCAService;

    @Autowired
    private DAOExceptionHandler                       exceptionHandler;

    @Autowired
    ActivateRequestMapper                             requestMapper;

    @Autowired
    LoggerDAO                                         logger;

    @TraceLog
    public DAOResponse<ActivateProductResponseDTO> activateProduct(ActivateProductDTO requestDTO) {

        try {
            ActivateProductArrangementRequest request = requestMapper.mapRequestForActivateService(requestDTO);
            ActivateProductArrangementResponse response = null;
            if ((AccountType.CA.toString()).equalsIgnoreCase(requestDTO.getArrangementType())) {
                response = activatePCAService.activateProductArrangement(request);
            } else {
                response = activateService.activateProductArrangement(request);
            }
            DAOError error = validateResponse(response);
            if (error == null) {
                logger.traceLog(this.getClass(), "Response");
                return withResult(mapResponseStructure(response));
            } else {
                logger.traceLog(this.getClass(), "Error in Response");
                return withError(error);
            }

        } catch (Exception ex) {
            logger.traceLog(this.getClass(), "Exception in Response");
            DAOError daoError = exceptionHandler.handleException(ex, CLASS_NAME, METHOD_NAME, requestDTO);
            return withError(daoError);
        }
    }

    public void setRequestMapper(ActivateRequestMapper requestMapper) {
        this.requestMapper = requestMapper;
    }

    // Response Mapping
    private ActivateProductResponseDTO mapResponseStructure(ActivateProductArrangementResponse response) {
        String customerNumber = null;
        ProductArrangement productArrangement = response.getProductArrangement();
        Customer customerDetails = productArrangement.getPrimaryInvolvedParty();
        SIRAScoreDTO aSIRAScoreDTO = new SIRAScoreDTO();
        if (customerDetails != null) {
            customerNumber = customerDetails.getCbsCustomerNumber();
            extractSIRACustomerDecision(customerDetails, aSIRAScoreDTO);
        }

        List<CustomerDocument> custDocs = null;
        if (productArrangement.getCustomerDocuments() != null) {
            custDocs = new ArrayList<CustomerDocument>(Arrays.asList(productArrangement.getCustomerDocuments()));
        }

        return new ActivateProductResponseDTO(getProductName(productArrangement), getMnemonic(productArrangement),
                productArrangement.getArrangementType(), productArrangement.getArrangementId(),
                productArrangement.getApplicationStatus(), productArrangement.getApplicationSubStatus(),
                productArrangement.getAccountNumber(), getSortCode(productArrangement), customerNumber,
                handleErrorCaseScenario(response), aSIRAScoreDTO, custDocs);
    }

    public void extractSIRACustomerDecision(Customer customerDetails, SIRAScoreDTO aSIRAScoreDTO) {
        CustomerScore[] customerScore = customerDetails.getCustomerScore();
        if (customerScore != null) {
            for (CustomerScore score : customerScore) {
                if (SIRA.equalsIgnoreCase(score.getAssessmentType())) {
                    if (score.getCustomerDecision() != null) {
                        CustomerDecision customerDecision = score.getCustomerDecision();
                        aSIRAScoreDTO.setTotalRuleMatchCount(customerDecision.getTotalRuleMatchCount());
                        aSIRAScoreDTO.setTotalEnquiryMatchCount(customerDecision.getTotalEnquiryMatchCount());
                        aSIRAScoreDTO.setTotalRuleScore(customerDecision.getTotalRuleScore());
                        aSIRAScoreDTO.setDecisionResult(customerDecision.getResultStatus());
                        aSIRAScoreDTO.setSiraWorkFlowExecutionKey(customerDecision.getWorkflowExecutionKey());
                        aSIRAScoreDTO.setSiraConnectionErrorFlag(
                                Boolean.getBoolean(customerDecision.getConnectivityErrorFlag()));
                    }
                }
            }
        }
    }

    /**
     * This method only maps the first value form the array since there is no
     * there is no multiple error case failures form wps
     *
     */
    public List<ConditionDTO> handleErrorCaseScenario(ActivateProductArrangementResponse response) {
        List<ConditionDTO> conditionDTO = null;
        String failureCode = null;
        String failureMessage = null;
        if (response.getResultCondition() != null && response.getResultCondition().getExtraConditions() != null) {
            Condition[] extraConditions = response.getResultCondition().getExtraConditions();
            if (extraConditions != null && extraConditions.length > 0) {
                conditionDTO = new ArrayList<ConditionDTO>();
                for (Condition extraCondition : extraConditions) {
                    if (!extraCondition.getReasonCode().equalsIgnoreCase(NO_ERROR)) {
                        failureCode = extraCondition.getReasonCode();
                        failureMessage = extraCondition.getReasonText();
                        conditionDTO.add(new ConditionDTO(failureCode, null, failureMessage));
                        logger.logError(extraConditions[0].getReasonCode(), extraConditions[0].getReasonText(),
                                this.getClass());
                    }
                }
            }
        }
        return conditionDTO;
    }

    public String getMnemonic(ProductArrangement productArrangement) {
        Product[] offeredProducts = productArrangement.getOfferedProducts();
        if (offeredProducts != null && offeredProducts.length > 0
                && offeredProducts[0].getExternalSystemProductIdentifier() != null) {
            for (ExtSysProdIdentifier identifier : offeredProducts[0].getExternalSystemProductIdentifier()) {
                if (identifier.getSystemCode().equals(MNEMONIC_ID)) {
                    return identifier.getProductIdentifier();
                }
            }
        }
        return null;
    }

    private String getProductName(ProductArrangement productArrangement) {
        Product product = productArrangement.getAssociatedProduct();
        return product == null ? null : product.getProductName();
    }

    public void setActivateService(IA_ActivateProductArrangement activateService) {
        this.activateService = activateService;
    }

    private DAOError validateResponse(ActivateProductArrangementResponse response) {
        if (response == null || (response != null && response.getProductArrangement() == null)) {
            DAOError error = new DAOError(BUSSINESS_ERROR, "Activate Product Arrangement details is not found");
            logger.logError(error.getErrorCode(), error.getErrorMessage(), this.getClass());
            return error;
        }

        if (response.getResultCondition() != null) {
            if (response.getResultCondition().getReasonCode() != null) {
                DAOError error = new DAOError(response.getResultCondition().getReasonCode(),
                        response.getResultCondition().getReasonText());
                logger.logError(response.getResultCondition().getReasonCode(),
                        response.getResultCondition().getReasonText(), this.getClass());
                return error;
            }

        }
        return null;
    }

    public String getSortCode(ProductArrangement productArrangement) {
        if (productArrangement.getFinancialInstitution() != null
                && productArrangement.getFinancialInstitution().getHasOrganisationUnits(0) != null) {
            return productArrangement.getFinancialInstitution().getHasOrganisationUnits(0).getSortCode();
        }
        return null;
    }
}
