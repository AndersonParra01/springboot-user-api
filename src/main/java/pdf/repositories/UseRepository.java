package pdf.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import pdf.models.UserModel;

public interface UseRepository extends CrudRepository<UserModel, Long> {

    List<UserModel> findAll();

}