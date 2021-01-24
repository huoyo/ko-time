package cn.langpy.kotime.controller;

import cn.langpy.kotime.model.RunTimeNode;
import cn.langpy.kotime.model.SystemStatistic;
import cn.langpy.kotime.service.RunTimeNodeService;
import cn.langpy.kotime.util.Context;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/koTime")
public class KoTimeController {

    @GetMapping
    public String index(Model model, HttpServletRequest request) {
        List<RunTimeNode> list = RunTimeNodeService.getControllers();
        model.addAttribute("list",list);
        SystemStatistic system = RunTimeNodeService.getRunStatistic();
        model.addAttribute("system",system);
        model.addAttribute("config",Context.getConfig());
        return "index";
    }

    @GetMapping("/getTree")
    @ResponseBody
    public RunTimeNode getTree(String methodName,Model model, HttpServletRequest request) {
        return RunTimeNodeService.getGraph(methodName);
    }

}
