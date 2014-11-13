package org.me.repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Row;
import org.junit.Assert;
import org.junit.Test;
import org.me.domain.Profile;

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
}
