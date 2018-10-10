package com.acme.airports.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.transform.TransformerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.acme.airports.dao.entity.Store;
import com.acme.airports.service.IStoreService;
import com.acme.airports.service.dto.NearestStores;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("/jumbo")
public class BaseController {		
	@Autowired
	IStoreService storeService;
	
	/** The entity manager. */
	@PersistenceContext
	private EntityManager entityManager;	

	@RequestMapping(value = "/nearestStores", method = RequestMethod.GET)
	public NearestStores getNearestStores(
			@RequestParam("latitude") final Double latitude,
			@RequestParam("longitude") final Double longitude,	
			@RequestParam(value = "radius",  defaultValue = "25") final Integer radius,
			@RequestParam(value = "maxResult", defaultValue = "5") final Integer maxResult
			) throws TransformerException {		
			return storeService.findNearestStores(latitude, longitude, radius, maxResult);
	}
	
	@RequestMapping(value = "/citiess", method = RequestMethod.GET)
	public List<String> getUniqueCities() throws TransformerException {		
		return storeService.findUniqueCities();
	}
	
	@JsonView(Views.Lazy.class)
	@RequestMapping(value = "/cities/{cityName}", method = RequestMethod.GET)
	public List<Store> getStoresByCity(
			@PathVariable("cityName") String cityName
			) throws TransformerException {		
		return storeService.findByCity(cityName);
	}
	
	@RequestMapping(value = "/cities/{cityName}/streets/{streetName}", method = RequestMethod.GET)
	public List<Store> getStoresByCityandStreet(
			@PathVariable("cityName") String cityName,
			@PathVariable("streetName") String streetName
			) throws TransformerException {		
		return storeService.findByCityAndStreet(cityName, streetName);
	}
}
