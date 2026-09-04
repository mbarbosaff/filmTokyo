package es.tokioschool.filmotokio.controller;

import es.tokioschool.filmotokio.model.Film;
import es.tokioschool.filmotokio.model.Person;
import es.tokioschool.filmotokio.repository.FilmRepository;
import es.tokioschool.filmotokio.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import es.tokioschool.filmotokio.model.Review;
import es.tokioschool.filmotokio.model.Score;
import es.tokioschool.filmotokio.model.User;
import es.tokioschool.filmotokio.repository.ReviewRepository;
import es.tokioschool.filmotokio.repository.ScoreRepository;
import es.tokioschool.filmotokio.repository.UserRepository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.security.Principal;
import java.time.LocalDate;

@Controller
public class FilmController {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/new-film")
    public String mostrarFormulario(Model model) {
        model.addAttribute("film", new Film());
        model.addAttribute("pessoas", personRepository.findAll());
        return "new-film";
    }

    @PostMapping("/new-film")
    public String criar(@ModelAttribute Film film,
                        @RequestParam Long directorId,
                        @RequestParam Long photographerId,
                        @RequestParam(required = false) List<Long> actorIds,
                        @RequestParam(required = false) List<Long> musicianIds,
                        @RequestParam(required = false) List<Long> screenwriterIds) {

        Person director = personRepository.findById(directorId)
                .orElseThrow(() -> new RuntimeException("Realizador não encontrado"));
        Person photographer = personRepository.findById(photographerId)
                .orElseThrow(() -> new RuntimeException("Fotógrafo não encontrado"));

        film.setDirector(director);
        film.setPhotographer(photographer);

        if (actorIds != null) {
            film.setActors(new HashSet<>(personRepository.findAllById(actorIds)));
        }
        if (musicianIds != null) {
            film.setMusicians(new HashSet<>(personRepository.findAllById(musicianIds)));
        }
        if (screenwriterIds != null) {
            film.setScreenwriters(new HashSet<>(personRepository.findAllById(screenwriterIds)));
        }

        filmRepository.save(film);

        return "redirect:/";
    }

    @GetMapping("/search-film")
    public String mostrarPesquisa() {
        return "search-film";
    }

    @GetMapping("/searched-film")
    public String pesquisar(@RequestParam String title, Model model) {
        model.addAttribute("films", filmRepository.findByTitleContainingIgnoreCase(title));
        model.addAttribute("query", title);
        return "searched-film";
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @GetMapping("/film/{id}")
    public String detalhe(@PathVariable Long id, Model model) {
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filme não encontrado"));
        model.addAttribute("film", film);

        double media = film.getScores().stream()
                .mapToInt(Score::getValue)
                .average()
                .orElse(0);
        model.addAttribute("mediaScore", media);

        return "film";
    }

    @PostMapping("/film/{id}/review")
    public String addReview(@PathVariable Long id,
                            @RequestParam String title,
                            @RequestParam String textReview,
                            Principal principal) {
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filme não encontrado"));
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));

        Review review = new Review();
        review.setTitle(title);
        review.setTextReview(textReview);
        review.setDate(LocalDate.now());
        review.setFilm(film);
        review.setUser(user);
        reviewRepository.save(review);

        return "redirect:/film/" + id;
    }

    @PostMapping("/film/{id}/score")
    public String addScore(@PathVariable Long id,
                           @RequestParam Integer value,
                           Principal principal) {
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filme não encontrado"));
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));

        Score score = new Score();
        score.setValue(value);
        score.setFilm(film);
        score.setUser(user);
        scoreRepository.save(score);

        return "redirect:/film/" + id;
    }
}
