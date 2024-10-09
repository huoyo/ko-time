package cn.langpy.kotime.controller;

import cn.langpy.kotime.annotation.Auth;
import cn.langpy.kotime.model.KoResult;
import cn.langpy.kotime.model.MethodInfo;
import cn.langpy.kotime.model.ParamMetric;
import cn.langpy.kotime.model.SystemStatistic;
import cn.langpy.kotime.service.GraphService;
import cn.langpy.kotime.util.Context;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


/**
 * zhangchang
 */
@Controller
@RequestMapping("/koTime/interfaces")
public class KoInterfaceController {
    private static Logger log = Logger.getLogger(KoInterfaceController.class.toString());

    @GetMapping("/usage")
    @ResponseBody
    @Auth
    public KoResult usage() {
        GraphService graphService = GraphService.getInstance();
        SystemStatistic system = graphService.getRunStatistic();
        return KoResult.success(system);
    }

    @GetMapping
    @ResponseBody
    @Auth
    public KoResult<List<MethodInfo>> getInterfaces(String question, String orderBy, String sort) {
        GraphService graphService = GraphService.getInstance();
        List<MethodInfo> list = null;
        if (StringUtils.hasText(question)) {
            list = graphService.searchMethods(question);
        } else {
            list = graphService.getControllers();
        }

        Collections.sort(list, (o1, o2) -> {
            int sortValue = -1;
            if ("asc".equals(sort)) {
                sortValue = 1;
            }
            if ("callNum".equals(orderBy)) {
                return o1.getCallNum().compareTo(o2.getCallNum()) * sortValue;
            } else {
                return o1.getAvgRunTime().compareTo(o2.getAvgRunTime()) * sortValue;
            }
        });
        return KoResult.success(list);
    }

    @GetMapping("/export")
    @ResponseBody
    @Auth
    public void export(String question, String orderBy, String sort, HttpServletResponse response) {
        List<MethodInfo> apis = getInterfaces(question, orderBy, sort).getContent();
        response.setCharacterEncoding("utf-8");
        response.addHeader("Content-Disposition", "attachment; filename=interfaces.csv");

        try (OutputStream out = response.getOutputStream();
             BufferedOutputStream bufferedOut = new BufferedOutputStream(out)) {
            String line = "序号,类名,方法名,路由,平均响应（ms）,调用次数\n";
            if ("english".equals(Context.getConfig().getLanguage())) {
                line = "No,ClassName,Method,Route,Avg(ms),CallNum\n";
            }
            bufferedOut.write(line.getBytes("utf-8"));
            for (int i = 0; i < apis.size(); i++) {
                MethodInfo methodInfo = apis.get(i);
                line = (i + 1) + "," + methodInfo.getClassName() + "," + methodInfo.getMethodName() + "()," + methodInfo.getRouteName() + "," + methodInfo.getAvgRunTime() + "," + methodInfo.getCallNum() + "\n";
                bufferedOut.write(line.getBytes("utf-8"));
            }
            bufferedOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/searchCondidate")
    @ResponseBody
    @Auth
    public KoResult<List<String>> searchCondidate(String question) {
        GraphService graphService = GraphService.getInstance();
        List<String> list = graphService.getCondidates(question);
        return KoResult.success(list);
    }

    @GetMapping("/{methodId}/paramMetric")
    @ResponseBody
    @Auth
    public KoResult<Map<String, ParamMetric>> paramMetric(@PathVariable("methodId") String methodId) {
        GraphService graphService = GraphService.getInstance();
        Map<String, ParamMetric> list = graphService.getMethodParamGraph(methodId);
        return KoResult.success(list);
    }

    @GetMapping("/{methodId}/tree")
    @ResponseBody
    @Auth
    public KoResult getTree(@PathVariable("methodId") String methodId) {
        GraphService graphService = GraphService.getInstance();
        MethodInfo tree = graphService.getTree(methodId);
        return KoResult.success(tree);
    }

}
