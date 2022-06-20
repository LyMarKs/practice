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
        boolean isExist = false;
        Admin admin = adminDAO.selectByNameAndPwd();

        if (admin != null) {
            isExist = true;
        }
        return isExist;
    }

    public void register() {

    }
}
