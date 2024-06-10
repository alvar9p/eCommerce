package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateNewUserWithOneRole(){
        Role roleAdmin = entityManager.find(Role.class, 7);
        User randomUser = new User("random@email.com", "password", "Name", "LastName");
        randomUser.addRole(roleAdmin);

        User savedUser = userRepository.save(randomUser);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateNewUserWithTwoRoles(){
        User multiRoleUser = new User("tworoles@email.com", "password", "Jhon", "Doe");

        Role roleEditor = new Role(9);
        Role roleAssistant = new Role(11);

        multiRoleUser.addRole(roleEditor);
        multiRoleUser.addRole(roleAssistant);

        User savedUser = userRepository.save(multiRoleUser);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers(){
        Iterable<User> listUsers = userRepository.findAll();
        listUsers.forEach(user -> System.out.println(user));
    }

    @Test
    public void testGetUserById(){
        User userRandom = userRepository.findById(2).get();
        System.out.println(userRandom);
        assertThat(userRandom).isNotNull();
    }

    @Test
    public void testUpdateUserDetails(){
        User userRandom = userRepository.findById(2).get();
        userRandom.setEnabled(true);
        userRandom.setEmail("test@details.com");

        userRepository.save(userRandom);
    }

    @Test
    public void testUpdateUserRoles(){
        User userTwoRoles = userRepository.findById(3).get();
        Role roleEditor = new Role(9);
        Role roleSalesPerson = new Role(8);

        userTwoRoles.getRoles().remove(roleEditor);
        userTwoRoles.addRole(roleSalesPerson);

        userRepository.save(userTwoRoles);
    }

    @Test
    public void testDeleteUser(){
        Integer userId = 3;
        userRepository.deleteById(userId);

    }

}
