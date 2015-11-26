package com.jurgen.blogex.filters;

import com.jurgen.blogex.daos.BlogExDAO;
import com.jurgen.blogex.entities.User;
import com.jurgen.blogex.listeners.InitListener;
import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminFilter implements Filter {
	private FilterConfig cfg;

	public void init(FilterConfig fConfig) throws ServletException {
		System.out.println("-------------------Admin filter installed--------------------");
		this.cfg = fConfig;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		if (req.getSession().getAttribute("currentUser") != null) {
			if (((User) req.getSession().getAttribute("currentUser")).isAdmin()) {
				if (req.getRequestURI().contains("AdminPage")) {
					BlogExDAO dao = (BlogExDAO) cfg.getServletContext().getAttribute(InitListener.DAO_ATTR);
					List<String> blacklist = dao.getBlackList();
					if (blacklist != null) {
						req.getSession().setAttribute("blacklist", blacklist);
					}
				}
				chain.doFilter(request, response);
				return;
			}
		}
		resp.sendRedirect(String.format("%s/HomePage.jsp", cfg.getServletContext().getContextPath()));
	}

	public void destroy() {

	}
}
