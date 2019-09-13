package com.jumbo.stores.service.impl;

import static org.mockito.Mockito.times;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
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
import com.jumbo.stores.dao.entity.StoreResult;
import com.jumbo.stores.dao.entity.StoreStatus;
import com.jumbo.stores.dao.repository.IStoreRepository;
import com.jumbo.stores.service.IStoreService;
import com.jumbo.stores.service.dto.NearestStores;

@RunWith(SpringRunner.class)
public class StoreServiceImplTest {
	private static final String CLOSED_PERMANENTLY = "Gesloten";

	private static final ZoneId zoneId = ZoneId.of("GMT+2");

	@TestConfiguration
    static class StoreServiceImplTestContextConfiguration {
        @Bean
        @Primary
        public IStoreService storeService() {
            return new StoreServiceImpl();
        }
    }

    @Autowired
    private StoreServiceImpl storeServiceImpl;

    @MockBean
    private IStoreRepository storeRepository;

    @MockBean
    private Clock clock;

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

	@Test
	public void testFindByCityAndStreet() throws Exception {
		final Store store1 = new Store.StoreBuilder(1).withTodayOpen("09:00").withTodayClose("11:00").build();
		final Store store2 = new Store.StoreBuilder(2).withTodayOpen("09:00").withTodayClose("20:00").build();
		final Store store3 = new Store.StoreBuilder(3).withTodayOpen("09:00").withTodayClose("13:00").build();
		final Store store4 = new Store.StoreBuilder(4).withTodayOpen("15:00").withTodayClose("22:00").build();

		Mockito.when(clock.getZone()).thenReturn(zoneId);
        Mockito.when(clock.instant()).thenReturn(LocalDateTime.of(LocalDate.of(2019, 9, 13), LocalTime.of(12, 30)).atZone(zoneId).toInstant());
		Mockito.when(storeRepository.findByCityAndStreetOrderByStreet(Mockito.anyString(), Mockito.anyString())).
			thenReturn(Stream.of(store1, store2, store3, store4).collect(Collectors.toList()));

		final NearestStores nearestStores = storeServiceImpl.findByCityAndStreet("Eindhoven", "Geldropseweg");
		final List<StoreResult> actual = nearestStores.getStores();
		Assert.assertEquals(4, actual.size());
		Assert.assertEquals(StoreStatus.CLOSED, actual.get(0).getStoreStatus());
		Assert.assertEquals(StoreStatus.OPEN, actual.get(1).getStoreStatus());
		Assert.assertEquals(StoreStatus.CLOSING_SOON, actual.get(2).getStoreStatus());
		Assert.assertEquals(StoreStatus.CLOSED, actual.get(3).getStoreStatus());

	}

	@Test
	public void testFindNearestStoresOpen() throws Exception {
		final StoreResult store1 = new StoreResult.StoreResultBuilder(1).withTodayOpen("09:00").withTodayClose("11:00").build();
		final StoreResult store2 = new StoreResult.StoreResultBuilder(2).withTodayOpen("09:00").withTodayClose("20:00").build();
		final StoreResult store3 = new StoreResult.StoreResultBuilder(3).withTodayOpen("09:00").withTodayClose("13:00").build();
		final StoreResult store4 = new StoreResult.StoreResultBuilder(4).withTodayOpen("15:00").withTodayClose("22:00").build();

		Mockito.when(clock.getZone()).thenReturn(zoneId);
        Mockito.when(clock.instant()).thenReturn(LocalDateTime.of(LocalDate.of(2019, 9, 13), LocalTime.of(12, 30)).atZone(zoneId).toInstant());
        Mockito.when(storeRepository.findNearestStores(Mockito.any(Double.class), Mockito.any(Double.class), Mockito.any(Integer.class), Mockito.any(Integer.class))).
			thenReturn(Stream.of(store1, store2, store3, store4).collect(Collectors.toList()));

		final NearestStores nearestStores = storeServiceImpl.findNearestStores(51.778461, 4.615551, 30, 4, true);
		final List<StoreResult> actual = nearestStores.getStores();
		Assert.assertEquals(2, actual.size());
	}

	@Test
	public void testFindNearestStoresAll() throws Exception {
		final StoreResult store1 = new StoreResult.StoreResultBuilder(1).withTodayOpen("09:00").withTodayClose("11:00").build();
		final StoreResult store2 = new StoreResult.StoreResultBuilder(2).withTodayOpen("09:00").withTodayClose("20:00").build();
		final StoreResult store3 = new StoreResult.StoreResultBuilder(3).withTodayOpen("09:00").withTodayClose("13:00").build();
		final StoreResult store4 = new StoreResult.StoreResultBuilder(4).withTodayOpen("15:00").withTodayClose("22:00").build();

		Mockito.when(clock.getZone()).thenReturn(zoneId);
        Mockito.when(clock.instant()).thenReturn(LocalDateTime.of(LocalDate.of(2019, 9, 13), LocalTime.of(12, 30)).atZone(zoneId).toInstant());
		Mockito.when(storeRepository.findNearestStores(Mockito.any(Double.class), Mockito.any(Double.class), Mockito.any(Integer.class), Mockito.any(Integer.class))).
			thenReturn(Stream.of(store1, store2, store3, store4).collect(Collectors.toList()));

		final NearestStores nearestStores = storeServiceImpl.findNearestStores(51.778461, 4.615551, 30, 4, false);
		final List<StoreResult> actual = nearestStores.getStores();
		Assert.assertEquals(4, actual.size());
	}


	@Test
	public void testFindUniqueCities() throws Exception {
		storeServiceImpl.findUniqueCities();
		Mockito.verify(storeRepository, times(1)).findDistinctCities();
	}
}