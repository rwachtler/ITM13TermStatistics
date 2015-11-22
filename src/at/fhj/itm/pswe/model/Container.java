package at.fhj.itm.pswe.model;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the container database table.
 * 
 */
@Entity
public class Container implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id")
	private int id;

	@Column(name="amount")
	private int amount;

	@Column(name="log_date")
	private String logDate;

	//bi-directional many-to-one association to Website
	@ManyToOne
	@JoinColumn(name="fk_website")
	private Website website;

	//bi-directional many-to-one association to Word
	@ManyToOne
	@JoinColumn(name="fk_word")
	private Word word;

	public Container() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getLogDate() {
		return this.logDate;
	}

	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}

	public Website getWebsite() {
		return this.website;
	}

	public void setWebsite(Website website) {
		this.website = website;
	}

	public Word getWord() {
		return this.word;
	}

	public void setWord(Word word) {
		this.word = word;
	}

}