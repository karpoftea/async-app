package org.me.repository;

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.google.common.util.concurrent.FutureCallback;
import org.me.domain.Profile;
import org.me.domain.Transfer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

public class Repository implements TransferRepository, UserProfileRepository {

	static Session session;
	static {
		Cluster cluster = Cluster.builder()
				.addContactPoint("127.0.0.1")
				.build();
		session = cluster.connect("my_social");
	}

	@Override
	public ResultSetFuture getTransferAsync(String networkType, String userId) {
		Select.Where query = QueryBuilder.select().all()
				.from("transfer")
				.where(eq("user_id", userId))
				.and(eq("network_type", networkType));

		return session.executeAsync(query);
	}

	@Override
	public ResultSetFuture getProfileAsync(String networkType, String userId) {
		Select.Where query = QueryBuilder.select().all()
				.from("profile")
				.where(eq("user_id", userId))
				.and(eq("network_type", networkType));

		return session.executeAsync(query);
	}

	public Transfer toTransfer(ResultSetFuture transferFuture) throws InterruptedException, java.util.concurrent.ExecutionException {
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

	public Profile toProfile(ResultSetFuture profileFuture) throws InterruptedException, java.util.concurrent.ExecutionException {
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
