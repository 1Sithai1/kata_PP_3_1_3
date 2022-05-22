package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDAO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;


@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public List<User> allUsers() {
        return userDAO.allUsers();
    }

    public void addUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDAO.addUser(user);
    }

    public void deleteUser(Long id) {
        userDAO.deleteUser(id);
    }

    public void editUser(Long id, User user) {
        String rawPass = userPass(id);
        if (user.getPassword().equals(rawPass) || user.getPassword().equals("")){
            user.setPassword(rawPass);
            userDAO.editUser(id, user);
        } else {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userDAO.editUser(id, user);
        }
    }

    public User getUserId(Long id) {
        return userDAO.getUserId(id);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return user;
    }

    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public User findUserByID(Long id) {
        return userDAO.findUserByID(id);
    }

    public List<Role> allRoles() {
        return userDAO.allRoles();
    }

    public Role findRoleByName(String roleName) {
        return userDAO.findRoleByName(roleName);
    }

    public String userPass(Long id){
        return userDAO.userPass(id);
    }
}
