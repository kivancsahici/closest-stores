package com.acme.airports.service.impl;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.acme.airports.dao.entity.Store;
import com.acme.airports.dao.entity.StoreResult;
import com.acme.airports.dao.entity.StoreStatus;
import com.acme.airports.dao.repository.IStoreRepository;
import com.acme.airports.service.IStoreService;
import com.acme.airports.service.dto.NearestStores;

@Service
public class StoreServiceImpl implements IStoreService{
	@Autowired
	IStoreRepository storeRepository;	
	
	@Override
	public List<Store> saveAll(List<Store> storeList) {				
		storeList.removeIf(s -> s.getTodayClose().equals("Gesloten"));
		return storeRepository.saveAll(storeList);
	}
	
	@Override
	public List<Store> findByCity(String city) {
		return storeRepository.findByCityOrderByStreet(city);
	}
	
	@Override
	public NearestStores findByCityAndStreet(String city, String street) {
		List<Store> storeList = storeRepository.findByCityAndStreetOrderByStreet(city, street);
		List<StoreResult> storeResultList = new ArrayList<>();
		
		LocalTime now = LocalTime.now(ZoneId.of("GMT+2"));
		
		for(Store store : storeList) {
			StoreResult storeResult = new StoreResult();
			storeResult.setAddressName(store.getAddressName());
			storeResult.setCity(store.getCity());
			storeResult.setLatitude(store.getLatitude());
			storeResult.setLongitude(store.getLongitude());
			storeResult.setLocationType(store.getLocationType());
			storeResult.setSapStoreID(store.getSapStoreID());
			storeResult.setTodayOpen(store.getTodayOpen());
			storeResult.setTodayClose(store.getTodayClose());
			
			LocalTime from = LocalTime.parse(storeResult.getTodayOpen());
			LocalTime to = LocalTime.parse(storeResult.getTodayClose());
			if(now.isAfter(from) && now.isBefore(to)) {
				if(ChronoUnit.MINUTES.between(now, to) < 60)
					storeResult.setStoreStatus(StoreStatus.CLOSING_SOON);
				else
					storeResult.setStoreStatus(StoreStatus.OPEN);
			}
			else
				storeResult.setStoreStatus(StoreStatus.CLOSED);
			storeResultList.add(storeResult);
		}
		NearestStores stores = new NearestStores();
		stores.setNearestStores(storeResultList);
		return stores;
	}
	
	@Override
	public NearestStores findNearestStores(Double latitude, Double longitude, Integer radius, Integer maxResult, Boolean showOpen) {		
		List<StoreResult> storeList = storeRepository.findNearestStores(latitude, longitude, radius, maxResult);
				
		//TODO undo comment out
		//LocalTime now = LocalTime.now(ZoneId.of("GMT+2"));
		LocalTime now = LocalTime.parse("20:10");
		
		for(StoreResult result : storeList) {
						
			LocalTime from = LocalTime.parse(result.getTodayOpen());
			LocalTime to = LocalTime.parse(result.getTodayClose());
			if(now.isAfter(from) && now.isBefore(to)) {
				if(ChronoUnit.MINUTES.between(now, to) < 60)
					result.setStoreStatus(StoreStatus.CLOSING_SOON);
				else
					result.setStoreStatus(StoreStatus.OPEN);
			}
			else
				result.setStoreStatus(StoreStatus.CLOSED);
		}
		if(showOpen)
			storeList.removeIf(s -> s.getStoreStatus().equals(StoreStatus.CLOSED));
		
		NearestStores nearestStores = new NearestStores();
		nearestStores.setNearestStores(storeList);
		nearestStores.setLatitude(latitude);
		nearestStores.setLongitude(longitude);
		return nearestStores;
	}
	
	@Override
	public List<String> findUniqueCities() {
		return storeRepository.findDistinctCities();
	}

}
