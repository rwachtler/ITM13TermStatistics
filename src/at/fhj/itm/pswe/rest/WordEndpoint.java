package at.fhj.itm.pswe.rest;

import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONArray;
import org.json.JSONObject;

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

	//For Datatable on Subiste "Website"
	@GET
	@Path("/{word}/websites")
	@Produces("application/json")
	public Response sitesOfWord(@PathParam("word") String word)
	{
		Query q = em
				.createQuery("SELECT c.website.id, c.website.domain, sum(c.amount)  "
						+ "FROM Container c WHERE c.word.text LIKE :word AND c.word.active = TRUE "
						+ "GROUP BY c.website ORDER BY sum(c.amount) DESC")
						.setParameter("word", word);

		List<Object[]> results	= q.getResultList();

		JSONArray returnResult=new JSONArray();

		for(Object[] wo: results){
			System.out.println(wo[0]+" | "+wo[1]);

			JSONObject temp= new JSONObject();
			temp.put("id", wo[0]);
			temp.put("adresse", wo[1]);
			temp.put("amount", wo[2]);

			returnResult.put(temp);
		}

		JSONObject my=new JSONObject();
		my.put("data", returnResult);

		return Response.ok(my.toString()).build();
	}

	@PUT
	@Produces("application/json")
	@Consumes("application/json")
	public Response editWord(String incoming)
	{
		System.out.println("Received PUT");
		JSONObject json= new JSONObject(incoming);
		System.out.println("JSON: "+json.toString());

		//Get KEY and
		Iterator<String> keys = json.keys();
		String id="";
		if( keys.hasNext() ){
			id = keys.next(); // First key in your json object
		}

		//CUpdate and Save object
		Word wo = em.find(Word.class, id);		
		if(json.getJSONObject(id).getJSONArray("active").length() == 0)
			wo.setActive(false);
		else
			wo.setActive(true);

		JSONObject output= new JSONObject();
		output.put("data", new JSONArray().put(findSingleWordWithAmount(id)));

		//Add info for Return object
		System.out.println("JSON: "+output.toString());		
		return Response.ok(output.toString()).build();
	}


	private JSONObject findSingleWordWithAmount(String word){
		List<Object[]> results = em
				.createQuery("SELECT w.text, w.active, sum(c.amount)  FROM Container c JOIN c.word w WHERE w.text = :word  GROUP BY w.text, w.active")
				.setParameter("word", word).getResultList();
		JSONObject temp= new JSONObject();

		if(!results.isEmpty()){

			temp.put("word", results.get(0)[0]);
			temp.put("amount", results.get(0)[2]);
			temp.put("active",results.get(0)[1]);
		}
		return temp;
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
	
	/**Ein Wort auf vorkommenden Seiten mit Anzahl und Datum mit Zeituebergabe Einschraenkung
	 * Analyse the amount of a word over a certain period
	 * @param word the word which should be counted
	 * @param startdate the date as String when the analysis should start
	 * @param enddate the date as String when the analysis should end
	 * @return List<Integer> with the amount of the word
	 * Example: http://localhost:8080/TermStatistics/rest/word/{someWord}/period/10.11.2015/30.11.2015
	 */
	@GET
	@Path("/{word:[a-zA-Z][a-zA-Z]*}/period/{startdate:[a-zA-Z0-9][a-zA-Z0-9.]*}/{enddate:[a-zA-Z0-9][a-zA-Z0-9.]*}")
	@Produces("application/json")
	public Response countWordOverPeriod(@PathParam("word") String word ,@PathParam("startdate") String startdate, @PathParam("enddate") String enddate){
		Query countWordPeriod = em.createQuery("SELECT co.logDate, SUM(co.amount) FROM Container co "
				+ "WHERE co.word.text = :word AND (co.logDate BETWEEN :startdate AND :enddate) "
				+ "GROUP BY co.logDate");
		countWordPeriod.setParameter("word", word);
		countWordPeriod.setParameter("startdate", startdate);
		countWordPeriod.setParameter("enddate", enddate);

		final List<Object[]> results = countWordPeriod.getResultList();

		JSONArray returnResult=new JSONArray();

		for(Object[] wo: results){
			System.out.println(wo[0]+" | "+wo[1]);

			JSONObject temp= new JSONObject();
			temp.put("date", wo[0]);
			temp.put("amount", wo[1]);

			returnResult.put(temp);
		}


		JSONObject my=new JSONObject();
		my.put(word, returnResult);
		System.out.println(my.toString());

		return Response.ok(my.toString()).build();		
	}
}