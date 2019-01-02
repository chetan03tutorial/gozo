package com.lbg.ib.api.sales.questionnaire.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import LIB_SIM_BO.Channel;
import LIB_SIM_BO.Communication;
import LIB_SIM_BO.Customer;
import LIB_SIM_BO.InformationContent;
import LIB_SIM_BO.Product;
import LIB_SIM_BO.ProductArrangement;
import LIB_SIM_BO.ProductOptions;
import LIB_SIM_BO.ProductRelatedOptions;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.ResponseError;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.questionnaire.dao.QuestionnaireDao;
import com.lbg.ib.api.sales.questionnaire.domain.message.ProductFeature;
import com.lbg.ib.api.sales.questionnaire.domain.message.ProductRelatedFeature;
import com.lbg.ib.api.sales.questionnaire.domain.message.QuestionnaireRequestBean;
import com.lbg.ib.api.sales.questionnaire.domain.message.QuestionnaireResponseBean;
import com.lbg.ib.api.sales.questionnaire.dto.QuestionnaireRequestDTO;
import com.lbg.ib.api.sales.questionnaire.dto.QuestionnaireResponseDTO;

@Component
public class QuestionnaireServiceImpl implements QuestionnaireService {

    @Autowired
    private QuestionnaireDao daoInstance;

    @Autowired
    private LoggerDAO        logger;

    @TraceLog
    public QuestionnaireResponseBean recordQuestionnaire(QuestionnaireRequestBean requestBean) throws ServiceException {
        DAOResponse<QuestionnaireResponseDTO> results;
        results = daoInstance.recordQuestionnaire(populateRequestDTO(requestBean));

        if (results.getError() != null) {
            logger.logError(results.getError().getErrorCode(), results.getError().getErrorMessage(), DAOResponse.class);
            throw new ServiceException(new ResponseError(ResponseErrorConstants.ERROR_SAVING_ARRANGEMENT,
                    results.getError().getErrorMessage()));
        }

        return processResponse(results.getResult());
    }

    private QuestionnaireRequestDTO populateRequestDTO(QuestionnaireRequestBean requestBean) {
        return prepareRequest(requestBean);
    }

    private QuestionnaireResponseBean processResponse(QuestionnaireResponseDTO responseDTO) {
        QuestionnaireResponseBean responseBean = new QuestionnaireResponseBean();
        responseBean.setReasonCode(Integer.parseInt(responseDTO.getReasonCode()));
        responseBean.setReasonText(responseDTO.getReasonText());
        return responseBean;
    }

    private QuestionnaireRequestDTO prepareRequest(QuestionnaireRequestBean request) {
        QuestionnaireRequestDTO requestDTO = new QuestionnaireRequestDTO();

        ProductArrangement productArrangement = new ProductArrangement();
        productArrangement.setAccountNumber(request.getAccountNumber());
        productArrangement.setArrangementType(request.getArrangementType());
        productArrangement.setArrangementId(request.getArrangementId());

        Product product = new Product();
        product.setProductIdentifier(request.getProduct().getProductIdentifier());
        product.setProductName(request.getProduct().getProductName());
        List<ProductOptions> productOptionsList = new ArrayList<ProductOptions>();
        List<ProductRelatedOptions> productRelatedOptionsList = null;

        for (ProductFeature productOption : request.getProduct().getProductOptions()) {
            ProductOptions productOptions = new ProductOptions();
            productOptions.setOptionsType(productOption.getOptionType());
            // Code Changes for Prod Fix
            // productOptions.setOptionsValue(ProductRelatedFeature.getBinaryValue(productOption.getOptionValue()));
            productOptions.setOptionsValue(productOption.getOptionValue());
            productRelatedOptionsList = new ArrayList<ProductRelatedOptions>();
            for (ProductRelatedFeature relatedOption : productOption.getProductRelatedOptions()) {
                ProductRelatedOptions productRelatedOption = new ProductRelatedOptions();
                productRelatedOption.setRelatedOptionsIdentifier(relatedOption.getRelatedOptionsIdentifier());
                // Code Changes for Prod Fix
                productRelatedOption.setRelatedOptionsValue(
                        ProductRelatedFeature.getBinaryValue(relatedOption.getRelatedOptionsValue()));
                productRelatedOptionsList.add(productRelatedOption);
                // productOptions.getProductRelatedOptions().add(productRelatedOption);
            }
            productOptions.setProductRelatedOptions(
                    productRelatedOptionsList.toArray(new ProductRelatedOptions[productRelatedOptionsList.size()]));
            productOptionsList.add(productOptions);
        }
        product.setProductoptions(productOptionsList.toArray(new ProductOptions[productOptionsList.size()]));

        List<ProductRelatedFeature> productRelatedOptionsInReq = request.getProduct().getProductRelatedOption();
        List<ProductRelatedOptions> productRelatedOptionList = new ArrayList<ProductRelatedOptions>();
        for (ProductRelatedFeature relatedOption : productRelatedOptionsInReq) {
            ProductRelatedOptions prdRelatedOption = new ProductRelatedOptions();
            prdRelatedOption.setRelatedOptionsIdentifier(relatedOption.getRelatedOptionsIdentifier());
            prdRelatedOption.setRelatedOptionsValue(relatedOption.getRelatedOptionsValue());
            productRelatedOptionList.add(prdRelatedOption);
        }
        // changes
        product.setProductRelatedOptions(
                productRelatedOptionList.toArray(new ProductRelatedOptions[productRelatedOptionList.size()]));
        productArrangement.setAssociatedProduct(product);

        Channel channel = new Channel();
        channel.setChannelCode(request.getChannelCode());
        channel.setSubChannelCode(request.getSubChannelCode());
        productArrangement.setInitiatedThrough(channel);

        Customer customer = new Customer();
        customer.setCustomerIdentifier(request.getCustomerIdentifier());
        customer.setUserType(request.getCustomerType());
        customer.setInternalUserIdentifier(request.getInternalUserIdentifier());
        customer.setPartyRole(request.getPartyRole());
        productArrangement.setPrimaryInvolvedParty(customer);

        Communication communication = new Communication();
        List<InformationContent> listInfoContent = new ArrayList<InformationContent>();
        InformationContent informationContent = new InformationContent();
        informationContent.setKey(request.getCommunicationKey());
        informationContent.setValue(request.getCommunicationValue());
        listInfoContent.add(informationContent);
        communication
                .setHasCommunicationContent(listInfoContent.toArray(new InformationContent[listInfoContent.size()]));
        // communication.getHasCommunicationContent().add(informationContent);

        requestDTO.setCommunication(communication);
        requestDTO.setProductArrangement(productArrangement);

        return requestDTO;
    }

}
