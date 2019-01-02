package com.lbg.ib.api.sales.questionnaire.dto;

import LIB_SIM_BO.Communication;
import LIB_SIM_BO.ProductArrangement;

public class QuestionnaireRequestDTO {

    private ProductArrangement productArrangement;
    private Communication      communication;

    public ProductArrangement getProductArrangement() {
        return productArrangement;
    }

    public void setProductArrangement(ProductArrangement productArrangement) {
        this.productArrangement = productArrangement;
    }

    public Communication getCommunication() {
        return communication;
    }

    public void setCommunication(Communication communication) {
        this.communication = communication;
    }

}
