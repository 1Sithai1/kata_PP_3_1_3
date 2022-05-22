package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class AdminController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String allUser(Model model, Principal principal) {
        model.addAttribute("userPrincipal", userService.findByUsername(principal.getName()));
        model.addAttribute("users", userService.allUsers());
        model.addAttribute("roleList", userService.allRoles());
        model.addAttribute("newUser", new User());
        return "admin";
    }

    @GetMapping("/admin/findUser")
    @ResponseBody
    public User findUserById(Long id) {
        return userService.findUserByID(id);
    }

    @PostMapping("/admin/newUser")
    public String create(@ModelAttribute("newUser") User user) {
        userService.addUser(user);
        return "redirect:/admin";
    }

    @PatchMapping("/admin/edit/{id}")
    public String update(@ModelAttribute("editUser")
                         User user,
                         @RequestParam(value = "roles", required = false) String[] roles,
                         @PathVariable("id") Long id) {
        Set<Role> roleSet = new HashSet<>();
        List<Role> allRoles = userService.allRoles();
        for (String roleByForm : roles) {
            for (Role role : allRoles) {
                if (role.getAuthority().equals(roleByForm)) {
                    roleSet.add(role);
                }
            }
        }
        user.setRoles(roleSet);
        userService.editUser(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
