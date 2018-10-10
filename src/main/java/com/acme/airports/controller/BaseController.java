package com.acme.airports.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.transform.TransformerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.acme.airports.dao.entity.Store;
import com.acme.airports.dao.repository.IStoreRepository;
import com.acme.airports.service.IStoreService;
import com.acme.airports.service.dto.NearestStores;

@RestController
@RequestMapping("/jumbo")
public class BaseController {		
	@Autowired
	IStoreService storeService;
	
	@Autowired
	IStoreRepository storeRepo;
	
	/** The entity manager. */
	@PersistenceContext
	private EntityManager entityManager;
	
	@RequestMapping(value = "/stores", method = RequestMethod.GET)
	public List<Store> getStores(@RequestParam(value = "city") final String city) throws TransformerException {			
		return storeService.findStoresByCity(city);		
	}

	@RequestMapping(value = "/nearestStores", method = RequestMethod.GET)
	public NearestStores getNearestStores(
			@RequestParam("latitude") final Double latitude,
			@RequestParam("longitude") final Double longitude,	
			@RequestParam(value = "radius",  defaultValue = "25") final Integer radius,
			@RequestParam(value = "maxResult", defaultValue = "5") final Integer maxResult
			) throws TransformerException {		
			return storeService.findNearestStores(latitude, longitude, radius, maxResult);
	}
	
	@RequestMapping(value = "/cities", method = RequestMethod.GET)
	public List<String> getUniqueCities() throws TransformerException {		
		return storeService.findUniqueCities();
	}
}
