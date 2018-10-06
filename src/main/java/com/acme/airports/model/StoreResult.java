package com.acme.airports.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown=true)
public class StoreResult {
	@Id
    private Integer sapStoreID;
	private String city;
	private String addressName;
	private Double latitude;
	private Double longitude;
    private Double distance;
	public Integer getSapStoreID() {
		return sapStoreID;
	}
	public void setSapStoreID(Integer sapStoreID) {
		this.sapStoreID = sapStoreID;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddressName() {
		return addressName;
	}
	public void setAddressName(String addressName) {
		this.addressName = addressName;
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
	public Double getDistance() {
		return distance;
	}
	public void setDistance(Double distance) {
		this.distance = distance;
	}
}
