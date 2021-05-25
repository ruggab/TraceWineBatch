package it.com.rfidtunnel.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import it.com.rfidtunnel.batch.item.InvioStreamProcessor;
import it.com.rfidtunnel.batch.item.StreamReader;
import it.com.rfidtunnel.batch.item.StreamWriter;
import it.com.rfidtunnel.batch.listener.JobCompletionNotificationListener;
import it.com.rfidtunnel.batch.quartz.QuartzConfiguration;

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
@Import({ QuartzConfiguration.class })
public class BatchConfiguration extends DefaultBatchConfigurer {
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	// StreamReader (Reader)
	@Bean
	public StreamReader streamReader() {
		return new StreamReader();
	}

	// StreamReader (Reader)
	@Bean
	public StreamWriter streamWriter() {
		return new StreamWriter();
	}

	// InvioStreamProcessor (Processor)
	@Bean
	public InvioStreamProcessor invioStreamProcessor() {
		return new InvioStreamProcessor();
	}

	// JobCompletionNotificationListener (File loader)
	@Bean
	public JobExecutionListener listener() {
		return new JobCompletionNotificationListener();
	}

	@Bean
	public Step step1() {
		// The job is thus scheduled to run every 2 minute. In fact it should
		// be successful on the first attempt, so the second and subsequent
		// attempts should through a JobInstanceAlreadyCompleteException, so you have to set allowStartIfComplete to
		// true
		return stepBuilderFactory.get("step1").<Object, Object>chunk(10).reader(streamReader()).writer(streamWriter()).build();
	}

	// Configure job step
	@Bean
	public Job recuperaInviaStreamJob() {
		return jobBuilderFactory.get("recuperaInviaStreamJob").incrementer(new RunIdIncrementer()).flow(step1()).end().build();
	}

	@Override
	public void setDataSource(DataSource dataSource) {
		// override to do not set datasource even if a datasource exist.
		// initialize will use a Map based JobRepository (instead of database)
	}

}
