package es.tokioschool.filmotokio.controller;

import es.tokioschool.filmotokio.model.Person;
import es.tokioschool.filmotokio.model.TypePersonEnum;
import es.tokioschool.filmotokio.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/new-person")
    public String mostrarFormulario(Model model) {
        model.addAttribute("person", new Person());
        model.addAttribute("tipos", TypePersonEnum.values());
        return "new-person";
    }

    @PostMapping("/new-person")
    public String criar(@ModelAttribute Person person) {
        personRepository.save(person);
        return "redirect:/";
    }
}
