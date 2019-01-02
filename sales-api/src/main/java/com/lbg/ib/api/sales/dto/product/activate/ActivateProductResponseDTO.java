
package com.lbg.ib.api.sales.dto.product.activate;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

import java.util.List;

import com.lbg.ib.api.sales.dto.product.ConditionDTO;
import com.lbg.ib.api.sales.dto.product.offer.SIRAScoreDTO;
import com.lbg.ib.api.sales.soapapis.commonapi.businessobjects.CustomerDocument;

public class ActivateProductResponseDTO {
    private String                 productName;

    private String                 mnemonic;

    private String                 arrangementType;

    private String                 arrangementId;

    private String                 applicationStatus;

    private String                 applicationSubStatus;

    private String                 accountNo;

    private String                 sortCode;

    private String                 customerNumber;

    private List<ConditionDTO>     conditionDTO;

    private List<CustomerDocument> customerDocuments;

    private SIRAScoreDTO           siraScoreDTO;

    public ActivateProductResponseDTO() {
        // Comments to avoid Sonar Violations
    }

    public ActivateProductResponseDTO(String productName, String mnemonic, String arrangementType, String arrangementId,
            String applicationStatus, String applicationSubStatus, String accountNo, String sortCode,
            String customerNumber, List<ConditionDTO> conditionDTO, SIRAScoreDTO siraScoreDTO,
            List<CustomerDocument> customerDocuments) {
        this.productName = productName;
        this.mnemonic = mnemonic;
        this.arrangementType = arrangementType;
        this.arrangementId = arrangementId;
        this.applicationStatus = applicationStatus;
        this.applicationSubStatus = applicationSubStatus;
        this.accountNo = accountNo;
        this.sortCode = sortCode;
        this.conditionDTO = conditionDTO;
        this.customerNumber = customerNumber;
        this.siraScoreDTO = siraScoreDTO;
        this.customerDocuments = customerDocuments;
    }

    public String getProductName() {
        return productName;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public String getArrangementType() {
        return arrangementType;
    }

    public String getArrangementId() {
        return arrangementId;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public String getApplicationSubStatus() {
        return applicationSubStatus;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public String getSortCode() {
        return sortCode;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public List<ConditionDTO> getConditionDTO() {
        return conditionDTO;
    }

    public SIRAScoreDTO getSiraScoreDTO() {
        return siraScoreDTO;
    }

    public void setSiraScoreDTO(SIRAScoreDTO siraScoreDTO) {
        this.siraScoreDTO = siraScoreDTO;
    }

    public List<CustomerDocument> getCustomerDocuments() {
        return customerDocuments;
    }

    public void setConditionDTO(List<ConditionDTO> conditionDTO) {
        this.conditionDTO = conditionDTO;
    }

    public void setCustomerDocuments(List<CustomerDocument> customerDocuments) {
        this.customerDocuments = customerDocuments;
    }

    @Override
    public boolean equals(Object o) {
        return reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }
}
