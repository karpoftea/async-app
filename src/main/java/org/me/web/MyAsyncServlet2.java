package org.me.web;

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

@WebServlet(urlPatterns = {"/asyncservlet2"}, asyncSupported = true)
public class MyAsyncServlet2 extends HttpServlet {

	Repository repo = new Repository();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		final AsyncContext asyncContext = req.startAsync();

		final String networkType = "vk";
		final String userId = "2";

		CompletableFuture<Transfer> transferFuture = CompletableFuture.supplyAsync(
				() -> repo.getTransferAsync(networkType, userId)
		).thenApply(
				resultSet -> {
					try {
						return repo.toTransfer(resultSet);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					} catch (ExecutionException e) {
						throw new RuntimeException(e);
					}
				}
		);

		CompletableFuture<Profile> profileFuture = CompletableFuture.supplyAsync(
				() -> repo.getProfileAsync(networkType, userId)
		).thenApply(resultSetFuture -> {
			try {
				return repo.toProfile(resultSetFuture);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} catch (ExecutionException e) {
				throw new RuntimeException(e);
			}
		});

		transferFuture
				.thenCombine(
						profileFuture,
						(transfer, profile) -> String.format("%s-%s", profile, transfer)
				)
				.thenAccept(
						result -> {
							try {
								PrintWriter writer = asyncContext.getResponse().getWriter();
								writer.write(result);
							} catch (IOException e) {
								e.printStackTrace();
							} finally {
								asyncContext.complete();
							}
						}
				)
				.exceptionally(
						throwable -> {
							try {
								PrintWriter writer = asyncContext.getResponse().getWriter();
								writer.write("Error has occurred:" + throwable.getMessage());
							} catch (IOException e) {
								e.printStackTrace();
							} finally {
								asyncContext.complete();
							}
							return null;
						}
				);
	}
}