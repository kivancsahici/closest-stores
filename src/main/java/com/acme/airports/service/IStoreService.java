package com.acme.airports.service;

import java.util.List;
import com.acme.airports.dao.entity.Store;
import com.acme.airports.service.dto.NearestStores;

public interface IStoreService {
	NearestStores findNearestStores(Double latitude, Double longitude, Integer radius, Integer maxResult);
	List<Store> saveAll(List<Store> storeList);
	List<String> findUniqueCities();
	List<Store> findByCity(String city);
	List<Store> findByCityAndStreet(String city, String street);
}
