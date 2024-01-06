package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;


@Controller
public class AdminController {

    private final UserServiceImpl userServiceImpl;
    private final RoleRepository roleRepository;



    @Autowired
    public AdminController(UserServiceImpl userServiceImpl, RoleRepository roleRepository) {
        this.userServiceImpl = userServiceImpl;
        this.roleRepository = roleRepository;
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

    @GetMapping("/admin/updateUser")
    public String editUserForm(@RequestParam("id") Long id, Model model) {
        Optional<User> userById = userServiceImpl.findById(id);

        if (userById.isPresent()) {
            model.addAttribute("user", userById.get());
            model.addAttribute("listRoles", roleRepository.findAll());
            return "/admin";
        } else {
            return "redirect:/admin";
        }
    }
    @PostMapping  (value = "/admin/updateUser")
    public String updateUser(@RequestParam("id") Long id,@RequestParam("role") String role, @ModelAttribute("user") User user) {
        User userUpdate = userServiceImpl.findById(id).orElse(null);
        userUpdate.setId(user.getId());
        userUpdate.setFirstname(user.getFirstname());
        userUpdate.setLastname(user.getLastname());
        userUpdate.setAge(user.getAge());
        userUpdate.setPassword(user.getPassword());
        userUpdate.setRoles(user.getRoles());
        userServiceImpl.updateUser(role,userUpdate);
        return "redirect:/admin";
    }

    @PostMapping(value = "/admin/deleteUser")
    public String deleteUser(@RequestParam long id) {
        userServiceImpl.deleteUser(id);
        return "redirect:/admin";
    }
}
