package com.jumbo.stores.service;

import java.util.List;

import com.jumbo.stores.dao.entity.Store;
import com.jumbo.stores.service.dto.NearestStores;

public interface IStoreService {
	NearestStores findNearestStores(Double latitude, Double longitude, Integer radius, Integer maxResult, Boolean showOpen);
	List<Store> saveAll(List<Store> storeList);
	List<String> findUniqueCities();
	List<Store> findByCity(String city);
	NearestStores findByCityAndStreet(String city, String street);
}
