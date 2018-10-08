package com.acme.airports.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.acme.airports.dao.entity.Store;

@Repository
public interface IStoreRepository extends JpaRepository<Store, Integer>, IStoreRepositoryCustom{
	
}
