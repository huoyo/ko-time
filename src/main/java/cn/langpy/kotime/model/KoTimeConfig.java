package cn.langpy.kotime.model;


public class KoTimeConfig {
    private String logLanguage;
    private Boolean kotimeEnable;
    private Boolean logEnable;
    private Double timeThreshold;
    private Boolean exceptionEnable;
    private String dataSaver;

    public Boolean getKotimeEnable() {
        return kotimeEnable;
    }

    public void setKotimeEnable(Boolean kotimeEnable) {
        this.kotimeEnable = kotimeEnable;
    }

    public String getDataSaver() {
        return dataSaver;
    }

    public void setDataSaver(String dataSaver) {
        this.dataSaver = dataSaver;
    }

    public Double getTimeThreshold() {
        return timeThreshold;
    }

    public void setTimeThreshold(Double timeThreshold) {
        this.timeThreshold = timeThreshold;
    }

    public String getLogLanguage() {
        return logLanguage;
    }

    public void setLogLanguage(String logLanguage) {
        this.logLanguage = logLanguage;
    }

    public Boolean getLogEnable() {
        return logEnable;
    }

    public void setLogEnable(Boolean logEnable) {
        this.logEnable = logEnable;
    }

    public Boolean getExceptionEnable() {
        return exceptionEnable;
    }

    public void setExceptionEnable(Boolean exceptionEnable) {
        this.exceptionEnable = exceptionEnable;
    }

    @Override
    public String toString() {
        return "KoTimeConfig{" +
                "logLanguage='" + logLanguage + '\'' +
                "timeThreshold='" + timeThreshold + '\'' +
                ", logEnable=" + logEnable +
                '}';
    }
}
