package cn.langpy.kotime.data;

import cn.langpy.kotime.model.*;
import cn.langpy.kotime.service.GraphService;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

/**
 * zhangchang
 */
// TODO: 2021-05-04 waiting to code
public class MysqlBase implements GraphService {
    @Override
    public void addMethodNode(MethodNode methodNode) {

    }

    @Override
    public void addParamAnalyse(String methodId, Parameter[] names, Object[] values, double v) {

    }

    @Override
    public void addExceptionNode(ExceptionNode exceptionNode) {

    }

    @Override
    public MethodInfo getTree(String methodId) {
        return null;
    }

    @Override
    public Map<String, ParamMetric> getMethodParamGraph(String methodId) {
        return null;
    }

    @Override
    public SystemStatistic getRunStatistic() {
        return null;
    }

    @Override
    public List<MethodInfo> getControllers() {
        return null;
    }

    @Override
    public List<String> getCondidates(String question) {
        return null;
    }

    @Override
    public List<MethodInfo> searchMethods(String question) {
        return null;
    }

    @Override
    public List<MethodInfo> getChildren(String methodId) {
        return null;
    }

    @Override
    public List<ExceptionInfo> getExceptionInfos(String exceptionId) {
        return null;
    }

    @Override
    public List<ExceptionInfo> getExceptions(String methodId) {
        return null;
    }

    @Override
    public List<ExceptionNode> getExceptions() {
        return null;
    }

    @Override
    public MethodRelation addMethodRelation(MethodNode sourceMethodNode, MethodNode targetMethodNode) {
        return null;
    }

    @Override
    public ExceptionRelation addExceptionRelation(MethodNode sourceMethodNode, ExceptionNode exceptionNode) {
        return null;
    }
}
