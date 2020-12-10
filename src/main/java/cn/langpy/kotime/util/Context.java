package cn.langpy.kotime.util;

import cn.langpy.kotime.model.RunTimeNode;
import cn.langpy.kotime.model.SystemStatistic;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Context {

    private static Map<String, RunTimeNode> runTimeNodeMap;
    static {
        runTimeNodeMap = new HashMap<>();
    }


    public static void set(RunTimeNode parent,RunTimeNode current) {

        String parentKey = parent.getClassName()+"."+parent.getMethodName();
        String currentKey = current.getClassName()+"."+current.getMethodName();
        if (!currentKey.contains("$")) {
            log.info("调用方法="+currentKey+"，耗时="+current.getAvgRunTime()+"毫秒");
        }else {
            return;
        }
        if (".".equals(parentKey)) {
            if (runTimeNodeMap.containsKey(currentKey)) {
                runTimeNodeMap.get(currentKey).setAvgRunTime(current.getAvgRunTime());
            }else{
                runTimeNodeMap.put(currentKey,current);
            }
        }else if (runTimeNodeMap.containsKey(parentKey)) {
            RunTimeNode hisRunTimeNode = runTimeNodeMap.get(parentKey);
            List<RunTimeNode> hisRunTimeNodeChildren = hisRunTimeNode.getChildren();
            if (hisRunTimeNodeChildren!=null) {
                if (hisRunTimeNodeChildren.contains(current)) {
                    int hisLength = hisRunTimeNodeChildren.size();
                    for (int i = 0; i < hisLength; i++) {
                        if (hisRunTimeNodeChildren.get(i)==current) {
                            current.setAvgRunTime((current.getAvgRunTime()+hisRunTimeNode.getChildren().get(i).getAvgRunTime())/2.0);
                            hisRunTimeNodeChildren.set(i,current) ;
                            break;
                        }
                    }
                }else{
                    hisRunTimeNodeChildren.add(current);
                }
            } else {
                List<RunTimeNode> list = new ArrayList<>();
                list.add(current);
                hisRunTimeNode.setChildren(list);
            }
            runTimeNodeMap.put(parentKey,hisRunTimeNode);
        }else{
            List<RunTimeNode> list = new ArrayList<>();
            list.add(current);
            parent.setChildren(list);
            runTimeNodeMap.put(parentKey,parent);
        }

    }

    public static RunTimeNode get(String key) {
        return runTimeNodeMap.get(key);
    }
    public static List<RunTimeNode> get(MethodType methodType) {
        return runTimeNodeMap.values().stream()
                .filter(runTimeNode -> runTimeNode.getMethodType()==methodType)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }
    public static RunTimeNode getTree(String key) {
        RunTimeNode root = runTimeNodeMap.get(key);
        if (root==null) {
            return root;
        }
        root.setValue(root.getAvgRunTime());
        List<RunTimeNode> children = root.getChildren();
        if (children!=null&&children.size()>0) {
            children.forEach(child->{
                String childKey = child.getClassName()+"."+child.getMethodName();
                RunTimeNode newChild = getTree(childKey);
                if (newChild!=null) {
                    child.setChildren(newChild.getChildren());
                    child.setValue(child.getAvgRunTime());
                }
            });
        }

        return root;
    }

    public static SystemStatistic getStatistic() {
        List<RunTimeNode> controllerApis = get(MethodType.Controller);
        SystemStatistic systemStatistic = new SystemStatistic();
        int delayNum = (int)controllerApis.stream().filter(controllerApi -> controllerApi.getAvgRunTime() >= 800).count();
        systemStatistic.setDelayNum(delayNum);
        int normalNum = (int)controllerApis.stream().filter(controllerApi -> controllerApi.getAvgRunTime() < 800).count();
        systemStatistic.setNormalNum(normalNum);
        int totalNum = (int)controllerApis.stream().count();
        systemStatistic.setTotalNum(totalNum);
        Double max = controllerApis.stream().map(api->api.getAvgRunTime()).max(Double::compareTo).get();
        Double min = controllerApis.stream().map(api->api.getAvgRunTime()).min(Double::compareTo).get();
        Double avg = controllerApis.stream().map(api->api.getAvgRunTime()).collect(Collectors.averagingDouble(Double::doubleValue));
        systemStatistic.setMaxRunTime(max);
        systemStatistic.setMinRunTime(min);
        systemStatistic.setAvgRunTime(avg);
        return systemStatistic;
    }
}
