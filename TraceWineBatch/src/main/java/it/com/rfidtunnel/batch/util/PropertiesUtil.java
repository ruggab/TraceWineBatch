package it.com.rfidtunnel.batch.util;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "batch.tunnel")
@Configuration
public class PropertiesUtil extends DefaultBatchConfigurer {
	private String user;
	private String password;
	private String article;
	private String cronExpression;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

}
