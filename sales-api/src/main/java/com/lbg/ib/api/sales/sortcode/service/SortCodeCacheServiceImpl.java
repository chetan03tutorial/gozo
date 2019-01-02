package com.lbg.ib.api.sales.sortcode.service;

import com.lbg.ib.api.sales.content.service.ContentService;
import com.lbg.ib.api.sales.sortcode.resource.PropositionIndex;
import com.lbg.ib.api.sales.sortcode.resource.SortCodeMapper;
import com.lbg.ib.api.sales.sortcode.resource.SortCodeMapperBeanWrapper;
import com.lbg.ib.api.sales.sortcode.resource.SortCodes;
import com.lbg.ib.api.shared.util.logger.LoggerDAO;
import com.lloydstsb.ea.enums.BrandValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class SortCodeCacheServiceImpl implements SortCodeCacheService {

    @Autowired
    private LoggerDAO logger;

    private static volatile Map<String, SortCodeMapper> lloydsSortCodemaper;
    private static volatile Map<String, SortCodeMapper> halifaxSortCodemaper;
    private static volatile Map<String, SortCodeMapper> bosSortCodemaper;

    private static volatile List<String> lloydsSortCodemaperDefault;
    private static volatile List<String> halifaxSortCodemaperDefault;
    private static volatile List<String> bosSortCodemaperDefault;

    private static final ReadWriteLock readWriteLockLLoyds = new ReentrantReadWriteLock();
    private static final Lock readLockLoyds = readWriteLockLLoyds.readLock();
    private static final Lock writeLockLoyds = readWriteLockLLoyds.writeLock();

    private static final ReadWriteLock readWriteLockHalifax = new ReentrantReadWriteLock();
    private static final Lock readLockHalifax = readWriteLockHalifax.readLock();
    private static final Lock writeLockHalifax = readWriteLockHalifax.writeLock();

    private static final ReadWriteLock readWriteLockLBos = new ReentrantReadWriteLock();
    private static final Lock readLockBos = readWriteLockLBos.readLock();
    private static final Lock writeLockBos = readWriteLockLBos.writeLock();

    private static String lloodysDefaultTown;
    private static String halifaxDefaultTown;
    private static String bosDefaultTown;

    private static final String SORT_CODE_INDEX = "index-sortcode.json";

    @Autowired
    private ContentService service;

    public static List<String> getLloydsSortCodemaperDefault() {
        readLockLoyds.lock();
        try {
            return lloydsSortCodemaperDefault;
        } finally {
            readLockLoyds.unlock();
        }
    }

    public static List<String> getHalifaxSortCodemaperDefault() {
        readLockHalifax.lock();
        try {
            return halifaxSortCodemaperDefault;
        } finally {
            readLockHalifax.unlock();
        }
    }

    public static List<String> getBosSortCodemaperDefault() {
        readLockBos.lock();
        try {
            return bosSortCodemaperDefault;
        } finally {
            readLockBos.unlock();
        }
    }

    public static Map<String, SortCodeMapper> getLloydsSortCodemaper() {
        readLockLoyds.lock();
        try {
            return lloydsSortCodemaper;
        } finally {
            readLockLoyds.unlock();
        }

    }

    public static Map<String, SortCodeMapper> getHalifaxSortCodemaper() {
        readLockHalifax.lock();
        try {
            return halifaxSortCodemaper;
        } finally {
            readLockHalifax.unlock();
        }
    }

    public static Map<String, SortCodeMapper> getBosSortCodemaper() {
        readLockBos.lock();
        try {
            return bosSortCodemaper;
        } finally {
            readLockBos.unlock();
        }
    }

    private void setLloydsSortCodemaper(String filePath) {
        writeLockLoyds.lock();
        try {
            SortCodeMapperBeanWrapper sortCodeMaperBeanWraper = getSortCodeMapperBeanWrapper(filePath);
            if (sortCodeMaperBeanWraper != null) {
                lloydsSortCodemaper = loadSortCodes(sortCodeMaperBeanWraper.getSortCodeMaper());
                lloodysDefaultTown = sortCodeMaperBeanWraper.getDefaultTown();
            }
            lloydsSortCodemaperDefault = fillDefaultSortCodes(lloodysDefaultTown, lloydsSortCodemaper);
        } catch (Exception x) {
            logger.logException(this.getClass(), x);
        } finally {
            writeLockLoyds.unlock();
        }
    }

    private void setHalifaxSortCodemaper(String filePath) {
        writeLockHalifax.lock();
        try {
            SortCodeMapperBeanWrapper sortCodeMaperBeanWraper = getSortCodeMapperBeanWrapper(filePath);
            if (sortCodeMaperBeanWraper != null) {
                halifaxSortCodemaper = loadSortCodes(sortCodeMaperBeanWraper.getSortCodeMaper());
                halifaxDefaultTown = sortCodeMaperBeanWraper.getDefaultTown();
            }
            halifaxSortCodemaperDefault = fillDefaultSortCodes(halifaxDefaultTown, halifaxSortCodemaper);
        } catch (Exception x) {
            logger.logException(this.getClass(), x);
        } finally {
            writeLockHalifax.unlock();
        }
    }

    private void setBosSortCodemaper(String filePath) {
        writeLockBos.lock();
        try {
            SortCodeMapperBeanWrapper sortCodeMaperBeanWraper = getSortCodeMapperBeanWrapper(filePath);
            if (sortCodeMaperBeanWraper != null) {
                bosSortCodemaper = loadSortCodes(sortCodeMaperBeanWraper.getSortCodeMaper());
                bosDefaultTown = sortCodeMaperBeanWraper.getDefaultTown();
            }
            bosSortCodemaperDefault = fillDefaultSortCodes(bosDefaultTown, bosSortCodemaper);
        } catch (Exception x) {
            logger.logException(this.getClass(), x);
        } finally {
            writeLockBos.unlock();
        }
    }

    public void sortCodeLoader() {
        PropositionIndex propositionIndex = getPropositionIndex(SORT_CODE_INDEX);
        if (propositionIndex != null) {
            List<SortCodes> sortCodes = propositionIndex.getSortCodes();
            if (sortCodes != null) {
                for (SortCodes sortCodeUrl : sortCodes) {
                    if ((BrandValue.LLOYDS.getBrand()).equalsIgnoreCase(sortCodeUrl.getBrand())) {
                        setLloydsSortCodemaper(sortCodeUrl.getUrl());
                    }
                    if ((BrandValue.HALIFAX.getBrand()).equalsIgnoreCase(sortCodeUrl.getBrand())) {
                        setHalifaxSortCodemaper(sortCodeUrl.getUrl());
                    }
                    if ((BrandValue.BOS.getBrand()).equalsIgnoreCase(sortCodeUrl.getBrand())) {
                        setBosSortCodemaper(sortCodeUrl.getUrl());
                    }
                }
            }
        }
    }

    private Map<String, SortCodeMapper> loadSortCodes(List<SortCodeMapper> sortCodeMaperList) {
        Map<String, SortCodeMapper> sortCodeMaperMap = new HashMap<String, SortCodeMapper>();
        for (SortCodeMapper sortCodeMaper : sortCodeMaperList) {
            sortCodeMaperMap.put(sortCodeMaper.getAddress(), sortCodeMaper);
        }
        return sortCodeMaperMap;
    }

    private List<String> fillDefaultSortCodes(String brnchName, Map<String, SortCodeMapper> sortCodeMaper) {
        List<String> defaultSortCodes = new ArrayList<String>();
        if (sortCodeMaper != null) {
            for (Map.Entry<String, SortCodeMapper> entry : sortCodeMaper.entrySet()) {
                if (entry.getKey().toLowerCase().contains(brnchName.toLowerCase())) {
                    SortCodeMapper sortCodeMaperBean = entry.getValue();
                    defaultSortCodes.add(sortCodeMaperBean.getSortCode());
                }
            }
        }
        return defaultSortCodes;
    }

    private PropositionIndex getPropositionIndex(String path) {
        logger.traceLog(this.getClass(), ":::Going to fetch index file from:::" + path);
        PropositionIndex propositionIndex = null;
        try {
            propositionIndex = service.genericContent(path, PropositionIndex.class);
        } catch (Exception x) {
            logger.logException(this.getClass(), x);
        }
        return propositionIndex;
    }

    private SortCodeMapperBeanWrapper getSortCodeMapperBeanWrapper(String path) {
        logger.traceLog(this.getClass(), ":::Going to fetch Sort code Map file from:::" + path);
        SortCodeMapperBeanWrapper sortCodeMapperBeanWrapper = null;
        try {
            sortCodeMapperBeanWrapper = service.genericContent(path, SortCodeMapperBeanWrapper.class);
        } catch (Exception x) {
            logger.logException(this.getClass(), x);
        }
        return sortCodeMapperBeanWrapper;
    }

}
