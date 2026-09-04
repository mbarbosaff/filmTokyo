package es.tokioschool.filmotokio.controller;

import es.tokioschool.filmotokio.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private FilmRepository filmRepository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("films", filmRepository.findAll());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
