package cn.langpy.kotime.model;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public class ThreadInfo {

    public static final ThreadInfoComparator COMPARATOR = new ThreadInfoComparator();

    private Long id;
    private String name;
    private String classType;
    private String state;
    private Boolean isInterrupted;
    private Boolean isDaemon;
    private Integer priority;
    private BigDecimal cpuUsage;
    private List<StackTraceElement> stacks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getInterrupted() {
        return isInterrupted;
    }

    public void setInterrupted(Boolean interrupted) {
        isInterrupted = interrupted;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean getDaemon() {
        return isDaemon;
    }

    public void setDaemon(Boolean daemon) {
        isDaemon = daemon;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public BigDecimal getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(BigDecimal cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public List<StackTraceElement> getStacks() {
        return stacks;
    }

    public void setStacks(List<StackTraceElement> stacks) {
        this.stacks = stacks;
    }

    /**
     * 状态码排序 + CPU使用率倒排
     */
    public static class ThreadInfoComparator implements Comparator<ThreadInfo> {
        @Override
        public int compare(ThreadInfo a, ThreadInfo b) {
            if (a.getState().compareTo(b.getState()) == 0 ) {
                return b.getCpuUsage().compareTo(a.getCpuUsage());
            }
            return a.getState().compareTo(b.getState());
        }
    }

}
