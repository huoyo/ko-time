package cn.langpy.kotime.service;

import cn.langpy.kotime.annotation.KoListener;
import cn.langpy.kotime.model.MethodNode;
import cn.langpy.kotime.util.Context;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;

@KoListener
public final class KoInvokedHandler implements InvokedHandler{
    @Override
    public void onInvoked(MethodNode current, MethodNode parent, Parameter[] names, Object[] values) {
        GraphService graphService = GraphService.getInstance();
        graphService.addMethodNode(parent);
        graphService.addMethodNode(current);
        graphService.addMethodRelation(parent, current);
        if (Context.getConfig().getParamAnalyse()) {
            graphService.addParamAnalyse(current.getId(),names, values,current.getValue());
        }
    }
}
