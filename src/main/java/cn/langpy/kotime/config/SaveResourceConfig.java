package cn.langpy.kotime.config;

import cn.langpy.kotime.model.MethodNode;
import cn.langpy.kotime.service.GraphService;
import cn.langpy.kotime.util.Context;
import cn.langpy.kotime.util.KoUtil;
import cn.langpy.kotime.util.MethodType;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Map;
import java.util.logging.Logger;


@Component
public class SaveResourceConfig implements CommandLineRunner {
    private static Logger log = Logger.getLogger(SaveResourceConfig.class.toString());
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private AspectJExpressionPointcutAdvisor aspectJExpressionPointcutAdvisor;
    @Override
    public void run(String... args) throws Exception {
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
        acquireControllers();
    }
    private void acquireControllers() {
        RequestMappingHandlerMapping handlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        GraphService graphService = GraphService.getInstance();
        MethodMatcher methodMatcher = aspectJExpressionPointcutAdvisor.getPointcut().getMethodMatcher();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> methodEntry : handlerMethods.entrySet()) {
            HandlerMethod handlerMethod = methodEntry.getValue();
            boolean matches = methodMatcher.matches(handlerMethod.getMethod(), handlerMethod.getClass());
            if (matches) {
                MethodNode methodNode = toMethodNode(handlerMethod);
                graphService.addMethodNode(methodNode);
            }
        }
    }

    private MethodNode toMethodNode(HandlerMethod method) {
        Class<?> beanType = method.getBeanType();
        RequestMapping requestMapping = beanType.getAnnotation(RequestMapping.class);
        String[] cvalues = requestMapping.value();
        String classRoute = "";
        if (cvalues != null && cvalues.length > 0) {
            classRoute = cvalues[0];
        }
        RequestMapping methodAnnotation = method.getMethodAnnotation(RequestMapping.class);
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
