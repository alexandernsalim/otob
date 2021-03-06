package otob.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import otob.model.entity.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
    Role findByName(String name);
}
