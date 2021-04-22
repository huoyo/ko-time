package cn.langpy.kotime.service;
import cn.langpy.kotime.model.RunTimeNode;
import cn.langpy.kotime.model.SystemStatistic;
import cn.langpy.kotime.util.Context;
import cn.langpy.kotime.util.GraphMap;
import cn.langpy.kotime.util.MethodType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RunTimeNodeService {

    public static void addOrUpdate(String key, RunTimeNode runTimeNode) {
        if (GraphMap.containsKey(key)) {
            RunTimeNode oldNode = GraphMap.get(key);
            if (0 == oldNode.getAvgRunTime()) {
                GraphMap.get(key).setAvgRunTime((runTimeNode.getAvgRunTime()));
            } else {
                BigDecimal bg = new BigDecimal((runTimeNode.getAvgRunTime()+oldNode.getAvgRunTime())/2.0);
                double avg = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                GraphMap.get(key).setAvgRunTime(avg);
            }
            GraphMap.get(key).setMaxRunTime(runTimeNode.getMaxRunTime()>oldNode.getMaxRunTime()?runTimeNode.getMaxRunTime():oldNode.getMaxRunTime());
            GraphMap.get(key).setMinRunTime(runTimeNode.getMinRunTime()<oldNode.getMinRunTime()?runTimeNode.getMinRunTime():oldNode.getMinRunTime());
        }else{
            GraphMap.put(key,runTimeNode);
        }
    }

    public static void add(String key, RunTimeNode runTimeNode) {
        GraphMap.put(key,runTimeNode);
    }

    public static boolean containsKey(String key) {
       return GraphMap.containsKey(key);
    }

    public static boolean containsNode(RunTimeNode node) {
        String key = node.getClassName()+"."+node.getMethodName();
        return GraphMap.containsKey(key);
    }

    public static RunTimeNode getRunTimeNode(String key) {
        return GraphMap.get(key);
    }

    public static void addOrUpdateChildren(RunTimeNode parent, RunTimeNode current) {
        String parentKey = parent.getClassName()+"."+parent.getMethodName();
        RunTimeNode hisRunTimeNode = RunTimeNodeService.getRunTimeNode(parentKey);
        List<RunTimeNode> hisRunTimeNodeChildren = hisRunTimeNode.getChildren();
        if (hisRunTimeNodeChildren!=null) {
            if (hisRunTimeNodeChildren.contains(current)) {
                updateChildren(current,hisRunTimeNodeChildren);
            }else{
                hisRunTimeNodeChildren.add(current);
            }
        } else {
            List<RunTimeNode> list = new ArrayList<>();
            list.add(current);
            hisRunTimeNode.setChildren(list);
        }
        GraphMap.put(parentKey,hisRunTimeNode);
    }

    public static void updateChildren(RunTimeNode child, List<RunTimeNode> hisRunTimeNodeChildren) {
        int hisLength = hisRunTimeNodeChildren.size();
        for (int i = 0; i < hisLength; i++) {
            RunTimeNode hisRunTimeNodeChild = hisRunTimeNodeChildren.get(i);
            if (hisRunTimeNodeChild.equals(child)) {
                double avg = (child.getAvgRunTime()+hisRunTimeNodeChild.getAvgRunTime())/2.0;
                BigDecimal bg = new BigDecimal(avg);
                avg = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                child.setAvgRunTime(avg);
                if (hisRunTimeNodeChild.getMaxRunTime()>child.getMaxRunTime()) {
                    child.setMaxRunTime(hisRunTimeNodeChild.getMaxRunTime());
                }
                if (hisRunTimeNodeChild.getMinRunTime()<child.getMinRunTime()) {
                    child.setMinRunTime(hisRunTimeNodeChild.getMinRunTime());
                }
                hisRunTimeNodeChildren.set(i,child) ;
                break;
            }
        }
    }

    public static SystemStatistic getRunStatistic() {
        SystemStatistic systemStatistic = new SystemStatistic();
        List<RunTimeNode> controllerApis = GraphMap.get(MethodType.Controller);
        if (null==controllerApis || controllerApis.size()==0 ) {
            return systemStatistic;
        }
        int delayNum = (int)controllerApis.stream().filter(controllerApi -> controllerApi.getAvgRunTime() >= Context.getConfig().getTimeThreshold()).count();
        systemStatistic.setDelayNum(delayNum);
        int normalNum = (int)controllerApis.stream().filter(controllerApi -> controllerApi.getAvgRunTime() < Context.getConfig().getTimeThreshold()).count();
        systemStatistic.setNormalNum(normalNum);
        int totalNum = (int)controllerApis.stream().count();
        systemStatistic.setTotalNum(totalNum);
        Double max = controllerApis.stream().map(api->api.getAvgRunTime()).max(Double::compareTo).get();
        Double min = controllerApis.stream().map(api->api.getAvgRunTime()).min(Double::compareTo).get();
        Double avg = controllerApis.stream().map(api->api.getAvgRunTime()).collect(Collectors.averagingDouble(Double::doubleValue));
        BigDecimal bg = new BigDecimal(avg);
        avg = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        systemStatistic.setMaxRunTime(max);
        systemStatistic.setMinRunTime(min);
        systemStatistic.setAvgRunTime(avg);
        return systemStatistic;
    }

    public static List<RunTimeNode> getControllers() {
        List<RunTimeNode> list = GraphMap.get(MethodType.Controller);
        return list;
    }

    public static RunTimeNode getGraph(String methodName) {
        return GraphMap.getTree(methodName);
    }
}
