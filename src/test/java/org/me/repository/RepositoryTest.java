package org.me.repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Row;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertNotNull;

public class RepositoryTest {

	Repository r = new Repository();

	@Test
	public void testGetProfile() throws ExecutionException, InterruptedException {
		ResultSetFuture future = r.getProfile("vk", "1");
		ResultSet rows = future.get();
		Row result = rows.one();
		assertNotNull(result);

		assertNotNull(result.getString("name"));
		assertNotNull(result.getString("user_id"));
		assertNotNull(result.getString("network_type"));
	}

	@Test
	public void testGetTransfer() throws ExecutionException, InterruptedException {
		ResultSetFuture future = r.getTransfer("vk", "2");
		ResultSet rows = future.get();
		Row result = rows.one();
		assertNotNull(result);

		assertNotNull(result.getString("trns_id"));
		assertNotNull(result.getString("user_id"));
		assertNotNull(result.getString("network_type"));
		assertNotNull(result.getString("value"));
		assertNotNull(result.getString("amount"));
	}
}
