package com.example.demo.config;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class BatchStepListener implements StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println(
                ">>> BEFORE STEP: " + stepExecution.getStepName()
        );
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println(
                ">>> AFTER STEP: " + stepExecution.getStepName()
                        + " - " + stepExecution.getStatus()
        );

        return stepExecution.getExitStatus();
    }
}
