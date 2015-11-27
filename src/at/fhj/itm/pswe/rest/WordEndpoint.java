package at.fhj.itm.pswe.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONArray;
import org.json.JSONObject;

import at.fhj.itm.pswe.model.Container;
import at.fhj.itm.pswe.model.Website;
import at.fhj.itm.pswe.model.Word;

@Stateless
@Path("/word")
public class WordEndpoint{
	@PersistenceContext(unitName = "TermStatistics")
	private EntityManager em;

	/*
	@GET
	@Produces("application/json")
	public List<Word> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult)
	{
		TypedQuery<Word> findAllQuery = em.createQuery("SELECT DISTINCT w FROM Word w ORDER BY w.text", Word.class);

		if (startPosition != null)
		{
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null)
		{
			findAllQuery.setMaxResults(maxResult);
		}

		final List<Word> results = findAllQuery.getResultList();
		return results;
	}*/

	@GET
	@Produces("application/json")
	public Response listAllwithAmount()
	{
		List<Object[]> results = em
				.createQuery("SELECT w.text, w.active, sum(c.amount)  FROM Container c JOIN c.word w  GROUP BY w.text, w.active")
				.getResultList();
		JSONArray returnResult=new JSONArray();

		for(Object[] wo: results){
			JSONObject temp= new JSONObject();
			temp.put("word", wo[0]);
			temp.put("amount", wo[2]);
			temp.put("active",wo[1]);

			returnResult.put(temp);
		}

		JSONObject my=new JSONObject();
		my.put("data", returnResult);

		return Response.ok(my.toString()).build();
	}


	@GET
	@Path("/{id:[a-zA-Z][a-zA-Z]*}")
	@Produces("application/json")
	public Response findById(@PathParam("id") String id)
	{
		TypedQuery<Word> findByIdQuery = em.createQuery("SELECT DISTINCT w FROM Word w WHERE w.text = :text", Word.class);
		findByIdQuery.setParameter("text", id);
		Word entity;
		try
		{
			entity = findByIdQuery.getSingleResult();
		}
		catch (NoResultException nre)
		{
			entity = null;
		}
		if (entity == null)
		{
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(entity).build();
	}
	
	@GET
    @Path("/visibility/{word:[a-zA-Z][a-zA-Z]*}")
	public void changeVisibility(@PathParam("word") String word){
		TypedQuery<Word> visibility = em.createQuery("SELECT w FROM Word w WHERE w.text = :word", Word.class);
		visibility.setParameter("word",word);
		
		List<Word> words=visibility.getResultList();
		for(Word w : words){
			if(w.getActive()==0){
				w.setActive((byte) 1);
			}else {
				w.setActive((byte) 0);
			}
		}
	}
		
}