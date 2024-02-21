package pdf.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pdf.models.Role;
import pdf.services.RoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("roles")
@CrossOrigin({ "*" })
public class RoleController {

    @Autowired
    RoleService service;

    @GetMapping("")
    public List<Role> getMethodName() {
        return service.getAllRoles();
    }

    @GetMapping("/{id}")
    public Role getRoleById(@PathVariable("id") Long id) {
        return service.getRoleById(id);
    }

    @PostMapping("/create")
    public Role createUser(@RequestBody Role user) {
        return service.create(user);
    }

    @PutMapping("/{id}")
    public Role updateUserById(@PathVariable Long id, @RequestBody Role user) {
        return service.updateById(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        service.deleteByIdUser(id);
    }

}
