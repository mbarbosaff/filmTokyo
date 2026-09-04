package es.tokioschool.filmotokio.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilmCsvDTO {
    private Long id;
    private String title;
    private Integer year;
    private Integer duration;
    private String director;
    private String photographer;
}
