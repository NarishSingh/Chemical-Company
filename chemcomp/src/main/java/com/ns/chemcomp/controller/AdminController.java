package com.ns.chemcomp.controller;

import com.ns.chemcomp.dao.RoleDao;
import com.ns.chemcomp.dao.UserDao;
import com.ns.chemcomp.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;

@Controller
public class AdminController {
    @Autowired
    RoleDao rDao;
    @Autowired
    UserDao uDao;
    Set<ConstraintViolation<User>> violations = new HashSet<>();

    @GetMapping({"/admin"})
    public String displayAdminPage(Model model) {
        model.addAttribute("roles", rDao.readAllRoles());
        model.addAttribute("users", uDao.readAllUsers());
        model.addAttribute("errors", violations);

        return "admin";
    }
}
