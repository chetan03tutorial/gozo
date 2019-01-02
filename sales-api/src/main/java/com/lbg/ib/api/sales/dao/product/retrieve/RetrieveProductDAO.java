package com.lbg.ib.api.sales.dao.product.retrieve;

import com.lbg.ib.api.sales.dto.product.RetrieveProductDTO;
import com.lbg.ib.api.sales.product.domain.features.ProductReference;
import com.lbg.ib.api.sales.soapapis.retrieveproduct.reqresp.RetrieveProductConditionsResponse;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.dto.product.ProductDTO;
import com.lbg.ib.api.shared.domain.ProductIdDTO;

import java.util.List;

public interface RetrieveProductDAO {
    DAOResponse<ProductDTO> retrieveProduct(ProductIdDTO requestDTO) throws Exception;

    DAOResponse<List<ProductDTO>>  retrieveProduct(RetrieveProductDTO retrieveProduct);

    public DAOResponse<RetrieveProductConditionsResponse> retrieveProductFromFamily(RetrieveProductDTO retrieveProduct);
}
