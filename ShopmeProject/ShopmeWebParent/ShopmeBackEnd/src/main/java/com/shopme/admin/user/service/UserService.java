package com.shopme.admin.user.service;

import com.shopme.admin.user.exception.UserNotFoundException;
import com.shopme.admin.user.repository.RoleRepository;
import com.shopme.admin.user.repository.UserRepository;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {

    public static final int USER_PER_PAGE = 10;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    // Se inyecta para encriptar las passwords
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getByEmail(String email){
        return userRepository.getUserByEmail(email);
    }

    public List<User> listAll(){
        // Para listar en asc order
        return (List<User>) userRepository.findAll(Sort.by("firstName").ascending());
    }

    // Se modifica para hacer Sort de los usuarios
    // Se vuelve a modificar para buscar por keyword
    public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword){
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, USER_PER_PAGE, sort);

        if(keyword != null){
            return userRepository.findAll(keyword, pageable);
        }

        return  userRepository.findAll(pageable);
    }

    public List<Role> listRoles(){
        return (List<Role>) roleRepository.findAll();
    }

    public User save(User user){
        boolean isUpdatingUser = (user.getId() != null);
        if (isUpdatingUser){
            User existingUser = userRepository.findById(user.getId()).get();
            // Se verifica que el formulario tenga una contrasena vacia
            // De ser asi, el User quiere mantener la pass
            if (user.getPassword().isEmpty()){
                user.setPassword(existingUser.getPassword());
            }else {
                encodePassword(user);
            }
        }else {
            encodePassword(user);
        }
        return userRepository.save(user);
    }

    // Se utiliza para actualizar los datos del usuario
    public User updateAccount(User userInForm){
        User userInDB = userRepository.findById(userInForm.getId()).get();

        if (!userInForm.getPassword().isEmpty()){
            userInDB.setPassword(userInForm.getPassword());
            encodePassword(userInDB);
        }

        if (userInForm.getPhotos() != null){
            userInDB.setPhotos(userInForm.getPhotos());
        }

        // Para mostrarlo en el navbar
        userInDB.setFirstName(userInForm.getFirstName());
        userInDB.setLastName(userInForm.getLastName());

        return userRepository.save(userInDB);
    }

    private void encodePassword(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    // Se anade Integer id luego de la funcion de JQuery en el formulario user_form.html
    // var params = { id: userId, email: userEmail, _csrf: csrfValue };
    // Necesita 2 parametros para poder actualizar un User
    public boolean isEmailUnique(Integer id, String email){
        User userByEmail = userRepository.getUserByEmail(email);

        // Al crear un usuario, se verifica que el email no se este utilizando
        if (userByEmail == null) return true;

        // Verifica que el usuario se esta editando
        boolean isCreatingNew = (id == null);

        if (isCreatingNew){
            if (userByEmail != null) return false;
        } else {
            if (userByEmail.getId() != id){
                return false;
            }
        }
        return true;
    }

    public User getUserById(Integer id) throws UserNotFoundException {
        try{
            return userRepository.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new UserNotFoundException("Could not find any user with ID " + id);
        }
    }

    public void delete(Integer id) throws UserNotFoundException {
        Long countById = userRepository.countById(id);
        if (countById == null || countById == 0){
            throw new UserNotFoundException("Could not find any user with ID " + id);
        }
        userRepository.deleteById(id);
    }

    public void updateUserEnabledStatus(Integer id, boolean enabled){
        userRepository.updateEnabledStatus(id, enabled);
    }










}
