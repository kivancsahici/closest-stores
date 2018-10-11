package com.acme.airports.test.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StoreControllerIntegrationTest {
 
    @Autowired
    private MockMvc mvc;
 
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
    public void findCities()
      throws Exception{
    	mvc.perform(get("/geoapi/v1/cities")
    	.contentType(MediaType.APPLICATION_JSON))
    	.andExpect(status().isOk())
    	.andExpect(jsonPath("$").isArray())
    	.andExpect(jsonPath("$").isNotEmpty())
    	.andExpect(jsonPath("$", hasSize(375)));
    }
}