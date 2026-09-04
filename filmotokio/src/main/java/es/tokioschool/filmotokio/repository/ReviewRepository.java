package es.tokioschool.filmotokio.repository;

import es.tokioschool.filmotokio.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}