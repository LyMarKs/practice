package com.controller;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Optional;

public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // 获取方法名称
        String methodName = (Optional.ofNullable(req.getPathInfo()).orElse("/")).substring(1);
        // 判断参数是否为空
        if (methodName.trim().isEmpty()) {
            try {
                resp.sendRedirect("404.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        try {
            // 获取方法对象
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            method.setAccessible(true);
            //执行方法，获取返回值
            Object value = method.invoke(this, req, resp);
            //响应数据
            PrintWriter out = resp.getWriter();
            System.out.println(value);
            out.print(value);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
