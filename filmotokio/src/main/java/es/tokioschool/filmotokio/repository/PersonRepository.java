package es.tokioschool.filmotokio.repository;

import es.tokioschool.filmotokio.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}