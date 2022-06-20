package com.util;

import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @Author 谭志扬
 * @Date 2022/6/8
 */
public class DBUtils {
    private static String url;
    private static String username;
    private static String password;
    private static Connection conn;
    private static PreparedStatement ps;
    private static ResultSet rs;

    static {
        try {
            InputStream rs = DBUtils.class.getResourceAsStream("dbInfo.properties");
            Properties prop = new Properties();
            prop.load(rs);
            Class.forName(prop.getProperty("driver"));
            url = prop.getProperty("url");
            username = prop.getProperty("username");
            password = prop.getProperty("password");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 方法描述：获取连接
     */
    public static void getConn() throws SQLException {
        conn =  DriverManager.getConnection(url, username, password);
    }

    /**
     * 方法描述：获取语句对象
     * @return 语句对象
     */
    public static void getPS(String sql) throws SQLException {
        //判断连接对象
        if (conn == null || conn.isClosed()) {
            getConn();
        }
        ps = conn.prepareStatement(sql);
    }

    /**
     * 关闭数据库对象
     */
    public static void closeAll() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            System.err.println("关闭数据库出错!错误信息" + e.getMessage());
        }
    }

    public static int update(String sql, Object... args) {
        int rows = -1;
        try {
            //初始化语句对象
            getPS(sql);
            //设置参数
            setPreparedParams(args);
            //执行增/删/改操作
            rows = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
        return rows;
    }

    /**
     * 查询多条数据
     */
    public static <E> List<E> selectMore(String sql, Class<E> c, Object... args) {
        List<E> data = null;
        try {
            //创建语句对象
            getPS(sql);
            //获取数据库的字段名(已格式化)
            String[] dbNames = getDbNames(args);
            //格式化数据库的字段名，转换为Java字段
            String[] fieldNames = formatDbName(dbNames);
            //调用查询数据操作的方法
            data = select(c, dbNames, fieldNames);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeAll();
        }
        //返回查询到的数据
        return data;
    }

    /**
     * 查询一条数据
     */
    public static <E> E selectOne(String sql, Class<E> c, Object... args) {
        E e = null;
        try {
            //创建语句对象
            getPS(sql);
            //获取数据库的字段名(已格式化)
            String[] dbNames = getDbNames(args);
            //判断是否查询的是数字，是数字直接返回结果
            if(("int".equals(c.getSimpleName())) || ("Integer".equals(c.getSimpleName())) ||
                ("long".equals(c.getSimpleName())) || ("Long".equals(c.getSimpleName()))) {
                rs.next();
                return (E) ((Integer) rs.getInt(1));
            }
            //格式化数据库的字段名，转换为Java字段
            String[] fieldNames = formatDbName(dbNames);
            //调用查询数据操作的方法
            List<E> data = select(c, dbNames, fieldNames);
            if (data.size() > 1) {
                throw new Exception("查询结果应是1条，但查出多条结果!");
            } else if (data.size() == 1) {
                e = data.get(0);
            }
        } catch (Exception ee) {
            throw new RuntimeException(ee);
        } finally {
            closeAll();
        }
        //返回查询到的数据
        return e;
    }

    /**
     * 执行查询操作
     */
    private static <E> List<E> select(Class<E> c, String[] dbNames, String[] fieldNames) {
        System.out.println(Arrays.toString(fieldNames));
        E e = null;
        List<E> data = new ArrayList<>();
        try {
            //执行查询
            rs = ps.executeQuery();
            //循环遍历
            while (rs.next()) {
                //通过反射获取实例
                e = c.newInstance();
                //循环设置字段值
                for (int i = 0; i < dbNames.length; i++) {
                    //获取字段对象
                    Field field = c.getDeclaredField(fieldNames[i]);
                    System.out.println(field.getName() + "---" + dbNames[i]);
                    //设置为数据可访问
                    field.setAccessible(true);
                    //设置指定对象的属性值
                    field.set(e, rs.getObject(dbNames[i]));
                }
                data.add(e);
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return data;
    }

    public static int getSumRecord(String sql, Object... args) {
        //1.先获取总记录数
        // 拼接SQL
        StringBuilder countSql = new StringBuilder("SELECT COUNT(*)");
        countSql.append(sql.substring(sql.indexOf("FROM")));
        // 将LIMIT部分移除
        countSql.replace(countSql.indexOf("LIMIT"), countSql.length(), "");
        // 获取总记录数
        return selectOne(countSql.toString(), int.class, Arrays.copyOf(args, args.length - 2));
    }

    /**
     * 获取数据库的所有格式化列名
     * @return 数据库列名
     */
    @SneakyThrows
    public static String[] getDbNames(Object[] args) {
        //设置sql参数
        setPreparedParams(args);
        //执行查询
        rs = ps.executeQuery();
        ResultSetMetaData metaData = rs.getMetaData();
        //声明字符串数组，存储数据库字段名
        String[] columnNames = new String[metaData.getColumnCount()];
        //循环获取数据库字段名
        for (int i = 0; i < columnNames.length; i++) {
            columnNames[i] = metaData.getColumnName((i + 1));
        }
        //返回格式化的数据库字段名
        return columnNames;
    }

    /**
     * 格式化数据库字段
     * @return 数据库列名
     */
    public static String[] formatDbName(String[] dbNames) {
        String[] formatNames = new String[dbNames.length];
        //循环遍历数据库字段
        for (int i = 0; i < dbNames.length; i++) {
            //进行转换
            StringBuilder convertName = new StringBuilder();
            String[] nameSplit = dbNames[i].split("_");
            if (nameSplit.length > 1) {
                for (int j = 0; j < nameSplit.length; j++) {
                    if (j == 0) {
                        convertName.append(nameSplit[j]);
                    } else {
                        //首字母大写
                        char initial = (char) (nameSplit[j].charAt(0) - 32);
                        //追加至格式化字符串中
                        convertName.append(initial).append(nameSplit[j].substring(1));
                    }
                }
                formatNames[i] = convertName.toString();
            } else {
                formatNames[i] = dbNames[i];
            }
        }
        return formatNames;
    }
    /**
     * 设置预编译对象的sql参数
     * @param args 参数
     */
    @SneakyThrows
    public static void setPreparedParams(Object[] args) {
        for (int i = 0; i < ps.getParameterMetaData().getParameterCount(); i++) {
            ps.setObject((i + 1), args[i]);
        }
    }
}