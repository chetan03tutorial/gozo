package com.lbg.ib.api.sales.header.mapper;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lbg.ib.api.sales.dao.session.SessionManagementDAO;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.OperatorTypeEnum;
import com.lbg.ib.api.sales.soapapis.commonapi.schema.infrastructure.soap.ContactPoint;
import com.lbg.ib.api.shared.service.configuration.ConfigurationDAO;
import com.lbg.ib.api.shared.service.reference.ReferenceDataServiceDAO;
import com.lbg.ib.api.sso.domain.user.UserContext;

@Component
public class DigitalHeaderMapper extends AbstractLBGHeaderMapper {

    @Autowired
    private SessionManagementDAO sessionManager;
    @Autowired
    private ConfigurationDAO configDAO;
    @Autowired
    private ReferenceDataServiceDAO referenceDataService;

    public ContactPoint prepareContactPointHeader() {

        UserContext userContext = sessionManager.getUserContext();
        String channelId = userContext.getChannelId();

        String contactPointID = referenceDataService.getReferenceDataItemValue(CONTACT_POINT_PORTFOLIO,
                CONTACT_POINT_ID);
        String contactPointType = referenceDataService.getReferenceDataItemValue(CONTACT_POINT_PORTFOLIO,
                CONTACT_POINT_TYPE);
        if (null == contactPointID) {
            contactPointID = (String) configDAO.getConfigurationValue(CONTACT_POINT_ID, channelId);
        }
        if (null == contactPointType) {
            contactPointType = (String) configDAO.getConfigurationValue(CONTACT_POINT_DETAILS, CONTACT_POINT_TYPE);
        }
        Map<String, Object> contactPointMap = configDAO.getConfigurationItems(CONTACT_POINT_HEADER);
        ContactPoint contactPointHeader = new ContactPoint();
        contactPointHeader.setContactPointType(contactPointType);
        contactPointHeader.setContactPointId(contactPointID);
        contactPointHeader.setApplicationId((String) contactPointMap.get(APPLICATION_ID));
        contactPointHeader.setInitialOriginatorType((String) contactPointMap.get(INITIAL_ORIGINATOR_TYPE));
        contactPointHeader.setInitialOriginatorId("10.245.224.125");
        contactPointHeader.setOperatorType(OperatorTypeEnum.Customer);
        contactPointHeader.setMustReturn(false);
        return contactPointHeader;

    }

}
