package at.fhj.itm.pswe.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the website database table.
 * 
 */
@Entity
@Table(name = "website")
public class Website implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "active")
	private boolean active;

	@Column(name = "description")
	private String description;

	@Column(name = "domain")
	private String domain;

	@Column(name = "crawldepth")
	private int crawldepth;

	@Column(name = "crawldate")
	private String crawldate;

	public int getCrawldepth() {
		return crawldepth;
	}

	public void setCrawldepth(int crawldepth) {
		this.crawldepth = crawldepth;
	}

	public Website() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean getActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDomain() {
		return this.domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getLast_crawldate() {
		return crawldate;
	}

	public void setLast_crawldate(String last_crawldate) {
		this.crawldate = last_crawldate;
	}

}