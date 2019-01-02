package com.lbg.ib.api.sales.dao.postcode;

import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.dto.postcode.PostcodeDTO;
import com.lbg.ib.api.sales.dto.postcode.PostalAddressDTO;

import java.util.List;

public interface PostcodeSearchDAO {
    DAOResponse<List<PostalAddressDTO>> search(PostcodeDTO postcodeDTO) throws Exception;
}
