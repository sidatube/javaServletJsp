package com.example.javaservletjsp.controller.product;

import com.example.javaservletjsp.entity.Product;
import com.example.javaservletjsp.model.UltraModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetlistController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UltraModel<Product> model = new UltraModel<>(Product.class);
        List<Product> list =  model.getAll();
        req.setAttribute("products",list);
        req.getRequestDispatcher("/product/list.jsp").forward(req,resp);
    }
}
