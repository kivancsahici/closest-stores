package com.acme.airports.test.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.acme.airports.dao.entity.Store;
import com.acme.airports.dao.repository.IStoreRepository;
import com.acme.airports.service.IStoreService;
import com.acme.airports.service.impl.StoreServiceImpl;

@RunWith(SpringRunner.class)
public class StoreServiceImplIntegrationTest {
	@TestConfiguration
	static class EmployeeServiceImplTestContextConfiguration {

		@Bean
		public IStoreService employeeService() {
			return new StoreServiceImpl();
		}
	}

	@Autowired
	private IStoreService employeeService;

	@MockBean
	private IStoreRepository employeeRepository;
/*
	@Before
	public void setUp() {
	    Store alex = new Store("alex");
	 
	    Mockito.when(employeeRepository.findByName(alex.getName()))
	      .thenReturn(alex);
	}
	
	@Test
	public void whenValidName_thenEmployeeShouldBeFound() {
	    String name = "alex";
	    Store found = employeeService.getEmployeeByName(name);
	  
	     assertThat(found.getName())
	      .isEqualTo(name);
	 }
*/
}
