package es.tokioschool.filmotokio.controller;

import es.tokioschool.filmotokio.model.Role;
import es.tokioschool.filmotokio.model.User;
import es.tokioschool.filmotokio.repository.RoleRepository;
import es.tokioschool.filmotokio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Controller
public class RegistoController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/registo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("user", new User());
        return "registo";
    }

    @PostMapping("/registo")
    public String registar(@ModelAttribute User user, Model model) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("erro", "Esse username já existe.");
            return "registo";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setCreationDate(LocalDate.now());

        Role roleUser = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role ROLE_USER não encontrada"));

        Set<Role> roles = new HashSet<>();
        roles.add(roleUser);
        user.setRoles(roles);

        userRepository.save(user);

        return "redirect:/login";
    }
}
