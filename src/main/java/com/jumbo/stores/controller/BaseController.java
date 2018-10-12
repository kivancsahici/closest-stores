package com.jumbo.stores.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.jumbo.stores.dao.entity.Store;
import com.jumbo.stores.service.IStoreService;

@RestController
@RequestMapping("/geoapi/v1")
public class BaseController {		
	@Autowired
	IStoreService storeService;
	
	/** The entity manager. */
	@PersistenceContext
	private EntityManager entityManager;	
	
	@GetMapping(value = "/stores/by_geocoord.json")
	public ResponseEntity<?> getNearestStores(
			@RequestParam("latitude") final Double latitude,
			@RequestParam("longitude") final Double longitude,	
			@RequestParam(value = "radius",  defaultValue = "25") final Integer radius,
			@RequestParam(value = "maxResult", defaultValue = "5") final Integer maxResult,
			@RequestParam(value = "showOpen", required = false) final Boolean showOpen
			){		
		return ResponseEntity.ok()
		        .contentType(MediaType.APPLICATION_JSON)
		        .body(storeService.findNearestStores(latitude, longitude, radius, maxResult, showOpen));
	}
	
	@GetMapping(value = "/cities")
	public ResponseEntity<?> getUniqueCities() {		
		return ResponseEntity.ok()
		        .contentType(MediaType.APPLICATION_JSON)
		        .body(storeService.findUniqueCities());
	}
	
	@JsonView(Views.Lazy.class)
	@GetMapping(value = "/cities/{city}")
	public ResponseEntity<?> getCity(
			@PathVariable("city") String cityName) {
		List<Store> retval = storeService.findByCity(cityName);
		if(retval.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		else {
			return ResponseEntity.ok()
			        .contentType(MediaType.APPLICATION_JSON)
			        .body(retval);	 
		}
	}
	
	@GetMapping(value = "/stores/search.json")
	public ResponseEntity<?> getStoresByCityandStreet(
			@RequestParam("city") String cityName,
			@RequestParam("street") String streetName
			) {				
		return ResponseEntity.ok()
		        .contentType(MediaType.APPLICATION_JSON)
		        .body(storeService.findByCityAndStreet(cityName, streetName));
	}
}
