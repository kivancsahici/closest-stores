package com.acme.airports.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.acme.airports.dao.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Integer>{

}
