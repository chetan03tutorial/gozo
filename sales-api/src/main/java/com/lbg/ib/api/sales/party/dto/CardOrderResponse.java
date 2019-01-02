/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.party.dto;

import java.util.List;

import com.lbg.ib.api.sales.party.CardOrder;

public class CardOrderResponse {

    private List<CardOrder> cardOrder;
    private String statusCode;
    private String responseText;

    /**
     * @return the statusCode
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return the responseText
     */
    public String getResponseText() {
        return responseText;
    }

    /**
     * @param responseText the responseText to set
     */
    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    /**
     * @return the cardOrder
     */
    public List<CardOrder> getCardOrder() {
        return cardOrder;
    }

    /**
     * @param cardOrder the cardOrder to set
     */
    public void setCardOrder(List<CardOrder> cardOrder) {
        this.cardOrder = cardOrder;
    }

}
