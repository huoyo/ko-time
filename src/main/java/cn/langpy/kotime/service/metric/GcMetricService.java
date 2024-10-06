package cn.langpy.kotime.service.metric;

import cn.langpy.kotime.model.GcUsage;
import cn.langpy.kotime.service.core.SystemService;

import java.lang.management.GarbageCollectorMXBean;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class GcMetricService extends SystemService {
    private final static List<String> youngMemoryPool = Arrays.asList("PS Eden Space", "PS Survivor Space");
    private final static List<String> oldMemoryPool = Arrays.asList("PS Old Gen", "Metaspace");
    Logger log = Logger.getLogger(GcMetricService.class.toString());

    public GcUsage getGcUsage() {
        GcUsage gcUsage = new GcUsage();
        gcUsage.setFullNum(0L);
        gcUsage.setMinorNum(0L);
        gcUsage.setFullCostTime(0L);
        gcUsage.setMinorCostTime(0L);
        List<GarbageCollectorMXBean> garbageCollectorMXBeans = getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean bean : garbageCollectorMXBeans) {
            if (isFullGc(bean)) {
                gcUsage.setFullNum(gcUsage.getFullNum() + bean.getCollectionCount());
                gcUsage.setFullCostTime(gcUsage.getFullCostTime() + bean.getCollectionTime());
            } else if (isMinorGc(bean)) {
                gcUsage.setMinorNum(gcUsage.getMinorNum() + bean.getCollectionCount());
                gcUsage.setMinorCostTime(gcUsage.getMinorCostTime() + bean.getCollectionTime());
            } else {
                log.warning("kotime=>Can not recognize th garbage collector: " + bean);
            }
        }
        gcUsage.setTotalNum(gcUsage.getFullNum() + gcUsage.getMinorNum());
        return gcUsage;
    }

    private boolean isMinorGc(GarbageCollectorMXBean bean) {
        String[] memoryPoolNames = bean.getMemoryPoolNames();
        boolean isMinor = false;
        boolean isMajor = false;
        for (String memoryPoolName : memoryPoolNames) {
            if (youngMemoryPool.contains(memoryPoolName)) {
                isMinor = true;
            }
            if (oldMemoryPool.contains(memoryPoolName)) {
                isMajor = true;
            }
        }
        return isMinor && !isMajor;
    }

    private boolean isFullGc(GarbageCollectorMXBean bean) {
        String[] memoryPoolNames = bean.getMemoryPoolNames();
        boolean isMajor = false;
        for (String memoryPoolName : memoryPoolNames) {
            if (oldMemoryPool.contains(memoryPoolName)) {
                isMajor = true;
            }
        }
        return isMajor;
    }

}
