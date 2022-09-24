package cn.langpy.kotime.service;

import cn.langpy.kotime.model.MethodNode;
import cn.langpy.kotime.util.Context;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class EmailSendService extends JavaMailSenderImpl {
    private static Logger log = Logger.getLogger(EmailSendService.class.toString());

    private static ConcurrentMap<String, Integer> redMethods = new ConcurrentHashMap<>();
    private final static ExecutorService emailExecutors = Executors.newFixedThreadPool(5);

    @Value("${ko-time.mail-receivers:}")
    private String receivers;
    @Value("${ko-time.mail-threshold:4}")
    private Integer threshold;
    @Value("${ko-time.data-prefix:}")
    private String dataPrefix;
    @Value("${ko-time.mail-user:}")
    private String user;

    public void sendNoticeAsync(MethodNode current) {
        emailExecutors.submit(() -> sendNotice(current));
    }

    public void sendNotice(MethodNode current) {
        if (!StringUtils.hasText(receivers)) {
            return;
        }
        if (redMethods.containsKey(current.getId())) {
            int n = redMethods.get(current.getId());
            n += 1;
            if (n >= threshold) {
                this.send(createMessage(current));
                redMethods.put(current.getId(), -2000);
            } else {
                redMethods.put(current.getId(), n);
            }
        } else {
            redMethods.put(current.getId(), 1);
            if (threshold == 1) {
                this.send(createMessage(current));
            }
        }
    }

    private MimeMessage createMessage(MethodNode current) {
        MimeMessage mimeMessage = null;
        try {
            mimeMessage = this.createMimeMessage();
            configMessage(current, mimeMessage);
        } catch (MessagingException e) {
            log.severe("Error email message!");
        }
        return mimeMessage;
    }

    private void configMessage(MethodNode current, MimeMessage mimeMessage) throws MessagingException {
        String[] receiversArray = receivers.split(",");
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setSubject("KoTime耗时预警-" + dataPrefix + "-" + current.getId());
        messageHelper.setFrom(user);
        messageHelper.setTo(receiversArray);
        messageHelper.setSentDate(new Date());
        messageHelper.setText(createNoticeTemplate(current), true);
    }

    private String createNoticeTemplate(MethodNode current) {
        return "<h4>您有一个方法耗时有" + threshold + "次超过了阈值（" + Context.getConfig().getThreshold() + "），详情如下：</h4>\n" +
                "<div style=\"background-color: #fafdfd;border-radius: 5px;width: 500px;padding: 10px;box-shadow: #75f1bf 2px 2px 2px 2px\">\n" +
                "    <div>项目：" + dataPrefix + "</div>\n" +
                "    <div>类名：" + current.getClassName() + "</div>\n" +
                "    <div>方法：" + current.getMethodName() + "</div>\n" +
                "    <div>最近耗时：" + current.getValue() + " ms</div>\n" +
                "</div>\n" +
                "<p>请前往系统查看</p>\n" +
                "<p>" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "</p>";
    }
}
