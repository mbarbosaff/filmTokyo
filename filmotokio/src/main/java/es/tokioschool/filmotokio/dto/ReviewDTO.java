package es.tokioschool.filmotokio.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReviewDTO {
    private Long id;
    private String title;
    private String textReview;
    private LocalDate date;
    private String username;
}
