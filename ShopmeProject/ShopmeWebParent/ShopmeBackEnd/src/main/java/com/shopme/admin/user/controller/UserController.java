package com.shopme.admin.user.controller;

import com.shopme.admin.user.service.UserService;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listAll(Model model){
        List<User> users = userService.listAll();
        model.addAttribute("users", users);
        return "users";
    }
}
