package cn.langpy.kotime.controller;

import cn.langpy.kotime.annotation.Auth;
import cn.langpy.kotime.config.DefaultConfig;

import cn.langpy.kotime.model.KoResult;
import cn.langpy.kotime.util.Context;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;


/**
 * zhangchang
 */
@Controller
@RequestMapping("/koTime/configs")
public class KoConfigController {
    private static Logger log = Logger.getLogger(KoConfigController.class.toString());

    @GetMapping
    @ResponseBody
    @Auth
    public KoResult getConfig() {
        DefaultConfig config = Context.getConfig();
        return KoResult.success(config);
    }


    @PutMapping
    @ResponseBody
    @Auth
    public KoResult updateConfig(@RequestBody DefaultConfig config) {
        DefaultConfig koTimeConfig = Context.getConfig();
        if (config.getEnable() != null) {
            koTimeConfig.setEnable(config.getEnable());
        }
        if (config.getExceptionEnable() != null) {
            koTimeConfig.setExceptionEnable(config.getExceptionEnable());
        }
        if (config.getLogEnable() != null) {
            koTimeConfig.setLogEnable(config.getLogEnable());
        }
        if (config.getMailEnable() != null) {
            koTimeConfig.setMailEnable(config.getMailEnable());
        }
        if (config.getAbbreviationEnable() != null) {
            koTimeConfig.setAbbreviationEnable(config.getAbbreviationEnable());
        }
        if (config.getThreshold() != null) {
            koTimeConfig.setThreshold(config.getThreshold());
        }
        if (config.getLanguage() != null) {
            koTimeConfig.setLanguage(config.getLanguage());
        }
        return KoResult.success();
    }

}
