package org.me.repository;

import org.me.domain.Transfer;

import java.util.concurrent.Future;

public interface TransferRepository {

	com.datastax.driver.core.ResultSetFuture getTransferAsync(String networkType, String userId);
}
