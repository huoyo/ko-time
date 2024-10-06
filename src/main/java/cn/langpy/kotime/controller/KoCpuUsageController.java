package cn.langpy.kotime.controller;

import cn.langpy.kotime.annotation.Auth;
import cn.langpy.kotime.model.KoResult;
import cn.langpy.kotime.service.metric.CpuMetricService;
import cn.langpy.kotime.service.core.SystemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.logging.Logger;


/**
 * zhangchang
 */
@Controller
@RequestMapping("/koTime/cpus")
public class KoCpuUsageController {
    private static Logger log = Logger.getLogger(KoCpuUsageController.class.toString());

    @GetMapping("/usage")
    @ResponseBody
    @Auth
    public KoResult usage() {
        CpuMetricService instance = SystemService.getInstance(CpuMetricService.class);
        return KoResult.success(instance.getCpuUsage());
    }

}
