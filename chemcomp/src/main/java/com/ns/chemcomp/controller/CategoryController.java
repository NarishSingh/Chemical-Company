package com.ns.chemcomp.controller;

import com.ns.chemcomp.dao.CategoryDao;
import com.ns.chemcomp.dao.ProductDao;
import com.ns.chemcomp.dto.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    ProductDao pDao;
    @Autowired
    CategoryDao cDao;

    @GetMapping({"/categories"})
    public String displayCategoriesPage(Model model) {
        List<Category> categories = cDao.readAllCategories();

        //get product counts
        int[] productCounts = categories.stream()
                .mapToInt(c -> pDao.readProductsByCategory(c).size())
                .toArray();

        model.addAttribute("categories", categories);
        model.addAttribute("productCounts", productCounts);

        return "categories";
    }
}
