package com.lbg.ib.api.sales.address.service;

import com.lbg.ib.api.sso.domain.address.PostalAddress;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.address.domain.Postcode;

import java.util.List;

public interface AddressService {
    List<PostalAddress> check(Postcode postcode) throws ServiceException;
}
