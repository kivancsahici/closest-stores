package com.acme.airports.controller;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.transform.TransformerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acme.airports.service.dto.NearestStores;
import com.acme.airports.service.impl.StoreServiceImpl;

@RestController
@RequestMapping("/jumbo")
public class BaseController {		
	@Autowired
	StoreServiceImpl storeService;
	
	/** The entity manager. */
	@PersistenceContext
	private EntityManager entityManager;
	
	@RequestMapping(value = "/stores", method = RequestMethod.GET)
	public NearestStores getNearestStores(
			@RequestParam("latitude") double latitude,
			@RequestParam("longitude") double longitude
			) throws TransformerException {		
		return storeService.findNearestStores(latitude, longitude);
	}
}
