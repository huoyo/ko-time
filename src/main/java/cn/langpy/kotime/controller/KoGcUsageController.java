package cn.langpy.kotime.controller;

import cn.langpy.kotime.annotation.Auth;
import cn.langpy.kotime.model.KoResult;
import cn.langpy.kotime.service.metric.GcMetricService;
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
@RequestMapping("/koTime/gcs")
public class KoGcUsageController {
    private static Logger log = Logger.getLogger(KoGcUsageController.class.toString());

    @GetMapping("/usage")
    @ResponseBody
    @Auth
    public KoResult usage() {
        GcMetricService instance = SystemService.getInstance(GcMetricService.class);
        return KoResult.success(instance.getGcUsage());
    }

}
