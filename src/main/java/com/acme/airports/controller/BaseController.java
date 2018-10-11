package com.acme.airports.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.transform.TransformerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.acme.airports.dao.entity.Store;
import com.acme.airports.service.IStoreService;
import com.acme.airports.service.dto.NearestStores;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("/geoapi/v1")
public class BaseController {		
	@Autowired
	IStoreService storeService;
	
	/** The entity manager. */
	@PersistenceContext
	private EntityManager entityManager;	
	
	@GetMapping(value = "/stores/by_geocoord.json")
	public NearestStores getNearestStores(
			@RequestParam("latitude") final Double latitude,
			@RequestParam("longitude") final Double longitude,	
			@RequestParam(value = "radius",  defaultValue = "25") final Integer radius,
			@RequestParam(value = "maxResult", defaultValue = "5") final Integer maxResult,
			@RequestParam(value = "showOpen", required = false) final Boolean showOpen
			) throws TransformerException {		
			return storeService.findNearestStores(latitude, longitude, radius, maxResult, showOpen);
	}
	
	@GetMapping(value = "/cities")
	public List<String> getUniqueCities() throws TransformerException {		
		return storeService.findUniqueCities();
	}
	
	@JsonView(Views.Lazy.class)
	@GetMapping(value = "/cities/{city}")
	public List<Store> getCity(
			@PathVariable("city") String cityName
			) throws TransformerException {		
		return storeService.findByCity(cityName);
	}
	
	@GetMapping(value = "/stores/search.json")
	public NearestStores getStoresByCityandStreet(
			@RequestParam("city") String cityName,
			@RequestParam("street") String streetName
			) throws TransformerException {		
		return storeService.findByCityAndStreet(cityName, streetName);
	}
}
