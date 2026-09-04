package es.tokioschool.filmotokio.controller;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AdminBatchController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job exportFilmsJob;

    @PostMapping("/api/admin/export-films")
    public Map<String, String> exportFilms() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

        JobExecution execution = jobLauncher.run(exportFilmsJob, params);
        BatchStatus status = execution.getStatus();

        Map<String, String> response = new HashMap<>();
        response.put("status", status.toString());
        return response;
    }
}
