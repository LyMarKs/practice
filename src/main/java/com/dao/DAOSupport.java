package com.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

public class DAOSupport {
    static Connection conn;
    static PreparedStatement ps;
    static String sql;

    public DAOSupport() {
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream("F:\\MyProjext\\JAVA\\maven_workspace\\practice\\src\\main\\java\\com\\util\\dbInfo.properties"));
            Class.forName(prop.getProperty("driver"));
            conn = DriverManager.getConnection(prop.getProperty("url"),
                                               prop.getProperty("username"),
                                               prop.getProperty("password"));
        } catch (Exception e) {
            System.err.println("连接数据库出错!");
            e.printStackTrace();
        }
    }
}
