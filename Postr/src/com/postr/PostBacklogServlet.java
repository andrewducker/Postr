package com.postr;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;

@SuppressWarnings("serial")
public class PostBacklogServlet extends HttpServlet {

	private static final Logger log = Logger.getLogger(PostBacklogServlet.class
			.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
				try{
					List<Long> feeds = DAO.LoadPostsInQueue();
					Queue queue = QueueFactory.getDefaultQueue();
					for (Long feedKey : feeds) {
						log.warning("Queuing up - " + feedKey.toString());
						queue.add(withUrl("/PostSingleFeed").param("key", feedKey.toString()));			
					}
				} catch (Exception e) {
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);

					e.printStackTrace(pw);
					log.severe("Uncaught Exception: " + sw.toString());
				}
			}

	
}
