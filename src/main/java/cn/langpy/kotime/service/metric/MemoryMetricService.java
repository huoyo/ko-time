package cn.langpy.kotime.service.metric;

import cn.langpy.kotime.model.JvmMemoryInfo;
import cn.langpy.kotime.model.PhysicalMemoryInfo;
import cn.langpy.kotime.service.core.SystemService;
import cn.langpy.kotime.util.Context;
import com.sun.management.HotSpotDiagnosticMXBean;
import com.sun.management.OperatingSystemMXBean;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class MemoryMetricService extends SystemService {
    Logger log = Logger.getLogger(MemoryMetricService.class.toString());
    private static final String STANDARD_DUMP_NAME = "kotime-heapdump-%s.hprof";

    public JvmMemoryInfo getHeapMemoryInfo() {
        MemoryUsage memoryUsage = getHeapMemoryUsage();
        long initTotalMemorySize = memoryUsage.getInit();
        long maxMemorySize = memoryUsage.getMax();
        long usedMemorySize = memoryUsage.getUsed();
        JvmMemoryInfo heapMemoryInfo = new JvmMemoryInfo();
        heapMemoryInfo.setInitValue(initTotalMemorySize);
        heapMemoryInfo.setMaxValue(maxMemorySize);
        heapMemoryInfo.setUsedValue(usedMemorySize);
        heapMemoryInfo.setUsedRate(usedMemorySize * 1.0 / maxMemorySize);
        return heapMemoryInfo;
    }

    public JvmMemoryInfo getNonHeapMemoryInfo() {
        MemoryUsage memoryUsage = getNonHeapMemoryUsage();
        long initTotalMemorySize = memoryUsage.getInit();
        long maxMemorySize = memoryUsage.getMax();
        long usedMemorySize = memoryUsage.getUsed();
        JvmMemoryInfo heapMemoryInfo = new JvmMemoryInfo();
        heapMemoryInfo.setInitValue(initTotalMemorySize);
        heapMemoryInfo.setMaxValue(maxMemorySize);
        heapMemoryInfo.setUsedValue(usedMemorySize);
        heapMemoryInfo.setUsedRate(usedMemorySize * 1.0 / maxMemorySize);
        return heapMemoryInfo;
    }

    public PhysicalMemoryInfo getPhysicalMemoryInfo() {
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        PhysicalMemoryInfo physicalMemoryInfo = new PhysicalMemoryInfo();
        physicalMemoryInfo.setInitValue(osmxb.getTotalPhysicalMemorySize());
        physicalMemoryInfo.setUsedValue(osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize());
        physicalMemoryInfo.setFreeValue(osmxb.getFreePhysicalMemorySize());
        physicalMemoryInfo.setUsedValue(physicalMemoryInfo.getInitValue() - physicalMemoryInfo.getFreeValue());
        physicalMemoryInfo.setUsedRate(physicalMemoryInfo.getUsedValue() * 1.0 / physicalMemoryInfo.getInitValue());
        if (isLinux()) {
            Map<String, String> processInfo = getProcessInfo();
            if (processInfo.containsKey("VmSize")) {
                String VmRSSStr = processInfo.get("VmRSS");
                String VmSizeStr = VmRSSStr.split(" ")[0].trim();
                long VmRSS = Long.valueOf(VmSizeStr);
                physicalMemoryInfo.setThisUsedValue(VmRSS * 1024);
                double rate = physicalMemoryInfo.getThisUsedValue() * 1.0 / physicalMemoryInfo.getInitValue();
                physicalMemoryInfo.setThisUsedRate(rate);
            }
        }
        return physicalMemoryInfo;
    }

    public boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    public Map<String, String> getProcessInfo() {
        Map<String, String> processMetrics = new HashMap();
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec("cat /proc/" + Context.getPid() + "/status");
        } catch (IOException e) {
            log.severe("Can not execute '" + "cat /proc/" + Context.getPid() + "/status" + "'");
            return processMetrics;
        }
        try (InputStream inputStream = process.getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
        ) {
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                String[] split = line.split(":");
                if (split.length == 2) {
                    String key = split[0].trim();
                    String value = split[1].trim();
                    processMetrics.put(key, value);
                }
            }
        } catch (Exception e) {
            log.severe("Can not read the result of '" + "cat /proc/" + Context.getPid() + "/status" + "'");
        }
        return processMetrics;
    }

    public String getHeapDumpFile(boolean live) {
        String targetFile = System.getProperty("java.io.tmpdir") + File.separator + getHeapDumpFileName(live);
        try {
            HotSpotDiagnosticMXBean hotSpotDiagnostic = getHotSpotDiagnosticMXBean();
            if (Files.exists(Paths.get(targetFile))) {
                new File(targetFile).delete();
            }
            hotSpotDiagnostic.dumpHeap(targetFile, live);
        } catch (IOException e) {
            e.printStackTrace();
            log.severe("Can not dump heap file!");
        }
        return targetFile;
    }

    public String getHeapDumpFileName(boolean live) {
        if (live) {
            return String.format(STANDARD_DUMP_NAME, "live");
        }
        return String.format(STANDARD_DUMP_NAME, "all");
    }


}
