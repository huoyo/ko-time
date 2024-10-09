package cn.langpy.kotime.controller;

import cn.langpy.kotime.annotation.Auth;
import cn.langpy.kotime.model.KoResult;
import cn.langpy.kotime.service.metric.JvmSpaceMetricService;
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
@RequestMapping("/koTime/jvmSpaces")
public class KoJvmSpaceController {
    private static Logger log = Logger.getLogger(KoJvmSpaceController.class.toString());

    @GetMapping("/edenSpace")
    @ResponseBody
    @Auth
    public KoResult edenSpace() {
        JvmSpaceMetricService instance = SystemService.getInstance(JvmSpaceMetricService.class);
        return KoResult.success(instance.getEdenSpaceInfo());
    }

    @GetMapping("/survivorSpace")
    @ResponseBody
    @Auth
    public KoResult survivorSpace() {
        JvmSpaceMetricService instance = SystemService.getInstance(JvmSpaceMetricService.class);
        return KoResult.success(instance.getSurvivorSpaceInfo());
    }

    @GetMapping("/oldGen")
    @ResponseBody
    @Auth
    public KoResult oldGen() {
        JvmSpaceMetricService instance = SystemService.getInstance(JvmSpaceMetricService.class);
        return KoResult.success(instance.getOldGenInfo());
    }

    @GetMapping("/metaspace")
    @ResponseBody
    @Auth
    public KoResult metaspace() {
        JvmSpaceMetricService instance = SystemService.getInstance(JvmSpaceMetricService.class);
        return KoResult.success(instance.getMetaspaceInfo());
    }


}
