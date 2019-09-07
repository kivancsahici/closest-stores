package com.jumbo.stores.test.service;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StoreControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    // This will create a mocked instance of ApplicationStartup and mark it as @Primary
    // in your test context thereby replacing the actual instance of ApplicationStartup
    //@MockBean
    //private StartupApplicationListener applicationStartup;

    @Test
    public void findNearestStores()
      throws Exception {
        mvc.perform(get("/geoapi/v1/stores/by_geocoord.json?latitude=52.040853&longitude=5.315468&radius=25&maxResult=5&showOpen=false")
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.stores").isArray())
          .andExpect(jsonPath("$.stores").isNotEmpty());
    }

    @Test
    public void getCities()
      throws Exception{
    	mvc.perform(get("/geoapi/v1/cities")
    	.contentType(MediaType.APPLICATION_JSON))
    	.andExpect(status().isOk())
    	.andExpect(jsonPath("$").isArray())
    	.andExpect(jsonPath("$").isNotEmpty())
    	.andExpect(jsonPath("$", hasSize(375)));
    }

    @Test
    public void getCity()
      throws Exception{
    	mvc.perform(get("/geoapi/v1/cities/Amsterdam")
    	.contentType(MediaType.APPLICATION_JSON))
    	.andExpect(status().isOk())
    	.andExpect(jsonPath("$").isArray())
    	.andExpect(jsonPath("$").isNotEmpty())
    	.andExpect(jsonPath("$..street", hasSize(14)))
    	.andExpect(jsonPath("$[0].street", is("Baarsjesweg")))
    	.andExpect(jsonPath("$[6].street", is("Oostelijke Handelskade")))
    	;
    }

    @Test
    public void getNonExistingCity()
      throws Exception{
    	mvc.perform(get("/geoapi/v1/cities/London")
    	.contentType(MediaType.APPLICATION_JSON))
    	.andExpect(status().isNotFound())
    	;
    }

    @Test
    public void getStoresByCityandStreet()
      throws Exception{
    	mvc.perform(get("/geoapi/v1/stores/search.json?city=Alkmaar&street=Paardenmarkt")
    	.contentType(MediaType.APPLICATION_JSON))
    	.andExpect(status().isOk())
    	.andExpect(jsonPath("$.stores").isArray())
    	.andExpect(jsonPath("$.stores").isNotEmpty())
    	.andExpect(jsonPath("$.stores", hasSize(1)))
    	.andExpect(jsonPath("$.stores[0].addressName", is("Jumbo Alkmaar Paardenmarkt")))
    	;
    }
}