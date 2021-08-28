package cn.langpy.kotime.controller;

import cn.langpy.kotime.config.DefaultConfig;
import cn.langpy.kotime.model.*;
import cn.langpy.kotime.service.GraphService;
import cn.langpy.kotime.util.Context;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/koTime")
public class KoTimeController {
    public static Logger log = Logger.getLogger(KoTimeController.class.toString());

    @GetMapping
    public void index(String test,HttpServletResponse response, HttpServletRequest request) throws Exception {
        if (null!=test) {
            return;
        }
        response.setContentType("text/html;charset=utf-8");
        ClassPathResource classPathResource = new ClassPathResource("kotime.html");
        BufferedReader reader = new BufferedReader(new InputStreamReader(classPathResource.getInputStream(),"utf-8"));
        PrintWriter out = response.getWriter();

        String context = request.getContextPath();
        if (StringUtils.hasText(Context.getConfig().getContextPath())) {
            context = Context.getConfig().getContextPath();
        }
        StringBuilder stringBuilder = new StringBuilder();
        String line = "";
        while((line = reader.readLine()) != null) {
            stringBuilder.append(line+"\n");
        }
        line = stringBuilder.toString()
                .replace("globalThresholdValue",Context.getConfig().getThreshold()+"")
                .replace("contextPath",context)
                .replace("exceptionTitleStyle",Context.getConfig().getExceptionEnable()==true?"":"display:none;");
        out.write(line);
        out.close();
    }


    @GetMapping("/getConfig")
    @ResponseBody
    public DefaultConfig getConfig() {
        return Context.getConfig();
    }

    @GetMapping("/getStatistic")
    @ResponseBody
    public SystemStatistic getStatistic() {
        GraphService graphService = GraphService.getInstance();
        SystemStatistic system = graphService.getRunStatistic();
        return system;
    }

    @GetMapping("/getApis")
    @ResponseBody
    public List<MethodInfo> getApis() {
        GraphService graphService = GraphService.getInstance();
        List<MethodInfo> list = graphService.getControllers();
        Collections.sort(list);
        return list;
    }


    @GetMapping("/getExceptions")
    @ResponseBody
    public List<ExceptionNode> getExceptions() {
        GraphService graphService = GraphService.getInstance();
        List<ExceptionNode> exceptionList = graphService.getExceptions();
        return exceptionList;
    }

    @GetMapping("/getTree")
    @ResponseBody
    public MethodInfo getTree(String methodName) {
        GraphService graphService = GraphService.getInstance();
        return graphService.getTree(methodName);
    }

    @GetMapping("/getMethodsByExceptionId")
    @ResponseBody
    public List<ExceptionInfo> getMethodsByExceptionId(String exceptionId) {
        GraphService graphService = GraphService.getInstance();
        return graphService.getExceptionInfos(exceptionId);
    }

    @PostMapping("/updateConfig")
    @ResponseBody
    public boolean updateConfig(@RequestBody DefaultConfig config) {
        DefaultConfig koTimeConfig = Context.getConfig();
        if (config.getEnable()!=null) {
            koTimeConfig.setEnable(config.getEnable());
        }
        if (config.getExceptionEnable()!=null) {
            koTimeConfig.setExceptionEnable(config.getExceptionEnable());
        }
        if (config.getLogEnable()!=null) {
            koTimeConfig.setLogEnable(config.getLogEnable());
        }
        if (config.getThreshold()!=null) {
            koTimeConfig.setThreshold(config.getThreshold());
        }
        return true;
    }
}
