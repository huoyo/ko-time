package cn.langpy.kotime.model;

import java.util.Objects;

public class ExceptionRelation {
    private String id;
    private String sourceId;
    private String targetId;
    private Integer exceptionNum = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public Integer getExceptionNum() {
        return exceptionNum;
    }

    public void setExceptionNum(Integer exceptionNum) {
        this.exceptionNum = exceptionNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExceptionRelation that = (ExceptionRelation) o;
        return Objects.equals(sourceId, that.sourceId) &&
                Objects.equals(targetId, that.targetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceId, targetId);
    }
}
