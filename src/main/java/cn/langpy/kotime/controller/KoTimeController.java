package cn.langpy.kotime.controller;

import cn.langpy.kotime.model.RunTimeNode;
import cn.langpy.kotime.model.SystemStatistic;
import cn.langpy.kotime.util.Context;
import cn.langpy.kotime.util.MethodType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/koTime")
@Slf4j
public class KoTimeController {
    @GetMapping
    public String index(Model model, HttpServletRequest request) {
        List<RunTimeNode> list = Context.get(MethodType.Controller);
        model.addAttribute("list",list);
        SystemStatistic system = Context.getStatistic();
        model.addAttribute("system",system);
        return "index";
    }
    @GetMapping("/getTree")
    @ResponseBody
    public RunTimeNode getTree(String methodName,Model model, HttpServletRequest request) {
        return Context.getTree(methodName);
    }

}
