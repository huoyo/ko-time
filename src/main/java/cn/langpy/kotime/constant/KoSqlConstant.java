package cn.langpy.kotime.constant;

public class KoSqlConstant {
    public final static String addMethod = "INSERT INTO ko_method_node(id, name, class_name, method_name, route_name, method_type) VALUES (?, ?, ?, ?, ?, ?)";
    public final static String queryMethod = "SELECT id, name, class_name, method_name, route_name, method_type FROM ko_method_node  WHERE id=?";
    public final static String queryMethodExist = "SELECT id FROM ko_method_node  WHERE id=?";
    public final static String queryMethodLikeName = "SELECT id, name, class_name, method_name, route_name, method_type FROM ko_method_node  WHERE name like ?";
    public final static String queryController = "SELECT id, name, class_name, method_name, route_name, method_type FROM ko_method_node  WHERE method_type=?";
    public final static String updateMethod = "UPDATE ko_method_node SET name=?, class_name=?, method_name=?, route_name=?, method_type=? WHERE id=?";
    public final static String addException = "INSERT INTO ko_exception_node(id, name, class_name) VALUES (?, ?, ?)";
    public final static String queryExceptions = "SELECT id, name, class_name FROM ko_exception_node";
    public final static String queryException = "SELECT id, name, class_name FROM ko_exception_node  WHERE id=?";
    public final static String queryExceptionExist = "SELECT id FROM ko_exception_node  WHERE id=?";
    public final static String addMethodRe = "INSERT INTO ko_method_relation(id, source_id, target_id, avg_run_time, max_run_time, min_run_time) VALUES (?, ?, ?, ?, ?, ?)";
    public final static String queryMethodRe = "SELECT id, source_id, target_id, avg_run_time, max_run_time, min_run_time FROM ko_method_relation WHERE id=?";
    public final static String queryMethodReBySource = "SELECT id, source_id, target_id, avg_run_time, max_run_time, min_run_time FROM ko_method_relation WHERE source_id=?";
    public final static String queryMethodReByTarget = "SELECT id, source_id, target_id, avg_run_time, max_run_time, min_run_time FROM ko_method_relation WHERE target_id=?";
    public final static String updateMethodRe = "UPDATE ko_method_relation SET source_id=?, target_id=?, avg_run_time=?, max_run_time=?, min_run_time=? WHERE id=?";
    public final static String addExceptionRe = "INSERT INTO ko_exception_relation(id, source_id, target_id, location,message) VALUES (?, ?, ?, ?, ?)";
    public final static String queryExceptionRe = "SELECT id, source_id, target_id, location, message FROM ko_exception_relation WHERE id=?";
    public final static String queryExceptionReExist = "SELECT id FROM ko_exception_relation WHERE id=?";
    public final static String queryExceptionReByTarget = "SELECT id, source_id, target_id, location, message FROM ko_exception_relation WHERE target_id=?";
    public final static String addParamsAna = "INSERT INTO ko_param_ana (source_id, params, avg_run_time, max_run_time, min_run_time) VALUES (?, ?, ?, ?, ?)";
    public final static String queryParamsAna = "SELECT source_id, params, avg_run_time, max_run_time, min_run_time FROM ko_param_ana WHERE source_id=? and params=?";
    public final static String queryParamsAnaBySource = "SELECT source_id, params, avg_run_time, max_run_time, min_run_time FROM ko_param_ana WHERE source_id=?";
    public final static String updateParamsAna = "UPDATE ko_param_ana SET avg_run_time=?, max_run_time=?, min_run_time=?  WHERE source_id=? and params=?";

}
