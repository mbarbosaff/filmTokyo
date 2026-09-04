package es.tokioschool.filmotokio.config;

import es.tokioschool.filmotokio.batch.FilmCsvProcessor;
import es.tokioschool.filmotokio.dto.FilmCsvDTO;
import es.tokioschool.filmotokio.model.Film;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.persistence.EntityManagerFactory;
import java.io.File;
import java.time.LocalDate;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private FilmCsvProcessor filmCsvProcessor;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean
    public JpaPagingItemReader<Film> filmItemReader() {
        JpaPagingItemReader<Film> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("SELECT f FROM Film f WHERE f.migrate = false");
        reader.setPageSize(10);
        return reader;
    }

    @Bean
    public FlatFileItemWriter<FilmCsvDTO> filmCsvWriter() {
        new File("exports").mkdirs();

        FlatFileItemWriter<FilmCsvDTO> writer = new FlatFileItemWriter<>();
        String filename = "exports/films_export_" + LocalDate.now() + ".csv";
        writer.setResource(new FileSystemResource(filename));

        BeanWrapperFieldExtractor<FilmCsvDTO> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"id", "title", "year", "duration", "director", "photographer"});

        DelimitedLineAggregator<FilmCsvDTO> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(";");
        lineAggregator.setFieldExtractor(fieldExtractor);

        writer.setLineAggregator(lineAggregator);
        writer.setHeaderCallback(w -> w.write("id;title;year;duration;director;photographer"));
        return writer;
    }

    @Bean
    public Step exportFilmsStep() {
        return stepBuilderFactory.get("exportFilmsStep")
                .<Film, FilmCsvDTO>chunk(10)
                .reader(filmItemReader())
                .processor(filmCsvProcessor)
                .writer(filmCsvWriter())
                .build();
    }

    @Bean
    public Job exportFilmsJob() {
        return jobBuilderFactory.get("exportFilmsJob")
                .start(exportFilmsStep())
                .build();
    }
}