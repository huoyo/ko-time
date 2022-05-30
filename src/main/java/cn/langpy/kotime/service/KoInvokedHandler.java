package cn.langpy.kotime.service;

import cn.langpy.kotime.annotation.KoListener;
import cn.langpy.kotime.constant.KoConstant;
import cn.langpy.kotime.constant.KoSqlConstant;
import cn.langpy.kotime.data.DataBase;
import cn.langpy.kotime.handler.InvokedHandler;
import cn.langpy.kotime.model.ExceptionNode;
import cn.langpy.kotime.model.MethodNode;
import cn.langpy.kotime.util.Common;
import cn.langpy.kotime.util.Context;

import java.lang.reflect.Parameter;
import java.util.logging.Logger;

@KoListener
public final class KoInvokedHandler implements InvokedHandler {
    public static Logger log = Logger.getLogger(KoInvokedHandler.class.toString());


    @Override
    public void onInvoked(MethodNode current, MethodNode parent, Parameter[] names, Object[] values) {
        GraphService graphService = GraphService.getInstance();
        graphService.addMethodNode(parent);
        graphService.addMethodNode(current);
        graphService.addMethodRelation(parent, current);
        if (Context.getConfig().getParamAnalyse()) {
            graphService.addParamAnalyse(current.getId(), names, values, current.getValue());
        }
        if (Context.getConfig().getLogEnable()) {
            Common.showLog(current);
        }
    }

    @Override
    public void onException(MethodNode current, MethodNode parent, ExceptionNode exception, Parameter[] names, Object[] values) {
        GraphService graphService = GraphService.getInstance();
        graphService.addMethodNode(current);
        graphService.addExceptionNode(exception);
        graphService.addExceptionRelation(current, exception);
    }
}
