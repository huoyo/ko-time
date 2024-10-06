package cn.langpy.kotime.controller;

import cn.langpy.kotime.annotation.Auth;
import cn.langpy.kotime.model.*;
import cn.langpy.kotime.service.GraphService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.List;
import java.util.logging.Logger;


/**
 * zhangchang
 */
@Controller
@RequestMapping("/koTime/exceptions")
public class KoExceptionController {
    private static Logger log = Logger.getLogger(KoExceptionController.class.toString());


    @GetMapping
    @ResponseBody
    @Auth
    public KoResult getExceptions() {
        GraphService graphService = GraphService.getInstance();
        List<ExceptionNode> exceptionList = graphService.getExceptions();
        return KoResult.success(exceptionList);
    }

    @GetMapping("/{exceptionId}/details")
    @ResponseBody
    @Auth
    public List<ExceptionInfo> getMethodsByExceptionId(@PathVariable("exceptionId") String exceptionId, String message) {
        GraphService graphService = GraphService.getInstance();
        List<ExceptionInfo> exceptionInfos = graphService.getExceptionInfos(exceptionId, message);
        return exceptionInfos;
    }

}
