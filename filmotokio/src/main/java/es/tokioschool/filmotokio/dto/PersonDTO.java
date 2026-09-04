package es.tokioschool.filmotokio.dto;

import es.tokioschool.filmotokio.model.TypePersonEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonDTO {
    private Long id;
    private String name;
    private String surname;
    private TypePersonEnum type;
}
