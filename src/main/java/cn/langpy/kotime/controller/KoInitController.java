package cn.langpy.kotime.controller;

import cn.langpy.kotime.constant.KoConstant;
import cn.langpy.kotime.model.*;
import cn.langpy.kotime.util.Context;
import cn.langpy.kotime.util.InvalidAuthInfoException;
import cn.langpy.kotime.util.KoUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * zhangchang
 */
@Controller
@RequestMapping("/koTime")
public class KoInitController {
    private static Logger log = Logger.getLogger(KoInitController.class.toString());

    @Value("${ko-time.user-name:}")
    private String userName;
    @Value("${ko-time.password:}")
    private String password;
    private final Pattern pattern = Pattern.compile("\\{\\{[a-zA-Z0-9\\.-]+\\}\\}");

    private final String uiKitCssText = getResourceText("kostatic/uikit.min.css");
    private final String uiKitJsText = getResourceText("kostatic/uikit.min.js");
    private final String metricFlowJsText = getResourceText("kostatic/Metricflow.js");
    private final String jQueryJsText = getResourceText("kostatic/JQuery.min.js");
    private final String uiKitIconsJs = getResourceText("kostatic/uikit-icons.js");
    private final String KoTimeUtil = getResourceText("kostatic/util.js");

    @PostMapping("/login")
    @ResponseBody
    public Map login(@RequestBody UserInfo userInfo) {
        if (null == userInfo || !StringUtils.hasText(userInfo.getUserName()) || !StringUtils.hasText(userInfo.getPassword())) {
            throw new InvalidAuthInfoException("failed to login for kotime,please fill userName and password!");
        }
        Map map = new HashMap();
        if (userName.equals(userInfo.getUserName()) && password.equals(userInfo.getPassword())) {
            String token = KoUtil.login(userInfo.getUserName());
            map.put("state", 1);
            map.put("token", token);
            return map;
        }
        map.put("state", 0);
        return map;
    }

    @GetMapping("/isLogin")
    @ResponseBody
    public Map isLogin(String kotoken) {
        Map map = new HashMap();
        map.put("state", 1);
        boolean checkLogin = false;
        if (StringUtils.hasText(kotoken)) {
            if (kotoken.equals(Context.getConfig().getStaticToken())) {
                checkLogin = true;
            } else {
                checkLogin = KoUtil.isLogin(kotoken);
            }
        }
        map.put("isLogin", checkLogin ? 1 : 0);
        return map;
    }



    @GetMapping
    public void index(String kotoken, String charset, String language, HttpServletResponse response, HttpServletRequest request) {
        if (!Context.getConfig().getEnable()) {
            return;
        }

        if (!StringUtils.hasText(charset)) {
            charset = "utf-8";
        }
        response.setContentType("text/html;charset=" + charset);
        ClassPathResource classPathResource = new ClassPathResource(KoConstant.kotimeViewer);
        try (
                InputStream inputStream = classPathResource.getInputStream();
                InputStreamReader streamReader = new InputStreamReader(inputStream, "utf-8");
                BufferedReader reader = new BufferedReader(streamReader);
                PrintWriter out = response.getWriter()) {
            Properties languageDict = KoUtil.getLanguageDict(language);
            String context = request.getContextPath();
            if (StringUtils.hasText(Context.getConfig().getContextPath())) {
                context = Context.getConfig().getContextPath();
            }
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                line = formatStaticResource(line,context,kotoken);
                line = formatLanguageDesc(line, languageDict);
                stringBuilder.append(line + "\n");
            }
            line = stringBuilder.toString();
            out.write(line);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatStaticResource(String line,String context,String kotoken) {
        boolean staticTokenVisit = false;
        if (StringUtils.hasText(kotoken)) {
            staticTokenVisit = true;
        }
        if (line.indexOf(KoConstant.globalThreshold) > -1) {
            line = line.replace(KoConstant.globalThreshold, Context.getConfig().getThreshold() + "");
        } else if (line.indexOf(KoConstant.globalNeedLogin) > -1) {
            line = line.replace(KoConstant.globalNeedLogin, Context.getConfig().getAuthEnable() + "");
        } else if (line.indexOf(KoConstant.contextPath) > -1) {
            line = line.replace(KoConstant.contextPath, context);
        } else if (line.indexOf(KoConstant.exceptionTitleStyle) > -1) {
            line = line.replace(KoConstant.exceptionTitleStyle, Context.getConfig().getExceptionEnable() == true ? "" : "display:none;");
        } else if (line.indexOf("UIKitCss") > -1) {
            line = line.replace("UIKitCss", uiKitCssText);
        } else if (line.indexOf("UIKitJs") > -1) {
            line = line.replace("UIKitJs", uiKitJsText);
        } else if (line.indexOf("MetricFlowJs") > -1) {
            line = line.replace("MetricFlowJs", metricFlowJsText);
        } else if (line.indexOf("jQueryJs") > -1) {
            line = line.replace("jQueryJs", jQueryJsText);
        } else if (line.indexOf("uiKitIconsJs") > -1) {
            line = line.replace("uiKitIconsJs", uiKitIconsJs);
        } else if (line.indexOf("staticTokenVisitValue") > -1) {
            line = line.replace("staticTokenVisitValue", staticTokenVisit + "");
        } else if (line.indexOf("staticTokenValue") > -1) {
            line = line.replace("staticTokenValue", "'" + kotoken + "'");
        } else if (line.indexOf("KoTimeUtil") > -1) {
            line = line.replace("KoTimeUtil", KoTimeUtil);
        } else if (line.indexOf("koTimeVersionValue") > -1) {
            line = line.replace("koTimeVersionValue", "'" + KoUtil.getVerssion()+ "'");
        }
        return line;
    }

    private String formatLanguageDesc(String line, Properties languageDict) {
        String languageKey = getLanguageKey(line);
        if (languageKey != null) {
            String key = languageKey.replaceAll("\\{|\\}", "");
            String value = languageDict.getProperty(key);
            line = line.replace(languageKey, value);
        }
        return line;
    }

    private String getLanguageKey(String line) {
        Matcher m = pattern.matcher(line);
        String key = null;
        if (m.find()) {
            key = m.group(0);
        }
        return key;
    }

    private String getResourceText(String fileName) {
        ClassPathResource classPathResource = new ClassPathResource(fileName);
        try (InputStream inputStream = classPathResource.getInputStream();
             InputStreamReader streamReader = new InputStreamReader(inputStream, "utf-8");
             BufferedReader reader = new BufferedReader(streamReader)) {
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


}
