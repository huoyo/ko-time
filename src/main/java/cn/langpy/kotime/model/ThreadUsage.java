package cn.langpy.kotime.model;

public class ThreadUsage {
    private Integer totalNum;
    private Integer runnableNum;
    private Integer deadLockNum;

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getRunnableNum() {
        return runnableNum;
    }

    public void setRunnableNum(Integer runnableNum) {
        this.runnableNum = runnableNum;
    }

    public Integer getDeadLockNum() {
        return deadLockNum;
    }

    public void setDeadLockNum(Integer deadLockNum) {
        this.deadLockNum = deadLockNum;
    }
}
