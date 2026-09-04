package es.tokioschool.filmotokio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FilmotokioApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilmotokioApplication.class, args);
	}
}