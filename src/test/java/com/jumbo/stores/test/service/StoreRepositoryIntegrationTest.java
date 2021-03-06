package com.jumbo.stores.test.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.Matchers.*;
/*
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Every.everyItem;
*/

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jumbo.stores.dao.entity.Store;
import com.jumbo.stores.dao.entity.StoreResult;
import com.jumbo.stores.dao.repository.IStoreRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class StoreRepositoryIntegrationTest {
    @Autowired
    private IStoreRepository storeRepo;
 
    @Test
    public void findNearestStores() {    	
    	List<StoreResult> storeResult = storeRepo.findNearestStores(52.075854, 4.234678, 25, 5);    	
        assertThat(storeResult, hasSize(5));
        
        storeResult = storeRepo.findNearestStores(51.497644, 7.393050,  25, 5);
        assertThat(storeResult, hasSize(0));
        
        storeResult = storeRepo.findNearestStores(51.823928, 6.735862,  25, 5);
        assertThat(storeResult, hasSize(4));
    }
    
    @Test
    public void findByCityOrderByStreet() {
    	List<Store> storeList = storeRepo.findByCityOrderByStreet("Alkmaar");
    	assertThat(storeList, hasSize(3));
    	assertThat(storeList.get(2).getStreet(), is("Winkelwaard"));
    }
    
    @Test
    public void findByCityAndStreetOrderByStreet() {
    	List<Store> storeList = storeRepo.findByCityAndStreetOrderByStreet("Eindhoven", "Boutenslaan");
    	assertThat(storeList, hasSize(1));
    	assertThat(storeList.get(0).getSapStoreID(), is(4943));
    }
}
