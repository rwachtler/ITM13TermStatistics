package at.fhj.itm.pswe.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.sleepycat.je.rep.elections.Protocol.Result;

import at.fhj.itm.pswe.model.Container;
import at.fhj.itm.pswe.model.Website;
import at.fhj.itm.pswe.model.Word;

@Stateless
@Path("/container")
public class ContainerEndpoint{
	@PersistenceContext(unitName = "TermStatistics")
	private EntityManager em;
	
	@GET
	@Produces("application/json")
	public List<Container> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult)
	{
		TypedQuery<Container> findAllQuery = em.createQuery("SELECT DISTINCT c FROM Container c ORDER BY c.id", Container.class);
		
		if (startPosition != null)
	      {
	         findAllQuery.setFirstResult(startPosition);
	      }
	      if (maxResult != null)
	      {
	         findAllQuery.setMaxResults(maxResult);
	      }

		final List<Container> results = findAllQuery.getResultList();
		return results;
	}
	
	@GET
    @Path("/id/{id:[0-9][0-9]*}")
    @Produces("application/json")
    public Response findById(@PathParam("id") int id)
    {
       TypedQuery<Container> findByIdQuery = em.createQuery("SELECT DISTINCT c FROM Container c WHERE c.id = :id", Container.class);
       findByIdQuery.setParameter("id", id);
       Container entity;
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
    @Path("/date/{date:[a-zA-Z0-9][a-zA-Z0-9.]*}")
    @Produces("application/json")
    public Response findByDate(@PathParam("date") String date, @QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult)
    {
       TypedQuery<Container> findByDateQuery = em.createQuery("SELECT DISTINCT c FROM Container c WHERE c.logDate = :date ORDER BY c.amount DESC", Container.class);
       findByDateQuery.setParameter("date", date);
       List<Container> entities;
       if (startPosition != null)
      {
         findByDateQuery.setFirstResult(startPosition);
      }
      if (maxResult != null)
      {
         findByDateQuery.setMaxResults(maxResult);
      }
      
      entities = findByDateQuery.getResultList();
      
      return Response.ok(entities).build();
   }
	
	/**ein Wort auf allen Seiten
	 * reads the amount of one word for all pages
	 * @param word is the word which should be counted over all sides
	 * @return Map<String, Integer> with the domain where the word was found and how often it wos found
	 */
	@GET
    @Path("/wordSides/{word:[a-zA-Z][a-zA-Z]*}")
    @Produces("application/json")
	public Response findOneWordAllSides(@PathParam("word") String word){
		TypedQuery<Container> findWordAllSides =em.createQuery("SELECT co FROM Container co WHERE co.word.text= :word GROUP BY co.word",Container.class);
		findWordAllSides.setParameter("word", word);
		final List<Container> results = findWordAllSides.getResultList();		
		JSONArray returnResult=new JSONArray();

		for(Container c: results){
			JSONObject temp= new JSONObject();
			temp.put("domain", c.getWebsite().getDomain());
			temp.put("amount", c.getAmount());
			returnResult.put(temp);
		}

		JSONObject my=new JSONObject();
		my.put("data", returnResult);

		return Response.ok(my.toString()).build();
	}
	
	/**Fuer eine Seite haeufigsten Woerter
	 * Reads the most frequent words of a page
	 * @param domain the side from which the word should be read
	 * @param max how much words should be read
	 * @return  Map<String, Integer> with the words and their amount
	 */
	@GET
    @Path("/frequency/{domain:[a-zA-Z.][a-zA-Z.]*}/{max:[0-9]*}")
    @Produces("application/json")
	public Response findFrequentWordsOfSide(@PathParam("domain") String domain, @PathParam("max") int max){
		TypedQuery<Container> frequentwords = em.createQuery("SELECT co FROM Container co JOIN co.website we WHERE we.domain= :domain ORDER BY SUM(co.amount) DESC ", Container.class);
		frequentwords.setParameter("domain", "http://"+domain);
		frequentwords.setMaxResults(max);
		
		final List<Container> results = frequentwords.getResultList();		
		JSONArray returnResult=new JSONArray();

		for(Container c: results){
			JSONObject temp= new JSONObject();
			temp.put("word", c.getWord().getText());
			temp.put("amount", c.getAmount());
			returnResult.put(temp);
		}

		JSONObject my=new JSONObject();
		my.put("data", returnResult);

		return Response.ok(my.toString()).build();
	}
	
	
	/**Pro Wort auf vorkommenden Seiten mit Anzahl und Datum mit Zeituebergabe Einschraenkung
	 * Analyse the amount of a word over a certain period
	 * @param word the word which should be counted
	 * @param startdate the date as String when the analysis should start
	 * @param enddate the date as String when the analysis should end
	 * @return List<Integer> with the amount of the word
	 */
	@GET
    @Path("/period/{word:[a-zA-Z][a-zA-Z]*}/{startdate:[a-zA-Z0-9][a-zA-Z0-9.]*}/{enddate:[a-zA-Z0-9][a-zA-Z0-9.]*}")
    @Produces("application/json")
	public List<Container> countWordOverPeriod(@PathParam("word") String word ,@PathParam("startdate") String startdate, @PathParam("enddate") String enddate){
		TypedQuery<Container> countWordPeriod = em.createQuery("SELECT co FROM Container co WHERE co.word.text = :word AND (co.logDate BETWEEN :startdate AND :enddate)",Container.class);
		countWordPeriod.setParameter("word", word);
		countWordPeriod.setParameter("startdate", startdate);
		countWordPeriod.setParameter("enddate", enddate);
		
		final List<Container> results = countWordPeriod.getResultList();
		return results;
		
	}
	
	
	
}