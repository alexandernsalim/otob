package future.phase2.offlinetoonlinebazaar.repository;

import future.phase2.offlinetoonlinebazaar.model.entity.Bazaar;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BazaarRepository extends MongoRepository<Bazaar, String> {

}
