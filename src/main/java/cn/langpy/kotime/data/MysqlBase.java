package cn.langpy.kotime.data;

import cn.langpy.kotime.constant.KoSqlConstant;
import cn.langpy.kotime.handler.ComputeTimeHandler;
import cn.langpy.kotime.model.*;
import cn.langpy.kotime.service.GraphService;
import cn.langpy.kotime.util.Common;
import cn.langpy.kotime.util.DataBaseUtil;
import cn.langpy.kotime.util.MethodType;
import org.springframework.util.StringUtils;

import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * zhangchang
 */
public class MysqlBase implements GraphService {
    public static Logger log = Logger.getLogger(MysqlBase.class.toString());

    @Override
    public void addMethodNode(MethodNode methodNode) {
        if (null == methodNode) {
            return;
        }
        List<Map<String, Object>> query = DataBaseUtil.query(KoSqlConstant.queryMethod, new Object[]{methodNode.getId()});
        if (query.size() == 0) {
            Object[] params = new Object[]{
                    methodNode.getId(),
                    methodNode.getName(),
                    methodNode.getClassName(),
                    methodNode.getMethodName(),
                    methodNode.getRouteName(),
                    methodNode.getMethodType().name()
            };
            DataBaseUtil.insert(KoSqlConstant.addMethod, params);
        }else {
            if (methodNode.getMethodType() == MethodType.Controller && !StringUtils.isEmpty(methodNode.getRouteName())) {
                Object[] params = new Object[]{
                        methodNode.getName(),
                        methodNode.getClassName(),
                        methodNode.getMethodName(),
                        methodNode.getRouteName(),
                        methodNode.getMethodType().name(),
                        methodNode.getId(),
                };
                DataBaseUtil.update(KoSqlConstant.updateMethod, params);
            }
        }
    }


    @Override
    public void addExceptionNode(ExceptionNode exceptionNode) {
        List<Map<String, Object>> query = DataBaseUtil.query(KoSqlConstant.queryException, new Object[]{exceptionNode.getId()});
        if (query.size() == 0) {
            Object[] params = new Object[]{
                    exceptionNode.getId(),
                    exceptionNode.getName(),
                    exceptionNode.getClassName(),
                    exceptionNode.getMessage()
            };
            DataBaseUtil.insert(KoSqlConstant.addException, params);
        }
    }


    @Override
    public MethodRelation addMethodRelation(MethodNode sourceMethodNode, MethodNode targetMethodNode) {
        if (null == sourceMethodNode || null == targetMethodNode) {
            return null;
        }
        if (sourceMethodNode.getId().equals(targetMethodNode.getId())) {
            return null;
        }
        List<Map<String, Object>> query = DataBaseUtil.query(KoSqlConstant.queryMethodRe, new Object[]{sourceMethodNode.getId() + targetMethodNode.getId()});
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
            DataBaseUtil.update(KoSqlConstant.updateMethodRe, params);
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
            DataBaseUtil.insert(KoSqlConstant.addMethodRe, params);
        }
        return null;
    }

    @Override
    public ExceptionRelation addExceptionRelation(MethodNode sourceMethodNode, ExceptionNode exceptionNode) {
        List<Map<String, Object>> query = DataBaseUtil.query(KoSqlConstant.queryExceptionRe, new Object[]{sourceMethodNode.getId() + exceptionNode.getId()});
        if (query.size() == 0) {
            Object[] params = new Object[]{
                    sourceMethodNode.getId() + exceptionNode.getId(),
                    sourceMethodNode.getId(),
                    exceptionNode.getId(),
                    exceptionNode.getValue()
            };
            DataBaseUtil.insert(KoSqlConstant.addExceptionRe, params);
        }
        return null;
    }

    @Override
    public void addParamAnalyse(String methodId, Parameter[] names, Object[] values, double v) {
        String paramsKey = Common.getPramsStr(names, values);
        List<Map<String, Object>> query = DataBaseUtil.query(KoSqlConstant.queryParamsAna, new Object[]{methodId, paramsKey});
        if (query.size() == 0) {
            Object[] params = new Object[]{
                    methodId,
                    paramsKey,
                    v,
                    v,
                    v
            };
            DataBaseUtil.insert(KoSqlConstant.addParamsAna, params);
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
            DataBaseUtil.update(KoSqlConstant.updateParamsAna, params);
        }
    }

    @Override
    public MethodInfo getTree(String methodId) {
        return null;
    }

    @Override
    public Map<String, ParamMetric> getMethodParamGraph(String methodId) {
        return null;
    }

    @Override
    public SystemStatistic getRunStatistic() {
        return null;
    }

    @Override
    public List<MethodInfo> getControllers() {
        return null;
    }

    @Override
    public List<String> getCondidates(String question) {
        return null;
    }

    @Override
    public List<MethodInfo> searchMethods(String question) {
        return null;
    }

    @Override
    public List<MethodInfo> getChildren(String methodId) {
        return null;
    }

    @Override
    public List<ExceptionInfo> getExceptionInfos(String exceptionId) {
        return null;
    }

    @Override
    public List<ExceptionInfo> getExceptions(String methodId) {
        return null;
    }

    @Override
    public List<ExceptionNode> getExceptions() {
        return null;
    }

}
