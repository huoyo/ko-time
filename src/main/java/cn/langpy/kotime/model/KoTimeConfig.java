package cn.langpy.kotime.model;


public class KoTimeConfig {
    private String logLanguage;
    private Boolean logEnable;
    private Double timeThreshold;

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

    @Override
    public String toString() {
        return "KoTimeConfig{" +
                "logLanguage='" + logLanguage + '\'' +
                "timeThreshold='" + timeThreshold + '\'' +
                ", logEnable=" + logEnable +
                '}';
    }
}
