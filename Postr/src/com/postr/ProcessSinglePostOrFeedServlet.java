package com.postr;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.postr.DataTypes.Outputs.BasePost;

@SuppressWarnings("serial")
public class ProcessSinglePostOrFeedServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		long key = Long.parseLong(req.getParameter("key"));
		BasePost toPost;
		try {
			toPost = DAO.LoadThing(BasePost.class, key);
		} catch (Exception e) {
			LogHandler.logException(this, e, "Uncaught Exception trying to load feed " + key);
			return;
		}
		try {
			toPost.PostOrSave();
		} catch (Exception e) {
			LogHandler.logException(this.getClass(), e, "Uncaught Exception trying to post feed");
			toPost.result = Result.Failure("An error occurred.  Please let andrew@ducker.org.uk know" + e.getMessage());
			try {
				DAO.SaveThing(toPost);
			} catch (Exception e1) {
				LogHandler.logException(this.getClass(), e1, "Uncaught Exception trying to save errored feed");
			}
		}
	}
}
