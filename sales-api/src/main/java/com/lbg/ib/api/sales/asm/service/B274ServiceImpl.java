package com.lbg.ib.api.sales.asm.service;

import com.lbg.ib.api.sales.dao.product.overdraft.OverdraftDAO;
import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.dto.b274.B274RequestDTO;
import com.lbg.ib.api.sales.dto.product.offer.ProductOfferedDTO;
import com.lbg.ib.api.sales.dto.product.overdraft.OverdraftRequestDTO;
import com.lbg.ib.api.sales.dto.product.overdraft.OverdraftResponseDTO;
import com.lbg.ib.api.sales.product.domain.ArrangeToActivateParameters;
import com.lbg.ib.api.sales.product.domain.arrangement.Overdraft;
import com.lbg.ib.api.sales.product.domain.arrangement.OverdraftIntrestRates;
import com.lbg.ib.api.sales.product.service.ArrangementService;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.service.branding.dao.ChannelBrandingDAO;
import com.lbg.ib.api.shared.service.branding.dto.ChannelBrandDTO;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import com.lbg.ib.api.sales.dao.product.overdraft.OverdraftDAO;
import org.springframework.stereotype.Component;

import static com.lbg.ib.api.sales.common.constant.Constants.B274ServiceConstants.INTEREST_RATES;

/**
 * Created by 8796528 on 26/07/2018.
 */

@Component
public class B274ServiceImpl  implements B274Service{

    @Autowired
    private  OverdraftDAO overdraftDAO;

    @Autowired
    private ChannelBrandingDAO channelService;

    @Autowired
    private SessionManagementDAO session;

    @Autowired
    private  ConfigurationDAO configurationService;

    @Autowired
    private LoggerDAO logger;

    public B274RequestDTO mapB274Request(final BigDecimal updatedOverDraftAmount){
        B274RequestDTO b274RequestDTO = new B274RequestDTO();
        ArrangeToActivateParameters arrangeToActivateParameters = session.getArrangeToActivateParameters();
        b274RequestDTO.setMnemonic(arrangeToActivateParameters.getProductMnemonic());
        b274RequestDTO.setCbsProductNumberTrimmed(arrangeToActivateParameters.getCbsProductNumberTrimmed());
        b274RequestDTO.setCurrentYearOfStudy(arrangeToActivateParameters.getCurrentYearOfStudy());
        b274RequestDTO.setOverdraftAmount(updatedOverDraftAmount);
        b274RequestDTO.setProductOptions(arrangeToActivateParameters.getProductOptions());
        return b274RequestDTO;
    }

    public DAOResponse<OverdraftResponseDTO> retrieveOverdraftInterstRates(final B274RequestDTO requestDTO) {
        final OverdraftRequestDTO overdraftRequestDTO = mapOverdraftRequestAttributes(requestDTO);
        DAOResponse<OverdraftResponseDTO> overdraftResponse = overdraftDAO.fetchOverdraftDetails(overdraftRequestDTO);
        if (overdraftResponse != null && overdraftResponse.getResult() != null) {
            if (overdraftResponse.getResult().getAmtIntFreeOverdraft().intValue() > 0
                    && INTEREST_RATES.equals(overdraftResponse.getResult().getIntrateAuthMnthly())
                    && INTEREST_RATES.equals(overdraftResponse.getResult().getIntrateAuthEAR())
                    && overdraftResponse.getResult().getAmtUsageFeeOverdraft().intValue() == 0) {
                overdraftRequestDTO.setAmtOverdraft(
                        overdraftResponse.getResult().getAmtIntFreeOverdraft().add(new BigDecimal(BigInteger.ONE)));
                overdraftResponse = overdraftDAO.fetchOverdraftDetails(overdraftRequestDTO);
            }

            return overdraftResponse;
        } else {
            logger.traceLog(this.getClass(),"Overdraft Response  error is : " +((overdraftResponse!=null)? overdraftResponse.getError():""));
            return null;
        }
    }

    public void updateB274ResponseToSession(DAOResponse<OverdraftResponseDTO> overdraftResponseFromBAPI274,
                                             final BigDecimal updatedOverDraftAmount){
        if (overdraftResponseFromBAPI274 != null && overdraftResponseFromBAPI274.getResult() != null) {
            final OverdraftResponseDTO responseFromBAPI274 = overdraftResponseFromBAPI274.getResult();
            Overdraft overdraftFeatures = new Overdraft();
            overdraftFeatures = mappingIntrestRatesToOfferResponse(responseFromBAPI274, overdraftFeatures,
                    updatedOverDraftAmount);
            ArrangeToActivateParameters arrangeToActivateParameters = session.getArrangeToActivateParameters();
            arrangeToActivateParameters.setOverdraftIntrestRates(
                    mappingIntrestRatesToDomainObject(responseFromBAPI274, updatedOverDraftAmount));

            if(session.getPackagedAccountSessionInfo()!=null) {
                session.getPackagedAccountSessionInfo().getOfferResponse().setOverdraft(overdraftFeatures);
            }
        }
    }


    public OverdraftIntrestRates mappingIntrestRatesToDomainObject(final OverdraftResponseDTO responseFromBAPI274,
            BigDecimal updatedOverDraftAmount) {
        final OverdraftIntrestRates overdraftIntrestRates = new OverdraftIntrestRates();
        overdraftIntrestRates.setAmtOverdraft(updatedOverDraftAmount);

        overdraftIntrestRates.setAmtIntFreeOverdraft(responseFromBAPI274.getAmtIntFreeOverdraft());
        overdraftIntrestRates.setAmtUsageFeeOverdraft(responseFromBAPI274.getAmtUsageFeeOverdraft());
        overdraftIntrestRates.setIntrateAuthEAR(responseFromBAPI274.getIntrateAuthEAR());
        overdraftIntrestRates.setIntrateAuthMnthly(responseFromBAPI274.getIntrateAuthMnthly());
        overdraftIntrestRates.setAmtExcessFee(responseFromBAPI274.getAmtExcessFee());
        overdraftIntrestRates.setAmtExcessFeeBalIncr(responseFromBAPI274.getAmtExcessFeeBalIncr());
        overdraftIntrestRates.setAmtTotalCreditCost(responseFromBAPI274.getAmtTotalCreditCost());
        overdraftIntrestRates.setIntrateBase(responseFromBAPI274.getIntrateBase());
        overdraftIntrestRates.setIntrateMarginOBR(responseFromBAPI274.getIntrateMarginOBR());
        overdraftIntrestRates.setIntrateUnauthEAR(responseFromBAPI274.getIntrateUnauthEAR());
        overdraftIntrestRates.setIntrateUnauthMnthly(responseFromBAPI274.getIntrateUnauthMnthly());
        overdraftIntrestRates.setNExcessFeeCap(responseFromBAPI274.getNExcessFeeCap());
        return overdraftIntrestRates;
    }

    private Overdraft mappingIntrestRatesToOfferResponse(final OverdraftResponseDTO responseFromBAPI274,
            final Overdraft overdraftFeatures, final BigDecimal overDraftAmount) {
        overdraftFeatures.setAmount(overDraftAmount);
        overdraftFeatures.setIntFreeAmount(responseFromBAPI274.getAmtIntFreeOverdraft());
        overdraftFeatures.setUsageFee(responseFromBAPI274.getAmtUsageFeeOverdraft());
        overdraftFeatures.setIntEar(responseFromBAPI274.getIntrateAuthEAR());
        overdraftFeatures.setIntMnthEar(responseFromBAPI274.getIntrateAuthMnthly());
        overdraftFeatures.setAmtExcessFee(responseFromBAPI274.getAmtExcessFee());
        overdraftFeatures.setAmtExcessFeeBalIncr(responseFromBAPI274.getAmtExcessFeeBalIncr());
        overdraftFeatures.setAmtTotalCreditCost(responseFromBAPI274.getAmtTotalCreditCost());
        overdraftFeatures.setIntrateBase(responseFromBAPI274.getIntrateBase());
        overdraftFeatures.setIntrateMarginOBR(responseFromBAPI274.getIntrateMarginOBR());
        overdraftFeatures.setIntrateUnauthEAR(responseFromBAPI274.getIntrateUnauthEAR());
        overdraftFeatures.setIntrateUnauthMnthly(responseFromBAPI274.getIntrateUnauthMnthly());
        overdraftFeatures.setnExcessFeeCap(responseFromBAPI274.getNExcessFeeCap());
        return overdraftFeatures;
    }

    private OverdraftRequestDTO mapOverdraftRequestAttributes(B274RequestDTO requestDTO) {
        final OverdraftRequestDTO overdraftRequestDTO = new OverdraftRequestDTO();
        overdraftRequestDTO.setUserContext(session.getUserContext());
        final DAOResponse<ChannelBrandDTO> channel = channelService.getChannelBrand();
        if (channel != null && channel.getError() == null) {
            final String contactPointId = generateSortCode(channel);
            if (contactPointId != null) {
                overdraftRequestDTO.setSortcode(contactPointId);
            }
        }
        // Default Behaviours
        overdraftRequestDTO.setAmtOverdraft(requestDTO.getOverdraftAmount());

        overdraftRequestDTO.setCbsprodnum(new BigInteger(
                requestDTO.getCbsProductNumberTrimmed() == null ? "0" : requestDTO.getCbsProductNumberTrimmed()));

        // Adding Logic for Tariff fetching logic for OD
        String tariffKey = "Tariff";
        if ("P_STUDENT".equals(requestDTO.getMnemonic())) {
            tariffKey = ArrangementService.Tariffs
                    .getTariffKeyFromStudentYear(requestDTO.getCurrentYearOfStudy() != null ? requestDTO.getCurrentYearOfStudy().intValue() : 0);

        }
        overdraftRequestDTO.setCbstariff(requestDTO.getProductOptions().get(tariffKey));
        return overdraftRequestDTO;
    }

    private String generateSortCode(final DAOResponse<ChannelBrandDTO> channel) {
        final String contactPointId = configurationService.getConfigurationStringValue("TRIMMED_CONTACT_POINT_ID",
                channel.getResult().getChannelId());
        return contactPointId;
    }
}
