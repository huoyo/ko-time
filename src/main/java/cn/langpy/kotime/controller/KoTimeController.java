package cn.langpy.kotime.controller;

import cn.langpy.kotime.model.*;
import cn.langpy.kotime.service.GraphService;
import cn.langpy.kotime.util.Context;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/koTime")
public class KoTimeController {
    @Value("${koTime.ui.template:freemarker}")
    private String showTemplate;

    @GetMapping
    public String index(Model model) {
        GraphService graphService = GraphService.getInstance();
        List<MethodInfo> methodList = graphService.getControllers();
        List<ExceptionNode> exceptionList = graphService.getExceptions();
        Collections.sort(methodList);
        model.addAttribute("methodList", methodList);
        model.addAttribute("exceptionList", exceptionList);
        SystemStatistic system = graphService.getRunStatistic();
        model.addAttribute("system", system);
        model.addAttribute("config", Context.getConfig());
        String template = "index-freemarker";
        if ("thymeleaf".equals(showTemplate)) {
            template = "index-thymeleaf";
        }
        return template;
    }

    @GetMapping("/getConfig")
    @ResponseBody
    public KoTimeConfig getConfig() {
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
    public boolean updateConfig(@RequestBody KoTimeConfig config) {
        KoTimeConfig koTimeConfig = Context.getConfig();
        if (config.getKotimeEnable()!=null) {
            koTimeConfig.setKotimeEnable(config.getKotimeEnable());
        }
        if (config.getExceptionEnable()!=null) {
            koTimeConfig.setExceptionEnable(config.getExceptionEnable());
        }
        if (config.getLogEnable()!=null) {
            koTimeConfig.setLogEnable(config.getLogEnable());
        }
        if (config.getTimeThreshold()!=null) {
            koTimeConfig.setTimeThreshold(config.getTimeThreshold());
        }
        return true;
    }
}
