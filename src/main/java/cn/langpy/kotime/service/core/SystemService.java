package cn.langpy.kotime.service.core;


import com.sun.management.HotSpotDiagnosticMXBean;

import javax.management.MBeanServer;
import java.io.IOException;
import java.lang.management.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * zhangchang
 */
public class SystemService {
    Logger log = Logger.getLogger(SystemService.class.toString());
    private static final String HotSpotDiagnosticName = "com.sun.management:type=HotSpotDiagnostic";

    final static Map<String, SystemService> serviceMap = new ConcurrentHashMap<>();

    public static <T extends SystemService> T getInstance(Class<? extends SystemService> clazz) {
        String simpleName = clazz.getSimpleName();
        T systemService = (T) serviceMap.get(simpleName);
        if (systemService == null) {
            synchronized (SystemService.class) {
                if (!serviceMap.containsKey(simpleName)) {
                    try {
                        systemService = (T) clazz.newInstance();
                        serviceMap.put(simpleName, systemService);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    systemService = (T) serviceMap.get(simpleName);
                }
            }
        }
        return systemService;
    }

    protected MemoryUsage getHeapMemoryUsage() {
        return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
    }

    protected MemoryUsage getNonHeapMemoryUsage() {
        return ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
    }

    protected List<GarbageCollectorMXBean> getGarbageCollectorMXBeans() {
        return ManagementFactory.getGarbageCollectorMXBeans();
    }

    protected List<MemoryPoolMXBean> getMemoryPoolMXBeans() {
        return ManagementFactory.getMemoryPoolMXBeans();
    }

    protected ThreadMXBean getThreadMXBean() {
        return ManagementFactory.getThreadMXBean();
    }

    protected ClassLoadingMXBean getClassLoadingMXBean() {
        return ManagementFactory.getClassLoadingMXBean();
    }

    public HotSpotDiagnosticMXBean getHotSpotDiagnosticMXBean() {
        HotSpotDiagnosticMXBean hotSpotDiagnostic = null;
        try {
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            hotSpotDiagnostic = ManagementFactory.newPlatformMXBeanProxy(server, HotSpotDiagnosticName, HotSpotDiagnosticMXBean.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hotSpotDiagnostic;
    }
}
