package com.controller;

import com.Model.Admin;
import com.util.DBUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/admin/*")
public class AdminServlet extends BaseServlet {

    /**
     * 登录
     */
    public void login(HttpServletRequest req, HttpServletResponse resp) {
        //获取参数
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        try {
            DBUtils.getConn();
            Admin admin = DBUtils.selectOne("SELECT * FROM admin WHERE nsername=?", Admin.class, username, password);
            System.out.println(admin.toString());
            DBUtils.closeAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
