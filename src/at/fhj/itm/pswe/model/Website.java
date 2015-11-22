package at.fhj.itm.pswe.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the website database table.
 * 
 */
@Entity
@NamedQuery(name="Website.findAll", query="SELECT w FROM Website w")
public class Website implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private byte active;

	private String description;

	private String domain;

	//bi-directional many-to-one association to Container
	@OneToMany(mappedBy="website")
	private List<Container> containers;

	public Website() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getActive() {
		return this.active;
	}

	public void setActive(byte active) {
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

	public List<Container> getContainers() {
		return this.containers;
	}

	public void setContainers(List<Container> containers) {
		this.containers = containers;
	}

	public Container addContainer(Container container) {
		getContainers().add(container);
		container.setWebsite(this);

		return container;
	}

	public Container removeContainer(Container container) {
		getContainers().remove(container);
		container.setWebsite(null);

		return container;
	}

}