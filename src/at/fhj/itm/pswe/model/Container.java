package at.fhj.itm.pswe.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The persistent class for the container database table.
 * 
 */
@Entity
@Table(name = "container")
public class Container implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "amount")
	private int amount;

	@Column(name = "log_date")
	private String logDate;

	// bi-directional many-to-one association to Website
	@ManyToOne
	@JoinColumn(name = "fk_website")
	private Website website;

	// bi-directional many-to-one association to Word
	@ManyToOne
	@JoinColumn(name = "fk_word")
	private Word word;

	// bi-directional many-to-one association to Article
	@ManyToOne
	@JoinColumn(name = "fk_article")
	private Article article;

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

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

}