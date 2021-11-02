package it.com.rfidtunnel.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Gabriele
 *
 */
@Entity
@Table(name = "settings")
public class Settings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	private String batchName;
	private String cronExpression;
	private String cronDescription;
	private String lastSend;
	private Integer limitMax;
	private Integer numMaxSend;
	

	public Settings() {
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getBatchName() {
		return batchName;
	}


	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}


	public String getCronExpression() {
		return cronExpression;
	}


	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}


	public String getLastSend() {
		return lastSend;
	}


	public void setLastSend(String lastSend) {
		this.lastSend = lastSend;
	}


	public String getCronDescription() {
		return cronDescription;
	}


	public void setCronDescription(String cronDescription) {
		this.cronDescription = cronDescription;
	}


	

	public Integer getNumMaxSend() {
		return numMaxSend;
	}


	public void setNumMaxSend(Integer numMaxSend) {
		this.numMaxSend = numMaxSend;
	}


	public Integer getLimitMax() {
		return limitMax;
	}


	public void setLimitMax(Integer limitMax) {
		this.limitMax = limitMax;
	}

	
	
	

}
