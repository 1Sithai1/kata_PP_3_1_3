package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class UserDaoImpl implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> allUsers() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    public void addUser(User user) {
        Role roleUser = findRoleByName("USER");
        user.addRole(roleUser);
        entityManager.persist(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = entityManager.find(User.class, id);
        entityManager.remove(user);
    }

    @Override
    public void editUser(Long id, User user) {
        if (user.getRoles().isEmpty()) {
            user.setRoles(getUserId(id).getRoles());
        }
        Set<Role> idRole = user.getRoles();
        Set<Role> newRole = new HashSet<>();
        for (Role role : idRole) {
            newRole.add(findRoleById(Long.valueOf(role.getName())));
        }
        user.setRoles(newRole);
        entityManager.merge(user);
    }

    @Override
    public User getUserId(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User findByUsername(String email) {
        return entityManager.createQuery("select user from  User user " +
                        "join fetch user.roles where user.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    public String userPass(Long id) {
        TypedQuery<String> query = entityManager
                .createQuery("select u.password from User u where u.id = :userId", String.class);
        return query.setParameter("userId", id).getSingleResult();
    }

    @Override
    public User findUserById(Long id) {
        return entityManager.createQuery("select user from User user " +
                        "join fetch user.roles where user.id = :id", User.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    // ******************************* Роли *********************************************************
    @Override
    public List<Role> allRoles() {
        return entityManager.createQuery("select r from Role r", Role.class).getResultList();
    }

    @Override
    public Role findRoleByName(String roleName) {
        TypedQuery<Role> query = entityManager
                .createQuery("select r from Role r where r.name = :roleName", Role.class);
        return query.setParameter("roleName", roleName).getSingleResult();
    }

    @Override
    public Role findRoleById(Long roleId) {
        TypedQuery<Role> query = entityManager
                .createQuery("select r from Role r where r.id = :roleId", Role.class);
        return query.setParameter("roleId", roleId).getSingleResult();
    }
}
