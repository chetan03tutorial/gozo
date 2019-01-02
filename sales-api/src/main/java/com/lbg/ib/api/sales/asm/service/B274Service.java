package com.lbg.ib.api.sales.asm.service;

import com.lbg.ib.api.sales.dto.b274.B274RequestDTO;
import com.lbg.ib.api.sales.dto.product.overdraft.OverdraftResponseDTO;
import com.lbg.ib.api.sales.product.domain.arrangement.OverdraftIntrestRates;
import com.lbg.ib.api.shared.domain.DAOResponse;

import java.math.BigDecimal;

/**
 * Created by 8796528 on 26/07/2018.
 */

public interface B274Service {
    public B274RequestDTO mapB274Request(final BigDecimal updatedOverDraftAmount);
    public DAOResponse<OverdraftResponseDTO> retrieveOverdraftInterstRates(final B274RequestDTO requestDTO);
    public void updateB274ResponseToSession(DAOResponse<OverdraftResponseDTO> overdraftResponseFromBAPI274,
            final BigDecimal updatedOverDraftAmount);
    public OverdraftIntrestRates mappingIntrestRatesToDomainObject(final OverdraftResponseDTO responseFromBAPI274,
            BigDecimal updatedOverDraftAmount);
}
