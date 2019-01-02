package com.lbg.ib.api.sales.header.mapper;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.OperatorTypeEnum;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ContactPoint;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.sso.domain.mca.BranchContext;

@Component
public class BranchHeaderMapper extends AbstractLBGHeaderMapper {

    @Autowired
    private SessionManagementDAO session;

    @Autowired
    private ConfigurationDAO configDAO;

    public ContactPoint prepareContactPointHeader() {
        BranchContext branchContext = session.getBranchContext();
        String contactPointType = (String) configDAO.getConfigurationValue(CONTACT_POINT_DETAILS, CONTACT_POINT_TYPE);
        Map<String, Object> contactPointMap = configDAO.getConfigurationItems(CONTACT_POINT_HEADER);
        ContactPoint contactPointHeader = new ContactPoint();
        contactPointHeader.setContactPointType(contactPointType);
        contactPointHeader.setContactPointId("0000" + branchContext.getOriginatingSortCode());
        contactPointHeader.setApplicationId("Digital Branch");
        contactPointHeader.setInitialOriginatorType((String) contactPointMap.get(INITIAL_ORIGINATOR_TYPE));
        contactPointHeader.setInitialOriginatorId(branchContext.getColleagueId());
        contactPointHeader.setOperatorType(OperatorTypeEnum.Staff);
        contactPointHeader.setMustReturn(false);
        return contactPointHeader;
    }

}
