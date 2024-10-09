package cn.langpy.kotime.model;

public class ClassUsage {
    private Long totalClassNum;
    private Integer currentClassNum;
    private Long unloadedClassNum;

    public Long getTotalClassNum() {
        return totalClassNum;
    }

    public void setTotalClassNum(Long totalClassNum) {
        this.totalClassNum = totalClassNum;
    }

    public Integer getCurrentClassNum() {
        return currentClassNum;
    }

    public void setCurrentClassNum(Integer currentClassNum) {
        this.currentClassNum = currentClassNum;
    }

    public Long getUnloadedClassNum() {
        return unloadedClassNum;
    }

    public void setUnloadedClassNum(Long unloadedClassNum) {
        this.unloadedClassNum = unloadedClassNum;
    }
}
