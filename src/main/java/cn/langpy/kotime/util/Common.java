package cn.langpy.kotime.util;

import cn.langpy.kotime.model.MethodNode;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * zhangchang
 */
public class Common {
    private static Logger log = Logger.getLogger(Common.class.toString());

    final static List<Class<?>> baseTypes = Arrays.asList(Integer.class, Double.class, Float.class, String.class, Boolean.class, MultipartFile.class);

    public static String getRoute(MethodInvocation pjp) {
        Class<?> targetClass = pjp.getThis().getClass();
        String[] classRoute = getRouteValue(targetClass);
        StringBuilder routes = new StringBuilder();
        if (classRoute != null && classRoute.length > 0) {
            routes = new StringBuilder(classRoute[0]);
        }
        String[] methodRoute = getRouteValue(pjp.getMethod());
        if (methodRoute == null || methodRoute.length == 0) {
            return null;
        }
        if (methodRoute[0].startsWith("/")) {
            routes.append(methodRoute[0]);
        } else {
            routes.append("/" + methodRoute[0]);
        }
        return routes.toString();
    }

    private static String[] getRouteValue(Class<?> targetClass) {
        RequestMapping methodAnnotationRequest = targetClass.getAnnotation(RequestMapping.class);
        if (methodAnnotationRequest != null) {
            return methodAnnotationRequest.value();
        }
        PostMapping methodAnnotationPost = targetClass.getAnnotation(PostMapping.class);
        if (methodAnnotationPost != null) {
            return methodAnnotationPost.value();
        }
        GetMapping methodAnnotationGet = targetClass.getAnnotation(GetMapping.class);
        if (methodAnnotationGet != null) {
            return methodAnnotationGet.value();
        }
        PutMapping methodAnnotationPut = targetClass.getAnnotation(PutMapping.class);
        if (methodAnnotationPut != null) {
            return methodAnnotationPut.value();
        }
        DeleteMapping methodAnnotationDelete = targetClass.getAnnotation(DeleteMapping.class);
        if (methodAnnotationDelete != null) {
            return methodAnnotationDelete.value();
        }
        return null;
    }

    private static String[] getRouteValue(Method method) {
        RequestMapping methodAnnotationRequest = method.getAnnotation(RequestMapping.class);
        if (methodAnnotationRequest != null) {
            return methodAnnotationRequest.value();
        }
        PostMapping methodAnnotationPost = method.getAnnotation(PostMapping.class);
        if (methodAnnotationPost != null) {
            return methodAnnotationPost.value();
        }
        GetMapping methodAnnotationGet = method.getAnnotation(GetMapping.class);
        if (methodAnnotationGet != null) {
            return methodAnnotationGet.value();
        }
        PutMapping methodAnnotationPut = method.getAnnotation(PutMapping.class);
        if (methodAnnotationPut != null) {
            return methodAnnotationPut.value();
        }
        DeleteMapping methodAnnotationDelete = method.getAnnotation(DeleteMapping.class);
        if (methodAnnotationDelete != null) {
            return methodAnnotationDelete.value();
        }
        return null;
    }

    public static MethodType getMethodType(MethodInvocation pjp) {
        Class<?> targetClass = pjp.getThis().getClass();
        if (targetClass.getAnnotation(Controller.class) != null || targetClass.getAnnotation(RestController.class) != null) {
            return MethodType.Controller;
        } else if (targetClass.getAnnotation(Service.class) != null) {
            return MethodType.Service;
        } else if (targetClass.getAnnotation(Repository.class) != null) {
            return MethodType.Dao;
        }
        String className = pjp.getMethod().getDeclaringClass().getName().toLowerCase();
        if (className.contains("controller")) {
            return MethodType.Controller;
        } else if (className.contains("service")) {
            return MethodType.Service;
        } else if (className.contains("dao") || className.contains("mapper") || className.contains("com.sun.proxy.$Proxy")) {
            return MethodType.Dao;
        } else {
            return MethodType.Others;
        }
    }

    public static MethodType getMethodType(String className) {
        className = className.toLowerCase();
        if (className.contains("controller")) {
            return MethodType.Controller;
        } else if (className.contains("service")) {
            return MethodType.Service;
        } else if (className.contains("dao") || className.contains("mapper") || className.contains("com.sun.proxy.$Proxy")) {
            return MethodType.Dao;
        } else {
            return MethodType.Others;
        }
    }

    public static void showLog(MethodNode current) {
        showLog(current.getId(), current.getValue());
    }

    public static void showLog(String method, double value) {
        if ("chinese".equals(Context.getConfig().getLanguage())) {
            log.info("调用方法=" + method + "()，耗时=" + value + "毫秒");
        } else if ( "english".equals(Context.getConfig().getLanguage())) {
            log.info("method=" + method + "(),runTime=" + value + "ms");
        }
    }

    public static String getPramsStr(Parameter[] names, Object[] values) {
        List<String> params = new ArrayList<>();
        if (names!=null) {
            int namesLen = names.length;
            for (int i = 0; i < namesLen; i++) {
                Class<?> type = names[i].getType();
                if (baseTypes.contains(type)) {
                    if (values[i] != null) {
                        if (values[i] instanceof String) {
                            if (!StringUtils.isEmpty(values[i])) {
                                params.add(names[i].getName());
                            }
                        }else {
                            params.add(names[i].getName());
                        }
                    }
                } else {
                    if (type == HttpServletRequest.class) {
                        continue;
                    }
                    Object valuesI = values[i];
                    if (valuesI==null) {
                        continue;
                    }
                    Field[] declaredFields = valuesI.getClass().getDeclaredFields();
                    for (Field field : declaredFields) {
                        if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                            continue;
                        }
                        try {
                            field.setAccessible(true);
                            Object value = field.get(valuesI);
                            if (value != null) {
                                if (value instanceof String) {
                                    if (!StringUtils.isEmpty(value)) {
                                        params.add(field.getName());
                                    }
                                }else {
                                    params.add(field.getName());
                                }
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }finally {
                            field.setAccessible(false);
                        }
                    }
                }
            }
        }
        String paramsKey = "-";
        if (params.size()>0) {
            paramsKey = String.join("-", params);
        }
        return paramsKey;
    }

}

