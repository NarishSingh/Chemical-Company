package com.ns.chemcomp.controller;

import com.ns.chemcomp.dao.CategoryDao;
import com.ns.chemcomp.dao.ProductDao;
import com.ns.chemcomp.dto.Category;
import com.ns.chemcomp.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CategoryController {

    @Autowired
    ProductDao pDao;
    @Autowired
    CategoryDao cDao;

    @GetMapping({"/categories"})
    public String displayCategoriesPage(Model model) {
        List<Category> categories = cDao.readAllCategories();

        Map<Category, List<Product>> productsByCategories = new HashMap<>();
        for (Category c : categories) {
            productsByCategories.put(c, pDao.readProductsByCategory(c));
        }
        int[] productCounts = productsByCategories.values().stream()
                .mapToInt(List::size)
                .toArray();

        model.addAttribute("categories", categories);
        model.addAttribute("productCounts", productCounts);

        return "categories";
    }
}
