package otob.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import otob.model.entity.Bazaar;

import java.util.Date;
import java.util.List;

public interface BazaarRepository extends MongoRepository<Bazaar, Integer> {

    Bazaar findByBazaarId(Long bazaarId);
    List<Bazaar> findAllByBazaarEndDateAfter(Date date);

}
