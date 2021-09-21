package it.com.rfidtunnel.batch.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "batch.tunnel")
@Configuration
public class PropertiesUtil {
	private static String user;
	private static String password;
	private static String article;
	private static String cronExpression;
	private static String wsAuthUrl;
	private static String wsSynchUrl;
	private static String host;
	private static Integer idCompany;
	private static String application;
	private static String subject;
	private static String funStart;
	private static String funStop;
	private static String funSendtu;
	private static String funGetInfo;
	private static String maxnumsend;
	



	public static String getFunStart() {
		return funStart;
	}

	public  void setFunStart(String funStart) {
		this.funStart = funStart;
	}

	public static String getFunStop() {
		return funStop;
	}

	public  void setFunStop(String funStop) {
		this.funStop = funStop;
	}

	public static String getFunSendtu() {
		return funSendtu;
	}

	public  void setFunSendtu(String funSendtu) {
		this.funSendtu = funSendtu;
	}

	public static String getUser() {
		return user;
	}

	public  void setUser(String user) {
		this.user = user;
	}

	public static String getPassword() {
		return password;
	}

	public  void setPassword(String password) {
		this.password = password;
	}

	public static String getArticle() {
		return article;
	}

	public  void setArticle(String article) {
		this.article = article;
	}

	public static String getCronExpression() {
		return cronExpression;
	}

	public  void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public static String getHost() {
		return host;
	}

	public  void setHost(String host) {
		this.host = host;
	}

	

	public static Integer getIdCompany() {
		return idCompany;
	}

	public  void setIdCompany(Integer idCompany) {
		this.idCompany = idCompany;
	}

	public static String getApplication() {
		return application;
	}

	public  void setApplication(String application) {
		this.application = application;
	}

	public static String getWsAuthUrl() {
		return wsAuthUrl;
	}

	public  void setWsAuthUrl(String wsAuthUrl) {
		this.wsAuthUrl = wsAuthUrl;
	}

	public static String getWsSynchUrl() {
		return wsSynchUrl;
	}

	public  void setWsSynchUrl(String wsSynchUrl) {
		this.wsSynchUrl = wsSynchUrl;
	}

	public static String getSubject() {
		return subject;
	}

	public  void setSubject(String subject) {
		this.subject = subject;
	}
	
	public static String getMaxnumsend() {
		return maxnumsend;
	}

	public  void setMaxnumsend(String maxnumsend) {
		this.maxnumsend = maxnumsend;
	}

	public static String getFunGetInfo() {
		return funGetInfo;
	}

	public  void setFunGetInfo(String funGetInfo) {
		this.funGetInfo = funGetInfo;
	}

	
	
	

}
