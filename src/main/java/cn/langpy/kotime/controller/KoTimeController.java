package cn.langpy.kotime.controller;

import cn.langpy.kotime.annotation.Auth;
import cn.langpy.kotime.config.DefaultConfig;
import cn.langpy.kotime.model.*;
import cn.langpy.kotime.service.ClassService;
import cn.langpy.kotime.service.GraphService;
import cn.langpy.kotime.service.SysUsageService;
import cn.langpy.kotime.service.ThreadUsageService;
import cn.langpy.kotime.util.Context;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static cn.langpy.kotime.model.ThreadInfo.COMPARATOR;

/**
 * zhangchang
 */
@Controller
@RequestMapping("/koTime")
public class KoTimeController {
    private static Logger log = Logger.getLogger(KoTimeController.class.toString());


    @GetMapping("/getConfig")
    @ResponseBody
    @Auth
    public DefaultConfig getConfig() {
        DefaultConfig config = Context.getConfig();
        return config;
    }

    @GetMapping("/getStatistic")
    @ResponseBody
    @Auth
    public SystemStatistic getStatistic() {
        GraphService graphService = GraphService.getInstance();
        SystemStatistic system = graphService.getRunStatistic();
        return system;
    }

    @GetMapping("/getApis")
    @ResponseBody
    @Auth
    public List<MethodInfo> getApis(String question,String orderBy,String sort) {
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
                return o1.getCallNum().compareTo(o2.getCallNum())* sortValue;
            }else {
                return o1.getAvgRunTime().compareTo(o2.getAvgRunTime())* sortValue;
            }
        });
        return list;
    }

    @GetMapping("/exportApis")
    @ResponseBody
    @Auth
    public void exportApis(String question, String orderBy, String sort, HttpServletResponse response) {
        List<MethodInfo> apis = getApis(question, orderBy, sort);
        response.setCharacterEncoding("utf-8");
        response.addHeader("Content-Disposition", "attachment; filename=interfaces.csv");

        try( OutputStream out = response.getOutputStream();
                BufferedOutputStream bufferedOut = new BufferedOutputStream(out)){
            String line = "序号,类名,方法名,路由,平均响应（ms）,调用次数\n";
            if ("english".equals(Context.getConfig().getLanguage())) {
                line = "No,ClassName,Method,Route,Avg(ms),CallNum\n";
            }
            bufferedOut.write(line.getBytes("utf-8"));
            for (int i = 0; i < apis.size(); i++) {
                MethodInfo methodInfo = apis.get(i);
                line = (i+1)+","+methodInfo.getClassName()+","+methodInfo.getMethodName()+"(),"+methodInfo.getRouteName()+","+methodInfo.getAvgRunTime()+","+methodInfo.getCallNum()+"\n";
                bufferedOut.write(line.getBytes("utf-8"));
            }
            bufferedOut.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @GetMapping("/getParamGraph")
    @ResponseBody
    @Auth
    public Map<String, ParamMetric> getParamGraph(String methodId) {
        GraphService graphService = GraphService.getInstance();
        Map<String, ParamMetric> list = graphService.getMethodParamGraph(methodId);
        return list;
    }

    @GetMapping("/getApiTips")
    @ResponseBody
    @Auth
    public List<String> getApiTips(String question) {
        GraphService graphService = GraphService.getInstance();
        List<String> list = graphService.getCondidates(question);
        return list;
    }


    @GetMapping("/getExceptions")
    @ResponseBody
    @Auth
    public List<ExceptionNode> getExceptions() {
        GraphService graphService = GraphService.getInstance();
        List<ExceptionNode> exceptionList = graphService.getExceptions();
        return exceptionList;
    }

    @GetMapping("/getTree")
    @ResponseBody
    @Auth
    public MethodInfo getTree(String methodName) {
        GraphService graphService = GraphService.getInstance();
        MethodInfo tree = graphService.getTree(methodName);
        return tree;
    }

    @GetMapping("/getMethodsByExceptionId")
    @ResponseBody
    @Auth
    public List<ExceptionInfo> getMethodsByExceptionId(String exceptionId, String message) {
        GraphService graphService = GraphService.getInstance();
        List<ExceptionInfo> exceptionInfos = graphService.getExceptionInfos(exceptionId, message);
        return exceptionInfos;
    }

    @PostMapping("/updateConfig")
    @ResponseBody
    @Auth
    public boolean updateConfig(@RequestBody DefaultConfig config) {
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
        return true;
    }

    @PostMapping("/updateClass")
    @ResponseBody
    @Auth
    public Map updateClass(@RequestParam("classFile") MultipartFile classFile, String className) {
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
        final ClassService classService = ClassService.getInstance();
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

    @GetMapping("/getCpuInfo")
    @ResponseBody
    @Auth
    public CpuInfo getCpuInfo() {
        SysUsageService usageService = SysUsageService.newInstance();
        CpuInfo cpuInfo = usageService.getCpuInfo();
        return cpuInfo;
    }

    @GetMapping("/getHeapMemoryInfo")
    @ResponseBody
    @Auth
    public HeapMemoryInfo getHeapMemoryInfo() {
        SysUsageService usageService = SysUsageService.newInstance();
        HeapMemoryInfo heapMemoryInfo = usageService.getHeapMemoryInfo();
        return heapMemoryInfo;
    }

    @GetMapping("/getPhysicalMemoryInfo")
    @ResponseBody
    @Auth
    public PhysicalMemoryInfo getPhysicalMemoryInfo() {
        SysUsageService usageService = SysUsageService.newInstance();
        PhysicalMemoryInfo physicalMemoryInfo = usageService.getPhysicalMemoryInfo();
        return physicalMemoryInfo;
    }

    @PostMapping("/clearData")
    @ResponseBody
    @Auth
    public boolean clearData() {
        GraphService graphService = GraphService.getInstance();
        graphService.clearAll();
        return true;
    }

    @GetMapping("/getThreadsInfo")
    @ResponseBody
    @Auth
    public Map getThreadsInfo(String state) {
        ThreadUsageService usageService = ThreadUsageService.newInstance();
        List<ThreadInfo> threads = usageService.getThreads();
        threads = threads.stream().sorted(COMPARATOR).collect(Collectors.toList());

        Map<String, Long> stateCounting = threads.stream().collect(Collectors.groupingBy(ThreadInfo::getState, Collectors.counting()));
        stateCounting.put("all", (long) threads.size());

        Map map = new HashMap();
        map.put("statistics", stateCounting);
        if (StringUtils.hasText(state)) {
            threads = threads.stream().filter(a -> a.getState().equals(state)).collect(Collectors.toList());
        }
        map.put("threads", threads);
        return map;
    }

    @PostMapping("/updateDynamicProperties")
    @ResponseBody
    @Auth
    public boolean updateDynamicProperties(@RequestBody TextParam textParam) {
        if (!StringUtils.hasText(textParam.getText())) {
            return false;
        }
        String[] textSplit = textParam.getText().trim().split("\n");
        Properties dynamicProperties = Context.getDynamicProperties();
        for (String line : textSplit) {
            line = line.trim();
            if (line.length() == 0 || line.startsWith("#") || line.startsWith("//")) {
                continue;
            }
            int i = line.indexOf("=");
            if (i < 1) {
                continue;
            }
            String propertyStr = line.substring(0, i).trim();
            String valueStr = line.substring(i + 1).trim();
            log.info("updated property: " + propertyStr + "=(" + dynamicProperties.get(propertyStr) + "->" + valueStr + ")");
            dynamicProperties.setProperty(propertyStr, valueStr);
        }

        return true;
    }

    @GetMapping("/getDynamicProperties")
    @ResponseBody
    @Auth
    public Map getDynamicProperties() {
        Map map = new HashMap();
        map.put("state", 0);
        map.put("message", "文件不能为空");
        Properties dynamicProperties = Context.getDynamicProperties();
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : dynamicProperties.stringPropertyNames()) {
            String value = dynamicProperties.getProperty(key);
            if (value != null) {
                stringBuilder.append(key + "=" + value + "\n");
            }
        }
        map.put("data", stringBuilder.toString());
        return map;
    }
}
