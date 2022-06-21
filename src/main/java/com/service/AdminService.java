package com.service;

import com.dao.AdminDAO;
import com.model.Admin;

public class AdminService {
    public AdminDAO adminDAO;

    public AdminService(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    /**
     * 登录：判断用户名和密码数据库中是否存在
     */
    public boolean login() {
        // 用于判断管理员是否存在（true:存在, false:不存在）
        boolean isExist = false;
        // 根据用户名和密码查询数据库中是否存在此管理员
        Admin admin = adminDAO.selectByNameAndPwd();
        // 判断管理员是否非空
        if (admin != null) {
            isExist = true;
        }
        return isExist;
    }

    public boolean register() {
        // 用于判断是否注册成功（true:注册成功, false:注册失败）
        boolean isRegister = false;
        // 执行数据库添加操作
        int rows = adminDAO.add();
        // 判断行数
        if (rows > 0) {
            isRegister = true;
        }
        return isRegister;
    }
}
