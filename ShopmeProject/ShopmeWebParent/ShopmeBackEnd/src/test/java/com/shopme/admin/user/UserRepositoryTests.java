package com.shopme.admin.user;

import com.shopme.admin.user.repository.UserRepository;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {

    // The assertThat is one of the JUnit methods from the Assert object that
    // can be used to check if a specific value match to an expected one.

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateNewUserWithOneRole(){
        // Para buscar rol por su ID
        Role roleAdmin = entityManager.find(Role.class, 7);

        User randomUser = new User("random@email.com", "password", "Name", "LastName");
        randomUser.addRole(roleAdmin);
        User savedUser = userRepository.save(randomUser);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateNewUserWithTwoRoles(){
        // Se buscan roles por su ID
        Role roleEditor = new Role(9);
        Role roleAssistant = new Role(11);

        User multiRoleUser = new User("tworoles@email.com", "password", "Jhon", "Doe");
        // Es necesario crear el equals y hashcode en la clase User
        multiRoleUser.addRole(roleEditor);
        multiRoleUser.addRole(roleAssistant);
        User savedUser = userRepository.save(multiRoleUser);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers(){
        // Iterable porque se revisa el metodo findAll() de la Interface
        // Iterable<T> findAll();
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

    @Test
    public void testGetUserByEmail(){
        String email = "test@details.com";
        User user =  userRepository.getUserByEmail(email);
        assertThat(user).isNotNull();
    }

    @Test
    public void testCountById(){
        Integer id = 42;
        Long countById = userRepository.countById(id);
        assertThat(countById).isNotNull().isGreaterThan(0);
    }

    @Test
    public void testDisabledUser(){
        Integer id = 2;
        userRepository.updateEnabledStatus(id, false);
    }

    @Test
    public void testEnabledUser(){
        Integer id = 2;
        userRepository.updateEnabledStatus(id, true);
    }

    @Test
    public void testListFirstPage(){
        // Numero de pag y elementos por pagina
        int pageNumber = 1;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userRepository.findAll(pageable);
        List<User> listUsers = page.getContent();

        // Imprime los usuarios por pagina
        listUsers.forEach(System.out::println);
        assertThat(listUsers.size()).isEqualTo(pageSize);
    }

    @Test
    public void testSearchUsers(){
        String keyword = "a";

        int pageNumber = 0;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userRepository.findAll(keyword, pageable);
        List<User> listUsers = page.getContent();

        listUsers.forEach(System.out::println);

        assertThat(listUsers.size()).isGreaterThan(0);
    }

}
