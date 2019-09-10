package com.jumbo.stores.dao.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.jumbo.stores.dao.entity.StoreResult;
import com.jumbo.stores.dao.repository.IStoreRepository;
import com.jumbo.stores.dao.repository.IStoreRepositoryCustom;

@RunWith(SpringRunner.class)
@DataJpaTest
public class IStoreRepositoryCustomImplTest {
	@Autowired
	private IStoreRepository testee;

	@TestConfiguration
    static class StoreServiceImplTestContextConfiguration {

        @Bean
        public IStoreRepositoryCustom storeService() {
            return new IStoreRepositoryCustomImpl();
        }
    }


	@Test
	public void testFindNearestStores() {
		final List<StoreResult> stores = testee.findNearestStores(51.778461, 4.615551, 30, 4);
		Assert.assertEquals(4, stores.size());
	}

	@Test
	public void testFindDistinctCities() {
		Assert.assertEquals(375, testee.findDistinctCities().size());
	}

	@Test
	public void testFindByCityOrderByStreet() {
		Assert.assertEquals(11, testee.findByCityOrderByStreet("Eindhoven").size());
	}

	@Test
	public void testFindByCityAndStreetOrderByStreet( ) {
		Assert.assertEquals(1, testee.findByCityAndStreetOrderByStreet("Eindhoven", "Geldropseweg").size());
	}
}
