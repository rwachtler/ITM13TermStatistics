package at.fhj.itm.pswe.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the word database table.
 * 
 */
@Entity
@Table(name = "wordlist")
public class WordlistEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private int id;

	@Column(name = "Wort")
	private String word;

	@Column(name = "Worttyp")
	private String wordtype;

	@Column(name = "Geschlecht")
	private String sex;

	public WordlistEntry() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getWordtype() {
		return wordtype;
	}

	public void setWordtype(String wordtype) {
		this.wordtype = wordtype;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

}