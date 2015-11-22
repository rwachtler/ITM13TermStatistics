package at.fhj.itm.pswe.rest;

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

import at.fhj.itm.pswe.model.Website;

@Stateless
@Path("/website")
public class WebsiteEndpoint{
	@PersistenceContext(unitName = "TermStatistics")
	private EntityManager em;
	
	@GET
	@Produces("application/json")
	public List<Website> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult)
	{
		TypedQuery<Website> findAllQuery = em.createQuery("SELECT DISTINCT w FROM Website w ORDER BY w.id", Website.class);
		
		if (startPosition != null)
	      {
	         findAllQuery.setFirstResult(startPosition);
	      }
	      if (maxResult != null)
	      {
	         findAllQuery.setMaxResults(maxResult);
	      }

		final List<Website> results = findAllQuery.getResultList();
		return results;
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
}