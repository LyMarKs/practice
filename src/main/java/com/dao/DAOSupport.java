package com.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DAOSupport {
    static Connection conn;

    public DAOSupport() {
        try {
            Properties prop = new Properties();
            prop.load(this.getClass().getResourceAsStream("com/util/dbInfo"));
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
