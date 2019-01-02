/**********************************************************************
 * This source code is the property of Lloyds TSB Group PLC.
 *
 * All Rights Reserved.
 *
 * Class Name: DeviceSniffingLoader
 *
 * Author(s): ct100704
 *
 * Date: 18 Feb 2014
 *
 ***********************************************************************/
package com.lbg.ib.api.sales.referencedata;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.lloydstsb.ea.config.parser.ConfigurationParser;
import com.lloydstsb.ea.exceptions.runtime.ApplicationRuntimeException;
import com.lloydstsb.ea.logging.constants.StateEventSubType;
import com.lloydstsb.ea.logging.event.StateEvent;
import com.lloydstsb.ea.logging.factory.LoggingServiceFactory;
import com.lloydstsb.ea.logging.interfaces.LoggingService;
import com.lloydstsb.ea.referencedata.ClassificationAdapter;
import com.lloydstsb.ea.referencedata.DeviceSniffingReferenceDataItem;
import com.lloydstsb.ea.referencedata.ReferenceDataGroups;
import com.lloydstsb.ea.referencedata.ReferenceDataItem;
import com.lloydstsb.ea.referencedata.UIClassificationLoaderBase;
import com.lloydstsb.ea.referencedata.helpers.ErrorMessages;
import com.lloydstsb.ea.referencedata.helpers.LookupTypes;
import com.lloydstsb.ea.referencedata.helpers.ReferenceDataConfigurationHelper;

/**
 * <code>DeviceSniffingLoader</code> extends
 * <code>UIClassificationLoaderBase</code>
 *
 * @author ct100696
 * @version 0.1
 *
 */
public class DeviceSniffingLoader extends UIClassificationLoaderBase {
    
    /**
     * Log4j initialisation - Existing EA component.
     */
    private static final LoggingService LOGGER           = LoggingServiceFactory.getLoggingService();
    
    /**
     * EventId to be used in state event.
     */
    private static final String         EVENT_ID         = "SW10888";                                                //$NON-NLS-1$
    
    /**
     * CLASSNAME used to administer Reference data.
     */
    private static final String         CLASSNAME        = DeviceSniffingLoader.class.getName();
    
    /**
     * Config section.
     */
    private static final String         DATABASE_SECTION = "DeviceSniffingLoader";
    
    /**
     * initialise Configuration helper - to retrieve XML Configuration file.
     */
    ConfigurationParser                 configService    = ReferenceDataConfigurationHelper.getConfigurationParser();
    
    /**
     * Group section.
     */
    private String                      groupCfgSection;
    
    /**
     * Constructor for the DeviceSniffingLoader.
     *
     * @param aLoaderCfgSection
     *            read from XML Configuration property file.
     */
    public DeviceSniffingLoader(String aLoaderCfgSection) {
        super(aLoaderCfgSection);
    }
    
    /**
     * Constructor for the DeviceSniffingLoader.
     *
     * @param aLoaderCfgSection
     *            read from XML Configuration property file.
     * @param aGroupCfgSection
     *            read from XML Configuration property file.
     */
    public DeviceSniffingLoader(String aLoaderCfgSection, String aGroupCfgSection) {
        super(aLoaderCfgSection);
        this.groupCfgSection = aGroupCfgSection;
    }
    
    /**
     * This is the root method overriden from the super class, this provides the
     * interface and loading mechanism of reference data from a RDMS.
     *
     * throws a Runtime exception.
     *
     * @param aClassification
     *            object
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void load(ClassificationAdapter aClassification) {
        
        /*
         * Channel Name.
         */
        String channelName = aClassification.getChannel();

        eventBasedLogging(channelName);

        /**
         * retrieve JNDI resource String from XML Configuration file. This is
         * used with a DataSource from the server pool of Connections.
         */
        String dataSourceJNDI = this.configService.getConfigurationValueAsString(getLoaderCfgSection(),
                LookupTypes.DataSourceJNDI.toString());
        
        /**
         * load Reference data.
         */
        ReferenceDataGroups referenceDataGroups = null;
        Connection conn = null;
        ResultSet resultSet = null;
        try {
            
            // get the Database connection
            conn = getConnection(dataSourceJNDI);
            
            if (LOGGER.isDebugEnabled()) {
                LOGGER.logDebug(CLASSNAME, " Gets database connection%s", dataSourceJNDI);
            }
            
            // load the data from DB
            resultSet = loadData(conn, DATABASE_SECTION, channelName);
            
            if (LOGGER.isDebugEnabled()) {
                LOGGER.logDebug(CLASSNAME, " Loads data from database%s", resultSet);
            }
            
            // Create Reference Data groups
            referenceDataGroups = buildReferenceDataGroup(resultSet);
            
            if (LOGGER.isDebugEnabled()) {
                LOGGER.logDebug(CLASSNAME, " Loads data from database:%s", referenceDataGroups);
            }
            
        } catch (ApplicationRuntimeException anException) {
            throw anException;
        } catch (Exception anException) {
            String errorKey = ErrorMessages.UNABLE_TO_RETRIEVE_DATA_EXCEPTION.toString();
            String errorMessage = "Unable to load data for " + CLASSNAME;
            throw logException(anException, errorKey, errorMessage);
            
        } finally {
            
            try {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.logDebug(CLASSNAME, " closes connection");
                }
                // clean the resources
                cleanJDBCResources(resultSet, null, conn);
            } catch (ApplicationRuntimeException anApplicationRuntimeException) {
                LOGGER.logException(anApplicationRuntimeException);
            }
        }
        
        // place retrieved Reference data in classification.
        aClassification.put(referenceDataGroups);
        return;
    }

    //SONAR fix
    private void eventBasedLogging(String channelName){
        if (LOGGER.isComponentStateLogEnabled(EVENT_ID)) {
            StateEvent stateEvent = new StateEvent();
            stateEvent.setClassName(CLASSNAME);
            stateEvent.setMessage("STARTUP:  Loading of  reference data started " + //$NON-NLS-1$
                    "in DeviceSniffingLoader for channel: " + channelName); // $NON-NLS-1$
            stateEvent.setSubType(StateEventSubType.STARTUP);
            stateEvent.setEventId(EVENT_ID);
            LOGGER.logComponentState(stateEvent);
        }
    }

    /**
     * This helper method iterates over the SQL Result set and builds the
     * ReferenceDataGroups type.
     *
     *
     * @param aResultSet
     *            Object
     * @return ReferenceDataGroups Object
     * @throws SQLException
     *
     *
     * @see com.lloydstsb.ea.referencedata.helpers.ReferenceDataTableMetaData
     * @see com.lloydstsb.ea.referencedata.ReferenceDataItem
     */
    private ReferenceDataGroups buildReferenceDataGroup(final ResultSet aResultSet) throws SQLException {
        final String groupCode = getGroupName(this.groupCfgSection);
        ReferenceDataGroups groups = new ReferenceDataGroups();
        int groupSize = 0;
        
        /**
         * Iterate through the ResultSet
         */
        
        try {
            while (aResultSet.next()) {
                
                DeviceSniffingReferenceDataItem deviceSniff = new DeviceSniffingReferenceDataItem();
                deviceSniff.setDeviceId(aResultSet.getString("ID"));
                deviceSniff.setAction(aResultSet.getString("ACTION"));
                deviceSniff.setChannelCode(aResultSet.getString("CHN_CODE"));
                deviceSniff.setDagId(aResultSet.getString("DAG_ID"));
                deviceSniff.setProductDescription(aResultSet.getString("PROD_DESC"));
                deviceSniff.setProductUrl(aResultSet.getString("PROD_URL"));
                deviceSniff.setStackCode(aResultSet.getString("STA_CODE"));
                deviceSniff.setDAGName(aResultSet.getString("DAG_NAME"));
                
                StringBuilder itemCode = new StringBuilder("");
                itemCode.append(deviceSniff.getDeviceId());
                itemCode.append("_");
                itemCode.append(deviceSniff.getProductUrl());
                itemCode.append("_");
                itemCode.append(deviceSniff.getChannelCode());
                
                /**
                 * creates a ReferenceDataItem and set values from table.
                 */
                ReferenceDataItem dataItem = new ReferenceDataItem(itemCode.toString(), "", Boolean.TRUE,
                        Integer.valueOf(0), deviceSniff, getLocale());
                
                groups.add(groupCode, dataItem);
                
            }
            
        } catch (SQLException aSQLException) {
            String errorKey = ErrorMessages.UNABLE_TO_RETRIEVE_DATA_FROM_RESULTSET_EXCEPTION.toString();
            String errorMessage = "Unable to load data for " + CLASSNAME;
            throw logException(aSQLException, errorKey, errorMessage);
        }
        
        if (LOGGER.isComponentStateLogEnabled(EVENT_ID)) {
            groupSize = getGroupSize(groups, groupCode, getLocale());
            StateEvent stateEvent = new StateEvent();
            stateEvent.setClassName(CLASSNAME);
            stateEvent.setMessage("INITIALIZED:  Group " + groupCode + " having " + groupSize + " data items"
                    + " loaded successfully"); //$NON-NLS-1$
            stateEvent.setSubType(StateEventSubType.INITIALIZE);
            stateEvent.setEventId(EVENT_ID);
            LOGGER.logComponentState(stateEvent);
        }
        return (groups);
    }
    
}
