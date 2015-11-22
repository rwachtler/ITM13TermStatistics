package at.fhj.itm.pswe.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import at.fhj.itm.pswe.model.Word;

@Stateless
@Path("/word")
public class WordEndpoint{
	@PersistenceContext(unitName = "TermStatistics")
	private EntityManager em;
	
	@GET
	@Path("/ids")
	@Produces("application/json")
	public List<Word> listAllIDs()
	{
		TypedQuery<Word> findAllQuery = em.createQuery("SELECT w FROM Word w", Word.class);

		final List<Word> results = findAllQuery.getResultList();
		return results;
	}
}
