package at.fhj.itm.pswe.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
	
	/**Fuer eine Seite haeufigsten Woerter
	 * Reads the most frequent words of a page
	 * @param domain the side from which the word should be read
	 * @param max how much words should be read
	 * @return  Map<String, Integer> with the words and their amount
	 */
	@GET
    @Path("/oneSite/{id:[0-9][0-9]*}")
    @Produces("application/json")
	public Response findFrequentWordsOfSide(@PathParam("id") int id){
		TypedQuery<Container> wordsOneSite = em.createQuery("SELECT co FROM Container co WHERE co.website.id = :id order by co.amount desc", Container.class);
		wordsOneSite.setParameter("id", id);
		wordsOneSite.setMaxResults(200);
		
		final List<Container> results = wordsOneSite.getResultList();		
		
	    Collections.sort(results, new Comparator<Container>() {
	    	@Override
			public int compare(Container o1, Container o2) {
				return o1.getWord().getText().compareTo(o2.getWord().getText());
			}
	    });
		List<Container> words=new ArrayList<Container>();
		
		words.add(results.get(0));
		int p = 0;
		for(int i=1; i<results.size();i++){
			if(words.get(p).getWord().equals(results.get(i).getWord())){
				words.get(p).setAmount(words.get(p).getAmount()+results.get(i).getAmount());
			}else{
				words.add(results.get(i));
				p++;
			}
		}
		
		JSONArray returnResult=new JSONArray();
		for(Container c: words){
			JSONObject temp= new JSONObject();
			temp.put("word", c.getWord().getText());
			temp.put("amount", c.getAmount());
			returnResult.put(temp);
		}

		JSONObject my=new JSONObject();
		my.put("data", returnResult);

		return Response.ok(my.toString()).build();
	}
	
	/**Ein Wort auf vorkommenden Seiten mit Anzahl und Datum mit Zeituebergabe Einschraenkung
	 * Analyse the amount of a word over a certain period
	 * @param word the word which should be counted
	 * @param startdate the date as String when the analysis should start
	 * @param enddate the date as String when the analysis should end
	 * @return List<Integer> with the amount of the word
	 */
	@GET
    @Path("/period/{word:[a-zA-Z][a-zA-Z]*}/{startdate:[a-zA-Z0-9][a-zA-Z0-9.]*}/{enddate:[a-zA-Z0-9][a-zA-Z0-9.]*}")
    @Produces("application/json")
	public Response countWordOverPeriod(@PathParam("word") String word ,@PathParam("startdate") String startdate, @PathParam("enddate") String enddate){
		TypedQuery<Container> countWordPeriod = em.createQuery("SELECT co FROM Container co WHERE co.word.text = :word AND (co.logDate BETWEEN :startdate AND :enddate)",Container.class);
		countWordPeriod.setParameter("word", word);
		countWordPeriod.setParameter("startdate", startdate);
		countWordPeriod.setParameter("enddate", enddate);
		
		final List<Container> results = countWordPeriod.getResultList();
		
		int amount = 0;
		for(Container c: results) {
			amount += c.getAmount();
		}
		
		JSONObject temp= new JSONObject();
		temp.put("amount", amount);
		temp.put("word", word);
		

		JSONObject my=new JSONObject();
		my.put("data", temp);

		return Response.ok(my.toString()).build();		
	}
}