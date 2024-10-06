package cn.langpy.kotime.controller;

import cn.langpy.kotime.annotation.Auth;
import cn.langpy.kotime.model.KoResult;
import cn.langpy.kotime.model.ThreadInfo;
import cn.langpy.kotime.service.core.SystemService;
import cn.langpy.kotime.service.metric.ThreadMetricService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static cn.langpy.kotime.model.ThreadInfo.COMPARATOR;


/**
 * zhangchang
 */
@Controller
@RequestMapping("/koTime/threads")
public class KoThreadController {
    private static Logger log = Logger.getLogger(KoThreadController.class.toString());

    @GetMapping("/usage")
    @ResponseBody
    @Auth
    public KoResult usage() {
        ThreadMetricService instance = SystemService.getInstance(ThreadMetricService.class);
        return KoResult.success(instance.getThreadUsage());
    }

    @GetMapping
    @ResponseBody
    @Auth
    public KoResult threads(String state) {
        ThreadMetricService instance = SystemService.getInstance(ThreadMetricService.class);
        List<ThreadInfo> threads = instance.getThreads();
        threads = threads.stream().sorted(COMPARATOR).collect(Collectors.toList());

        Map<String, Long> stateCounting = threads.stream().collect(Collectors.groupingBy(ThreadInfo::getState, Collectors.counting()));
        stateCounting.put("all", (long) threads.size());

        Map map = new HashMap();
        map.put("statistics", stateCounting);
        if (StringUtils.hasText(state)) {
            threads = threads.stream().filter(a -> a.getState().equals(state)).collect(Collectors.toList());
        }
        map.put("threads", threads);
        return KoResult.success(map);
    }



}
