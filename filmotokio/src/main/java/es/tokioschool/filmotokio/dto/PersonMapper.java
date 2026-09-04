package es.tokioschool.filmotokio.dto;

import es.tokioschool.filmotokio.model.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {

    public PersonDTO toDTO(Person person) {
        PersonDTO dto = new PersonDTO();
        dto.setId(person.getId());
        dto.setName(person.getName());
        dto.setSurname(person.getSurname());
        dto.setType(person.getType());
        return dto;
    }
}
