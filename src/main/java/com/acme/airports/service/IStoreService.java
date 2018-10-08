package com.acme.airports.service;

import org.springframework.stereotype.Service;

import com.acme.airports.service.dto.NearestStores;

public interface IStoreService {
	public NearestStores findNearestStores(Double latitude, Double longitude);
}
