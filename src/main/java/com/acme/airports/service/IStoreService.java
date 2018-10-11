package com.acme.airports.service;

import java.util.List;
import com.acme.airports.dao.entity.Store;
import com.acme.airports.service.dto.NearestStores;

public interface IStoreService {
	NearestStores findNearestStores(Double latitude, Double longitude, Integer radius, Integer maxResult, Boolean showOpen);
	List<Store> saveAll(List<Store> storeList);
	List<String> findUniqueCities();
	List<Store> findByCity(String city);
	NearestStores findByCityAndStreet(String city, String street);
}
