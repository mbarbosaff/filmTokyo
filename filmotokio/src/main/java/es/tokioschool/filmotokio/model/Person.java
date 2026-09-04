package es.tokioschool.filmotokio.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "person")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;

    @Enumerated(EnumType.STRING)
    private TypePersonEnum type;

    // Relações ManyToMany (o lado "dono" da relação está definido em Film)
    @ManyToMany(mappedBy = "actors")
    private Set<Film> filmsActor = new HashSet<>();

    @ManyToMany(mappedBy = "musicians")
    private Set<Film> filmsMusician = new HashSet<>();

    @ManyToMany(mappedBy = "screenwriters")
    private Set<Film> filmsScreenwriter = new HashSet<>();

    // Relações OneToMany (o lado "dono" - ManyToOne - está definido em Film)
    @OneToMany(mappedBy = "photographer")
    private Set<Film> filmsPhotographer = new HashSet<>();

    @OneToMany(mappedBy = "director")
    private Set<Film> filmsDirector = new HashSet<>();
}