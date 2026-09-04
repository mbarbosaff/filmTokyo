package es.tokioschool.filmotokio.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FilmDTO {
    private Long id;
    private String title;
    private Integer year;
    private Integer duration;
    private String sypnosis;
    private String poster;
    private String directorName;
    private String photographerName;
    private List<String> actorNames;
    private List<String> musicianNames;
    private List<String> screenwriterNames;
}
