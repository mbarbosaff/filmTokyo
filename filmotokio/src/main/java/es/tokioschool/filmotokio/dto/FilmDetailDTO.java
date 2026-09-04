package es.tokioschool.filmotokio.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FilmDetailDTO extends FilmDTO {
    private List<ReviewDTO> reviews;
    private Double mediaScore;
}