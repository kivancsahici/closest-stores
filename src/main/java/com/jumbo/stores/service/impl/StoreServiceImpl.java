package com.jumbo.stores.service.impl;

import java.time.Clock;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jumbo.stores.dao.entity.Store;
import com.jumbo.stores.dao.entity.StoreResult;
import com.jumbo.stores.dao.entity.StoreStatus;
import com.jumbo.stores.dao.repository.IStoreRepository;
import com.jumbo.stores.service.IStoreService;
import com.jumbo.stores.service.dto.NearestStores;

@Service
public class StoreServiceImpl implements IStoreService{
    private static final ZoneId zoneId = ZoneId.of("GMT+2");

    @Autowired(required=false)
    private Clock clock = Clock.system(zoneId);

	@Autowired
	IStoreRepository storeRepository;

	@Override
	public List<Store> saveAll(List<Store> storeList) {
		storeList.removeIf(s -> s.getTodayClose() != null && s.getTodayClose().equals("Gesloten"));
		return storeRepository.saveAll(storeList);
	}

	@Override
	public List<Store> findByCity(String city) {
		return storeRepository.findByCityOrderByStreet(city);
	}

	@Override
	public NearestStores findByCityAndStreet(String city, String street) {
		List<Store> storeList = storeRepository.findByCityAndStreetOrderByStreet(city, street);

		final LocalTime now = LocalTime.now(clock);

		final List<StoreResult> storeResultList = storeList.stream().map(store -> {
			StoreResult storeResult = new StoreResult();
			storeResult.setAddressName(store.getAddressName());
			storeResult.setCity(store.getCity());
			storeResult.setLatitude(store.getLatitude());
			storeResult.setLongitude(store.getLongitude());
			storeResult.setLocationType(store.getLocationType());
			storeResult.setSapStoreID(store.getSapStoreID());
			storeResult.setTodayOpen(store.getTodayOpen());
			storeResult.setTodayClose(store.getTodayClose());

			final LocalTime from = LocalTime.parse(store.getTodayOpen());
			final LocalTime to = LocalTime.parse(store.getTodayClose());

			if(now.isAfter(from) && now.isBefore(to)) {
				if(ChronoUnit.MINUTES.between(now, to) < 60)
					storeResult.setStoreStatus(StoreStatus.CLOSING_SOON);
				else
					storeResult.setStoreStatus(StoreStatus.OPEN);
			}
			else
				storeResult.setStoreStatus(StoreStatus.CLOSED);

			return storeResult;
		}).collect(Collectors.toList());

		NearestStores stores = new NearestStores();
		stores.setNearestStores(storeResultList);
		return stores;
	}

	@Override
	public NearestStores findNearestStores(Double latitude, Double longitude, Integer radius, Integer maxResult,
			Boolean showOpen) {
		List<StoreResult> storeList = storeRepository.findNearestStores(latitude, longitude, radius, maxResult);

		LocalTime now = LocalTime.now(clock);

		storeList.stream().forEach(result -> {
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
		});

		if(Boolean.TRUE.equals(showOpen))
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
