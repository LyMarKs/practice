package com.dao;

import com.model.Admin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO extends DAOSupport {
    public Admin admin;

    public AdminDAO(Admin admin) {
        this.admin = admin;
    }

    public Admin getAdmin() {
        return admin;
    }

    /**
     * 添加管理员
     */
    public int add() {
        int rows = -1;
        sql = "INSERT admin VALUE(?,?,?,?)";
        try {
            // 创建语句对象，并设置参数
            ps = conn.prepareStatement(sql);
            ps.setString(1, admin.getName());
            ps.setString(2, admin.getUsername());
            ps.setString(3, admin.getPassword());
            ps.setString(4, admin.getPhone());
            // 执行SQL
            rows = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * 根据用户名和密码查询管理员
     */
    public Admin selectByNameAndPwd() {
        Admin admin = null;
        sql = "SELECT * FROM admin WHERE username=? AND password=?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, this.admin.getUsername());
            ps.setString(2, this.admin.getPassword());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                admin = new Admin();
                admin.setAid(rs.getInt("aid"));
                admin.setName(rs.getString("name"));
                admin.setUsername(this.admin.getUsername());
                admin.setPassword(this.admin.getPassword());
                admin.setPhone(rs.getString("phone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin;
    }
}
