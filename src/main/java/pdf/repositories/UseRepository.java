package pdf.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

import pdf.models.UserModel;

public interface UseRepository extends CrudRepository<UserModel, Long> {

    List<UserModel> findAll();

    Optional<UserModel> findByUsername(String username);

}