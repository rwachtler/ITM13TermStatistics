package at.fhj.itm.pswe.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the word database table.
 * 
 */
@Entity
@Table(name = "wordtype")
public class Wordtype implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "texttype")
	private String texttype;

	// bi-directional many-to-one association to Container
	/*
	 * @OneToMany(mappedBy="word")
	 * 
	 * @Column(name="containers") private List<Container> containers;
	 */

	public Wordtype() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTexttype() {
		return texttype;
	}

	public void setTexttype(String texttype) {
		this.texttype = texttype;
	}

}