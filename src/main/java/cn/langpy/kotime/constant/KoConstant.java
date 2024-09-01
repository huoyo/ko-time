package cn.langpy.kotime.constant;

import cn.langpy.kotime.util.Context;
import org.springframework.util.StringUtils;

public class KoConstant {
    public final static String comMethodRange = "@annotation(cn.langpy.kotime.annotation.ComputeTime)";
    public final static String authRange = "@annotation(cn.langpy.kotime.annotation.Auth)";
    public final static String exceptionTitleStyle = "exceptionTitleStyle";
    public final static String globalThreshold = "globalThresholdValue";
    public final static String globalNeedLogin = "globalNeedLoginValue";
    public final static String contextPath = "contextPath";
    public final static String kotimeViewer = "kotime.html";
    public final static String kotimeViewerEn = "kotime-en.html";

    @Deprecated
    public static String getViewName(String language) {
        if (!StringUtils.hasText(language)) {
            language = Context.getConfig().getLanguage();
        }
        if ("chinese".equals(language)) {
            return kotimeViewer;
        }else {
            return kotimeViewerEn;
        }

    }
}
