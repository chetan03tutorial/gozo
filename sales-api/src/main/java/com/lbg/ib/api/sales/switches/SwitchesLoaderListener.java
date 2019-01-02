package com.lbg.ib.api.sales.switches;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.lbg.ib.api.sales.common.SpringContextHolder;
import com.lbg.ib.api.shared.util.logger.TraceLog;
import com.lbg.ib.api.sales.switches.service.SwitchesManagementService;

/**
 * @author 1141431
 *
 *         SwitchesLoaderListener will load the switch status for the ones
 *         configured in configApplicationSwitches, by calling B500.
 *
 *         On failing, this will stop the application to get started.
 *
 */
public class SwitchesLoaderListener implements ServletContextListener {

    @TraceLog
    public void contextInitialized(ServletContextEvent aServletContextEvent) {
        loadApplicationSwitches();

    }

    /**
     * This method will load the switches which has been configured under
     * configApplicationSwitches.xml into the EA component
     * GlobalApplicationSwitches.
     *
     * Any switches which doesn't have an entry in the config file will be
     * failed to load.
     *
     */

    @TraceLog
    private void loadApplicationSwitches() {
        SwitchesManagementService switchesService = SpringContextHolder.getBean(SwitchesManagementService.class);
        switchesService.loadSwitches();
    }

    public void contextDestroyed(ServletContextEvent aServletContextEvent) {
        // To be implemented
    }

}
