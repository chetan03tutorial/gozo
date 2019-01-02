package com.lbg.ib.api.sales.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.lbg.ib.api.sales.sortcode.service.SortCodeCacheService;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;

@Configuration
@EnableScheduling
public class SortCodeCache {

    @Autowired
    SortCodeCacheService sortCodeCacheService;

    @Autowired
    private LoggerDAO logger;

    // Run Once in 24 hours time in milliseconds
    @Scheduled(fixedRate = 86400000)
    public void writeSortCodeCache() {
        logger.traceLog(this.getClass(), ":::Running Sort Code Cache Scheduler at:::" + System.currentTimeMillis());
        sortCodeCacheService.sortCodeLoader();

    }

}
