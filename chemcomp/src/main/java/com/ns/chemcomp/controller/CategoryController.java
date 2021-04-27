package com.ns.chemcomp.controller;

import com.ns.chemcomp.dao.CategoryDao;
import com.ns.chemcomp.dao.ProductDao;
import com.ns.chemcomp.dto.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    ProductDao pDao;
    @Autowired
    CategoryDao cDao;

    /**
     * GET - category list page view
     *
     * @param model {Model} holds list of categories, and array of product counts
     * @return {String} load view
     */
    @GetMapping({"/categories"})
    public String displayCategoriesPage(Model model) {
        List<Category> categories = cDao.readAllCategories();

        //get product counts for badge pill
        int[] productCounts = categories.stream()
                .mapToInt(c -> pDao.readProductsByCategory(c.getCategoryId()).size())
                .toArray();

        model.addAttribute("categories", categories);
        model.addAttribute("productCounts", productCounts);

        return "categories";
    }

    /**
     * GET - list of products for selected category view
     *
     * @param model   {Model} holds list of products
     * @param request {HttpServletRequest} used to pull id from request
     * @return {String} load view
     */
    @GetMapping({"/viewCategory"})
    public String displayViewCategoryPage(Model model, HttpServletRequest request) {
        int categoryId = Integer.parseInt(request.getParameter("id"));

        model.addAttribute("category", cDao.readCategoryById(categoryId));
        model.addAttribute("products", pDao.readProductsByCategory(categoryId));

        return "viewCategory";
    }
}
