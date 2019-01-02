package com.lbg.ib.api.sales.dto.product;

/**
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 * 
 *
 * @author Debashish Bhattacharjee
 * @version 1.0
 * @since 14thMarch2016
 */
public class ProductHoldingRequestDTO {

    private String ibSessionId;

    private String partyId;

    private String ocisId;

    public String getIbSessionId() {
        return ibSessionId;
    }

    public void setIbSessionId(String ibSessionId) {
        this.ibSessionId = ibSessionId;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getOcisId() {
        return ocisId;
    }

    public void setOcisId(String ocisId) {
        this.ocisId = ocisId;
    }

    public ProductHoldingRequestDTO(String ibSessionId, String partyId, String ocisId) {
        super();
        this.ibSessionId = ibSessionId;
        this.partyId = partyId;
        this.ocisId = ocisId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ibSessionId == null) ? 0 : ibSessionId.hashCode());
        result = prime * result + ((ocisId == null) ? 0 : ocisId.hashCode());
        result = prime * result + ((partyId == null) ? 0 : partyId.hashCode());
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
        ProductHoldingRequestDTO other = (ProductHoldingRequestDTO) obj;
        if (ibSessionId == null) {
            if (other.ibSessionId != null) {
                return false;
            }
        } else if (!ibSessionId.equals(other.ibSessionId)) {
            return false;
        }
        if (ocisId == null) {
            if (other.ocisId != null) {
                return false;
            }
        } else if (!ocisId.equals(other.ocisId)) {
            return false;
        }

        if (partyId == null) {
            if (other.partyId != null) {
                return false;
            }
        } else if (!partyId.equals(other.partyId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProductHoldingRequestDTO [ibSessionId=" + ibSessionId + ", partyId=" + partyId + ", ocisId=" + ocisId
                + "]";
    }

}
