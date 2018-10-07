package com.acme.airports.controller;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.xml.transform.TransformerException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acme.airports.model.NearestStores;
import com.acme.airports.model.StoreResult;
import com.acme.airports.model.StoreStatus;

@RestController
@RequestMapping("/jumbo")
public class BaseController {		
	/** The entity manager. */
	@PersistenceContext
	private EntityManager entityManager;
	
	@RequestMapping(value = "/stores", method = RequestMethod.GET)
	public NearestStores getNearestStores(
			@RequestParam("latitude") double latitude,
			@RequestParam("longitude") double longitude
			) throws TransformerException {		
		String baseQuery =   "SELECT * FROM (  "  + 
				 "   SELECT sap_storeid, city, address_name,  "  + 
				 "          latitude, longitude, distance, today_open, today_close  "  + 
				 "     FROM (  "  + 
				 "    SELECT z.sap_storeid, z.city, z.address_name,  "  + 
				 "           z.latitude, z.longitude, z.today_open, z.today_close, "  + 
				 "           p.radius,  "  + 
				 "           p.distance_unit  "  + 
				 "               * rad2deg * (ACOS(COS(deg2rad * (:paramLatitude))  "  + 
				 "                               * COS(deg2rad * (z.latitude))  "  + 
				 "                               * COS(deg2rad * (:paramLongitude - z.longitude))  "  + 
				 "                               + SIN(deg2rad * (:paramLatitude))  "  + 
				 "                               * SIN(deg2rad * (z.latitude)))) AS distance  "  + 
				 "     FROM store z  "  + 
				 "     JOIN (  "  + 
				 //"           SELECT  42.81  AS latpoint,    -70.81 AS longpoint,  "  + 
				 "           SELECT  50.0 AS radius,        111.045 AS distance_unit,  "  + 
				 "                   57.2957795 AS rad2deg,  "  + 
				 "                   0.0174532925 AS deg2rad  "  + 
				 "             FROM  DUAL  "  + 
				 "       ) p ON 1=1  "  + 
				 "     WHERE z.latitude  "  +
				 "        BETWEEN :paramLatitude  - (p.radius / p.distance_unit)  "  + 
				 "            AND :paramLatitude  + (p.radius / p.distance_unit)  "  + 
				 "       AND z.longitude  "  + 
				 "        BETWEEN :paramLongitude - (p.radius / (p.distance_unit * COS(deg2rad * (:paramLatitude))))  "  + 
				 "            AND :paramLongitude + (p.radius / (p.distance_unit * COS(deg2rad * (:paramLatitude))))  "  +
				 "    )  "  + 
				 "    WHERE distance <= radius  "  + 
				 "    ORDER BY distance  "  + 
				 "   )  "  + 
				 "  WHERE ROWNUM <= 5  " ; 
		
		Query query = entityManager.createNativeQuery(baseQuery, StoreResult.class);
		query.setParameter("paramLatitude", latitude);
		query.setParameter("paramLongitude", longitude);
		
		@SuppressWarnings("unchecked")
		List<StoreResult> storeList = query.getResultList();
		NearestStores stores = new NearestStores();
		stores.setNearestStores(storeList);
		stores.setLatitude(latitude);
		stores.setLongitude(longitude);
		
		LocalTime now = LocalTime.now(ZoneId.of("GMT+2"));
		for(StoreResult result : storeList) {
			//if(result.getTodayOpen())
			result.setStoreStatus(StoreStatus.CLOSING_SOON);
		}
		return stores;
	}
}
