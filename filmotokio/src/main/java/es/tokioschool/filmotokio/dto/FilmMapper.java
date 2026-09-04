package es.tokioschool.filmotokio.dto;

import es.tokioschool.filmotokio.model.Film;
import es.tokioschool.filmotokio.model.Person;
import es.tokioschool.filmotokio.model.Review;
import es.tokioschool.filmotokio.model.Score;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FilmMapper {

    public FilmDTO toDTO(Film film) {
        FilmDTO dto = new FilmDTO();
        preencherCamposBase(dto, film);
        return dto;
    }

    public FilmDetailDTO toDetailDTO(Film film) {
        FilmDetailDTO dto = new FilmDetailDTO();
        preencherCamposBase(dto, film);

        List<ReviewDTO> reviewDTOs = film.getReviews().stream()
                .map(this::toReviewDTO)
                .collect(Collectors.toList());
        dto.setReviews(reviewDTOs);

        double media = film.getScores().stream()
                .mapToInt(Score::getValue)
                .average()
                .orElse(0);
        dto.setMediaScore(media);

        return dto;
    }

    private void preencherCamposBase(FilmDTO dto, Film film) {
        dto.setId(film.getId());
        dto.setTitle(film.getTitle());
        dto.setYear(film.getYear());
        dto.setDuration(film.getDuration());
        dto.setSypnosis(film.getSypnosis());
        dto.setPoster(film.getPoster());

        dto.setDirectorName(nomeCompleto(film.getDirector()));
        dto.setPhotographerName(nomeCompleto(film.getPhotographer()));

        dto.setActorNames(nomesDe(film.getActors()));
        dto.setMusicianNames(nomesDe(film.getMusicians()));
        dto.setScreenwriterNames(nomesDe(film.getScreenwriters()));
    }

    private ReviewDTO toReviewDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setTitle(review.getTitle());
        dto.setTextReview(review.getTextReview());
        dto.setDate(review.getDate());
        dto.setUsername(review.getUser().getUsername());
        return dto;
    }

    private String nomeCompleto(Person person) {
        if (person == null) return null;
        return person.getName() + " " + person.getSurname();
    }

    private List<String> nomesDe(java.util.Set<Person> pessoas) {
        if (pessoas == null) return List.of();
        return pessoas.stream()
                .map(this::nomeCompleto)
                .collect(Collectors.toList());
    }
}
