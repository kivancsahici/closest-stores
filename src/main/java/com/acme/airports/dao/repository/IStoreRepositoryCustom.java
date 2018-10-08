package com.acme.airports.dao.repository;

import java.util.List;

import com.acme.airports.dao.entity.StoreResult;

public interface IStoreRepositoryCustom {
	public List<StoreResult> findByLatitudeAndLongitude(Double latitude, Double longitude);
}
