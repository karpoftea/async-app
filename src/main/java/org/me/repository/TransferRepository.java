package org.me.repository;

import org.me.domain.Transfer;

import java.util.concurrent.Future;

public interface TransferRepository {

	Future<Transfer> getTransfer(String networkType, String userId);
}
