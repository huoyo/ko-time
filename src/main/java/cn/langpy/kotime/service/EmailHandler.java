package cn.langpy.kotime.service;

import cn.langpy.kotime.annotation.KoListener;
import cn.langpy.kotime.handler.InvokedHandler;
import cn.langpy.kotime.model.MethodNode;
import cn.langpy.kotime.util.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;

import java.lang.reflect.Parameter;
import java.util.logging.Logger;

@KoListener
@Lazy
public class EmailHandler implements InvokedHandler {
    private static Logger log = Logger.getLogger(EmailHandler.class.toString());
    @Value("${ko-time.mail-scope:Controller}")
    private String mailScope;
    @Autowired
    private EmailSendService emailSendService;

    @Override
    public void onInvoked(MethodNode current, MethodNode parent, Parameter[] names, Object[] values) {
        if (current == null || current.getValue() < Context.getConfig().getThreshold()) {
            return;
        }
        if (mailScope.equals("All") || current.getMethodType().name().equals(mailScope)) {
            emailSendService.sendNoticeAsync(current);
        }
    }
}
