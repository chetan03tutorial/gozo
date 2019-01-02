/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.conversion.domain;

import com.lbg.ib.api.sales.communication.domain.ScheduleEmailSmsResponse;
import com.lbg.ib.api.sales.party.dto.CardOrderResponse;

/**
 * Contain Conversion response.
 * @author tkhann/amit/cshar8
 */
public class AccountConversionResponse {

    private ScheduleEmailSmsResponse communication;
    private boolean isUpgradeDone;
    private boolean isUpdateOcisDone;
    private boolean isLifeStyleDone;
    private CardOrderResponse cardOrderDetails;

    public ScheduleEmailSmsResponse getCommunication() {
        return communication;
    }

    public void setCommunication(ScheduleEmailSmsResponse communication) {
        this.communication = communication;
    }

    public boolean isUpgradeDone() {
        return isUpgradeDone;
    }

    public void setUpgradeDone(boolean upgradeDone) {
        isUpgradeDone = upgradeDone;
    }

    public boolean isUpdateOcisDone() {
        return isUpdateOcisDone;
    }

    public void setUpdateOcisDone(boolean updateOcisDone) {
        isUpdateOcisDone = updateOcisDone;
    }

    /**
     * @return the isLifeStyleDone
     */
    public boolean isLifeStyleDone() {
        return isLifeStyleDone;
    }

    /**
     * @param isLifeStyleDone the isLifeStyleDone to set
     */
    public void setLifeStyleDone(boolean isLifeStyleDone) {
        this.isLifeStyleDone = isLifeStyleDone;
    }

    /**
     * @return the cardOrderDetails
     */
    public CardOrderResponse getCardOrderDetails() {
        return cardOrderDetails;
    }

    /**
     * @param cardOrderDetails the cardOrderDetails to set
     */
    public void setCardOrderDetails(CardOrderResponse cardOrderDetails) {
        this.cardOrderDetails = cardOrderDetails;
    }

}
