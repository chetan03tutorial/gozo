package com.lbg.ib.api.sales.dto.postcode;

import com.lbg.ib.api.sales.soapapis.postcodesearch.postcode.domain.PostalAddressExternal;

public class ExternalToDTOMapper {
    public PostalAddressDTO map(PostalAddressExternal postalAddress) {
        return new PostalAddressDTO(postalAddress.getDistrict(), postalAddress.getTown(), postalAddress.getPostcode(),
                postalAddress.getCounty(), postalAddress.getOrganisation(), postalAddress.getSubBuilding(),
                postalAddress.getBuildingName(), postalAddress.getHouseNumber(), postalAddress.getAddressLines(),
                postalAddress.getDeliveryPointSuffix());
    }
}
