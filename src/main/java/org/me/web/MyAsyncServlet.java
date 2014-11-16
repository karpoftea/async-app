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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@WebServlet(urlPatterns = {"/asyncservlet"}, asyncSupported = true)
public class MyAsyncServlet extends HttpServlet {

	Repository repo = new Repository();

	Executor exec = Executors.newCachedThreadPool();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		final AsyncContext asyncContext = req.startAsync();

		exec.execute(() -> {
			String userId = "2";
			String networkType = "vk";

			final ResultSetFuture profileFuture = repo.getProfile(networkType, userId);
			final ResultSetFuture transferFuture = repo.getTransfer(networkType, userId);

			try {
				Profile profile = toProfile(profileFuture);
				Transfer transfer = toTransfer(transferFuture);

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

	private Transfer toTransfer(ResultSetFuture transferFuture) throws InterruptedException, java.util.concurrent.ExecutionException {
		ResultSet transferResult = transferFuture.get();
		Row row = transferResult.one();
		return row == null ?
				null :
				new Transfer(
						row.getString("trns_id"),
						row.getString("value"),
						row.getString("user_id"),
						row.getString("network_type"),
						row.getString("amount")
				);
	}

	private Profile toProfile(ResultSetFuture profileFuture) throws InterruptedException, java.util.concurrent.ExecutionException {
		ResultSet profileResult = profileFuture.get();
		Row row = profileResult.one();
		return row == null ?
				null :
				new Profile(
						row.getString("name"),
						row.getString("user_id"),
						row.getString("network_type")
				);
	}
}
