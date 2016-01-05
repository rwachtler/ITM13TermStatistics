package at.fhj.itm.pswe.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The persistent class for the word database table.
 * 
 */
@Entity
@Table(name = "word")
public class Word implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "text")
	private String text;

	@Column(name = "active")
	private boolean active;

	// bi-directional many-to-one association to Wordtype
	@ManyToOne
	@JoinColumn(name = "fk_wordtype")
	private Wordtype wordtype;

	public Word() {
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean getActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Wordtype getWordtype() {
		return wordtype;
	}

	public void setWordtype(Wordtype wordtype) {
		this.wordtype = wordtype;
	}

}