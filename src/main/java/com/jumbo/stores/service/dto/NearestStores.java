package com.jumbo.stores.service.dto;

import java.util.List;

import com.jumbo.stores.dao.entity.StoreResult;

public class NearestStores {
	private List<StoreResult> stores;
	private Double latitude;
	private Double longitude;
	public List<StoreResult> getStores() {
		return stores;
	}
	public void setNearestStores(List<StoreResult> nearestStores) {
		this.stores = nearestStores;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
}
