package com.acme.airports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.acme.airports.model.Store;

public interface StoreRepository extends JpaRepository<Store, Integer>{

}
