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
}