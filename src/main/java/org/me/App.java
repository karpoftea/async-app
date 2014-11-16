package org.me;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.me.web.MyAsyncServlet;

public class App {

	Server server;

	public void start() throws Exception {
		server = new Server(8080);
		ServletHandler servletHandler = new ServletHandler();
		servletHandler.addServletWithMapping(MyAsyncServlet.class, "/asyncservlet");
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
