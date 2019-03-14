package future.phase2.offlinetoonlinebazaar.repository;


import future.phase2.offlinetoonlinebazaar.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);

    Long deleteByEmail(String email);
}
