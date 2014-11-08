package org.me.repository;

import org.me.domain.Profile;

import java.util.concurrent.Future;

public interface UserProfileRepository {

	Future<Profile> getProfile(String networkType, String userId);
}
