package org.me.web;

import com.datastax.driver.core.ResultSetFuture;
import org.me.domain.Profile;
import org.me.domain.Transfer;
import org.me.repository.Repository;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@WebServlet(urlPatterns = {"/asyncservlet"}, asyncSupported = true)
public class MyAsyncServlet extends HttpServlet {

	Repository repo = new Repository();

	Executor exec = Executors.newCachedThreadPool();

	@Override//try CompleteableFuture:
	//http://www.nurkiewicz.com/2013/05/java-8-definitive-guide-to.html
	//http://www.nurkiewicz.com/2013/05/java-8-completablefuture-in-action.html
	//http://www.ibm.com/developerworks/library/j-jvmc3/index.html
	//source: http://stackoverflow.com/questions/826212/java-executors-how-to-be-notified-without-blocking-when-a-task-completes

	//IO vs NIO vs NIO2
	//http://samolisov.blogspot.ru/2013/11/java.html
	//http://www.javaworld.com/article/2078654/java-se/five-ways-to-maximize-java-nio-and-nio-2.html
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		final AsyncContext asyncContext = req.startAsync();

		exec.execute(() -> {
			String userId = "2";
			String networkType = "vk";

			final ResultSetFuture profileFuture = repo.getProfileAsync(networkType, userId);
			final ResultSetFuture transferFuture = repo.getTransferAsync(networkType, userId);

			try {
				Profile profile = repo.toProfile(profileFuture);
				Transfer transfer = repo.toTransfer(transferFuture);

				PrintWriter writer = asyncContext.getResponse().getWriter();
				writer.write(String.format("%s-%s", profile, transfer));
			} catch (Exception e) {
				try (PrintWriter writer = asyncContext.getResponse().getWriter()) {
					writer.write("Error:" + e.getMessage());
				} catch (IOException io) {
					//ignore
				}
			} finally {
				asyncContext.complete();
			}
		});
	}
}