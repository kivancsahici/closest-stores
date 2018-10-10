package com.acme.airports.dao.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.acme.airports.controller.Views;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@JsonIgnoreProperties(ignoreUnknown=true)
public class Store implements Serializable{
	private static final long serialVersionUID = 2976102889581906993L;
	
	@Id
    private Integer sapStoreID;
	private String city;
	private String postalCode;
	
	@JsonView(Views.Lazy.class)
	private String street;
	
    private String addressName;
    private double longitude;
    private double latitude;
    private String todayClose;
    private String todayOpen;
    private String locationType;
    
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getAddressName() {
		return addressName;
	}
	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public Integer getSapStoreID() {
		return sapStoreID;
	}
	public void setSapStoreID(Integer sapStoreID) {
		this.sapStoreID = sapStoreID;
	}
	public String getTodayClose() {
		return todayClose;
	}
	public void setTodayClose(String todayClose) {
		this.todayClose = todayClose;	
	}
	public String getTodayOpen() {
		return todayOpen;
	}
	public void setTodayOpen(String todayOpen) {
		this.todayOpen = todayOpen;
	}
	public String getLocationType() {
		return locationType;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
}
