package cn.langpy.kotime.controller;

import cn.langpy.kotime.annotation.Auth;
import cn.langpy.kotime.config.DefaultConfig;
import cn.langpy.kotime.constant.KoConstant;
import cn.langpy.kotime.model.*;
import cn.langpy.kotime.service.GraphService;
import cn.langpy.kotime.util.Context;
import cn.langpy.kotime.util.InvalidAuthInfoException;
import cn.langpy.kotime.util.KoUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * zhangchang
 */
@Controller
@RequestMapping("/koTime")
public class KoTimeController {
    @Value("${ko-time.user-name:}")
    private String userName;
    @Value("${ko-time.password:}")
    private String password;

    private static Logger log = Logger.getLogger(KoTimeController.class.toString());

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
    public Map isLogin(String token) {
        Map map = new HashMap();
        map.put("state", 1);
        boolean checkLogin = KoUtil.isLogin(token);
        map.put("isLogin", checkLogin ? 1 : 0);
        return map;
    }


    @GetMapping
    public void index(String test, HttpServletResponse response, HttpServletRequest request) {
        if (null != test) {
            return;
        }
        response.setContentType("text/html;charset=utf-8");
        ClassPathResource classPathResource = new ClassPathResource(KoConstant.kotimeViewer);
        try (
                InputStream inputStream = classPathResource.getInputStream();
                InputStreamReader streamReader = new InputStreamReader(inputStream, "utf-8");
                BufferedReader reader = new BufferedReader(streamReader);
                PrintWriter out = response.getWriter()) {

            String context = request.getContextPath();
            if (StringUtils.hasText(Context.getConfig().getContextPath())) {
                context = Context.getConfig().getContextPath();
            }
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            int n = 0;
            while ((line = reader.readLine()) != null) {
                if (n > 14) {
                    if (line.indexOf(KoConstant.globalThreshold) > -1) {
                        line = line.replace(KoConstant.globalThreshold, Context.getConfig().getThreshold() + "");
                    } else if (line.indexOf(KoConstant.globalNeedLogin) > -1) {
                        line = line.replace(KoConstant.globalNeedLogin, Context.getConfig().getAuthEnable() + "");
                    } else if (line.indexOf(KoConstant.contextPath) > -1) {
                        line = line.replace(KoConstant.contextPath, context);
                    } else if (line.indexOf(KoConstant.exceptionTitleStyle) > -1) {
                        line = line.replace(KoConstant.exceptionTitleStyle, Context.getConfig().getExceptionEnable() == true ? "" : "display:none;");
                    }
                    stringBuilder.append(line + "\n");
                } else {
                    stringBuilder.append(line + "\n");
                }
                n++;
            }
            line = stringBuilder.toString();
            out.write(line);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/getConfig")
    @ResponseBody
    @Auth
    public DefaultConfig getConfig() {
        DefaultConfig config = Context.getConfig();
        return config;
    }

    @GetMapping("/getStatistic")
    @ResponseBody
    @Auth
    public SystemStatistic getStatistic() {
        GraphService graphService = GraphService.getInstance();
        SystemStatistic system = graphService.getRunStatistic();
        return system;
    }

    @GetMapping("/getApis")
    @ResponseBody
    @Auth
    public List<MethodInfo> getApis(String question) {
        GraphService graphService = GraphService.getInstance();
        List<MethodInfo> list = null;
        if (StringUtils.hasText(question)) {
            list = graphService.searchMethods(question);
        } else {
            list = graphService.getControllers();
        }
        Collections.sort(list);
        return list;
    }

    @GetMapping("/getParamGraph")
    @ResponseBody
    @Auth
    public Map<String, ParamMetric> getParamGraph(String methodId) {
        GraphService graphService = GraphService.getInstance();
        Map<String, ParamMetric> list = graphService.getMethodParamGraph(methodId);
        return list;
    }

    @GetMapping("/getApiTips")
    @ResponseBody
    @Auth
    public List<String> getApiTips(String question) {
        GraphService graphService = GraphService.getInstance();
        List<String> list = graphService.getCondidates(question);
        return list;
    }


    @GetMapping("/getExceptions")
    @ResponseBody
    @Auth
    public List<ExceptionNode> getExceptions() {
        GraphService graphService = GraphService.getInstance();
        List<ExceptionNode> exceptionList = graphService.getExceptions();
        return exceptionList;
    }

    @GetMapping("/getTree")
    @ResponseBody
    @Auth
    public MethodInfo getTree(String methodName) {
        GraphService graphService = GraphService.getInstance();
        MethodInfo tree = graphService.getTree(methodName);
        return tree;
    }

    @GetMapping("/getMethodsByExceptionId")
    @ResponseBody
    @Auth
    public List<ExceptionInfo> getMethodsByExceptionId(String exceptionId) {
        GraphService graphService = GraphService.getInstance();
        List<ExceptionInfo> exceptionInfos = graphService.getExceptionInfos(exceptionId);
        return exceptionInfos;
    }

    @PostMapping("/updateConfig")
    @ResponseBody
    @Auth
    public boolean updateConfig(@RequestBody DefaultConfig config) {
        DefaultConfig koTimeConfig = Context.getConfig();
        if (config.getEnable() != null) {
            koTimeConfig.setEnable(config.getEnable());
        }
        if (config.getExceptionEnable() != null) {
            koTimeConfig.setExceptionEnable(config.getExceptionEnable());
        }
        if (config.getLogEnable() != null) {
            koTimeConfig.setLogEnable(config.getLogEnable());
        }
        if (config.getThreshold() != null) {
            koTimeConfig.setThreshold(config.getThreshold());
        }
        return true;
    }
}
