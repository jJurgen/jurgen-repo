package com.jurgen.blogex.servlets;

import com.jurgen.blogex.daos.BlogExDAO;
import com.jurgen.blogex.entities.Post;
import com.jurgen.blogex.listeners.InitListener;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String byAuthor = req.getParameter("byAuthor");
		String byTitle = req.getParameter("byTitle");
		String byContent = req.getParameter("byContent");
		String byComments = req.getParameter("byComments");
		String searchReq = req.getParameter("searchReq");
		HashSet<Post> searchResult = new HashSet<>();
		if ((searchReq != null) && (!searchReq.equals(""))) {
			final BlogExDAO dao = (BlogExDAO) getServletContext().getAttribute(InitListener.DAO_ATTR);
			List<Post> resultList;
			if ((byAuthor != null) && (byAuthor.equals("on"))) {
				resultList = dao.getPostsByAuthor(searchReq);
				if (resultList != null) {
					searchResult.addAll(resultList);
				}
				System.out.println("filter by author!");
			}
			if ((byTitle != null) && (byTitle.equals("on"))) {
				resultList = dao.getPostsByTitle(searchReq);
				if(resultList != null){
					searchResult.addAll(resultList);
				}
				System.out.println("filter by title!");
			}

			if ((byContent != null) && (byContent.equals("on"))) {
				resultList = dao.getPostsByContent(searchReq);
				if(resultList != null){
					searchResult.addAll(resultList);
				}
				System.out.println("filter by Content!");
			}

			if ((byComments != null) && (byComments.equals("on"))) {
				resultList = dao.getPostsByComment(searchReq);
				if(resultList != null){
					searchResult.addAll(resultList);
				}
				System.out.println("filter by Comments!");
			}
		}
		
		req.setAttribute("searchResult", searchResult);
		RequestDispatcher rd = req.getRequestDispatcher("/SearchPage.jsp");
		rd.forward(req, resp);
	}

}
