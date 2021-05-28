package it.com.rfidtunnel.batch.quartz;

import java.util.Properties;

import javax.activation.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import it.com.rfidtunnel.batch.util.PropertiesUtil;

/**
 * The Class QuartzConfiguration.
 *
 * @author ashraf
 */
@Configuration
public class QuartzConfig {

	private ApplicationContext applicationContext;

	public QuartzConfig(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Bean
	public SpringBeanJobFactory springBeanJobFactory() {
		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		return jobFactory;
	}

	@Bean
	public JobDetailFactoryBean jobDetailFactoryBean() {
		JobDetailFactoryBean jobfactory = new JobDetailFactoryBean();
		jobfactory.setJobClass(RecuperaInviaStreamJob.class);
		return jobfactory;
	}

	// Job is scheduled after every 2 minute
	@Bean
	public CronTriggerFactoryBean cronTriggerFactoryBean() {
		CronTriggerFactoryBean ctFactory = new CronTriggerFactoryBean();
		ctFactory.setJobDetail(jobDetailFactoryBean().getObject());
		// ctFactory.setStartDelay(3000);
		// ctFactory.setName("cron_trigger");
		// ctFactory.setGroup("cron_group");
		// ctFactory.setCronExpression("*/10 * * * * ? *");
		ctFactory.setCronExpression(PropertiesUtil.getCronExpression());
		return ctFactory;
	}

	public static final int DEFAULT_THREAD_COUNT = 1;

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		Properties properties = new Properties();

		properties.setProperty("org.quartz.threadPool.threadCount", "1");

		SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
		scheduler.setTriggers(cronTriggerFactoryBean().getObject());
		scheduler.setOverwriteExistingJobs(true);
		scheduler.setAutoStartup(true);
		scheduler.setQuartzProperties(properties);
		// scheduler.setDataSource (dataSource);
		scheduler.setJobFactory(springBeanJobFactory());
		scheduler.setWaitForJobsToCompleteOnShutdown(true);
		return scheduler;
	}

}
