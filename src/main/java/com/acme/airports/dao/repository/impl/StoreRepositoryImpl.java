package com.acme.airports.dao.repository.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.acme.airports.dao.entity.Store;
import com.acme.airports.dao.entity.StoreResult;
import com.acme.airports.dao.repository.IStoreRepository;
import com.acme.airports.dao.repository.IStoreRepositoryCustom;

@Repository
public class StoreRepositoryImpl implements IStoreRepositoryCustom {
	@PersistenceContext
	private EntityManager em;
	
	@Override
	@Transactional
	public List<StoreResult> foo(Double latitude, Double longitude) {
		String baseQuery =   "SELECT * FROM (  "  + 
				 "   SELECT sap_storeid, city, address_name,  "  + 
				 "          latitude, longitude, distance, today_open, today_close, location_type  "  + 
				 "     FROM (  "  + 
				 "    SELECT z.sap_storeid, z.city, z.address_name,  "  + 
				 "           z.latitude, z.longitude, z.today_open, z.today_close, z.location_type, "  + 
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
		
		Query query = em.createNativeQuery(baseQuery, StoreResult.class);
		query.setParameter("paramLatitude", latitude);
		query.setParameter("paramLongitude", longitude);
		
		@SuppressWarnings("unchecked")
		List<StoreResult> storeList = query.getResultList();
		return storeList;
	}
}
