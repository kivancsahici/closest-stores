package com.acme.airports.dao.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown=true)
@NamedNativeQuery(name = "Store.fetchByCity", query = "SELECT sap_StoreID, city, postal_Code, street, address_Name, latitude, longitude, today_Close, today_Open, location_Type FROM Store a where a.city =:paramCity", 
resultClass=Store.class)
public class Store implements Serializable{
	private static final long serialVersionUID = 2976102889581906993L;
	
	@Id
    private Integer sapStoreID;
	private String city;
	private String postalCode;	
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
