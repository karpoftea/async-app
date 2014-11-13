package org.me.repository;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.google.common.util.concurrent.FutureCallback;

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
	public ResultSetFuture getTransfer(String networkType, String userId) {
		Select.Where query = QueryBuilder.select().all()
				.from("transfer")
				.where(eq("userId", userId))
				.and(eq("networkType", networkType));

		return session.executeAsync(query);
	}

	@Override
	public ResultSetFuture getProfile(String networkType, String userId) {
		Select.Where query = QueryBuilder.select().all()
				.from("profile")
				.where(eq("user_id", userId))
				.and(eq("network_type", networkType));

		return session.executeAsync(query);
	}
}
