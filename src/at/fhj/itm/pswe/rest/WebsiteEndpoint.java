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
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONArray;
import org.json.JSONObject;

import at.fhj.itm.pswe.model.Website;

@Stateless
@Path("/website")
public class WebsiteEndpoint{
	@PersistenceContext(unitName = "TermStatistics")
	private EntityManager em;

	@GET
	@Produces("application/json")
	public Response listAll()
	{

		TypedQuery<Website> findAllQuery = em.createQuery("SELECT DISTINCT w FROM Website w ORDER BY w.id", Website.class);
		final List<Website> results = findAllQuery.getResultList();		
		JSONArray returnResult=new JSONArray();

		for(Website ws: results){
			JSONObject temp= new JSONObject();
			temp.put("id", ws.getId());
			temp.put("address", ws.getDomain());
			temp.put("description", ws.getDescription());
			temp.put("active",ws.getActive());
			temp.put("depth", ws.getCrawldepth());
			returnResult.put(temp);
		}

		JSONObject my=new JSONObject();
		my.put("data", returnResult);

		return Response.ok(my.toString()).build();
	}

	//For Datatable on Subiste "Website"
	@GET
	@Path("/{id:[0-9][0-9]*}/words")
	@Produces("application/json")
	public Response wordsOfSite(@PathParam("id") int id)
	{

		JSONObject my=new JSONObject();
		my.put("data", findWordsOFSite(id, 0));

		return Response.ok(my.toString()).build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}/words/{num:[0-9][0-9]*}")
	@Produces("application/json")
	public Response wordsOfSiteNumbered(@PathParam("id") int id, @PathParam("num") int num)
	{

		JSONObject my=new JSONObject();
		my.put("data", findWordsOFSite(id, num));

		return Response.ok(my.toString()).build();
	}
	
	
	private JSONArray findWordsOFSite(int id, int maxNum){
		Query q = em
				.createQuery("SELECT c.word.text, sum(c.amount)  "
						+ "FROM Container c WHERE c.website.id=:id AND c.word.active = TRUE "
						+ "GROUP BY c.word ORDER BY sum(c.amount) DESC")
						.setParameter("id", id);
		
		if(maxNum > 0){
			q.setMaxResults(maxNum);
		}
		List<Object[]> results	= q.getResultList();

		JSONArray returnResult=new JSONArray();
		
		for(Object[] wo: results){
			System.out.println(wo[0]+" | "+wo[1]);

			JSONObject temp= new JSONObject();
			temp.put("word", wo[0]);
			temp.put("amount", wo[1]);

			returnResult.put(temp);
		}
		return returnResult;

	}




	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("id") int id)
	{
		TypedQuery<Website> findByIdQuery = em.createQuery("SELECT DISTINCT w FROM Website w WHERE w.id = :id", Website.class);
		findByIdQuery.setParameter("id", id);
		Website entity;
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


	@POST
	@Produces("application/json")
	@Consumes("application/json")
	public Response addSite(String incoming)
	{
		System.out.println("Received Request");
		JSONObject json= new JSONObject(incoming);
		System.out.println("JSON: "+json.toString());

		//Create Website object
		Website ws = new Website();
		ws.setDomain(json.getString("address"));
		ws.setDescription(json.getString("description"));
		ws.setCrawldepth(json.getInt("depth"));
		ws.setActive(true);

		//Save to DB
		em.persist(ws);
		em.flush();

		System.out.println("ID:" +ws.getId());

		//Add info for Return object
		json.put("id", ws.getId());
		json.put("active", ws.getActive());
		System.out.println("JSON: "+json.toString());

		//TODO STart crawler


		return Response.ok(new JSONObject().put("data", json).toString()).build();
	}

	@DELETE
	@Produces("application/json")
	@Consumes("application/json")
	public Response deleteSite(String incoming)
	{
		System.out.println("Received DELETE");
		JSONObject json= new JSONObject(incoming);
		System.out.println("JSON: "+json.toString());

		//Get KEY and
		Iterator<String> keys = json.keys();
		String id="";
		if( keys.hasNext() ){
			id = (String)keys.next(); // First key in your json object
		}
		Website ws = em.find(Website.class, Integer.parseInt(id));
		em.remove(ws);


		//Send empty object on success

		return Response.ok("{}").build();
	}

	@PUT
	@Produces("application/json")
	@Consumes("application/json")
	public Response editSite(String incoming)
	{
		System.out.println("Received PUT");
		JSONObject json= new JSONObject(incoming);
		System.out.println("JSON: "+json.toString());

		//Get KEY and
		Iterator<String> keys = json.keys();
		String id="";
		if( keys.hasNext() ){
			id = (String)keys.next(); // First key in your json object
		}

		//CUpdate and Save object
		Website ws = em.find(Website.class, Integer.parseInt(id));
		ws.setDomain(json.getJSONObject(id).getString("address"));
		ws.setDescription(json.getJSONObject(id).getString("description"));
		ws.setCrawldepth(json.getJSONObject(id).getInt("depth"));

		if(json.getJSONObject(id).getJSONArray("active").length() == 0)
			ws.setActive(false);
		else
			ws.setActive(true);

		JSONObject output= new JSONObject();
		output.put("data", new JSONArray().put(
				new JSONObject().put("id", ws.getId())
				.put("address", ws.getDomain())
				.put("description", ws.getDescription())
				.put("depth",ws.getCrawldepth())
				.put("active", ws.getActive())
				));


		//Add info for Return object
		System.out.println("JSON: "+output.toString());		
		return Response.ok(output.toString()).build();
	}


	/**
	 * Returns all words from a given domain in the chosen period
	 * @param domainID site where words should be counted
	 * @param startdate the date as String when the analysis should start
	 * @param enddate the date as String when the analysis should end
	 * @return List<Integer> with the amount of the word
	 * Example: http://localhost:8080/TermStatistics/rest/website/period/1/10.11.2015/30.11.2015
	 */
	@GET
	@Path("/period/{domainID:[a-zA-Z][a-zA-Z]*}/{startdate:[a-zA-Z0-9][a-zA-Z0-9.]*}/{enddate:[a-zA-Z0-9][a-zA-Z0-9.]*}")
	@Produces("application/json")
	public Response countSiteOverPeriod(@PathParam("domainID") String domainID ,@PathParam("startdate") String startdate, @PathParam("enddate") String enddate){
		Query countWordPeriod = em.createQuery("SELECT co.logDate, SUM(co.amount) FROM Container co "
				+ "WHERE co.website = :domainID AND (co.logDate BETWEEN :startdate AND :enddate) "
				+ "GROUP BY co.logDate");
		countWordPeriod.setParameter("domainID", domainID);
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
		my.put("", returnResult);
		System.out.println(my.toString());

		return Response.ok(my.toString()).build();		
	}

}