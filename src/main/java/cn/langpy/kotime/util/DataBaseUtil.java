package cn.langpy.kotime.util;

import cn.langpy.kotime.data.MysqlBase;
import cn.langpy.kotime.model.TableInfo;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class DataBaseUtil {
    public static Logger log = Logger.getLogger(DataBaseUtil.class.toString());

    public static DataSource getDataSource() {
        return Context.getConfig().getDataSource();
    }

    public static int insert(String sql, Object[] values) {
        PreparedStatement statement;
        try (Connection connection = getDataSource().getConnection()) {
            connection.setAutoCommit(true);
            statement = connection.prepareStatement(sql);
            statement = setParams(statement, values);
            int n = statement.executeUpdate();
            return n;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    public static int update(String sql, Object[] values) {
        return insert(sql,values);
    }

    public static List<Map<String, Object>> query(String sql, Object[] values) {
        List<Map<String, Object>> list = new ArrayList<>();
        PreparedStatement statement;
        try (Connection connection = getDataSource().getConnection()) {
            statement = connection.prepareStatement(sql);
            statement = setParams(statement, values);
            final ResultSetMetaData metaData = statement.getMetaData();
            ResultSet resultSet = statement.executeQuery();
            List<TableInfo> columns = getColumns(metaData);
            while (resultSet.next()) {
                Map<String, Object> map = new HashMap<>();
                for (TableInfo column : columns) {
                    map.put(column.getName(), getColumnValue(resultSet, column));
                }
                list.add(map);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    private static PreparedStatement setParams(PreparedStatement statement, Object[] values) {
        try {
            for (int i = 0; i < values.length; i++) {
                if (values[i] ==null) {
                    statement.setObject(i + 1, null);
                }else if (values[i] instanceof String) {
                    statement.setString(i + 1, (String) values[i]);
                } else if (values[i] instanceof Integer) {
                    statement.setInt(i + 1, (Integer) values[i]);
                } else if (values[i] instanceof Double) {
                    statement.setDouble(i + 1, (Double) values[i]);
                } else if (values[i] instanceof Boolean) {
                    statement.setBoolean(i + 1, (Boolean) values[i]);
                } else {
                    throw new DataBaseException("Invalid type="+values[i].getClass().getName());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statement;
    }

    private static Object getColumnValue(ResultSet resultSet, TableInfo column) throws SQLException {
        if ("VARCHAR".equals(column.getDataType()) || "TEXT".equals(column.getDataType())) {
            return resultSet.getString(column.getName());
        } else if ("DATETIME".equals(column.getDataType())) {
            return resultSet.getTimestamp(column.getName());
        } else if ("INT".equals(column.getDataType())) {
            return resultSet.getInt(column.getName());
        } else {
            return resultSet.getObject(column.getName());
        }
    }

    private static List<TableInfo> getColumns(ResultSetMetaData metaData) throws SQLException {
        List<TableInfo> colnames = new ArrayList<TableInfo>();
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            String colname = metaData.getColumnName(i + 1);
            String colType = metaData.getColumnTypeName(i + 1);
            TableInfo tableInfo = new TableInfo();
            tableInfo.setName(colname);
            tableInfo.setDataType(colType);
            colnames.add(tableInfo);
        }
        return colnames;
    }

}
