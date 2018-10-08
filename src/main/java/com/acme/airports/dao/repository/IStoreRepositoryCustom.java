package com.acme.airports.dao.repository;

import java.util.List;

import com.acme.airports.dao.entity.StoreResult;

public interface IStoreRepositoryCustom {
	public List<StoreResult> foo(Double latitude, Double longitude);
}
