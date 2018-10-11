package com.acme.airports.dao.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

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
    private String todayOpen;
    private String todayClose;
    private String locationType;
    
    @Transient
    private StoreStatus storeStatus;
    
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
	public StoreStatus getStoreStatus() {
		return storeStatus;
	}
	public void setStoreStatus(StoreStatus storeStatus) {
		this.storeStatus = storeStatus;
	}
	public String getTodayOpen() {
		return todayOpen;
	}
	public void setTodayOpen(String todayOpen) {
		this.todayOpen = todayOpen;
	}
	public String getTodayClose() {
		return todayClose;
	}
	public void setTodayClose(String todayClose) {
		this.todayClose = todayClose;
	}
	public String getLocationType() {
		return locationType;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
}