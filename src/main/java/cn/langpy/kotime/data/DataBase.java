package cn.langpy.kotime.data;

import cn.langpy.kotime.constant.KoSqlConstant;
import cn.langpy.kotime.model.*;
import cn.langpy.kotime.service.GraphService;
import cn.langpy.kotime.util.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * zhangchang
 */
@Component("database")
@Lazy
public class DataBase implements GraphService {
    public static Logger log = Logger.getLogger(DataBase.class.toString());

    private Connection readConnection;
    private Connection writeConnection;

    public  DataBase() {
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    try {
                        if (null!=readConnection) {
                            readConnection.close();
                        }
                        if (null!=writeConnection) {
                            writeConnection.close();
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }finally {
                        log.info("kotime=>closed database connections...");
                    }
                })
        );
    }

    public Connection getReadConnection() {
        try {
            if (null == readConnection || readConnection.isClosed()) {
                DataSource dataSource = Context.getDataSource();
                if (null==dataSource) {
                    throw new DataBaseException("`ko-time.saver=database` needs a DataSource for MySQl or Oracle");
                }else {
                    readConnection = dataSource.getConnection();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return readConnection;
    }

    public Connection getWriteConnection() {
        try {
            if (null == writeConnection || writeConnection.isClosed()) {
                DataSource dataSource = Context.getDataSource();
                if (null==dataSource) {
                    throw new DataBaseException("`ko-time.saver=database` needs a DataSource for MySQl or Oracle");
                }else {
                    writeConnection = dataSource.getConnection();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return writeConnection;
    }

    @Override
    public void addMethodNode(MethodNode methodNode) {
        if (null == methodNode) {
            return;
        }
//        List<Map<String, Object>> query = DataBaseUtil.query(getWriteConnection(),KoSqlConstant.queryMethod, new Object[]{methodNode.getId()});
        boolean existsById = DataBaseUtil.existsById(getWriteConnection(),KoSqlConstant.queryMethod, methodNode.getId());
        if (!existsById) {
            Object[] params = new Object[]{
                    methodNode.getId(),
                    methodNode.getName(),
                    methodNode.getClassName(),
                    methodNode.getMethodName(),
                    methodNode.getRouteName(),
                    methodNode.getMethodType().name()
            };
            DataBaseUtil.insert(getWriteConnection(),KoSqlConstant.addMethod, params);
        } else {
            if (methodNode.getMethodType() == MethodType.Controller && !StringUtils.isEmpty(methodNode.getRouteName())) {
                Object[] params = new Object[]{
                        methodNode.getName(),
                        methodNode.getClassName(),
                        methodNode.getMethodName(),
                        methodNode.getRouteName(),
                        methodNode.getMethodType().name(),
                        methodNode.getId(),
                };
                DataBaseUtil.update(getWriteConnection(),KoSqlConstant.updateMethod, params);
            }
        }
    }


    @Override
    public synchronized void addExceptionNode(ExceptionNode exceptionNode) {
//        List<Map<String, Object>> query = DataBaseUtil.query(getWriteConnection(),KoSqlConstant.queryException, new Object[]{exceptionNode.getId()});
        boolean existsById = DataBaseUtil.existsById(getWriteConnection(),KoSqlConstant.queryException, exceptionNode.getId());
        if (!existsById) {
            Object[] params = new Object[]{
                    exceptionNode.getId(),
                    exceptionNode.getName(),
                    exceptionNode.getClassName(),
                    exceptionNode.getMessage()
            };
            DataBaseUtil.insert(getWriteConnection(),KoSqlConstant.addException, params);
        }
    }


    @Override
    public synchronized MethodRelation addMethodRelation(MethodNode sourceMethodNode, MethodNode targetMethodNode) {
        if (null == sourceMethodNode || null == targetMethodNode) {
            return null;
        }
        if (sourceMethodNode.getId().equals(targetMethodNode.getId())) {
            return null;
        }
        try {
            List<Map<String, Object>> query = DataBaseUtil.query(getWriteConnection(),KoSqlConstant.queryMethodRe, new Object[]{sourceMethodNode.getId() + targetMethodNode.getId()});
            if (query.size() > 0) {
                Map<String, Object> old = query.get(0);
                double oldAvg = Double.valueOf(old.get("avg_run_time") + "");
                double oldMax = Double.valueOf(old.get("max_run_time") + "");
                double oldMin = Double.valueOf(old.get("min_run_time") + "");
                BigDecimal bg = BigDecimal.valueOf((targetMethodNode.getValue() + oldAvg) / 2.0);
                double avg = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                double max = targetMethodNode.getValue() > oldMax ? targetMethodNode.getValue() : oldMax;
                double min = targetMethodNode.getValue() < oldMin ? targetMethodNode.getValue() : oldMin;
                Object[] params = new Object[]{
                        sourceMethodNode.getId(),
                        targetMethodNode.getId(),
                        avg,
                        max,
                        min,
                        sourceMethodNode.getId() + targetMethodNode.getId()
                };
                DataBaseUtil.update(getWriteConnection(),KoSqlConstant.updateMethodRe, params);
                return null;
            } else {
                Object[] params = new Object[]{
                        sourceMethodNode.getId() + targetMethodNode.getId(),
                        sourceMethodNode.getId(),
                        targetMethodNode.getId(),
                        targetMethodNode.getValue(),
                        targetMethodNode.getValue(),
                        targetMethodNode.getValue()
                };
                DataBaseUtil.insert(getWriteConnection(),KoSqlConstant.addMethodRe, params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public synchronized ExceptionRelation addExceptionRelation(MethodNode sourceMethodNode, ExceptionNode exceptionNode) {
//        List<Map<String, Object>> query = DataBaseUtil.query(getWriteConnection(),KoSqlConstant.queryExceptionRe, new Object[]{sourceMethodNode.getId() + exceptionNode.getId()});
        boolean existsById = DataBaseUtil.existsById(getWriteConnection(),KoSqlConstant.queryExceptionRe, sourceMethodNode.getId() + exceptionNode.getId());
        if (!existsById) {
            Object[] params = new Object[]{
                    sourceMethodNode.getId() + exceptionNode.getId(),
                    sourceMethodNode.getId(),
                    exceptionNode.getId(),
                    exceptionNode.getValue()
            };
            DataBaseUtil.insert(getWriteConnection(),KoSqlConstant.addExceptionRe, params);
        }
        return null;
    }

    @Override
    public synchronized void addParamAnalyse(String methodId, Parameter[] names, Object[] values, double v) {
        String paramsKey = Common.getPramsStr(names, values);
        List<Map<String, Object>> query = DataBaseUtil.query(getWriteConnection(),KoSqlConstant.queryParamsAna, new Object[]{methodId, paramsKey});
        if (query.size() == 0) {
            Object[] params = new Object[]{
                    methodId,
                    paramsKey,
                    v,
                    v,
                    v
            };
            DataBaseUtil.insert(getWriteConnection(),KoSqlConstant.addParamsAna, params);
        } else {
            Map<String, Object> old = query.get(0);
            double oldAvg = Double.valueOf(old.get("avg_run_time") + "");
            double oldMax = Double.valueOf(old.get("max_run_time") + "");
            double oldMin = Double.valueOf(old.get("min_run_time") + "");
            BigDecimal bg = BigDecimal.valueOf((v + oldAvg) / 2.0);
            double avg = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            double max = v > oldMax ? v : oldMax;
            double min = v < oldMin ? v : oldMin;
            Object[] params = new Object[]{
                    avg,
                    max,
                    min,
                    methodId,
                    paramsKey
            };
            DataBaseUtil.update(getWriteConnection(),KoSqlConstant.updateParamsAna, params);
        }
    }

    @Override
    public MethodInfo getTree(String methodId) {
        MethodInfo rootInfo = new MethodInfo();
        List<MethodNode> methodNodes = DataBaseUtil.query(getReadConnection(),KoSqlConstant.queryMethod, new Object[]{methodId}, MethodNode.class);
        if (methodNodes.size() == 0) {
            return rootInfo;
        }

        MethodNode methodNode = methodNodes.get(0);
        rootInfo.setId(methodNode.getId());
        rootInfo.setName(methodNode.getName());
        rootInfo.setClassName(methodNode.getClassName());
        rootInfo.setMethodName(methodNode.getMethodName());
        rootInfo.setMethodType(methodNode.getMethodType());
        rootInfo.setRouteName(methodNode.getRouteName());

        List<MethodRelation> relations = DataBaseUtil.query(getReadConnection(),KoSqlConstant.queryMethodReByTarget, new Object[]{methodId}, MethodRelation.class);
        if (relations.size() == 0) {
            return rootInfo;
        }
        MethodRelation methodRelation = relations.get(0);

        rootInfo.setValue(methodRelation.getAvgRunTime());
        rootInfo.setAvgRunTime(methodRelation.getAvgRunTime());
        rootInfo.setMaxRunTime(methodRelation.getMaxRunTime());
        rootInfo.setMinRunTime(methodRelation.getMinRunTime());
        List<ExceptionInfo> exceptionInfos = getExceptions(methodId);
        rootInfo.setExceptionNum(exceptionInfos.size());
        rootInfo.setExceptions(exceptionInfos);
        List<String> methodInfos = new ArrayList<>();
        recursionMethod(rootInfo, methodInfos);
        methodInfos.clear();
        return rootInfo;
    }

    public void recursionMethod(MethodInfo rootInfo, List<String> methodInfos) {
        List<MethodInfo> children = getChildren(rootInfo.getId());
        if (children != null && children.size() > 0) {
            if (!methodInfos.contains(rootInfo.getId())) {
                methodInfos.add(rootInfo.getId());
                rootInfo.setChildren(children);
                for (MethodInfo child : children) {
                    recursionMethod(child, methodInfos);
                }
            }

        }

    }

    @Override
    public Map<String, ParamMetric> getMethodParamGraph(String methodId) {
        Map<String, ParamMetric> paramMetricMap = new HashMap<>();
        List<ParamAna> paramAnas = DataBaseUtil.query(getReadConnection(),KoSqlConstant.queryParamsAnaBySource, new Object[]{methodId}, ParamAna.class);

        if (paramAnas.size() == 0) {
            return paramMetricMap;
        }
        for (ParamAna paramAna : paramAnas) {
            if (!paramMetricMap.containsKey(paramAna.getSourceId())) {
                ParamMetric paramMetric = new ParamMetric();
                paramMetric.setAvgRunTime(paramAna.getAvgRunTime());
                paramMetric.setMaxRunTime(paramAna.getMaxRunTime());
                paramMetric.setMinRunTime(paramAna.getMinRunTime());
                paramMetricMap.put(paramAna.getParams(), paramMetric);
            }
        }
        return paramMetricMap;
    }

    @Override
    public SystemStatistic getRunStatistic() {
        SystemStatistic systemStatistic = new SystemStatistic();
        List<MethodInfo> controllerApis = getControllers();
        if (null == controllerApis || controllerApis.size() == 0) {
            return systemStatistic;
        }
        int delayNum = (int) controllerApis.stream().filter(controllerApi -> controllerApi.getAvgRunTime() >= Context.getConfig().getThreshold()).count();
        systemStatistic.setDelayNum(delayNum);
        int normalNum = (int) controllerApis.stream().filter(controllerApi -> controllerApi.getAvgRunTime() < Context.getConfig().getThreshold()).count();
        systemStatistic.setNormalNum(normalNum);
        int totalNum = (int) controllerApis.stream().count();
        systemStatistic.setTotalNum(totalNum);
        Double max = controllerApis.stream().map(api -> api.getAvgRunTime()).max(Double::compareTo).get();
        Double min = controllerApis.stream().map(api -> api.getAvgRunTime()).min(Double::compareTo).get();
        Double avg = controllerApis.stream().map(api -> api.getAvgRunTime()).collect(Collectors.averagingDouble(Double::doubleValue));
        BigDecimal bg = BigDecimal.valueOf(avg);
        avg = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        systemStatistic.setMaxRunTime(max);
        systemStatistic.setMinRunTime(min);
        systemStatistic.setAvgRunTime(avg);
        return systemStatistic;
    }

    @Override
    public List<MethodInfo> getControllers() {
        List<MethodInfo> methodInfos = new ArrayList<>();
        List<MethodInfo> controllers = DataBaseUtil.query(getReadConnection(),KoSqlConstant.queryController, new Object[]{MethodType.Controller.name()}, MethodInfo.class);
        for (MethodInfo methodNode : controllers) {
            String id = methodNode.getId();
            List<MethodRelation> relations = DataBaseUtil.query(getReadConnection(),KoSqlConstant.queryMethodReByTarget, new Object[]{id}, MethodRelation.class);
            if (relations.size() == 0) {
                continue;
            }
            MethodRelation relation = relations.get(0);
            MethodInfo methodInfo = new MethodInfo();
            methodInfo.setId(methodNode.getId());
            methodInfo.setName(methodNode.getName());
            methodInfo.setClassName(methodNode.getClassName());
            methodInfo.setMethodName(methodNode.getMethodName());
            methodInfo.setMethodType(methodNode.getMethodType());
            methodInfo.setRouteName(methodNode.getRouteName());
            methodInfo.setValue(relation.getAvgRunTime());
            methodInfo.setAvgRunTime(relation.getAvgRunTime());
            methodInfo.setMaxRunTime(relation.getMaxRunTime());
            methodInfo.setMinRunTime(relation.getMinRunTime());
            if (!methodInfos.contains(methodInfo)) {
                methodInfos.add(methodInfo);
            }
        }
        return methodInfos;
    }

    @Override
    public List<String> getCondidates(String question) {
        List<MethodNode> methodNodes = DataBaseUtil.query(getReadConnection(),KoSqlConstant.queryMethodLikeName, new Object[]{"%" + question + "%"}, MethodNode.class);
        List<String> methodInfos = new ArrayList<>();
        if (methodNodes.size() > 0) {
            methodInfos = methodNodes.stream().map(MethodNode::getName).collect(toList());
        }
        return methodInfos;
    }

    @Override
    public List<MethodInfo> searchMethods(String question) {
        List<MethodInfo> methodInfos = new ArrayList<>();
        List<MethodNode> methodNodes = DataBaseUtil.query(getReadConnection(),KoSqlConstant.queryMethodLikeName, new Object[]{"%" + question + "%"}, MethodNode.class);
        for (MethodNode methodNode : methodNodes) {
                String id = methodNode.getId();
                List<MethodRelation> relations = DataBaseUtil.query(getReadConnection(),KoSqlConstant.queryMethodReByTarget, new Object[]{id}, MethodRelation.class);
                if (relations.size() == 0) {
                    continue;
                }
                MethodRelation relation = relations.get(0);
                MethodInfo methodInfo = new MethodInfo();
                methodInfo.setId(methodNode.getId());
                methodInfo.setName(methodNode.getName());
                methodInfo.setClassName(methodNode.getClassName());
                methodInfo.setMethodName(methodNode.getMethodName());
                methodInfo.setMethodType(methodNode.getMethodType());
                methodInfo.setRouteName(methodNode.getRouteName());
                methodInfo.setValue(relation.getAvgRunTime());
                methodInfo.setAvgRunTime(relation.getAvgRunTime());
                methodInfo.setMaxRunTime(relation.getMaxRunTime());
                methodInfo.setMinRunTime(relation.getMinRunTime());
                if (!methodInfos.contains(methodInfo)) {
                    methodInfos.add(methodInfo);
                }
        }
        return methodInfos;
    }

    @Override
    public List<MethodInfo> getChildren(String methodId) {

        List<MethodRelation> relations = DataBaseUtil.query(getReadConnection(),KoSqlConstant.queryMethodReBySource, new Object[]{methodId}, MethodRelation.class);

        List<MethodInfo> methodInfos = new ArrayList<>();
        for (MethodRelation methodRelation : relations) {
            List<MethodNode> methodNodes = DataBaseUtil.query(getReadConnection(),KoSqlConstant.queryMethod, new Object[]{methodRelation.getTargetId()}, MethodNode.class);
            if (methodNodes.size() == 0) {
                continue;
            }
            MethodNode methodNode = methodNodes.get(0);
            MethodInfo methodInfo = new MethodInfo();
            methodInfo.setId(methodNode.getId());
            methodInfo.setName(methodNode.getName());
            methodInfo.setClassName(methodNode.getClassName());
            methodInfo.setMethodName(methodNode.getMethodName());
            methodInfo.setRouteName(methodNode.getRouteName());
            methodInfo.setMethodType(methodNode.getMethodType());
            methodInfo.setValue(methodRelation.getAvgRunTime());
            methodInfo.setAvgRunTime(methodRelation.getAvgRunTime());
            methodInfo.setMaxRunTime(methodRelation.getMaxRunTime());
            methodInfo.setMinRunTime(methodRelation.getMinRunTime());

            List<ExceptionInfo> exceptionInfos = getExceptions(methodNode.getId());
            methodInfo.setExceptionNum(exceptionInfos.size());
            methodInfo.setExceptions(exceptionInfos);
            if (!methodInfos.contains(methodInfo)) {
                methodInfos.add(methodInfo);
            }
        }
        return methodInfos;
    }

    @Override
    public List<ExceptionInfo> getExceptionInfos(String exceptionId) {
        List<ExceptionRelation> relations = DataBaseUtil.query(getReadConnection(),KoSqlConstant.queryExceptionReByTarget, new Object[]{exceptionId}, ExceptionRelation.class);
        List<ExceptionInfo> exceptionInfos = new ArrayList<>();
        for (ExceptionRelation relation : relations) {
            String sourceMethodId = relation.getSourceId();
            List<MethodNode> methodNodes = DataBaseUtil.query(getReadConnection(),KoSqlConstant.queryMethod, new Object[]{sourceMethodId}, MethodNode.class);
            if (methodNodes.size() == 0) {
                continue;
            }
            MethodNode methodNode = methodNodes.get(0);

            List<ExceptionNode> exceptions = DataBaseUtil.query(getReadConnection(),KoSqlConstant.queryMethod, new Object[]{exceptionId}, ExceptionNode.class);
            if (methodNodes.size() == 0) {
                continue;
            }
            ExceptionNode exceptionNode = exceptions.get(0);
            ExceptionInfo exceptionInfo = new ExceptionInfo();
            exceptionInfo.setId(exceptionNode.getId());
            exceptionInfo.setName(exceptionNode.getName());
            exceptionInfo.setClassName(exceptionNode.getClassName());
            exceptionInfo.setLocation(relation.getLocation());
            exceptionInfo.setMessage(exceptionNode.getMessage());
            exceptionInfo.setMethodName(methodNode.getMethodName());
            exceptionInfo.setOccurClassName(methodNode.getClassName());
            if (!exceptionInfos.contains(exceptionInfo)) {
                exceptionInfos.add(exceptionInfo);
            }
        }
        return exceptionInfos;
    }

    @Override
    public List<ExceptionInfo> getExceptions(String methodId) {
        List<ExceptionInfo> exceptionInfos = new ArrayList<>();
        List<ExceptionRelation> relations = DataBaseUtil.query(getReadConnection(),KoSqlConstant.queryExceptionReByTarget, new Object[]{methodId}, ExceptionRelation.class);
        for (ExceptionRelation relation : relations) {
            String exceptionId = relation.getTargetId();
            List<ExceptionNode> exceptionNodes = DataBaseUtil.query(getReadConnection(),KoSqlConstant.queryException, new Object[]{exceptionId}, ExceptionNode.class);
            if (exceptionNodes.size() == 0) {
                continue;
            }
            ExceptionNode exceptionNode = exceptionNodes.get(0);
            ExceptionInfo exceptionInfo = new ExceptionInfo();
            exceptionInfo.setId(exceptionNode.getId());
            exceptionInfo.setName(exceptionNode.getName());
            exceptionInfo.setClassName(exceptionNode.getClassName());
            exceptionInfo.setMessage(exceptionNode.getMessage());
            exceptionInfo.setLocation(relation.getLocation());
            if (!exceptionInfos.contains(exceptionInfo)) {
                exceptionInfos.add(exceptionInfo);
            }
        }
        return exceptionInfos;
    }

    @Override
    public List<ExceptionNode> getExceptions() {
        List<ExceptionNode> exceptionNodes = DataBaseUtil.query(getReadConnection(),KoSqlConstant.queryExceptions, null, ExceptionNode.class);
        return exceptionNodes;
    }


}
