package com.jumbo.stores.dao.repository.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;

import com.jumbo.stores.dao.entity.StoreResult;
import com.jumbo.stores.dao.repository.IStoreRepositoryCustom;

public class IStoreRepositoryCustomImpl implements IStoreRepositoryCustom {
	@PersistenceContext
	private EntityManager em;
			
	@Override
	@Transactional
	public List<StoreResult> findNearestStores(Double latitude, Double longitude, Integer radius, Integer maxResult) {		
		String baseQuery =   "SELECT * FROM (  "  + 
				 "   SELECT sap_storeid, city, address_name,  "  + 
				 //"          latitude, longitude, distance, today_open, today_close, location_type  "  + 
				 "          latitude, longitude, today_open, today_close, location_type  "  +
				 "     FROM (  "  + 
				 "    SELECT z.sap_storeid, z.city, z.address_name,  "  + 
				 "           z.latitude, z.longitude, z.today_open, z.today_close, z.location_type, "  + 
		//		 "           p.radius,  "  + 
				 "           p.distance_unit  "  + 
				 "               * rad2deg * (ACOS(COS(deg2rad * (:paramLatitude))  "  + 
				 "                               * COS(deg2rad * (z.latitude))  "  + 
				 "                               * COS(deg2rad * (:paramLongitude - z.longitude))  "  + 
				 "                               + SIN(deg2rad * (:paramLatitude))  "  + 
				 "                               * SIN(deg2rad * (z.latitude)))) AS distance  "  + 
				 "     FROM store z  "  + 
				 "     JOIN (  "  + 
				 //"           SELECT  42.81  AS latpoint,    -70.81 AS longpoint,  "  + 
				 //"           SELECT  50.0 AS radius,        111.045 AS distance_unit,  "  + 
				 //"           SELECT  25.0 AS radius,        111.045 AS distance_unit,  "  +
				 "           SELECT     111.045 AS distance_unit,  "  +
				 "                   57.2957795 AS rad2deg,  "  + 
				 "                   0.0174532925 AS deg2rad  "  + 
				 "             FROM  DUAL  "  + 
				 "       ) p ON 1=1  "  + 
				 "     WHERE z.latitude  "  +
				 "        BETWEEN :paramLatitude  - (:paramRadius / p.distance_unit)  "  + 
				 "            AND :paramLatitude  + (:paramRadius / p.distance_unit)  "  + 
				 "       AND z.longitude  "  + 
				 "        BETWEEN :paramLongitude - (:paramRadius / (p.distance_unit * COS(deg2rad * (:paramLatitude))))  "  + 
				 "            AND :paramLongitude + (:paramRadius / (p.distance_unit * COS(deg2rad * (:paramLatitude))))  "  +
				 "    )  "  + 
				 "    WHERE distance <= :paramRadius  "  + 
				 "    ORDER BY distance  "  + 
				 "   )  "  + 
				 "  WHERE ROWNUM <= :paramMaxResult  " ; 
		
		Query query = em.createNativeQuery(baseQuery, StoreResult.class);
		query.setParameter("paramLatitude", latitude);
		query.setParameter("paramLongitude", longitude);
		query.setParameter("paramRadius", radius);
		query.setParameter("paramMaxResult", maxResult);
		
		@SuppressWarnings("unchecked")
		List<StoreResult> storeList = query.getResultList();
		return storeList;
	}
}
