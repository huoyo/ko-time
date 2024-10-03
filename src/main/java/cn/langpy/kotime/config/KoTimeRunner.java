package cn.langpy.kotime.config;

import cn.langpy.kotime.model.MethodNode;
import cn.langpy.kotime.service.GraphService;
import cn.langpy.kotime.util.Context;
import cn.langpy.kotime.util.KoUtil;
import cn.langpy.kotime.util.MethodType;
import jakarta.annotation.Resource;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.sql.DataSource;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author ccz
 */
@ComponentScan("cn.langpy.kotime")
@AutoConfiguration
public class KoTimeRunner implements ApplicationRunner {
    private static Logger log = Logger.getLogger(KoTimeRunner.class.toString());

    @Resource
    private LoadConfig loadConfig;
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private AspectJExpressionPointcutAdvisor aspectJExpressionPointcutAdvisor;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        loadConfig.initConfig();
        setFinalDataSource();
        acquireControllers();
    }

    private void setFinalDataSource() {
        DataSource dataSource = KoUtil.getDataSource();
        if (null != dataSource) {
            log.info("kotime=>Setting the final DataSource for kotime so that previous DataSources will be invalid.");
            Context.setDataSource(dataSource);
        }
        StringRedisTemplate redisTemplate = KoUtil.getStringRedisTemplate();
        if (null != redisTemplate) {
            log.info("kotime=>Setting the final StringRedisTemplate for kotime so that previous StringRedisTemplate will be invalid.");
            Context.setStringRedisTemplate(redisTemplate);
        }

        if (Context.getConfig().getDataReset()) {
            log.info("kotime=>Deleting all data for kotime.");
            GraphService instance = GraphService.getInstance();
            instance.clearAll();
        }

        KoUtil.clearCaches();
    }
    private void acquireControllers() {
        try {
            Map<String, RequestMappingHandlerMapping> handlerMappings = applicationContext.getBeansOfType(RequestMappingHandlerMapping.class);
            for (Map.Entry<String, RequestMappingHandlerMapping> handlerMappingMap : handlerMappings.entrySet()) {
                RequestMappingHandlerMapping handlerMapping = handlerMappingMap.getValue();
                Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
                GraphService graphService = GraphService.getInstance();
                MethodMatcher methodMatcher = aspectJExpressionPointcutAdvisor.getPointcut().getMethodMatcher();
                for (Map.Entry<RequestMappingInfo, HandlerMethod> methodEntry : handlerMethods.entrySet()) {
                    HandlerMethod handlerMethod = methodEntry.getValue();
                    boolean matches = methodMatcher.matches(handlerMethod.getMethod(), handlerMethod.getClass());
                    if (matches) {
                        MethodNode methodNode = toMethodNode(handlerMethod);
                        if (methodNode == null) {
                            continue;
                        }
                        graphService.addMethodNode(methodNode);
                    }
                }
            }
        } catch (Exception e) {
            log.warning("kotime=>An error occured while loading all controllers:"+e.getMessage());
        }

    }

    private MethodNode toMethodNode(HandlerMethod method) {
        Class<?> beanType = method.getBeanType();
        RequestMapping requestMapping = beanType.getAnnotation(RequestMapping.class);
        if (requestMapping == null) {
            return null;
        }
        String[] cvalues = requestMapping.value();
        String classRoute = "";
        if (cvalues != null && cvalues.length > 0) {
            classRoute = cvalues[0];
        }
        RequestMapping methodAnnotation = method.getMethodAnnotation(RequestMapping.class);
        if (methodAnnotation == null) {
            return null;
        }
        String[] mvalues = methodAnnotation.value();
        String methodRoute = "";
        if (mvalues != null && mvalues.length > 0) {
            methodRoute = mvalues[0];
        }
        String route = classRoute+methodRoute;
        MethodNode methodNode = new MethodNode();
        methodNode.setId(beanType.getName() + "." + method.getMethod().getName());
        methodNode.setClassName(beanType.getName());
        methodNode.setMethodName(method.getMethod().getName());
        methodNode.setName( beanType.getSimpleName()+ "." + method.getMethod().getName());
        methodNode.setRouteName(route);
        methodNode.setMethodType(MethodType.Controller);
        return methodNode;
    }

}
