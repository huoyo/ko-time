package cn.langpy.kotime.service;

import cn.langpy.kotime.data.MemoryBase;
import cn.langpy.kotime.data.MysqlBase;
import cn.langpy.kotime.model.*;
import cn.langpy.kotime.util.Context;

import java.util.List;

public interface GraphService {

    static GraphService getInstance() {
        GraphService graphService = null;
        if (Context.getConfig().getSaveSaver().equals("memory")) {
            graphService = new MemoryBase();
        } else if (Context.getConfig().getSaveSaver().equals("mysql")) {
            graphService = new MysqlBase();
        }
        return graphService;
    }

    void addMethodNode(MethodNode methodNode);

    void addExceptionNode(ExceptionNode exceptionNode);

    MethodInfo getTree(String methodId);

    SystemStatistic getRunStatistic();

    List<MethodInfo> getControllers();

    List<MethodInfo> getChildren(String methodId);

    List<ExceptionInfo> getExceptionInfos(String exceptionId);

    List<ExceptionInfo> getExceptions(String methodId);

    List<ExceptionNode> getExceptions();

    MethodRelation addMethodRelation(MethodNode sourceMethodNode, MethodNode targetMethodNode);

    ExceptionRelation addExceptionRelation(MethodNode sourceMethodNode, ExceptionNode exceptionNode);


}
