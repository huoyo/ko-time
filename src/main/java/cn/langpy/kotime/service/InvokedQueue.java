package cn.langpy.kotime.service;

import cn.langpy.kotime.handler.InvokedHandler;
import cn.langpy.kotime.model.InvokedInfo;
import cn.langpy.kotime.util.Context;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

public class InvokedQueue {
    public static Logger log = Logger.getLogger(InvokedQueue.class.toString());

    private volatile static ConcurrentLinkedQueue<InvokedInfo> queue = new ConcurrentLinkedQueue();

    public static void add(InvokedInfo invokedInfo) {
        queue.add(invokedInfo);
    }

    public static void onInveked() {
        while (true) {
            try {
                if (queue.isEmpty()) {
                    continue;
                }
                InvokedInfo poll = queue.poll();
                if (poll==null) {
                    continue;
                }
                for (InvokedHandler invokedHandler : Context.getInvokedHandlers()) {
                    invokedHandler.onInvoked(poll.getCurrent(), poll.getParent(), poll.getNames(), poll.getValues());
                    if (null != poll.getException()) {
                        invokedHandler.onException(poll.getCurrent(), poll.getParent(), poll.getException(), poll.getNames(), poll.getValues());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
