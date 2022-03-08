package cn.langpy.kotime.service;

import cn.langpy.kotime.data.MemoryBase;
import cn.langpy.kotime.data.MysqlBase;
import cn.langpy.kotime.model.*;
import cn.langpy.kotime.util.Context;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

/**
 * zhangchang
 */
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

    void addParamAnalyse(String methodId, Parameter[] names, Object[] values, double v);

    void addExceptionNode(ExceptionNode exceptionNode);

    MethodInfo getTree(String methodId);

    Map<String, ParamMetric> getMethodParamGraph(String methodId);

    SystemStatistic getRunStatistic();

    List<MethodInfo> searchMethods(String question);

    List<MethodInfo> getControllers();

    List<String> getCondidates(String question);

    List<MethodInfo> getChildren(String methodId);

    List<ExceptionInfo> getExceptionInfos(String exceptionId);

    List<ExceptionInfo> getExceptions(String methodId);

    List<ExceptionNode> getExceptions();

    MethodRelation addMethodRelation(MethodNode sourceMethodNode, MethodNode targetMethodNode);

    ExceptionRelation addExceptionRelation(MethodNode sourceMethodNode, ExceptionNode exceptionNode);


}
