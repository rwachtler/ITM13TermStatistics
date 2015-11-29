package at.fhj.itm.pswe.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
	 * @param id domain ID
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
}