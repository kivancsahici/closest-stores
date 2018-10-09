package com.acme.airports.service;

import java.util.List;
import com.acme.airports.dao.entity.Store;
import com.acme.airports.service.dto.NearestStores;

public interface IStoreService {
	public NearestStores findNearestStores(Double latitude, Double longitude);
	public List<Store> saveAll(List<Store> storeList);
	public List<String> findUniqueCities();
}
