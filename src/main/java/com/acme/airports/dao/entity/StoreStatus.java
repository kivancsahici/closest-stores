package com.acme.airports.dao.entity;

import com.fasterxml.jackson.annotation.JsonValue; 

public enum StoreStatus {
	OPEN, CLOSING_SOON, CLOSED;
	
	@JsonValue
	public String toJson() {
		return name().toLowerCase();
	}
}
