package com.lbg.ib.api.sales.activatebenefitarrangement.service;

import com.lbg.ib.api.sales.dto.arrangementsetup.ArrangementSetUpResponse;
import com.lbg.ib.api.sales.product.domain.lifestyle.CreateServiceArrangement;
import org.springframework.stereotype.Component;

/**
 * Created by 8796528 on 08/03/2018.
 */
@Component
public interface ActivateBenefitArrangementService {

    public ArrangementSetUpResponse createArrangementSetupService(CreateServiceArrangement createServiceArrangement);
}
