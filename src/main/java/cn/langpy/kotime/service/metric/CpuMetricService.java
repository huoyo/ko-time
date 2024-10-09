package cn.langpy.kotime.service.metric;

import cn.langpy.kotime.model.CpuUsage;
import cn.langpy.kotime.service.core.SystemService;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class CpuMetricService extends SystemService {
    Logger log = Logger.getLogger(CpuMetricService.class.toString());
    public CpuUsage getCpuUsage() {
        SystemInfo systemInfo = new SystemInfo();
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long[] ticks = processor.getSystemCpuLoadTicks();
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
        CpuUsage cpuInfo = new CpuUsage();
        cpuInfo.setLogicalNum(processor.getLogicalProcessorCount());
        cpuInfo.setUserRate(user * 1.0 / totalCpu);
        cpuInfo.setSysRate(cSys * 1.0 / totalCpu);
        cpuInfo.setWaitRate(iowait * 1.0 / totalCpu);
        cpuInfo.setSystemLoad(processor.getSystemCpuLoad(1000));
        return cpuInfo;
    }

}
