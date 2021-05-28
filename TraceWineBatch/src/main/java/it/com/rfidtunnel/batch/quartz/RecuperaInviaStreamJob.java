package it.com.rfidtunnel.batch.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.com.rfidtunnel.db.services.DataStreamService;

@Component
@DisallowConcurrentExecution
public class RecuperaInviaStreamJob implements Job {

    @Autowired
    private DataStreamService jobService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
			jobService.inviaDati();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}