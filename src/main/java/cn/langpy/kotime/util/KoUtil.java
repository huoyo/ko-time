package cn.langpy.kotime.util;

import cn.langpy.kotime.constant.KoConstant;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class KoUtil {

    public static void login(String userName) {
        getSession().setAttribute(KoConstant.loginName, userName);
    }

    public static void logout() {
        getSession().removeAttribute(KoConstant.loginName);
    }

    public static void checkLogin() {
        Object userName = getSession().getAttribute(KoConstant.loginName);
        if (null == userName) {
            throw new KoTimeNotLoginException("can not find login information for kotime,please login first!");
        }
    }

    public static boolean isLogin() {
        Object userName = getSession().getAttribute(KoConstant.loginName);
        if (null == userName) {
            return false;
        }
        return true;
    }

    private static HttpSession getSession() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        return session;
    }

}
