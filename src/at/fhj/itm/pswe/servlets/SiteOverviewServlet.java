package at.fhj.itm.pswe.servlets;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.fhj.itm.pswe.model.Website;

/**
 * Servlet implementation class SiteOverviewServlet
 */
@WebServlet("/SiteOverviewServlet/*")
public class SiteOverviewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@PersistenceUnit(unitName="TermStatistics")
	private EntityManagerFactory emf;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SiteOverviewServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("SERVLET REQUEST");
		System.out.println("PATHt: "+request.getPathInfo());
		
		
		
		
		String path=request.getPathInfo();
		int id;
		try{
			if(path != null && path.length()>1){
				id = Integer.parseInt(path.substring(1));
				System.out.println("ID: "+id);
				//Check if id is valid
				EntityManager em = emf.createEntityManager();
				System.out.println(em.toString());
				Website ws = em.find(Website.class,id);
				System.out.println("WS: "+ws);
				if(!(ws==null)){
					System.out.println(ws.getDomain());
					RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/sites/site-overview.html");
					view.forward(request, response); 
				}else{
					response.sendError(404);
				}


			}else
				//Either no subpath or just /
				response.sendError(404);
		}catch (NumberFormatException ex)
		{
			//subpath is not a valid number
			response.sendError(404);
		}
	}

}
