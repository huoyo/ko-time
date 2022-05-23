package cn.langpy.kotime.service;

import cn.langpy.kotime.annotation.KoListener;
import cn.langpy.kotime.model.MethodNode;

import java.lang.reflect.Parameter;

/**
 * zhangchang
 */
public interface InvokedHandler {
    /**
     * return the results of invoking a method.
     */
    void onInvoked(MethodNode current, MethodNode parent, Parameter[] names, Object[] values);
}
