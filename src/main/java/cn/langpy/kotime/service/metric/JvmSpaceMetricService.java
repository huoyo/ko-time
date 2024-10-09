package cn.langpy.kotime.service.metric;

import cn.langpy.kotime.model.JvmMemoryInfo;
import cn.langpy.kotime.service.core.SystemService;


import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * zhangchang
 */
public class JvmSpaceMetricService extends SystemService {
    Logger log = Logger.getLogger(JvmSpaceMetricService.class.toString());

    public JvmMemoryInfo getEdenSpaceInfo() {
        return getSpaceInfo("PS Eden Space");
    }

    public JvmMemoryInfo getSurvivorSpaceInfo() {
        return getSpaceInfo("PS Survivor Space");
    }

    public JvmMemoryInfo getOldGenInfo() {
        return getSpaceInfo("PS Old Gen");
    }

    public JvmMemoryInfo getMetaspaceInfo() {
        return getSpaceInfo("Metaspace");
    }

    private JvmMemoryInfo getSpaceInfo(String name) {
        List<MemoryPoolMXBean> memoryPoolMXBeans = getMemoryPoolMXBeans();
        Map<String, MemoryPoolMXBean> mxBeanMap = memoryPoolMXBeans.stream().collect(Collectors.toMap(v -> v.getName(), v -> v));
        MemoryPoolMXBean psEdenSpace = mxBeanMap.get(name);
        JvmMemoryInfo heapMemoryInfo = new JvmMemoryInfo();
        if (psEdenSpace == null) {
            heapMemoryInfo.setInitValue(0L);
            heapMemoryInfo.setMaxValue(0L);
            heapMemoryInfo.setUsedValue(0L);
            heapMemoryInfo.setUsedRate(0.0);
            return heapMemoryInfo;
        }
        MemoryUsage usage = psEdenSpace.getUsage();
        heapMemoryInfo.setInitValue(usage.getInit());
        heapMemoryInfo.setMaxValue(usage.getMax());
        heapMemoryInfo.setUsedValue(usage.getUsed());
        heapMemoryInfo.setUsedRate(usage.getUsed() * 1.0 / usage.getMax());
        return heapMemoryInfo;
    }


}
