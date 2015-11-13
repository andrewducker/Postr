package com.postr;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;

@SuppressWarnings("serial")
public class PostBacklogServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
				try{
					List<Long> feeds = DAO.LoadPostsInQueue();
					Queue queue = QueueFactory.getDefaultQueue();
					for (Long feedKey : feeds) {
						LogHandler.logInfo(this,"Queuing up - " + feedKey.toString());
						queue.add(withUrl("/ProcessSinglePostOrFeed").param("key", feedKey.toString()));			
					}
				} catch (Exception e) {
					LogHandler.logException(this, e, "Uncaught Exception:");
				}
			}

	
}
