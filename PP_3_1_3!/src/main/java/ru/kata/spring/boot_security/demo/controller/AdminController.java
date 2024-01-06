package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;
import java.util.List;
import java.util.Map;


@Controller
public class AdminController {

    private final UserServiceImpl userServiceImpl;


    @Autowired
    public AdminController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }


    @GetMapping("/admin")
    public String showAllUsers(ModelMap modelMap, Principal principal) {
        User userUs = userServiceImpl.findByFirstname(principal.getName());
        String userRoles = userServiceImpl.getUserRoles(userUs);
        modelMap.addAttribute("userUs", userUs);
        modelMap.addAttribute("userRoles", userRoles);

        Map<User, String> usersWithRoles = userServiceImpl.getAllUsersWithRoles();
        modelMap.addAttribute("usersWithRoles", usersWithRoles);
        return "admin";
    }

    @PostMapping(value = "/admin/addUser")
    public String addNewUser(@RequestParam("role") String role, @ModelAttribute("user") User user) {
        userServiceImpl.addNewUser(role,user);
        return "redirect:/admin";
    }

    @PostMapping  (value = "/admin/updateUser")
    public String updateUser(@RequestParam("id") Long id,@RequestParam("role") String role, @ModelAttribute("user") User user) {
        User userUpdate = userServiceImpl.findById(id).orElse(null);
        userUpdate.setId(id);
            userServiceImpl.updateUser(role,userUpdate);
        return "redirect:/admin";
    }

    @PostMapping(value = "/admin/deleteUser")
    public String deleteUser(@RequestParam long id) {
        userServiceImpl.deleteUser(id);
        return "redirect:/admin";
    }
}
