package example.billingjob;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//@Configuration
@Slf4j
//@EnableScheduling
public class JobScheduler {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	
	// 참조 : https://96-brain.tistory.com/82

	//@Scheduled(cron = "1 * * * * *")
	@Scheduled(cron = "0 0/5 * * * *")
	public void jobSchduled() throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
			JobRestartException, JobInstanceAlreadyCompleteException {

		//Map<String, JobParameter> jobParametersMap = new HashMap<>();
		
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		Date time = new Date();

		String time1 = format1.format(time);

		//jobParametersMap.put("date",new JobParameter(time1));

		//JobParameters parameters = new JobParameters(jobParametersMap);

        JobParameters jobParameters = new JobParametersBuilder()
                //.addString("input.file", "/some/input/file")
                //.addString("input.file", "src/main/resources/billing-2023-01.csv")
                .addJobParameter("input.file", "input\\billing-2023-01.csv", String.class)
                .addJobParameter("output.file", "staging\\billing-report-2023-01.csv", String.class)
                .addJobParameter("data.year", 2023, Integer.class)
                .addJobParameter("data.month", 1, Integer.class)
                .addString("date", time1)
                .toJobParameters();

		JobExecution jobExecution = jobLauncher.run(job, jobParameters);

		while (jobExecution.isRunning()) {
			log.info("...");
		}

		log.info("Job Execution: " + jobExecution.getStatus());
		//log.info("Job getJobConfigurationName: " + jobExecution.getJobConfigurationName());
		log.info("Job getJobId: " + jobExecution.getJobId());
		log.info("Job getExitStatus: " + jobExecution.getExitStatus());
		log.info("Job getJobInstance: " + jobExecution.getJobInstance());
		log.info("Job getStepExecutions: " + jobExecution.getStepExecutions());
		log.info("Job getLastUpdated: " + jobExecution.getLastUpdated());
		log.info("Job getFailureExceptions: " + jobExecution.getFailureExceptions());
		
	}
}