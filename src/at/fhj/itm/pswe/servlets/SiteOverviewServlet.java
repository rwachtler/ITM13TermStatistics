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

import at.fhj.itm.pswe.model.Website;

/**
 * Servlet implementation class SiteOverviewServlet Delivers the siteoverview
 * Subpage.
 */
@WebServlet("/SiteOverview/*")
public class SiteOverviewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@PersistenceUnit(unitName = "TermStatistics")
	private EntityManagerFactory emf;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SiteOverviewServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response) Delivers the .jsp Invoked on GET
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * System.out.println("SERVLET REQUEST"); System.out.println("PATHt: "
		 * +request.getPathInfo());
		 */

		// Pathinfo is whole path behind Servlet (
		// /Termstatistics/SiteOverview{/pathinfo including leading slash)
		String path = request.getPathInfo();
		int id;
		try {
			// Check if there is a subpath defined and not only slash
			if (path != null && path.length() > 1) {
				id = Integer.parseInt(path.substring(1));
				System.out.println("ID: " + id);
				// Check if id is valid
				EntityManager em = emf.createEntityManager();
				Website ws = em.find(Website.class, id);
				System.out.println("WS: " + ws);
				// If it is really a webiste, continue
				if (!(ws == null)) {
					System.out.println(ws.getDomain());
					// Set attribute of jsp page
					request.setAttribute("siteID", id);
					request.setAttribute("domain", ws.getDomain());
					em.close();
					// Dispatch to jsp
					RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/sites/site-overview.jsp");
					view.forward(request, response);
				} else {
					// TODO: Appropriate Response
					response.sendError(404);
				}
			} else
				// Either no subpath or just /
				// TODO: Appropriate Response
				response.sendError(404);
		} catch (NumberFormatException ex) {
			// subpath is not a valid number
			// TODO: Appropriate Response
			response.sendError(404);
		}
	}

}
