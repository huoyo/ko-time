package cn.langpy.kotime.service;

import cn.langpy.kotime.handler.InvokedHandler;
import cn.langpy.kotime.model.InvokedInfo;
import cn.langpy.kotime.util.Context;

import java.util.LinkedList;
import java.util.logging.Logger;

public class InvokedQueue {
    public static Logger log = Logger.getLogger(InvokedQueue.class.toString());

    private volatile static LinkedList<InvokedInfo> linkedList = new LinkedList();

    public static void add(InvokedInfo invokedInfo) {
        linkedList.add(invokedInfo);
    }

    public static void onInveked() {
        while (true) {
            try {
                InvokedInfo poll = null;
                synchronized (linkedList) {
                    if (linkedList.size() > 0) {
                        poll = linkedList.poll();
                    }
                }
                if (null != poll) {
                    for (InvokedHandler invokedHandler : Context.getInvokedHandlers()) {
                        invokedHandler.onInvoked(poll.getCurrent(), poll.getParent(), poll.getNames(), poll.getValues());
                        if (null!=poll.getException()) {
                            invokedHandler.onException(poll.getCurrent(), poll.getParent(),poll.getException(), poll.getNames(), poll.getValues());
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        InvokedQueue.add(new InvokedInfo());
        System.out.println(InvokedQueue.linkedList.size());
        InvokedQueue.linkedList.poll();
        System.out.println(InvokedQueue.linkedList.size());
    }
}
