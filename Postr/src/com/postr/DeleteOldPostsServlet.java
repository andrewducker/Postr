package com.postr;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class DeleteOldPostsServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			{
					DAO.DeleteOldPosts();
			}
}
