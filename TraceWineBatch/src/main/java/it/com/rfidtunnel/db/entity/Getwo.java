package it.com.rfidtunnel.db.entity;

import java.sql.Timestamp;
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
@Table(name = "getwo")
public class Getwo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idtrasmition")
	private Long idtrasmition;
	private String workorder;
	private Long globalqty;
	private Long boxqty;
	private Timestamp timeStamp;
	private String artname;
	private String artcode;
	private String dateschedule;

	public Getwo() {
	}

	public Long getIdtrasmition() {
		return idtrasmition;
	}

	public void setIdtrasmition(Long idtrasmition) {
		this.idtrasmition = idtrasmition;
	}

	public String getWorkorder() {
		return workorder;
	}

	public void setWorkorder(String workorder) {
		this.workorder = workorder;
	}

	public Long getGlobalqty() {
		return globalqty;
	}

	public void setGlobalqty(Long globalqty) {
		this.globalqty = globalqty;
	}

	public Long getBoxqty() {
		return boxqty;
	}

	public void setBoxqty(Long boxqty) {
		this.boxqty = boxqty;
	}

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getArtname() {
		return artname;
	}

	public void setArtname(String artname) {
		this.artname = artname;
	}

	public String getArtcode() {
		return artcode;
	}

	public void setArtcode(String artcode) {
		this.artcode = artcode;
	}

	public String getDateschedule() {
		return dateschedule;
	}

	public void setDateschedule(String dateschedule) {
		this.dateschedule = dateschedule;
	}
	
	

}
