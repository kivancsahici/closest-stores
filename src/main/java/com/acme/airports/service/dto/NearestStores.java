package com.acme.airports.service.dto;

import java.util.List;

import com.acme.airports.dao.entity.StoreResult;

public class NearestStores {
	private List<StoreResult> nearestStores;
	private Double latitude;
	private Double longitude;
	public List<StoreResult> getNearestStores() {
		return nearestStores;
	}
	public void setNearestStores(List<StoreResult> nearestStores) {
		this.nearestStores = nearestStores;
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
