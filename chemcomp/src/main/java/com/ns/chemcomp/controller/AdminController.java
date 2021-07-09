package com.ns.chemcomp.controller;

import com.ns.chemcomp.dao.RoleDao;
import com.ns.chemcomp.dao.UserDao;
import com.ns.chemcomp.dto.Role;
import com.ns.chemcomp.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.Set;

@Controller
public class AdminController {
    @Autowired
    RoleDao rDao;
    @Autowired
    UserDao uDao;
    @Autowired
    PasswordEncoder encoder;
    Set<ConstraintViolation<User>> violations = new HashSet<>();

    /**
     * GET - load admin dashboard
     *
     * @param model contains users, roles, and validation errors
     * @return load admin view
     */
    @GetMapping({"/admin"})
    public String displayAdminPage(Model model) {
        model.addAttribute("roles", rDao.readAllRoles());
        model.addAttribute("users", uDao.readAllUsers());
        model.addAttribute("errors", violations);

        return "admin";
    }

    /**
     * POST - create a new user account as an Admin - created account will be automatically enabled
     *
     * @param request marshals form data
     * @return reload admin view
     */
    @PostMapping({"/addUser"})
    public String addUserAsAdmin(HttpServletRequest request) {
        User user = new User();
        String usernameString = request.getParameter("username");
        String passwordString = request.getParameter("password");

        if (usernameString.isBlank() || passwordString.isBlank()) {
            return "admin";
        } else {
            user.setUsername(usernameString);
            user.setPassword(encoder.encode(passwordString));
            user.setEnabled(true);
            user.setName(request.getParameter("name"));
            user.setPhone(request.getParameter("phone"));
            user.setEmail(request.getParameter("email"));
            user.setAddress(request.getParameter("address"));

            Set<Role> userRoles = new HashSet<>();
            userRoles.add(rDao.readRoleById(Integer.parseInt(request.getParameter("roleId"))));
            user.setRoles(userRoles);
        }

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(user);

        if (violations.isEmpty()) {
            uDao.createUser(user);
        }

        return "redirect:/admin";
    }
}
