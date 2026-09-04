package es.tokioschool.filmotokio.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "film")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Integer year;
    private Integer duration;
    private String sypnosis;
    private String poster;
    private boolean migrate;
    private LocalDate dateMigrate;

    // ManyToMany - Film é o lado "dono" (define a tabela de junção)
    @ManyToMany
    @JoinTable(
            name = "film_actor",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private Set<Person> actors = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "film_musician",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private Set<Person> musicians = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "film_screenwriter",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private Set<Person> screenwriters = new HashSet<>();

    // ManyToOne - cada filme tem exatamente 1 fotógrafo e 1 realizador
    @ManyToOne
    @JoinColumn(name = "photographer_id")
    private Person photographer;

    @ManyToOne
    @JoinColumn(name = "director_id")
    private Person director;

    // OneToMany - críticas e pontuações associadas a este filme
    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL)
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL)
    private Set<Score> scores = new HashSet<>();
}
