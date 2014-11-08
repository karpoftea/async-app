package org.me.repository;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.google.common.util.concurrent.FutureCallback;
import org.me.domain.Profile;
import org.me.domain.Transfer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

public class Repository implements TransferRepository, UserProfileRepository {

	@Override
	public Future<Transfer> getTransfer(String networkType, String userId) {
		return null;
	}

	public Future<Profile> getProfile(String networkType, String userId) {
		Select.Where query = QueryBuilder.select().all()
				.from("profile")
				.where(eq("userId", userId))
				.and(eq("networkType", networkType));

		ResultSetFuture future = session.executeAsync(query);
		while (future.isDone() || future.isCancelled()) {

		}
		return null;
	}

	private Executor exec = Executors.newCachedThreadPool();

	static Session session;
	static {
		Cluster cluster = Cluster.builder()
				.addContactPoint("127.0.0.1")
				.build();
		session = cluster.connect("social");
	}



	private static class GetProfileCallback implements FutureCallback<ResultSet> {

		@Override
		public void onSuccess(ResultSet result) {
			if (result == null || result.one() == null) {
				return;
			}
		}

		@Override
		public void onFailure(Throwable t) {

		}
	}
}
