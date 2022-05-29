package cn.langpy.kotime.util;

import cn.langpy.kotime.model.ColumnInfo;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class DataBaseUtil {
    public static Logger log = Logger.getLogger(DataBaseUtil.class.toString());

    static Map<String, ColumnInfo> tableInfoMap = new ConcurrentHashMap<>();

    public static DataSource getDataSource() {
        return Context.getDataSource();
    }

    public static int insert(String sql, Object[] values) {
        try {
            Connection connection = getDataSource().getConnection();
            return insert(connection, sql, values);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public static int insert(Connection connection, String sql, Object[] values) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            if (null != values) {
                statement = setParams(statement, values);
            }
            int n = statement.executeUpdate();
            return n;
        } catch (SQLIntegrityConstraintViolationException e) {
            log.info("Duplicate Method id：" + values[0]);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return 0;
    }

    public static int update(String sql, Object[] values) {
        try (Connection connection = getDataSource().getConnection()) {
            return update(connection, sql, values);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public static int update(Connection connection, String sql, Object[] values) {
        return insert(connection, sql, values);
    }

    public static List<Map<String, Object>> query(String sql, Object[] values) {
        try (Connection connection = getDataSource().getConnection()) {
            return query(connection, sql, values);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static List<Map<String, Object>> query(Connection connection, String sql, Object[] values) {
        List<Map<String, Object>> list = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            if (null != values) {
                statement = setParams(statement, values);
            }
            final ResultSetMetaData metaData = statement.getMetaData();
            ResultSet resultSet = statement.executeQuery();
            List<ColumnInfo> columns = getColumns(metaData);
            while (resultSet.next()) {
                Map<String, Object> map = new HashMap<>();
                for (ColumnInfo column : columns) {
                    map.put(column.getName(), getColumnValue(resultSet, column));
                }
                list.add(map);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return list;
    }

    public static boolean existsById(Connection connection, String sql, Object id) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement = setParams(statement, new Object[]{id});
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return false;
    }

    public static <T> List<T> query(Connection connection, String sql, Object[] values, Class<T> c) {
        List<T> list = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            if (null != values) {
                statement = setParams(statement, values);
            }
            final ResultSetMetaData metaData = statement.getMetaData();
            ResultSet resultSet = statement.executeQuery();
            List<ColumnInfo> columns = getColumns(metaData);
            while (resultSet.next()) {
                T object = c.newInstance();
                Field[] fields = object.getClass().getDeclaredFields();
                for (Field field : fields) {
                    int mod = field.getModifiers();
                    if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                        continue;
                    }
                    ColumnInfo column = null;
                    if (tableInfoMap.containsKey(columns.get(0).getTable() + field.getName())) {
                        column = tableInfoMap.get(columns.get(0).getTable() + field.getName());
                    } else {
                        column = matchColumn(field, columns);
                        if (null == column) {
                            continue;
                        }
                        tableInfoMap.put(column.getTable() + field.getName(), column);
                    }
                    field.setAccessible(true);
                    Object columnValue = getColumnValue(resultSet, column);
                    if (field.getName().equals("methodType")) {
                        field.set(object, MethodType.valueOf(columnValue + ""));
                    } else {
                        field.set(object, columnValue);
                    }
                }
                list.add(object);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return list;
    }

    public static <T> List<T> query(String sql, Object[] values, Class<T> c) {
        try (Connection connection = getDataSource().getConnection()) {
            return query(connection, sql, values, c);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static ColumnInfo matchColumn(Field field, List<ColumnInfo> columns) {
        for (ColumnInfo column : columns) {
            String columnReplace = column.getName().replace("_", "");
            if (field.getName().equalsIgnoreCase(columnReplace)) {
                return column;
            }
        }
        return null;
    }

    private static PreparedStatement setParams(PreparedStatement statement, Object[] values) {
        try {
            int length = values.length;
            for (int i = 0; i < length; i++) {
                if (values[i] == null) {
                    statement.setObject(i + 1, null);
                } else if (values[i] instanceof String) {
                    statement.setString(i + 1, (String) values[i]);
                } else if (values[i] instanceof Double) {
                    statement.setDouble(i + 1, (Double) values[i]);
                } else if (values[i] instanceof Integer) {
                    statement.setInt(i + 1, (Integer) values[i]);
                } else if (values[i] instanceof Boolean) {
                    statement.setBoolean(i + 1, (Boolean) values[i]);
                } else {
                    throw new DataBaseException("Invalid type=" + values[i].getClass().getName());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statement;
    }

    private static Object getColumnValue(ResultSet resultSet, ColumnInfo column) throws SQLException {
        if ("VARCHAR".equals(column.getDataType()) || "TEXT".equals(column.getDataType())) {
            return resultSet.getString(column.getName());
        } else if ("DOUBLE".equalsIgnoreCase(column.getDataType())) {
            return resultSet.getDouble(column.getName());
        } else if ("DECIMAL".equalsIgnoreCase(column.getDataType())) {
            return resultSet.getBigDecimal(column.getName()).doubleValue();
        } else if ("INT".equalsIgnoreCase(column.getDataType())) {
            return resultSet.getInt(column.getName());
        } else if ("DATETIME".equalsIgnoreCase(column.getDataType())) {
            return resultSet.getTimestamp(column.getName());
        } else {
            return resultSet.getObject(column.getName());
        }
    }

    private static List<ColumnInfo> getColumns(ResultSetMetaData metaData) throws SQLException {
        List<ColumnInfo> colnames = new ArrayList<ColumnInfo>();
        int columnCount = metaData.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            String table = metaData.getTableName(i + 1);
            String colname = metaData.getColumnName(i + 1);
            String colType = metaData.getColumnTypeName(i + 1);
            ColumnInfo tableInfo = new ColumnInfo();
            tableInfo.setTable(table);
            tableInfo.setName(colname);
            tableInfo.setDataType(colType);
            colnames.add(tableInfo);
        }
        return colnames;
    }

}
