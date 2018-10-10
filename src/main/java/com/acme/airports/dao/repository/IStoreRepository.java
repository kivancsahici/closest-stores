package com.acme.airports.dao.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.acme.airports.dao.entity.Store;

@Repository
public interface IStoreRepository extends JpaRepository<Store, Integer>, IStoreRepositoryCustom{
	 @Query("SELECT DISTINCT(a.city) FROM Store a")
	 List<String> findDistinctCities();
	 
	 List<Store> fetchByCity(@Param("paramCity") String city);
}
