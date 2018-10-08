package com.acme.airports.service.impl;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acme.airports.dao.entity.StoreResult;
import com.acme.airports.dao.entity.StoreStatus;
import com.acme.airports.dao.repository.impl.StoreRepositoryImpl;
import com.acme.airports.service.IStoreService;
import com.acme.airports.service.dto.NearestStores;

@Service
public class StoreServiceImpl implements IStoreService{
	@Autowired
	StoreRepositoryImpl storeRepository;
	
	@Override
	public NearestStores findNearestStores(Double latitude, Double longitude) {		
		List<StoreResult> storeList = storeRepository.foo(latitude, longitude);
		NearestStores stores = new NearestStores();
		stores.setNearestStores(storeList);
		stores.setLatitude(latitude);
		stores.setLongitude(longitude);
		
		//TODO undo comment out
		LocalTime now = LocalTime.now(ZoneId.of("GMT+2"));
		//LocalTime now = LocalTime.parse("19:10");
		
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
		return stores;
	}

}
