package com.acme.airports.service.dto;

import java.util.List;

import com.acme.airports.dao.entity.Store;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * This class is only used for persisting the data in the json data 
 * source into the in-memory database during application startup
 * @author Sahici
 * 
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Stores {
	public List<Store> getStores() {
		return stores;
	}

	public void setStores(List<Store> stores) {
		this.stores = stores;
	}

	private List<Store> stores;
}
