package es.tokioschool.filmotokio.controller;

import es.tokioschool.filmotokio.dto.PersonDTO;
import es.tokioschool.filmotokio.dto.PersonMapper;
import es.tokioschool.filmotokio.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/persons")
public class PersonRestController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonMapper personMapper;

    @GetMapping
    public List<PersonDTO> listar() {
        return personRepository.findAll().stream()
                .map(personMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PersonDTO detalhe(@PathVariable Long id) {
        return personRepository.findById(id)
                .map(personMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada"));
    }
}
