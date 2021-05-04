package cn.langpy.kotime.service;

import cn.langpy.kotime.model.*;

import java.util.List;

public class MysqlBase implements GraphService {
    @Override
    public void addMethodNode(MethodNode methodNode) {

    }

    @Override
    public void addExceptionNode(ExceptionNode exceptionNode) {

    }

    @Override
    public MethodRelation addMethodRelation(MethodNode sourceMethodNode, MethodNode targetMethodNode) {
        return null;
    }

    @Override
    public ExceptionRelation addExceptionRelation(MethodNode sourceMethodNode, ExceptionNode exceptionNode) {
        return null;
    }

    @Override
    public List<MethodInfo> getControllers() {
        return null;
    }

    @Override
    public List<ExceptionInfo> getExceptions(String methodId) {
        return null;
    }

    @Override
    public List<MethodInfo> getChildren(String methodId) {
        return null;
    }

    @Override
    public SystemStatistic getRunStatistic() {
        return null;
    }

    @Override
    public MethodInfo getTree(String methodId) {
        return null;
    }
}
