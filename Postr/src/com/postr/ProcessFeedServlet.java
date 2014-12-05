package com.postr;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.postr.DataTypes.Outputs.BaseFeed;

@SuppressWarnings("serial")
public class ProcessFeedServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		long key = Long.parseLong(req.getParameter("key"));
		BaseFeed toPost;
		try {
			toPost = DAO.LoadThing(BaseFeed.class, key);
		} catch (Exception e1) {
			Logger log = Logger.getLogger(ProcessFeedServlet.class.getName());
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e1.printStackTrace(pw);
			log.severe("Uncaught Exception: " + sw.toString());
			return;
		}
		try {
			toPost.PostOrSave();
		} catch (Exception e) {
			Logger log = Logger.getLogger(ProcessFeedServlet.class.getName());
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			log.severe("Uncaught Exception: " + sw.toString());
			toPost.result = Result.Failure("An error occurred.  Please let andrew@ducker.org.uk know" + e.getMessage());
			DAO.SaveThing(toPost);
		}
	}

}
