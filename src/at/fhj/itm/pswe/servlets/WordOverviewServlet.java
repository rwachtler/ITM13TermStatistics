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
import at.fhj.itm.pswe.model.Word;

/**
 * Servlet implementation class SiteOverviewServlet
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
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("SERVLET REQUEST");
		System.out.println("PATHt: "+request.getPathInfo());
		
		
		String path=request.getPathInfo();
		String id;
		try{
			if(path != null && path.length()>1){
				id = path.substring(1);
				System.out.println("ID: "+id);
				//Check if id is valid
				EntityManager em = emf.createEntityManager();
				Word wo = em.find(Word.class,id);
				
				
				
				
				if(!(wo==null)){
					request.setAttribute("wordID", wo.getText());
					request.setAttribute("word", wo.getText());
					RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/sites/word-overview.jsp");
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
