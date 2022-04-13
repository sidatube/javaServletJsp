package com.example.javaservletjsp.controller.user;

import com.example.javaservletjsp.entity.User;
import com.example.javaservletjsp.model.UltraModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/user/login.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UltraModel<User> model = new UltraModel<>(User.class);
        String username = req.getParameter("username");
        String password =req.getParameter("password");
        if (username==null){
            req.setAttribute("errorsLog","Invalid username");
            req.getRequestDispatcher("/user/login.jsp").forward(req,resp);
            return;
        }

        User user= model.findByColumns("username",new User(username,password));
        if (user==null){
            req.setAttribute("errorsLog","Not found username");
            req.getRequestDispatcher("/user/login.jsp").forward(req,resp);
            return;

        }
        if (!user.getPassword().contains(password)){
            req.setAttribute("errorsLog","Invalid information");
            req.getRequestDispatcher("/user/login.jsp").forward(req,resp);
            return;
        }
        req.getSession().setAttribute("currentUser",user);
        resp.sendRedirect("/products");
    }
}
