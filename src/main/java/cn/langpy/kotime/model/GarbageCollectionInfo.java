package cn.langpy.kotime.model;

public class GarbageCollectionInfo {
    private Integer youngGcNum;
    private Long youngGcTime;
    private Integer oldGcNum;
    private Long oldGcNumTime;

    public Integer getYoungGcNum() {
        return youngGcNum;
    }

    public void setYoungGcNum(Integer youngGcNum) {
        this.youngGcNum = youngGcNum;
    }

    public Long getYoungGcTime() {
        return youngGcTime;
    }

    public void setYoungGcTime(Long youngGcTime) {
        this.youngGcTime = youngGcTime;
    }

    public Integer getOldGcNum() {
        return oldGcNum;
    }

    public void setOldGcNum(Integer oldGcNum) {
        this.oldGcNum = oldGcNum;
    }

    public Long getOldGcNumTime() {
        return oldGcNumTime;
    }

    public void setOldGcNumTime(Long oldGcNumTime) {
        this.oldGcNumTime = oldGcNumTime;
    }
}
