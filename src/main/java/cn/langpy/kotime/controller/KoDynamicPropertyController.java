package cn.langpy.kotime.controller;

import cn.langpy.kotime.annotation.Auth;
import cn.langpy.kotime.model.*;

import cn.langpy.kotime.util.Context;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.logging.Logger;


/**
 * zhangchang
 */
@Controller
@RequestMapping("/koTime/dynamicProperties")
public class KoDynamicPropertyController {
    private static Logger log = Logger.getLogger(KoDynamicPropertyController.class.toString());


    @PutMapping
    @ResponseBody
    @Auth
    public KoResult updateDynamicProperties(@RequestBody TextParam textParam) {
        if (!StringUtils.hasText(textParam.getText())) {
            return KoResult.failed("更新失败");
        }
        String[] textSplit = textParam.getText().trim().split("\n");
        Properties dynamicProperties = Context.getDynamicProperties();
        for (String line : textSplit) {
            line = line.trim();
            if (line.length()==0 || line.startsWith("#") || line.startsWith("//")) {
                continue;
            }
            int i = line.indexOf("=");
            if (i<1) {
                continue;
            }
            String propertyStr = line.substring(0, i).trim();
            String valueStr = line.substring(i+1).trim();
            log.info("updated property: "+propertyStr+"=("+dynamicProperties.get(propertyStr)+"->"+valueStr+")");
            dynamicProperties.setProperty(propertyStr,valueStr);
        }

        return KoResult.success();
    }

    @GetMapping
    @ResponseBody
    @Auth
    public KoResult getDynamicProperties() {
        Properties dynamicProperties = Context.getDynamicProperties();
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : dynamicProperties.stringPropertyNames()) {
            String value = dynamicProperties.getProperty(key);
            if (value!=null) {
                stringBuilder.append(key+"="+value+"\n");
            }
        }
        return KoResult.success(stringBuilder.toString());
    }
}
