package com.lbg.ib.api.sales.product.service;

import com.lbg.ib.api.sales.dto.product.RetrieveProductDTO;
import com.lbg.ib.api.sales.soapapis.retrieveproduct.reqresp.RetrieveProductConditionsResponse;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.sales.product.domain.features.ProductReference;
import com.lbg.ib.api.sales.product.domain.features.ProductFeatures;

import java.util.List;

public interface RetrieveProductFeaturesService {
    ProductFeatures retrieveProductFeatures(ProductReference reference) throws ServiceException;

    ProductFeatures retrieveProductFeaturesByProductId(ProductReference reference) throws ServiceException;

    ProductFeatures retrieveProductFeaturesUsingProductId(ProductReference reference) throws ServiceException;

    List<ProductFeatures> retrieveProductFeaturesByProductFamilyIdentifier(RetrieveProductDTO retrieveProductDTO) throws ServiceException;

    RetrieveProductConditionsResponse retrieveProductFromFamily(RetrieveProductDTO retrieveProductDTO) throws ServiceException;

    ProductFeatures retrieveProductFeaturesByProductIdNoSessionUpdate(ProductReference reference) throws ServiceException;

}
