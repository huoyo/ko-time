package cn.langpy.kotime.service;

import com.sun.management.HotSpotDiagnosticMXBean;

import javax.management.MBeanServer;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class HeapDumpService {
    private static final String STANDARD_DUMP_NAME = "kotime-heapdump-%s.hprof";
    private static Logger log = Logger.getLogger(HeapDumpService.class.toString());
    private static final String HotSpotDiagnosticName = "com.sun.management:type=HotSpotDiagnostic";

    public static HeapDumpService newInstance() {
        return new HeapDumpService();
    }

    public String getHeapDumpFile(boolean live) {
        String targetFile = System.getProperty("java.io.tmpdir")+File.separator+getHeapDumpFileName(live);
        try {
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            HotSpotDiagnosticMXBean hotSpotDiagnostic = ManagementFactory.newPlatformMXBeanProxy(server, HotSpotDiagnosticName, HotSpotDiagnosticMXBean.class);
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
