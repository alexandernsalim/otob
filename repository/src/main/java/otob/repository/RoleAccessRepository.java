package otob.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import otob.model.entity.RoleAccess;

public interface RoleAccessRepository extends MongoRepository<RoleAccess, String> {

    RoleAccess findByIdEquals(String role);

}
