package org.me.web;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Row;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
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
import java.util.List;

@WebServlet(urlPatterns={"/asyncservlet"}, asyncSupported=true)
public class MyAsyncServlet extends HttpServlet {

	Repository repo = new Repository();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		final AsyncContext asyncContext = req.startAsync();

		String userId = "100500";
		String networkType = "vk";

		try {
			final ResultSetFuture profileFuture = repo.getProfile(networkType, userId);
			final ResultSetFuture transferFuture = repo.getTransfer(networkType, userId);

			ListenableFuture<List<ResultSet>> future = Futures.allAsList(profileFuture, transferFuture);

			Futures.addCallback(future, new FutureCallback<List<ResultSet>>() {
				@Override
				public void onSuccess(List<ResultSet> result) {
					try {
						PrintWriter writer = asyncContext.getResponse().getWriter();
						if (result.size() != 2) {
							writer.write("Wrong results");
							return;
						}

						Profile profile = null;
						Transfer transfer = null;
						for (ResultSet resultSet : result) {
							Row row = resultSet.one();
							if (row == null) {
								writer.write("Missing results");
								return;
							}

							if (!row.isNull("name")) {
								profile = new Profile(row.getString("name"), row.getString("user_id"), row.getString("network_type"));
							} else {
								transfer = new Transfer(
										row.getString("trnsId"),
										row.getString("value"),
										row.getString("user_id"),
										row.getString("network_type"),
										row.getLong("amount")
								);
							}
						}

						writer.write(String.format("%s-%s", profile, transfer));
					} catch (IOException e) {
						throw new RuntimeException(e);
					} finally {
						asyncContext.complete();
					}
				}

				@Override
				public void onFailure(Throwable t) {
					try {
						asyncContext.getResponse().getWriter().write("Wrong");
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						asyncContext.complete();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			try {
				asyncContext.getResponse().getWriter().write("Error");
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			asyncContext.complete();
		}


	}
}
