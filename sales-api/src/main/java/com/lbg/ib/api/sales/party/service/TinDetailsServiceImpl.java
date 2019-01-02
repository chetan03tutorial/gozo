/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/
package com.lbg.ib.api.sales.party.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.common.constant.Constants;
import com.lbg.ib.api.sales.common.constant.ResponseErrorConstants;
import com.lbg.ib.api.shared.exception.GalaxyErrorCodeResolver;
import com.lbg.ib.api.shared.exception.ServiceException;
import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.sales.dao.party.classify.ClassifyInvolvedPartyDAO;
import com.lbg.ib.api.sales.dto.party.ClassifyPartyRequestDTO;
import com.lbg.ib.api.sales.dto.party.ClassifyPartyResponseDTO;
import com.lbg.ib.api.sales.party.domain.ClassifiedPartyDetails;
import com.lbg.ib.api.shared.domain.TaxResidencyDetails;
import com.lbg.ib.api.shared.domain.TinDetails;

@Component
public class TinDetailsServiceImpl implements TinDetailsService {

    @Autowired
    private GalaxyErrorCodeResolver  resolver;

    @Autowired
    private ClassifyInvolvedPartyDAO classifyInvolvedPartyDAO;

    public List<ClassifiedPartyDetails> identify(TinDetails partyDetails) throws ServiceException {
        DAOResponse<List<ClassifyPartyResponseDTO>> results;
        results = classifyInvolvedPartyDAO.classify(populateClassifyPartyDTO(partyDetails));
        if (results == null) {
            throw new ServiceException(resolver.resolve(ResponseErrorConstants.CLASSIFY_INVOLVED_PARTY_NOT_FOUND));
        }
        if (results.getError() != null || results.getResult().size()==0) {

            Set<TaxResidencyDetails> taxResidencies = partyDetails.getTaxResidencies();
            if (taxResidencies.size() == 1) {
                for (TaxResidencyDetails taxResidency : taxResidencies) {
                    if (Constants.CountryConstants.FRENCH_MNEMONIC.equals(taxResidency.getTaxResidency())) {
                        List<ClassifiedPartyDetails> classifiedPartyDetailsList = new ArrayList<ClassifiedPartyDetails>();
                        ClassifiedPartyDetails classifiedPartyDetails = new ClassifiedPartyDetails();
                        classifiedPartyDetails.setCountryName(Constants.CountryConstants.FRENCH_MNEMONIC);
                        classifiedPartyDetails.setTinRequired(false);
                        classifiedPartyDetails.setTaxResidencyType(Constants.CountryConstants.CRS);
                        classifiedPartyDetailsList.add(classifiedPartyDetails);
                        return classifiedPartyDetailsList;
                    }
                }
            }

            throw new ServiceException(resolver.resolve(results.getError().getErrorCode()));
        }
        List<ClassifiedPartyDetails> classifiedPartyDetails = new ArrayList<ClassifiedPartyDetails>();
        List<ClassifyPartyResponseDTO> classifyPartyResponseDTOs = results.getResult();
        if (classifyPartyResponseDTOs == null || classifyPartyResponseDTOs.isEmpty()) {
            throw new ServiceException(
                    resolver.resolve(ResponseErrorConstants.UNEXPECTED_VALUE_IN_CLASSIFY_INVOLVED_PARTY));
        }

        for (ClassifyPartyResponseDTO classifyPartyResponseDTO : classifyPartyResponseDTOs) {
            ClassifiedPartyDetails classifiedPartyDetail = new ClassifiedPartyDetails();
            classifiedPartyDetail.setCountryName(classifyPartyResponseDTO.getCountryName());
            classifiedPartyDetail.setTinRequired(classifyPartyResponseDTO.isTinRequired());
            classifiedPartyDetail.setRegex(classifyPartyResponseDTO.getRegex());
            classifiedPartyDetail.setTaxResidencyType(classifyPartyResponseDTO.getTaxResidencyType());
            classifiedPartyDetails.add(classifiedPartyDetail);
        }
        return classifiedPartyDetails;
    }

    private ClassifyPartyRequestDTO populateClassifyPartyDTO(TinDetails partyDetails) {
        ClassifyPartyRequestDTO classifyPartyRequestDTO = new ClassifyPartyRequestDTO();
        classifyPartyRequestDTO.setBirthCountry(partyDetails.getBirthCountry());
        classifyPartyRequestDTO.setNationalities(partyDetails.getNationalities());
        classifyPartyRequestDTO.setTaxResidencies(partyDetails.getTaxResidencies());
        return classifyPartyRequestDTO;
    }

}
