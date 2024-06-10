package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.shopme.admin.user.repository.RoleRepository;
import com.shopme.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testCreateFirstRole(){
        Role roleAdmin = new Role("Admin", "Manage everything");
        Role savedRole = roleRepository.save(roleAdmin);
        assertThat(savedRole.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateRestRoles(){
        Role roleSalesPerson = new Role("Salesperson", "Manage product, price, customers, shipping, orders and sales report");
        Role roleEditorPerson = new Role("Editor", "Manage categories, brands, products, articles and menus");
        Role roleShipper = new Role("Shipper", "View products, view orders and update order status");
        Role roleAssistant = new Role("Assistant", "Manage questions and reviews");

        roleRepository.saveAll(List.of(roleSalesPerson, roleEditorPerson, roleShipper, roleAssistant));
    }
}
