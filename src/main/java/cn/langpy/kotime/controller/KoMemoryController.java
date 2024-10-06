package cn.langpy.kotime.controller;

import cn.langpy.kotime.annotation.Auth;
import cn.langpy.kotime.model.*;
import cn.langpy.kotime.service.core.SystemService;
import cn.langpy.kotime.service.metric.MemoryMetricService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.logging.Logger;


/**
 * zhangchang
 */
@Controller
@RequestMapping("/koTime/memories")
public class KoMemoryController {
    private static Logger log = Logger.getLogger(KoMemoryController.class.toString());

    @GetMapping("/heap")
    @ResponseBody
    @Auth
    public KoResult heap() {
        MemoryMetricService instance = SystemService.getInstance(MemoryMetricService.class);
        JvmMemoryInfo heapMemoryInfo = instance.getHeapMemoryInfo();
        return KoResult.success(heapMemoryInfo);
    }

    @GetMapping("/nonHeap")
    @ResponseBody
    @Auth
    public KoResult nonHeap() {
        MemoryMetricService instance = SystemService.getInstance(MemoryMetricService.class);
        JvmMemoryInfo heapMemoryInfo = instance.getNonHeapMemoryInfo();
        return KoResult.success(heapMemoryInfo);
    }

    @GetMapping("/physical")
    @ResponseBody
    @Auth
    public KoResult physical() {
        MemoryMetricService instance = SystemService.getInstance(MemoryMetricService.class);
        PhysicalMemoryInfo heapMemoryInfo = instance.getPhysicalMemoryInfo();
        return KoResult.success(heapMemoryInfo);
    }

    @GetMapping("/heap/export")
    @ResponseBody
    @Auth
    public void heapExport(Boolean live, HttpServletResponse response) {
        live = live == null ? false : live;
        MemoryMetricService instance = SystemService.getInstance(MemoryMetricService.class);
        String heapDumpFile = instance.getHeapDumpFile(live);
        if (heapDumpFile == null) {
            throw new RuntimeException("Can not dumpheap file!");
        }
        log.info(heapDumpFile);
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + instance.getHeapDumpFileName(live));
        try (OutputStream out = response.getOutputStream();
             BufferedOutputStream bufferedOut = new BufferedOutputStream(out);
             FileInputStream fileInputStream = new FileInputStream(heapDumpFile);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                bufferedOut.write(buffer, 0, bytesRead);
            }
            bufferedOut.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
