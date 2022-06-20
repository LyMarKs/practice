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
     * 根据用户名和密码查询管理员
     */
    public Admin selectByNameAndPwd() {
        Admin admin = null;
        String sql = "SELECT * FROM admin WHERE username=? AND password=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, admin.getUsername());
            ps.setString(2, admin.getPassword());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                admin = new Admin();
                admin.setAid(rs.getInt("aid"));
                admin.setName(rs.getString("name"));
                admin.setUsername(admin.getUsername());
                admin.setPassword(admin.getPassword());
                admin.setPhone(rs.getString("phone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin;
    }
}
