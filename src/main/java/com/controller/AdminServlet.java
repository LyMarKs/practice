package com.controller;

import com.dao.AdminDAO;
import com.model.Admin;
import com.service.AdminService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/admin/*")
public class AdminServlet extends BaseServlet {

    /**
     * 登录
     */
    public boolean login(HttpServletRequest req, HttpServletResponse resp) {
        Admin admin = new Admin();
        // 获取参数
        admin.setUsername(req.getParameter("username"));
        admin.setPassword(req.getParameter("password"));
        // 实例化数据访问层对象
        AdminDAO adminDAO = new AdminDAO(admin);
        // 实例化业务逻辑层对象
        AdminService adminService = new AdminService(adminDAO);
        // 管理员登录
        return adminService.login();
    }

    /**
     * 登录
     */
    public boolean register(HttpServletRequest req, HttpServletResponse resp) {
        Admin admin = new Admin();
        // 获取参数
        admin.setName(req.getParameter("name"));
        admin.setUsername(req.getParameter("username"));
        admin.setPassword(req.getParameter("password"));
        admin.setPhone(req.getParameter("phone"));
        // 实例化数据访问层对象
        AdminDAO adminDAO = new AdminDAO(admin);
        // 实例化业务逻辑层对象
        AdminService adminService = new AdminService(adminDAO);
        // 管理员登录
        return adminService.register();
    }
}
