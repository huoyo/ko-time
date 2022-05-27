package cn.langpy.kotime.service;

import cn.langpy.kotime.annotation.KoListener;
import cn.langpy.kotime.handler.InvokedHandler;
import cn.langpy.kotime.model.ExceptionNode;
import cn.langpy.kotime.model.MethodNode;
import cn.langpy.kotime.util.Common;
import cn.langpy.kotime.util.Context;

import java.lang.reflect.Parameter;

@KoListener
public final class KoInvokedHandler implements InvokedHandler {

    @Override
    public void onInvoked(MethodNode current, MethodNode parent, Parameter[] names, Object[] values) {
        GraphService graphService = GraphService.getInstance();
//        long start1 = System.currentTimeMillis();
        graphService.addMethodNode(parent);
//        long start2 = System.currentTimeMillis();
        graphService.addMethodNode(current);
//        long start3 = System.currentTimeMillis();
        graphService.addMethodRelation(parent, current);
//        long start4 = System.currentTimeMillis();
//        System.out.println("添加方法耗时："+(start2-start1)/1000.0);
//        System.out.println("添加方法耗时："+(start3-start2)/1000.0);
//        System.out.println("添加关系耗时："+(start4-start3)/1000.0);
        if (Context.getConfig().getParamAnalyse()) {
            graphService.addParamAnalyse(current.getId(),names, values,current.getValue());
        }
        if (Context.getConfig().getLogEnable()){
            Common.showLog(current);
        }
        graphService.close();
    }

    @Override
    public void onInvoked(MethodNode current, MethodNode parent, ExceptionNode exception, Parameter[] names, Object[] values) {
        GraphService graphService = GraphService.getInstance();
        graphService.addMethodNode(current);
        graphService.addExceptionNode(exception);
        graphService.addExceptionRelation(current, exception);
        graphService.close();
    }
}
