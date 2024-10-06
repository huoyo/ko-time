package cn.langpy.kotime.model;

public class GcUsage {
    private Long totalNum;
    private Long minorNum;
    private Long minorCostTime;
    private Long fullNum;
    private Long fullCostTime;

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public Long getMinorNum() {
        return minorNum;
    }

    public void setMinorNum(Long minorNum) {
        this.minorNum = minorNum;
    }

    public Long getMinorCostTime() {
        return minorCostTime;
    }

    public void setMinorCostTime(Long minorCostTime) {
        this.minorCostTime = minorCostTime;
    }

    public Long getFullNum() {
        return fullNum;
    }

    public void setFullNum(Long fullNum) {
        this.fullNum = fullNum;
    }

    public Long getFullCostTime() {
        return fullCostTime;
    }

    public void setFullCostTime(Long fullCostTime) {
        this.fullCostTime = fullCostTime;
    }
}
