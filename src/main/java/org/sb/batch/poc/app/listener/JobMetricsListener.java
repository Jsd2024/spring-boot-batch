package org.sb.batch.poc.app.listener;


import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class JobMetricsListener extends JobExecutionListenerSupport {

    private final MeterRegistry meterRegistry;

    public JobMetricsListener(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus().isUnsuccessful()) {
            // Increment a custom counter for failed jobs
            meterRegistry.counter("batch.job.failures", "job", jobExecution.getJobInstance().getJobName())
                    .increment();
        } else {
            // Increment a custom counter for successful jobs
            meterRegistry.counter("batch.job.success", "job", jobExecution.getJobInstance().getJobName())
                    .increment();
        }
    }
}
