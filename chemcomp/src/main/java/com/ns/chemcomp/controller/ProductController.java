package com.ns.chemcomp.controller;

import com.ns.chemcomp.dao.CategoryDao;
import com.ns.chemcomp.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProductController {

    @Autowired
    CategoryDao cDao;
    @Autowired
    ProductDao pDao;

    @GetMapping({"/products"})
    public String displayProductsPage(Model model) {
        model.addAttribute("categories", cDao.readAllCategories());
        model.addAttribute("products", pDao.readAllProducts());
        return "products";
    }

    @GetMapping({"/viewProduct"})
    public String displayViewProductPage(Model model, HttpServletRequest request) {
        model.addAttribute("categories", cDao.readAllCategories());
        model.addAttribute("product", pDao.readProductById(Integer.parseInt(request.getParameter("id"))));
        return "viewProduct";
    }
}
