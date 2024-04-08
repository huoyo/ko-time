package cn.langpy.kotime.service;

import cn.langpy.kotime.model.ThreadInfo;
import cn.langpy.kotime.util.Context;
import org.springframework.util.CollectionUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ThreadUsageService {
    private static Logger log = Logger.getLogger(ThreadUsageService.class.toString());

    public static ThreadUsageService newInstance() {
        return new ThreadUsageService();
    }

    public List<ThreadInfo> getThreads() {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();;
        int activeCount = threadGroup.activeCount();
        Thread[] threads = new Thread[activeCount];
        threadGroup.enumerate(threads);
        List<ThreadInfo> list = new ArrayList<>();
        for (int i = 0; i < activeCount; i++) {
            Thread thread = threads[i];
            ThreadInfo threadInfo = new ThreadInfo();
            threadInfo.setId(thread.getId());
            threadInfo.setCpuUsage(BigDecimal.valueOf(threadMXBean.getThreadCpuTime(thread.getId()))
                    .divide(BigDecimal.valueOf(threadMXBean.getThreadUserTime(thread.getId())), 2, BigDecimal.ROUND_HALF_UP));
            threadInfo.setName(thread.getName());
            threadInfo.setClassType(thread.getClass().getSimpleName());
            threadInfo.setState(thread.getState().name());
            threadInfo.setInterrupted(thread.isInterrupted());
            threadInfo.setDaemon(thread.isDaemon());
            threadInfo.setPriority(thread.getPriority());
            StackTraceElement[] stackTrace = thread.getStackTrace();
            threadInfo.setStacks(Arrays.stream(stackTrace).collect(Collectors.toList()));
            list.add(threadInfo);
        }
        Collections.sort(list, Comparator.comparing(ThreadInfo::getCpuUsage).reversed());
        return list;
    }

    public List<ThreadInfo> getThreads(String state) {
        List<ThreadInfo> threads = getThreads();
        return threads.stream().filter(a -> a.getState().equals(state)).collect(Collectors.toList());
    }
}
