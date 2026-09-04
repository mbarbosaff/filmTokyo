package es.tokioschool.filmotokio.batch;

import es.tokioschool.filmotokio.dto.FilmCsvDTO;
import es.tokioschool.filmotokio.model.Film;
import es.tokioschool.filmotokio.repository.FilmRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class FilmCsvProcessor implements ItemProcessor<Film, FilmCsvDTO> {

    @Autowired
    private FilmRepository filmRepository;

    @Override
    public FilmCsvDTO process(Film film) {
        FilmCsvDTO dto = new FilmCsvDTO();
        dto.setId(film.getId());
        dto.setTitle(film.getTitle());
        dto.setYear(film.getYear());
        dto.setDuration(film.getDuration());
        dto.setDirector(film.getDirector() != null ? film.getDirector().getName() + " " + film.getDirector().getSurname() : "");
        dto.setPhotographer(film.getPhotographer() != null ? film.getPhotographer().getName() + " " + film.getPhotographer().getSurname() : "");

        film.setMigrate(true);
        film.setDateMigrate(LocalDate.now());
        filmRepository.save(film);

        return dto;
    }
}