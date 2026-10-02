package com.example.demo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// @Component  commenting to prevent automatic scheduling
public class BatchScheduler {
    private final JobLauncher jobLauncher;
    private final Job firstJob;

    public BatchScheduler(JobLauncher jobLauncher,Job firstJob){
        this.jobLauncher=jobLauncher;
        this.firstJob=firstJob;
    }
    @Scheduled(fixedDelay = 30000)
    public void runBatchJob() throws Exception{
        System.out.println(">>> Scheduler is launching the batch job!");
        jobLauncher.run(
            firstJob, 
        new JobParametersBuilder()
            .addLong(
                "scheduledTime", 
            System.currentTimeMillis()).toJobParameters()
    );
    }
}
