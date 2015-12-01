package at.fhj.itm.pswe.servlets;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.fhj.itm.pswe.model.Word;

/**
 * Servlet implementation class SiteOverviewServlet
 * Delivers the siteoverview Subpage.
 */
@WebServlet("/WordOverview/*")
public class WordOverviewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@PersistenceUnit(unitName="TermStatistics")
	private EntityManagerFactory emf;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WordOverviewServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * Delivers WordOverview Subpage jsp
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		System.out.println("SERVLET REQUEST");
		System.out.println("PATHt: "+request.getPathInfo());
		*/
		
		//Pathinfo is whole path behind Servlet ( /Termstatistics/SiteOverview{/pathinfo including leading slash)
		String path=request.getPathInfo();
		String id;
		try{
			//Check if there is a subpath defined and not only slash
			if(path != null && path.length()>1){
				id = path.substring(1);
				System.out.println("ID: "+id);
				//Check if id is valid
				EntityManager em = emf.createEntityManager();
				Word wo = em.find(Word.class,id);
				//If really a word, continue
				if(!(wo==null)){
					//Set jsp attributes
					request.setAttribute("wordID", wo.getText());
					request.setAttribute("word", wo.getText());
					em.close();
					//Dispatch to jsp
					RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/sites/word-overview.jsp");
					view.forward(request, response); 
				}else{
					//TODO: Proper response
					response.sendError(404);
				}


			}else
				//Either no subpath or just /
				//TODO: Proper response
				response.sendError(404);
		}catch (NumberFormatException ex)
		{
			//subpath is not a valid number
			//TODO: Proper response
			response.sendError(404);
		}
	}

}
