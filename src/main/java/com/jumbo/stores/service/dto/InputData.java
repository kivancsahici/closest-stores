package com.jumbo.stores.service.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jumbo.stores.dao.entity.Store;


/**
 * This class is only used for persisting the data in the json data 
 * source into the in-memory database during application startup
 * @author Sahici
 * 
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class InputData {
	public List<Store> getStores() {
		return stores;
	}

	public void setStores(List<Store> stores) {
		this.stores = stores;
	}

	private List<Store> stores;
}
