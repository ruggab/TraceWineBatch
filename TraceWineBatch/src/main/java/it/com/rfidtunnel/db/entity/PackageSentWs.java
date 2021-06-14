package it.com.rfidtunnel.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Gabriele
 *
 */
@Entity
@Table(name = "package_sent_ws")
public class PackageSentWs {

	@Id
	@Column(name = "id_send")
	private Long idSend;
	
	private String gtinbox;

	private String codewo;

	private String codearticle;

	private String nbtu;
	
	@Column(name = "tid_list")
	private String tidList;

	private boolean sent;
	
	

	public PackageSentWs() {
	}



	public Long getIdSend() {
		return idSend;
	}



	public void setIdSend(Long idSend) {
		this.idSend = idSend;
	}



	public String getGtinbox() {
		return gtinbox;
	}



	public void setGtinbox(String gtinbox) {
		this.gtinbox = gtinbox;
	}



	public String getCodewo() {
		return codewo;
	}



	public void setCodewo(String codewo) {
		this.codewo = codewo;
	}



	public String getCodearticle() {
		return codearticle;
	}



	public void setCodearticle(String codearticle) {
		this.codearticle = codearticle;
	}



	public String getNbtu() {
		return nbtu;
	}



	public void setNbtu(String nbtu) {
		this.nbtu = nbtu;
	}



	public String getTidList() {
		return tidList;
	}



	public void setTidList(String tidList) {
		this.tidList = tidList;
	}



	public boolean isSent() {
		return sent;
	}



	public void setSent(boolean sent) {
		this.sent = sent;
	}

	

	

	
	
	

}
