package it.com.rfidtunnel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class TraceWineBatchApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(TraceWineBatchApplication.class, args);
	}
   
}

