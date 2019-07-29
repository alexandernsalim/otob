package otob.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import otob.model.entity.User;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
    Boolean existsByEmail(String email);
    long deleteByEmail(String email);
}
