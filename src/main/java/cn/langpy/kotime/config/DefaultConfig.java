package cn.langpy.kotime.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * zhangchang
 */
@Component
@ConfigurationProperties(prefix = "ko-time")
@Lazy(value = false)
public class DefaultConfig {
    /**
     * to enable kotime
     */
    private Boolean enable = true;


    /**
     * to select language for viewer, chinese
     */
    private String language = "chinese";
    @Deprecated
    private String logLanguage;

    /**
     * false
     * to print log in console
     */
    private Boolean logEnable = false;

    /**
     * "defaultValue": true,
     * "description": "to enable version-notice",
     */
    private Boolean versionNotice = true;

    /**
     * red will appear if avgRunTime>threshold
     */
    private Double threshold = 800.0;


    /**
     * data will be discard if Math.random()<discard-rate
     */
    private Double discardRate = 0.3;

    /**
     * pointcut for aop
     * execution(* com.example..*.*(..))
     */
    private String pointcut = "execution(* com.example..*.*(..))";

    /**
     * to enable exception listener
     * "defaultValue": false,
     */
    private Boolean exceptionEnable = false;

    /**
     * the charger of analysing params
     */
    private Boolean paramAnalyse = true;

    /**
     * a place to store data
     * memory
     */
    private String saver = "memory";

    /**
     * datasource
     */
    private String dataSource;

    /**
     * StringRedisTemplate‘s bean
     */
    private String redisTemplate;

    /**
     * number of thread to store data
     */
    private Integer threadNum = 2;

    /**
     * server url for static resources
     * http://localhost:8080
     */
    private String contextPath = "";

    /**
     * dataPrefix
     */
    private String dataPrefix;

    /**
     * to delete all data
     */
    private Boolean dataReset = false;

    /**
     * to enable authentication
     */
    private Boolean authEnable = false;

    /**
     * userName for authentication
     */
    private String userName;

    /**
     * password for authentication
     */
    private String password;

    /**
     * static token of html
     */
    private String staticToken;

    /**
     * expire within 43200 s
     */
    private Long authExpire = 43200L;

    /**
     * to enable email
     */
    private Boolean mailEnable = false;

    /**
     * host of email
     */
    private String mailHost = "smtp.qq.com";

    /**
     * port of email
     */
    private Integer mailPort = 587;

    /**
     * protocol of email
     */
    private String mailProtocol = "smtp";

    /**
     * encoding of email
     */
    private String mailEncoding = "UTF-8";

    /**
     * sender of email
     */
    private String mailUser;

    /**
     * code of email
     */
    private String mailCode;

    /**
     * receivers of email
     */
    private String mailReceivers;

    /**
     * trigger thresold of email
     */
    private Integer mailThreshold = 4;

    /**
     * trigger scope of email
     */
    private String mailScope = "Controller";

    /**
     * dynamic.properties
     */
    private String propertyFile = "dynamic.properties";

    public String getPropertyFile() {
        return propertyFile;
    }

    public void setPropertyFile(String propertyFile) {
        this.propertyFile = propertyFile;
    }

    public Boolean getMailEnable() {
        return mailEnable;
    }

    public void setMailEnable(Boolean mailEnable) {
        this.mailEnable = mailEnable;
    }

    public String getMailHost() {
        return mailHost;
    }

    public void setMailHost(String mailHost) {
        this.mailHost = mailHost;
    }

    public Integer getMailPort() {
        return mailPort;
    }

    public void setMailPort(Integer mailPort) {
        this.mailPort = mailPort;
    }

    public String getMailProtocol() {
        return mailProtocol;
    }

    public void setMailProtocol(String mailProtocol) {
        this.mailProtocol = mailProtocol;
    }

    public String getMailEncoding() {
        return mailEncoding;
    }

    public void setMailEncoding(String mailEncoding) {
        this.mailEncoding = mailEncoding;
    }

    public String getMailUser() {
        return mailUser;
    }

    public void setMailUser(String mailUser) {
        this.mailUser = mailUser;
    }

    public String getMailCode() {
        return mailCode;
    }

    public void setMailCode(String mailCode) {
        this.mailCode = mailCode;
    }

    public String getMailReceivers() {
        return mailReceivers;
    }

    public void setMailReceivers(String mailReceivers) {
        this.mailReceivers = mailReceivers;
    }

    public Integer getMailThreshold() {
        return mailThreshold;
    }

    public void setMailThreshold(Integer mailThreshold) {
        this.mailThreshold = mailThreshold;
    }

    public String getMailScope() {
        return mailScope;
    }

    public void setMailScope(String mailScope) {
        this.mailScope = mailScope;
    }

    public String getStaticToken() {
        return staticToken;
    }

    public void setStaticToken(String staticToken) {
        this.staticToken = staticToken;
    }

    public Boolean getVersionNotice() {
        return versionNotice;
    }

    public void setVersionNotice(Boolean versionNotice) {
        this.versionNotice = versionNotice;
    }

    public Long getAuthExpire() {
        return authExpire;
    }

    public void setAuthExpire(Long authExpire) {
        this.authExpire = authExpire;
    }

    public Double getDiscardRate() {
        return discardRate;
    }

    public void setDiscardRate(Double discardRate) {
        this.discardRate = discardRate;
    }

    public Boolean getDataReset() {
        return dataReset;
    }

    public void setDataReset(Boolean dataReset) {
        this.dataReset = dataReset;
    }

    public String getDataPrefix() {
        return dataPrefix;
    }

    public void setDataPrefix(String dataPrefix) {
        this.dataPrefix = dataPrefix;
    }

    public String getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(String redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
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

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public String getPointcut() {
        return pointcut;
    }

    public void setPointcut(String pointcut) {
        this.pointcut = pointcut;
    }

    public Boolean getExceptionEnable() {
        return exceptionEnable;
    }

    public void setExceptionEnable(Boolean exceptionEnable) {
        this.exceptionEnable = exceptionEnable;
    }

    public String getSaver() {
        return saver;
    }

    public void setSaver(String saver) {
        this.saver = saver;
    }


    public Integer getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(Integer threadNum) {
        this.threadNum = threadNum;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAuthEnable() {
        return authEnable;
    }

    public void setAuthEnable(Boolean authEnable) {
        this.authEnable = authEnable;
    }

    public Boolean getParamAnalyse() {
        return paramAnalyse;
    }

    public void setParamAnalyse(Boolean paramAnalyse) {
        this.paramAnalyse = paramAnalyse;
    }

}
