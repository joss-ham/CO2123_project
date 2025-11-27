package co2123.streetfood.repo;

import co2123.streetfood.model.Review;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review,Integer> {
}
