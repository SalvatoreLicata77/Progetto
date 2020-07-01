package it.enaip.corso.model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ArticlesController", urlPatterns = { "/ArticlesController" })
public class ArticlesController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DaoArticles articlesDao = DaoArticles.getInstance();
	private static final Logger LOGGER = Logger.getLogger(ArticlesController.class.getName());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String op = req.getParameter("op");

		try {

			switch (op) {
			case "new":
				showNewForm(req, resp);
				break;
			case "insert":
				insertArticles(req, resp);
				break;
			case "delete":
				deleteArticles(req, resp);
				break;
			case "edit":
				showEditForm(req, resp);
				break;
			case "update":
				updateArticles(req, resp);
				break;
			default:
				listArticles(req, resp);
				break;
			}
		} catch (SQLException e) {
			// For simplicity just Log the Exceptions
			LOGGER.log(Level.SEVERE, "SQL Error", e);
		}
	}

	private void updateArticles(HttpServletRequest req, HttpServletResponse resp)
			throws SQLException, IOException, ServletException {
		int id = Integer.parseInt(req.getParameter("id"));
		String name = req.getParameter("name");
		String description = req.getParameter("description");
		int quantity = Integer.parseInt(req.getParameter("quantity"));
		String location = req.getParameter("location");

		Articles articles = new Articles(id, name, description, quantity, location);
		articlesDao.update(articles);
		resp.sendRedirect("list");
	}

	private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
			throws SQLException, IOException, ServletException {
		String id = req.getParameter("id");
		Optional<Articles> existingArticles = articlesDao.find(id);
		RequestDispatcher dispatcher = req.getRequestDispatcher("jsp/ArticlesForm.jsp");
		existingArticles.ifPresent(s -> req.setAttribute("articles", s));
		dispatcher.forward(req, resp);
	}

	private void deleteArticles(HttpServletRequest req, HttpServletResponse resp)
			throws SQLException, IOException, ServletException {
		int id = Integer.parseInt(req.getParameter("id"));

		Articles articles = new Articles(id);
		articlesDao.delete(articles);
		resp.sendRedirect("list");
	}

	private void insertArticles(HttpServletRequest req, HttpServletResponse resp)
			throws SQLException, IOException, ServletException {
		String name = req.getParameter("name");
		String description = req.getParameter("description");
		int quantity = Integer.parseInt(req.getParameter("quantity"));
		String location = req.getParameter("location");

		Articles newArticles = new Articles(name, description, quantity, location);
		articlesDao.save(newArticles);
		resp.sendRedirect("list");
	}

	private void showNewForm(HttpServletRequest req, HttpServletResponse resp)
			throws SQLException, IOException, ServletException {

		RequestDispatcher dispatcher = req.getRequestDispatcher("jsp/ArticlesForm.jsp");
		dispatcher.forward(req, resp);
	}

	private void listArticles(HttpServletRequest req, HttpServletResponse resp)
			throws SQLException, IOException, ServletException {
		RequestDispatcher dispatcher = req.getRequestDispatcher("jsp/Stufflist.jsp");

		List<Articles> listArticles = articlesDao.findAll();
		req.setAttribute("listarticles", listArticles);

		dispatcher.forward(req, resp);
	}
}