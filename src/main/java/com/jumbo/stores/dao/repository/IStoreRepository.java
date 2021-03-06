package com.jumbo.stores.dao.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jumbo.stores.dao.entity.Store;

@Repository
public interface IStoreRepository extends JpaRepository<Store, Integer>, IStoreRepositoryCustom{
	 @Query("SELECT DISTINCT(a.city) FROM Store a ORDER BY a.city")
	 List<String> findDistinctCities();
	 	 	 
	 List<Store> findByCityOrderByStreet(String city);
	 List<Store> findByCityAndStreetOrderByStreet(String city, String street);
}
