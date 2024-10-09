package cn.langpy.kotime.service.metric;

import cn.langpy.kotime.model.ThreadInfo;
import cn.langpy.kotime.model.ThreadUsage;
import cn.langpy.kotime.service.core.SystemService;

import java.lang.management.ThreadMXBean;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ThreadMetricService extends SystemService {
    private static Logger log = Logger.getLogger(ThreadMetricService.class.toString());


    public ThreadUsage getThreadUsage() {
        ThreadUsage usage = new ThreadUsage();
        List<ThreadInfo> threads = getThreads();
        usage.setTotalNum(threads.size());
        usage.setRunnableNum(getThreads("RUNNABLE").size());
        long[] deadlockedThreads = getThreadMXBean().findDeadlockedThreads();
        usage.setDeadLockNum(deadlockedThreads==null?0:deadlockedThreads.length);
        return usage;
    }

    public List<Long> getDeadlockThreadIds() {
        List<Long> threads = new ArrayList<>();
        long[] deadlockedThreads = getThreadMXBean().findDeadlockedThreads();
        if (deadlockedThreads==null || deadlockedThreads.length == 0) {
            return threads;
        }
        java.lang.management.ThreadInfo[] threadInfos = getThreadMXBean().getThreadInfo(deadlockedThreads);
        List<Long> collect = Arrays.stream(threadInfos).map(a -> a.getThreadId()).collect(Collectors.toList());
        return collect;
    }

    public List<ThreadInfo> getThreads() {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        ThreadMXBean threadMXBean = getThreadMXBean();
        int activeCount = threadGroup.activeCount();
        Thread[] threads = new Thread[activeCount];
        threadGroup.enumerate(threads);
        List<Long> deadlockThreadIds = getDeadlockThreadIds();
        List<ThreadInfo> list = new ArrayList<>();
        for (int i = 0; i < activeCount; i++) {
            Thread thread = threads[i];
            ThreadInfo threadInfo = new ThreadInfo();
            threadInfo.setId(thread.getId());
            BigDecimal threadCpuTime = BigDecimal.valueOf(threadMXBean.getThreadCpuTime(thread.getId()));
            BigDecimal threadUserTime = BigDecimal.valueOf(threadMXBean.getThreadUserTime(thread.getId()));
            if (threadUserTime.doubleValue() > 0) {
                threadInfo.setCpuUsage(threadCpuTime.divide(threadUserTime, 2, BigDecimal.ROUND_HALF_UP));
            }else {
                threadInfo.setCpuUsage(BigDecimal.ZERO);
            }
            threadInfo.setName(thread.getName());
            threadInfo.setClassType(thread.getClass().getSimpleName());
            threadInfo.setState(thread.getState().name());
            threadInfo.setInterrupted(thread.isInterrupted());
            threadInfo.setDaemon(thread.isDaemon());
            threadInfo.setPriority(thread.getPriority());
            StackTraceElement[] stackTrace = thread.getStackTrace();
            threadInfo.setStacks(Arrays.stream(stackTrace).collect(Collectors.toList()));
            if (deadlockThreadIds.contains(thread.getId())) {
                threadInfo.setDeadLock(true);
            }else {
                threadInfo.setDeadLock(false);
            }
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
