package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDAO {
    List<User> allUsers();

    void addUser(User user);

    void deleteUser(Long id);

    void editUser(Long id, User user);

    User getUserId(Long id);

    User findByUsername(String username);

    User findUserByID(Long id);

    List<Role> allRoles();

    Role findRoleByName(String roleName);

    String userPass(Long id);

}
