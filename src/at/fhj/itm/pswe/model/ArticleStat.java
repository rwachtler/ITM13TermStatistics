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
 * The persistent class for the article_stat database table.
 * 
 */
@Entity
@Table(name = "article_stat")
public class ArticleStat implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "log_date")
	private String logDate;

	@Column(name = "analyzeduration")
	private long analyzeDuration;

	@ManyToOne
	@JoinColumn(name = "fk_article")
	private Article article;

	public ArticleStat() {
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public String getLogDate() {
		return logDate;
	}

	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}

	public long getAnalyzeDuration() {
		return analyzeDuration;
	}

	public void setAnalyzeDuration(long analyzeDuration) {
		this.analyzeDuration = analyzeDuration;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

}