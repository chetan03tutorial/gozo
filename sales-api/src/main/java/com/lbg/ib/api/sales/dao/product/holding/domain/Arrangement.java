package com.lbg.ib.api.sales.dao.product.holding.domain;

import java.util.List;

import com.lbg.ib.api.sso.domain.product.Event;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 * @author Debashish Bhattacharjee
 * @version 1.0
 * @since 14thFeb2016
 ***********************************************************************/
@JsonIgnoreProperties(ignoreUnknown = true)
public class Arrangement {

    private String accountName;

    private String accountNumber;

    private String sortCode;

    private String accountType;

    private String accountOpenedDate;

    private String externalSystemId;

    private ProductArrangementLifecycleStatus productArrangementLifecycleStatus;

    private String sellingLegalEntity;

    private String manufacturingLegalEntity;

    private ArrangementAdditionalDetails arrangementDetail;

    private List<Event> events;

    public String getAccountName() {
        return accountName;
    }

    public ArrangementAdditionalDetails getArrangementDetail() {
        return arrangementDetail;
    }

    public void setArrangementDetail(ArrangementAdditionalDetails arrangementDetail) {
        this.arrangementDetail = arrangementDetail;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountOpenedDate() {
        return accountOpenedDate;
    }

    public void setAccountOpenedDate(String accountOpenedDate) {
        this.accountOpenedDate = accountOpenedDate;
    }

    public ProductArrangementLifecycleStatus getProductArrangementLifecycleStatus() {
        return productArrangementLifecycleStatus;
    }

    public void setProductArrangementLifecycleStatus(
            ProductArrangementLifecycleStatus productArrangementLifecycleStatus) {
        this.productArrangementLifecycleStatus = productArrangementLifecycleStatus;
    }

    public String getSellingLegalEntity() {
        return sellingLegalEntity;
    }

    public void setSellingLegalEntity(String sellingLegalEntity) {
        this.sellingLegalEntity = sellingLegalEntity;
    }

    public String getManufacturingLegalEntity() {
        return manufacturingLegalEntity;
    }

    public void setManufacturingLegalEntity(String manufacturingLegalEntity) {
        this.manufacturingLegalEntity = manufacturingLegalEntity;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public String getExternalSystemId() {
        return externalSystemId;
    }

    public void setExternalSystemId(String externalSystemId) {
        this.externalSystemId = externalSystemId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accountName == null) ? 0 : accountName.hashCode());
        result = prime * result + ((accountNumber == null) ? 0 : accountNumber.hashCode());
        result = prime * result + ((accountOpenedDate == null) ? 0 : accountOpenedDate.hashCode());
        result = prime * result + ((accountType == null) ? 0 : accountType.hashCode());
        result = prime * result + ((arrangementDetail == null) ? 0 : arrangementDetail.hashCode());
        result = prime * result + ((events == null) ? 0 : events.hashCode());
        result = prime * result + ((manufacturingLegalEntity == null) ? 0 : manufacturingLegalEntity.hashCode());
        result = prime * result
                + ((productArrangementLifecycleStatus == null) ? 0 : productArrangementLifecycleStatus.hashCode());
        result = prime * result + ((sellingLegalEntity == null) ? 0 : sellingLegalEntity.hashCode());
        result = prime * result + ((sortCode == null) ? 0 : sortCode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Arrangement other = (Arrangement) obj;
        if (accountName == null) {
            if (other.accountName != null) {
                return false;
            }
        } else if (!accountName.equals(other.accountName)) {
            return false;
        }
        if (accountNumber == null) {
            if (other.accountNumber != null) {
                return false;
            }
        } else if (!accountNumber.equals(other.accountNumber)) {
            return false;
        }
        if (accountOpenedDate == null) {
            if (other.accountOpenedDate != null) {
                return false;
            }
        } else if (!accountOpenedDate.equals(other.accountOpenedDate)) {
            return false;
        }
        if (accountType == null) {
            if (other.accountType != null) {
                return false;
            }
        } else if (!accountType.equals(other.accountType)) {
            return false;
        }
        if (arrangementDetail == null) {
            if (other.arrangementDetail != null) {
                return false;
            }
        } else if (!arrangementDetail.equals(other.arrangementDetail)) {
            return false;
        }
        if (events == null) {
            if (other.events != null) {
                return false;
            }
        } else if (!events.equals(other.events)) {
            return false;
        }
        if (manufacturingLegalEntity != other.manufacturingLegalEntity) {
            return false;
        }
        if (productArrangementLifecycleStatus != other.productArrangementLifecycleStatus) {
            return false;
        }
        if (sellingLegalEntity != other.sellingLegalEntity) {
            return false;
        }
        if (sortCode == null) {
            if (other.sortCode != null) {
                return false;
            }
        } else if (!sortCode.equals(other.sortCode)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Arrangement [accountName=" + accountName + ", accountNumber=" + accountNumber + ", sortCode=" + sortCode
                + ", accountType=" + accountType + ", accountOpenedDate=" + accountOpenedDate
                + ", productArrangementLifecycleStatus=" + productArrangementLifecycleStatus + ", sellingLegalEntity="
                + sellingLegalEntity + ", manufacturingLegalEntity=" + manufacturingLegalEntity
                + ", additionalInformation=" + arrangementDetail + ", events=" + events + ", externalSystemId="
                + externalSystemId + "]";

    }

}
