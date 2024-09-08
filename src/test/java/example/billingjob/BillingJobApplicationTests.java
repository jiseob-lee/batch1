package example.billingjob;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

@SpringBatchTest
@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class BillingJobApplicationTests {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private JobRepositoryTestUtils jobRepositoryTestUtils;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
    @Autowired
    private Job job;

    @Autowired
    private JobLauncher jobLauncher;

    @Test
    void testJobExecution(CapturedOutput output) throws Exception {
        // given
        //JobParameters jobParameters = new JobParameters();
    	
        // given
        //JobParameters jobParameters = new JobParametersBuilder()
                //.addString("input.file", "/some/input/file")
                //.addString("input.file", "src/main/resources/billing-2023-01.csv")
                //.toJobParameters();

        //JobParameters jobParameters = new JobParametersBuilder()
                //.addString("input.file", "E:\\work\\20240728\\input\\billing-2023-01.csv")
                //.addString("output.file", "E:\\work\\20240728\\staging\\billing-report-2023-01.csv")
                //.addJobParameter("data.year", 2023, Integer.class)
                //.addJobParameter("data.month", 1, Integer.class)
                //.toJobParameters();

        JobParameters jobParameters = new JobParametersBuilder()
                //.addJobParameter("input.file", "E:\\work\\20240728\\input\\billing-2023-01.csv", String.class)
                //.addJobParameter("output.file", "E:\\work\\20240728\\staging\\billing-report-2023-01.csv", String.class)
                .addJobParameter("input.file", "input\\billing-2023-01.csv", String.class)
                .addJobParameter("output.file", "staging\\billing-report-2023-01.csv", String.class)
                .addJobParameter("data.year", 2023, Integer.class)
                .addJobParameter("data.month", 1, Integer.class)
                .toJobParameters();
        
        //JobParameters jobParameters = this.jobLauncherTestUtils.getUniqueJobParametersBuilder()
        	      //.addString("input.file", "/some/input/file")
        	      //.toJobParameters();
        
        // when
        //JobExecution jobExecution = this.jobLauncher.run(this.job, jobParameters);
        JobExecution jobExecution = this.jobLauncherTestUtils.launchJob(jobParameters);

        // then
        //Assertions.assertTrue(output.getOut().contains("processing billing information"));
        //Assertions.assertTrue(output.getOut().contains("processing billing information from file /some/input/file"));
        //Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        
        Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        Assertions.assertTrue(Files.exists(Paths.get("staging", "billing-2023-01.csv")));
        
        Assertions.assertEquals(1000, JdbcTestUtils.countRowsInTable(jdbcTemplate, "BILLING_DATA"));
        
        Path billingReport = Paths.get("staging", "billing-report-2023-01.csv");
        Assertions.assertTrue(Files.exists(billingReport));
        Assertions.assertEquals(781, Files.lines(billingReport).count());
    }
    
    @BeforeEach
    public void setUp() {
    	this.jobRepositoryTestUtils.removeJobExecutions();
    	JdbcTestUtils.deleteFromTables(this.jdbcTemplate, "BILLING_DATA");
    }
}