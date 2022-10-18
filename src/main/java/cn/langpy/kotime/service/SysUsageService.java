package cn.langpy.kotime.service;

import cn.langpy.kotime.model.CpuInfo;
import cn.langpy.kotime.model.HeapMemoryInfo;
import cn.langpy.kotime.model.PhysicalMemoryInfo;
import com.sun.management.OperatingSystemMXBean;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.concurrent.TimeUnit;

public class SysUsageService {

    public static SysUsageService newInstance() {
        return new SysUsageService();
    }

    public CpuInfo getCpuInfo() {
        SystemInfo systemInfo = new SystemInfo();
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long[] ticks =     processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()]
                - prevTicks[CentralProcessor.TickType.NICE.getIndex()];

        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()]
                - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];

        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()]
                - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];

        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()]
                - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];

        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()]
                - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];

        long user = ticks[CentralProcessor.TickType.USER.getIndex()]
                - prevTicks[CentralProcessor.TickType.USER.getIndex()];

        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()]
                - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];

        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()]
                - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];

        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        CpuInfo cpuInfo = new CpuInfo();
        cpuInfo.setLogicalNum(processor.getLogicalProcessorCount());
        cpuInfo.setUserRate(user * 1.0 / totalCpu);
        cpuInfo.setSysRate(cSys * 1.0 / totalCpu);
        cpuInfo.setSystemLoad(processor.getSystemCpuLoad(1000));
        return cpuInfo;
    }

    public HeapMemoryInfo getHeapMemoryInfo() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();
        long initTotalMemorySize = memoryUsage.getInit();
        long maxMemorySize = memoryUsage.getMax();
        long usedMemorySize = memoryUsage.getUsed();
        HeapMemoryInfo heapMemoryInfo = new HeapMemoryInfo();
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
        return physicalMemoryInfo;
    }

}

