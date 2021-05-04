package cn.langpy.kotime.controller;

import cn.langpy.kotime.model.KoTimeConfig;
import cn.langpy.kotime.model.MethodInfo;
import cn.langpy.kotime.model.SystemStatistic;
import cn.langpy.kotime.service.GraphService;
import cn.langpy.kotime.util.Context;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
        List<MethodInfo> list = graphService.getControllers();
        Collections.sort(list);
        model.addAttribute("list", list);
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

    @GetMapping("/getTree")
    @ResponseBody
    public MethodInfo getTree(String methodName) {
        GraphService graphService = GraphService.getInstance();
        return graphService.getTree(methodName);
    }
}
