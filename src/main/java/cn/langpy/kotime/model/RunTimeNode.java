package cn.langpy.kotime.model;

import cn.langpy.kotime.util.MethodType;

import java.util.List;
import java.util.Objects;


public class RunTimeNode implements Comparable<RunTimeNode> {
    private String name;
    private String className;
    private String methodName;
    private Double avgRunTime = 0.0;
    private Double maxRunTime = 0.0;
    private Double minRunTime = 10000.0;
    private Double value = 0.0;
    private String avgRunTimeUnit = "ms";
    private MethodType methodType;
    private List<RunTimeNode> children;
    @Override
    public int compareTo(RunTimeNode ob) {
        return this.avgRunTime.compareTo(ob.getAvgRunTime());
    }



    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvgRunTimeUnit() {
        return avgRunTimeUnit;
    }

    public void setAvgRunTimeUnit(String avgRunTimeUnit) {
        this.avgRunTimeUnit = avgRunTimeUnit;
    }

    public MethodType getMethodType() {
        return methodType;
    }

    public void setMethodType(MethodType methodType) {
        this.methodType = methodType;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Double getAvgRunTime() {
        return avgRunTime;
    }

    public void setAvgRunTime(Double avgRunTime) {
        this.avgRunTime = avgRunTime;
    }

    public Double getMaxRunTime() {
        return maxRunTime;
    }

    public void setMaxRunTime(Double maxRunTime) {
        this.maxRunTime = maxRunTime;
    }

    public Double getMinRunTime() {
        return minRunTime;
    }

    public void setMinRunTime(Double minRunTime) {
        this.minRunTime = minRunTime;
    }

    public List<RunTimeNode> getChildren() {
        return children;
    }

    public void setChildren(List<RunTimeNode> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RunTimeNode that = (RunTimeNode) o;
        return Objects.equals(className, that.className) &&
                Objects.equals(methodName, that.methodName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, methodName);
    }

    @Override
    public String toString() {
        return "RunTimeNode{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", avgRunTime=" + avgRunTime +
                ", maxRunTime=" + maxRunTime +
                ", minRunTime=" + minRunTime +
                ", avgRunTimeUnit='" + avgRunTimeUnit + '\'' +
                ", methodType=" + methodType +
                ", children=" + children +
                '}';
    }
}
