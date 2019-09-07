package com.jumbo.stores.service.impl;

import static org.mockito.Mockito.times;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;

import com.jumbo.stores.dao.entity.Store;
import com.jumbo.stores.dao.repository.IStoreRepository;
import com.jumbo.stores.service.IStoreService;

@RunWith(SpringRunner.class)
public class StoreServiceImplTest {
	private static final String CLOSED_PERMANENTLY = "Gesloten";

	@TestConfiguration
    static class StoreServiceImplTestContextConfiguration {

        @Bean
        @Primary
        public IStoreService employeeService() {
            return new StoreServiceImpl();
        }
    }

    @Autowired
    private StoreServiceImpl storeServiceImpl;

    @MockBean
    private IStoreRepository storeRepository;

    @Test
	public void testSaveAll() throws Exception {
		final Store store1 = new Store.StoreBuilder(1).build();
		final Store store2 = new Store.StoreBuilder(2).withTodayClose("20:00").build();
		List<Store> storeList = Stream.of(store1, store2)
				.collect(Collectors.toList());

		storeServiceImpl.saveAll(storeList);

		Mockito.verify(storeRepository, times(1)).saveAll(storeList);
		Assert.assertTrue(storeList.contains(store1));
		Assert.assertTrue(storeList.contains(store2));
	}

    @Test
	public void testSaveAllClosedPermanently() throws Exception {
		final Store store1 = new Store.StoreBuilder(1).withTodayClose(CLOSED_PERMANENTLY).build();
		final Store store2 = new Store.StoreBuilder(2).build();
		List<Store> storeList = Stream.of(store1, store2)
				.collect(Collectors.toList());

		storeServiceImpl.saveAll(storeList);

		Mockito.verify(storeRepository, times(1)).saveAll(storeList);
		Assert.assertNotEquals(true, storeList.contains(store1));
		Assert.assertEquals(1, storeList.size());
		Assert.assertTrue(storeList.contains(store2));
	}

	@Test
	public void testFindByCity() throws Exception {
		final String city = "Amsterdam";

		Mockito.when(storeRepository.findByCityOrderByStreet(city))
			.thenReturn(Stream.of(new Store.StoreBuilder(118).build())
			.collect(Collectors.toList()));

		List<Store> stores = storeServiceImpl.findByCity(city);

		Mockito.verify(storeRepository, times(1)).findByCityOrderByStreet(city);
		Assert.assertEquals(new Integer(118), stores.get(0).getSapStoreID());
	}

	@Ignore
	public void testFindByCityAndStreet() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

	@Ignore
	public void testFindNearestStores() throws Exception {
		throw new RuntimeException("not yet implemented");
	}

	@Ignore
	public void testFindUniqueCities() throws Exception {
		throw new RuntimeException("not yet implemented");
	}
}