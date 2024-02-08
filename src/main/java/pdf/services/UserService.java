package pdf.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pdf.models.UserModel;
import pdf.repositories.UseRepository;

@Service
public class UserService {

    @Autowired
    UseRepository repository;

    public List<UserModel> getAllUsers() {
        return repository.findAll();
    }

    public UserModel getUserById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException(
                "No se encontro el usuario con el id" + id));
    }

    public UserModel createUser(UserModel user) {
        return repository.save(user);
    }

    public UserModel updateById(Long id, UserModel userDto) {
        UserModel user = repository.findById(id.longValue()).orElseThrow(() -> new NoSuchElementException(
                "No se encontro el usuario con el id" + id));
        BeanUtils.copyProperties(userDto, user, "id");
        return repository.save(user);
    }

    public void deleteByIdUser(Long id) {
        repository.deleteById(id);
    }
}
