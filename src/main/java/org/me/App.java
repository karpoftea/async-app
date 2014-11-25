package org.me;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.me.web.MyAsyncServlet;
import org.me.web.MyAsyncServlet2;

public class App {

	Server server;

	public void start() throws Exception {
		server = new Server(8080);
		ServletHandler servletHandler = new ServletHandler();
		servletHandler.addServletWithMapping(MyAsyncServlet.class, "/asyncservlet");
		servletHandler.addServletWithMapping(MyAsyncServlet2.class, "/asyncservlet2");
		server.setHandler(servletHandler);

		server.start();
	}

	public void stop() throws Exception {
		server.stop();
	}

	public static void main(String[] args) throws Exception {
		new App().start();
	}
}
