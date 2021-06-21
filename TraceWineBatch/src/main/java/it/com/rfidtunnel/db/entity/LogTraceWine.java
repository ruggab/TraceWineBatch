package it.com.rfidtunnel.db.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Gabriele
 *
 */
@Entity
@Table(name = "log_trace_wine")
public class LogTraceWine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	private Long idSend;

	private Date dataInvio;

	private String esitoInvio;

	private String descError;
	private String idProduction;
	private String codeWO;
	private String codeArticle;
	private String intNBLigne;
	private String intNbArticle;


	public LogTraceWine() {
	}

	public Long getIdSend() {
		return idSend;
	}

	public void setIdSend(Long idSend) {
		this.idSend = idSend;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataInvio() {
		return dataInvio;
	}

	public void setDataInvio(Date dataInvio) {
		this.dataInvio = dataInvio;
	}

	public String getEsitoInvio() {
		return esitoInvio;
	}

	public void setEsitoInvio(String esitoInvio) {
		this.esitoInvio = esitoInvio;
	}

	public String getDescError() {
		return descError;
	}

	public void setDescError(String descError) {
		this.descError = descError;
	}

	public String getIdProduction() {
		return idProduction;
	}

	public void setIdProduction(String idProduction) {
		this.idProduction = idProduction;
	}

	public String getCodeWO() {
		return codeWO;
	}

	public void setCodeWO(String codeWO) {
		this.codeWO = codeWO;
	}

	public String getCodeArticle() {
		return codeArticle;
	}

	public void setCodeArticle(String codeArticle) {
		this.codeArticle = codeArticle;
	}

	public String getIntNBLigne() {
		return intNBLigne;
	}

	public void setIntNBLigne(String intNBLigne) {
		this.intNBLigne = intNBLigne;
	}

	public String getIntNbArticle() {
		return intNbArticle;
	}

	public void setIntNbArticle(String intNbArticle) {
		this.intNbArticle = intNbArticle;
	}
	
	

}
