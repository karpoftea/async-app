package org.me.repository;

import org.me.domain.Profile;

import java.util.concurrent.Future;

public interface UserProfileRepository {

	com.datastax.driver.core.ResultSetFuture getProfile(String networkType, String userId);
}
