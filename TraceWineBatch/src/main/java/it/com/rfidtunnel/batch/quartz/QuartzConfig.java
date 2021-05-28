package it.com.rfidtunnel.batch.quartz;

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
//		ctFactory.setStartDelay(3000);
//		ctFactory.setName("cron_trigger");
//		ctFactory.setGroup("cron_group");
		// ctFactory.setCronExpression("*/10 * * * * ? *");
		ctFactory.setCronExpression(PropertiesUtil.getCronExpression());
		return ctFactory;
	}

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
		scheduler.setTriggers(cronTriggerFactoryBean().getObject());
		scheduler.setOverwriteExistingJobs(true);
		scheduler.setAutoStartup(true);
		//scheduler.setQuartzProperties (properties);
		//scheduler.setDataSource (dataSource);
		scheduler.setJobFactory(springBeanJobFactory());
		scheduler.setWaitForJobsToCompleteOnShutdown(true);
		return scheduler;
	}

}
