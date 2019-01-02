package com.lbg.ib.api.sales.address.service;

import java.util.ArrayList;
import java.util.List;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sso.domain.address.PostalAddress;
import com.lbg.ib.api.sales.address.domain.Postcode;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dao.postcode.PostcodeSearchDAO;
import com.lbg.ib.api.sales.dto.postcode.PostalAddressDTO;
import com.lbg.ib.api.sales.dto.postcode.PostcodeDTO;

@Component
public class AddressServiceImpl implements AddressService {

    private PostcodeSearchDAO       postcodeSearchDAO;
    private GalaxyErrorCodeResolver resolver;
    private LoggerDAO               logger;

    @Autowired
    public AddressServiceImpl(PostcodeSearchDAO postcodeSearchDAO, GalaxyErrorCodeResolver resolver, LoggerDAO logger) {
        this.postcodeSearchDAO = postcodeSearchDAO;
        this.resolver = resolver;
        this.logger = logger;
    }

    @TraceLog
    public List<PostalAddress> check(Postcode postcode) throws ServiceException {
        DAOResponse<List<PostalAddressDTO>> search;
        try {
            search = postcodeSearchDAO.search(new PostcodeDTO(postcode.outPostcode(), postcode.inPostcode()));
        } catch (Exception e) {
            logger.logException(AddressServiceImpl.class, e);
            throw new ServiceException(resolver.resolve(ResponseErrorConstants.SERVICE_UNAVAILABLE));
        }
        if (search.getError() != null) {
            throw new ServiceException(resolver.resolve(search.getError().getErrorCode()));
        }
        return addresses(search.getResult());
    }

    private List<PostalAddress> addresses(List<PostalAddressDTO> dtos) {
        List<PostalAddress> addresses = new ArrayList<PostalAddress>();
        if (dtos == null) {
            return addresses;
        }
        for (PostalAddressDTO dto : dtos) {
            addresses.add(new PostalAddress(dto.getDistrict(), dto.getTown(), dto.getCounty(), dto.getOrganisation(),
                    dto.getSubBuilding(), dto.getBuildingName(), dto.getBuildingNumber(), dto.getAddressLines(),
                    dto.getPostcode(), dto.getDeliveryPointSuffix()));
        }
        return addresses;
    }
}
