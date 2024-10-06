package cn.langpy.kotime.controller;

import cn.langpy.kotime.annotation.Auth;
import cn.langpy.kotime.model.KoResult;
import cn.langpy.kotime.service.core.SystemService;
import cn.langpy.kotime.service.metric.ClassInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


/**
 * zhangchang
 */
@Controller
@RequestMapping("/koTime/classes")
public class KoClassController {
    private static Logger log = Logger.getLogger(KoClassController.class.toString());

    @GetMapping("/usage")
    @ResponseBody
    @Auth
    public KoResult usage() {
        ClassInfoService instance = SystemService.getInstance(ClassInfoService.class);
        return KoResult.success(instance.getClassUsage());
    }

    @PutMapping("/{className}/replace")
    @ResponseBody
    @Auth
    public Map updateClass(@RequestParam("classFile") MultipartFile classFile, @PathVariable("className") String className) {
        Map map = new HashMap();
        if (classFile == null || classFile.isEmpty()) {
            map.put("state", 0);
            map.put("message", "文件不能为空");
            return map;
        }
        if (!StringUtils.hasText(className)) {
            map.put("state", 0);
            map.put("message", "类名不能为空");
            return map;
        }
        className = className.trim();
        File file = null;
        try {
            String originalFilename = classFile.getOriginalFilename();
            if (!originalFilename.endsWith(".class")) {
                map.put("state", 0);
                map.put("message", "仅支持.class文件");
                return map;
            }
            String[] filename = originalFilename.split("\\.");
            String substring = className.substring(className.lastIndexOf(".") + 1);
            if (!substring.equals(filename[0])) {
                map.put("state", 0);
                map.put("message", "请确认类名是否正确");
                return map;
            }
            file = uploadFile(classFile.getBytes(), filename[0]);
        } catch (IOException e) {
            log.severe("Error class file!");
            map.put("state", 0);
            map.put("message", "无法解析文件");
            return map;
        }
        final ClassInfoService classService = SystemService.getInstance(ClassInfoService.class);
        classService.updateClass(className, file.getAbsolutePath());
        file.deleteOnExit();

        map.put("state", 1);
        map.put("message", "更新成功");
        return map;
    }


    private static File uploadFile(byte[] file, String fileName) throws IOException {
        FileOutputStream out = null;
        try {
            File targetFile = File.createTempFile(fileName, ".class", new File(System.getProperty("java.io.tmpdir")));
            out = new FileOutputStream(targetFile.getAbsolutePath());
            out.write(file);
            out.flush();
            return targetFile;
        } catch (Exception e) {
            log.severe("" + e);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
        return null;
    }

}
