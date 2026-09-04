package es.tokioschool.filmotokio.controller;

import es.tokioschool.filmotokio.dto.*;
import es.tokioschool.filmotokio.model.Film;
import es.tokioschool.filmotokio.model.Review;
import es.tokioschool.filmotokio.model.Score;
import es.tokioschool.filmotokio.model.User;
import es.tokioschool.filmotokio.repository.FilmRepository;
import es.tokioschool.filmotokio.repository.ReviewRepository;
import es.tokioschool.filmotokio.repository.ScoreRepository;
import es.tokioschool.filmotokio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/films")
public class FilmRestController {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FilmMapper filmMapper;

    @GetMapping
    public List<FilmDTO> listar() {
        return filmRepository.findAll().stream()
                .map(filmMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public FilmDetailDTO detalhe(@PathVariable Long id) {
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filme não encontrado"));
        return filmMapper.toDetailDTO(film);
    }

    @GetMapping("/search")
    public List<FilmDTO> pesquisar(@RequestParam String title) {
        return filmRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(filmMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/{id}/review")
    public ReviewDTO addReview(@PathVariable Long id,
                               @RequestBody ReviewCreateDTO request,
                               Principal principal) {
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filme não encontrado"));
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));

        Review review = new Review();
        review.setTitle(request.getTitle());
        review.setTextReview(request.getTextReview());
        review.setDate(LocalDate.now());
        review.setFilm(film);
        review.setUser(user);
        reviewRepository.save(review);

        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setTitle(review.getTitle());
        dto.setTextReview(review.getTextReview());
        dto.setDate(review.getDate());
        dto.setUsername(user.getUsername());
        return dto;
    }

    @PostMapping("/{id}/score")
    public ScoreDTO addScore(@PathVariable Long id,
                             @RequestBody ScoreCreateDTO request,
                             Principal principal) {
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filme não encontrado"));
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));

        Score score = new Score();
        score.setValue(request.getValue());
        score.setFilm(film);
        score.setUser(user);
        scoreRepository.save(score);

        ScoreDTO dto = new ScoreDTO();
        dto.setId(score.getId());
        dto.setValue(score.getValue());
        dto.setUsername(user.getUsername());
        return dto;
    }
}