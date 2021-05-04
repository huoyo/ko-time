package cn.langpy.kotime.model;

import java.util.Objects;

public class ExceptionInfo {
    private String name;
    private String className;
    private String message;
    private Integer location;
    private Integer exceptionNum = 0;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public Integer getExceptionNum() {
        return exceptionNum;
    }

    public void setExceptionNum(Integer exceptionNum) {
        this.exceptionNum = exceptionNum;
    }

}
