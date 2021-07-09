package com.ns.chemcomp.controller;

import com.ns.chemcomp.dao.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @Autowired
    CategoryDao cDao;

    /**
     * GET - Load login page
     *
     * @param model holds list of static pages
     * @return load login view
     */
    @GetMapping({"/login"})
    public String displayLoginPage(Model model) {
        model.addAttribute("categories", cDao.readAllCategories());

        return "login";
    }
}
