package com.jumbo.stores.dao.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.jumbo.stores.dao.entity.StoreResult;

@RunWith(SpringRunner.class)
@DataJpaTest
public class IStoreRepositoryCustomImplTest {

	@Autowired
    private TestEntityManager entityManager;

	@Autowired
	private IStoreRepositoryCustomImpl testee;

	@Test
	public void testFindNearestStores() {
		final List<StoreResult> stores = testee.findNearestStores(51.778461, 4.615551, 30, 4);
		Assert.assertEquals(4, stores.size());
	}
}
