package com.jumbo.stores.dao.repository;

import java.util.List;

import com.jumbo.stores.dao.entity.StoreResult;

public interface IStoreRepositoryCustom {
	public List<StoreResult> findNearestStores(Double latitude, Double longitude, Integer radius, Integer maxResult);
}
