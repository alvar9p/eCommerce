package com.shopme.admin.user.controller;

import com.shopme.admin.user.exception.UserNotFoundException;
import com.shopme.admin.user.service.UserService;
import com.shopme.admin.utils.FileUploadUtil;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // Traer el listado de todos los usuarios
    // Model porque retorna un model a la vista de Spring
    @GetMapping("/users")
    public String listFirstPage(Model model){
        // Se agrega para que al hacer click en 'Users', solo muestre la primera pagina
        // firstName viene de la clase User
        return listByPage(1, model, "firstName", "asc", null);

        // Se comenta para NO listar el total de users
        /* List<User> users = userService.listAll();
        model.addAttribute("users", users);
        return "users"; */
    }

    // Se agrega este metodo para mostrar cierta cantidad de usuarios por pagina
    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
                             @Param("sortField") String sortField, @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword){

        Page<User> page = userService.listByPage(pageNum, sortField, sortDir, keyword);
        List<User> listUsers = page.getContent();

        long startCount = (pageNum - 1) * UserService.USER_PER_PAGE + 1;
        long endCount = startCount + (UserService.USER_PER_PAGE - 1);

        if (endCount > page.getTotalElements()){
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("users", listUsers);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);
        return "users";
    }

    // Carga varios atributos antes de re-dirigir al formulario de creacion
    @GetMapping("users/new")
    public String newUser(Model model){
        List<Role> listRoles = userService.listRoles();
        User user = new User();
        user.setEnabled(true);
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        // Se utiliza para parametrizar el nombre de user_form.html
        model.addAttribute("pageTitle", "Create New User");
        return "user_form";
    }

    // Recibe el formulario de user_form.html
    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes,
                           @RequestParam("image")MultipartFile multipartFile) throws IOException {

    if (!multipartFile.isEmpty()){
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        user.setPhotos(fileName);
        User savedUser = userService.save(user);
        String uploadDir = "user-photos/" + savedUser.getId();

        // Antes de salvar el archivo, se limpia el directorio
        FileUploadUtil.cleanDirectory(uploadDir);
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
    } else {
        // Significa que el form no tiene ningun archivo subido
        if(user.getPhotos().isEmpty()) user.setPhotos(null);
        userService.save(user);
    }

        // Este mensaje se va a mostrar cuando el usuario se ha creado correctamente
        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");
        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try{
            // Con el path variable, traemos al usuario por su ID y listamos todos los roles
            User user = userService.getUserById(id);
            List<Role> listRoles = userService.listRoles();

            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "Edit User with ID number " + user.getId());
            model.addAttribute("listRoles", listRoles);
            return "user_form";
        }catch (UserNotFoundException exception){
            redirectAttributes.addFlashAttribute("message", exception.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes){
        try{
            userService.delete(id);
            redirectAttributes.addFlashAttribute("message", "The user ID " + id + " has been deleted successfully");
        }catch (UserNotFoundException exception){
            redirectAttributes.addFlashAttribute("message", exception.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable("id") Integer id,
                                          @PathVariable("status") boolean enabled,
                                          RedirectAttributes redirectAttributes){
        userService.updateUserEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The user ID " + id + " has been " + status;

        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/users";
    }
}
