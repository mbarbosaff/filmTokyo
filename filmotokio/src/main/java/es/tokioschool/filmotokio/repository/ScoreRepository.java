package es.tokioschool.filmotokio.repository;

import es.tokioschool.filmotokio.model.Score;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Long> {
}