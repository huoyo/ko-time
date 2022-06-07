package cn.langpy.kotime.constant;

import cn.langpy.kotime.util.Context;

public class KoConstant {
    public final static String comMethodRange = "@annotation(cn.langpy.kotime.annotation.ComputeTime)";
    public final static String authRange = "@annotation(cn.langpy.kotime.annotation.Auth)";
    public final static String exceptionTitleStyle = "exceptionTitleStyle";
    public final static String globalThreshold = "globalThresholdValue";
    public final static String globalNeedLogin = "globalNeedLoginValue";
    public final static String globalIsLogin = "globalIsLoginValue";
    public final static String contextPath = "contextPath";
    public final static String kotimeViewer = "kotime.html";
    public final static String kotimeViewerEn = "kotime-en.html";
    public final static String loginName = "kotimeUserName";

    public static String getViewName() {
        if ("chinese".equals(Context.getConfig().getLanguage())) {
            return kotimeViewer;
        }else {
            return kotimeViewerEn;
        }

    }
}
