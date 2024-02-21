package pdf.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pdf.models.Role;
import pdf.repositories.RoleRepository;

@Service
public class RoleService {

    @Autowired
    RoleRepository repository;

    public List<Role> getAllRoles() {
        return repository.findAll();
    }

    public Role getRoleById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException(
                "No se encontro el rol con el id" + id));
    }

    public Role create(Role role) {
        return repository.save(role);
    }

    public Role updateById(Long id, Role roleDto) {
        Role role = repository.findById(id.longValue()).orElseThrow(() -> new NoSuchElementException(
                "No se encontro el usuario con el id" + id));
        BeanUtils.copyProperties(roleDto, role, "id");
        return repository.save(role);
    }

    public void deleteByIdUser(Long id) {
        repository.deleteById(id);
    }
}
