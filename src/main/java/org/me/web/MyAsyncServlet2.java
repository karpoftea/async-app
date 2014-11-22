package org.me.web;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Row;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Function;

@WebServlet(urlPatterns = {"/asyncservlet2"}, asyncSupported = true)
public class MyAsyncServlet2 extends HttpServlet {

	Repository repo = new Repository();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		final AsyncContext asyncContext = req.startAsync();

		final String networkType = "vk";
		final String userId = "2";

		CompletableFuture<Profile> profile = CompletableFuture.supplyAsync(() ->
						repo.getProfileAsync(networkType, userId)
		).thenApply(resultSetFuture -> {
			try {
				return repo.toProfile(resultSetFuture);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} catch (ExecutionException e) {
				throw new RuntimeException(e);
			}
		});
	}


}
