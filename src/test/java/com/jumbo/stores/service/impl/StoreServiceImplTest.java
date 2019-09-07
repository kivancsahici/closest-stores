package com.jumbo.stores.service.impl;

import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.jumbo.stores.dao.entity.Store;
import com.jumbo.stores.dao.repository.IStoreRepository;
@RunWith(MockitoJUnitRunner.class)
public class StoreServiceImplTest {
	private static final String CLOSED_PERMANENTLY = "Gesloten";

	@Spy
	private IStoreRepository storeRepository;
	@InjectMocks
	private StoreServiceImpl storeServiceImpl;

	@Test
	public void testSaveAll() throws Exception {
		final Store store = new Store.StoreBuilder(2).build();
		List<Store> storeList = new ArrayList<Store>();
		storeList.add(store);

		storeServiceImpl.saveAll(storeList);

		Mockito.verify(storeRepository, times(1)).saveAll(storeList);
		Assert.assertTrue(storeList.contains(store));
	}

	@Test
	public void testSaveAllClosedPermanently() throws Exception {
		List<Store> storeList = new ArrayList<Store>();
		final Store store1 = new Store.StoreBuilder(1).withTodayClose(CLOSED_PERMANENTLY).build();
		final Store store2 = new Store.StoreBuilder(2).build();
		storeList.add(store1);
		storeList.add(store2);

		storeServiceImpl.saveAll(storeList);

		Mockito.verify(storeRepository, times(1)).saveAll(storeList);
		Assert.assertNotEquals(true, storeList.contains(store1));
		Assert.assertEquals(1, storeList.size());
		Assert.assertTrue(storeList.contains(store2));
	}

	@Ignore
	public void testFindByCity() throws Exception {
		throw new RuntimeException("not yet implemented");
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
