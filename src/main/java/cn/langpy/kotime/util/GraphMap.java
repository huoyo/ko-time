package cn.langpy.kotime.util;


import cn.langpy.kotime.model.RunTimeNode;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GraphMap {

    /*只需保证可见性，无需保证线程安全*/
    private volatile static Map<String, RunTimeNode> runTimeNodeMap;

    static {
        runTimeNodeMap = new HashMap<>();
    }

    public static RunTimeNode get(String key) {
        return runTimeNodeMap.get(key);
    }

    public static RunTimeNode put(String key, RunTimeNode runTimeNode) {
        return runTimeNodeMap.put(key,runTimeNode);
    }

    public static boolean containsKey(String key) {
        return GraphMap.runTimeNodeMap.containsKey(key);
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
}
