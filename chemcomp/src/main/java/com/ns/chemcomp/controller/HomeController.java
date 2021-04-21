package com.ns.chemcomp.controller;

import com.ns.chemcomp.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    ProductDao pDao;

    @GetMapping({"/", "/home"})
    public String displayHomePage(Model model) {
        //TODO make wireframes

        return "home";
    }
}
