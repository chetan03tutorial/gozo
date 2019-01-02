/**********************************************************************
 * This source code is the property of Lloyds Banking Group PLC.
 * <p/>
 * All Rights Reserved.
 ***********************************************************************/

package com.lbg.ib.api.sales.dao.party.classify;

import static com.lbg.ib.api.sales.dao.constants.DAOErrorConstants.BUSSINESS_ERROR;
import static com.lbg.ib.api.sales.dao.constants.DAOErrorConstants.REMOTE_EXCEPTION;
import static com.lbg.ib.api.shared.domain.DAOResponse.withError;
import static com.lbg.ib.api.shared.domain.DAOResponse.withResult;
import static com.lbg.ib.api.sales.dao.party.classify.ClassifyInvolvedPartyRequestBuilder.CRS_VALUE;
import static com.lbg.ib.api.sales.dao.party.classify.ClassifyInvolvedPartyRequestBuilder.FATCA_VALUE;
import static com.lbg.ib.api.sales.party.domain.TaxResidencyType.findTaxResidencyTypeFromCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.lbg.ib.api.shared.util.logger.TraceLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import LB_GBO_Sales.IA_CommonReportingStandards;

import com.lbg.ib.api.shared.domain.DAOResponse;
import com.lbg.ib.api.shared.domain.DAOResponse.DAOError;
import com.lbg.ib.api.sales.dao.SalsaDAOExceptionHandler;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lbg.ib.api.sales.dto.party.ClassifyPartyRequestDTO;
import com.lbg.ib.api.sales.dto.party.ClassifyPartyResponseDTO;
import com.lloydstsb.ib.salsa.crs.messages.AssociatedCategories;
import com.lloydstsb.ib.salsa.crs.messages.Classifications;
import com.lloydstsb.ib.salsa.crs.messages.ClassifyInvolvedPartyResponse;

@Component
public class ClassifyInvolvedPartyDAOImpl implements ClassifyInvolvedPartyDAO {

    private static final Class<ClassifyInvolvedPartyDAOImpl> CLASS_NAME       = ClassifyInvolvedPartyDAOImpl.class;
    private static String                                    METHOD_NAME      = "classify";
    private static final String                              REGEX            = "regex";
    private static final String                              TIN_REQD_CODE    = "041";
    private static final String                              US_CATEGORY_NAME = "005";
    private static final String                              SPLIT_BY_SPACE   = "\\s+";
    private static final String                              A                = "A";
    @Autowired
    private LoggerDAO                                        logger;
    @Autowired
    private IA_CommonReportingStandards                      classifyservice;
    @Autowired
    private ClassifyInvolvedPartyRequestBuilder              reqBuilder;
    @Autowired
    private SalsaDAOExceptionHandler                         exceptionHandler;

    @TraceLog
    public DAOResponse<List<ClassifyPartyResponseDTO>> classify(ClassifyPartyRequestDTO classifyPartyRequestDTO) {
        try {
            ClassifyInvolvedPartyResponse response = classifyservice
                    .classifyInvolvedParty(reqBuilder.build(classifyPartyRequestDTO));
            DAOError error = validateResponse(response);
            if (error == null) {
                return withResult(populateClassifyInvolvedPartyResponse(response));
            } else {
                return withError(error);
            }
        } catch (Exception e) {
            DAOError daoError = exceptionHandler.handleException(e, CLASS_NAME, METHOD_NAME, classifyPartyRequestDTO);
            return withError(daoError);
        }

    }

    private DAOError validateResponse(ClassifyInvolvedPartyResponse response) {
        if (response != null && response.getClassifications() != null && response.getClassifications().length > 0) {
            return null;
        } else {
            DAOError error = new DAOError(BUSSINESS_ERROR, "Classifications can not be null");
            logger.logError(error.getErrorCode(), error.getErrorMessage(), this.getClass());
            return error;
        }
    }

    /**
     * Method to populate the SALSA service response into a List of DTOs.
     *
     * @param aclassifyInvolvedPartyResponse
     * @return List<ClassifyPartyResponseDTO>
     */
    private List<ClassifyPartyResponseDTO> populateClassifyInvolvedPartyResponse(
            ClassifyInvolvedPartyResponse aclassifyInvolvedPartyResponse) {
        Classifications[] classificationValues = aclassifyInvolvedPartyResponse.getClassifications();
        Map<String, ClassifyPartyResponseDTO> fatcaMap = null;
        Map<String, ClassifyPartyResponseDTO> crsMap = null;
        for (Classifications classificationValue : classificationValues) {
            if (classificationValue != null && classificationValue.getExceptionDetails() != null) {
                if (FATCA_VALUE.equalsIgnoreCase(classificationValue.getSchemeName())) {

                    if (classificationValue.getExceptionDetails().getAssociatedCategories() != null) {
                        fatcaMap = getPartyAssociationCategoryDetailsForFATCAScheme(
                                classificationValue.getExceptionDetails().getAssociatedCategories());
                    }
                    continue;
                } else if (CRS_VALUE.equalsIgnoreCase(classificationValue.getSchemeName())) {

                    if (classificationValue.getExceptionDetails().getAssociatedCategories() != null) {
                        crsMap = getPartyAssociationCategoryDetailsForCRSScheme(
                                classificationValue.getExceptionDetails().getAssociatedCategories());
                    }
                    continue;
                } else {
                    // Scenario when any value is present other than FATCA &
                    // CRS.
                    DAOError error = new DAOError(BUSSINESS_ERROR, "Unexpected scheme value in service response");
                    logger.logError(error.getErrorCode(), error.getErrorMessage(), this.getClass());
                    continue;
                }
            }
        }
        // Scenario when there is no response for FATCA at all & hence return
        // only CRS values.
        return populateCRSMap(fatcaMap, crsMap);
    }

	/**
	 * @param fatcaMap
	 * @param crsMap
	 * @return
	 */
	private List<ClassifyPartyResponseDTO> populateCRSMap(
			Map<String, ClassifyPartyResponseDTO> fatcaMap,
			Map<String, ClassifyPartyResponseDTO> crsMap) {

		if ((fatcaMap == null || !(fatcaMap.size() > 0)) && crsMap != null && crsMap.size() > 0) {
            return (new ArrayList<ClassifyPartyResponseDTO>(crsMap.values()));
        } else if (crsMap != null && crsMap.size() > 0) {
            return populateCRSPrecedence(fatcaMap, crsMap);
        } else if ((crsMap == null || !(crsMap.size() > 0)) && fatcaMap != null && fatcaMap.size() > 0) {
            // scenario when there is no response for FATCA at all & hence
            // return
            // only CRS values.
            return (new ArrayList<ClassifyPartyResponseDTO>(fatcaMap.values()));
        } else {
            // Scenario when both FATCA & CRS response is not populated
            return new ArrayList<ClassifyPartyResponseDTO>();
        }
	}

	/**
	 * @param fatcaMap
	 * @param crsMap
	 * @return
	 */
	private List<ClassifyPartyResponseDTO> populateCRSPrecedence(
			Map<String, ClassifyPartyResponseDTO> fatcaMap,
			Map<String, ClassifyPartyResponseDTO> crsMap) {
		// Scenario when both FATCA & CRS response is populated. If any
		// country
		// is present in both FATCA & CRS, CRS is given precedence.
		for (Entry<String, ClassifyPartyResponseDTO> countryName : fatcaMap.entrySet()) {
		    crsMap.put(countryName.getKey(), countryName.getValue());
		}
		return new ArrayList<ClassifyPartyResponseDTO>(crsMap.values());
	}

    /**
     * Method to get the details of response for FATCA scheme.
     *
     * @param categories
     * @return Map<String, ClassifyPartyResponseDTO>
     */
    private Map<String, ClassifyPartyResponseDTO> getPartyAssociationCategoryDetailsForFATCAScheme(
            AssociatedCategories[] categories) {
        ClassifyPartyResponseDTO classifyPartyResponseDTO = new ClassifyPartyResponseDTO();
        Map<String, ClassifyPartyResponseDTO> map = new HashMap<String, ClassifyPartyResponseDTO>();
        if (categories != null) {
            for (AssociatedCategories aCategory : categories) {
                if (aCategory != null) {
                    String countryName = aCategory.getCountryName();
                    // As per the service implementation the country name
                    // returned for USA in FATCA scheme is "US TIN". Hence
                    // this logic is required here to make the country name as
                    // USA.
                    if (US_CATEGORY_NAME.equalsIgnoreCase(aCategory.getName())) {
                        String[] a = countryName.split(SPLIT_BY_SPACE);
                        a[0] = a[0].concat(A);
                        countryName = a[0];
                    }
                    classifyPartyResponseDTO.setCountryName(countryName);
                    if (REGEX.equalsIgnoreCase(aCategory.getCategoryName())) {
                        classifyPartyResponseDTO.setRegex(aCategory.getCategoryType());
                    }
                    classifyPartyResponseDTO.setTinRequired(false);
                    classifyPartyResponseDTO.setTaxResidencyType(findTaxResidencyTypeFromCode(FATCA_VALUE).toString());
                    map.put(countryName, classifyPartyResponseDTO);
                }
            }
        }
        return map;
    }

    /**
     * Method to get the details of response for CRS scheme.
     *
     * @param categories
     * @return Map<String, ClassifyPartyResponseDTO>
     */
    private Map<String, ClassifyPartyResponseDTO> getPartyAssociationCategoryDetailsForCRSScheme(
            AssociatedCategories[] categories) {
        ClassifyPartyResponseDTO classifyPartyResponseDTO = null;
        Map<String, ClassifyPartyResponseDTO> map = new HashMap<String, ClassifyPartyResponseDTO>();
        if (categories != null) {
            for (AssociatedCategories aCategory : categories) {
                if (aCategory != null) {
                    classifyPartyResponseDTO = new ClassifyPartyResponseDTO();
                    classifyPartyResponseDTO.setCountryName(aCategory.getCountryName());
                    if (REGEX.equalsIgnoreCase(aCategory.getCategoryName())) {
                        classifyPartyResponseDTO.setRegex(aCategory.getCategoryType());
                    }
                    if (TIN_REQD_CODE.equalsIgnoreCase(aCategory.getCategoryType())) {
                        classifyPartyResponseDTO.setTinRequired(true);
                    }
                    classifyPartyResponseDTO.setTaxResidencyType(findTaxResidencyTypeFromCode(CRS_VALUE).toString());
                    map.put(aCategory.getCountryName(), classifyPartyResponseDTO);
                }
            }
        }
        return map;
    }

    public void setReqBuilder(ClassifyInvolvedPartyRequestBuilder reqBuilder) {
        this.reqBuilder = reqBuilder;
    }

    public void setClassifyservice(IA_CommonReportingStandards classifyservice) {
        this.classifyservice = classifyservice;
    }
}
